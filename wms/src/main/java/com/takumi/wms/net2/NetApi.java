package com.takumi.wms.net2;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface NetApi {

    @GET("{path}")
    Observable<Response<ResponseBody>> getNoParam(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<Response<ResponseBody>> getHasParam(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, String> map);

    @POST("{path}")
    Observable<Response<ResponseBody>> postHasBody(@Path(value = "path", encoded = true) String path, @Body Object o);

    @POST("{path}")
    Observable<Response<ResponseBody>> postHasParam(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, String> map);

    @PUT("{path}")
    Observable<Response<ResponseBody>> putHasBody(@Path(value = "path", encoded = true) String path, @Body Object o);

    @PATCH("{path}")
    Observable<Response<ResponseBody>> patchHasBody(@Path(value = "path", encoded = true) String path, @Body Object o);

    @DELETE("{path}")
    Observable<Response<ResponseBody>> deleteNoBody(@Path(value = "path", encoded = true) String path);

    @DELETE("{path}")
    Observable<Response<ResponseBody>> deleteParam(@Path(value = "path", encoded = true) String path,@QueryMap Map<String,Object> map);
}
