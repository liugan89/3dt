package com.takumi.wms.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TouchLinear extends LinearLayout {

    public TouchLinear(Context context) {
        super(context);
    }

    public TouchLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLinear(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
