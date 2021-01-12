package com.takumi.wms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.takumi.wms.model.SingletonCache;

import java.util.List;

/**
 * Created by Administrator on 2019/6/2/002.
 */

public class StringSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> ss;

    public StringSpinnerAdapter(Context context, List<String> ss) {
        this.context = context;
        this.ss = ss;
    }

    @Override
    public int getCount() {
        return ss == null ? 0 : ss.size();
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
        tv.setText(ss.get(position));
        return convertView;
    }
}
