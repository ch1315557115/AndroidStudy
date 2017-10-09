/*
 * TouchImageView.java
 * By: Michael Ortiz
 * Updated By: Patrick Lackemacher
 * Updated By: Babay88
 * Updated By: @ipsilondev
 * Updated By: hank-cp
 * Updated By: singpolyma
 * -------------------
 * Extends Android ImageView to include pinch zooming, panning, fling and double tap zoom.
 */

package com.ortiz.touch;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * 最好用的可缩放ImagView，继承自ImageView具有ImageView的所有功能；除此之外，还有缩放、拖拽、双击放大等功能，并伴有动画效果
 *
 * TouchImageView所支持的缩放类型的取值：centerCrop,centerInside,fitXY,fitCenter,center
 *
 * http://blog.csdn.net/coderinchina/article/details/50770234  SimpleOnGestureListener详解
 */

public class TouchImageView extends ImageView {
	
	private static final String DEBUG = "DEBUG";
	
	//
	// SuperMin and SuperMax multipliers. Determine how much the image can be
	// zoomed below or above the zoom boundaries, before animating back to the
	// min/max zoom boundary.
	//
	private static final float SUPER_MIN_MULTIPLIER = .75f;
	private static final float SUPER_MAX_MULTIPLIER = 1.25f;

    //
    // Scale of image ranges from minScale to maxScale, where minScale == 1
    // when the image is stretched to fit view.
    //
    private float normalizedScale;
    
    //
    // Matrix applied to image. MSCALE_X and MSCALE_Y should always be equal.
    // MTRANS_X and MTRANS_Y are the other values used. prevMatrix is the matrix
    // saved prior to the screen rotating.
    //
	private Matrix matrix, prevMatrix;

    //ZOOM 缩放  DRAG 拖动
    private static enum State { NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM };
    private State state;

    private float minScale;
    private float maxScale;
    //最小缩放倍数
    private float superMinScale;
    //最大扩大倍数
    private float superMaxScale;
    private float[] m;
    
    private Context context;
    private Fling fling;
    
    private ScaleType mScaleType;
    
    private boolean imageRenderedAtLeastOnce;
    private boolean onDrawReady;
    
    private ZoomVariables delayedZoomVariables;

    //
    // Size of view and previous view size (ie before rotation)
    //
    private int viewWidth, viewHeight, prevViewWidth, prevViewHeight;
    
