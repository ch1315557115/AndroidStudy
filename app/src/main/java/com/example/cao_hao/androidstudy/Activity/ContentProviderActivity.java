package com.example.cao_hao.androidstudy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.Adapter.ContentProviderAdapter;
import com.example.cao_hao.androidstudy.db.ContentProviderDBHelper;
import com.example.cao_hao.androidstudy.Manager.ContentProviderDBManager;
import com.example.cao_hao.androidstudy.utils.SharedPreferencesUtils;

public class ContentProviderActivity extends AppCompatActivity {
    private ListView mList;
    private ContentProviderDBHelper mDbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

        mList = (ListView) findViewById(R.id.lv);

        ContentProviderDBManager manager = ContentProviderDBManager.getInstance();
        SharedPreferencesUtils.saveData("name","zhangsan");


    }


    @Override
    protected void onResume() {
        super.onResume();
        mList.setAdapter(new ContentProviderAdapter(this, ContentProviderDBManager.getInstance().getData()));
    }
}
