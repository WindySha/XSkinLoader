package com.wind.me.xskinloader.pluginLoader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class PluginLoadUtils {

    private Context mContext;
    private final Map<String, PluginInfo> mPluginInfoHolder = new HashMap<>();
    private static PluginLoadUtils sInstance;
    private static final String TAG = "PluginLoadUtils";

    private PluginLoadUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    public static PluginLoadUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginLoadUtils.class) {
                if (sInstance == null) {
                    sInstance = new PluginLoadUtils(context);
                }
            }
        }
        return sInstance;
    }

    public static PluginLoadUtils getInstance() {
        return sInstance;
    }

    public PluginInfo install(String apkPath) {
        PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            return pluginInfo;
        }

        DexClassLoader dexClassLoader = createDexClassLoader(apkPath);
        AssetManager assetManager = createAssetManager(apkPath);
        Resources resources = createResources(assetManager);
        Resources.Theme theme = null;
        if (resources != null) {
            theme = resources.newTheme();
//            theme.applyStyle(R.style.AppTheme, false);
        }
        PackageInfo packageInfo = createPackageInfo(apkPath);

        pluginInfo = new PluginInfo(apkPath, dexClassLoader, resources, theme, packageInfo);
        mPluginInfoHolder.put(apkPath, pluginInfo);
        return pluginInfo;
    }

    public DexClassLoader getClassLoader(String apkPath) {
        PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            ClassLoader loader = pluginInfo.getClassLoader();
            if (loader != null) {
                return (DexClassLoader) loader;
            }
        }
        return createDexClassLoader(apkPath);
    }

    public PackageInfo getPackageInfo(String apkPath) {
        PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            PackageInfo info = pluginInfo.getPackageInfo();
            if (info != null) {
                return info;
            }
        }
        return createPackageInfo(apkPath);
    }

    public Resources getPluginResources(String apkPath) {
        PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            Resources res = pluginInfo.getResources();
            if (res != null) {
                return res;
            }
        }
        return createResources(apkPath);
    }

    public AssetManager getPluginAssets(String apkPath) {
        PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            Resources resources = pluginInfo.getResources();
            if (resources != null) {
                AssetManager assetManager = resources.getAssets();
                if (assetManager != null) {
                    return assetManager;
                }
            }
        }
        return createAssetManager(apkPath);
    }

    /**
     * 创建插件classloader
     */
    private DexClassLoader createDexClassLoader(String dexPath) {
        DexClassLoader loader = new DexClassLoader(dexPath,
                mContext.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(),
                null,
                mContext.getClassLoader());
        return loader;
    }

    /**
     * 创建AssetManager对象
     */
    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建Resource对象
     */
    @SuppressWarnings("deprecation")
    private Resources createResources(AssetManager assetManager) {
        if (assetManager == null) {
            Log.e(TAG, " create Resources failed assetManager is NULL !! ");
            return null;
        }
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    private Resources createResources(String dexPath) {
        AssetManager assetManager = createAssetManager(dexPath);
        if (assetManager != null) {
            return createResources(assetManager);
        }
        return null;
    }

    private PackageInfo createPackageInfo(String apkFilepath) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath,
                    PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }

    private Class<?> loadClassByClassLoader(ClassLoader classLoader, String className) {
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}
