package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.GridImageAdapter;
import com.takumi.wms.adapter.LotAdapter;
import com.takumi.wms.adapter.ProductAdapter;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.adapter.SpacesItemDecoration;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.Locator;
import com.takumi.wms.model.Lot;
import com.takumi.wms.model.OutDocumentInfo;
import com.takumi.wms.model.OutInventoryAtt;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.StatusItem;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.obj.OssFile;
import com.takumi.wms.utils.AttrInflater;
import com.takumi.wms.utils.BeanUtil;
import com.takumi.wms.utils.DateUtil;
import com.takumi.wms.utils.FileUtil;
import com.takumi.wms.utils.OSSUtil;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.DropEdit;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LineBaseActivty extends BottomActivity {
    @BindView(R.id.product_odd_title)
    TextView product_odd_title;
    @BindView(R.id.product_odd_number)
    TextView product_odd_number;
    @BindView(R.id.tv_line_id)
    TextView tv_line_id;
    @BindView(R.id.quanlity_content)
    TextView quanlity_content;

    @BindView(R.id.product_add)
    Button product_add;
    @BindView(R.id.serial_query)
    Button serial_query;


    @BindView(R.id.product_in_location)
    RelativeLayout product_in_location;
    @BindView(R.id.product_out_location)
    RelativeLayout product_out_location;

    @BindView(R.id.rv_image_upload)
    RecyclerView rv_image_upload;
    @BindView(R.id.ll_image_upload)
    LinearLayout ll_image_upload;
    @BindView(R.id.rv_image_download)
    RecyclerView rv_image_download;
    @BindView(R.id.ll_image_download)
    LinearLayout ll_image_download;
    @BindView(R.id.ll_line)
    LinearLayout ll_line;
    @BindView(R.id.ll_acceptedcount_layout)
    LinearLayout ll_acceptedcount_layout;
    @BindView(R.id.ll_line_info)
    LinearLayout ll_line_info;
    @BindView(R.id.ll_product)
    LinearLayout ll_product;
    @BindView(R.id.ll_product_info)
    LinearLayout ll_product_info;
    @BindView(R.id.serial_layout)
    LinearLayout serial_layout;
    @BindView(R.id.quanlity_layout)
    LinearLayout quanlity_layout;
    @BindView(R.id.describe_layout)
    LinearLayout describe_layout;
    @BindView(R.id.ll_count_layout)
    LinearLayout ll_count_layout;
    @BindView(R.id.ll_quanlity)
    LinearLayout ll_quanlity;
    @BindView(R.id.ll_bag_layout)
    LinearLayout ll_bag_layout;
    @BindView(R.id.damage_layout)
    LinearLayout damage_layout;
    @BindView(R.id.real_layout)
    LinearLayout real_layout;


    @BindView(R.id.describe_content)
    EditText describe_content;
    @BindView(R.id.product_quantity)
    EditText product_quantity;
    @BindView(R.id.serial_content)
    EditText serial_content;
    @BindView(R.id.et_product)
    DropEdit et_product;
    @BindView(R.id.et_prelocator)
    EditText et_prelocator;
    @BindView(R.id.et_targetlocator)
    EditText et_targetlocator;
    @BindView(R.id.product_acceptedcount)
    EditText product_acceptedcount;
    @BindView(R.id.product_bag)
    EditText product_bag;
    @BindView(R.id.damage_content)
    EditText damage_content;
    @BindView(R.id.real_content)
    EditText real_content;

    @BindView(R.id.product_modify)
    ImageView product_modify;
    @BindView(R.id.prelocator_scan)
    ImageView prelocator_scan;
    @BindView(R.id.targetlocator_scan)
    ImageView targetlocator_scan;
    @BindView(R.id.product_next)
    ImageView product_next;
    @BindView(R.id.targetlocator_next)
    ImageView targetlocator_next;
    @BindView(R.id.prelocator_next)
    ImageView prelocator_next;


    private LotAdapter lotAdapter;


    private Spinner product_lot_number;
    private EditText product_lot_time;
    private EditText product_lot_quantity;
    private Button product_new_lot;
    protected Button bt_reEntry;

    protected GridImageAdapter downloadGridImageAdapter;
    protected GridImageAdapter uploadGridImageAdapter;
    protected ProductAdapter productAdapter;


    protected LayoutInflater inflater;
    protected OSSUtil ou;


    protected InOutDocument o;
    protected List<StatusItem> statusItems;
    protected List<Lot> lots;
    protected Locator chooseTargetLocator;
    protected Locator choosePreLocator;
    protected Lot chooseLot;
    protected Product chooseProduct;
    protected InventoryAttribute inventoryAttribute;
    protected OutInventoryAtt outInventoryAtt;
    protected OutDocumentInfo.DocumentLinesItem bean;
    protected OutDocumentInfo outLine;

    private boolean isSerialScan = true;
    int lastposition;
    protected String serialNumber;

    protected final static int addLine = 1;
    protected final static int ProductScanRequest = 102;
    protected final static int PrelocatorScanRequest = 103;
    protected final static int TargetlocatorScanRequest = 104;
    protected static final int AddLotRequest = 105;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.white, true);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        o = getIntent().getParcelableExtra(Constant.Flags.Param1);
        bean = getIntent().getParcelableExtra(Constant.Flags.Param5);
        outLine = getIntent().getParcelableExtra(Constant.Flags.Param6);
        inflater = LayoutInflater.from(this);
        ou = new OSSUtil();
        initStatusItem();
        getLot();
        setBarShadow(false);
        product_odd_number.setText(o.getDocumentNumber());
        //product_odd_title.setText(fun.getName() + "单：");
        if (SingletonCache.locators != null && !SingletonCache.locators.isEmpty()) {
            choosePreLocator = SingletonCache.locators.get(0);
            chooseTargetLocator = SingletonCache.locators.get(0);
            et_targetlocator.setText(chooseTargetLocator.getLocatorId());
            et_prelocator.setText(choosePreLocator.getLocatorId());
        }

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
                        NetUtil.getInstance().NoDialogNoBody_Response(LineBaseActivty.this, null, id, NetService.getBlurId, new NetUtil.MyCallBack<List<String>>() {
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

        product_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //setBag();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void setBag() {
        try {
            if (chooseProduct != null && chooseProduct.getProductDepth() != 0 && "1".equals(chooseProduct.getInShippingBox())) {
                ll_bag_layout.setVisibility(View.VISIBLE);
                double d = Double.valueOf(product_quantity.getText().toString()) / chooseProduct.getProductDepth();
                int n;
                if ((d - (int) d) > 0) {
                    n = ((int) d) + 1;
                } else {
                    n = (int) d;
                }
                product_bag.setText(n + "");
            } else {
                ll_bag_layout.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
        }
    }

    protected void initStatusItem() {
        if (SingletonCache.statusItems == null) {
            getStatusItem();
        } else {
            showStatusItem();
        }
    }

    /**
     * 生成质量缺陷的StatusId数组，用于上传
     */
    @Nullable
    protected String[] getStatusArray() {
        List<String> list = new ArrayList<>();
        for (StatusItem i : statusItems) {
            if (i.isChecked()) {
                list.add(i.getStatusId());
            }
        }

        String[] l = new String[list.size()];
        if (list.size() > 0) {
            list.toArray(l);
        } else {
            l = null;
        }
        return l;
    }

    protected void getStatusItem() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.status, NetService.statusItem);
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getStatusItem, new NetUtil.MyCallBack<List<StatusItem>>() {
            @Override
            public void onSuccess(List<StatusItem> result) {
                if (!BeanUtil.isNull(result)) {
                    SingletonCache.statusItems = result;
                    showStatusItem();
                }
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    /*protected void showStatusItem() {
        quanlity_content.setAdapter(new StatusItemAdapter(this));
        quanlity_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusItem = SingletonCache.statusItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    protected void showStatusItem() {
        if (statusItems == null) {
            statusItems = new ArrayList<>(SingletonCache.statusItems.size());
        }
        for (StatusItem i : SingletonCache.statusItems) {
            statusItems.add(new StatusItem(i.getStatusId(), i.getDescription()));
        }
        quanlity_content.setText("请选择质量缺陷");

    }

    /**
     * 获取所有批次
     */
    protected void getLot() {
        String id = NetUtil.makeId(true, NetService.Lots);
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getLots, new NetUtil.MyCallBack<List<Lot>>() {
            @Override
            public void onSuccess(List<Lot> result) {
                lots = result;
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    /**
     * 判断产品是否为批次管理的产品，若是，显示批次界面
     */
    protected void showLot() {
        //生成批号显示
        View v = inflater.inflate(R.layout.product_lot, null);
        product_lot_number = v.findViewById(R.id.product_lot_number);
        product_new_lot = v.findViewById(R.id.product_new_lot);
        product_lot_time = v.findViewById(R.id.product_lot_time);
        product_lot_quantity = v.findViewById(R.id.product_lot_quantity);
        lotAdapter = new LotAdapter(this, lots);
        product_lot_number.setAdapter(lotAdapter);
        product_lot_number.setSelection(0);
        product_lot_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    product_lot_time.setText(DateUtil.toDateTime(lots.get(position).getExpirationDate().getTime()));
                    product_lot_quantity.setText(lots.get(position).getQuantity() + "");
                    chooseLot = lots.get(position);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showToast(LineBaseActivty.this, "批次配置错误");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        product_new_lot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LineBaseActivty.this, AddLotActivity.class);
                startActivityForResult(i, AddLotRequest);
            }
        });
        ll_line_info.addView(v);
    }

    protected void removeLL_productView() {
        ll_line_info.removeAllViews();
        ll_product_info.removeAllViews();
        ou.getDownloadImages().clear();
        ou.getUploadImages().clear();
        downloadGridImageAdapter.notifyDataSetChanged();
        uploadGridImageAdapter.notifyDataSetChanged();
    }

    protected void initImageRV(boolean photoable, RecyclerView rv, final List<OssFile> images) {
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.addItemDecoration(new SpacesItemDecoration(40));
        if (rv == rv_image_download) {
            downloadGridImageAdapter = new GridImageAdapter(this, images, photoable);
            downloadGridImageAdapter.setClick(new RVItemClick() {
                @Override
                public void itemClick(int position) {
                    if (position == images.size()) {
                        onTakePhoto();
                    } else {
                        Intent i = new Intent(LineBaseActivty.this, ImageActivity.class);
                        i.putExtra(Constant.Flags.Param1, images.get(position));
                        i.putExtra(Constant.Flags.Param2, position);
                        i.putExtra(Constant.Flags.Param3, downloadGridImageAdapter.isPhotoable());
                        startActivityForResult(i, REQUEST_IMAGE_SHOW);
                    }
                }
            });
            rv.setAdapter(downloadGridImageAdapter);
        } else if (rv == rv_image_upload) {
            uploadGridImageAdapter = new GridImageAdapter(this, images, photoable);
            uploadGridImageAdapter.setClick(new RVItemClick() {
                @Override
                public void itemClick(int position) {
                    if (position == images.size()) {
                        showBottomDialog();
                    } else {
                        Intent i = new Intent(LineBaseActivty.this, ImageActivity.class);
                        i.putExtra(Constant.Flags.Param1, images.get(position));
                        i.putExtra(Constant.Flags.Param2, position);
                        i.putExtra(Constant.Flags.Param3, uploadGridImageAdapter.isPhotoable());
                        startActivityForResult(i, REQUEST_IMAGE_SHOW);
                    }
                }
            });
            rv.setAdapter(uploadGridImageAdapter);
        }
    }


    public void setPrelocatorChoosable(boolean canChoose) {
        et_prelocator.setEnabled(canChoose);
    }

    public void setTargetlocatorChoosable(boolean canChoose) {
        et_targetlocator.setEnabled(canChoose);
    }

    public void setProdctIdChoosable(boolean canChoose) {
        et_product.setEnabled(canChoose);
    }


    public void setShowInOutLocator(Boolean isIn) {
        if (isIn == null) {
            product_out_location.setVisibility(View.VISIBLE);
            product_in_location.setVisibility(View.VISIBLE);
        } else if (isIn) {
            product_out_location.setVisibility(View.GONE);
            product_in_location.setVisibility(View.VISIBLE);
        } else {
            product_out_location.setVisibility(View.VISIBLE);
            product_in_location.setVisibility(View.GONE);
        }
    }

    /**
     * 查看行项，显示行项属性
     *
     * @param info
     */
    protected void showLineInfo(DocumentLine info) {
        if (info != null) {
            et_product.setTextNoWatch(info.getProductId());
            product_quantity.setText(info.getCount() + "");
            et_prelocator.setText(info.getLocatorId());
            findPreLocator();
            if (info.getInventoryattribute() != null) {
                new AttrInflater(info, false, this, ll_line_info, false);
            }
        }
    }


    protected void showProdctInfo(Product productAttribute) {
        ll_product_info.removeAllViews();
        //product_quantity.setText("0");
        new AttrInflater(productAttribute, this, ll_product_info);
    }

    protected void getChoosePrelocatorById(String id) {
        choosePreLocator = null;
        for (Locator l : SingletonCache.locators) {
            if (id.equals(l.getLocatorId())) {
                choosePreLocator = l;
                break;
            }
        }
    }

    protected void setPreLocatorChooseful(boolean canChoose) {
        if (!canChoose) {
            et_prelocator.setEnabled(false);
            prelocator_next.setVisibility(View.GONE);
        } else {
            et_prelocator.setEnabled(true);
            prelocator_next.setVisibility(View.VISIBLE);
        }
    }

    protected void setTargetLocatorChooseful(boolean canChoose) {
        if (!canChoose) {
            et_targetlocator.setEnabled(false);
            targetlocator_next.setVisibility(View.GONE);
        } else {
            et_targetlocator.setEnabled(true);
            targetlocator_next.setVisibility(View.VISIBLE);
        }
    }


    protected void getChooseTargetlocatorById(String id) {
        chooseTargetLocator = null;
        for (Locator l : SingletonCache.locators) {
            if (id.equals(l.getLocatorId())) {
                chooseTargetLocator = l;
                break;
            }
        }
    }

    protected void addProduct(Product p) {
        if (SingletonCache.products == null) {
            SingletonCache.products = new ArrayList<>();
        }
        SingletonCache.products.add(p);
    }

    /**
     * 获取产品信息并显示
     */
    protected void getProduct(String productId, NetUtil.MyCallBack callBack) {
        String id = NetUtil.makeId(true, NetService.queries, NetService.productAttribute, productId);
        NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getProduct, callBack);
    }

    protected void setChooseProductNull() {
        chooseProduct = null;
        ll_product.setVisibility(View.GONE);
    }


    /*@OnClick(R.id.product_scan)
    void product_scan() {
        Intent i = new Intent(this, WeChatCaptureActivity.class);
        startActivityForResult(i, ProductScanRequest);
    }*/

    @OnClick(R.id.prelocator_scan)
    void prelocator_scan() {
        Intent i = new Intent(this, WeChatCaptureActivity.class);
        startActivityForResult(i, PrelocatorScanRequest);
    }

    @OnClick(R.id.targetlocator_scan)
    void targetlocator_scan() {
        Intent i = new Intent(this, WeChatCaptureActivity.class);
        startActivityForResult(i, TargetlocatorScanRequest);
    }

    @OnClick(R.id.serial_query)
    void serial_query() {
        Intent i = new Intent(this, WeChatCaptureActivity.class);
        product_add.setEnabled(true);
        startActivityForResult(i, SerialScanRequest);
    }

    @OnClick(R.id.product_next)
    void product_next() {
        dialogOneChoice();
    }

    @OnClick(R.id.ll_quanlity)
    void ll_quanlity() {
        dialogMoreChoice();
    }

    @OnClick(R.id.prelocator_next)
    void prelocator_next() {
        if (SingletonCache.locators != null) {
            final String items[] = new String[SingletonCache.locators.size()];
            for (int i = 0; i < SingletonCache.locators.size(); i++) {
                items[i] = SingletonCache.locators.get(i).getLocatorId();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择货位");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    choosePreLocator = SingletonCache.locators.get(which);
                    et_prelocator.setText(choosePreLocator.getLocatorId());
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

    @OnClick(R.id.targetlocator_next)
    void targetlocator_next() {
        if (SingletonCache.locators != null) {
            final String items[] = new String[SingletonCache.locators.size()];
            for (int i = 0; i < SingletonCache.locators.size(); i++) {
                items[i] = SingletonCache.locators.get(i).getLocatorId();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择货位");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    chooseTargetLocator = SingletonCache.locators.get(which);
                    et_targetlocator.setText(chooseTargetLocator.getLocatorId());
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

    protected boolean findPreLocator() {
        String locatorId = et_prelocator.getText().toString().trim();
        if (TextUtils.isEmpty(locatorId)) {
            ToastUtil.showToast(this, "货位不能为空");
            return false;
        }
        for (Locator l : SingletonCache.locators) {
            if (locatorId.equals(l.getLocatorId())) {
                choosePreLocator = l;
                break;
            }
        }
        if (choosePreLocator == null) {
            ToastUtil.showToast(this, "找不到该货位");
            return false;
        }
        return true;
    }

    protected boolean findTargetLocator() {
        String locatorId = et_targetlocator.getText().toString().trim();
        if (TextUtils.isEmpty(locatorId)) {
            ToastUtil.showToast(this, "货位不能为空");
            return false;
        }
        if (SingletonCache.locators != null) {
            for (Locator l : SingletonCache.locators) {
                if (locatorId.equals(l.getLocatorId())) {
                    chooseTargetLocator = l;
                    break;
                }
            }
        }
        if (chooseTargetLocator == null) {
            ToastUtil.showToast(this, "找不到该货位");
            return false;
        }
        return true;
    }


    protected void getSerialOutInfo() {
        serialNumber = serial_content.getText().toString();
        String id = NetUtil.makeId(true, NetService.queries, NetService.InventoryAttribute, NetService.serialNumber);
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber", serial_content.getText().toString());
        map.put("warehouseId", Warehouse.getHouse().getWarehouseId());
        NetUtil.getInstance().NoBody_Response(LineBaseActivty.this, map, id, NetService.getOutInventoryAtt, new NetUtil.MyCallBack<OutInventoryAtt>() {
            @Override
            public void onSuccess(OutInventoryAtt result) {
                /*if (result.getAttributes().get("POReference") != null && bean != null && !result.getAttributes().get("POReference").equals(bean.getPoNumberItem())) {
                    ToastUtil.showToast(LineBaseActivty.this, "合同号不符");
                    return;
                }
                if (bean != null && !result.getProductId().equals(bean.getProductAttributeDto().getProductId())) {
                    ToastUtil.showToast(LineBaseActivty.this, "产品ID不匹配");
                    return;
                }*/
                outInventoryAtt = result;
                getChooseProductById(result.getProductId());
                product_quantity.setText(result.getCount() + "");
                real_content.setText(new BigDecimal(result.getCount()).multiply(new BigDecimal(0.997)).setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString());
                et_prelocator.setText(outInventoryAtt.getLocatorId());
                findPreLocator();
                prelocator_next.setVisibility(View.GONE);
                et_prelocator.setEnabled(false);
                String s = (String) result.getAttributes().get("ImageUrl");
                if (s != null) {
                    String[] ss = s.split(",");
                    ou.downloadPics(LineBaseActivty.this, ss, new NetUtil.MyCallBack() {
                        @Override
                        public void onSuccess(Object result) {
                            downloadGridImageAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public boolean onError(String msg) {
                            return false;
                        }
                    });
                }
                OutProductInfo(outInventoryAtt, false);
            }

            @Override
            public boolean onError(String msg) {
                ToastUtil.showToast(LineBaseActivty.this, "未查询到该卷号");
                return false;
            }
        });
    }

    protected boolean isSerialRepeat(String s) {
        if (outLine != null && outLine.getDocumentLines() != null) {
            for (OutDocumentInfo.DocumentLinesItem bean : outLine.getDocumentLines()) {
                if (bean.getItemissuance() != null) {
                    for (DocumentLine l : bean.getItemissuance()) {
                        if (l.getInventoryattribute().containsKey("serialNumber")) {
                            if (s.equals(l.getInventoryattribute().get("serialNumber").toString().toUpperCase())) {
                                Intent i = new Intent(this, ShowShipmentActivity.class);
                                i.putExtra(Constant.Flags.Param1, o);
                                i.putExtra(Constant.Flags.Param2, l);
                                i.putExtra(Constant.Flags.MainFlag, fun);
                                startActivity(i);
                                finish();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected void OutProductInfo(OutInventoryAtt result, boolean enable) {
        ll_line_info.removeAllViews();
        if (chooseProduct != null) {
            new AttrInflater(result, false, this, ll_line_info, 0);
        }
        if (!result.getDamageStatuss().isEmpty()) {
            quanlity_layout.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (StatusItem i : result.getDamageStatuss()) {
                i.setChecked(true);
                sb.append(i.getDescription() + ";");
            }
            sb.deleteCharAt(sb.length() - 1);
            quanlity_content.setText(sb.toString());

            setStatusItemCheckByMap(result.getDamageStatuss());
        }
    }

    protected void setStatusItemCheckByMap(List<StatusItem> result) {
        if (statusItems != null && !statusItems.isEmpty() && result != null) {
            for (int i = 0; i < statusItems.size(); i++) {
                for (int j = 0; j < result.size(); j++) {
                    if (statusItems.get(i).equals(result.get(j))) {
                        statusItems.get(i).setChecked(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                ou.getUploadImages().add(OssFile.createOssFile(FileUtil.getImageDir() + File.separator + photoUri.getLastPathSegment(), OSSUtil.makeKey(photoUri.getLastPathSegment()), true));
                uploadGridImageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_IMAGE_SHOW) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(Constant.Flags.Param1, -1);
                ou.getUploadImages().remove(position);
                uploadGridImageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_ALBUM) {
            if (resultCode == RESULT_OK) {
                ou.getUploadImages().add(OssFile.createOssFile(imageFile, OSSUtil.makeKey(imageFile.getName()), true));
                uploadGridImageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == SerialScanRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("number");
                    serial_content.setText(s);
                    if (fun.isOut()) {
                        onOutSerialScan(s);
                    }
                }
            }
        } else if (requestCode == AddLotRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Lot lot = data.getParcelableExtra(Constant.Flags.ResultParam1);
                    lots.add(lot);
                    lotAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    protected void onOutSerialScan(String s) {

    }

    protected String makeImageUrl() {
        if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (OssFile f : ou.getUploadImages()) {
                sb.append(f.getObjectUrl() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return null;
    }

    protected void setProductChoosable(boolean choosable) {
        setProdctIdChoosable(false);
        product_next.setVisibility(View.GONE);

    }

    protected void setShowModel() {
        quanlity_layout.setVisibility(View.GONE);
        describe_layout.setVisibility(View.GONE);
        setShowInOutLocator(true);
        setPrelocatorChoosable(false);
        setProdctIdChoosable(false);
        product_modify.setVisibility(View.GONE);
        prelocator_scan.setVisibility(View.GONE);
        product_next.setVisibility(View.GONE);
        targetlocator_scan.setVisibility(View.GONE);
        serial_query.setVisibility(View.GONE);
        ll_count_layout.setVisibility(View.GONE);
        serial_layout.setVisibility(View.GONE);
        product_quantity.setEnabled(false);
        setTitle("行项详情");
        ll_image_upload.setVisibility(View.GONE);
        product_add.setVisibility(View.GONE);
    }


    /**
     * 多选
     */
    protected void dialogMoreChoice() {
        final String items[] = new String[statusItems.size()];
        for (int i = 0; i < statusItems.size(); i++) {
            items[i] = statusItems.get(i).getDescription();
        }
        final boolean selected[] = new boolean[statusItems.size()];
        for (int i = 0; i < statusItems.size(); i++) {
            selected[i] = statusItems.get(i).isChecked();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("多选");
        builder.setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                statusItems.get(which).setChecked(isChecked);
                StringBuilder sb = new StringBuilder();
                for (StatusItem i : statusItems) {
                    if (i.isChecked()) {
                        sb.append(i.getDescription() + ";");
                    }
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                } else {
                    sb.append("请选择质量缺陷");
                }
                quanlity_content.setText(sb.toString());
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

    /**
     * 单选
     */
    protected void dialogOneChoice() {
        if (SingletonCache.products != null) {
            final String items[] = new String[SingletonCache.products.size()];
            for (int i = 0; i < SingletonCache.products.size(); i++) {
                items[i] = SingletonCache.products.get(i).getProductId();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("单选");
            builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
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

    protected void getChooseProductById(String id) {
        if (SingletonCache.products == null) {
            SingletonCache.products = new ArrayList<>();
        }
        setChooseProductNull();
        inventoryAttribute = null;
        for (Product p : SingletonCache.products) {
            if (id.equals(p.getProductId())) {
                chooseProduct = p;
                setChooseProduct(p);
                break;
            }
        }
        if (chooseProduct == null) {
            getProduct(id, new NetUtil.MyCallBack<Product>() {
                @Override
                public void onSuccess(Product result) {
                    chooseProduct = result;
                    addProduct(result);
                    setChooseProduct(result);
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }

    protected void setChooseProduct(Product p) {

    }

    protected void showMyProductInfo(DocumentLine info) {
        ou.downloadPics(this, info.getDocumentLineImageUrls(), new NetUtil.MyCallBack() {
            @Override
            public void onSuccess(Object result) {
                downloadGridImageAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
        if (SingletonCache.products != null) {
            boolean has = false;
            for (Product p : SingletonCache.products) {
                if (p.getProductId().equals(info.getProductId())) {
                    showProdctInfo(p);
                    has = true;
                    break;
                }
            }
            if (!has) {
                getProductByLine(info);
            }
        } else {
            getProductByLine(info);
        }
    }

    private void getProductByLine(DocumentLine d) {
        getProduct(d.getProductId(), new NetUtil.MyCallBack<Product>() {
            @Override
            public void onSuccess(Product result) {
                if (SingletonCache.products == null) {
                    SingletonCache.products = new ArrayList<>();
                }
                SingletonCache.products.add(result);
                showProdctInfo(result);
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (ou != null) {
            outState.putParcelable("ou", ou);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ou = savedInstanceState.getParcelable("ou");
    }*/

    @Override
    protected int getContentViewID() {
        return R.layout.activity_product;
    }
}
