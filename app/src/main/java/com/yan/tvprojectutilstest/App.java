package com.yan.tvprojectutilstest;

import android.app.Application;

import com.yan.tvprojectutils.DensityHelper;

/**
 * Created by yan on 2017/8/7.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DensityHelper.activate(getApplicationContext(), 1280);
    }
}
