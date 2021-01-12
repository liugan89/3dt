package com.takumi.wms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.takumi.wms.R;

public class BottomLayout extends RelativeLayout {
    public BottomLayout(Context context) {
        this(context, null);
    }

    public BottomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.bottom_layout, this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
