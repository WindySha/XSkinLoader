package com.wind.me.xskinloader_sample;

import android.app.Application;
import android.view.LayoutInflater;

import com.wind.me.xskinloader.SkinInflaterFactory;
import com.wind.me.xskinloader.SkinManager;

/**
 * Created by Windy on 2018/2/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ExtraAttrRegister.init();
        SkinInflaterFactory.setFactory(LayoutInflater.from(this));  // for skin change
        SkinManager.get().init(this);
    }
}
