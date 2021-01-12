package com.takumi.wms.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.takumi.wms.R;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.dto.Command;
import com.takumi.wms.model.dto.movement.CreateOrMergePatchMovementDto;
import com.takumi.wms.model.dto.movement.CreateOrMergePatchMovementLineDto;
import com.takumi.wms.model.dto.movement.RemoveMovementLineDto;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ShowMovementLineActivity extends LineBaseActivty {

    private DocumentLine info;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getIntent().getParcelableExtra(Constant.Flags.Param2);
        quanlity_layout.setVisibility(View.GONE);
        describe_layout.setVisibility(View.GONE);
        ll_image_download.setVisibility(View.VISIBLE);
        initImageRV(false, rv_image_download, ou.getDownloadImages());
        setShowInOutLocator(null);
        setPrelocatorChoosable(false);
        setProdctIdChoosable(false);
        et_product.setTextNoWatch(info.getProductId());
        product_next.setVisibility(View.GONE);
        et_prelocator.setText(info.getLocatorId());
        et_targetlocator.setText(info.getLocatorIdTo());
        prelocator_scan.setVisibility(View.GONE);
        targetlocator_scan.setVisibility(View.GONE);
        setTitle("行项详情");
        serial_query.setVisibility(View.VISIBLE);

        ll_count_layout.setVisibility(View.GONE);
        String s = (String) info.getInventoryattribute().get("serialNumber");
        if (s == null) {
            serial_layout.setVisibility(View.GONE);
        } else {
            serial_content.setText(s);
            serial_content.setEnabled(false);
        }
        product_quantity.setEnabled(false);
        product_add.setText("删除");
        serial_query.setVisibility(View.GONE);
        showLineInfo(info);
        showMyProductInfo(info);
    }


    private void getMovementVersion() {
        String id = NetUtil.makeId(true, NetService.Movements, o.getDocumentNumber());
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");
        NetUtil.getInstance().NoBody_Response(ShowMovementLineActivity.this, m, id, NetService.getInOutDocumentVersion, new NetUtil.MyCallBack<InOutDocument>() {
            @Override
            public void onSuccess(InOutDocument result) {
                deleteMovementLine(result.getVersion());
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    /**
     * 删除移动单行项
     */
    private void deleteMovementLine(long version) {
        CreateOrMergePatchMovementDto.MergePatchMovementDto patchMovement = new CreateOrMergePatchMovementDto.MergePatchMovementDto();
        patchMovement.setDocumentNumber(o.getDocumentNumber());//单号（Id）
        patchMovement.setVersion(version);// inventoryAttribute current version
        RemoveMovementLineDto removeMovementLine = new RemoveMovementLineDto();
        removeMovementLine.setLineNumber(info.getDocumentLineId());
        patchMovement.setMovementLines(new CreateOrMergePatchMovementLineDto[]{removeMovementLine});
        removeMovementLine.setCommandType(Command.COMMAND_TYPE_REMOVE);
        patchMovement.setCommandId(GUID.getUUID(patchMovement));
        String id = NetUtil.makeId(true, NetService.Movements, o.getDocumentNumber());
        NetUtil.getInstance().Body_NoResponse(this, patchMovement, id, NetService.DeleteMovementLine, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                System.out.println(result);
                finish();
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });

    }

    @OnClick({R.id.product_add})
    void click(View v) {
        getMovementVersion();
    }
}
