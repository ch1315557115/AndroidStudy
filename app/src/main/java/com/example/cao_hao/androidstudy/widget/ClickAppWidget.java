package com.example.cao_hao.androidstudy.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.utils.LogUtils;

/**
 * Created by cao-hao on 17-8-18.
 *
 *  我们运行应用后，长按桌面出现widget 进去找到该应用 长按拖拽就可以把widget放到桌面上。
 */

public class ClickAppWidget extends AppWidgetProvider {

    // 删除widget的时候调用 widget能创建多个 每删除一个该方法就会被调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        LogUtils.d("onDeleted");
    }

    // 当最后一个widget被删除时调用 当创建多个widget时候 最后一次被删除的时候该方法才会被调用
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        LogUtils.d("onDisabled");
    }

    // widget第一次被添加到桌面的时候调用
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtils.d("onEnabled");
    }

    // 接受广播事件，接受一次就调用一次，和普通广播用法一样
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals("click")){
            LogUtils.d("onReceive");

            // 获得该widget的远程视图实例
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

            remoteViews.setTextViewText(R.id.btnSend, "点击成功");

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            //获得该应用的所有appWidgetIds  如果桌面有多个该应用widget 一个widget被点击，所有都会被更新
            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context,ClickAppWidget.class));
            manager.updateAppWidget(appWidgetIds, remoteViews);

        }
    }

    /**
     * 到达xml配置文件里 指定的更新时间或者当用户向桌面添加Widget时就被调用
     *
     * @param AppWidgetManager AppWidget的管理器
     * @param appWidgetIds     桌面上 所有的widget都会被分配一个唯一的ID,存在数组里
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // 创建一个Intent,设定action为click
        Intent intent = new Intent("click");

        // 设置pendingIntent的作用
        /**
         * 1、PendingIntent作用

         根据字面意思就知道是延迟的intent，主要用来在某个事件完成后执行特定的Action。PendingIntent包含了Intent及Context，
         所以就算Intent所属程序结束，PendingIntent依然有效，可以在其他程序中使用。常用在通知栏及短信发送系统中。

         PendingIntent一般作为参数传给某个实例，在该实例完成某个操作后自动执行PendingIntent上的Action，也可以通过
         PendingIntent的send函数手动执行，并可以在send函数中设置OnFinished表示send成功后执行的动作。
         */
        PendingIntent mpendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         *  顾名思义，它是一个远程视图。App Widget中的视图，都是通过RemoteViews转换表现的。
         在RemoteViews的构造函数中，通过传入layout文件的id来获取 “layout文件对应的视图(RemoteViews)”；
         调用RemoteViews中的方法能对layout中的组件进行设置  widgetViews.setOnClickPendingIntent(R.id.widget_btn, mpendingIntent);
         来设ID对应的Button的点击响应事件)。
         */

        //  获得该widget的远程视图实例
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //  设定点击事件
        remoteViews.setOnClickPendingIntent(R.id.btnSend, mpendingIntent);

        //  调用updateAppWidget方法更新Appwidget
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }
}
