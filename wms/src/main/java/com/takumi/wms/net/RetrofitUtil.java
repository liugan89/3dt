package com.takumi.wms.net;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RetrofitUtil {
    public static OkHttpClient genericClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).build();
        client.interceptors().add(chain -> {
            Request request = chain.request();
            MediaType mediaType = request.body().contentType();
            try {
                Field field = mediaType.getClass().getDeclaredField("mediaType");
                field.setAccessible(true);
                field.set(mediaType, "application/json");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return chain.proceed(request);
        });
        return client;
    }
}

