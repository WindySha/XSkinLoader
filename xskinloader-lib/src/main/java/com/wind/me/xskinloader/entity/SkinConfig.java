package com.wind.me.xskinloader.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SkinConfig {
    /***
     * 支持的命名空间
     */
    public static final String SKIN_XML_NAMESPACE = "http://schemas.android.com/android/skin";

    /**界面元素支持换肤的属性*/
    public static final String ATTR_SKIN_ENABLE = "enable";
    public static final String SUPPORTED_ATTR_SKIN_LIST = "attrs";

    public static final String SKIN_APK_SUFFIX = ".skin";
    public static final String PREF_CUSTOM_SKIN_PATH = "music_skin_custom_path";

    public static final boolean DEBUG = false;


    /**
     * 属性值对应的类型是color
     */
    public static final String RES_TYPE_NAME_COLOR = "color";

    /**
     * 属性值对应的类型是drawable
     */
    public static final String RES_TYPE_NAME_DRAWABLE = "drawable";

    public static final String PREFERENCE_NAME = "music_skin_pref";


    public static String getCustomSkinPath(Context context) {
        return getString(context, PREF_CUSTOM_SKIN_PATH, null);
    }

    public static void saveSkinPath(Context context, String path) {
        putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    public static boolean isDefaultSkin(Context context) {
        return getCustomSkinPath(context) == null;
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static boolean isCurrentAttrSpecified(@NonNull String attr, @Nullable String[] attrList) {
        if (attrList == null || attrList.length == 0) {
            return true;
        }
        for (String a : attrList) {
            if (a != null && (a.trim()).equals(attr)) {
                return true;
            }
        }
        return false;
    }
}
