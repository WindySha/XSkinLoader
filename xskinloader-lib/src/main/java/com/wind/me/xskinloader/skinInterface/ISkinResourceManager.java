package com.wind.me.xskinloader.skinInterface;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Created by xiawanli on 2018/1/10.
 *
 * 换肤功能，替换资源管理接口
 */

public interface ISkinResourceManager {

    String getPkgName();

    Resources getPluginResource();

    void setPluginResourcesAndPkgName(Resources resources, String pkgName);

    int getColor(@ColorRes int resId) throws Resources.NotFoundException;

    ColorStateList getColorStateList(@ColorRes int resId) throws Resources.NotFoundException;

    Drawable getDrawable(@DrawableRes int resId) throws Resources.NotFoundException;

}
