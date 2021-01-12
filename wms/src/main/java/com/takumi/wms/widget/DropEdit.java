package com.takumi.wms.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.RVItemClick;

import java.util.List;

public class DropEdit extends EditText {

    private int w;


    public DropEdit(Context context) {
        super(context);
        initWidth();
    }

    public DropEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidth();
    }

    public DropEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidth();
    }

    private void initWidth() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                w = getWidth();
            }
        });
    }

    private PopupWindow pop;

    public boolean isTextChange = true;

    public void setTextNoWatch(String s) {
        isTextChange = false;
        setText(s);
        isTextChange = true;
    }

    public void showDrop(List<String> ids, RVItemClick click) {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
        RecyclerView rv = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.drop_recycler, null);
        rv.setAdapter(new DropAdapter(ids, click));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        if (llm.getItemCount() < 5) {
            pop = new PopupWindow(rv, w, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            pop = new PopupWindow(rv, w, 600);
        }
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);

        pop.showAsDropDown(this);
    }

    public void dismissDrop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    class DropAdapter extends RecyclerView.Adapter<DropAdapter.DropHoder> {
        private List<String> ids;
        private RVItemClick click;

        public DropAdapter(List<String> ids, RVItemClick click) {
            this.ids = ids;
            this.click = click;
        }

        @Override
        public DropHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            return new DropHoder(v);
        }

        @Override
        public void onBindViewHolder(DropHoder holder, int position) {
            holder.text1.setText(ids.get(position));
            holder.text1.setOnClickListener((v -> {
                click.itemClick(position);
            }));
        }

        @Override
        public int getItemCount() {
            return ids == null ? 0 : ids.size();
        }

        class DropHoder extends RecyclerView.ViewHolder {

            TextView text1;

            public DropHoder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
            }
        }

    }


}
