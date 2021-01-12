package com.takumi.wms.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.takumi.wms.R;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConditionProductDialog extends Dialog {

    public ConditionProductDialog(@NonNull Context context) {
        super(context, R.style.ConditionProductDialog);
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        for (Product p : SingletonCache.products) {
            p.setChecked(false);
        }
    }

    @Override
    public void show() {
        super.show();
        setOnDismissListener(null);
    }

    public static class Builder {
        private Context context;
        private ProductSelectListener listener;
        private List<Product> products;

        public Builder(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public ProductSelectListener getListener() {
            return listener;
        }

        public void setListener(ProductSelectListener listener) {
            this.listener = listener;
        }

        public ConditionProductDialog create() {
            if (SingletonCache.products == null || SingletonCache.products.isEmpty()) {
                ToastUtil.showToast(context, "没有产品");
                return null;
            }
            View v = LayoutInflater.from(context).inflate(R.layout.dialog_condition_product, null, false);
            EditText et_kezhong = v.findViewById(R.id.et_kezhong);
            EditText et_fukuan = v.findViewById(R.id.et_fukuan);
            RecyclerView rv_product = v.findViewById(R.id.rv_product);
            Button bt_condition = v.findViewById(R.id.bt_condition);
            Button bt_ok = v.findViewById(R.id.bt_ok);
            ConditionProductDialog dialog = new ConditionProductDialog(context);
            products = new ArrayList<>(SingletonCache.products.size());
            products.addAll(SingletonCache.products);
            ConditionProductAdapter adapter = new ConditionProductAdapter(products);
            rv_product.setLayoutManager(new LinearLayoutManager(context));
            rv_product.setAdapter(adapter);
            bt_condition.setOnClickListener((bt) -> {
                for (Product p : products) {
                    p.setChecked(false);
                }
                adapter.notifyDataSetChanged();
                BigDecimal kezhong = new BigDecimal(et_kezhong.getText().toString().isEmpty() ? "0" : et_kezhong.getText().toString());
                BigDecimal fukuan = new BigDecimal(et_fukuan.getText().toString().isEmpty() ? "0" : et_fukuan.getText().toString());
                if (kezhong.equals(new BigDecimal("0")) && fukuan.equals(new BigDecimal("0"))) {
                    ToastUtil.showToast(context, "请输入克重或幅宽");
                    return;
                }
                products.clear();
                for (Product p : SingletonCache.products) {
                    BigDecimal gsm = new BigDecimal(p.getGsm() == null ? "0" : p.getGsm());
                    BigDecimal width = new BigDecimal(p.getProductWidth() == null ? "0" : p.getProductWidth() + "");
                    if (fukuan.equals(new BigDecimal("0"))) {
                        if (kezhong.compareTo(gsm) == 0) {
                            adapter.addData(p);
                        }
                    } else if (kezhong.equals(new BigDecimal("0"))) {
                        if (fukuan.compareTo(width) == 0) {
                            adapter.addData(p);
                        }
                    } else {
                        if (kezhong.compareTo(gsm) == 0 && fukuan.compareTo(width) == 0) {
                            adapter.addData(p);
                        }
                    }
                }
                if (products.isEmpty()) {
                    adapter.addData(SingletonCache.products);
                    ToastUtil.showToast(context, "未查询到产品");
                }
            });
            bt_ok.setOnClickListener((bt) -> {
                if (listener != null) {
                    Product product = null;
                    for (Product p : adapter.getData()) {
                        if (p.isChecked()) {
                            product = p;
                            p.setChecked(false);
                            break;
                        }
                    }
                    listener.productSelect(dialog, product);
                }
            });
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.cb_choose:
                            for (Product p : products) {
                                p.setChecked(false);
                            }
                            products.get(position).setChecked(true);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            });
            dialog.setContentView(v);
            return dialog;
        }
    }


    static class ConditionProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

        public ConditionProductAdapter(@Nullable List<Product> data) {
            super(R.layout.item_condition_product, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Product item) {
            helper.setText(R.id.tv_product, item.getProductId());
            CheckBox box = helper.getView(R.id.cb_choose);
            box.setChecked(item.isChecked());
            helper.addOnClickListener(R.id.cb_choose);
            helper.setText(R.id.tv_product_name, item.getProductName() == null ? "" : item.getProductName());
        }
    }

    public interface ProductSelectListener {
        void productSelect(DialogInterface dialog, Product product);
    }
}
