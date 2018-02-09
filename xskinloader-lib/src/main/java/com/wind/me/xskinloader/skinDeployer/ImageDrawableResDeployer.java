package com.wind.me.xskinloader.skinDeployer;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.skinInterface.ISkinResDeployer;
import com.wind.me.xskinloader.skinInterface.ISkinResourceManager;

/**
 * Created by Windy on 2018/1/10.
 */

public class ImageDrawableResDeployer implements ISkinResDeployer {
    @Override
    public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
        if (!(view instanceof ImageView)) {
            return;
        }
        Drawable drawable = null;
        if (SkinConfig.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            drawable = new ColorDrawable(resource.getColor(skinAttr.attrValueRefId));
        } else if (SkinConfig.RES_TYPE_NAME_DRAWABLE.equals(skinAttr.attrValueTypeName)) {
            drawable = resource.getDrawable(skinAttr.attrValueRefId);
        }
        if (drawable != null) {
            ((ImageView) view).setImageDrawable(drawable);
        }
    }
}
