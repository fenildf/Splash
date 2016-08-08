package com.example.keranbing.splash;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by keranbing on 2016/8/5.
 */
public class App extends Application {
    public static App app;
    public static OkHttpClient okHttpClient;


    public static Context context;
    private static List<Activity> list=new ArrayList<>();



    public static void addActivity(Activity activity){
        list.add(activity);
    }

    public static Context context(){
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app=this;

    }


    public static OkHttpClient OkHttpInstance(){

        if (okHttpClient==null){
            okHttpClient=new OkHttpClient().newBuilder()
                    .connectTimeout(1000, TimeUnit.MILLISECONDS)
                    .readTimeout(5000,TimeUnit.MILLISECONDS)
                    .writeTimeout(5000,TimeUnit.MILLISECONDS)
                    .cache(new Cache(app.getExternalCacheDir(),10*1024*1024))
                    .build();
            return  okHttpClient;
        }else {
            return okHttpClient;
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }


}

