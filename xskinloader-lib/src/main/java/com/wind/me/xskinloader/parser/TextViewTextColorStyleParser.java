package com.wind.me.xskinloader.parser;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.SkinResDeployerFactory;
import com.wind.me.xskinloader.skinInterface.ISkinStyleParser;
import com.wind.me.xskinloader.util.ReflectUtils;

import java.util.Map;

/**
 * 解析Xml中的style属性，使支持style中定义的TextView的textColor支持换肤
 * Created by Windy on 2018/1/23.
 */

public class TextViewTextColorStyleParser implements ISkinStyleParser{

    private static int[] sTextViewStyleList;
    private static int sTextViewTextColorStyleIndex;

    @Override
    public void parseXmlStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs, String[] specifiedAttrList) {
        if (!TextView.class.isAssignableFrom(view.getClass())) {
            return;
        }
        Context context = view.getContext();
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
}
