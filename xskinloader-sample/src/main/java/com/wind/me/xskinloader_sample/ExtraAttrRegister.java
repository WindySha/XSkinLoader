package com.wind.me.xskinloader_sample;

import com.wind.me.xskinloader.SkinResDeployerFactory;
import com.wind.me.xskinloader.StyleParserFactory;
import com.wind.me.xskinloader_sample.deployer.CustomViewTextColorResDeployer;
import com.wind.me.xskinloader_sample.deployer.ViewBackgroundStyleParser;

/**
 * 扩展换肤属性和style中的换肤属性
 * Created by Windy on 2018/2/9.
 */

public class ExtraAttrRegister {

    public static final String CUSTIOM_VIEW_TEXT_COLOR = "titleTextColor";

    public static void init() {
        //增加自定义控件的自定义属性的换肤支持
        SkinResDeployerFactory.registerDeployer(CUSTIOM_VIEW_TEXT_COLOR, new CustomViewTextColorResDeployer());

        //增加xml里的style中指定的View background属性换肤
        StyleParserFactory.addStyleParser(new ViewBackgroundStyleParser());
    }

}
