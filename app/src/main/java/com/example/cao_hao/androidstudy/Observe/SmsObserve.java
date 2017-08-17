package com.example.cao_hao.androidstudy.Observe;

import android.database.ContentObserver;
import android.os.Handler;

import com.example.cao_hao.androidstudy.utils.LogUtils;

/**
 * Created by cao-hao on 17-8-7.
 */

public class SmsObserve extends ContentObserver {
    private Handler mHandler;
    public SmsObserve(Handler handler) {
        super(handler);
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        /*super.onChange(selfChange);*/ //不能用父类方法
        LogUtils.d("监听到短信来了");
        mHandler.sendEmptyMessage(0);
    }
}
