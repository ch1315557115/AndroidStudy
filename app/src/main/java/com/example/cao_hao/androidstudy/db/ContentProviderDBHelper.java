package com.example.cao_hao.androidstudy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cao-hao on 17-8-9.
 */

public class ContentProviderDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "lenve.com.example.cao_hao.androidstudy.db";
    public static final String USERTABLE_NAME = "user_table";
    private static final int DATABASE_VERSION = 1;

    public ContentProviderDBHelper(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public ContentProviderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USERTABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT,NICKNAME TEXT,GENDER TEXT,AGE TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
