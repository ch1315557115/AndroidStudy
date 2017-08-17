package com.example.cao_hao.androidstudy.Activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;

public class HandlerTestAcitvity extends AppCompatActivity {

    private TextView mText = null;
    private Button mStart = null;
    private Button mEend = null;

    Handler handler = new Handler();

    Runnable update_thread = new Runnable() {
        public void run() {
            //线程每次执行时输出"UpdateThread..."文字,且自动换行
            mText.append("\nUpdateThread...");
            //延时1s后又将线程加入到线程队列中
            handler.postDelayed(update_thread, 1000);

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test_acitvity);

        mText = (TextView) findViewById(R.id.text_view);
        mStart = (Button) findViewById(R.id.start);
        mStart.setOnClickListener(new StartClickListener());
        mEend = (Button) findViewById(R.id.end);
        mEend.setOnClickListener(new EndClickListener());

    }

    private class StartClickListener implements View.OnClickListener {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //将线程接口立刻送到线程队列中
            handler.post(update_thread);
        }
    }

    private class EndClickListener implements View.OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            //将接口从线程队列中移除
            handler.removeCallbacks(update_thread);
        }

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    }*/
}
