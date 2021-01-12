package com.takumi.wms.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.takumi.wms.R;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.Production;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.production.CreateOrMergePatchProductionDto;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.DateUtil;
import com.takumi.wms.utils.GUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ProductionListActivity extends BottomActivity {

    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.rv1)
    RecyclerView rv1;

    private List<Production> productions;
    private ProductionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("加工单列表");
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

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> m = new HashMap<>();
        m.put("documentStatusId", "Drafted");
        NetUtil.getInstance().NoBody_Response(this, m, NetUtil.makeId(true, "Productions"), NetService.getProductionList, new NetUtil.MyCallBack<List<Production>>() {
            @Override
            public void onSuccess(List<Production> result) {
                productions = result;
                rv1.setLayoutManager(new LinearLayoutManager(ProductionListActivity.this));
                adapter = new ProductionListAdapter();
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        goProduction(productions.get(position));
                    }
                });
                rv1.setAdapter(adapter);
                rv1.addItemDecoration(new DividerItemDecoration(ProductionListActivity.this, DividerItemDecoration.VERTICAL));
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    @OnClick(R.id.bt1)
    void new_production(View v) {
        CreateOrMergePatchProductionDto.CreateProductionDto dto = new CreateOrMergePatchProductionDto.CreateProductionDto();
        dto.setProductionId(System.currentTimeMillis() + "");
        dto.setFacilityId(Warehouse.getHouse().getWarehouseId());
        dto.setCommandId(GUID.getUUID(dto));
        NetUtil.getInstance().Body_NoResponse(this, dto, NetUtil.makeId(true, "Productions", dto.getProductionId()), NetService.createListProduction, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                Production p = new Production();
                p.setProductionId(dto.getProductionId());
                goProduction(p);
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    public void goProduction(Production p) {
        Intent intent = new Intent(this, ProductionActivity.class);
        intent.putExtra("Production", p);
        startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_production_list;
    }


    class ProductionListAdapter extends BaseQuickAdapter<Production, BaseViewHolder> {

        public ProductionListAdapter() {
            super(R.layout.inout_item, productions);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Production item) {
            helper.setText(R.id.odd_item_number, item.getProductionId());
            helper.setText(R.id.odd_item_time, DateUtil.toDateTime(item.getCreatedAt().getTime()));
        }
    }
}
