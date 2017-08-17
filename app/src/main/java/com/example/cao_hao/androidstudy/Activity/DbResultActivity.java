package com.example.cao_hao.androidstudy.Activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.cao_hao.androidstudy.R;

import java.util.ArrayList;
import java.util.List;

import com.example.cao_hao.androidstudy.Adapter.DbAdapter;
import com.example.cao_hao.androidstudy.db.DBHelper;
import com.example.cao_hao.androidstudy.javabean.Person;

public class DbResultActivity extends AppCompatActivity {
    private ListView mListView;
    private DbAdapter mAdapter;
    DBHelper mDbHelper;
    List<Person> mBookList = new ArrayList<Person>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_result);

        mListView = (ListView) findViewById(R.id.listView1);

        mBookList = queryData();
        mAdapter = new DbAdapter(this,mBookList);

        mListView.setAdapter(mAdapter);
    }


    /**
     * 查询数据库，将每一行的数据封装成一个person 对象，然后将对象添加到List中
     * @return
     */
    private List<Person> queryData(){
        List<Person> list = new ArrayList<Person>();
        mDbHelper = new DBHelper(this);

        //调用query()获取Cursor
        Cursor c = mDbHelper.query();
        while (c.moveToNext()){
            int _id = c.getInt(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            String sex = c.getString(c.getColumnIndex("sex"));
            String number = c.getString(c.getColumnIndex("number"));
            String desc = c.getString(c.getColumnIndex("desc"));

            //用一个Person对象来封装查询出来的数据
            Person p = new Person();
            p.set_id(_id);
            p.setName(name);
            p.setSex(sex);
            p.setNumber(number);
            p.setDesc(desc);

            list.add(p);
        }
        return list;
    }
}
