package com.takumi.wms.adapter.expand;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.InOutDocument;

/**
 * Created by hbh on 2017/4/20.
 * 子布局ViewHolder
 */

public class ChildViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;
    private TextView inout_item_1;
    private TextView inout_item_2;
    private TextView inout_item_3;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final InOutDocument.LinesBean dataBean, final int pos) {

        inout_item_1 = (TextView) view.findViewById(R.id.inout_item_1);
        inout_item_2 = (TextView) view.findViewById(R.id.inout_item_2);
        inout_item_3 = (TextView) view.findViewById(R.id.inout_item_3);
        inout_item_1.setText(dataBean.getLocatorId());
        inout_item_2.setText(dataBean.getProductId());
        inout_item_3.setText(dataBean.getAttributeSetInstanceId());
    }
}
