package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.LineGridImageAdapter;
import com.takumi.wms.adapter.SpacesItemDecoration;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.DocumentInfo;
import com.takumi.wms.model.MandatoryAtts;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.OssImage;
import com.takumi.wms.model.OutDocumentInfo;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentDto;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentImageDto;
import com.takumi.wms.model.dto.shipment.RemoveShipmentImageDto;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.obj.OssFile;
import com.takumi.wms.utils.AttrInflater;
import com.takumi.wms.utils.FileUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.OSSUtil;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.SumGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class ShipmentDetailBaseActivity extends BottomActivity {

    @BindView(R.id.tv_shipmentid)
    TextView tv_shipmentid;
    @BindView(R.id.et_shipment_serialnumber)
    EditText et_shipment_serialnumber;
    @BindView(R.id.iv_shipment_serial_scan)
    ImageView iv_shipment_serial_scan;
    @BindView(R.id.rv_shipment_item)
    RecyclerView rv_shipment_item;
    @BindView(R.id.btn_commit_shipment)
    Button btn_commit_shipment;
    @BindView(R.id.rv_images)
    RecyclerView rv_images;
    @BindView(R.id.RequiredSum)
    SumGroup RequiredSum;
    @BindView(R.id.bt_create_shipmentLine)
    Button bt_create_shipmentLine;
    @BindView(R.id.tv_shipment_notice)
    TextView tv_shipment_notice;
    @BindView(R.id.tv_shipment_contractNo)
    TextView tv_shipment_contractNo;
    @BindView(R.id.ll_shipment_contract)
    LinearLayout ll_shipment_contract;
    @BindView(R.id.bt_scan)
    Button bt_scan;
    @BindView(R.id.ll_scan)
    LinearLayout ll_scan;
    @BindView(R.id.ll_shipment_serial)
    LinearLayout ll_shipment_serial;
    @BindView(R.id.ll_shipment_notice)
    LinearLayout ll_shipment_notice;
    @BindView(R.id.btn_push)
    Button btn_push;


    protected ShipmentDetail ship;
    protected DocumentInfo o;
    protected OutDocumentInfo ol;
    protected InOutDocument inOutDocument;

    protected OSSUtil ou;
    protected LineGridImageAdapter gridImageAdapter;
    protected int imageFlag = -1;
    protected MandatoryAtts requireds;
    protected List<DocumentLine> ls;

    protected LinearLayoutManager llm;

    protected List<Integer> expands = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.white, true);
        setBarShadow(false);

        ou = new OSSUtil();
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        ship = getIntent().getParcelableExtra(Constant.Flags.Param1);
        inOutDocument = new InOutDocument();
        inOutDocument.setDocumentNumber(ship.getShipmentId());
        tv_shipmentid.setText(ship.getShipmentId());
        tv_shipment_notice.setText(ship.getPrimaryShipGroupSeqId());
        if (ship.getPrimaryOrderId() == null) {
            ll_shipment_contract.setVisibility(View.GONE);
        } else {
            ll_shipment_contract.setVisibility(View.VISIBLE);
            tv_shipment_contractNo.setText(ship.getPrimaryOrderId());
        }
        llm = new LinearLayoutManager(this);
        rv_shipment_item.setLayoutManager(llm);
        et_shipment_serialnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    goNextById(v.getText().toString());
                }
                return false;
            }
        });
        initImages();
        ls = new ArrayList<>();

    }

    protected void getRequireds(String id) {
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getMandatoryAtts, new NetUtil.MyCallBack<MandatoryAtts>() {
            @Override
            public void onSuccess(MandatoryAtts result) {
                RequiredSum.removeAllViews();
                requireds = result;
                Map<String, Object> sum = new LinkedHashMap<>();
                sum.put("总数量", result.getCountSum());
                sum.put("接收数量", result.getAcceptedCountSum());
                sum.put("总重量", result.getWeightKgSum());
                sum.put("接收重量", result.getAcceptedWeightKgSum());
                new AttrInflater(sum, false, ShipmentDetailBaseActivity.this, RequiredSum, false);
                getShipmentDoc();
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    @OnClick(R.id.bt_scan)
    void bt_scan() {
        Intent intent = new Intent(this, WeChatCaptureActivity.class);
        startActivityForResult(intent, SerialScanRequest);
    }

    protected void initImages() {
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        rv_images.setLayoutManager(glm);
        rv_images.setHasFixedSize(true);
        rv_images.setNestedScrollingEnabled(false);
        rv_images.addItemDecoration(new SpacesItemDecoration(40));
        gridImageAdapter = new LineGridImageAdapter(this, ou.getDownloadImages(), ou.getUploadImages(), true);
        gridImageAdapter.setClick(new LineGridImageAdapter.RVLineItemClick() {
            @Override
            public void itemClick(int position, int flag) {
                if (flag == LineGridImageAdapter.TakePhoto) {
                    showBottomDialog();
                } else {
                    imageFlag = flag;
                    Intent i = new Intent(ShipmentDetailBaseActivity.this, ImageActivity.class);
                    if (flag == LineGridImageAdapter.Download) {
                        i.putExtra(Constant.Flags.Param1, ou.getDownloadImages().get(position));
                    } else if (flag == LineGridImageAdapter.Upload) {
                        i.putExtra(Constant.Flags.Param1, ou.getUploadImages().get(position - ou.getDownloadImages().size()));
                    }
                    i.putExtra(Constant.Flags.Param2, position);
                    i.putExtra(Constant.Flags.Param3, gridImageAdapter.isPhotoable());
                    startActivityForResult(i, REQUEST_IMAGE_SHOW);
                }
            }
        });
        rv_images.setAdapter(gridImageAdapter);
    }

    protected void getImages(List<OssImage> objectKeys) {
        if (objectKeys != null && !objectKeys.isEmpty()) {
            ou.downloadPics(this, objectKeys, new NetUtil.MyCallBack() {
                @Override
                public void onSuccess(Object result) {
                    gridImageAdapter.notifyDataSetChanged();
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }

    protected void getShipmentDoc() {
    }

    protected void goShowNext(DocumentLine l) {
        Intent i = new Intent(ShipmentDetailBaseActivity.this, ShowShipmentActivity.class);
        i.putExtra(Constant.Flags.Param1, inOutDocument);
        i.putExtra(Constant.Flags.Param2, l);
        i.putExtra(Constant.Flags.MainFlag, fun);
        startActivity(i);
    }


    @OnClick(R.id.iv_shipment_serial_scan)
    void iv_shipment_serial_scan() {
        Intent i = new Intent(ShipmentDetailBaseActivity.this, WeChatCaptureActivity.class);
        startActivityForResult(i, SerialScanRequest);
    }

    protected void CommitShipmentComplete(long version) {
    }

    protected void goNext(DocumentLine l) {
        Intent i = new Intent(this, CheckShipmentActivity.class);
        i.putExtra(Constant.Flags.Param1, inOutDocument);
        i.putExtra(Constant.Flags.Param2, l);
        i.putExtra(Constant.Flags.MainFlag, fun);
        startActivity(i);
    }

    protected void goAddInNext(int seqId) {
        Intent i = new Intent(this, AddShipmentInLineActivity.class);
        i.putExtra(Constant.Flags.Param1, inOutDocument);
        i.putExtra(Constant.Flags.Param2, seqId);
        i.putExtra(Constant.Flags.MainFlag, fun);
        startActivity(i);
    }

    protected void goAddOutNext(int position, int seqId) {
        Intent i = new Intent(this, AddShipmentOutLineActivity.class);
        i.putExtra(Constant.Flags.Param1, inOutDocument);
        i.putExtra(Constant.Flags.Param2, seqId);
        i.putExtra(Constant.Flags.Param4, ol.getDocumentLines().get(position).getDocumentLineId());
        i.putExtra(Constant.Flags.Param5, ol.getDocumentLines().get(position));
        i.putExtra(Constant.Flags.Param6, ol);
        i.putExtra(Constant.Flags.MainFlag, fun);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    final OssFile f = OssFile.createOssFile(FileUtil.getImageDir() + File.separator + photoUri.getLastPathSegment(), OSSUtil.makeKey(photoUri.getLastPathSegment()), true);
                    UploadImage(f);
                    gridImageAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_SHOW) {
            if (resultCode == RESULT_OK) {
                final int position = data.getIntExtra(Constant.Flags.Param1, -1);
                getVersion(new NetUtil.MyCallBack<ShipmentDetail>() {
                    @Override
                    public void onSuccess(ShipmentDetail result) {
                        if (imageFlag == LineGridImageAdapter.Download) {
                            patchDocImage(ou.getDownloadImages().get(position), REMOVE, result.getVersion());
                            ou.getDownloadImages().remove(position);
                            gridImageAdapter.notifyItemRemoved(position);
                            gridImageAdapter.notifyDataSetChanged();
                        } else if (imageFlag == LineGridImageAdapter.Upload) {
                            patchDocImage(ou.getUploadImages().get(position - ou.getDownloadImages().size()), REMOVE, result.getVersion());
                            ou.getUploadImages().remove(position - ou.getDownloadImages().size());
                            gridImageAdapter.notifyItemRemoved(position);
                            gridImageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public boolean onError(String msg) {
                        return false;
                    }
                });

            }
        } else if (requestCode == REQUEST_ALBUM) {
            if (resultCode == RESULT_OK) {
                try {
                    final OssFile f = OssFile.createOssFile(imageFile, OSSUtil.makeKey(imageFile.getName()), true);
                    UploadImage(f);
                    gridImageAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void UploadImage(OssFile f) throws IOException {
        ou.getUploadImages().add(f);
        ou.uploadPic(this, f, new NetUtil.MyCallBack() {
            @Override
            public void onSuccess(Object result) {
                Observable.just(null).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object n) {
                        getShipmentDoc();
                        getVersion(new NetUtil.MyCallBack<ShipmentDetail>() {
                            @Override
                            public void onSuccess(ShipmentDetail result) {
                                patchDocImage(f, PATCH, result.getVersion());
                            }

                            @Override
                            public boolean onError(String msg) {
                                return false;
                            }
                        });

                    }
                });
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    protected void goNextById(String s) {

    }

    public static final String REMOVE = "REMOVE";
    public static final String PATCH = "PATCH";

    protected void getVersion(NetUtil.MyCallBack callback) {
        String id = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId());
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");
        NetUtil.getInstance().NoBody_Response(ShipmentDetailBaseActivity.this, m, id, NetService.getShipmentVersion, callback);
    }

    protected void patchDocImage(OssFile f, final String s, long version) {
        CreateOrMergePatchShipmentDto.MergePatchShipmentDto patchShipment = new CreateOrMergePatchShipmentDto.MergePatchShipmentDto();
        patchShipment.setShipmentId(ship.getShipmentId());//单号（Id）
        patchShipment.setVersion(version);// set current version
        CreateOrMergePatchShipmentImageDto dto = null;
        if (s.equals(REMOVE)) {
            dto = new RemoveShipmentImageDto();
        } else if (s.equals(PATCH)) {
            dto = new CreateOrMergePatchShipmentImageDto.CreateShipmentImageDto();
        }
        dto.setSequenceId(f.getName());
        dto.setUrl(f.getObjectUrl());
        patchShipment.setShipmentImages(new CreateOrMergePatchShipmentImageDto[]{dto});

        String id = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId());
        patchShipment.setCommandId(GUID.getUUID(patchShipment));
        NetUtil.getInstance().Body_NoResponse(this, patchShipment, id, NetService.AddShipmentImage, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (s.equals(REMOVE)) {
                    ToastUtil.showToast(ShipmentDetailBaseActivity.this, "删除成功");
                } else if (s.equals(PATCH)) {
                    ToastUtil.showToast(ShipmentDetailBaseActivity.this, "上传成功");
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
        return R.layout.activity_shipment_detail;
    }
}
