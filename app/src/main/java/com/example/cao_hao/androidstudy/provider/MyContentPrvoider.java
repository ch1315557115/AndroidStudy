package com.example.cao_hao.androidstudy.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.cao_hao.androidstudy.db.ContentProviderDBHelper;
import com.example.cao_hao.androidstudy.utils.LogUtils;

/**
 * 在secondDayStudy中ContentProviderActivity 测试 MyContentPrvoider
 *
 * Created by cao-hao on 17-8-9.
 */

public class MyContentPrvoider extends ContentProvider {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private static UriMatcher matcher;
    private static final String AUTHORITY = "com.example.cao_hao.thirddaystudy.mycontentprovider";

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "user", 1);// 配置表
        matcher.addURI(AUTHORITY, "user/#", 2);// 匹配任何数字
        matcher.addURI(AUTHORITY, "user/*", 3);// 匹配任何文本
    }

    @Override
    public boolean onCreate() {
        helper = new ContentProviderDBHelper(getContext(), "lenve.com.example.cao_hao.androidstudy.db", null, 1);
        db = helper.getWritableDatabase();
        LogUtils.d("MyContentProvider--->onCreate()");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        db.insert(ContentProviderDBHelper.USERTABLE_NAME,null,contentValues);
        LogUtils.d("插入数据了！");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int code = matcher.match(uri);
        int result = 0;
        switch (code) {
            case UriMatcher.NO_MATCH:
                break;
            case 1:
                // 删除所有
                result = db.delete(ContentProviderDBHelper.USERTABLE_NAME, null, null);
                LogUtils.d("删除所有数据！");
                break;
            case 2:
                // com.example.cao_hao.thirddaystudy.mycontentprovider/user/10
                // 按条件删除，id
                result = db.delete(ContentProviderDBHelper.USERTABLE_NAME, "_id=?", new String[] { ContentUris.parseId(uri) + "" });
                LogUtils.d("配置数字删除一条数据");
                break;
            case 3:
                // com.example.cao_hao.thirddaystudy.mycontentprovider/user/zhangsan
                // uri.getPathSegments()拿到一个List<String>，里边的值分别是0-->user、1-->zhangsan
                result = db.delete(ContentProviderDBHelper.USERTABLE_NAME, "USERNAME=?", new String[] { uri.getPathSegments().get(1) });
                LogUtils.d("配置文本删除一条数据");
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int code = matcher.match(uri);
        int result = 0;
        switch (code) {
            case 1:
                result = db.update(ContentProviderDBHelper.USERTABLE_NAME, values, selection, selectionArgs);
                LogUtils.d("更新数据1");
                break;
            case 2:  // 根据手动传参id来更新
                result = db.update(ContentProviderDBHelper.USERTABLE_NAME, values, "_id=" + ContentUris.parseId(uri) + " AND " + selection,
                        selectionArgs);
                LogUtils.d("更新数据2");
                break;

            case 3:
                result = db.update(ContentProviderDBHelper.USERTABLE_NAME, values, selection, selectionArgs);
                LogUtils.d("更新数据3");
                break;
        }
        return result;
    }
}
