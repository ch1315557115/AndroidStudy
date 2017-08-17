package com.example.cao_hao.androidstudy.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.cao_hao.androidstudy.db.ContentProviderDBHelper;
import com.example.cao_hao.androidstudy.javabean.User;
import com.example.cao_hao.androidstudy.utils.LogUtils;
import com.example.cao_hao.androidstudy.utils.MyApplication;

/**
 * Created by cao-hao on 17-8-9.
 */

public class ContentProviderDBManager {
    private ContentProviderDBHelper mHelper;
    private static ContentProviderDBManager mInstance;
    private SQLiteDatabase mDatabase;

    /**上下文如果传这个会报错
     * at android.database.sqlite.SQLiteOpenHelper.getDatabaseLocked(SQLiteOpenHelper.java:224)
     at android.database.sqlite.SQLiteOpenHelper.getWritableDatabase(SQLiteOpenHelper.java:164)
     */
    private ContentProviderDBManager() {
        Context context = MyApplication.getAppContext();
        mHelper = new ContentProviderDBHelper(context);
        mDatabase = mHelper.getWritableDatabase();
        LogUtils.d("");
    }

    public static ContentProviderDBManager getInstance() {
        if (null == mInstance) {
            synchronized (ContentProviderDBManager.class) {
                if (null == mInstance) {
                    mInstance = new ContentProviderDBManager();
                }
            }
            mInstance = new ContentProviderDBManager();
        }
        return mInstance;
    }

    public void init(Context context){
        mHelper = new ContentProviderDBHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public List<User> getData() {
        List<User> list = new ArrayList<User>();
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + mHelper.USERTABLE_NAME, null);
        User u = null;
        while (c.moveToNext()) {
            String username = c.getString(c.getColumnIndex("USERNAME"));
            String nickname = c.getString(c.getColumnIndex("NICKNAME"));
            String gender = c.getString(c.getColumnIndex("GENDER"));
            String age = c.getString(c.getColumnIndex("AGE"));
            u = new User(username, nickname, gender, age);
            list.add(u);
        }
        c.close();
        return list;
    }
}
