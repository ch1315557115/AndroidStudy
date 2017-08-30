package com.example.cao_hao.androidstudy.Activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.cao_hao.androidstudy.Adapter.MyCursorAdapter;
import com.example.cao_hao.androidstudy.Adapter.MyQueryHandler;
import com.example.cao_hao.androidstudy.R;


public class CursorAdapterActivity extends AppCompatActivity {
    private static  final String URI_SMS ="content://sms";//短信的Uri
    private ListView mLvCursorAdapter;
    private CursorAdapter mCursorAdapter;
    private ContentResolver mResolver;//内容解析者  获取ContentProvider提供的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursor_adapter);
        mLvCursorAdapter = (ListView) findViewById(R.id.lv_crusor_adapter);



        mResolver = getContentResolver();

        /**
         * 参数1：Uri对象
         * 参数2：查询数据表中字段数组
         * 参数3: 查询的条件
         * 参数4：查询条件的占位符
         * 参数5： 排序*/

        Cursor cursor =mResolver.query(Uri.parse(URI_SMS), null, null, null, null);
        mCursorAdapter = new MyCursorAdapter(this,cursor);
        mLvCursorAdapter.setAdapter(mCursorAdapter);
/*

        */
/**
         * 利用AsyncQueryHandler +CursorAdapter 能实现短信数据增加、删除时候UI同步更新,studio模拟手机向模拟器发送短信，模拟器收到短信后UI界面能同步更新
         */
  mCursorAdapter = new MyCursorAdapter(this,null);
        MyQueryHandler queryHandler = new MyQueryHandler(getContentResolver());
        //执行查询语句，可以将adapter作为第二个参数传入
        queryHandler.startQuery(0, mCursorAdapter, Uri.parse(URI_SMS), null, null, null, null);
        mLvCursorAdapter.setAdapter(mCursorAdapter);



    }
}
