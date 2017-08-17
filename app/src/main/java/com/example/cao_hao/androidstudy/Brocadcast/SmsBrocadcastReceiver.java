package com.example.cao_hao.androidstudy.Brocadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.cao_hao.androidstudy.utils.LogUtils;

/**
 * Created by cao-hao on 17-8-7.
 */

public class SmsBrocadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("收到短信啦～～～～");

    }
}
