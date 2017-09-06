package com.example.cao_hao.androidstudy.Activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.View.MoveImage;

/**
 *  关于View的拖动大家应该比较了解了，比如对一个控件IamgeView拖动，或者一个视图View拖动，实现方式也很容易，继承OnTouchListener接口，
 *  然后重写onTouch方法，在触屏事件进行处理即可。但是Popupwindow如何实现拖动呢，我们都知道它和普通的View不一样，
 *  因为它不是继承于View类的 ,Popupwindow的拖动这一块，也和View有联系。首先看一下它的API，看一看有没有和View移动、
 *  变化相关的方法，果然在最后有几个update()方法, update()方法用来更新Popupwindow的位置和大小的
 */
public class ViewActivity extends Activity  implements View.OnTouchListener {

    private MoveImage mMoveImage;
    private ImageView mImage;
    private ImageView mImage1;
    private View parentView;

    private WindowManager mManager;
    private WindowManager.LayoutParams mLayoutParams;

    private int lastX;
    private int lastY;
    private int maxRight;
    private int maxBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view);

        //自定义拖动控件
        mMoveImage = (MoveImage) findViewById(R.id.mi_image);

        //代码设置监听拖动事件
        mImage = (ImageView) findViewById(R.id.iv_image);
        mImage1 = (ImageView) findViewById(R.id.iv_image1);
        mImage1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                 //得到事件的坐标
                int orgX = 0, orgY = 0;
                int offsetX, offsetY;
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        orgX = (int) event.getX();
                        orgY = (int) event.getY();

                }
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - orgX;
                        offsetY = (int) event.getRawY() - orgY;
//                        popupWindow.update(offsetX, offsetY, -1, -1, true);

                }

                return true;
            }
        });

        // 获取屏幕密度（方法）  自定义的可拖动控件滑动底部还有问题
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：px）
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：p）
        mMoveImage.set(screenWidth,screenHeight);


        parentView = (View) mImage.getParent();
        //设置touch监听
        mImage.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //得到事件的坐标
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //得到父视图的right/bottom
                if(maxRight==0) {//保证只赋一次值
                    maxRight = parentView.getRight();
                    maxBottom = parentView.getBottom();
                }
                //第一次记录lastX/lastY
                lastX =eventX;
                lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算事件的偏移
                int dx = eventX-lastX;
                int dy = eventY-lastY;
                //根据事件的偏移来移动imageView
                int left = mImage.getLeft()+dx;
                int top = mImage.getTop()+dy;
                int right = mImage.getRight()+dx;
                int bottom = mImage.getBottom()+dy;
                //限制left >=0
                if(left<0) {
                    right += -left;
                    left = 0;
                }
                //限制top
                if(top<0) {
                    bottom += -top;
                    top = 0;
                }
                //限制right <=maxRight
                if(right>maxRight) {
                    left -= right-maxRight;
                    right = maxRight;
                }
                //限制bottom <=maxBottom
                if(bottom>maxBottom) {
                    top -= bottom-maxBottom;
                    bottom = maxBottom;
                }
                mImage.layout(left, top, right, bottom);
                //再次记录lastX/lastY
                lastX = eventX;
                lastY = eventY;
                break;
            default:
                break;
        }
        return true;//所有的motionEvent都交给imageView处理
    }
}