    //
    // Size of image when it is stretched to fit view. Before and After rotation.
    //
    private float matchViewWidth, matchViewHeight, prevMatchViewWidth, prevMatchViewHeight;
    
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private GestureDetector.OnDoubleTapListener doubleTapListener = null;
    private OnTouchListener userTouchListener = null;
    private OnTouchImageViewListener touchImageViewListener = null;//

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }
    
    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    	sharedConstructing(context);
    }
    
    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        matrix = new Matrix();
        prevMatrix = new Matrix();
        m = new float[9];
        normalizedScale = 1;
        if (mScaleType == null) {
        	mScaleType = ScaleType.FIT_CENTER;
        }
        minScale = 1;
        maxScale = 3;
        superMinScale = SUPER_MIN_MULTIPLIER * minScale;
        superMaxScale = SUPER_MAX_MULTIPLIER * maxScale;
        setImageMatrix(matrix);
        //有些时候我们需要调整显示角度或者放大缩小图形， 复杂的可以通过自定义View重新onDraw方法来绘制，简单的则可以通过设置matrix来完成。
        setScaleType(ScaleType.MATRIX);
        setState(State.NONE);
        onDrawReady = false;
        super.setOnTouchListener(new PrivateOnTouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        userTouchListener = l;
    }
    
    public void setOnTouchImageViewListener(OnTouchImageViewListener l) {
    	touchImageViewListener = l;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener l) {
        doubleTapListener = l;
    }

    @Override
    public void setImageResource(int resId) {
    	super.setImageResource(resId);
    	savePreviousImageValues();
    	fitImageToView();
    }
    
    @Override
    public void setImageBitmap(Bitmap bm) {
    	super.setImageBitmap(bm);
    	savePreviousImageValues();
    	fitImageToView();
    }
    
    @Override
    public void setImageDrawable(Drawable drawable) {
    	super.setImageDrawable(drawable);
    	savePreviousImageValues();
    	fitImageToView();
    }
    
    @Override
    public void setImageURI(Uri uri) {
    	super.setImageURI(uri);
    	savePreviousImageValues();
    	fitImageToView();
    }
    
    @Override
    public void setScaleType(ScaleType type) {
    	if (type == ScaleType.FIT_START || type == ScaleType.FIT_END) {
    		throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
    	}
    	if (type == ScaleType.MATRIX) {
    		super.setScaleType(ScaleType.MATRIX);
    		
    	} else {
    		mScaleType = type;
    		if (onDrawReady) {
    			//
    			// If the image is already rendered, scaleType has been called programmatically
    			// and the TouchImageView should be updated with the new scaleType.
    			//
    			setZoom(this);
    		}
    	}
    }
    
    @Override
    public ScaleType getScaleType() {
    	return mScaleType;
    }
    
    /**
     * 如果图片初始化是没有缩放的状态，返回false，否则返回true;
     * Returns false if image is in initial, unzoomed state. False, otherwise.
     * @return true if image is zoomed
     */
    public boolean isZoomed() {
    	return normalizedScale != 1;
    }
    
    /**返回代表缩放图片的矩形
     * Return a Rect representing the zoomed image.
     * @return rect representing zoomed image
     */
    public RectF getZoomedRect() {
    	if (mScaleType == ScaleType.FIT_XY) {
    		throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
    	}
    	PointF topLeft = transformCoordTouchToBitmap(0, 0, true);
    	PointF bottomRight = transformCoordTouchToBitmap(viewWidth, viewHeight, true);
    	
    	float w = getDrawable().getIntrinsicWidth();
    	float h = getDrawable().getIntrinsicHeight();
    	return new RectF(topLeft.x / w, topLeft.y / h, bottomRight.x / w, bottomRight.y / h);
    }
    
    /**
     * Save the current matrix and view dimensions
     * in the prevMatrix and prevView variables.
     */
    private void savePreviousImageValues() {
    	if (matrix != null && viewHeight != 0 && viewWidth != 0) {
	    	matrix.getValues(m);
	    	prevMatrix.setValues(m);
	    	prevMatchViewHeight = matchViewHeight;
	        prevMatchViewWidth = matchViewWidth;
	        prevViewHeight = viewHeight;
	        prevViewWidth = viewWidth;
    	}
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
    	Bundle bundle = new Bundle();
    	bundle.putParcelable("instanceState", super.onSaveInstanceState());
    	bundle.putFloat("saveScale", normalizedScale);
    	bundle.putFloat("matchViewHeight", matchViewHeight);
    	bundle.putFloat("matchViewWidth", matchViewWidth);
    	bundle.putInt("viewWidth", viewWidth);
    	bundle.putInt("viewHeight", viewHeight);
    	matrix.getValues(m);
    	bundle.putFloatArray("matrix", m);
    	bundle.putBoolean("imageRendered", imageRenderedAtLeastOnce);
    	return bundle;
    }
    
    @Override
    public void onRestoreInstanceState(Parcelable state) {
      	if (state instanceof Bundle) {
	        Bundle bundle = (Bundle) state;
	        normalizedScale = bundle.getFloat("saveScale");
	        m = bundle.getFloatArray("matrix");
	        prevMatrix.setValues(m);
	        prevMatchViewHeight = bundle.getFloat("matchViewHeight");
	        prevMatchViewWidth = bundle.getFloat("matchViewWidth");
	        prevViewHeight = bundle.getInt("viewHeight");
	        prevViewWidth = bundle.getInt("viewWidth");
	        imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered");
	        super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
	        return;
      	}

      	super.onRestoreInstanceState(state);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	onDrawReady = true;
    	imageRenderedAtLeastOnce = true;
    	if (delayedZoomVariables != null) {
    		setZoom(delayedZoomVariables.scale, delayedZoomVariables.focusX, delayedZoomVariables.focusY, delayedZoomVariables.scaleType);
    		delayedZoomVariables = null;
    	}
    	super.onDraw(canvas);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	savePreviousImageValues();
    }
    
    /**
         得到最大的缩放倍数.
     * Get the max zoom multiplier.
     * @return max zoom multiplier.
     */
    public float getMaxZoom() {
    	return maxScale;
    }

    /**
     * 设置最大的缩放级别. 默认值是: 3
     * Set the max zoom multiplier. Default value: 3.
     * @param max max zoom multiplier.
     */
    public void setMaxZoom(float max) {
        maxScale = max;
        superMaxScale = SUPER_MAX_MULTIPLIER * maxScale;
    }
    
    /**得到最小的缩放倍数
     * Get the min zoom multiplier.
     * @return min zoom multiplier.
     */
    public float getMinZoom() {
    	return minScale;
    }
    
    /**
     * // 得到当前的缩放比。是和TouchImageView相关的所放比，而和原始的图片没有关系
     * Get the current zoom. This is the zoom relative to the initial
     * scale, not the original resource.
     * @return current zoom multiplier.
     */
    public float getCurrentZoom() {
    	return normalizedScale;
    }
    
    /**
     * 设置最小的缩放级别. 默认值是: 1.
     * Set the min zoom multiplier. Default value: 1.
     * @param min min zoom multiplier.
     */
    public void setMinZoom(float min) {
    	minScale = min;
    	superMinScale = SUPER_MIN_MULTIPLIER * minScale;
    }
    
    /**
     * 重新设定缩放级别返回到初始化状态。
     * Reset zoom and translation to initial state.
     */
    public void resetZoom() {
    	normalizedScale = 1;
    	fitImageToView();
    }
    
    /**
     * 设置特定级别的所放比。图片默认是放在整个view的中间
     * Set zoom to the specified scale. Image will be centered by default.
     * @param scale
     */
    public void setZoom(float scale) {
    	setZoom(scale, 0.5f, 0.5f);
    }
    
    /**
     *
     * 设置特定级别的所放比。图片的中心点就是所设置的点的位置(focusX, focusY)。focusX和focusY以view的左上角
     * 为基准表示为取值0到1的百分比。例如，图片的左上角表示为（0，0），图片的右下角表示为（1，1）。
     * 三个参数分别表示;缩放级别，横纵坐标
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left 
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     * @param scale
     * @param focusX
     * @param focusY
     */
    public void setZoom(float scale, float focusX, float focusY) {
    	setZoom(scale, focusX, focusY, mScaleType);
    }
    
    /**
     * 设置特定级别的所放比。图片的中心点就是所设置的点的位置(focusX, focusY)。focusX和focusY
     * 以view的左上角为基准表示为取值0到1的百分比。例如，图片的左上角表示为（0，0），图片的右下角表示为（1，1）。
     * 四个参数分别表示;缩放级别，横纵坐标，缩放类型。
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left 
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     * @param scale
     * @param focusX
     * @param focusY
     * @param scaleType
     */
    public void setZoom(float scale, float focusX, float focusY, ScaleType scaleType) {
    	//
    	// setZoom can be called before the image is on the screen, but at this point, 
    	// image and view sizes have not yet been calculated in onMeasure. Thus, we should
    	// delay calling setZoom until the view has been measured.
    	//
    	if (!onDrawReady) {
    		delayedZoomVariables = new ZoomVariables(scale, focusX, focusY, scaleType);
    		return;
    	}
    	
    	if (scaleType != mScaleType) {
    		setScaleType(scaleType);
    	}
    	resetZoom();
    	scaleImage(scale, viewWidth / 2, viewHeight / 2, true);
    	matrix.getValues(m);
    	m[Matrix.MTRANS_X] = -((focusX * getImageWidth()) - (viewWidth * 0.5f));
    	m[Matrix.MTRANS_Y] = -((focusY * getImageHeight()) - (viewHeight * 0.5f));
    	matrix.setValues(m);
    	fixTrans();
    	setImageMatrix(matrix);
    }
    
    /**
     * 设置和别的TouchImageView一样的缩放比。包括缩放级别，缩放位置和缩放的类型。
     * Set zoom parameters equal to another TouchImageView. Including scale, position,
     * and ScaleType.
     * @param TouchImageView
     */
    public void setZoom(TouchImageView img) {
    	PointF center = img.getScrollPosition();
    	setZoom(img.getCurrentZoom(), center.x, center.y, img.getScaleType());
    }
    
    /**返回可缩放图片的中心点。PointF的坐标在0到1之间，焦点以整个view的左上角的百分比来表示。
     * 例如，图片的左上角是（0，0），右下角是（1，1）。
     * Return the point at the center of the zoomed image. The PointF coordinates range
     * in value between 0 and 1 and the focus point is denoted as a fraction from the left 
     * and top of the view. For example, the top left corner of the image would be (0, 0). 
     * And the bottom right corner would be (1, 1).
     * @return PointF representing the scroll position of the zoomed image.
     */
    public PointF getScrollPosition() {
    	Drawable drawable = getDrawable();
    	if (drawable == null) {
    		return null;
    	}
    	int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        
        PointF point = transformCoordTouchToBitmap(viewWidth / 2, viewHeight / 2, true);
        point.x /= drawableWidth;
        point.y /= drawableHeight;
        return point;
    }
    
    /**
     * 设置图片的所放焦点。焦点以整个view的左上角为基准表示为百分比的形式取值为0到1之间
     * Set the focus point of the zoomed image. The focus points are denoted as a fraction from the
     * left and top of the view. The focus points can range in value between 0 and 1. 
     * @param focusX
     * @param focusY
     */
    public void setScrollPosition(float focusX, float focusY) {
    	setZoom(normalizedScale, focusX, focusY);
    }
    
    /**执行边界检查并修复图像矩阵
     * Performs boundary checking and fixes the image matrix if it 
     * is out of bounds.
     */
    private void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        
        float fixTransX = getFixTrans(transX, viewWidth, getImageWidth());
        float fixTransY = getFixTrans(transY, viewHeight, getImageHeight());
        
        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }
    
    /**
     * When transitioning from zooming from focus to zoom from center (or vice versa)
     * the image can become unaligned within the view. This is apparent when zooming
     * quickly. When the content size is less than the view size, the content will often
     * be centered incorrectly within the view. fixScaleTrans first calls fixTrans() and 
     * then makes sure the image is centered correctly within the view.
     */
    private void fixScaleTrans() {
    	fixTrans();
    	matrix.getValues(m);
    	if (getImageWidth() < viewWidth) {
    		m[Matrix.MTRANS_X] = (viewWidth - getImageWidth()) / 2;
    	}
    	
    	if (getImageHeight() < viewHeight) {
    		m[Matrix.MTRANS_Y] = (viewHeight - getImageHeight()) / 2;
    	}
    	matrix.setValues(m);
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
            
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }
    
    private float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }
    
    private float getImageWidth() {
    	return matchViewWidth * normalizedScale;
    }
    
    private float getImageHeight() {
    	return matchViewHeight * normalizedScale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
        	setMeasuredDimension(0, 0);
        	return;
        }
        //取得Drawable的固有的宽度和高度 这个地方获取到的是dp,比如再SingleTouchImageView中用到的700*700像素照片，
        // 再屏幕分辨率为1080×1920手机上获取到的 drawableWidth和drawableHeight都2100  在720×1080手机上获取宽高为1400
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        viewWidth = setViewSize(widthMode, widthSize, drawableWidth);
        viewHeight = setViewSize(heightMode, heightSize, drawableHeight);
        
        //
        // Set view dimensions
        //
        setMeasuredDimension(viewWidth, viewHeight);
        
        //
        // Fit content within view
        //
        fitImageToView();
    }
    
    /**
     * If the normalizedScale is equal to 1, then the image is made to fit the screen. Otherwise,
     * it is made to fit the screen according to the dimensions of the previous image matrix. This
     * allows the image to maintain its zoom after rotation.
     *
     * 如果标准化的缩放比例等于1，那么图像就会被用来匹配屏幕。否则,它是根据前一个图像矩阵的尺寸来适应屏幕的。这
     允许图像在旋转后保持缩放。
     */
    private void fitImageToView() {
    	Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
        	return;
        }
        if (matrix == null || prevMatrix == null) {
        	return;
        }
        
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
    	
    	//
    	// Scale image for view
    	//
        float scaleX = (float) viewWidth / drawableWidth;
        float scaleY = (float) viewHeight / drawableHeight;
        
        switch (mScaleType) {
        case CENTER:
        	scaleX = scaleY = 1;
        	break;
        	
        case CENTER_CROP:
        	scaleX = scaleY = Math.max(scaleX, scaleY);
        	break;
        	
        case CENTER_INSIDE:
        	scaleX = scaleY = Math.min(1, Math.min(scaleX, scaleY));
        	
        case FIT_CENTER:
        	scaleX = scaleY = Math.min(scaleX, scaleY);
        	break;
        	
        case FIT_XY:
        	break;
        	
    	default:
    		//
    		// FIT_START and FIT_END not supported
    		//
    		throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        	
        }

        //
        // Center the image
        //
        float redundantXSpace = viewWidth - (scaleX * drawableWidth);
        float redundantYSpace = viewHeight - (scaleY * drawableHeight);
        matchViewWidth = viewWidth - redundantXSpace;
        matchViewHeight = viewHeight - redundantYSpace;
        if (!isZoomed() && !imageRenderedAtLeastOnce) {
        	//
        	// Stretch and center image to fit view
        	//
        	matrix.setScale(scaleX, scaleY);
        	matrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2);
        	normalizedScale = 1;
        	
        } else {
        	//
        	// These values should never be 0 or we will set viewWidth and viewHeight
        	// to NaN in translateMatrixAfterRotate. To avoid this, call savePreviousImageValues
        	// to set them equal to the current values.
        	//
        	if (prevMatchViewWidth == 0 || prevMatchViewHeight == 0) {
        		savePreviousImageValues();
        	}
        	
        	prevMatrix.getValues(m);
        	
        	//
        	// Rescale Matrix after rotation
        	//
        	m[Matrix.MSCALE_X] = matchViewWidth / drawableWidth * normalizedScale;
        	m[Matrix.MSCALE_Y] = matchViewHeight / drawableHeight * normalizedScale;
        	
        	//
        	// TransX and TransY from previous matrix
        	//
            float transX = m[Matrix.MTRANS_X];
            float transY = m[Matrix.MTRANS_Y];
            
            //
            // Width
            //
            float prevActualWidth = prevMatchViewWidth * normalizedScale;
            float actualWidth = getImageWidth();
            translateMatrixAfterRotate(Matrix.MTRANS_X, transX, prevActualWidth, actualWidth, prevViewWidth, viewWidth, drawableWidth);
            
            //
            // Height
            //
            float prevActualHeight = prevMatchViewHeight * normalizedScale;
            float actualHeight = getImageHeight();
            translateMatrixAfterRotate(Matrix.MTRANS_Y, transY, prevActualHeight, actualHeight, prevViewHeight, viewHeight, drawableHeight);
            
            //
            // Set the matrix to the adjusted scale and translate values.
            //
            matrix.setValues(m);
        }
        fixTrans();
        setImageMatrix(matrix);
    }
    
    /**
     * Set view dimensions based on layout params
     * 
     * @param mode 
     * @param size
     * @param drawableWidth
     * @return
     */
    private int setViewSize(int mode, int size, int drawableWidth) {
    	int viewSize;
    	switch (mode) {
		case MeasureSpec.EXACTLY:
			viewSize = size;
			break;
			
		case MeasureSpec.AT_MOST:
			viewSize = Math.min(drawableWidth, size);
			break;
			
		case MeasureSpec.UNSPECIFIED:
			viewSize = drawableWidth;
			break;
			
		default:
			viewSize = size;
		 	break;
		}
    	return viewSize;
    }
    
    /**
     * After rotating, the matrix needs to be translated. This function finds the area of image 
     * which was previously centered and adjusts translations so that is again the center, post-rotation.
     * 
     * @param axis Matrix.MTRANS_X or Matrix.MTRANS_Y
     * @param trans the value of trans in that axis before the rotation
     * @param prevImageSize the width/height of the image before the rotation
     * @param imageSize width/height of the image after rotation
     * @param prevViewSize width/height of view before rotation
     * @param viewSize width/height of view after rotation
     * @param drawableSize width/height of drawable
     */
    private void translateMatrixAfterRotate(int axis, float trans, float prevImageSize, float imageSize, int prevViewSize, int viewSize, int drawableSize) {
    	if (imageSize < viewSize) {
        	//
        	// The width/height of image is less than the view's width/height. Center it.
        	//
        	m[axis] = (viewSize - (drawableSize * m[Matrix.MSCALE_X])) * 0.5f;
        	
        } else if (trans > 0) {
        	//
        	// The image is larger than the view, but was not before rotation. Center it.
        	//
        	m[axis] = -((imageSize - viewSize) * 0.5f);
        	
        } else {
        	//
        	// Find the area of the image which was previously centered in the view. Determine its distance
        	// from the left/top side of the view as a fraction of the entire image's width/height. Use that percentage
        	// to calculate the trans in the new view width/height.
        	//
        	float percentage = (Math.abs(trans) + (0.5f * prevViewSize)) / prevImageSize;
        	m[axis] = -((percentage * imageSize) - (viewSize * 0.5f));
        }
    }
    
    private void setState(State state) {
    	this.state = state;
    }
    
    public boolean canScrollHorizontallyFroyo(int direction) {
        return canScrollHorizontally(direction);
    }
    
    @Override
    public boolean canScrollHorizontally(int direction) {
    	matrix.getValues(m);
    	float x = m[Matrix.MTRANS_X];
    	
    	if (getImageWidth() < viewWidth) {
    		return false;
    		
    	} else if (x >= -1 && direction < 0) {
    		return false;
    		
    	} else if (Math.abs(x) + viewWidth + 1 >= getImageWidth() && direction > 0) {
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Gesture Listener detects a single click or long click and passes that on
     * to the view's listener.
     * @author Ortiz
     *
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
    	
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            if(doubleTapListener != null) {
            	return doubleTapListener.onSingleTapConfirmed(e);
            }
        	return performClick();
        }
        
        @Override
        public void onLongPress(MotionEvent e)
        {
        	performLongClick();
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
        	if (fling != null) {
        		//
        		// If a previous fling is still active, it should be cancelled so that two flings
        		// are not run simultaenously.
        		//
        		fling.cancelFling();
        	}
        	fling = new Fling((int) velocityX, (int) velocityY);
        	compatPostOnAnimation(fling);
        	return super.onFling(e1, e2, velocityX, velocityY);
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
        	boolean consumed = false;
            if(doubleTapListener != null) {
            	consumed = doubleTapListener.onDoubleTap(e);
            }
        	if (state == State.NONE) {
                //判断双击后是放大还是缩小
	        	float targetZoom = (normalizedScale == minScale) ? maxScale : minScale;
	        	DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, e.getX(), e.getY(), false);
	        	compatPostOnAnimation(doubleTap);
	        	consumed = true;
        	}
        	Log.d("cao-hao","consumed:" + consumed);
        	return consumed;//根据打打印结果推测返回ture 事件被消耗掉，不会传递到onTouch里执行setImageMatrix(matrix)，touchImageViewListener.onMove()等方法了
            //双击看到有这两个方法打印值是因为点一下屏幕事件会传递到onTouch里  点击一次会执行两次这里面方法，按下和抬起。双击会执行3个，
            //第一次按下和抬起，第二次按下，抬起时候不会执行，事件被消费掉了
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if(doubleTapListener != null) {
            	return doubleTapListener.onDoubleTapEvent(e);
            }
            return false;
        }
    }
    
    public interface OnTouchImageViewListener {
    	public void onMove();
    }
    
    /**
     * Responsible for all touch events. Handles the heavy lifting of drag and also sends
     * touch events to Scale Detector and Gesture Detector.
     * @author Ortiz
     *
     */
    private class PrivateOnTouchListener implements OnTouchListener {
    	
    	//
        // Remember last point position for dragging
        //
        private PointF last = new PointF();
    	
    	@Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());
            
            if (state == State.NONE || state == State.DRAG || state == State.FLING) {
	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                	last.set(curr);
	                    if (fling != null)
	                    	fling.cancelFling();
	                    setState(State.DRAG);
	                    break;
	                    
	                case MotionEvent.ACTION_MOVE:
	                    if (state == State.DRAG) {
	                        float deltaX = curr.x - last.x;
	                        float deltaY = curr.y - last.y;
	                        float fixTransX = getFixDragTrans(deltaX, viewWidth, getImageWidth());
	                        float fixTransY = getFixDragTrans(deltaY, viewHeight, getImageHeight());
	                        matrix.postTranslate(fixTransX, fixTransY);
	                        fixTrans();
	                        last.set(curr.x, curr.y);
	                    }
	                    break;
	
	                case MotionEvent.ACTION_UP:
	                case MotionEvent.ACTION_POINTER_UP:
	                    setState(State.NONE);
	                    break;
	            }
            }
            
            setImageMatrix(matrix);
            Log.d("cao-hao","setImageMatrix(matrix)");
            
            //
    		// User-defined OnTouchListener
    		//
    		if(userTouchListener != null) {
    			userTouchListener.onTouch(v, event);
    		}
            
    		//
    		// OnTouchImageViewListener is set: TouchImageView dragged by user.
    		//
    		if (touchImageViewListener != null) {
    			touchImageViewListener.onMove();
                Log.d("cao-hao","touchImageViewListener.onMove()");
    		}
    		
            //
            // indicate event was handled
            //
            return true;
        }
    }

    /**
     * ScaleListener detects user two finger scaling and scales image.
     * @author Ortiz
     *
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            setState(State.ZOOM);
            return true;// 一定要返回true才会进入onScale()这个函数
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d("cao-hao","onScale");
            //根据打印结果 让图片缩放代码在onTouch中的 setImageMatrix(matrix)方法
        	scaleImage(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY(), true);
        	
        	//
        	// OnTouchImageViewListener is set: TouchImageView pinch zoomed by user.
        	//
        	if (touchImageViewListener != null) {
        		touchImageViewListener.onMove();
        	}
            return true;//并没有消费事件
        }
        
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        	super.onScaleEnd(detector);
            /**
             * 手势放大或者缩小结束后，如果超过设定值，会自动缩小回来，缩小回来是一个动画过程 ，设定值是maxScale和minScale，但是
             * 手势控制缩放范围是superMinScale和superMaxScale， 它们之间关系 是superMinScale = 0.75 * minScale;
             * superMaxScale = 1.25 * maxScale;所以手势放大和缩小过程中会到达最大值，缩放结束后会回退到设定值，看起来有一个动画效果
             */
        	setState(State.NONE);
        	boolean animateToZoomBoundary = false;
        	float targetZoom = normalizedScale;
        	if (normalizedScale > maxScale) {
        		targetZoom = maxScale;
        		animateToZoomBoundary = true;
        		
        	} else if (normalizedScale < minScale) {
        		targetZoom = minScale;
        		animateToZoomBoundary = true;
        	}
        	
        	if (animateToZoomBoundary) {
	        	DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, viewWidth / 2, viewHeight / 2, true);
	        	compatPostOnAnimation(doubleTap);
        	}
        }
    }
    
    private void scaleImage(double deltaScale, float focusX, float focusY, boolean stretchImageToSuper) {
    	
    	float lowerScale, upperScale;
    	if (stretchImageToSuper) {
    		lowerScale = superMinScale;
    		upperScale = superMaxScale;
    		
    	} else {
    		lowerScale = minScale;
    		upperScale = maxScale;
    	}
    	//此处是缩放边界控制，如果normalizedScale已经到了大于设置最大扩大倍数，让normalizedScale一直等于最大
    	float origScale = normalizedScale;
        normalizedScale *= deltaScale;
        if (normalizedScale > upperScale) {
            normalizedScale = upperScale;
            deltaScale = upperScale / origScale;
        } else if (normalizedScale < lowerScale) {
            normalizedScale = lowerScale;
            deltaScale = lowerScale / origScale;
        }
        
        matrix.postScale((float) deltaScale, (float) deltaScale, focusX, focusY);
        fixScaleTrans();
    }
    
    /**
     * DoubleTapZoom calls a series of runnables which apply
     * an animated zoom in/out graphic to the image.
     * @author Ortiz
     *双tapzoom调用了一系列的runnables，它将动画用到图像缩放中。
     */
    private class DoubleTapZoom implements Runnable {
    	
    	private long startTime;
    	private static final float ZOOM_TIME = 500;
    	private float startZoom, targetZoom;
    	private float bitmapX, bitmapY;
    	private boolean stretchImageToSuper;
        //在动画开始与结束的地方速率改变比较慢，在中间的时候加速  加减速插值器
    	private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
    	private PointF startTouch;
    	private PointF endTouch;

    	DoubleTapZoom(float targetZoom, float focusX, float focusY, boolean stretchImageToSuper) {
    		setState(State.ANIMATE_ZOOM);
    		startTime = System.currentTimeMillis();
    		this.startZoom = normalizedScale;
    		this.targetZoom = targetZoom;
    		this.stretchImageToSuper = stretchImageToSuper;
    		PointF bitmapPoint = transformCoordTouchToBitmap(focusX, focusY, false);
    		this.bitmapX = bitmapPoint.x;
    		this.bitmapY = bitmapPoint.y;
            Log.d("cao-hao","focusX:" + focusX + ";" + "focusY:" + focusY );
            Log.d("cao-hao","bitmapX:" + bitmapX + ";" + "bitmapY:" + bitmapY);

            //
    		// Used for translating image during scaling
    		//
    		startTouch = transformCoordBitmapToTouch(bitmapX, bitmapY);
    		endTouch = new PointF(viewWidth / 2, viewHeight / 2);
            Log.d("cao-hao","startTouch.x:" + startTouch.x+ ";" + "startTouch.y:" + startTouch.y);
            Log.d("cao-hao","endTouch.x:" + endTouch.x+ ";" + "endTouch.y:" + endTouch.y);

        }

		@Override
		public void run() {
			float t = interpolate();
			double deltaScale = calculateDeltaScale(t);
            Log.d("cao-hao","t:" + t + ";" + "deltaScale:" + deltaScale);
			scaleImage(deltaScale, bitmapX, bitmapY, stretchImageToSuper);
			translateImageToCenterTouchPosition(t);
			fixScaleTrans();
			setImageMatrix(matrix);
			
			//
			// OnTouchImageViewListener is set: double tap runnable updates listener
			// with every frame.
			//
			if (touchImageViewListener != null) {
				touchImageViewListener.onMove();
			}
			
			if (t < 1f) {
				//
				// We haven't finished zooming
				//
				compatPostOnAnimation(this);
				
			} else {
				//
				// Finished zooming
				//
				setState(State.NONE);
			}
		}
		
		/**
		 * Interpolate between where the image should start and end in order to translate
		 * the image so that the point that is touched is what ends up centered at the end
		 * of the zoom.
		 * @param t
		 */
		private void translateImageToCenterTouchPosition(float t) {
			float targetX = startTouch.x + t * (endTouch.x - startTouch.x);
			float targetY = startTouch.y + t * (endTouch.y - startTouch.y);
			PointF curr = transformCoordBitmapToTouch(bitmapX, bitmapY);
			matrix.postTranslate(targetX - curr.x, targetY - curr.y);
		}
		
		/**
		 * Use interpolator to get t
		 * @return
		 */
		private float interpolate() {
			long currTime = System.currentTimeMillis();
            //动画在500毫秒执行完，当currTime - startTime>500 时elapsed会返回1f 动画1028行会finish
			float elapsed = (currTime - startTime) / ZOOM_TIME;
			elapsed = Math.min(1f, elapsed);
            //用处:比如你自己定义一个动画,用线程不断的刷新让一个东西旋转或者移动,你就可以用这个函数把输入
            // 的值变化一下再给真正处理动画刷新的函数,这样就可以看到加速和减速等效果.
			return interpolator.getInterpolation(elapsed);//根据时间流逝的百分比来计算当前属性值改变的百分比 0-1之间
		}
		
		/** 计算缩放比，类似于估值器，由插值器计算出的百分比算出改变后的属性，也就是随着时间流逝在当前需要缩放的比例
		 * Interpolate the current targeted zoom and get the delta
		 * from the current zoom.
		 * @param t
		 * @return
		 */
		private double calculateDeltaScale(float t) {
			double zoom = startZoom + t * (targetZoom - startZoom);
			return zoom / normalizedScale;
		}
    }
    
    /**这个函数将把触摸事件中的坐标转换为图片点击坐标。
     * This function will transform the coordinates in the touch event to the coordinate 
     * system of the drawable that the imageview contain
     * @param x x-coordinate of touch event
     * @param y y-coordinate of touch event
     * @param clipToBitmap
     * @return Coordinates of the point touched, in the cooTouch event may occur within view, but outside image content. True, to clip return value
     * 			to the bounds of the bitmap size.rdinate system of the original drawable.
     */
    private PointF transformCoordTouchToBitmap(float x, float y, boolean clipToBitmap) {
         matrix.getValues(m);
         float origW = getDrawable().getIntrinsicWidth();
         float origH = getDrawable().getIntrinsicHeight();
         float transX = m[Matrix.MTRANS_X];
         float transY = m[Matrix.MTRANS_Y];
         float finalX = ((x - transX) * origW) / getImageWidth();
         float finalY = ((y - transY) * origH) / getImageHeight();
         
         if (clipToBitmap) {
        	 finalX = Math.min(Math.max(finalX, 0), origW);
        	 finalY = Math.min(Math.max(finalY, 0), origH);
         }
         
         return new PointF(finalX , finalY);
    }
    
    /**
     * Inverse of transformCoordTouchToBitmap. This function will transform the coordinates in the
     * drawable's coordinate system to the view's coordinate system.
     * @param bx x-coordinate in original bitmap coordinate system
     * @param by y-coordinate in original bitmap coordinate system
     * @return Coordinates of the point in the view's coordinate system.
     */
    private PointF transformCoordBitmapToTouch(float bx, float by) {
        matrix.getValues(m);        
        float origW = getDrawable().getIntrinsicWidth();
        float origH = getDrawable().getIntrinsicHeight();
        float px = bx / origW;
        float py = by / origH;
        float finalX = m[Matrix.MTRANS_X] + getImageWidth() * px;
        float finalY = m[Matrix.MTRANS_Y] + getImageHeight() * py;
        return new PointF(finalX , finalY);
    }
    
    /**
     * Fling launches sequential runnables which apply
     * the fling graphic to the image. The values for the translation
     * are interpolated by the Scroller.
     * @author Ortiz
     *
     */
    private class Fling implements Runnable {
    	
        CompatScroller scroller;
    	int currX, currY;
    	
    	Fling(int velocityX, int velocityY) {
    		setState(State.FLING);
    		scroller = new CompatScroller(context);
    		matrix.getValues(m);
    		
    		int startX = (int) m[Matrix.MTRANS_X];
    		int startY = (int) m[Matrix.MTRANS_Y];
    		int minX, maxX, minY, maxY;
    		
    		if (getImageWidth() > viewWidth) {
    			minX = viewWidth - (int) getImageWidth();
    			maxX = 0;
    			
    		} else {
    			minX = maxX = startX;
    		}
    		
    		if (getImageHeight() > viewHeight) {
    			minY = viewHeight - (int) getImageHeight();
    			maxY = 0;
    			
    		} else {
    			minY = maxY = startY;
    		}
    		
    		scroller.fling(startX, startY, (int) velocityX, (int) velocityY, minX,
                    maxX, minY, maxY);
    		currX = startX;
    		currY = startY;
    	}
    	
    	public void cancelFling() {
    		if (scroller != null) {
    			setState(State.NONE);
    			scroller.forceFinished(true);
    		}
    	}
    	
		@Override
		public void run() {
			
			//
			// OnTouchImageViewListener is set: TouchImageView listener has been flung by user.
			// Listener runnable updated with each frame of fling animation.
			//
			if (touchImageViewListener != null) {
				touchImageViewListener.onMove();
			}
			
			if (scroller.isFinished()) {
        		scroller = null;
        		return;
        	}
			
			if (scroller.computeScrollOffset()) {
	        	int newX = scroller.getCurrX();
	            int newY = scroller.getCurrY();
	            int transX = newX - currX;
	            int transY = newY - currY;
	            currX = newX;
	            currY = newY;
	            matrix.postTranslate(transX, transY);
	            fixTrans();
	            setImageMatrix(matrix);
	            compatPostOnAnimation(this);
        	}
		}
    }
    
    @TargetApi(VERSION_CODES.GINGERBREAD)
	private class CompatScroller {
    	Scroller scroller;
    	OverScroller overScroller;
    	boolean isPreGingerbread;
    	
    	public CompatScroller(Context context) {
    		if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD) {
    			isPreGingerbread = true;
    			scroller = new Scroller(context);
    			
    		} else {
    			isPreGingerbread = false;
    			overScroller = new OverScroller(context);
    		}
    	}
    	
    	public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
    		if (isPreGingerbread) {
    			scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    		} else {
    			overScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    		}
    	}
    	
    	public void forceFinished(boolean finished) {
    		if (isPreGingerbread) {
    			scroller.forceFinished(finished);
    		} else {
    			overScroller.forceFinished(finished);
    		}
    	}
    	
    	public boolean isFinished() {
    		if (isPreGingerbread) {
    			return scroller.isFinished();
    		} else {
    			return overScroller.isFinished();
    		}
    	}
    	
    	public boolean computeScrollOffset() {
    		if (isPreGingerbread) {
    			return scroller.computeScrollOffset();
    		} else {
    			overScroller.computeScrollOffset();
    			return overScroller.computeScrollOffset();
    		}
    	}
    	
    	public int getCurrX() {
    		if (isPreGingerbread) {
    			return scroller.getCurrX();
    		} else {
    			return overScroller.getCurrX();
    		}
    	}
    	
    	public int getCurrY() {
    		if (isPreGingerbread) {
    			return scroller.getCurrY();
    		} else {
    			return overScroller.getCurrY();
    		}
    	}
    }
    
    @TargetApi(VERSION_CODES.JELLY_BEAN)
	private void compatPostOnAnimation(Runnable runnable) {
    	if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            postOnAnimation(runnable);
            Log.d("cao-hao","postOnAnimation");
            
        } else {
            postDelayed(runnable, 1000/60);
        }
    }
    
    private class ZoomVariables {
    	public float scale;
    	public float focusX;
    	public float focusY;
    	public ScaleType scaleType;
    	
    	public ZoomVariables(float scale, float focusX, float focusY, ScaleType scaleType) {
    		this.scale = scale;
    		this.focusX = focusX;
    		this.focusY = focusY;
    		this.scaleType = scaleType;
    	}
    }
    
    private void printMatrixInfo() {
    	float[] n = new float[9];
    	matrix.getValues(n);
    	Log.d(DEBUG, "Scale: " + n[Matrix.MSCALE_X] + " TransX: " + n[Matrix.MTRANS_X] + " TransY: " + n[Matrix.MTRANS_Y]);
    }
}