package com.example.cao_hao.androidstudy.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.cao_hao.androidstudy.R;

/**
 * Created by cao-hao on 17-8-25.
 */

public class RectView extends View {
    int left = 150;
    int top = 75;
    int right = 400;
    int bottom = 300;

    public RectView(Context context) {
        super(context);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 首先定义一个paint
        Paint paint = new Paint();


        paint.setColor(Color.RED);
        // 设置样式-空心矩形
        paint.setStyle(Paint.Style.STROKE);
        // 绘制一个矩形


        canvas.drawRect(left, top, right, bottom, paint);


       /* paint.setColor(Color.YELLOW);
        canvas.drawRect(250, 75, 350, 120, paint);


        paint.setColor(Color.GREEN);


        canvas.drawRect(150, 120, 250, 170, paint);


        paint.setColor(Color.CYAN);
        canvas.drawRect(250, 120, 350, 170, paint);


        // 绘文字
        // 设置颜色
        paint.setColor(Color.BLACK);
        // 绘文字
        canvas.drawText("Hello1", 200, 90, paint);
        canvas.drawText("Hello2", 300, 100, paint);
        canvas.drawText("Hello3", 200, 150, paint);
        canvas.drawText("Hello4", 300, 170, paint);
*/

        // 绘图
        // 从资源文件中生成位图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.is_check);
/*       *//* Rect Rect1 = new Rect(0, 0, 100, 100);
        Rect Rect2 = new Rect(0, 0, 100, 100);*//*
        // 绘图
//        canvas.drawBitmap(bitmap, 120, 120, paint);
//        Canvas canvas = new Canvas(target);
        *//**
         * 首先绘制圆形
         *//*
        int min = 60 ;
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        *//**
         * 使用SRC_IN
         *//*
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        *//**
         * 绘制图片
         */
//        canvas.drawBitmap(bitmap, left, top, paint);
        drawBitmapPostScale(canvas,left,top,paint);
    }

    /**
     * 缩放图片
     *
     * @param canvas
     */
    private void drawBitmapPostScale(Canvas canvas,int left,int top,Paint paint) {
        // 获取图片资源
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.is_check);
      /*  // Matrix类进行图片处理（缩小或者旋转）
        Matrix matrix = new Matrix();
        // 缩小一倍
        matrix.postScale(1f, 1f);
        // 生成新的图片
        Bitmap dstbmp = Bitmap.createBitmap(bmp1, 0, 0, bmp1.getWidth(),
                bmp1.getHeight(), matrix, true);*/
        // 添加到canvas
       int with =  this.getResources().getDimensionPixelSize(
                R.dimen.with);
        canvas.drawBitmap(bitmap, right -with, bottom-with, paint);
//        canvas.drawBitmap(bitmap, 100, 100, paint);
    }
}
