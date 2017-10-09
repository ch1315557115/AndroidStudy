package com.example.cao_hao.androidstudy.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.View.Matrix.MatrixView;
import com.example.cao_hao.androidstudy.utils.LogUtils;

public class MatrixActivity extends Activity implements View.OnClickListener {
    private MatrixView mIvMatrixView, iv2;
    private Button mBtTranslate,mBtScale,mBtTotate,mBtReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_matrix);
        mIvMatrixView = (MatrixView)findViewById(R.id.iv1);
        mBtTranslate = (Button) findViewById(R.id.bt_translate);
        mBtScale = (Button) findViewById(R.id.bt_scale);
        mBtTotate = (Button) findViewById(R.id.bt_rotate);
        mBtReset = (Button) findViewById(R.id.bt_reset);

        mBtTranslate.setOnClickListener(this);
        mBtScale.setOnClickListener(this);
        mBtTotate.setOnClickListener(this);
        mBtReset.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_translate:
                LogUtils.d("平移了");
                mIvMatrixView.setTranslate(30,30);
                break;
            case R.id.bt_scale:
                LogUtils.d("平移了");
                mIvMatrixView.setScale(1.1f,1.1f);
                break;
            case R.id.bt_rotate:
                LogUtils.d("平移了");
                mIvMatrixView.setRotate(30);
                break;
            case R.id.bt_reset:
                LogUtils.d("平移了");
                mIvMatrixView.setReset();
                break;
        }
    }
}
