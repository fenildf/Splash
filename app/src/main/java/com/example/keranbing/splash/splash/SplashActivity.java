package com.example.keranbing.splash.splash;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.keranbing.splash.R;
import com.example.keranbing.splash.helper.viewsHelper.dialogHelper.DialogHelper;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.ProgressHelper.UIProgressListener;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.view.NumberProgressBar;
import com.example.keranbing.splash.helper.viewsHelper.popWindowHelper.PopupWindowHelper;
import com.example.keranbing.splash.okHttp.HttpCon;
import com.example.keranbing.splash.others.AppVersion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by keranbing on 2016/8/5.
 */
public class SplashActivity extends Activity implements DialogHelper.DialogPromptListener {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123:
                   updateAppVersion(msg.obj.toString());
                    break;
                case 0x234:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getAppVersion();
    }

    private void getAppVersion() {
        Map<String, Object> map = new HashMap<>();
        //具体参数视服务器要求传的参数而定
        map.put("tid", "fsdfdsfsfs");
        HttpCon.Params(map, 0x123, handler);
    }


    private void updateAppVersion(String s) {
        try {
            if (!s.equals("")) {
                JSONObject jsonObject = new JSONObject(s);
                HttpCon.updateUrl = jsonObject.getString("url");
                if (AppVersion.compareVersion(jsonObject.getString("version"))) {
                    DialogHelper.createPromptDialog(SplashActivity.this,
                            R.layout.dialog_prompt,
                            R.id.btnRight,
                            R.id.btnLeft,
                            R.id.titledialog,
                            "检测到新版本，是否更新?",
                            99999,
                            this
                    );
                } else {


                }
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setOnPrompItemClick(int position) {
        PopupWindowHelper.createDownLoadProgressPopwindow(
                SplashActivity.this,
                SplashActivity.this.findViewById(R.id.rl),
                SplashActivity.this.getWindow(),
                R.layout.progress_download,
                R.id.tvPercentage
        );
        downLoadFile();
    }


    private void downLoadFile() {
        //构造请求
        final Request request1 = new Request.Builder()
//                .url("http://alpha.ebao.ceionline.com.cn/travel/travel.apk")
                .url(HttpCon.updateUrl)
                .build();
        HttpCon.ProgressOkHttpInstance(new UIProgressListener() {
            @Override
            public void onUIProgress(final long bytesRead, final long contentLength, boolean done) {
                Log.e("TAG", "bytesRead:" + bytesRead);
                Log.e("TAG", "contentLength:" + contentLength);
                Log.e("TAG", "done:" + done);
                if (contentLength != -1) {
                    //长度未知的情况下回返回-1
                    Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
                }
                Log.e("TAG", "================================");
                //ui层回调

                PopupWindowHelper.setProgress(new PopupWindowHelper.ProgressHelperListener() {
                    @Override
                    public void setProgress(NumberProgressBar numberProgressBar, TextView tvPercentage) {
                        numberProgressBar.setProgress((int) ((100 * bytesRead) / contentLength));
                        tvPercentage.setText((100 * bytesRead) / contentLength + "%");
                    }
                });

            }

            @Override
            public void onUIStart(long currentBytes, long contentLength, boolean done) {
                super.onUIStart(currentBytes, contentLength, done);
            }

            @Override
            public void onUIFinish(long currentBytes, long contentLength, boolean done) {
                super.onUIFinish(currentBytes, contentLength, done);
                /*
                * 下载完毕，关闭popupwindow
                * */
                PopupWindowHelper.dismissProgress(new PopupWindowHelper.ProgressHelperDismissListener() {
                    @Override
                    public void dismissProgress(PopupWindow popupWindow) {
                        popupWindow.dismiss();
                    }
                });
            }

        }).newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UpdateApp(response);
            }
        });
    }


    /*
    * 下载成功，安装新版本的App
    * */
    public  void UpdateApp(Response response) {
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
        InputStream inputStream = response.body().byteStream();
        int len = 0;
        int lens = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            while ((len = inputStream.read(buff)) != -1) {
                Log.i("dd", len + "");
                fileOutputStream.write(buff, 0, len);
                lens += len;
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
       startActivity(intent);
    }
    


}
