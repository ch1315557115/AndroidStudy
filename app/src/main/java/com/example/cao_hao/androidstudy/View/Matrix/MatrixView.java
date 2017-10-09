package com.example.cao_hao.androidstudy.View.Matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.utils.LogUtils;

/**设置(set)

 设置使用的不是矩阵乘法，而是直接覆盖掉原来的数值，所以使用设置可能会导致之前的操作失效。

 平移 Translate(x,y),而是原来横坐标都加上x ,纵坐标加上y 平移到新的位置


         * 仅用pre：

         Matrix m ＝ new Matrix();
         m.reset();
         m.preTranslate(tx, ty); //使用pre，越靠后越先执行。
         m.preScale(sx, sy);

         仅用post:
         Matrix m ＝ new Matrix();
         m.reset();
         m.postScale(sx, sy);  //使用post，越靠前越先执行。
         m.postTranslate(tx, ty);

        混合:  先pre  再post

     Andorid的API提供了set、post和pre三种操作：

     set是直接设置Matrix的值，每次set一次，整个Matrix的数组都会变掉；

     post是后乘，当前的矩阵乘以参数给出的矩阵。可以连续多次使用post，来完成所需的整个变换。

     pre是前乘，参数给出的矩阵乘以当前的矩阵。所以操作是在当前矩阵的最前面发生的。

 可以理解为原来矩阵为a  pre b,pre c, 再post d,post e,  则结果为cbade  ,所以是使用post，越靠前越先执行，使用pre，越靠后越先执行

 *
 * Created by cao-hao on 17-9-27.
 */

public class MatrixView extends View {
    // 初始化位图实例
    private Bitmap bitmap;
    // Matrix实例
    private Matrix matrix = new Matrix();

    // 位图的宽和高
    private int width, height;
    // 缩放比
    private float scale = 1.0f;
    // 判断缩放还是旋转
    private boolean isScale = false;

    private Context mContext;

    public MatrixView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        // 获得位图
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.holo_stamp_big_image3);
        // 获得宽度
        width = bitmap.getWidth();
        // 获得高度
        height = bitmap.getHeight();
        // 是当前视图获得焦点
//        this.setFocusable(true);
        LogUtils.d("width:" + width + ";" + "height:" + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null){
            // 1.根据原始位图和Matrix创建新图片
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
            // 2.绘制新位图
            canvas.drawBitmap(bitmap2, matrix, null);

            //上面1和2步骤可以合并成下面一步
//            canvas.drawBitmap(bitmap,matrix,null);
        }
    }


    /**
     * 设置倒影效果
     *
     * @param bitmap1
     * @param matrix
     */
    public void setInvertedImage(Bitmap bitmap1, Matrix matrix) {
        matrix.setScale(1, -1);
        matrix.postTranslate(0, bitmap1.getHeight());
    }

    public void setTranslate(float x,float y) {
        matrix.postTranslate(x,y); //这个地方并不是移动到（x,y）而是原来横坐标都加上x ,纵坐标加上y，变成新的坐标x0和y0，
        // 假如原来图像坐标为p(x0,y0),新坐标变为p(x0+x,y0+y)

        matrix.setTranslate(x,y);// setTranslate只有一次效果，设置使用的不是矩阵乘法，而是直接覆盖掉原来的数值，所以，使用设置可能会导致之前的操作失效。
        invalidate();

    }

    public void setScale(float x,float y){
        matrix.preScale(x,y);//默认以自己的左上角为缩放中心 ，向右下方缩放
        invalidate();
    }

    public void setRotate(float degree){
        matrix.postRotate(degree, width / 2,
                height / 2);
        invalidate();
    }

    public void setReset(){
        matrix.reset();
        invalidate();
    }
}
