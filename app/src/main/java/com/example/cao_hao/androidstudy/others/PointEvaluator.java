package com.example.cao_hao.androidstudy.others;

import android.animation.TypeEvaluator;

import com.example.cao_hao.androidstudy.bean.Point;

/**
 * Created by cao-hao on 17-9-6.
 */

public class PointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        Point point = new Point(x, y);
        return point;
    }
}
