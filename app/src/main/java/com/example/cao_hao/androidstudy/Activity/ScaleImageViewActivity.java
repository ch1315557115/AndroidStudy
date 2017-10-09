package com.example.cao_hao.androidstudy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.View.Matrix.MyScaleImage;

public class ScaleImageViewActivity extends AppCompatActivity {
    private MyScaleImage mScaleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_image_view);
        mScaleImage = (MyScaleImage) findViewById(R.id.iv_myscaleimage);
        mScaleImage.setTranslate(75,75);
    }
}
