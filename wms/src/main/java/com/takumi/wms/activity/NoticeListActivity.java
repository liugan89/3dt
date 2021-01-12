package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.OnItemClickListener;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InOutNotice;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.DateUtil;
import com.takumi.wms.utils.ToastUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeListActivity extends BottomActivity {

    @BindView(R.id.et_notice_search)
    EditText et_notice_search;
    @BindView(R.id.rv_notice_undone)
    RecyclerView rv_notice_undone;
    @BindView(R.id.bt_unhandle)
    Button bt_unhandle;
    @BindView(R.id.bt_created)
    Button bt_created;
    @BindView(R.id.bt_outed)
    Button bt_outed;

    private List<InOutNotice> notices;
    private List<InOutNotice> ns;

    private String StatusId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        setTitle(fun.getName());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_notice_undone.setLayoutManager(llm);
        rv_notice_undone.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ns = new LinkedList<>();
        et_notice_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ns.clear();
                for (InOutNotice o : notices) {
                    if (o.getInOutNoticeId().contains(s.toString().toUpperCase())) {
                        ns.add(o);
                    }
                }
                initRV(ns);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_notice_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    findNotice(et_notice_search.getText().toString());
                }
                return false;
            }
        });
    }

    protected void selectButton(int id) {
        bt_unhandle.setSelected(false);
        bt_created.setSelected(false);
        bt_outed.setSelected(false);
        switch (id) {
            case R.id.bt_unhandle:
                bt_unhandle.setSelected(true);
                break;
            case R.id.bt_created:
                bt_created.setSelected(true);
                break;
            case R.id.bt_outed:
                bt_outed.setSelected(true);
                break;
        }
    }

    private void findNotice(String s) {
        if (TextUtils.isEmpty(s)) {
            ToastUtil.showToast(this, "请输入单号");
            return;
        }
        if (notices == null || notices.isEmpty()) {
            ToastUtil.showToast(this, "无通知单");
            return;
        }
        for (InOutNotice o : notices) {
            if (s.equals(o.getInOutNoticeId())) {
                goNext(o);
                return;
            }
        }
        ToastUtil.showToast(this, "未找到此装运单");
    }

    @OnClick({R.id.bt_unhandle, R.id.bt_created, R.id.bt_outed})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_unhandle:
                selectButton(R.id.bt_unhandle);
                StatusId = "NOTICE_CREATED";
                break;
            case R.id.bt_created:
                selectButton(R.id.bt_created);
                StatusId = "NOTICE_APPROVED";
                break;
            case R.id.bt_outed:
                selectButton(R.id.bt_outed);
                StatusId = "NOTICE_COMPLETED";
                break;
        }
        getDocs();
    }

    private void goNext(InOutNotice o) {
        Intent intent = new Intent(this, NoticeLineList2Activity.class);
        intent.putExtra(Constant.Flags.MainFlag, fun);
        intent.putExtra(Constant.Flags.Param1, o);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDocs();
    }

    private void getDocs() {
        et_notice_search.getText().clear();
        String id = NetUtil.makeId(true, NetService.InOutNotices);
        Map<String, String> map = new HashMap<>();
        map.put("sort", "-createdAt");
        if (!TextUtils.isEmpty(StatusId)) {
            map.put("StatusId", StatusId);
        }
        map.put("warehouseId", Warehouse.getHouse().getWarehouseId());
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getInOutNotices, new NetUtil.MyCallBack<List<InOutNotice>>() {
            @Override
            public void onSuccess(List<InOutNotice> result) {
                notices = result;
                initRV(notices);
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void initRV(List<InOutNotice> ns) {
        NoticeListAdapter adapter = new NoticeListAdapter(ns);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goNext(ns.get(position));
            }
        });
        rv_notice_undone.setAdapter(adapter);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_notice_list2;
    }

    class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder> {

        private List<InOutNotice> ns;
        private OnItemClickListener mItemClickListener, mRelationClickListener;

        public NoticeListAdapter(List<InOutNotice> ns) {
            this.ns = ns;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_undone_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.v1.setText("出库单通知号:");
            holder.odd_item_number.setText(ns.get(position).getInOutNoticeId());
            holder.odd_item_time.setText(DateUtil.toDateTime(ns.get(position).getCreatedAt().getTime()));
            holder.itemView.setTag(position);
            holder.ll_notice.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick((Integer) v.getTag());
                    }
                }
            });
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }

        @Override
        public int getItemCount() {
            return ns == null ? 0 : ns.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.odd_item_number)
            TextView odd_item_number;
            @BindView(R.id.odd_item_time)
            TextView odd_item_time;
            @BindView(R.id.v1)
            TextView v1;
            @BindView(R.id.ll_notice)
            LinearLayout ll_notice;
            @BindView(R.id.odd_item_notice)
            TextView odd_item_notice;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
