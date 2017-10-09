package com.example.cao_hao.androidstudy.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {

    /**
     * 展示Toast
     * @param mContext
     * @param content
     */
    public static void showToast(Context mContext, String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getDisplayWidth(Activity context) {
        WindowManager wm = context.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getDisplayWHHeigth(Activity context) {
        WindowManager wm = context.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }


    /**
     * 获取精确的屏幕大小
     * @param context
     * @return
     */
    public static int[] getAccurateScreenDpi(Activity context) {
        int[] screenWH = new int[2];
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("屏幕宽高:" +  screenWH[0] + "," + "屏幕高度：" + screenWH[1]);
        return screenWH;
    }

    /**
     * 获取屏幕实际显示View的高度
     * @param context
     * @return
     */
    public static int getDisplsyViewHeight(Activity context){
        int screenHeight =  getAccurateScreenDpi(context)[1];
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        LogUtils.d("实际显示View高度" + dm.heightPixels);
        return dm.heightPixels;
    }

    /**
     * 获取底部虚拟键盘的高度  虚拟键盘表示底部的返回键、Home键、最近任务列表键
     * @param context
     * @return
     */
    public static int getBottomKeyboardHeight(Activity context){
        int screenHeight =  getAccurateScreenDpi(context)[1];
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightDifference = screenHeight - dm.heightPixels;
        LogUtils.d("获取底部虚拟键盘的高度:" + heightDifference);
        return heightDifference;
    }

    /**
     * 获取View高度，布局完成后在屏幕中显示高度
     * @param
     * @return
     */
    public static int[] getViewHeight(final View view){
        final  int[] viewWH = new int[2];
        ViewTreeObserver treeObserver = view.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                viewWH[0] = view.getWidth();
                viewWH[1] = view.getHeight();
                LogUtils.d("View宽度：" + viewWH[0] + "," + "View高度：" + viewWH[1]);
            }
        });
        return viewWH;
    }

    /**
     * 数字时间串
     *
     * @return
     */
    public static String getDateNumber() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 随机num个字母
     *
     * @return
     */
    public static String randomCapital(int num) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            buffer.append(chars.charAt((int) (Math.random() * 26)));
        }
        return buffer.toString();
    }


    /**
     * 获取时间字符串（yyyy-MM-dd HH:mm:ss），如 2017-04-01 12:10:22
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        if (date != null) {
            return sdf.format(new Date());
        }
        return sdf.format(new Date());
    }

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 返回文字描述的日期
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static String getTimeFormatText(String dateStr) {

        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            return "";
        }

        return getTimeFormatText(date);
    }


}
