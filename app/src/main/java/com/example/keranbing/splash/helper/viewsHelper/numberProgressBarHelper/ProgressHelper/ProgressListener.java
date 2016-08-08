package com.example.keranbing.splash.helper.viewsHelper.numberProgressBarHelper.ProgressHelper;

/**
 * Created by keranbin on 2016/8/2.
 */
public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
