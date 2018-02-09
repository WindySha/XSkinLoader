package com.wind.me.xskinloader;

import android.text.TextUtils;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.skinInterface.ISkinResDeployer;
import com.wind.me.xskinloader.skinDeployer.ActivityStatusBarColorResDeployer;
import com.wind.me.xskinloader.skinDeployer.BackgroundResDeployer;
import com.wind.me.xskinloader.skinDeployer.ImageDrawableResDeployer;
import com.wind.me.xskinloader.skinDeployer.ListViewDividerResDeployer;
import com.wind.me.xskinloader.skinDeployer.ListViewSelectorResDeployer;
import com.wind.me.xskinloader.skinDeployer.ProgressBarIndeterminateDrawableDeployer;
import com.wind.me.xskinloader.skinDeployer.TextColorHintResDeployer;
import com.wind.me.xskinloader.skinDeployer.TextColorResDeployer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Windy on 2018/1/10.
 */

public class SkinResDeployerFactory {

    public static final String BACKGROUND = "background";
    public static final String IMAGE_SRC = "src";
    public static final String TEXT_COLOR = "textColor";
    public static final String TEXT_COLOR_HINT = "textColorHint";
    public static final String LIST_SELECTOR = "listSelector";
    public static final String DIVIDER = "divider";

    public static final String ACTIVITY_STATUS_BAR_COLOR = "statusBarColor";
    public static final String PROGRESSBAR_INDETERMINATE_DRAWABLE = "indeterminateDrawable";


    //存放支持的换肤属性和对应的处理器
    private static Map<String, ISkinResDeployer> sSupportedSkinDeployerMap = new HashMap<String, ISkinResDeployer>();

    //静态注册支持的属性和处理器
    static {
        registerDeployer(BACKGROUND, new BackgroundResDeployer());
        registerDeployer(IMAGE_SRC, new ImageDrawableResDeployer());
        registerDeployer(TEXT_COLOR, new TextColorResDeployer());
        registerDeployer(TEXT_COLOR_HINT, new TextColorHintResDeployer());
        registerDeployer(LIST_SELECTOR, new ListViewSelectorResDeployer());
        registerDeployer(DIVIDER, new ListViewDividerResDeployer());
        registerDeployer(ACTIVITY_STATUS_BAR_COLOR, new ActivityStatusBarColorResDeployer());
        registerDeployer(PROGRESSBAR_INDETERMINATE_DRAWABLE, new ProgressBarIndeterminateDrawableDeployer());
    }

    public static void registerDeployer(String attrName, ISkinResDeployer skinResDeployer) {
        if (TextUtils.isEmpty(attrName) || null == skinResDeployer) {
            return;
        }
        if (sSupportedSkinDeployerMap.containsKey(attrName)) {
            throw new IllegalArgumentException("The attrName has been registed, please rename it");
        }
        sSupportedSkinDeployerMap.put(attrName, skinResDeployer);
    }

    public static ISkinResDeployer of(SkinAttr attr) {
        if (attr == null) {
            return null;
        }
        return of(attr.attrName);
    }

    public static ISkinResDeployer of(String attrName) {
        if (TextUtils.isEmpty(attrName)) {
            return null;
        }
        return sSupportedSkinDeployerMap.get(attrName);
    }

    public static boolean isSupportedAttr(String attrName) {
        return of(attrName) != null;
    }

    public static boolean isSupportedAttr(SkinAttr attr) {
        return of(attr) != null;
    }

}
