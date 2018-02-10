package com.wind.me.xskinloader.skinInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.wind.me.xskinloader.entity.SkinAttr;

import java.util.Map;

/**
 * Created by Windy on 2018/2/10.
 */

public interface ISkinStyleParser {
    /**
     * 解析xml中设置的style属性
     * @param view 当前需要解析的view
     * @param attrs xml中参数集合
     * @param viewAttrs 解析出来的属性需要保存的集合
     * @param specifiedAttrList 非空时，仅即系此数组中的属性，空时，全部解析
     */
    void parseXmlStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs, String[] specifiedAttrList);
}
