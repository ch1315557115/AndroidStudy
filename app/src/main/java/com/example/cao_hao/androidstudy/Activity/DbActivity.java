package com.example.cao_hao.androidstudy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.db.DBHelper;

public class DbActivity extends AppCompatActivity implements View.OnClickListener {
    private DBHelper mDbHelper;

    private EditText mName, mNumber, mDesc;
    private Button mInsert, mLook;

    //定义一个RadioGroup
    private RadioGroup mRadio;
    private String mNameStr, mNumberStr, mDescStr;
    private String mSexStr = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        mDbHelper = new DBHelper(this);
        //根据id 获取到相对应的控件
        mName = (EditText) findViewById(R.id.editText1);
        mNumber = (EditText) findViewById(R.id.editText2);
        mDesc = (EditText) findViewById(R.id.editText3);
        mInsert = (Button) findViewById(R.id.bt_insert);
        mLook = (Button) findViewById(R.id.bt_look);
        mRadio = (RadioGroup) findViewById(R.id.radioGroup1);

        //设置按钮监听
        mInsert.setOnClickListener(this);
        mLook.setOnClickListener(this);

        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.rb_boy) {
                    TextView r = (TextView) radioGroup.findViewById(checkedId);
                    mSexStr = r.getText().toString();
                }
                if (checkedId == R.id.rb_girl) {
                    RadioButton r = (RadioButton) radioGroup.findViewById(checkedId);
                    mSexStr = r.getText().toString();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_insert:
                insetData();
                break;
            case R.id.bt_look:
                Intent intent = new Intent();
               /* intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                intent.setClass(this, DbResultActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 插入数据
     */
    private void insetData() {
        if (mName.getText().toString().length() != 0) {
            mNameStr = mName.getText().toString();
        } else {
            Toast.makeText(getApplication(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mNumber.getText().toString().length() != 0) {
            mNumberStr = mNumber.getText().toString();
        } else {
            Toast.makeText(getApplication(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mDesc.getText().toString().length() != 0) {
            mDescStr = mDesc.getText().toString();
        } else {
            Toast.makeText(getApplication(), "备注不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //实例化一个ContentValues， ContentValues是以键值对的形式，键是数据库的列名，值是要插入的值
        ContentValues values = new ContentValues();
        values.put("name", mNameStr);
        values.put("sex", mSexStr);
        values.put("number", mNumberStr);
        values.put("desc", mDescStr);

        //调用insert插入数据库
        mDbHelper.insert(values);
        resetData();
    }

    private void resetData() {
        mName.setText("");
        mNumber.setText("");
        mDesc.setText("");
    }
}
