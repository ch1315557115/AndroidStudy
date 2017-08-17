package com.example.cao_hao.androidstudy.Activity;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.cao_hao.androidstudy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {
    private Cursor mCursor = null;  // 既然要查询，查询返回的就是结果
    private ListView mContactsList = null;  // 定义ListView组件
    private List<Map<String, Object>> mContacts = null;
    private SimpleAdapter mSimpleAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mContactsList = (ListView) super.findViewById(R.id.contactsList);
        //这里获取通讯录联系人URI里的内容
        mCursor = super.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null); // 查询

      /*  // 将结果集交给容器管理
        super.startManagingCursor(this.mCursor);*/
        mContacts = new ArrayList<Map<String, Object>>();

        // 实例化List集合
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
                .moveToNext()) {
            Map<String, Object> contact = new HashMap<String, Object>();
            contact.put("_id", mCursor.getInt(mCursor
                    .getColumnIndex(ContactsContract.Contacts._ID)));
            contact.put("name", mCursor.getString(mCursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            mContacts.add(contact);
        }
        mSimpleAdapter = new SimpleAdapter(this, mContacts,
                R.layout.activity_contact_item, new String[]{"_id", "name"}, new int[]{
                R.id._id, R.id.name});

        mContactsList.setAdapter(mSimpleAdapter);
    }
}
