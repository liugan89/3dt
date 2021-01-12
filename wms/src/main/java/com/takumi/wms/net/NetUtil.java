package com.takumi.wms.net;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutDto;
import com.takumi.wms.model.dto.inout.InOutCommandDtos;
import com.takumi.wms.model.dto.lot.CreateOrMergePatchLotDto;
import com.takumi.wms.model.dto.movement.CreateOrMergePatchMovementDto;
import com.takumi.wms.model.dto.movement.MovementCommandDtos;
import com.takumi.wms.model.dto.order.OrderCommandDtos;
import com.takumi.wms.model.dto.production.CreateOrMergePatchProductionDto;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentDto;
import com.takumi.wms.model.dto.shipment.OrderShipGroupServiceCommandDtos;
import com.takumi.wms.model.dto.shipment.ShipmentCommandDtos;
import com.takumi.wms.utils.converter.DateConverter;
import com.takumi.wms.utils.converter.TimestampConverter;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.utils.converter.DoubleTypeAdapter;
import com.takumi.wms.utils.converter.FloatTypeAdapter;
import com.takumi.wms.utils.converter.IntegerTypeAdapter;
import com.takumi.wms.utils.converter.LongTypeAdapter;
import com.takumi.wms.widget.LoadingDialog;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NetUtil {
    private static NetUtil utils = new NetUtil(null);
    private NetApi netApi;
    private Gson gson;
    private int count = 0;
    private static LoadingDialog ld;
    private OkHttpClient client;
    private Retrofit retrofit;

    public static NetUtil getInstance() {
        return utils;
    }

    private NetUtil(String authToken) {
        //gson = new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.PUBLIC).create();//屏蔽所有为public的对象
        gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampConverter()).registerTypeAdapter(Date.class, new DateConverter())
                //.registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                //.registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(double.class, new DoubleTypeAdapter())
                //.registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(long.class, new LongTypeAdapter())
                //.registerTypeAdapter(Float.class, new FloatTypeAdapter())
                .registerTypeAdapter(float.class, new FloatTypeAdapter()).create();
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder().addHeader("Content-Type", "application/json;charset=utf-8").method(original.method(), original.body());

                if (!TextUtils.isEmpty(authToken)) {
                    requestBuilder.addHeader("Authorization", "Bearer " + authToken);
                }
                Request request = requestBuilder.build();
                okhttp3.Response response = chain.proceed(request);
                return response;
            }
        }).build();
        retrofit = new Retrofit.Builder().baseUrl(NetService.BaseURL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        netApi = retrofit.create(NetApi.class);
    }

    public void setToken(String authToken) {
        utils = new NetUtil(authToken);
    }


    /**
     * 无body，有返回值的方法
     *
     * @param context
     * @param map
     * @param id
     * @param callBack
     * @param <T>
     */
    public <T> void NoBody_Response(final Context context, Map<String, String> map, String id, String fun, final MyCallBack<T> callBack) {
        showDialog(context);
        Observable<T> ob = getFun(id, fun, map, null);
        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                HttpException he = null;
                if (e instanceof HttpException) {
                    he = (HttpException) e;
                }
                String m = e.getMessage();
                if (!callBack.onError(m)) {
                    if ("End of input at line 1 column 1 path $".equals(m)) {
                        ToastUtil.showToast(context, "暂无数据");
                    } else {
                        if (he != null) {
                            int code = he.code();
                            if (code >= 500) {
                                ToastUtil.showToast(context, "系统错误");
                            } else if (code == 403) {
                                ToastUtil.showToast(context, "重新登录");
                            } else {
                                ToastUtil.showToast(context, "请求发生" + code + "错误");
                            }
                        } else {
                            ToastUtil.showToast(context, "系统错误");
                        }
                    }
                }
                dismissDialog();
            }

            @Override
            public void onNext(T t) {
                callBack.onSuccess(t);
                dismissDialog();
            }
        });
    }

    private List<Observable> obs = new ArrayList<>();

    /**
     * 无body，有返回值的方法
     *
     * @param context
     * @param map
     * @param id
     * @param callBack
     * @param <T>
     */
    public <T> void NoDialogNoBody_Response(final Context context, Map<String, String> map, String id, String fun, final MyCallBack<T> callBack) {
        Observable<T> ob = getFun(id, fun, map, null);
        obs.add(ob);
        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                HttpException he = null;
                if (e instanceof HttpException) {
                    he = (HttpException) e;
                }
                String m = e.getMessage();
                if (!callBack.onError(m)) {
                    if ("End of input at line 1 column 1 path $".equals(m)) {
                        ToastUtil.showToast(context, "暂无数据");
                    } else {
                        if (he != null) {
                            int code = he.code();
                            if (code >= 500) {
                                ToastUtil.showToast(context, "系统错误");
                            } else if (code == 403) {
                                ToastUtil.showToast(context, "重新登录");
                            } else {
                                ToastUtil.showToast(context, "请求发生" + code + "错误");
                            }
                        } else {
                            //ToastUtil.showToast(context, fun + "_" + m);
                            ToastUtil.showToast(context, "系统错误");
                        }
                    }
                }
                obs.remove(ob);
            }

            @Override
            public void onNext(T t) {
                callBack.onSuccess(t);
                obs.remove(ob);
            }
        });
    }

    private void dismissDialog() {
        if (--count <= 0) {
            ld.dismiss();
        }
    }

    private void showDialog(Context context) {
        count++;
        if (ld == null || !ld.isShowing()) {
            ld = new LoadingDialog(context);
            ld.show();
        }
    }


    /**
     * 有body，无返回值的方法
     *
     * @param context
     * @param o
     * @param id
     * @param callBack
     */
    public void Body_NoResponse(final Context context, Object o, String id, String fun, final MyCallBack<Response<ResponseBody>> callBack) {
        showDialog(context);
        Observable<Response<ResponseBody>> ob = getFun(id, fun, null, o);
        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!callBack.onError(e.getMessage())) {
                    ToastUtil.showToast(context, fun + "_" + e.getMessage());
                }
                dismissDialog();
            }

            @Override
            public void onNext(Response<ResponseBody> t) {
                int code = t.code();
                if (code == 200 || code == 201) {
                    callBack.onSuccess(t);
                } else {
                    if (!callBack.onError(code + "")) {
                        if (code >= 500) {
                            ToastUtil.showToast(context, "系统错误");
                        } else if (code == 403) {
                            ToastUtil.showToast(context, "重新登录");
                        } else {
                            ToastUtil.showToast(context, "请求发生" + code + "错误");
                        }
                    }
                }
                dismissDialog();
            }
        });
    }

    /**
     * 无body，无返回值的方法
     *
     * @param context
     * @param o
     * @param id
     * @param callBack
     */
    public void NoBody_NoResponse(final Context context, Map<String, String> o, String id, String fun, final MyCallBack<Response<ResponseBody>> callBack) {
        showDialog(context);
        Observable<Response<ResponseBody>> ob = getFun(id, fun, o, null);
        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!callBack.onError(e.getMessage())) {
                    ToastUtil.showToast(context, fun + "_" + e.getMessage());
                }
                dismissDialog();
            }

            @Override
            public void onNext(Response<ResponseBody> t) {
                int code = t.code();
                if (code == 200 || code == 201) {
                    callBack.onSuccess(t);
                } else {
                    if (!callBack.onError(code + "")) {
                        if (code >= 500) {
                            ToastUtil.showToast(context, "系统错误");
                        } else if (code == 403) {
                            ToastUtil.showToast(context, "重新登录");
                        } else {
                            ToastUtil.showToast(context, "请求发生" + code + "错误");
                        }
                    }
                }
                dismissDialog();
            }
        });
    }

    public void cancelConnect() {
        for (Observable ob : obs) {
            ob.subscribe().isUnsubscribed();
        }
    }

    /**
     * 有body，有返回值的方法
     *
     * @param context
     * @param o
     * @param id
     * @param callBack
     */
    public <T> void Body_Response(final Context context, Object o, String id, String fun, final MyCallBack<T> callBack) {
        showDialog(context);
        Observable<T> ob = getFun(id, fun, null, o);

        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!callBack.onError(e.getMessage())) {
                    ToastUtil.showToast(context, fun + "_" + e.getMessage());
                }
                dismissDialog();
            }

            @Override
            public void onNext(T t) {
                callBack.onSuccess(t);
                dismissDialog();
            }
        });
    }

    /**
     * 根据map得到请求
     *
     * @param id
     * @param map
     * @param <T>
     * @return
     */
    private <T> Observable<T> getFun(String id, String fun, Map<String, String> map, Object o) {
        Object ob = null;
        switch (fun) {
            case NetService.getProducts:
                ob = map == null ? netApi.getProducts(id) : netApi.getProducts(id, map);
                break;
            case NetService.getInOutDocuments:
                ob = map == null ? netApi.getInOutDocuments(id) : netApi.getInOutDocuments(id, map);
                break;
            case NetService.getAttrSet:
                ob = map == null ? netApi.getAttrSet(id) : netApi.getAttrSet(id, map);
                break;
            case NetService.getLineInfo:
                ob = netApi.getLineInfo(id);
                break;
            case NetService.getInOutDocument:
                ob = map == null ? netApi.getInOutDocument(id) : netApi.getInOutDocument(id, map);
                break;
            case NetService.getWarehouses:
                ob = netApi.getWarehouses(id, map);
                break;
            case NetService.getLocators:
                ob = netApi.getLocators(id, map);
                break;
            case NetService.CommitInOut:
                ob = netApi.CommitInOut(id, (InOutCommandDtos.CompleteRequestContent) o);
                break;
            case NetService.newInOut:
                ob = netApi.newInOut(id, (CreateOrMergePatchInOutDto) o);
                break;
            case NetService.AddLineFun:
                ob = netApi.AddLineFun(id, (InOutCommandDtos.AddLineRequestContent) o);
                break;
            case NetService.DeleteInOutLine:
                ob = netApi.DeleteInOutLine(id, (CreateOrMergePatchInOutDto.MergePatchInOutDto) o);
                break;
            case NetService.getLots:
                ob = map == null ? netApi.getLots(id) : netApi.getLots(id, map);
                break;
            case NetService.addLot:
                ob = netApi.addLot(id, (CreateOrMergePatchLotDto) o);
                break;
            case NetService.getMovements:
                ob = netApi.getMovements(id, map);
                break;
            case NetService.createNewMovement:
                ob = netApi.createNewMovement(id, (CreateOrMergePatchMovementDto.CreateMovementDto) o);
                break;
            case NetService.getMandatoryAtts:
                ob = netApi.getMandatoryAtts(id);
                break;
            case NetService.AddMovementLine:
                ob = netApi.AddMovementLine(id, (MovementCommandDtos.AddLineRequestContent) o);
                break;
            case NetService.DeleteMovementLine:
                ob = netApi.DeleteMovementLine(id, (CreateOrMergePatchMovementDto.MergePatchMovementDto) o);
                break;
            case NetService.CommitMovement:
                ob = netApi.CommitMovement(id, (MovementCommandDtos.DocumentActionRequestContent) o);
                break;
            case NetService.getShipments:
                ob = netApi.getShipments(id, map);
                break;
            case NetService.AddLineImage:
                ob = netApi.AddLineImage(id, (CreateOrMergePatchInOutDto.MergePatchInOutDto) o);
                break;
            case NetService.AddInOutDocImage:
                ob = netApi.AddInOutDocImage(id, (CreateOrMergePatchInOutDto.MergePatchInOutDto) o);
                break;
            case NetService.getProductAtt:
                ob = netApi.getProductAtt(id);
                break;
            case NetService.getOutInventoryAtt:
                ob = netApi.getOutInventoryAtt(id, map);
                break;
            case NetService.getStatusItem:
                ob = netApi.getStatusItem(id);
                break;
            case NetService.getProduct:
                ob = netApi.getProduct(id);
                break;
            case NetService.getShipment:
                ob = netApi.getShipment(id, map);
                break;
            case NetService.CommitShipmentItem:
                ob = netApi.CommitShipmentItem(id, (ShipmentCommandDtos.ReceiveItemRequestContent) o);
                break;
            case NetService.CommitShipment:
                ob = netApi.CommitShipment(id, (ShipmentCommandDtos.ConfirmAllItemsReceivedRequestContent) o);
                break;
            case NetService.getShipmentVersion:
                ob = netApi.getShipmentVersion(id, map);
                break;
            case NetService.getInOutDocumentVersion:
                ob = netApi.getInOutDocumentVersion(id, map);
                break;
            case NetService.AddShipmentImage:
                ob = netApi.AddShipmentImage(id, (CreateOrMergePatchShipmentDto.MergePatchShipmentDto) o);
                break;
            case NetService.getBlurId:
                ob = map == null ? netApi.getBlurId(id) : netApi.getBlurId(id, map);
                break;
            case NetService.getContractByNotice:
                ob = netApi.getContractByNotice(id, map);
                break;
            case NetService.RelateNotice:
                ob = netApi.RelateNotice(id, (CreateOrMergePatchShipmentDto.MergePatchShipmentDto) o);
                break;
            case NetService.createPOShipment:
                ob = netApi.createPOShipment(id, (OrderShipGroupServiceCommandDtos.CreatePOShipmentDto) o);
                break;
            case NetService.login:
                ob = netApi.login_debug(id, map);
                /*if (BuildConfig.isDebug) {
                } else {
                    ob = netApi.login_release(id, map);
                }*/
                break;
            case NetService.AddShipmentItem:
                ob = netApi.AddShipmentItem(id, (ShipmentCommandDtos.ReceiveItemRequestContent) o);
                break;
            case NetService.getDocumentLineById:
                ob = netApi.getDocumentLineById(id, map);
                break;
            case NetService.createSOShipment2:
                //ob = netApi.createSOShipment2(id, (OrderShipGroupServiceCommandDtos.CreateSOShipmentDto) o);
                ob = netApi.createSOShipment(id, (OrderShipGroupServiceCommandDtos.CreateSOShipmentDto) o);
                break;
            case NetService.CommitShipmentOut:
                ob = netApi.CommitShipmentOut(id, (ShipmentCommandDtos.ConfirmAllItemsIssuedRequestContent) o);
                break;
            case NetService.CommitShipmentItemOut:
                ob = netApi.CommitShipmentItemOut(id, (ShipmentCommandDtos.IssueItemRequestContent) o);
                break;
            case NetService.changeShipmentStatus:
                ob = netApi.changeShipmentStatus(id, (OrderShipGroupServiceCommandDtos.ShipPOShipmentDto) o);
                break;
            case NetService.getOrderShipGroup:
                ob = netApi.getOrderShipGroup(id, map);
                break;
            case NetService.getNoticeVersion:
                ob = netApi.getNoticeVersion(id);
                break;
            case NetService.completeNotice:
                ob = netApi.completeNotice(id, (OrderCommandDtos.OrderShipGroupActionRequestContent) o);
                break;
            case NetService.getNoticeInfo:
                ob = netApi.getNoticeInfo(id, map);
                break;
            case NetService.getInOutNotices:
                ob = netApi.getInOutNotices(id, map);
                break;
            case NetService.getNoticeInf:
                ob = netApi.getNoticeInf(id);
                break;
            case NetService.createSOShipment:
                ob = netApi.createSOShipment(id, (OrderShipGroupServiceCommandDtos.CreateSOShipmentDto) o);
                break;
            case NetService.getOutShipment:
                ob = netApi.getOutShipment(id, map);
                break;
            case NetService.getInfor:
                ob = netApi.getInfor(id, map);
                break;
            case NetService.DeleteShipmentOutLineItem:
                ob = netApi.DeleteShipmentOutLineItem(id);
                break;
            case NetService.deleteShipmentInItem:
                ob = netApi.deleteShipmentInItem(id, map);
                break;
            case NetService.getCheckProduct:
                ob = netApi.getCheckProduct(id, map);
                break;
            case NetService.getProductName:
                ob = netApi.getProductName(id);
                break;
            case NetService.shipmentPush:
                ob = netApi.shipmentPush(id, map);
                break;
            case NetService.getProductionList:
                ob = netApi.getProductionList(id, map);
                break;
            case NetService.createListProduction:
                ob = netApi.createListProduction(id, (CreateOrMergePatchProductionDto.CreateProductionDto) o);
                break;

        }
        return (Observable<T>) ob;
    }

    //这是一个回调借口
    public interface MyCallBack<T> {
        void onSuccess(T result);

        boolean onError(String msg);
    }

    /**
     * 生成接口路径
     *
     * @param ss
     * @return
     */
    public static String makeId(boolean api, String... ss) {
        StringBuilder sb = new StringBuilder();
        if (api) {
            sb.append("api/");
        }
        for (String s : ss) {
            sb.append(s);
            sb.append("/");
        }
        return sb.substring(0, sb.length() - 1);
    }

}