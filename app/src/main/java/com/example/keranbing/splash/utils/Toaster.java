package com.example.keranbing.splash.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by houyanxu on 2016/8/3.
 */
public class Toaster {
    public static Toast mToast;
    public static Context context;
    public static void showMsg(String msg){
        if (mToast==null){
            mToast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
