package com.wind.me.xskinloader.skinDeployer;

import android.os.Build;
import android.view.View;
import android.view.Window;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.skinInterface.ISkinResDeployer;
import com.wind.me.xskinloader.skinInterface.ISkinResourceManager;
import com.wind.me.xskinloader.util.ReflectUtils;

/**
 * Created by Windy on 2018/1/10.
 */

public class ActivityStatusBarColorResDeployer implements ISkinResDeployer {
    @Override
    public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
        //the view is the window's DecorView
        Window window;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //API23以上，DecorView独立成一个类，并持有mWindow对象
            window = (Window) ReflectUtils.getField(view, "mWindow");
        } else {
            //API23以下，DecorView是PhoneWindow的内部类，隐式持有PhoneWindow对象
            window = ReflectUtils.getExternalField(view);
        }
        if (window == null) {
            throw new IllegalArgumentException("view is not a DecorView, cannot get the window");
        }
        if (SkinConfig.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            window.setStatusBarColor(resource.getColor(skinAttr.attrValueRefId));
        }
    }
}
