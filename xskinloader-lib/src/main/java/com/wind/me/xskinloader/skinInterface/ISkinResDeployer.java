package com.wind.me.xskinloader.skinInterface;

import android.view.View;

import com.wind.me.xskinloader.entity.SkinAttr;

public interface ISkinResDeployer {
	/**
	 * 将属性skinAttr通过resource设置到当前view上
	 *
	 * @param view 当前view
	 * @param skinAttr 属性
	 * @param resource 设置的资源工具
	 */
	void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource);
}
