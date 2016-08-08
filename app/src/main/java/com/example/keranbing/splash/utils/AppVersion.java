package com.example.keranbing.splash.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.keranbing.splash.App;


/**
 * Created by houyanxu on 2016/6/29.
 */
public class AppVersion {
    public static boolean compareVersion(String newVersion){
        PackageManager packageManager= App.context().getPackageManager();
        try {
            PackageInfo packag=packageManager.getPackageInfo(App.context().getPackageName(),0);
            int oldVersionCode=packag.versionCode;
            String oldVersionName=packag.versionName;
            if (!newVersion.equals(oldVersionName)){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    };
}
