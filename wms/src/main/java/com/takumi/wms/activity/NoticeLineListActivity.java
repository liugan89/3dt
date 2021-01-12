package com.takumi.wms.activity;

import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.NoticeDocument;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.OrderShipGroup;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.dto.order.OrderCommandDtos;
import com.takumi.wms.model.dto.order.OrderShipGroupId;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.DropEdit;
import com.takumi.wms.widget.LotDialog;
import com.takumi.wms.widget.ProductDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/8/27/027.
 */

public class NoticeLineListActivity extends BottomActivity {

    @BindView(R.id.tv_notice_contractNo)
    TextView tv_notice_contractNo;
    @BindView(R.id.et_notice_id)
    DropEdit et_notice_id;
    @BindView(R.id.rv_notice_list)
    RecyclerView rv_notice_list;
    @BindView(R.id.ll_contract_number)
    LinearLayout ll_contract_number;
    @BindView(R.id.bt_notice_complete)
    Button bt_notice_complete;
    @BindView(R.id.ll_notice_shipment)
    LinearLayout ll_notice_shipment;
    @BindView(R.id.tv_notice_shipmentid)
    TextView tv_notice_shipmentid;
    @BindView(R.id.tv_inf_name)
    TextView tv_inf_name;
    @BindView(R.id.tv_inf_phone)
    TextView tv_inf_phone;
    @BindView(R.id.tv_inf_number)
    TextView tv_inf_number;
    @BindView(R.id.tv_inf_instructions)
    TextView tv_inf_instructions;
    @BindView(R.id.et_realnumber)
    TextView et_realnumber;
    @BindView(R.id.ll_realnumber)
    LinearLayout ll_realnumber;


