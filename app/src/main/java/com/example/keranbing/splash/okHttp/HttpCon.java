package com.example.keranbing.splash.okHttp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.keranbing.splash.App;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.ProgressHelper.ProgressListener;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.ProgressHelper.ProgressResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by houyanxu on 2016/6/14.
 */
public class HttpCon {
    private static OkHttpClient okHttpClient = App.OkHttpInstance();

    //新版本App下载地址
    public static String updateUrl = "";
    //与服务器打交道的地址
    private static String URL = "";


    public static void Params(Map<String, Object> map, final int code, final Handler handler) {
        Log.i("参数map", map.toString());
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Log.i("参数:", entry.getKey() + ":" + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(URL)
                .header("Cache-Control", " max-stale=600")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("网络错误", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string().toString();
                Log.i("数据请求结果:", s);
                Message message = handler.obtainMessage();
                message.what = code;
                message.obj = s;
                handler.sendMessage(message);
            }

        });
    }


    public static OkHttpClient ProgressOkHttpInstance(final ProgressListener progressListener) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        };
        return App.OkHttpInstance().newBuilder()
                .addInterceptor(interceptor)
                .build();
    }


}

