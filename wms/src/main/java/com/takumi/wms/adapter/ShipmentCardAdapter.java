package com.takumi.wms.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.DocumentLine;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipmentCardAdapter extends RecyclerView.Adapter<ShipmentCardAdapter.ViewHolder> {
    private List<DocumentLine> ls;
    private LayoutInflater inflater;
    private RVItemClick click;
    private RVItemClick deleteClick;

    public ShipmentCardAdapter(List<DocumentLine> ls) {
        this.ls = ls;
    }

    public void setClick(RVItemClick click) {
        this.click = click;
    }

    public void setDeleteClick(RVItemClick deleteClick) {
        this.deleteClick = deleteClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_shipment_card, null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ll_shipmentline_content.removeAllViews();
        holder.tv_shipment_label.setText("产品编号：" + ls.get(position).getProductId());
        holder.ll_shipmentin_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.itemClick(position);
            }
        });

        holder.rl_shipmentout_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClick.itemClick(position);
            }
        });
        /*if (!TextUtils.isEmpty(ls.get(position).getProductId())) {
            createView("产品ID：", ls.get(position).getProductId(), holder, position);
        }
        if (!TextUtils.isEmpty(ls.get(position).getProductName())) {
            createView("产品名称：", ls.get(position).getProductName(), holder, position);
        }*/
        /*if (ls.get(position).getInventoryattribute() != null) {
            if (!TextUtils.isEmpty(ls.get(position).getInventoryattribute().get("serialNumber") + "")) {
                createView("卷号：", ls.get(position).getInventoryattribute().get("serialNumber") + "", holder, position);
            }
        }
        createView("重量：", ls.get(position).getAcceptedCount() + "", holder, position);*/
        if (ls.get(position).getInventoryattribute() != null) {
            String o = (String) ls.get(position).getInventoryattribute().get("serialNumber");
            if (!TextUtils.isEmpty(o)) {
                holder.tv_shipitem_number.setText(o);
            }
        }
        holder.tv_shipitem_count.setText(ls.get(position).getAcceptedCount() + "");
    }

    private void createView(String key, String value, ViewHolder holder, int position) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.product_item_unedit, null);
        TextView tv1 = ll.findViewById(R.id.product_title);
        TextView tv2 = ll.findViewById(R.id.product_content);
        tv1.setText(key);
        tv2.setText(value);
        tv2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        holder.ll_shipmentline_content.addView(ll);
    }

    @Override
    public int getItemCount() {
        return ls == null ? 0 : ls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_shipment_label)
        TextView tv_shipment_label;
        @BindView(R.id.ll_shipmentline_content)
        LinearLayout ll_shipmentline_content;
        @BindView(R.id.tv_shipitem_number)
        TextView tv_shipitem_number;
        @BindView(R.id.tv_shipitem_count)
        TextView tv_shipitem_count;
        @BindView(R.id.ll_shipmentin_item)
        LinearLayout ll_shipmentin_item;
        @BindView(R.id.rl_shipmentout_item_delete)
        RelativeLayout rl_shipmentout_item_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
