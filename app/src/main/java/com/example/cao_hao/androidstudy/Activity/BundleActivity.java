package com.example.cao_hao.androidstudy.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.View.RectView;
import com.example.cao_hao.androidstudy.bean.PersonPar;
import com.example.cao_hao.androidstudy.bean.PersonSer;

/**
 * Android中为了能够在Activity之间传递值，需要只用Intent中的put函数。如果需要传递对象可以用
 * putExtras(Bundle extras)

 其中bundle.putParcelable可以实现传递对象，但是这个对象的类必须实现Parcelable接口才能够使用。
 */

public class BundleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SER_KEY = "aaa";
    private static final String SER_TYPE = "aa";
    private static final String PAR_KEY = "bbb";
    private static final String PAR_TYPE = "bb";

    private Button mBtSeri;
    private Button mBtParc;

    Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle);

        mBtSeri = (Button) findViewById(R.id.bt_serializable);
        mBtParc = (Button) findViewById(R.id.bt_parcelable);
        intent = new Intent(BundleActivity.this, BundleDesActivity.class);
        bundle = new Bundle();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_serializable:
                // Serializable传递对象
                PersonSer personSer = new PersonSer();
                personSer.setName("zuolong");
                personSer.setAge(26);
                bundle.putSerializable(SER_KEY, personSer);
                intent.putExtra("type", SER_TYPE);
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case R.id.bt_parcelable:

                PersonPar personPar = new PersonPar();// Parcelable传递对象
                personPar.setName("LiSi");
                personPar.setAge(24);
                // Bundle有putParcelableArray和putParcelableArrayList方法，也就可以传递数组和列表
                bundle.putParcelable(PAR_KEY, personPar);
                intent.putExtra("type", PAR_TYPE);

              // Intent也有putParcelableArrayListExtra方法，可以传递实现Parcelable接口的对象列表
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }
}
