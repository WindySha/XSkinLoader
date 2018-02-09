package com.wind.me.xskinloader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.impl.SkinResourceManagerImpl;
import com.wind.me.xskinloader.parser.SkinAttributeParser;
import com.wind.me.xskinloader.skinInterface.ISkinResDeployer;
import com.wind.me.xskinloader.skinInterface.ISkinResourceManager;
import com.wind.me.xskinloader.pluginLoader.PluginLoadUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class SkinManager {

    private static final String TAG = SkinManager.class.getSimpleName();
    private static SkinManager sInstance;
    private Context mContext;
    private String mPluginSkinPath;
    private ISkinResourceManager mSkinResourceManager;

    //使用这个map保存所有需要换肤的view和其对应的换肤属性及资源
    //使用WeakHashMap两个作用，1.避免内存泄漏，2.避免重复的view被添加
    //使用HashMap存SkinAttr，为了避免同一个属性值存了两次
    private WeakHashMap<View, HashMap<String, SkinAttr>> mSkinAttrMap = new WeakHashMap<>();

    @MainThread
    public static SkinManager get() {
        if (sInstance == null) {
            sInstance = new SkinManager();
        }
        return sInstance;
    }

    //在Application的onCreate中初始化
    @MainThread
    public void init(Context context) {
        mContext = context.getApplicationContext();
        mSkinResourceManager = new SkinResourceManagerImpl(mContext, null, null);
        load();
    }

    public void restoreToDefaultSkin() {
        SkinConfig.saveSkinPath(mContext, null);
        mSkinResourceManager.setPluginResourcesAndPkgName(null, null);
        notifySkinChanged();
    }

    /**
     * 加载已经用户默认设置的皮肤资源
     */
    public void load() {
        String skinApkPath = SkinConfig.getCustomSkinPath(mContext);
        if (TextUtils.isEmpty(skinApkPath)) {
            restoreToDefaultSkin();
        } else {
            loadNewSkin(skinApkPath);
        }
    }

    /**
     * 加载新皮肤
     *
     * @param skinApkPath 新皮肤路径
     * @return true 加载新皮肤成功 false 加载失败
     */
    public boolean loadNewSkin(String skinApkPath) {
        return doNewSkinLoad(skinApkPath);
    }

    public void setTextViewColor(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.TEXT_COLOR, resId);
    }

    public void setHintTextColor(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.TEXT_COLOR_HINT, resId);
    }

    public void setViewBackground(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.BACKGROUND, resId);
    }

    public void setImageDrawable(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.IMAGE_SRC, resId);
    }

    public void setListViewSelector(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.LIST_SELECTOR, resId);
    }

    public void setListViewDivider(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.DIVIDER, resId);
    }

    public void setWindowStatusBarColor(Window window, @ColorRes int resId) {
        View decorView = window.getDecorView();
        setSkinViewResource(decorView, SkinResDeployerFactory.ACTIVITY_STATUS_BAR_COLOR, resId);
    }

    public void setProgressBarIndeterminateDrawable(View view, int resId) {
        setSkinViewResource(view, SkinResDeployerFactory.PROGRESSBAR_INDETERMINATE_DRAWABLE, resId);
    }

    /**
     * 设置可以换肤的view的属性
     *
     * @param view     设置的view
     * @param attrName 这个取值只能是 {@link SkinResDeployerFactory#BACKGROUND} {@link SkinResDeployerFactory#DIVIDER} {@link SkinResDeployerFactory#TEXT_COLOR}
     *                 {@link SkinResDeployerFactory#LIST_SELECTOR} {@link SkinResDeployerFactory#IMAGE_SRC} 等等
     * @param resId    资源id
     *
     */
    @MainThread
    public void setSkinViewResource(View view, String attrName, int resId) {
        if (TextUtils.isEmpty(attrName)) {
            return;
        }

        SkinAttr attr = SkinAttributeParser.parseSkinAttr(view.getContext(), attrName, resId);
        if (attr != null) {
            doSkinAttrsDeploying(view, attr);
            saveSkinView(view, attr);
        }
    }

    private boolean doNewSkinLoad(String skinApkPath) {
        if (TextUtils.isEmpty(skinApkPath)) {
            return false;
        }

        File file = new File(skinApkPath);
        if (!file.exists()) {
            return false;
        }

        PackageInfo packageInfo = PluginLoadUtils.getInstance(mContext).getPackageInfo(skinApkPath);
        Resources pluginResources = PluginLoadUtils.getInstance(mContext).getPluginResources(skinApkPath);
        if (packageInfo == null || pluginResources == null) {
            return false;
        }
        String skinPackageName = packageInfo.packageName;

        if (TextUtils.isEmpty(skinPackageName)) {
            return false;
        }

        mSkinResourceManager.setPluginResourcesAndPkgName(pluginResources, skinPackageName);

        SkinConfig.saveSkinPath(mContext, skinApkPath);

        mPluginSkinPath = skinApkPath;

        notifySkinChanged();
        return true;
    }

    //将View保存到被监听的view列表中,使得在换肤时能够及时被更新
    void saveSkinView(View view, HashMap<String, SkinAttr> viewAttrs) {
        if (view == null || viewAttrs == null || viewAttrs.size() == 0) {
            return;
        }
        HashMap<String, SkinAttr> originalSkinAttr = mSkinAttrMap.get(view);
        if (originalSkinAttr != null && originalSkinAttr.size() > 0) {
            originalSkinAttr.putAll(viewAttrs);
            mSkinAttrMap.put(view, originalSkinAttr);
        } else {
            mSkinAttrMap.put(view, viewAttrs);
        }
    }

    private void saveSkinView(View view, SkinAttr viewAttr) {
        if (view == null || viewAttr == null) {
            return;
        }
        HashMap<String, SkinAttr> viewAttrs = new HashMap<>();
        viewAttrs.put(viewAttr.attrName, viewAttr);
        saveSkinView(view, viewAttrs);
    }

    public void removeObservableView(View view) {
        mSkinAttrMap.remove(view);
    }

    public void clear() {
        mSkinAttrMap.clear();
    }

    //更换皮肤时，通知view更换资源
    private void notifySkinChanged() {
        View view;
        HashMap<String, SkinAttr> viewAttrs;
        Iterator iter = mSkinAttrMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            view = (View) entry.getKey();
            viewAttrs = (HashMap<String, SkinAttr>) entry.getValue();
            if (view != null) {
                deployViewSkinAttrs(view, viewAttrs);
            }
        }
    }

    void deployViewSkinAttrs(@Nullable View view, @Nullable HashMap<String, SkinAttr> viewAttrs) {
        if (view == null || viewAttrs == null || viewAttrs.size() == 0) {
            return;
        }
        Iterator iter = viewAttrs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            SkinAttr attr = (SkinAttr) entry.getValue();
            doSkinAttrsDeploying(view, attr);
        }
    }

    //将新皮肤的属性部署到view上
    private void doSkinAttrsDeploying(@Nullable View view, @Nullable SkinAttr skinAttr) {
        ISkinResDeployer deployer = SkinResDeployerFactory.of(skinAttr);
        if (deployer != null) {
            deployer.deploy(view, skinAttr, mSkinResourceManager);
        }
    }

    public String getCurrentSkinPackageName() {
        return mSkinResourceManager.getPkgName();
    }

    public Resources getPlugintResources() {
        return mSkinResourceManager.getPluginResource();
    }

    public boolean isUsingDefaultSkin() {
        return getPlugintResources() == null;
    }

    public String getCurrentSkinPath() {
        return mPluginSkinPath;
    }

    public int getSkinViewMapSize() {
        return mSkinAttrMap.size();
    }

}