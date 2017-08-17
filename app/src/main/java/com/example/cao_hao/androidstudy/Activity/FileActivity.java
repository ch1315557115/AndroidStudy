package com.example.cao_hao.androidstudy.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.utils.FileUtils;

public class FileActivity extends AppCompatActivity {
    private final String filePath = "/data/data/com.example.cao_hao.thirddaystudy/log.txt";

    private Button mBtWrite;
    private Button mBtRead;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        mBtWrite = (Button) findViewById(R.id.bt_write);
        mBtRead = (Button) findViewById(R.id.bt_read);
        mEditText = (EditText) findViewById(R.id.et_file);

        mBtWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = mEditText.getText().toString();
                if (null != st)
                FileUtils.writeData(st,filePath);
                mEditText.setText("");
            }
        });

        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = FileUtils.readByChars(filePath);
                Toast.makeText(FileActivity.this,st,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
