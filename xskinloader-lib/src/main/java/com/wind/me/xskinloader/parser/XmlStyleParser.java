package com.wind.me.xskinloader.parser;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.SkinResDeployerFactory;
import com.wind.me.xskinloader.util.ReflectUtils;

import java.util.HashMap;

/**
 * 解析Xml中的style属性，使支持style中定义的textColor, indeterminateDrawable支持换肤
 * Created by Windy on 2018/1/23.
 */

public class XmlStyleParser {

    private static int[] sTextViewStyleList;
    private static int sTextViewTextColorStyleIndex;

    private static int[] sProgressBarStyleList;
    private static int sProgressBarIndeterminateDrawableIndex;

    public static final void parseTextViewStyles(Context context, AttributeSet attrs,
                                                 HashMap<String, SkinAttr> viewAttrs, String[] specifiedAttrList) {
        int[] textViewStyleable = getTextViewStyleableList();
        int textViewStyleableTextColor = getTextViewTextColorStyleableIndex();

        TypedArray a = context.obtainStyledAttributes(attrs, textViewStyleable, 0, 0);
        if (a != null) {
            int n = a.getIndexCount();
            for (int j = 0; j < n; j++) {
                int attr = a.getIndex(j);
                if (attr == textViewStyleableTextColor &&
                        SkinConfig.isCurrentAttrSpecified(SkinResDeployerFactory.TEXT_COLOR, specifiedAttrList)) {
                    int colorResId = a.getResourceId(attr, -1);
                    SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context, SkinResDeployerFactory.TEXT_COLOR, colorResId);
                    if (skinAttr != null) {
                        viewAttrs.put(skinAttr.attrName, skinAttr);
                    }
                }
            }
            a.recycle();
        }
    }


    public static final void parseProgressBarStyle(Context context, AttributeSet attrs,
                                                   HashMap<String, SkinAttr> viewAttrs, String[] specifiedAttrList) {
        int[] progressBarStyleList = getProgressBarStyleable();
        int progressBarIndeterminateDrawableIndex = getProgressBarIndeterminateDrawableIndex();

        final TypedArray a = context.obtainStyledAttributes(attrs, progressBarStyleList, 0, 0);

        if (a != null) {
            int n = a.getIndexCount();
            for (int j = 0; j < n; j++) {
                int attr = a.getIndex(j);
                if (attr == progressBarIndeterminateDrawableIndex &&
                        SkinConfig.isCurrentAttrSpecified(SkinResDeployerFactory.PROGRESSBAR_INDETERMINATE_DRAWABLE, specifiedAttrList)) {
                    int drawableResId = a.getResourceId(attr, -1);
                    SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context, SkinResDeployerFactory.PROGRESSBAR_INDETERMINATE_DRAWABLE, drawableResId);
                    if (skinAttr != null) {
                        viewAttrs.put(skinAttr.attrName, skinAttr);
                    }
                }
            }
            a.recycle();
        }
    }

    private static int[] getTextViewStyleableList() {
        if (sTextViewStyleList == null || sTextViewStyleList.length == 0) {
            sTextViewStyleList = (int[]) ReflectUtils.getField("com.android.internal.R$styleable", "TextView");
        }
        return sTextViewStyleList;
    }

    private static int getTextViewTextColorStyleableIndex() {
        if (sTextViewTextColorStyleIndex == 0) {
            Object o = ReflectUtils.getField("com.android.internal.R$styleable", "TextView_textColor");
            if (o != null) {
                sTextViewTextColorStyleIndex = (int) o;
            }
        }
        return sTextViewTextColorStyleIndex;
    }

    private static int[] getProgressBarStyleable() {
        if (sProgressBarStyleList == null || sProgressBarStyleList.length == 0) {
            sProgressBarStyleList = (int[])ReflectUtils.getField("com.android.internal.R$styleable", "ProgressBar");
        }
        return sProgressBarStyleList;
    }

    private static int getProgressBarIndeterminateDrawableIndex() {
        if (sProgressBarIndeterminateDrawableIndex == 0) {
            Object o = ReflectUtils.getField("com.android.internal.R$styleable", "ProgressBar_indeterminateDrawable");
            if (o != null) {
                sProgressBarIndeterminateDrawableIndex = (int) o;
            }
        }
        return sProgressBarIndeterminateDrawableIndex;
    }
}
