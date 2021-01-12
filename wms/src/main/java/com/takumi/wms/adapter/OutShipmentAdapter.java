package com.takumi.wms.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.OutDocumentInfo;
import com.takumi.wms.utils.AttrInflater;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class OutShipmentAdapter extends RecyclerView.Adapter<OutShipmentAdapter.Holder> {
    private List<OutDocumentInfo.DocumentLinesItem> ls;
    private LayoutInflater inflater;
    private RVItemClick itemClick;
    private Context context;
    private RVItemItemClick itemItemClick;
    private RVItemItemClick itemDeleteClick;
    private List<Integer> expands;

    public OutShipmentAdapter(List<Integer> expands, List<OutDocumentInfo.DocumentLinesItem> ls) {
        this.expands = expands;
        this.ls = ls;
    }


    public void setItemClick(RVItemClick click) {
        this.itemClick = click;
    }

    public void setItemItemClick(RVItemItemClick itemItemClick) {
        this.itemItemClick = itemItemClick;
    }

    public void setItemDeleteClick(RVItemItemClick itemDeleteClick) {
        this.itemDeleteClick = itemDeleteClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.ship_out_item, null);
        Holder holder = new Holder(v);
        return holder;
    }

    public int getInfoItemSize(int position) {
        return ls.get(position).getItemissuance() == null ? 0 : ls.get(position).getItemissuance().size();
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        OutDocumentInfo.DocumentLinesItem b = ls.get(position);
        List<DocumentLine> ds = b.getItemissuance();
        BigDecimal count = new BigDecimal(0);
        int n = 0;
        if (ds != null) {
            for (DocumentLine d : ds) {
                count = count.add(d.getCount());
                n++;
            }
        }
        holder.tv_receive_count.setText(n + "");
        holder.tv_receive_weight.setText(new DecimalFormat("0.###").format(count));
        holder.tv_shipment_label.setText("产品编号：" + b.getProductIdItem());
        holder.tv_shipitem_number.setText(b.getPoNumberItem());
        holder.tv_shipitem_count.setText(new DecimalFormat("0.###").format(b.getQuantityItem()));
        new AttrInflater(b.getProductAttributeDto(), context, holder.ll_product_info);
        if (expands.contains(position)) {
            holder.ll_expand.setVisibility(View.VISIBLE);
            holder.rv_item.setVisibility(View.VISIBLE);
        }
        holder.iv_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ll_expand.getVisibility() == View.VISIBLE) {
                    holder.ll_expand.setVisibility(View.GONE);
                    holder.rv_item.setVisibility(View.GONE);
                    expands.remove(new Integer(position));
                } else {
                    holder.ll_expand.setVisibility(View.VISIBLE);
                    holder.rv_item.setVisibility(View.VISIBLE);
                    expands.add(new Integer(position));
                }
            }
        });
        holder.bt_addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.itemClick(position);
                }
            }
        });
        OutShipmentItemRVAdapter adapter = new OutShipmentItemRVAdapter(ds);
        adapter.setDeleteClick(new RVItemClick() {
            @Override
            public void itemClick(int p) {
                itemDeleteClick.itemClick(position, p);
            }
        });
        adapter.setItemClick(new RVItemClick() {
            @Override
            public void itemClick(int p) {
                itemItemClick.itemClick(position, p);
            }
        });
        holder.rv_item.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return ls == null ? 0 : ls.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_shipment_label;
        TextView tv_shipitem_number;
        TextView tv_shipitem_count;
        ImageView iv_pull;
        LinearLayout ll_expand;
        LinearLayout ll_product_info;
        RecyclerView rv_item;
        Button bt_addLine;
        TextView tv_receive_count;
        TextView tv_receive_weight;

        public Holder(View itemView) {
            super(itemView);
            tv_shipment_label = itemView.findViewById(R.id.tv_shipment_label);
            tv_shipitem_number = itemView.findViewById(R.id.tv_shipitem_number);
            tv_shipitem_count = itemView.findViewById(R.id.tv_shipitem_count);
            iv_pull = itemView.findViewById(R.id.iv_pull);
            ll_expand = itemView.findViewById(R.id.ll_expand);
            ll_product_info = itemView.findViewById(R.id.ll_product_info);
            rv_item = itemView.findViewById(R.id.rv_item);
            bt_addLine = itemView.findViewById(R.id.bt_addLine);
            tv_receive_count = itemView.findViewById(R.id.tv_receive_count);
            tv_receive_weight = itemView.findViewById(R.id.tv_receive_weight);
            rv_item.setLayoutManager(new LinearLayoutManager(context));
        }
    }


}
