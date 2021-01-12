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

public class LineGridImageAdapter extends RecyclerView.Adapter<LineGridImageAdapter.ViewHolder> {

    private List<OssFile> uploadImages;
    private List<OssFile> downloadImages;
    private Context context;
    private RVLineItemClick click;
    private boolean photoable;

    public static final int Download = 0;
    public static final int Upload = 1;
    public static final int TakePhoto = 2;
    private RequestOptions options;

    public LineGridImageAdapter(Context context, List<OssFile> downloadImages, List<OssFile> uploadImages, boolean photoable) {
        this.uploadImages = uploadImages;
        this.downloadImages = downloadImages;
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

    public void setClick(RVLineItemClick click) {
        this.click = click;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == uploadImages.size() + downloadImages.size()) {
            RequestOptions ro = new RequestOptions().centerInside();
            Glide.with(context).load(R.mipmap.photo).apply(ro).into(holder.iv_item);
            holder.iv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click != null) {
                        click.itemClick(position, TakePhoto);
                    }
                }
            });
        } else if (position < downloadImages.size()) {
            Glide.with(context).load(downloadImages.get(position)).apply(options).thumbnail(0.2f).into(holder.iv_item);
            holder.iv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click != null) {
                        click.itemClick(position, Download);
                    }
                }
            });
        } else {
            Glide.with(context).load(uploadImages.get(position - downloadImages.size())).apply(options).thumbnail(0.2f).into(holder.iv_item);
            holder.iv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click != null) {
                        click.itemClick(position, Upload);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return photoable ? uploadImages.size() + downloadImages.size() + 1 : uploadImages.size() + downloadImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_item;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_item = itemView.findViewById(R.id.iv_item);
        }
    }

    public interface RVLineItemClick {
        void itemClick(int position, int flag);
    }

}
