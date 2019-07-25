package com.wind.me.xskinloader_sample;

import android.app.Application;
import android.view.LayoutInflater;

import com.wind.me.xskinloader.SkinInflaterFactory;

/**
 * Created by Windy on 2018/2/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 处理自定义的换肤属性
        ExtraAttrRegister.init();

        // 使用Application的LayoutInflater加载的view也能换肤
        SkinInflaterFactory.setFactory(LayoutInflater.from(this));
    }
}
