package com.takumi.wms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Function;
import com.takumi.wms.utils.AttrInflater;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 卡片式的行项View
 */
public class LineCardView extends RelativeLayout {

    @BindView(R.id.tv_shipment_label)
    TextView tv_shipment_label;//顶部序号
    @BindView(R.id.line_info)
    LinearLayout line_info;//产品属性详细信息
    @BindView(R.id.odd_list_item_productid)
    TextView odd_list_item_productid;//产品ID
    @BindView(R.id.line_quantity)
    TextView line_quantity;//产品数量
    @BindView(R.id.odd_list_item_prelocatorId)
    TextView odd_list_item_prelocatorId;
    @BindView(R.id.odd_list_item_targetlocatorId)
    TextView odd_list_item_targetlocatorId;
    @BindView(R.id.ll_pre)
    LinearLayout ll_pre;
    @BindView(R.id.ll_target)
    LinearLayout ll_target;

    int infoh;

    private LayoutInflater inflater;
    private LinearLayout InfoView;
    private float scale;


    public LineCardView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LineCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.inoutline_list_item, this);
        ButterKnife.bind(this);
        scale = context.getResources().getDisplayMetrics().density;
    }


    /**
     * 根据行项实体类设置显示样式
     */
    public void setLine(DocumentLine info, Function fun) {
        if (info != null) {
            odd_list_item_productid.setText(info.getProductId());
            line_quantity.setText(info.getCount() + "");
            switch (fun) {
                case In:
                    ll_pre.setVisibility(View.GONE);
                    odd_list_item_targetlocatorId.setText(info.getLocatorId());
                    break;
                case Out:
                    ll_target.setVisibility(View.GONE);
                    odd_list_item_prelocatorId.setText(info.getLocatorId());
                    break;
                case Movement:
                    odd_list_item_targetlocatorId.setText(info.getLocatorIdTo());
                    odd_list_item_prelocatorId.setText(info.getLocatorIdFrom());
                    break;
            }

            line_info.removeAllViews();


            if (info.getInventoryattribute() != null && !info.getInventoryattribute().isEmpty()) {
                for (Map.Entry<String, Object> m : info.getInventoryattribute().entrySet()) {
                    if (AttrInflater.FilterField(m)) {
                        LinearLayout InfoView = (LinearLayout) inflater.inflate(R.layout.linecardview_item, null);
                        TextView tv1 = InfoView.findViewById(R.id.product_title);
                        TextView tv2 = InfoView.findViewById(R.id.product_content);
                        tv2.setText(m.getValue() + "");
                        if (m.getKey().equals("serialNumber")) {
                            tv1.setText("卷号：");
                            line_info.addView(InfoView, 0);
                        } else {
                            tv1.setText(m.getKey() + "：");
                            line_info.addView(InfoView);
                        }
                    }

                }
            }
        }


    }

    public void setSeq(String productid) {
        tv_shipment_label.setText("产品编号：" + productid);
    }
}
