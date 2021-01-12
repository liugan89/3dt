package com.takumi.wms.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.utils.DateUtil;

import java.util.List;

/**
 * 未完成的单子列表适配器
 */
public class ShipmentDocumentAdapter extends RecyclerView.Adapter<ShipmentDocumentAdapter.ViewHolder> {

    private List<ShipmentDetail> shipmentDetails;//单子列表
    private OnItemClickListener mItemClickListener, mRelationClickListener;
    private Function fun;

    public ShipmentDocumentAdapter(List<ShipmentDetail> shipmentDetails, Function fun) {
        this.shipmentDetails = shipmentDetails;
        this.fun = fun;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_undone_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.odd_item_number.setText(shipmentDetails.get(position).getShipmentId() + "");
        holder.odd_item_time.setText(DateUtil.toDateTime(shipmentDetails.get(position).getCreatedAt().getTime()));
        holder.itemView.setTag(position);
        holder.v1.setText(fun.getName() + "单号：");
        if (fun == Function.OutgoingShipment) {
            holder.odd_item_notice.setText(shipmentDetails.get(position).getPrimaryShipGroupSeqId());
        } else {
            holder.ll_notice.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick((Integer) v.getTag());
                }
            }
        });
        if (!TextUtils.isEmpty(shipmentDetails.get(position).getPrimaryOrderId())) {
            holder.tv_relation.setText("已关联");
        } else {
            holder.tv_relation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRelationClickListener != null) {
                        mRelationClickListener.onItemClick((Integer) holder.itemView.getTag());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDetails == null ? 0 : shipmentDetails.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setRelationClickListener(OnItemClickListener mRelationClickListener) {
        this.mRelationClickListener = mRelationClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView odd_item_number, odd_item_time, v1, tv_relation, odd_item_notice;
        LinearLayout ll_notice;

        public ViewHolder(View itemView) {
            super(itemView);
            odd_item_number = itemView.findViewById(R.id.odd_item_number);
            odd_item_time = itemView.findViewById(R.id.odd_item_time);
            v1 = itemView.findViewById(R.id.v1);
            tv_relation = itemView.findViewById(R.id.tv_relation);
            ll_notice = itemView.findViewById(R.id.ll_notice);
            odd_item_notice = itemView.findViewById(R.id.odd_item_notice);
        }
    }


}
