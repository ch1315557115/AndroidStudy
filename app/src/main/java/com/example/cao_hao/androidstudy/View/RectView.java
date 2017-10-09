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
import com.example.cao_hao.androidstudy.utils.BitmapUtils;

/**
 * Created by cao-hao on 17-8-25.
 *
 * Bitmap绘制中的canvas paint 可以参考 http://blog.csdn.net/carson_ho/article/details/60598775
 *
 *  Bitmap常用方法可以见BitmapUtils工具类
 */

public class RectView extends View {
    /*int left = 150;
    int top = 75;
    int right = 400;
    int bottom = 300;*/

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
//        drawTextCenter(canvas);
        drawBitmapPostScale(canvas);
    }

    /**
     * 缩放图片
     *
     * @param canvas
     */
    private void drawBitmapPostScale(Canvas canvas) {
        // 首先定义一个paint
        Paint paint = new Paint();

        paint.setColor(Color.RED);
        // 设置样式-空心矩形
        paint.setStyle(Paint.Style.STROKE);
        // 绘制一个矩形
        /*Rect targetRect = new Rect(50, 50, 150, 150);
        canvas.drawRect(targetRect, paint);*/

        // 获取图片资源
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.heibian);

//         Matrix类进行图片处理（缩小或者旋转）
        Matrix matrix = new Matrix();
//         缩小一倍
        matrix.postScale(1f, 1f);
//         生成新的图片
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
//         添加到canvas
        canvas.drawBitmap(dstbmp, 0, 0, paint);

//        canvas.drawCircle(50,50,30,paint);
    }

    /**
     * 绘制文字在图形中居中
     * 参考 地址 ：http://blog.csdn.net/hursing/article/details/18703599
     * @param canvas
     */
    private void drawTextCenter(Canvas canvas){
        Rect targetRect = new Rect(50, 50, 400, 150);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setTextSize(30);
        String testString = "测试：ijkJQKA:1234";
        paint.setColor(Color.CYAN);
        canvas.drawRect(targetRect, paint);
        paint.setColor(Color.RED);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(testString, targetRect.centerX(), baseline, paint);
    }
}
