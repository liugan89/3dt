package com.haphest.a3dtracking.net;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haphest.a3dtracking.utils.ToastUtil;
import com.haphest.a3dtracking.utils.converter.DateConverter;
import com.haphest.a3dtracking.utils.converter.DoubleTypeAdapter;
import com.haphest.a3dtracking.utils.converter.FloatTypeAdapter;
import com.haphest.a3dtracking.utils.converter.IntegerTypeAdapter;
import com.haphest.a3dtracking.utils.converter.LongTypeAdapter;
import com.haphest.a3dtracking.utils.converter.TimestampConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ikidou.reflect.TypeBuilder;
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

import static com.haphest.a3dtracking.widget.LoadingDialog.dismissDialog;
import static com.haphest.a3dtracking.widget.LoadingDialog.showDialog;


public class NetUtil {
    private static NetUtil utils = new NetUtil(null);
    private NetApi netApi;
    private Gson gson;
    private OkHttpClient client;
    private Retrofit retrofit;

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int PATCH = 3;
    public static final int DELETE = 4;

    public static NetUtil getInstance() {
        return utils;
    }

    private NetUtil(final String authToken) {
        gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampConverter()).registerTypeAdapter(Date.class, new DateConverter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .registerTypeAdapter(double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(long.class, new LongTypeAdapter())
                .registerTypeAdapter(float.class, new FloatTypeAdapter()).create();
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder().addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json,text/plain").method(original.method(), original.body());

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

    private List<Observable> obs = new ArrayList<>();

    public <T> void NoDialogCall(final Context context, String path, Map<String, String> map, Object o, int fun, final Type type, final MyCallBack<T> callBack) {
        final Observable<Response<ResponseBody>> ob = getFun(path, map, o, fun);
        obs.add(ob);
        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<ResponseBody>>() {
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
                                ToastUtil.showToast(context, "发生" + code + "错误");
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
            public void onNext(Response<ResponseBody> response) {
                try {
                    int code = response.code();
                    if (code == 200 || code == 201) {
                        if (type == null) {
                            callBack.onSuccess((T) response);
                        } else {
                            T t = gson.fromJson(response.body().string(), type);
                            callBack.onSuccess(t);
                        }
                    } else {
                        callBack.onError(response.message());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    obs.remove(ob);
                }
            }
        });
    }

    public synchronized void getVersion(Context context, String path, VersionBack listener) {
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");

        utils.DialogCall(context, path, m, null, GET, null, new MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                try {
                    String json = result.body().string();
                    JsonParser p = new JsonParser();
                    JsonObject jo = (JsonObject) p.parse(json);
                    long version = jo.get("version").getAsLong();
                    listener.versionDo(version);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    public synchronized <T> void DialogCall(final Context context, String path, Map<String, String> map, Object o, int fun, final Type type, final MyCallBack<T> callBack) {
        showDialog(context);
        final Observable<Response<ResponseBody>> ob = getFun(path, map, o, fun);
        obs.add(ob);
        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onCompleted() {
                System.out.println(1);
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
                dismissDialog();
            }

            @Override
            public void onNext(Response<ResponseBody> response) {
                try {
                    int code = response.code();
                    if (code == 200 || code == 201) {
                        if (type == null) {
                            callBack.onSuccess((T) response);
                        } else {
                            T t = gson.fromJson(response.body().string(), type);
                            callBack.onSuccess(t);
                        }
                    } else {
                        callBack.onError(response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onError(e.getMessage());
                } finally {
                    obs.remove(ob);
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
     * 根据map得到请求
     *
     * @param path
     * @param map
     * @return
     */
    private Observable<Response<ResponseBody>> getFun(String path, Map<String, String> map, Object o, int fun) {
        Observable<Response<ResponseBody>> ob = null;
        switch (fun) {
            case GET:
                if (map == null) {
                    ob = netApi.getNoParam(path);
                } else if (map != null) {
                    ob = netApi.getHasParam(path, map);
                }
                break;
            case POST:
                if (o != null) {
                    ob = netApi.postHasBody(path, o);
                } else if (map != null) {
                    ob = netApi.postHasParam(path, map);
                }
                break;
            case PUT:
                if (o != null) {
                    ob = netApi.putHasBody(path, o);
                }
                break;
            case PATCH:
                if (o != null) {
                    ob = netApi.patchHasBody(path, o);
                }
                break;
            case DELETE:
                if (o != null) {
                    ob = netApi.deleteNoBody(path);
                }
                break;
        }

        return ob;
    }

    public static Type createObjType(Class clazz) {
        return TypeBuilder.newInstance(clazz).build();
    }

    public static Type createPageType(Class clazz) {
        return TypeBuilder.newInstance(Page.class).addTypeParam(clazz).build();
    }

    public static Type createListType(Class clazz) {
        return TypeBuilder.newInstance(List.class).addTypeParam(clazz).build();
    }

    //这是一个回调借口
    public interface MyCallBack<T> {
        void onSuccess(T result);

        boolean onError(String msg);
    }

    public interface VersionBack {
        void versionDo(long version);
    }

}