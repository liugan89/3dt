package com.takumi.wms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.utils.DateUtil;

import java.util.List;

/**
 * 未完成的单子列表适配器
 */
public class InOutDocumentAdapter extends RecyclerView.Adapter<InOutDocumentAdapter.ViewHolder> implements View.OnClickListener {

    private List<InOutDocument> inOutDocuments;//单子列表
    private OnItemClickListener mItemClickListener;
    private ViewHolder holder;
    private Function fun;

    public InOutDocumentAdapter(List<InOutDocument> inOutDocuments, Function fun) {
        this.inOutDocuments = inOutDocuments;
        this.fun = fun;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inout_item, parent, false);
        holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.odd_item_number.setText(inOutDocuments.get(position).getDocumentNumber() + "");
        holder.odd_item_time.setText(DateUtil.toDateTime(inOutDocuments.get(position).getCreatedAt().getTime()));
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        holder.v1.setText(fun.getName() + "单号：");
    }

    @Override
    public int getItemCount() {
        return inOutDocuments == null ? 0 : inOutDocuments.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView odd_item_number, odd_item_time, v1;

        public ViewHolder(View itemView) {
            super(itemView);
            odd_item_number = itemView.findViewById(R.id.odd_item_number);
            odd_item_time = itemView.findViewById(R.id.odd_item_time);
            v1 = itemView.findViewById(R.id.v1);
        }
    }


}
