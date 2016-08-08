package com.example.keranbing.splash.splash;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.keranbing.splash.R;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.ProgressHelper.UIProgressListener;
import com.example.keranbing.splash.okHttp.HttpCon;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            downLoadFile();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.pbw);
        try {
            new Thread().sleep(3000);
            Message msg=new Message();
            handler.sendMessage(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        downLoadFile();
    }

    private void downLoadFile() {
        //构造请求
        final Request request1 = new Request.Builder()
                .url("http://alpha.ebao.ceionline.com.cn/travel/travel.apk")
                .build();

        HttpCon.ProgressOkHttpInstance(new UIProgressListener() {
            @Override
            public void onUIProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("TAG", "bytesRead:" + bytesRead);
                Log.e("TAG", "contentLength:" + contentLength);
                Log.e("TAG", "done:" + done);
                if (contentLength != -1) {
                    //长度未知的情况下回返回-1
                    Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
                }
                Log.e("TAG", "================================");
                //ui层回调
                progressBar.setProgress((int) ((100 * bytesRead) / contentLength));
                //Toast.makeText(getApplicationContext(), bytesRead + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUIStart(long currentBytes, long contentLength, boolean done) {
                super.onUIStart(currentBytes, contentLength, done);
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUIFinish(long currentBytes, long contentLength, boolean done) {
                super.onUIFinish(currentBytes, contentLength, done);
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
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

