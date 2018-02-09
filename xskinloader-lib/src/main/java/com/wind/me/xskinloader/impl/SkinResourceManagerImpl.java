package com.wind.me.xskinloader.impl;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.skinInterface.ISkinResourceManager;


/**
 * Created by Windy on 2018/1/10.
 */

public class SkinResourceManagerImpl implements ISkinResourceManager {

    private static final String TAG = "SkinResourceManagerImpl";

    private Resources mDefaultResources;
    private String mSkinPluginPackageName;
    private Resources mSkinPluginResources;

    public SkinResourceManagerImpl(Context context, String pkgName, Resources resources) {
        mDefaultResources = context.getResources();
        mSkinPluginPackageName = pkgName;
        mSkinPluginResources = resources;
    }

    @Override
    public String getPkgName() {
        return mSkinPluginPackageName;
    }

    @Override
    public Resources getPluginResource() {
        return mSkinPluginResources;
    }

    @Override
    public void setPluginResourcesAndPkgName(Resources resources, String pkgName) {
        mSkinPluginResources = resources;
        mSkinPluginPackageName = pkgName;
    }

    @Override
    public int getColor(int resId) throws Resources.NotFoundException {
        int originColor = mDefaultResources.getColor(resId);
        if (mSkinPluginResources == null) {
            return originColor;
        }

        String resName = mDefaultResources.getResourceEntryName(resId);

        int trueResId = mSkinPluginResources.getIdentifier(resName, SkinConfig.RES_TYPE_NAME_COLOR, mSkinPluginPackageName);
        int trueColor = 0;

        try {
            trueColor = mSkinPluginResources.getColor(trueResId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }

    @Override
    public ColorStateList getColorStateList(int resId) throws Resources.NotFoundException {
        boolean isExtendSkin = true;

        if (mSkinPluginResources == null) {
            isExtendSkin = false;
        }

        String resName = mDefaultResources.getResourceEntryName(resId);
        if (isExtendSkin) {
            int trueResId = mSkinPluginResources.getIdentifier(resName, SkinConfig.RES_TYPE_NAME_COLOR, mSkinPluginPackageName);
            ColorStateList trueColorList = null;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    ColorStateList originColorList = mDefaultResources.getColorStateList(resId);
                    return originColorList;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    if (SkinConfig.DEBUG) {
                        Log.d(TAG, "resName = " + resName + " NotFoundException : " + e.getMessage());
                    }
                }
            } else {
                try {
                    trueColorList = mSkinPluginResources.getColorStateList(trueResId);
                    if (SkinConfig.DEBUG) {
                        Log.d(TAG, "getColorStateList the trueColorList is = " + trueColorList);
                    }
                    return trueColorList;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "resName = " + resName + " NotFoundException :" + e.getMessage());
                }
            }
        } else {
            try {
                ColorStateList originColorList = mDefaultResources.getColorStateList(resId);
                return originColorList;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "resName = " + resName + " NotFoundException :" + e.getMessage());
            }
        }


        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{mDefaultResources.getColor(resId)});
    }

    @Override
    public Drawable getDrawable(int resId) throws Resources.NotFoundException {
        Drawable originDrawable = mDefaultResources.getDrawable(resId);
        if (mSkinPluginResources == null) {
            return originDrawable;
        }
        String resName = mDefaultResources.getResourceEntryName(resId);

        int trueResId = mSkinPluginResources.getIdentifier(resName, SkinConfig.RES_TYPE_NAME_DRAWABLE, mSkinPluginPackageName);

        Drawable trueDrawable;
        try {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mSkinPluginResources.getDrawable(trueResId);
            } else {
                trueDrawable = mSkinPluginResources.getDrawable(trueResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }
        return trueDrawable;
    }
}
