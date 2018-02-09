package com.wind.me.xskinloader.skinDeployer;

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
        Window window = (Window) ReflectUtils.getField(view, "mWindow");
        if (window == null) {
            throw new IllegalArgumentException("view is not a DecorView, cannot get the window");
        }
        if (SkinConfig.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            window.setStatusBarColor(resource.getColor(skinAttr.attrValueRefId));
        }
    }
}
