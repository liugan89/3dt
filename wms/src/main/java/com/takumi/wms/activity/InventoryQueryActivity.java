package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InventoryItemInfo;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.widget.DropEdit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class InventoryQueryActivity extends BottomActivity {

    @BindView(R.id.et_product)
    DropEdit et_product;
    @BindView(R.id.product_next)
    ImageView product_next;
    @BindView(R.id.et_contract)
    TextView et_contract;
    @BindView(R.id.tv_product)
    TextView tv_product;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.tv_quantity)
    TextView tv_quantity;

    private Product chooseProduct;
    private List<String> contracts;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        setTitle(fun.getName());
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
        et_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NetUtil.getInstance().cancelConnect();
                if (et_product.isTextChange) {
                    if (!TextUtils.isEmpty(s)) {
                        String id = NetUtil.makeId(true, NetService.queries, NetService.fuzzyQuery, NetService.product, s.toString());
                        NetUtil.getInstance().NoDialogNoBody_Response(InventoryQueryActivity.this, null, id, NetService.getBlurId, new NetUtil.MyCallBack<List<String>>() {
                            @Override
                            public void onSuccess(List<String> result) {
                                et_product.showDrop(result, new RVItemClick() {
                                    @Override
                                    public void itemClick(int position) {
                                        getChooseProductById(result.get(position));
                                    }
                                });
                            }

                            @Override
                            public boolean onError(String msg) {
                                return false;
                            }
                        });
                    }
                } else {
                    et_product.isTextChange = true;
                    et_product.dismissDrop();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    protected void getChooseProductById(String id) {
        if (SingletonCache.products == null) {
            SingletonCache.products = new ArrayList<>();
        }
        chooseProduct = null;
        tv_product.setText("");
        tv_count.setText("");
        tv_quantity.setText("");
        for (Product p : SingletonCache.products) {
            if (id.equals(p.getProductId())) {
                chooseProduct = p;
                et_product.setTextNoWatch(chooseProduct.getProductId());
                getContract();
                break;
            }
        }
        if (chooseProduct == null) {
            getProduct(id, new NetUtil.MyCallBack<Product>() {
                @Override
                public void onSuccess(Product result) {
                    chooseProduct = result;
                    et_product.setTextNoWatch(chooseProduct.getProductId());
                    getContract();
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }

    private void getContract() {
        String id = "api/queries/check/poNumbers";
        Map<String, String> m = new HashMap<>();
        m.put("productId", chooseProduct.getProductId());
        NetUtil.getInstance().NoBody_Response(this, m, id, NetService.getBlurId, new NetUtil.MyCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                contracts = result;
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    /**
     * 单选
     */
    protected void dialogContractChoice() {
        if (contracts != null && !contracts.isEmpty()) {
            final String items[] = new String[contracts.size()];
            for (int i = 0; i < contracts.size(); i++) {
                items[i] = contracts.get(i);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("单选");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String id = "api/queries/check/information";
                    Map<String, String> m = new HashMap<>();
                    m.put("productId", chooseProduct.getProductId());
                    m.put("poNumber", items[which]);
                    NetUtil.getInstance().NoBody_Response(InventoryQueryActivity.this, m, id, NetService.getInfor, new NetUtil.MyCallBack<InventoryItemInfo>() {
                        @Override
                        public void onSuccess(InventoryItemInfo result) {
                            et_contract.setText(contracts.get(which));
                            tv_product.setText(result.getProductId());
                            tv_count.setText(result.getCount() + "");
                            tv_quantity.setText(result.getQuantity() + "");
                        }

                        @Override
                        public boolean onError(String msg) {
                            return false;
                        }
                    });
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    /**
     * 获取产品信息并显示
     */
    protected void getProduct(String productId, NetUtil.MyCallBack callBack) {
        String id = NetUtil.makeId(true, NetService.queries, NetService.productAttribute, productId);
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getProduct, callBack);
    }


    /**
     * 单选
     */
    protected void dialogProductChoice() {
        if (SingletonCache.products != null && !SingletonCache.products.isEmpty()) {
            final String items[] = new String[SingletonCache.products.size()];
            for (int i = 0; i < SingletonCache.products.size(); i++) {
                items[i] = SingletonCache.products.get(i).getProductId();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("单选");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getChooseProductById(items[which]);
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

    }

    @OnClick(R.id.product_next)
    void product_next() {
        dialogProductChoice();
    }

    @OnClick(R.id.contract_next)
    void contract_next() {
        dialogContractChoice();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_inventoryquery;
    }
}
