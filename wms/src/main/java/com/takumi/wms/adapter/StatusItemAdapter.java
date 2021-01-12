package com.takumi.wms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.takumi.wms.model.SingletonCache;

public class StatusItemAdapter extends BaseAdapter {

    private Context context;

    public StatusItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return SingletonCache.statusItems == null ? 0 : SingletonCache.statusItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, null);
            tv = convertView.findViewById(android.R.id.text1);
            convertView.setTag(tv);
        } else {
            tv = (TextView) convertView.getTag();
        }
        tv.setText(SingletonCache.statusItems.get(position).getDescription());
        return convertView;
    }
}
