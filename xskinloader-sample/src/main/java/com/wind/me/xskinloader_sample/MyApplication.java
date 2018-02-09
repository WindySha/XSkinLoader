package com.wind.me.xskinloader_sample;

import android.app.Application;

import com.wind.me.xskinloader.SkinManager;

/**
 * Created by Windy on 2018/2/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DeployRegister.init();
        SkinManager.get().init(this);
    }
}
