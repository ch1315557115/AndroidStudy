package com.example.cao_hao.androidstudy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.bean.PersonPar;
import com.example.cao_hao.androidstudy.bean.PersonSer;
import com.example.cao_hao.androidstudy.utils.LogUtils;

public class BundleDesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_des);

        StringBuffer sb = new StringBuffer();
        String type = getIntent().getStringExtra("type");
/*// 根据type判断是哪种类型
        if (type.equals(BundleActivity.SER_TYPE)) {
            // 获取Serializable对象
            PersonSer personSer = (PersonSer) getIntent().getSerializableExtra(
                    BundleActivity.SER_KEY);
            sb.append("----From Serializable----").append("\n");
            sb.append("Name:").append(personSer.getName()).append("\n");
            sb.append("Age:").append(personSer.getAge()).append("\n");
        } else if (type.equals(BundleActivity.PAR_TYPE)) {
            // 获取Parcelable对象
            PersonPar personPar = (PersonPar) getIntent().getParcelableExtra(
                    BundleActivity.PAR_KEY);
            sb.append("----From Parcelable----").append("\n");
            sb.append("Name:").append(personPar.getName()).append("\n");
            sb.append("Age:").append(personPar.getAge()).append("\n");
            LogUtils.d(sb.toString());

        }*/
    }
}
