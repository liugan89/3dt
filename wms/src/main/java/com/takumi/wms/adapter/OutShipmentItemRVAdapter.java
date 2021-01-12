package com.takumi.wms.adapter;

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

public class OutShipmentItemRVAdapter extends RecyclerView.Adapter<OutShipmentItemRVAdapter.Holder> {

    private List<DocumentLine> lines;
    private RVItemClick itemClick;
    private RVItemClick deleteClick;

    public OutShipmentItemRVAdapter(List<DocumentLine> lines) {

        this.lines = lines;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.outship_item_item, parent, false));
    }

    public void setItemClick(RVItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setDeleteClick(RVItemClick deleteClick) {
        this.deleteClick = deleteClick;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (lines.get(position).getInventoryattribute() != null) {
            String o = (String) lines.get(position).getInventoryattribute().get("serialNumber");
            if (!TextUtils.isEmpty(o)) {
                holder.tv_shipitem_number.setText(o);
            }
        }
        holder.tv_shipitem_count.setText(lines.get(position).getCount() + "");
        holder.ll_swipe_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(position);
            }
        });
        holder.rl_shipmentout_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClick.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lines == null ? 0 : lines.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_shipitem_number;
        TextView tv_shipitem_count;
        RelativeLayout rl_shipmentout_item_delete;
        LinearLayout ll_swipe_content;

        public Holder(View itemView) {
            super(itemView);
            tv_shipitem_number = itemView.findViewById(R.id.tv_shipitem_number);
            tv_shipitem_count = itemView.findViewById(R.id.tv_shipitem_count);
            rl_shipmentout_item_delete = itemView.findViewById(R.id.rl_shipmentout_item_delete);
            ll_swipe_content = itemView.findViewById(R.id.ll_swipe_content);

        }
    }
}
