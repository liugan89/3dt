package com.takumi.wms.base;

import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Warehouse;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 包含底部用户和仓库的Activity
 */
public abstract class BottomActivity extends BaseActivity {
    @BindView(R.id.bottom_user)
    public TextView bottom_user;
    @BindView(R.id.bottom_warehouse)
    public TextView bottom_warehouse;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    protected Function fun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottom_user != null || bottom_warehouse != null) {
            bottom_user.setText(Operator.getOperator().getAccount());
            bottom_warehouse.setText(Warehouse.getHouse().getWarehouseId());
        }
    }

    public void isBottomShow(boolean isShow) {
        if (isShow) {
            rl_bottom.setVisibility(View.VISIBLE);
        } else {
            rl_bottom.setVisibility(View.GONE);
        }
    }

    protected void setTitle(String title) {
        tv_title.setText(title);
    }

    protected void setIvBackVisibility(boolean isVisible) {
        if (isVisible) {
            iv_back.setVisibility(View.VISIBLE);
        } else {
            iv_back.setVisibility(View.GONE);
        }
    }

    protected void setBarShadow(boolean has) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (has) {

                rl_title.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));

            } else {
                rl_title.setElevation(0f);
            }
        }
    }

    @OnClick({R.id.iv_back})
    void iv_back_click() {
        finish();
    }
}
