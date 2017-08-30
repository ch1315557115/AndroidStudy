package com.example.cao_hao.androidstudy.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.aidl.IRemoteService;
import com.example.cao_hao.androidstudy.utils.LogUtils;

/**
 * 在secondDayStudy 中的 DDService中有服务端
 *
 * 客户端实现步骤：

    首先建立一个项目，把服务端的aidl 包和类一起拷贝到客户端项目中（两个包名必须一致，经过测试包名不一样的话会报错误，）。，因为客户端要和服务端通信，必须要使用同一个aidl。
 */
public class AIDLActivity extends AppCompatActivity {
    private IRemoteService remoteService;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        mButton = (Button) findViewById(R.id.bt_start_aidl);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick(v);
            }
        });
    }
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IRemoteService.Stub.asInterface(service);
            try {
                int pid = remoteService.getPid();
                int currentPid = android.os.Process.myPid();
                LogUtils.d("currentPID: " + currentPid +"  remotePID: " + pid);
//                remoteService.basicTypes(12, 1223, true, 12.2f, 12.3, "我们的爱，我明白");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            LogUtils.d("bind success! " + remoteService.toString());
        }
    };

    /**
     * 监听按钮点击
     * @param view
     */
    public void buttonClick(View view) {
        System.out.println("begin bindService");
        Intent intent = new Intent("com.IRemoteService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
