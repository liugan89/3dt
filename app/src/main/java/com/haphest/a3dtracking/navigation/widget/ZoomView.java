package com.haphest.a3dtracking.navigation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class ZoomView extends View {
    // 属性变量
    public float translationX; // 移动X
    public float translationY; // 移动Y
    public float scale = 1; // 伸缩比例
    public float rotation; // 旋转角度

    // 移动过程中临时变量
    public float actionX;
    public float actionY;
    public float spacing;
    public float degree;
    public int moveType; // 0=未选择，1=拖动，2=缩放

    public float tScale = 1;//用于判断缩放比例是否小于1，不能小于1
    public float tTranslationX;
    public float tTranslationY;

    public ZoomView(Context context) {
        this(context, null);
    }

    public ZoomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveType = 1;
                actionX = event.getRawX();
                actionY = event.getRawY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                moveType = 2;
                spacing = getSpacing(event);
                degree = getDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (moveType == 1) {
                    tTranslationX = translationX + event.getRawX() - actionX;
                    tTranslationY = translationY + event.getRawY() - actionY;
                    float w = getMeasuredWidth() * scale / 2;
                    float h = getMeasuredHeight() * scale / 2;
                    if (Math.abs(tTranslationX) < w) {
                        translationX = tTranslationX;
                        setTranslationX(translationX);
                    }
                    if (Math.abs(tTranslationY) < h) {
                        translationY = tTranslationY;
                        setTranslationY(translationY);
                    }
                    actionX = event.getRawX();
                    actionY = event.getRawY();
                } else if (moveType == 2) {
                    tScale = scale * getSpacing(event) / spacing;
                    if (tScale >= 1 && tScale <= 5) {
                        scale = tScale;
                        setScaleX(scale);
                        setScaleY(scale);
                    }
                    rotation = rotation + getDegree(event) - degree;
                    if (rotation > 360) {
                        rotation = rotation - 360;
                    }
                    if (rotation < -360) {
                        rotation = rotation + 360;
                    }
                    setRotation(rotation);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                moveType = 0;
                break;
        }
        return super.onTouchEvent(event);
    }

    // 触碰两点间距离
    private float getSpacing(MotionEvent event) {
        //通过三角函数得到两点间的距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 取旋转角度
    private float getDegree(MotionEvent event) {
        //得到两个手指间的旋转角度
        double delta_x = event.getX(0) - event.getX(1);
        double delta_y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}
