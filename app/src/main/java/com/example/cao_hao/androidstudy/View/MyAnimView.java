package com.example.cao_hao.androidstudy.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.example.cao_hao.androidstudy.bean.Point;
import com.example.cao_hao.androidstudy.others.ColorEvaluator;
import com.example.cao_hao.androidstudy.others.PointEvaluator;

/**
 * 实现细节见 http://blog.csdn.net/guolin_blog/article/details/43816093
 *
 * Created by cao-hao on 17-9-6.
 */

public class MyAnimView extends View {
    private static final float RADIUS = 50f;
    private Paint mPaint;
    private Point mCurrentPoint;

    public MyAnimView(Context context) {
        super(context);
    }


    public MyAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentPoint == null){
            mCurrentPoint = new Point(RADIUS,RADIUS);
            DrawCircle(canvas);
            startAnimation();
        }
        else {
            DrawCircle(canvas);
        }
    }
 //---------------------------------------------
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }
//-----------------------------------------------

    private void DrawCircle(Canvas canvas) {
        float currentX = mCurrentPoint.getX();
        float currentY = mCurrentPoint.getY();
        canvas.drawCircle(currentX, currentY, RADIUS, mPaint);
    }

    private void startAnimation(){
        Point startPoint = new Point(getWidth() / 2, RADIUS);
//        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);//弧形
        Point endPoint = new Point( getWidth() / 2, getHeight() - RADIUS);//垂直
        ValueAnimator animator = ObjectAnimator.ofObject(new PointEvaluator(),startPoint,endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();

        /*ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(),
                "#0000FF", "#FF0000");
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator).with(anim2);
        animSet.setDuration(5000);
        animSet.start();*/

        /*相比于ValueAnimator，ObjectAnimator可能才是我们最常接触到的类，因为ValueAnimator只不过是对值进行了一个平滑的动画过渡，
        但我们实际使用到这种功能的场景好像并不多。而ObjectAnimator则就不同了，它是可以直接对任意对象的任意属性进行动画操作的，
        比如说View的alpha属性。*/
    }
}
