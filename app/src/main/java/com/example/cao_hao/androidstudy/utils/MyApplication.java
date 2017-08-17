package com.example.cao_hao.androidstudy.utils;

import android.app.Application;
import android.content.Context;

/**
 * 需要在配置文件中注册
 * <p>
 * <application
 * android:name="com.example.cao_hao.androidstudy.utils.MyApplication"
 * android:allowBackup="true"
 * android:icon="@mipmap/ic_launcher"
 * android:label="@string/app_name"
 *
 * Application和Actovotu,Service一样是android框架的一个系统组件，当android程序启动时系统会创建一个 application对象，
 * 用来存储系统的一些信息。通常我们是不需要指定一个Application的，这时系统会自动帮我们创建，如果需要创建自己 的Application，
 * 也很简单创建一个类继承 Application并在manifest的application标签中进行注册(只需要给Application标签增加个name属性把自
 * 己的 Application的名字定入即可)。
 * <p>
 * android系统会为每个程序运行时创建一个Application类的对象且仅创建一个，所以Application可以说是单例 (singleton)模式的一个类.
 * 且application对象的生命周期是整个程序中最长的，它的生命周期就等于这个程序的生命周期。因为它是全局 的单例的，所以在不同的Activity,
 * Service中获得的对象都是同一个对象。所以通过Application来进行一些，数据传递，数据共享 等,数据缓存等操作。
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SharedPreferencesUtils.init(mContext);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
