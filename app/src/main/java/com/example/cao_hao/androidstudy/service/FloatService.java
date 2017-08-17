package com.example.cao_hao.androidstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.Manager.FloatViewManager;

/**
 *Created by cao-hao on 17-8-10.
 */
public class FloatService extends Service {

    private FloatViewManager mFloatViewManager;
    private View mFloatView;

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatViewManager = new FloatViewManager(this);
        mFloatView = LayoutInflater.from(this).inflate(R.layout.float_view, null);
        mFloatViewManager.showAtCenter(mFloatView);
    }

    @Override
    public void onDestroy() {
        mFloatViewManager.remove(mFloatView);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
