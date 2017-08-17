package com.example.cao_hao.androidstudy.Adapter;

import android.content.Context;
import android.database.Cursor;

import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Android Cursor自动更新的实现和原理
 * http://blog.csdn.net/dq1005/article/details/51317397

 在Android日常开发中，时常会请求数据到Cursor，然后再通过Cursor获取数据。像SQLiteDatabase和ContentProvider都使用了Cursor。
 在这些应用中，往往希望当数据发生改变时，Cursor也会自动的更新数据。这篇文章，我就会向你阐述如何通过android自身的API实现Cursor的自
 动更新。另外我还将向你阐述这背后的原理。通过这些原理你可以举一反三的实现更为广泛的自动跟新。
 * Created by cao-hao on 17-8-21.
 */

public class MyCursorAdapter extends CursorAdapter {
    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_cursor_adapter_item,null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        TextView tvType = (TextView) view.findViewById(R.id.tv_type);
        //短信的号码
        String number = cursor.getString(cursor.getColumnIndex("address"));
        tvNumber.setText(number);
        //发送短信的时间
        int date = cursor.getInt(cursor.getColumnIndex("date"));
        tvTime.setText(GetDate(date));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        String str =null ;
        if(1==type){
            //接受
            str ="接受";
        }else{
            //发送
            str ="发送";
        }
        tvType.setText(str);

    }


    /**
     * int 类型时间 转换成string
     *
     * @param date
     * @return
     */
    public String GetDate(int date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(date));
    }
}
