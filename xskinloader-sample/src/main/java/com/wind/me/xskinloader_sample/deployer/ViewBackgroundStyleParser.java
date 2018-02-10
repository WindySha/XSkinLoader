package com.wind.me.xskinloader_sample.deployer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.wind.me.xskinloader.SkinResDeployerFactory;
import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.parser.SkinAttributeParser;
import com.wind.me.xskinloader.skinInterface.ISkinStyleParser;
import com.wind.me.xskinloader.util.ReflectUtils;

import java.util.Map;

/**
 * 解析Xml中的style属性，使支持style中定义的View的background支持换肤
 * Created by Windy on 2018/1/23.
 */
public class ViewBackgroundStyleParser implements ISkinStyleParser{

    private static int[] sViewStyleList;
    private static int sViewBackgroundStyleIndex;

    @Override
    public void parseXmlStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs, String[] specifiedAttrList) {
        Context context = view.getContext();
        int[] viewStyleable = getTextViewStyleableList();
        int viewStyleableBackground = getTextViewTextColorStyleableIndex();

        TypedArray a = context.obtainStyledAttributes(attrs, viewStyleable, 0, 0);
        if (a != null) {
            int n = a.getIndexCount();
            for (int j = 0; j < n; j++) {
                int attr = a.getIndex(j);
                if (attr == viewStyleableBackground &&
                        SkinConfig.isCurrentAttrSpecified(SkinResDeployerFactory.BACKGROUND, specifiedAttrList)) {
                    int drawableResId = a.getResourceId(attr, -1);
                    SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context, SkinResDeployerFactory.BACKGROUND, drawableResId);
                    if (skinAttr != null) {
                        viewAttrs.put(skinAttr.attrName, skinAttr);
                    }
                }
            }
            a.recycle();
        }
    }

    private static int[] getTextViewStyleableList() {
        if (sViewStyleList == null || sViewStyleList.length == 0) {
            sViewStyleList = (int[]) ReflectUtils.getField("com.android.internal.R$styleable", "View");
        }
        return sViewStyleList;
    }

    private static int getTextViewTextColorStyleableIndex() {
        if (sViewBackgroundStyleIndex == 0) {
            Object o = ReflectUtils.getField("com.android.internal.R$styleable", "View_background");
            if (o != null) {
                sViewBackgroundStyleIndex = (int) o;
            }
        }
        return sViewBackgroundStyleIndex;
    }
}
