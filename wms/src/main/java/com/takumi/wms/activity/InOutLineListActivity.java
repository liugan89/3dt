package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.LineGridImageAdapter;
import com.takumi.wms.adapter.LineListAdapter;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.adapter.SpacesItemDecoration;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentInfo;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.MandatoryAtts;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutDto;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutImageDto;
import com.takumi.wms.model.dto.inout.InOutCommandDtos;
import com.takumi.wms.model.dto.inout.RemoveInOutImageDto;
import com.takumi.wms.model.dto.movement.MovementCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.obj.OssFile;
import com.takumi.wms.utils.AttrInflater;
import com.takumi.wms.utils.BeanUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.OSSUtil;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.MessageDialog;
import com.takumi.wms.widget.SumGroup;

import java.io.IOException;
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

import static com.takumi.wms.net.NetService.getInOutDocument;

public class InOutLineListActivity extends BottomActivity {
    @BindView(R.id.btn_add_item)
    Button btn_add_item;
    @BindView(R.id.tv_odd_list_name)
    TextView tv_odd_list_name;
    @BindView(R.id.rv_odd_list)
    RecyclerView rv_odd_list;
    @BindView(R.id.rv_images)
    RecyclerView rv_images;
    @BindView(R.id.btn_odd_list_ok)
    Button btn_odd_list_ok;
    @BindView(R.id.tv_odd_list_number)
    TextView tv_odd_list_number;
    @BindView(R.id.RequiredSum)
    SumGroup RequiredSum;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    private InOutDocument o;
    private LineListAdapter adapter;
    private DocumentInfo infos;
    private MandatoryAtts requireds;
    private Map<String, Double> sum = new HashMap<>();
    private OSSUtil ou;
    private LineGridImageAdapter gridImageAdapter;

