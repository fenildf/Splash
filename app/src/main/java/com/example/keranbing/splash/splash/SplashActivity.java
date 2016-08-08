package com.example.keranbing.splash.splash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.keranbing.splash.R;
import com.example.keranbing.splash.helper.viewsHelper.dialogHelper.DialogHelper;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.ProgressHelper.UIProgressListener;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.view.NumberProgressBar;
import com.example.keranbing.splash.helper.viewsHelper.popWindowHelper.PopWindowHelper;
import com.example.keranbing.splash.okHttp.HttpCon;
import com.example.keranbing.splash.others.AppVersion;
import com.example.keranbing.splash.utils.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by keranbing on 2016/8/5.
 */
public class SplashActivity extends Activity implements DialogHelper.DialogPromptListener {
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        updateAppVersionTest();
    }




    private void updateAppVersionTest() {
        DialogHelper.createPromptDialog(SplashActivity.this,
                R.layout.dialog_prompt,
                R.id.btnRight,
                R.id.btnLeft,
                R.id.titledialog,
                "检测到新版本，是否更新?",
                99999,
                this
        );
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
                    Toaster.showMsg("已是最新版本，无需更新！");

                }
            } else {
                Toaster.showMsg("暂时没有更新信息！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setOnPrompItemClick(int position) {
        PopWindowHelper.createDownLoadProgressPopwindow(
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
                .url("http://alpha.ebao.ceionline.com.cn/travel/travel.apk")
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

                PopWindowHelper.setProgress(new PopWindowHelper.ProgressHelperListener() {
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
            }

        }).newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", response.body().string());
            }
        });
    }



}