    protected NoticeDocument noticeDocument;
    protected OrderShipGroup order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        setTitle(fun.getName());
        initNotice();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_notice_list.setLayoutManager(llm);
        if (SingletonCache.products == null) {
            String id = NetUtil.makeId(true, NetService.Products);
            NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getProducts, new NetUtil.MyCallBack<List<Product>>() {
                @Override
                public void onSuccess(List<Product> result) {
                    SingletonCache.products = result;
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }


    private void getOrder() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.noticeDocument, NetService.orderGroup);
        Map<String, String> m = new HashMap<>();
        m.put("id", noticeDocument.getId() + "");
        m.put("contractNo", noticeDocument.getContractNo());
        if (fun == Function.InventoryItemQuery) {
            m.put("orderType", "PURCHASE_ORDER");
        } else if (fun == Function.OutNotice) {
            m.put("orderType", "SALES_ORDER");
        }

        NetUtil.getInstance().NoBody_Response(this, m, id, NetService.getOrderShipGroup, new NetUtil.MyCallBack<OrderShipGroup>() {
            @Override
            public void onSuccess(OrderShipGroup result) {
                order = result;
                rv_notice_list.setAdapter(new OrderAdapter(order));
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    @OnClick(R.id.bt_notice_complete)
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_notice_complete:
                if (noticeDocument == null) {
                    ToastUtil.showToast(this, "请选择合同号");
                    return;
                }
                getVersion();
                break;
        }
    }

    private void getVersion() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.noticeDocument, NetService.getVersion, noticeDocument.getId() + "");
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getNoticeVersion, new NetUtil.MyCallBack<Long>() {
            @Override
            public void onSuccess(Long result) {
                completeOrder(result, "Approve");
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void completeOrder(Long version, String value) {
        OrderCommandDtos.OrderShipGroupActionRequestContent content = new OrderCommandDtos.OrderShipGroupActionRequestContent();
        content.setRequesterId(Operator.getOperator().getAccount());
        OrderShipGroupId o = new OrderShipGroupId();
        o.setShipGroupSeqId(noticeDocument.getId());
        o.setOrderId(noticeDocument.getContractNo());
        content.setOrderShipGroupId(o);
        content.setValue(value);
        content.setVersion(version);
        content.setCommandId(GUID.getUUID(content));
        String id = NetUtil.makeId(true, "Orders", noticeDocument.getContractNo(), "OrderShipGroups", noticeDocument.getId() + "", "_commands/OrderShipGroupAction");
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.completeNotice, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if ("Approve".equals(value)) {
                    completeOrder(version + 1, "Complete");
                } else if ("Complete".equals(value)) {
                    ToastUtil.showToast(NoticeLineListActivity.this, "通知单完成");
                    tv_notice_contractNo.setText("");
                    et_notice_id.setTextNoWatch("");
                    order = null;
                    rv_notice_list.setAdapter(new OrderAdapter(null));
                }
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_notice_list;
    }

    private void getFuzzyNotice(CharSequence s) {
        String id = NetUtil.makeId(true, NetService.queries, NetService.fuzzyQuery, NetService.noticeDocument, s.toString());
        Map<String, String> m = new HashMap<>();
        if (fun == Function.InventoryItemQuery) {
            m.put("type", "PURCHASE_ORDER");
        } else if (fun == Function.OutNotice) {
            m.put("type", "SALES_ORDER");
        }
        NetUtil.getInstance().NoDialogNoBody_Response(this, m, id, NetService.getBlurId, new NetUtil.MyCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                et_notice_id.showDrop(result, new RVItemClick() {
                    @Override
                    public void itemClick(int position) {
                        getContract(result.get(position));
                        et_notice_id.setTextNoWatch(result.get(position));
                        et_notice_id.dismissDrop();
                    }
                });
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void getContract(String shipId) {
        noticeDocument = null;
        tv_notice_contractNo.setText("");
        String id = NetUtil.makeId(true, NetService.queries, NetService.noticeDocument, shipId);
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getContractByNotice, new NetUtil.MyCallBack<NoticeDocument>() {
            @Override
            public void onSuccess(NoticeDocument result) {
                noticeDocument = result;
                tv_notice_contractNo.setText(result.getContractNo());
                getOrder();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void initNotice() {
        et_notice_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NetUtil.getInstance().cancelConnect();
                if (et_notice_id.isTextChange) {
                    if (!TextUtils.isEmpty(s)) {
                        getFuzzyNotice(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_notice_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getContract(et_notice_id.getText().toString());
                }
                return true;
            }
        });
    }

    private Product chooseProduct;

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
        private OrderShipGroup order;

        OrderAdapter(OrderShipGroup order) {
            this.order = order;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(NoticeLineListActivity.this).inflate(R.layout.item_order_card, parent, false);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            showViewText(holder.tv_order_label, null, "产品    " + order.getOrderShipGroupLine().get(position).getProductId());
            showViewText(holder.tv_order_count, holder.ll_order_count, order.getOrderShipGroupLine().get(position).getPlanQuantity());
            showViewText(holder.tv_order_acceptedCount, holder.ll_order_acceptedCount, order.getOrderShipGroupLine().get(position).getQuantity());
            showViewText(holder.tv_order_contract, holder.ll_order_contract, order.getOrderShipGroupLine().get(position).getOrderId());
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getChooseProductById(order.getOrderShipGroupLine().get(position).getProductId());
                    if (chooseProduct != null) {
                        ProductDialog dialog = new ProductDialog(NoticeLineListActivity.this, chooseProduct);
                        dialog.show();
                    }
                }
            });
        }

        protected void getChooseProductById(String id) {
            chooseProduct = null;
            if (SingletonCache.products == null) {
                SingletonCache.products = new ArrayList<>();
            }
            for (Product p : SingletonCache.products) {
                if (id.equals(p.getProductId())) {
                    chooseProduct = p;
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return order == null || order.getOrderShipGroupLine() == null ? 0 : order.getOrderShipGroupLine().size();
        }

        private void showViewText(TextView tv, LinearLayout ll, Object s) {
            if (ll != null) {
                if (null == s) {
                    ll.setVisibility(View.GONE);
                } else {
                    tv.setText(s + "");
                }
            } else {
                tv.setText(s + "");
            }
        }

        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_order_label)
            TextView tv_order_label;
            @BindView(R.id.tv_order_count)
            TextView tv_order_count;
            @BindView(R.id.tv_order_acceptedCount)
            TextView tv_order_acceptedCount;
            @BindView(R.id.tv_order_contract)
            TextView tv_order_contract;
            @BindView(R.id.ll_order_contract)
            LinearLayout ll_order_contract;
            @BindView(R.id.ll_order_count)
            LinearLayout ll_order_count;
            @BindView(R.id.ll_order_acceptedCount)
            LinearLayout ll_order_acceptedCount;
            @BindView(R.id.ll)
            LinearLayout ll;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