    private int imageFlag = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarShadow(false);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        initImmersionBar(R.color.white, true);
        setTitle(fun.getName());
        o = getIntent().getParcelableExtra(Constant.Flags.Param1);
        ou = new OSSUtil();
        initView();
    }

    private void initView() {
        tv_odd_list_name.setText(fun.getName() + "单：");
        tv_odd_list_number.setText(o.getDocumentNumber());
        LinearLayoutManager llm = new LinearLayoutManager(InOutLineListActivity.this);
        rv_odd_list.setLayoutManager(llm);
        rv_odd_list.addItemDecoration(new SpacesItemDecoration(10));
        rv_odd_list.setHasFixedSize(true);
        rv_odd_list.setNestedScrollingEnabled(false);
        initImages();
    }

    private void initImages() {
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
                    Intent i = new Intent(InOutLineListActivity.this, ImageActivity.class);
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

    private void getImages() {
        if (infos != null && infos.getDocumentImages() != null && !infos.getDocumentImages().isEmpty()) {
            ou.downloadPics(this, infos.getDocumentImages(), new NetUtil.MyCallBack() {
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

    @Override
    protected void onResume() {
        super.onResume();
        String id = "";
        switch (fun) {
            case In:
            case Out:
                id = NetUtil.makeId(true, NetService.queries, NetService.mandatoryAtts, NetService.inout, o.getDocumentNumber());
                break;
            case Movement:
                id = NetUtil.makeId(true, NetService.queries, NetService.mandatoryAtts, NetService.movement, o.getDocumentNumber());
                break;
        }
        getRequireds(id);
    }

    private void getRequireds(String id) {
        RequiredSum.removeAllViews();
        sum.clear();
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getMandatoryAtts, new NetUtil.MyCallBack<MandatoryAtts>() {
            @Override
            public void onSuccess(MandatoryAtts result) {
                RequiredSum.removeAllViews();
                requireds = result;
                Map<String, String> sum = new LinkedHashMap<>();
                sum.put("总数量", result.getCountSum() == null ? null : result.getCountSum() + "");
                sum.put("接收数量", result.getAcceptedCountSum() == null ? null : result.getAcceptedCountSum() + "");
                sum.put("总重量", result.getWeightKgSum() == null ? null : result.getWeightKgSum() + "");
                sum.put("接收重量", result.getAcceptedWeightKgSum() == null ? null : result.getAcceptedWeightKgSum() + "");
                new AttrInflater(sum, false, InOutLineListActivity.this, RequiredSum, false);
                getLines();
                getDocs(new NetUtil.MyCallBack<List<InOutDocument>>() {
                    @Override
                    public void onSuccess(List<InOutDocument> result) {
                        if (!BeanUtil.isNull(result)) {
                            InOutLineListActivity.this.o = result.get(0);
                        }
                    }

                    @Override
                    public boolean onError(String msg) {
                        System.out.println(msg);
                        return false;
                    }
                });
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    private void getDocs(NetUtil.MyCallBack<List<InOutDocument>> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("documentStatusId", "Drafted");
        map.put("documentTypeId", fun.getValue());
        map.put("documentNumber", o.getDocumentNumber());
        if (fun == Function.Movement) {
            map.put("warehouseIdFrom", Warehouse.getHouse().getWarehouseId());
        } else {
            map.put("WarehouseId", Warehouse.getHouse().getWarehouseId());
        }
        String id = NetUtil.makeId(true, NetService.InOuts);
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getInOutDocuments, callBack);
    }


    private void getLines() {
        String id = null;
        switch (fun) {
            case In:
            case Out:
                id = NetUtil.makeId(true, NetService.queries, NetService.document, NetService.inout, o.getDocumentNumber());
                break;
            case Movement:
                id = NetUtil.makeId(true, NetService.queries, NetService.document, NetService.movement, o.getDocumentNumber());
                break;
        }
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getLineInfo, new NetUtil.MyCallBack<DocumentInfo>() {
            @Override
            public void onSuccess(DocumentInfo result) {
                infos = result;
                if (ou.getDownloadImages().isEmpty()) {
                    getImages();
                }

                setRV();
            }

            @Override
            public boolean onError(String msg) {
                infos = null;
                setRV();
                return false;
            }
        });
    }

    private void setRV() {

        if (infos != null && infos.getDocumentLines() != null) {
            adapter = new LineListAdapter(infos.getDocumentLines(), fun);
            adapter.setRvItemClick(new RVItemClick() {
                @Override
                public void itemClick(int position) {
                    Intent intent = null;
                    switch (fun) {
                        case In:
                            intent = new Intent(InOutLineListActivity.this, ShowInLineActivity.class);
                            break;
                        case Out:
                            intent = new Intent(InOutLineListActivity.this, ShowOutLineActivity.class);
                            break;
                        case Movement:
                            intent = new Intent(InOutLineListActivity.this, ShowMovementLineActivity.class);
                            break;
                    }
                    intent.putExtra(Constant.Flags.MainFlag, fun);
                    intent.putExtra(Constant.Flags.Param1, o);
                    intent.putExtra(Constant.Flags.Param2, infos.getDocumentLines().get(position));
                    intent.putExtra(Constant.Flags.Param3, infos);
                    startActivityForResult(intent, addLine);
                }
            });
            rv_odd_list.setAdapter(adapter);
        } else {
            adapter = new LineListAdapter(null, fun);
            rv_odd_list.setAdapter(adapter);
        }
    }

    @OnClick({R.id.btn_add_item, R.id.btn_odd_list_ok})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_add_item:
                switch (fun) {
                    case In:
                        Intent intent1 = new Intent(this, AddInLineActivity.class);
                        intent1.putExtra(Constant.Flags.MainFlag, fun);
                        intent1.putExtra(Constant.Flags.Param1, o);
                        intent1.putExtra(Constant.Flags.Param3, infos);
                        startActivityForResult(intent1, addLine);
                        break;
                    case Out:
                        Intent intent2 = new Intent(this, AddOutLineActivity.class);
                        intent2.putExtra(Constant.Flags.MainFlag, fun);
                        intent2.putExtra(Constant.Flags.Param1, o);
                        intent2.putExtra(Constant.Flags.Param3, infos);
                        startActivityForResult(intent2, addLine);
                        break;
                    case Movement:
                        Intent intent = new Intent(this, AddMovementLineActivity.class);
                        intent.putExtra(Constant.Flags.MainFlag, fun);
                        intent.putExtra(Constant.Flags.Param1, o);
                        intent.putExtra(Constant.Flags.Param3, infos);
                        startActivityForResult(intent, addLine);
                        break;
                }

                break;
            case R.id.btn_odd_list_ok:
                showCommitDialog("确认提交" + fun.getName() + "单？");
                break;
        }
    }

    int i = -1;

    private boolean checkRepeat() {
        for (int i = 0; i < infos.getDocumentLines().size(); i++) {
            String serialNumber = null;
            if (infos.getDocumentLines().get(i).getInventoryattribute().containsKey("serialNumber")) {
                serialNumber = infos.getDocumentLines().get(i).getInventoryattribute().get("serialNumber").toString();
            }
            if (serialNumber != null) {
                for (int j = i + 1; j < infos.getDocumentLines().size(); j++) {
                    if (serialNumber.equals(infos.getDocumentLines().get(j).getInventoryattribute().get("serialNumber"))) {
                        this.i = i;
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private void showCommitDialog(String content) {
        AlertDialog dialog = MessageDialog.getDialog(this, fun.getName(), content, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        String id = NetUtil.makeId(true, fun == Function.Movement ? NetService.Movements : NetService.InOuts, o.getDocumentNumber());
                        Map<String, String> map = new HashMap<>();
                        map.put("fields", "version");
                        NetUtil.getInstance().NoBody_Response(InOutLineListActivity.this, map, id, getInOutDocument, new NetUtil.MyCallBack<InOutDocument>() {
                            @Override
                            public void onSuccess(InOutDocument result) {
                                switch (fun) {
                                    case In:
                                        commitInOutDoc(result.getVersion());
                                        break;
                                    case Out:
                                        if (checkRepeat()) {
                                            commitInOutDoc(result.getVersion());
                                        } else {
                                            AlertDialog dialog = MessageDialog.getCancelDialog(InOutLineListActivity.this, "发现重复项", "卷号：" + infos.getDocumentLines().get(i).getInventoryattribute().get("serialNumber").toString() + " ，是否继续提交？", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                        /*case DialogInterface.BUTTON_POSITIVE:
                                                            commitInOutDoc(result.getVersion());
                                                            i = -1;
                                                            dialog.dismiss();
                                                            break;*/
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            int scrollHeight = 0;
                                                            LinearLayoutManager llm = (LinearLayoutManager) rv_odd_list.getLayoutManager();
                                                            for (int i = 0; i < InOutLineListActivity.this.i; i++) {
                                                                View v = llm.getChildAt(i);
                                                                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) v.getLayoutParams();
                                                                scrollHeight += v.getHeight() + lp.bottomMargin + lp.topMargin;
                                                            }
                                                            nsv.scrollTo(0, scrollHeight);
                                                            i = -1;
                                                            dialog.dismiss();
                                                            break;
                                                    }
                                                }
                                            });
                                            dialog.show();
                                        }
                                        break;
                                    case Movement:
                                        commitMovementDoc(result.getVersion());
                                        break;
                                }
                            }

                            @Override
                            public boolean onError(String msg) {
                                return false;
                            }
                        });

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        });
        dialog.show();
    }

    private void commitInOutDoc(long version) {
        String id = NetUtil.makeId(true, NetService.InOuts, o.getDocumentNumber(), NetService._commands, NetService.Complete);
        InOutCommandDtos.CompleteRequestContent content = new InOutCommandDtos.CompleteRequestContent();
        content.setDocumentNumber(o.getDocumentNumber());
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(InOutLineListActivity.this, content, id, NetService.CommitInOut, new NetUtil.MyCallBack<Response<ResponseBody>>() {
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

    private void commitMovementDoc(long version) {
        String id = NetUtil.makeId(true, NetService.Movements, o.getDocumentNumber(), NetService._commands, NetService.DocumentAction);
        MovementCommandDtos.DocumentActionRequestContent content = new MovementCommandDtos.DocumentActionRequestContent();
        content.setDocumentNumber(o.getDocumentNumber());
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setValue("Complete");
        content.setVersion(version);
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(InOutLineListActivity.this, content, id, NetService.CommitMovement, new NetUtil.MyCallBack<Response<ResponseBody>>() {
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

    private final static int addLine = 1;//添加行项requestCode

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_TAKE_PHOTO || requestCode == REQUEST_ALBUM) {
            if (resultCode == RESULT_OK) {
                try {
                    final OssFile f = OssFile.createOssFile(imageFile, OSSUtil.makeKey(imageFile.getName()), true);
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
                                    getDocs(new NetUtil.MyCallBack<List<InOutDocument>>() {
                                        @Override
                                        public void onSuccess(List<InOutDocument> result) {
                                            if (!BeanUtil.isNull(result)) {
                                                InOutLineListActivity.this.o = result.get(0);
                                            }
                                            patchDocImage(f, PATCH);
                                        }

                                        @Override
                                        public boolean onError(String msg) {
                                            System.out.println(msg);
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
                    gridImageAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_SHOW) {
            if (resultCode == RESULT_OK) {
                final int position = data.getIntExtra(Constant.Flags.Param1, -1);
                getDocs(new NetUtil.MyCallBack<List<InOutDocument>>() {
                    @Override
                    public void onSuccess(List<InOutDocument> result) {
                        if (imageFlag == LineGridImageAdapter.Download) {
                            patchDocImage(ou.getDownloadImages().get(position), REMOVE);
                            ou.getDownloadImages().remove(position);
                            gridImageAdapter.notifyItemRemoved(position);
                        } else if (imageFlag == LineGridImageAdapter.Upload) {
                            patchDocImage(ou.getUploadImages().get(position - ou.getDownloadImages().size()), REMOVE);
                            ou.getUploadImages().remove(position - ou.getDownloadImages().size());
                            gridImageAdapter.notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public boolean onError(String msg) {
                        return false;
                    }
                });

            }
        }

    }

    public static final String REMOVE = "REMOVE";
    public static final String PATCH = "PATCH";

    private void patchDocImage(OssFile f, final String s) {
        CreateOrMergePatchInOutDto.MergePatchInOutDto patchInOut = new CreateOrMergePatchInOutDto.MergePatchInOutDto();
        patchInOut.setDocumentNumber(o.getDocumentNumber());//单号（Id）
        patchInOut.setVersion(o.getVersion());// set current version
        CreateOrMergePatchInOutImageDto dto = null;
        if (s.equals(REMOVE)) {
            dto = new RemoveInOutImageDto();
        } else if (s.equals(PATCH)) {
            dto = new CreateOrMergePatchInOutImageDto.CreateInOutImageDto();
        }
        dto.setSequenceId(f.getName());
        dto.setUrl(f.getObjectUrl());
        patchInOut.setInOutImages(new CreateOrMergePatchInOutImageDto[]{dto});

        String id = NetUtil.makeId(true, NetService.InOuts, o.getDocumentNumber());
        patchInOut.setCommandId(GUID.getUUID(patchInOut));
        NetUtil.getInstance().Body_NoResponse(this, patchInOut, id, NetService.AddInOutDocImage, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (s.equals(REMOVE)) {
                    ToastUtil.showToast(InOutLineListActivity.this, "删除成功");
                } else if (s.equals(PATCH)) {
                    ToastUtil.showToast(InOutLineListActivity.this, "上传成功");
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
        return R.layout.activity_inoutline_list;
    }
}
