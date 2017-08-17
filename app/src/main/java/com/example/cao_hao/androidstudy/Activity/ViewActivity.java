package com.example.cao_hao.androidstudy.Activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cao_hao.androidstudy.R;

import com.example.cao_hao.androidstudy.View.MoveImage;

public class ViewActivity extends Activity /* implementsView.OnTouchListener*/ {

    private MoveImage mMoveImage;
    private ImageView mImage;
    private ImageView mImage1;
    private RelativeLayout parentView;

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
        mManager = getWindowManager();
        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,0,0, PixelFormat.TRANSPARENT);

        mMoveImage = (MoveImage) findViewById(R.id.mi_image);
        mImage = (ImageView) findViewById(R.id.iv_image);
        mImage1 = (ImageView) findViewById(R.id.iv_image1);
        mImage1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                 //得到事件的坐标
                int eventX = (int) event.getRawX();
                int eventY = (int) event.getRawY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.x = eventX;
                        mLayoutParams.y = eventY;
                        mManager.updateViewLayout(mImage1,mLayoutParams);

                }

                return true;
            }
        });

        ///
        parentView = (RelativeLayout) mImage.getParent();


        // 获取屏幕密度（方法）
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：px）
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：p）
        mMoveImage.set(screenWidth,screenHeight,getStatusBarHeight());

        //设置touch监听
//        mImage.setOnTouchListener(this);
    }


  /*  @Override
    public boolean onTouch(com.example.cao_hao.androidstudy.View v, MotionEvent event) {
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
*/
    /**
     * 获取底部菜单栏
     * @return
     */
    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

}
