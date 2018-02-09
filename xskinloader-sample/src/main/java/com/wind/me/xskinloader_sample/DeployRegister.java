package com.wind.me.xskinloader_sample;

import com.wind.me.xskinloader.SkinResDeployerFactory;
import com.wind.me.xskinloader_sample.deployer.CustomViewTextColorResDeployer;

/**
 * Created by Windy on 2018/2/9.
 */

public class DeployRegister {

    public static final String CUSTIOM_VIEW_TEXT_COLOR = "titleTextColor";

    static {
        SkinResDeployerFactory.registerDeployer(CUSTIOM_VIEW_TEXT_COLOR, new CustomViewTextColorResDeployer());
    }

    public static void init() {}

}
