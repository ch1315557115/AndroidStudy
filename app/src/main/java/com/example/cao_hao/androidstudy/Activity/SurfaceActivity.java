package com.example.cao_hao.androidstudy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cao_hao.androidstudy.View.MySurfaceView;

public class SurfaceActivity extends AppCompatActivity {
    private MySurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new MySurfaceView(this);

        setContentView(mSurfaceView);
    }
}
