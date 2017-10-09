package com.example.cao_hao.androidstudy.View.Matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.utils.LogUtils;

/**1.Matrix中的几个常用的变换方法

 setTranslate(float dx, float dy)：控制Matrix进行平移
 setRotate(float degrees, float px, float py)：旋转，参数依次是:旋转角度，轴心(x,y)
 setScale(float sx, float sy, float px, float py):缩放， 参数依次是：X，Y轴上的缩放比例；缩放的轴心
 setSkew(float kx, float ky)：倾斜(扭曲)，参数依次是：X，Y轴上的缩放比例

 *
 * http://blog.csdn.net/true100/article/details/51141496
 * http://blog.csdn.net/s740088128/article/details/48316525
 *
 * http://www.runoob.com/w3cnote/android-tutorial-canvas-api3.html
 *
 * http://blog.csdn.net/cdzz11/article/details/51657429
 * 自定义缩放ImageView
 * Created by cao-hao on 17-9-20.
 */


public class ScaleImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    // 是否是初次加载
    private boolean mIsFirst = false;
    // 初始化的图片缩放值
    private float mBaseScale;
    // 图片放大的最大值
    private float mMaxScale;
    // 图片缩放工具操作类Matrix
    private Matrix mImageMatrix;
    // 检测两个手指在屏幕上做缩放的手势工具类
    private ScaleGestureDetector mScaleGestureDetector;

    /**
     * 以下三个方法为自定义View要实现的构造方法
     *
     * @param context
     */
    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化数据
     *
     * @description：
     * @author ldm
     * @date 2016-4-12 下午4:57:04
     */
    private void init(Context context) {
        mImageMatrix = new Matrix();
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);

    }


    // 当View加载完成时可能通过OnGlobalLayoutListener监听，在布局加载完成后获得一个view的宽高。
    @Override
    public void onGlobalLayout() {
        if (!mIsFirst) {
            mIsFirst = true;
            // 获取控件的宽度和高度
            int width = getWidth();
            int height = getHeight();
            // 获取到ImageView对应图片的宽度和高度
            Drawable d = getDrawable();
            if (null == d) {
                return;
            }
            int dw = d.getIntrinsicWidth();// 图片固有宽度
            int dh = d.getIntrinsicHeight();// 图片固有高度
            float scale = 1.0f;
            if (dw > width && dh < height) {// 图片宽度大于控件宽度但高度小于控件高度
                scale = width * 1.0f / dw;// 缩小一定值
            }
            // 图片宽度大于控件宽度但高度小于控件高度& 图片的宽高都小于控件的宽高
            if ((dw < width && dh < height) || (dw > width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);// 按照宽度对应缩放最小值进行缩放
            }
            if (dw < width && dh > height) {// 图片宽度小于控件宽度，但图片高度大于控件高度
                scale = height * 1.0f / dh;// 缩小一定的比例
            }
            mBaseScale = scale;
            mMaxScale = mBaseScale * 4;
            // 将图片移动到手机屏幕的中间位置
            float dx = width / 2 - dw / 2;
            float dy = height / 2 - dh / 2;
            mImageMatrix.postTranslate(dx, dy);
            mImageMatrix.postScale(mBaseScale, mBaseScale, width / 2,
                    height / 2);// 参数依次是：X，Y轴上的缩放比例；缩放的轴心
            setImageMatrix(mImageMatrix);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        invalidate();
        return true;//这里要return true才行
    }

    // 当view被附着到一个窗口时触发
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //注册OnGlobalLayoutListener
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    // 当view离开附着的窗口时触发
    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //移除OnGlobalLayoutListener
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    // 手势发生时监听器
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 前一个伸缩事件至当前伸缩事件的伸缩比率
        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();
        if (null == getDrawable()) {
            return true;
        }
        // 控件图片的缩放范围
        if ((scale < mMaxScale && scaleFactor > 1.0f)
                || (scale > mBaseScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mBaseScale) {
                scaleFactor = mBaseScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            // 以屏幕中央位置进行缩放
            // mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2,
            // getHeight() / 2);
            // 以手指所在地方进行缩放  detector.getFocusX(), detector.getFocusY()) 获得点击的位置
            mImageMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());
            borderAndCenterCheck();
            setImageMatrix(mImageMatrix);

        }
        return false;
    }

    // 手势发生开始监听

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // 一定要返回true才会进入onScale()这个函数
        return true;
    }

    // 手势结束监听
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub

    }

    private float getScale() {
        float[] values = new float[9];
        mImageMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /**
     * 图片在缩放时进行边界控制
     */
    private void borderAndCenterCheck() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        // 缩放时进行边界检测，防止出现白边
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽度或者高度小于控件的宽或者高；则让其居中
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f;

        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }
        mImageMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 获得图片放大缩小以后的宽和高
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mImageMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    private void paint(Canvas canvas,Paint mPaint) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.holo_stamp_big_image2);
//            canvas.drawBitmap(bmp, mCurrentShowing.matrix, mPaint);
        canvas.drawBitmap(bmp,30,70,mPaint);
    }
}