package com.takumi.wms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Function;
import com.takumi.wms.widget.LineCardView;

import java.util.List;

public class LineListAdapter extends RecyclerView.Adapter<LineListAdapter.ViewHolder> {

    private List<DocumentLine> infos;
    private RVItemClick click;
    private Function fun;

    public LineListAdapter(List<DocumentLine> infos, Function fun) {
        this.infos = infos;
        this.fun = fun;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LineCardView lcv = new LineCardView(parent.getContext());//使用自定义控件
        return new ViewHolder(lcv);
    }

    public void setRvItemClick(RVItemClick click) {
        this.click = click;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (infos != null) {
            LineCardView lcv = (LineCardView) holder.itemView;
            lcv.setTag(position);
            lcv.setLine(infos.get(position), fun);
            lcv.setSeq(infos.get(position).getProductId());
            lcv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.itemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return infos == null ? 0 : infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
