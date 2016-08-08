package com.example.keranbing.splash.helper.viewsHelper.popWindowHelper;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.keranbing.splash.R;
import com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.view.NumberProgressBar;

/**
 * Created by keranbin on 2016/8/7.
 */
public class PopWindowHelper {
    public static NumberProgressBar numberProgressBar;
    public static TextView tvPercentage;


    public static void createDownLoadProgressPopwindow(Activity context, View parent, final Window window, int layout, int tvPercentageId) {
        final View view = LayoutInflater.from(context).inflate(layout, null);
        numberProgressBar= (NumberProgressBar) view.findViewById(R.id.progressBar);
        tvPercentage= (TextView) view.findViewById(R.id.tvPercentage);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //设置MyPopupWindow的View
        popupWindow.setContentView(view);
        //设置MyPopupWindow的View弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置MyPopupWindow的View弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置MyPopupWindow的View弹出窗体可点击,如果不添加这个属性，那么点击EditText无法弹出输入法
        popupWindow.setFocusable(true);
        //设置MyPopupWindow去除边际黑线
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //避免输入法覆盖掉popWindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        setWindowGray(popupWindow, window);


    }


    public static void setWindowGray(PopupWindow popupWindow, Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        if (popupWindow.isShowing()) {
            lp.alpha = 0.5f;
            window.setAttributes(lp);
        } else {
            lp.alpha = 1.0f;
            window.setAttributes(lp);
        }
    }

    public static void setProgress(ProgressHelperListener progressHelperListener) {
      progressHelperListener.setProgress(numberProgressBar,tvPercentage);
    }

    public interface ProgressHelperListener{
        public void setProgress(NumberProgressBar numberProgressBar, TextView tvPercentage);
    }

}

