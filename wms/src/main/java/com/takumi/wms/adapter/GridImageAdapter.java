package com.takumi.wms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.takumi.wms.R;
import com.takumi.wms.obj.OssFile;

import java.util.List;

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {

    private List<OssFile> images;
    private Context context;
    private RVItemClick click;
    private boolean photoable;
    private RequestOptions options;

    public GridImageAdapter(Context context, List<OssFile> images, boolean photoable) {
        this.images = images;
        this.context = context;
        this.photoable = photoable;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop(); // 磁盘缓存策略
        return holder;
    }

    public boolean isPhotoable() {
        return photoable;
    }

    public void setClick(RVItemClick click) {
        this.click = click;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == images.size()) {
            RequestOptions ro = new RequestOptions().centerInside();
            Glide.with(context).load(R.mipmap.photo).apply(ro).into(holder.iv_item);
        } else {
            Glide.with(context).load(images.get(position)).thumbnail(0.2f).apply(options).into(holder.iv_item);
        }
        holder.iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null) {
                    click.itemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoable ? images.size() + 1 : images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_item;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_item = itemView.findViewById(R.id.iv_item);
        }
    }
}
