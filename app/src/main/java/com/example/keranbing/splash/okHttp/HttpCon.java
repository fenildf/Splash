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

    public static String updateUrl = "";
    private static String URL = " http://10.11.5.229:8080/kqgl/ajax.mobileSword";
//    private static String URL = "http://10.11.5.191:8080/qxgl/ajax.mobileSword";
//    private static String URL = "http://10.25.1.15:8080/kqgl/ajax.mobileSword";
//    private static String URL = "http://alpha.kqgl.ceionline.com.cn/kqgl/ajax.mobileSword";
//    private static String URL = "http://beta.kqgl.ceionline.com.cn/kqgl/ajax.mobileSword";
//    private static String URL ="http://kqgl.ceionline.com.cn/kqgl/ajax.mobileSword";


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

    private static void showDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("开启网络服务");
        builder.setMessage("网络没有连接，请到设置进行网络设置！");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                            context.startActivity(new Intent(
                                    android.provider.Settings.ACTION_SETTINGS));
                        } else {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public static void save(Response response) {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "/download/attence.apk");
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        byte[] buff = new byte[1024 * 4];
        long total = response.body().contentLength();
        InputStream inputStream = response.body().byteStream();
        int len = 0;
        int progress = 0;
        int lens = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            while ((len = inputStream.read(buff)) != -1) {
                Log.i("dd", len + "");
                fileOutputStream.write(buff, 0, len);
                lens += len;
                progress = (int) (lens * 100 / total);
                Message message = handler.obtainMessage();
                message.what = 200;
                message.arg1 = progress;
                handler.sendMessage(message);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Handler handler = null;


    public static void downloadFile(ProgressListener progressListener) {
        Request request = new Request.Builder().url(updateUrl).build();
        ProgressOkHttpInstance(progressListener).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("onUIProgress", "下载成功");
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

