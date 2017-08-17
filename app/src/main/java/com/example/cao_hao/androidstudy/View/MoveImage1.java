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

public class MoveImage1 extends ImageView {
    private int lastX;
    private int lastY;
    private int  left, top, right, bottom;
    int screenWidth;
    int screenHeight;


    public void set(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public MoveImage1(Context context) {
        super(context);
      /*  screenWidth = getScreenWidth(this);//获取屏幕宽度
        screenHeight = getScreenHeight(this) - getStatusHeight(MainActivity.this);*/
    }

    public MoveImage1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImage1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MoveImage1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.d("screenWidth:" + screenWidth + "screenHeight:" + screenHeight);
        //得到事件的坐标
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = eventX;
                lastY = eventY;

                LogUtils.d("moveimage---按下了");
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = eventX - lastX;
                int dy = eventY - lastY;

                top = getTop() + dy;
                left = getLeft() + dx;
               /* right = getRight()+dx;
                bottom = getBottom()+dy;*/

                right = left+getWidth();
                bottom = top+getHeight();

                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (top >= screenHeight - getHeight())
                    top = screenHeight - getHeight();

                layout(left, top, right, bottom);

                LogUtils.d("moveimage---滑动了" + "left:" + left + "rignt:" + right + "top:" + top + "bottom:" + bottom);
                //重新确定上一次滑动起点位置，计算dx 和 dy
                lastX = eventX;
                lastY = eventY;
                break;
        }
        /*return super.onTouchEvent(event);*/  //不要调用父类返回值，否则后面滑动没有效果
        return true;
    }
}