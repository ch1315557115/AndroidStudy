package com.example.cao_hao.androidstudy.View.Matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.utils.LogUtils;

/**
 * Created by cao-hao on 17-9-27.
 */

public class MyScaleImage extends View {
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    // 初始化位图实例
    private Bitmap mBitmap;
    // Matrix实例
    private Matrix mMatrix;

    private int mWidth, mHeight;

    private Context mContext;
    private boolean isScale = true;
    private float scale=1f;

    public MyScaleImage(Context context) {
        super(context);
        init(context);
    }

    public MyScaleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScaleImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mGestureDetector = new GestureDetector(mContext, new MyGestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(mContext,new MyScaleGestureListener());
        // 获得位图
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.holo_stamp_big_image3);

        mMatrix = new Matrix();
        // 获得宽度
        mWidth = mBitmap.getWidth();
        // 获得高度
        mHeight = mBitmap.getHeight();

        LogUtils.d("mWidth:" + mWidth + ";" + "mHeight:" + mHeight);


       /* RelativeLayout.LayoutParams params = new RelativeLayout.
                LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        params.leftMargin = 100;
        params.rightMargin = 100;
        setLayoutParams(params);*/
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

 /*   class MyGestureListener implements GestureDetector.OnDoubleTapListener {

        private MyGestureListener() {

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogUtils.d("onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }*/

    public void setTranslate(float x,float y) {
        mMatrix.postTranslate(x,y); //这个地方并不是移动到（x,y）而是原来横坐标都加上x ,纵坐标加上y，变成新的坐标x0和y0，
        // 假如原来图像坐标为p(x0,y0),新坐标变为p(x0+x,y0+y)

        mMatrix.setTranslate(x,y);// setTranslate只有一次效果，设置使用的不是矩阵乘法，而是直接覆盖掉原来的数值，所以，使用设置可能会导致之前的操作失效。
        invalidate();

    }

    public void setScale(float x,float y){
        mMatrix.preScale(x,y,mWidth/2,mHeight/2);//默认以自己的左上角为缩放中心 ，向右下方缩放
//        mMatrix.preScale(x,y,100,100);//默认以自己的左上角为缩放中心 ，向右下方缩放
        invalidate();
    }

    public void setReset(){
        mMatrix.reset();
        invalidate();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isScale){
                setScale(2f,2f);
                isScale = false;
            }
            else {
                setScale(0.5f,0.5f);
                isScale = true;
            }
            LogUtils.d("onDoubleTap");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtils.d("onSingleTapUp");
            return super.onSingleTapUp(e);
        }
    }

    class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override//手势再进行缩放时会多次调用该方法
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scale=scale *scaleFactor;
            LogUtils.d("scaleFactor:"+scaleFactor);
            if (mBitmap.getWidth()>700){
                return true;
            }
            LogUtils.d("mBitmap.getWidth():"+mBitmap.getWidth());
            if (scale>=4){
                scale =4;

            }
            else if (scale <=0.5f){
                scale =0.5f;
            }
            else {
                setScale(scaleFactor,scaleFactor);//再前一次放大基础上再次放大，所以看到的是scaleFactor很小，图片却越来越来大，因为有之前放大的基础
            }
            LogUtils.d("scale:" + scale);

            return super.onScale(detector);

        }
    }

}
