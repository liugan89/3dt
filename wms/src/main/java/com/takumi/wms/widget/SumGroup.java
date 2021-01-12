package com.takumi.wms.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.takumi.wms.R;

public class SumGroup extends LinearLayout {
    private int count;
    private LinearLayout ll1;
    private LinearLayout ll2;

    public SumGroup(Context context) {
        this(context, null);
    }

    public SumGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SumGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.sum_group, this);
        ll1 = ll.findViewById(R.id.ll1);
        ll2 = ll.findViewById(R.id.ll2);
    }

    @Override
    public void removeAllViews() {
        ll1.removeAllViews();
        ll2.removeAllViews();
        count = 0;
    }

    @Override
    public void addView(View child) {
        count++;
        if (count % 2 == 1) {
            ll1.addView(child);
        } else {
            ll2.addView(child);
        }
    }
}
