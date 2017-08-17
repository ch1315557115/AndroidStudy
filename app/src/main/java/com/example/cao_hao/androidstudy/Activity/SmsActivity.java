package com.example.cao_hao.androidstudy.Activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.cao_hao.androidstudy.R;

import java.util.ArrayList;
import java.util.List;

import com.example.cao_hao.androidstudy.Adapter.SmsAdapter;
import com.example.cao_hao.androidstudy.Brocadcast.SmsBrocadcastReceiver;
import com.example.cao_hao.androidstudy.Observe.SmsObserve;
import com.example.cao_hao.androidstudy.javabean.SmsInfo;
import com.example.cao_hao.androidstudy.utils.LogUtils;

public class SmsActivity extends AppCompatActivity {
    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";
    /**
     * 收件箱短信
     */
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    /**
     * 发件箱短信
     */
    public static final String SMS_URI_SEND = "content://sms/sent";
    /**
     * 草稿箱短信
     */
    public static final String SMS_URI_DRAFT = "content://sms/draft";
    private Uri SMS_INBOX = Uri.parse("content://sms/");

    private Button mStartSms;
    private Button mStopSms;
    private Button mReadSmsDb;
    private BroadcastReceiver mSmsReceiver = new SmsBrocadcastReceiver();

    private RecyclerView mRecylerView;
    private SmsAdapter mAdapter;

    private ArrayList<SmsInfo> info  = new ArrayList<SmsInfo>();

    private SmsObserve mSmsObserve;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != info){
                info.clear();
               info = (ArrayList<SmsInfo>) getSmsInfo();
               mAdapter.refresh(info);
                LogUtils.d("刷新数据啦");
            }

            /*switch (msg.what){
                case :

                    break;
            }*/
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        mStartSms = (Button) findViewById(R.id.bt_start_sms);
        mStopSms = (Button) findViewById(R.id.bt_stop_sms);
        mReadSmsDb = (Button) findViewById(R.id.bt_read_sms);
        mRecylerView = (RecyclerView) findViewById(R.id.ry_sms);
        mAdapter = new SmsAdapter(this,getSmsInfo());



        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecylerView.setLayoutManager(linearLayoutManager);

        //添加装饰类
        mRecylerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));
        mRecylerView.setAdapter(mAdapter);

        mSmsObserve = new SmsObserve(mHandler);
        getContentResolver().registerContentObserver(Uri.parse("content://sms"),true,mSmsObserve);
        //地址要用"content://sms"  不能用"content://sms/inbox"


        mStartSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.com.example.cao_hao.androidstudy.provider.Telephony.SMS_RECEIVED");
                registerReceiver(mSmsReceiver,filter);
                LogUtils.d("注册广播了");
            }
        });

        mStopSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mSmsReceiver){
                    unregisterReceiver(mSmsReceiver);
                }
                LogUtils.d("取消注册广播了");
            }
        });

        mReadSmsDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSmsInfo();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
    }

    /**
     * 获取系统收件箱短信
     */

    public List<SmsInfo> getSmsInfo() {
        ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type"};
        Cursor cusor = resolver.query(SMS_INBOX, projection, null, null,
                "date desc");
        int nameColumn = cusor.getColumnIndex("person");
        int dateColumn = cusor.getColumnIndex("date");
        int phoneNumberColumn = cusor.getColumnIndex("address");
        int smsbodyColumn = cusor.getColumnIndex("body");
        int typeColumn = cusor.getColumnIndex("type");

        if (cusor != null) {
            while (cusor.moveToNext()) {
                SmsInfo smsinfo = new SmsInfo();
                smsinfo.setName(cusor.getString(nameColumn));
                smsinfo.setDate(cusor.getString(dateColumn));
                smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn));
                smsinfo.setSmsbody(cusor.getString(smsbodyColumn));
                LogUtils.d("短信内容： "+ cusor.getString(smsbodyColumn));
                smsinfo.setType(cusor.getString(typeColumn));
                info.add(smsinfo);
            }
            cusor.close();
        }
        return info;
    }
}
