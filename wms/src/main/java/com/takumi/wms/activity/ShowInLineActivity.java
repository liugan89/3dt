package com.takumi.wms.activity;

import android.os.Bundle;
import android.view.View;

import com.takumi.wms.R;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.dto.Command;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutDto;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutLineDto;
import com.takumi.wms.model.dto.inout.RemoveInOutLineDto;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ShowInLineActivity extends LineBaseActivty {

    private DocumentLine info;//代表删除行项


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getIntent().getParcelableExtra(Constant.Flags.Param2);
        ll_image_download.setVisibility(View.VISIBLE);
        initImageRV(false, rv_image_download, ou.getDownloadImages());
        setShowInOutLocator(false);
        setTargetLocatorChooseful(false);
        setProdctIdChoosable(false);
        et_product.setTextNoWatch(info.getProductId());
        et_targetlocator.setText(info.getLocatorId());
        prelocator_scan.setVisibility(View.GONE);
        targetlocator_scan.setVisibility(View.GONE);
        setTitle("行项详情");
        product_quantity.setEnabled(false);
        product_next.setVisibility(View.GONE);
        product_add.setText("删除");
        showLineInfo(info);
        ll_count_layout.setVisibility(View.GONE);
        quanlity_layout.setVisibility(View.GONE);
        describe_layout.setVisibility(View.GONE);

        /*if (info.getInventoryattribute().containsKey("serialNumber")) {
            serial_content.setText(info.getInventoryattribute().get("serialNumber").toString());
            serial_content.setEnabled(false);
        } else {
        }*/
        serial_layout.setVisibility(View.GONE);

        showMyProductInfo(info);
    }


    /**
     * 删除出入库行项
     */
    private void deleteInOutLine() {
        CreateOrMergePatchInOutDto.MergePatchInOutDto patchInOut = new CreateOrMergePatchInOutDto.MergePatchInOutDto();
        patchInOut.setDocumentNumber(o.getDocumentNumber());//单号（Id）
        patchInOut.setVersion(o.getVersion());// inventoryAttribute current version
        RemoveInOutLineDto removeInOutLine = new RemoveInOutLineDto();
        removeInOutLine.setLineNumber(info.getDocumentLineId());
        patchInOut.setInOutLines(new CreateOrMergePatchInOutLineDto[]{removeInOutLine});
        removeInOutLine.setCommandType(Command.COMMAND_TYPE_REMOVE);
        patchInOut.setCommandId(GUID.getUUID(patchInOut));
        String id = NetUtil.makeId(true, NetService.InOuts, o.getDocumentNumber());
        NetUtil.getInstance().Body_NoResponse(this, patchInOut, id, NetService.DeleteInOutLine, new NetUtil.MyCallBack<Response<ResponseBody>>() {
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
        deleteInOutLine();
    }
}
