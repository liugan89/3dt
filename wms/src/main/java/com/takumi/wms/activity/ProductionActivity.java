package com.takumi.wms.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.takumi.wms.R;
import com.takumi.wms.adapter.SpacesItemDecoration;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.Production;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.dto.inout.InOutCommands;
import com.takumi.wms.model.dto.production.ProductionCommandDtos;
import com.takumi.wms.net2.NetService;
import com.takumi.wms.net2.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ProductionActivity extends BottomActivity {

    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.bt_complete)
    Button bt_complete;
    @BindView(R.id.rv_lines)
    RecyclerView rv_lines;
    @BindView(R.id.bt_add)
    Button bt_add;

    private Production production;
    private ProductionLineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("加工单");
        Production p = getIntent().getParcelableExtra("Production");
        production = quickSort(p);
        tv_id.setText(production.getProductionId());

        rv_lines.setLayoutManager(new LinearLayoutManager(this));
        rv_lines.addItemDecoration(new SpacesItemDecoration(10));
        setAdapter();

        getDetail();

    }

    private void setAdapter() {
        adapter = new ProductionLineAdapter();
        rv_lines.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.bt_add_input:
                        Intent i1 = new Intent(ProductionActivity.this, AddProductionInputActivity.class);
                        InOutDocument o = new InOutDocument();
                        o.setProductionId(production.getProductionId());
                        o.setVersion(production.getVersion());
                        o.setDocumentNumber(1 + "");
                        if (production.getProductionLines() != null && !production.getProductionLines().isEmpty()) {
                            o.setLineNumber(Integer.parseInt(production.getProductionLines().get(position).getLineNumber()));
                        } else {
                            o.setLineNumber(1);
                        }
                        i1.putExtra(Constant.Flags.Param1, o);
                        startActivityForResult(i1, 0);
                        break;
                    case R.id.bt_add_output:
                        Intent i2 = new Intent(ProductionActivity.this, AddProductionOutputActivity.class);
                        InOutDocument i = new InOutDocument();
                        i.setProductionId(production.getProductionId());
                        i.setVersion(production.getVersion());
                        int n = production.getProductionLines().get(position).getProductionLineOutputs() == null ? 0 : production.getProductionLines().get(position).getProductionLineOutputs().size();
                        i.setDocumentNumber((n + 1) + "");
                        if (production.getProductionLines() != null && !production.getProductionLines().isEmpty()) {
                            i.setLineNumber(Integer.parseInt(production.getProductionLines().get(position).getLineNumber()));
                        } else {
                            i.setLineNumber(1);
                        }
                        i2.putExtra(Constant.Flags.Param1, i);
                        if (production.getProductionLines().get(position).getProductionLineInputs() != null
                                && !production.getProductionLines().get(position).getProductionLineInputs().isEmpty())
                            i2.putExtra("production", production.getProductionLines().get(position).getProductionLineInputs().get(0));
                        startActivityForResult(i2, 0);
                        break;
                }
            }
        });
    }

    private void getDetail() {
        Map m = new HashMap<>();
        Map<String, String> ids = new IdentityHashMap<>();
        if (production.getProductionLines() != null) {
            for (Production.ProductionLine p : production.getProductionLines()) {
                if (p.getProductionLineInputs() != null) {
                    for (Production.ProductionLine.ProductionLineInput i : p.getProductionLineInputs()) {
                        ids.put(new String("id"), i.getAttributeSetInstanceId());
                        getChooseProductById(i, i.getProductId());
                    }
                }
                if (p.getProductionLineOutputs() != null) {
                    for (Production.ProductionLine.ProductionLineOutput o : p.getProductionLineOutputs()) {
                        ids.put(new String("id"), o.getAttributeSetInstanceId());
                        getChooseProductById(o, o.getProductId());
                    }
                }
            }
        }
        NetUtil.getInstance().DialogCall(this, NetService.getAttributeSetInstances, ids, null, NetUtil.GET, NetUtil.createListType(Map.class), new NetUtil.MyCallBack<List<Map>>() {
            @Override
            public void onSuccess(List<Map> result) {
                for (Map map : result) {
                    m.put(map.get("AttributeSetInstanceId"), map);
                }

                if (production.getProductionLines() != null) {
                    for (Production.ProductionLine p : production.getProductionLines()) {
                        if (p.getProductionLineInputs() != null) {
                            for (Production.ProductionLine.ProductionLineInput i : p.getProductionLineInputs()) {
                                i.setAttributeSetInstance((Map) m.get(i.getAttributeSetInstanceId()));
                            }
                        }
                        if (p.getProductionLineOutputs() != null) {
                            for (Production.ProductionLine.ProductionLineOutput o : p.getProductionLineOutputs()) {
                                o.setAttributeSetInstance((Map) m.get(o.getAttributeSetInstanceId()));
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    protected void getChooseProductById(Production.ProductionLine.ProductionLineInput i, String id) {
        if (SingletonCache.products == null) {
            SingletonCache.products = new ArrayList<>();
        }

        for (Product p : SingletonCache.products) {
            if (id.equals(p.getProductId())) {
                i.setProduct(p);
                adapter.notifyDataSetChanged();
                break;
            }
        }
        if (i.getProduct() == null) {
            getProduct(id, new com.takumi.wms.net.NetUtil.MyCallBack<Product>() {
                @Override
                public void onSuccess(Product result) {
                    i.setProduct(result);
                    addProduct(result);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }

    protected void getChooseProductById(Production.ProductionLine.ProductionLineOutput o, String id) {
        if (SingletonCache.products == null) {
            SingletonCache.products = new ArrayList<>();
        }

        for (Product p : SingletonCache.products) {
            if (id.equals(p.getProductId())) {
                o.setProduct(p);
                adapter.notifyDataSetChanged();
                break;
            }
        }
        if (o.getProduct() == null) {
            getProduct(id, new com.takumi.wms.net.NetUtil.MyCallBack<Product>() {
                @Override
                public void onSuccess(Product result) {
                    o.setProduct(result);
                    addProduct(result);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }

    protected void getProduct(String productId, com.takumi.wms.net.NetUtil.MyCallBack callBack) {
        String id = com.takumi.wms.net.NetUtil.makeId(true, com.takumi.wms.net.NetService.queries, com.takumi.wms.net.NetService.productAttribute, productId);
        com.takumi.wms.net.NetUtil.getInstance().NoBody_Response(this, null, id, com.takumi.wms.net.NetService.getProduct, callBack);
    }

    protected void addProduct(Product p) {
        if (SingletonCache.products == null) {
            SingletonCache.products = new ArrayList<>();
        }
        SingletonCache.products.add(p);
    }

    @OnClick(R.id.bt_add)
    void bt_add(View v) {
        if (production.getProductionLines() != null) {
            for (Production.ProductionLine l : production.getProductionLines()) {
                if (l.getProductionLineInputs() == null || l.getProductionLineInputs().isEmpty()) {
                    ToastUtil.showToast(this, "存在空白行项，请先处理空白行项");
                    return;
                }
            }
        }
        Production.ProductionLine line = new Production.ProductionLine();
        line.setLineNumber((production.getProductionLines() == null ? 1 : production.getProductionLines().size() + 1) + "");
        if (production.getProductionLines() == null) {
            production.setProductionLines(new ArrayList<>());
        }
        adapter.addData(0, line);
        rv_lines.scrollToPosition(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            NetUtil.getInstance().DialogCall(this, NetService.GetUrl(NetService.getProductions, production.getProductionId())
                    , null, null, NetUtil.GET, NetUtil.createObjType(Production.class), new NetUtil.MyCallBack<Production>() {
                        @Override
                        public void onSuccess(Production result) {
                            production = quickSort(result);
                            setAdapter();
                            getDetail();
                        }

                        @Override
                        public boolean onError(String msg) {
                            return false;
                        }
                    });
        }
    }

    private Production quickSort(Production production) {
        try {
            if (production.getProductionLines() != null) {
                int max = 0;
                for (Production.ProductionLine l : production.getProductionLines()) {
                    if (Integer.parseInt(l.getLineNumber()) > max) {
                        max = Integer.parseInt(l.getLineNumber());
                    }
                }
                Production.ProductionLine[] ls = new Production.ProductionLine[max + 1];
                for (Production.ProductionLine l : production.getProductionLines()) {
                    ls[Integer.parseInt(l.getLineNumber())] = l;
                }
                List<Production.ProductionLine> lines = new ArrayList<>();
                for (int i = ls.length - 1; i >= 0; i--) {
                    Production.ProductionLine l = ls[i];
                    if (l != null) {
                        lines.add(l);
                    }
                }
                production.setProductionLines(lines);
            }
        } catch (Exception e) {
            return production;
        }

        return production;
    }

    @OnClick(R.id.bt_complete)
    void bt_complete(View v) {
        ProductionCommandDtos.DocumentActionRequestContent c = new ProductionCommandDtos.DocumentActionRequestContent();
        c.setValue("Complete");
        c.setProductionId(production.getProductionId());
        c.setVersion(production.getVersion());
        c.setCommandId(GUID.getUUID(c));
        NetUtil.getInstance().DialogCall(this, NetService.GetUrl(NetService.completeProduction, c.getProductionId()), null, c, NetUtil.PUT, null, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(ProductionActivity.this, "提交成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                ToastUtil.showToast(ProductionActivity.this, "提交失败");
                return false;
            }
        });

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_production;
    }

    class ProductionLineAdapter extends BaseQuickAdapter<Production.ProductionLine, BaseViewHolder> {
        public ProductionLineAdapter() {
            super(R.layout.item_production_line, production.getProductionLines());
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Production.ProductionLine item) {
            if (item.getProductionLineInputs() != null && !item.getProductionLineInputs().isEmpty()) {
                Production.ProductionLine.ProductionLineInput input = item.getProductionLineInputs().get(0);
                helper.setText(R.id.tv_seq, item.getLineNumber());
                helper.setText(R.id.tv_product, input.getProductId());
                helper.setText(R.id.tv_number, input.getAttributeSetInstance() == null ? "" : (String) input.getAttributeSetInstance().get("serialNumber"));
                helper.setText(R.id.tv_quantity, input.getQuantity() + "");
                helper.setText(R.id.tv_locator, input.getLocatorId());
                if (input.getProduct() != null) {
                    helper.setText(R.id.tv_name, input.getProduct().getProductName());
                    helper.setText(R.id.tv_uom, input.getProduct().getDiameterUomId());
                }
            } else {
                helper.setGone(R.id.ll_info, false);
                helper.setText(R.id.tv_seq, item.getLineNumber());
                Button addOutput = helper.getView(R.id.bt_add_output);
                addOutput.setEnabled(false);
            }

            if (item.getProductionLineOutputs() != null && !item.getProductionLineOutputs().isEmpty()) {
                RecyclerView rv = helper.getView(R.id.rv_out);
                ProductionLineOutAdapter a = new ProductionLineOutAdapter(item.getProductionLineOutputs());
                rv.setLayoutManager(new LinearLayoutManager(ProductionActivity.this));
                rv.addItemDecoration(new SpacesItemDecoration(10));
                a.setOnItemLongClickListener((adapter, view, position) -> {
                    new AlertDialog.Builder(ProductionActivity.this).setTitle("是否删除产出项")
                            .setMessage("确认删除？")
                            .setPositiveButton("确定", (d, w) -> {
                                deleteOut(position, item, a);
                                d.dismiss();
                            }).setNegativeButton("取消", (d, w) -> {
                        d.dismiss();
                    }).setCancelable(false)
                            .show();
                    return true;
                });
                rv.setAdapter(a);
            }

            helper.addOnClickListener(R.id.bt_add_input, R.id.bt_add_output);
        }

        private void deleteOut(int position, Production.ProductionLine item, ProductionLineOutAdapter a) {

            NetUtil.getInstance().getVersion(ProductionActivity.this, NetService.GetUrl(NetService.getProductions, item.getProductionId()), new NetUtil.VersionBack() {
                @Override
                public void versionDo(long version) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("version", version);
                    m.put("commandId", GUID.getUUID(m));
                    NetUtil.getInstance().DialogCall(ProductionActivity.this
                            , NetService.GetUrl(NetService.deleteProductionOut, production.getProductionId(), item.getLineNumber(), item.getProductionLineOutputs().get(position).getProductionLineOutputSeqId())
                            , m, null, NetUtil.DELETE, null, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                                @Override
                                public void onSuccess(Response<ResponseBody> result) {
                                    a.remove(position);
                                }

                                @Override
                                public boolean onError(String msg) {
                                    ToastUtil.showToast(ProductionActivity.this, "删除失败");
                                    return false;
                                }
                            });
                }
            });
        }
    }

    class ProductionLineOutAdapter extends BaseQuickAdapter<Production.ProductionLine.ProductionLineOutput, BaseViewHolder> {

        public ProductionLineOutAdapter(@Nullable List<Production.ProductionLine.ProductionLineOutput> data) {
            super(R.layout.item_production_line_output, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Production.ProductionLine.ProductionLineOutput item) {
            if (getData() != null && !getData().isEmpty()) {
                helper.setText(R.id.tv_product, item.getProductId());
                helper.setText(R.id.tv_number, item.getAttributeSetInstance() == null ? "" : (String) item.getAttributeSetInstance().get("serialNumber"));
                helper.setText(R.id.tv_quantity, item.getQuantity() + "");
                helper.setText(R.id.tv_locator, item.getLocatorId());
                if (item.getProduct() != null) {
                    helper.setText(R.id.tv_name, item.getProduct().getProductName());
                    helper.setText(R.id.tv_uom, item.getProduct().getDiameterUomId());
                }
            } else {
                helper.setVisible(R.id.ll_info, false);
            }

        }
    }

}
