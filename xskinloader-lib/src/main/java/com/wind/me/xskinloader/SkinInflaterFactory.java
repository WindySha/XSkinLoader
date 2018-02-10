package com.wind.me.xskinloader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;

import com.wind.me.xskinloader.entity.SkinAttr;
import com.wind.me.xskinloader.entity.SkinConfig;
import com.wind.me.xskinloader.parser.SkinAttributeParser;
import com.wind.me.xskinloader.util.ReflectUtils;

import java.util.HashMap;

public class SkinInflaterFactory implements Factory {

    private static final String TAG = SkinInflaterFactory.class.getSimpleName();
    private Factory mViewCreateFactory;

    public static void setFactory(LayoutInflater inflater) {
        inflater.setFactory(new SkinInflaterFactory());
    }

    public static void setFactory(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        SkinInflaterFactory factory = new SkinInflaterFactory();
        if (activity instanceof AppCompatActivity) {
            //AppCompatActivity本身包含一个factory,将TextView等转换为AppCompatTextView.java, 参考：AppCompatDelegateImplV9.java
            final AppCompatDelegate delegate = ((AppCompatActivity) activity).getDelegate();
            factory.setInterceptFactory(new Factory() {
                @Override
                public View onCreateView(String name, Context context, AttributeSet attrs) {
                    return delegate.createView(null, name, context, attrs);
                }
            });
        }
        inflater.setFactory(factory);
    }

    //因为LayoutInflater的setFactory方法只能调用一次，当框架外需要处理view的创建时，可以调用此方法
    public void setInterceptFactory(Factory factory) {
        mViewCreateFactory = factory;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if (SkinConfig.DEBUG) {
            Log.d(TAG, "SkinInflaterFactory onCreateView(), create view name="+name+"  ");
        }
        View view = null;
        if (mViewCreateFactory != null) {
            //给框架外提供创建View的机会
            view = mViewCreateFactory.onCreateView(name, context, attrs);
        }
        if (isSupportSkin(attrs)) {
            if (view == null) {
                view = createView(context, name, attrs);
            }
            if (view != null) {
                parseAndSaveSkinAttr(attrs, view);
            }
        }

        return view;
    }

    private View createView(Context context, String name, AttributeSet attrs) {
        View view = null;
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            assertInflaterContext(inflater, context);

            if (-1 == name.indexOf('.')) {
                if ("View".equals(name) || "ViewStub".equals(name) || "ViewGroup".equals(name)) {
                    view = inflater.createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.webkit.", attrs);
                }
            } else {
                view = inflater.createView(name, null, attrs);
            }

        } catch (Exception ex) {
            Log.e(TAG, "createView(), create view failed", ex);
            view = null;
        }
        return view;
    }

    //只有在xml中设置了View的属性skin:enable，才支持xml属性换肤
    public boolean isSupportSkin(AttributeSet attrs) {
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.SKIN_XML_NAMESPACE,
                SkinConfig.ATTR_SKIN_ENABLE, false);
        return isSkinEnable;
    }

    //获取xml中指定的换肤属性，比如：skin:attrs = "textColor|background", 假如为空，表示支持所有能够支持的换肤属性
    private @Nullable
    String getXmlSpecifiedAttrs(@NonNull AttributeSet attrs) {
        return attrs.getAttributeValue(SkinConfig.SKIN_XML_NAMESPACE, SkinConfig.SUPPORTED_ATTR_SKIN_LIST);
    }

    private void parseAndSaveSkinAttr(AttributeSet attrs, View view) {
        String specifiedAttrs = getXmlSpecifiedAttrs(attrs);
        String[] specifiedAttrsList = null;
        if (specifiedAttrs != null && specifiedAttrs.trim().length() > 0) {
            specifiedAttrsList = specifiedAttrs.split("\\|");
        }
        HashMap<String, SkinAttr> viewAttrs = SkinAttributeParser.parseSkinAttr(attrs, view, specifiedAttrsList);
        if (viewAttrs == null || viewAttrs.size() == 0) {
            return;
        }

        //设置view的皮肤属性
        SkinManager.get().deployViewSkinAttrs(view, viewAttrs);
        //save view attribute
        SkinManager.get().saveSkinView(view, viewAttrs);
    }

    //在低版本系统中会出inflaterContext为空的问题， 因此需要处理inflaterContext为空的情况
    private void assertInflaterContext(LayoutInflater inflater, Context context) {
        Context inflaterContext = inflater.getContext();
        if (inflaterContext == null) {
            ReflectUtils.setField(inflater, "mContext", context);
        }


        //设置mConstructorArgs的第一个参数context
        Object[] constructorArgs = (Object[]) ReflectUtils.getField(inflater, "mConstructorArgs");
        if (null == constructorArgs || constructorArgs.length < 2) {
            //异常，一般不会发生
            constructorArgs = new Object[2];
            ReflectUtils.setField(inflater, "mConstructorArgs", constructorArgs);
        }

        //如果mConstructorArgs的第一个参数为空，则设置为mContext
        if (null == constructorArgs[0]) {
            constructorArgs[0] = inflater.getContext();
        }
    }
}
