package com.example.cao_hao.androidstudy.Adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

/**
 * 在一般的应用中可以使用ContentProvider去操作数据库。
 这在数据量很小的时候是没有问题的，但是如果数据量大了，可能导致UI线程发生ANR异常(超过5秒)。
 当然你也可以写个Handler去做这些操作，只是你每次使用ContentProvider时都要再写个Handler，必然降低了效率。
 因此当数据量较大时，最好还是使用android已经封装好的异步查询框架AsyncQueryHandler,优化我们的代码
 * Created by cao-hao on 17-8-21.
 */

public class MyQueryHandler extends AsyncQueryHandler {
    public MyQueryHandler(ContentResolver cr) {
        super(cr);
    }

    //此cursor是查询结束后返回的cursor
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        //将查询后得到的cursor替换原来的cursor
        ((MyCursorAdapter)cookie).changeCursor(cursor);
    }
}
