package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.takumi.wms.R;
import com.takumi.wms.adapter.InOutDocumentAdapter;
import com.takumi.wms.adapter.OnItemClickListener;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.Command;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutDto;
import com.takumi.wms.model.dto.movement.CreateOrMergePatchMovementDto;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class UndoneActivity extends BottomActivity {

    @BindView(R.id.btn_newExecute)
    Button btn_newExecute;
    @BindView(R.id.rv_undone)
    RecyclerView rv_undone;

    private List<InOutDocument> inOutDocuments;

    private InOutDocument o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        initImmersionBar(R.color.white, true);
        setTitle(fun.getName());
        initView();
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_undone.setLayoutManager(llm);
        rv_undone.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (fun) {
            case In:
            case Out:
                getInOutUndoneDocs();
                break;
            case Movement:
                getMovementUndoneDocs();
                break;
        }
    }

    private void getInOutUndoneDocs() {
        Map<String, String> map = new HashMap<>();
        map.put("documentStatusId", "Drafted");
        map.put("documentTypeId", fun.getValue());
        map.put(fun == Function.Movement ? "warehouseIdFrom" : "warehouseId", Warehouse.getHouse().getWarehouseId());
        String id = NetUtil.makeId(true, NetService.InOuts);
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getInOutDocuments, new NetUtil.MyCallBack<List<InOutDocument>>() {
            @Override
            public void onSuccess(List<InOutDocument> result) {
                UndoneActivity.this.inOutDocuments = result;
                if (result == null || result.isEmpty()) {
                    ToastUtil.showToast(UndoneActivity.this, "无未完成" + fun.getName() + "单");
                }
                initRV();
            }

            @Override
            public boolean onError(String msg) {
                initRV();
                System.out.println(msg);
                return false;
            }
        });
    }

    private void getMovementUndoneDocs() {
        Map<String, String> map = new HashMap<>();
        map.put("documentStatusId", "Drafted");
        map.put("warehouseIdFrom", Warehouse.getHouse().getWarehouseId());
        String id = NetUtil.makeId(true, NetService.Movements);
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getMovements, new NetUtil.MyCallBack<List<InOutDocument>>() {
            @Override
            public void onSuccess(List<InOutDocument> result) {
                UndoneActivity.this.inOutDocuments = result;
                if (result == null || result.isEmpty()) {
                    ToastUtil.showToast(UndoneActivity.this, "无未完成" + fun.getName() + "单");
                }
                initRV();
            }

            @Override
            public boolean onError(String msg) {
                initRV();
                System.out.println(msg);
                return false;
            }
        });
    }


    private void initRV() {
        InOutDocumentAdapter adapter = new InOutDocumentAdapter(inOutDocuments, fun);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                o = inOutDocuments.get(position);
                goDocList(o);
            }
        });
        rv_undone.setAdapter(adapter);
    }

    private void initView() {
        btn_newExecute.setText("创建新" + fun.getName() + "单");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_inouts_undone;
    }

    @OnClick({R.id.btn_newExecute})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_newExecute:
                switch (fun) {
                    case In:
                    case Out:
                        newInOutExecute();
                        break;
                    case Movement:
                        newMovementExecute();
                        break;
                    case IncomingShipment:

                        break;
                }
                break;
        }
    }

    /**
     * 创建新的出入库单
     */
    private void newInOutExecute() {
        CreateOrMergePatchInOutDto o = new CreateOrMergePatchInOutDto();
        String number = System.currentTimeMillis() + "";
        o.setActive(true);
        o.setDocumentNumber(number);
        o.setDocumentTypeId(fun.getValue());
        o.setCommandType(Command.COMMAND_TYPE_CREATE);
        o.setWarehouseId(Warehouse.getHouse().getWarehouseId());
        o.setCommandId(GUID.getUUID(o));

        String id = NetUtil.makeId(true, NetService.InOuts, number);
        //o.setVersion(1L);
        NetUtil.getInstance().Body_NoResponse(this, o, id, NetService.newInOut, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                try {
                    InOutDocument o = new InOutDocument();
                    o.setDocumentNumber(number);
                    goDocList(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    /**
     * 创建新的移动单
     */
    private void newMovementExecute() {
        CreateOrMergePatchMovementDto.CreateMovementDto o = new CreateOrMergePatchMovementDto.CreateMovementDto();
        o.setDocumentNumber(System.currentTimeMillis() + "");
        o.setRequesterId(Operator.getOperator().getAccount());
        o.setDocumentTypeId(fun.getValue());
        o.setMovementDate(new Date());
        o.setIsInTransit(false);
        o.setWarehouseIdFrom(Warehouse.getHouse().getWarehouseId());
        o.setCommandId(GUID.getUUID(o));
        String id = NetUtil.makeId(true, NetService.Movements);
        //o.setVersion(1L);
        NetUtil.getInstance().Body_NoResponse(this, o, id, NetService.createNewMovement, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                try {
                    InOutDocument o = new InOutDocument();
                    String s = result.body().string();
                    o.setDocumentNumber(s);
                    goDocList(o);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }


    private void goDocList(InOutDocument o) {
        Intent intent = new Intent(UndoneActivity.this, InOutLineListActivity.class);
        intent.putExtra(Constant.Flags.MainFlag, fun);
        intent.putExtra(Constant.Flags.Param1, o);
        startActivity(intent);
    }
}
