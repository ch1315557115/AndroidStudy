package com.example.cao_hao.androidstudy.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.cao_hao.androidstudy.utils.LogUtils;


/**
 * 属性构造方法要重载，不然在布局文件中引用会报错
 * Created by cao-hao on 17-8-11.
 */

public class MoveImage extends ImageView {

    int lastX = 0;
    int lastY = 0;
    int left, top, right, bottom;
    int screenWidth;
    int screenHeight;
    int statusBarHeight;

    public void set(int screenWidth, int screenHeight,int statusBarHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.statusBarHeight = statusBarHeight;
    }

    public MoveImage(Context context) {
        super(context);
      /*  screenWidth = getScreenWidth(this);//获取屏幕宽度
        screenHeight = getScreenHeight(this) - getStatusHeight(MainActivity.this);*/
    }

    public MoveImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MoveImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /* int lastX = 0;
        int lastY = 0;
        不能放在这个位置，每次事件按下、滑动、抬起都会执行这里面的方法 ，如果在这定义最后一次滑动坐标，按下时候正常，
        滑动时候在次执行会将lastX = 0 lastY = 0 这样在计算 滑动dx 和 dy时候会出现错误
        */

        LogUtils.d("screenWidth:" + screenWidth + "screenHeight:" + screenHeight + "statusBarHeight:" + statusBarHeight);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                LogUtils.d("moveimage---按下了");
                break;
            case MotionEvent.ACTION_MOVE:

                int dx = (int) (event.getRawX() - lastX);
                int dy = (int) (event.getRawY() - lastY);

                top = getTop() + dy;
                left = getLeft() + dx;
                right = left + getWidth();
                bottom = top + getHeight();

                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (right >= screenWidth) {
                    right = screenWidth;
                    left = screenWidth - getWidth();
                }
                if (bottom >= screenHeight - statusBarHeight) {
                    bottom = screenHeight - statusBarHeight;
                    top = screenHeight - getHeight();
                }

                layout(left, top, right, bottom);

                LogUtils.d("moveimage---滑动了" + "left:" + left + "rignt:" + right + "top:" + top + "bottom:" + bottom);
                //重新确定上一次滑动起点位置，计算dx 和 dy
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
        }
        /*return super.onTouchEvent(event);*/  //不要调用父类返回值，否则后面滑动没有效果
        return true;
    }
}