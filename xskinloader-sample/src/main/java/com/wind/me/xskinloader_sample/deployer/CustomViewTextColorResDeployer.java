package com.wind.me.xskinloader_sample.deployer;

import android.view.View;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.skinInterface.ISkinResDeployer;
import com.wind.me.xskinloader.skinInterface.ISkinResourceManager;
import com.wind.me.xskinloader_sample.CustomTitleView;

/**
 * Created by Windy on 2018/1/10.
 */

public class CustomViewTextColorResDeployer implements ISkinResDeployer {

    @Override
    public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
        if (!(view instanceof CustomTitleView)) {
            return;
        }
        CustomTitleView titleView = (CustomTitleView) view;
        if (SkinConfig.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            titleView.setTextColor(resource.getColor(skinAttr.attrValueRefId));
        }
    }
}
