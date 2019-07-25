# XSkinLoader

源码分析，请参考我的个人博客：[侵入性低扩展性强的Android换肤框架XSkinLoader的用法及原理][1]

## **XSkinLoader的使用方法**
XSkinLoader的使用方式特别简单，对代码的侵入性很低，需要换肤的Activity中只用在调用一行代码即可：
```
    SkinInflaterFactory.setFactory(this);
```
用法跟其他换肤框架基本相同，先在Application中初始化，然后在相关xml中加上`skin:enable="true"`即可，具体用法如下：

### **初始化**
首先在`Application`的`onCreate`中进行初始化：
```
        SkinManager.get().init(this);
```
如果代码中需要经常使用Application Context的LayoutInflater加载View，最好同时加上这样一行代码：
```
        SkinInflaterFactory.setFactory(LayoutInflater.from(this));  // for skin change
        SkinManager.get().init(this);
```
如此，使用LayoutInflater.from(context.getApplicationContext()).inflate()加载的view也是可以换肤的

### **XML换肤**
xml布局中的View需要换肤的，只需要在布局文件中相关View标签下添加`skin:enable="true"`即可,例如：
```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:skin="http://schemas.android.com/android/skin"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/status_bar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        skin:enable="true"
        android:background="@color/title_color">
    </TextView>
<RelativeLayout/>
```
能换肤的前提是解析这个xml的LayoutInflater设置Factory接口：SkinInflaterFactory
因此，在相关activity的`onCreate()`中`setContentView()`方法之前添加：
```
    //干涉xml中view的创建，实现xml中资源换肤
    SkinInflaterFactory.setFactory(this);  //for skin change in XML
```
>**PS:** 对于AppCompatActivity，务必要在`onCreate()`的`super.onCreate()`之前添加，否则不会使用AppComt包装的控件，比如：AppCompatTextView等。

某些view的资源是在代码中动态设置的，使用以下方式来设置资源，才能实现换肤效果：
```
    //设置imageView的src资源
    SkinManager.get().setImageDrawable(imageView, R.drawable.ic_action);
    //设置imageView的backgroud资源
    SkinManager.get().setViewBackground(imageView, R.drawable.ic_action);
    //设置textVie的color资源
    SkinManager.get().setTextViewColor(textView, R.color.title_color);
    //设置Activity的statusBarColor
    SkinManager.get().setWindowStatusBarColor(MainActivity.this.getWindow(), R.color.title_color);
    ...
``` 

### **xml中指定换肤属性**
xml中假如出现了多个可换肤属性，但只需要换其中部分属性，而不是全部属性，比如：
```
<Button
        android:id="@+id/use_sdcard_skin"
        android:layout_width="180dp"
        android:layout_height="40dp"
        skin:enable="true"
        android:background="@drawable/confirm_skin_btn_border"
        android:textColor="@color/music_skin_change_button_color" />
```
这个布局中，包含两个换肤属性：`background`，`textColor`，假如只想换`textColor`,那该怎么办？
此处，借鉴了[andSkin][9]中的一个办法，增加一个属性`attrs`，在此属性中声明需要换肤的属性。
具体到上面的例子，只需要增加这样一行代码`skin:attrs="textColor"`就行：
```
<Button
        android:id="@+id/use_sdcard_skin"
        android:layout_width="180dp"
        android:layout_height="40dp"
        skin:enable="true"
        skin:attrs="textColor"
        android:background="@drawable/confirm_skin_btn_border"
        android:textColor="@color/music_skin_change_button_color" />
```
如果支持多个属性，使用`|`分割就行：
```
        skin:attrs="textColor|background"
```
其实，大多数情况下并不用在Xml中加此属性来控制，如若不想此属性换肤，也可以在相应的皮肤apk中去掉此属性指定的资源。

### **新增换肤属性**
对已经成型的大型项目来说，XSkinLoader中提供的换肤属性是不够用的，需要额外增加的换肤属性该怎么办？
在sample中写好了相应的模板，具体参考ExtraAttrRegister.java
```
public static final String CUSTIOM_VIEW_TEXT_COLOR = "titleTextColor";

    static {
        //增加自定义控件的自定义属性的换肤支持
        SkinResDeployerFactory.registerDeployer(CUSTIOM_VIEW_TEXT_COLOR, new CustomViewTextColorResDeployer());

    }
```

### **新增style中的换肤属性**
假如style中的换肤属性不够用，需要新增，该怎么办？
sample中也写了一个模板，在ExtraAttrRegister.java中:
```
static {
        //增加xml里的style中指定的View background属性换肤
        StyleParserFactory.addStyleParser(new ViewBackgroundStyleParser());
    }
```
## License
```
Copyright 2018 Windy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: https://windysha.github.io/2018/02/10/%E4%BE%B5%E5%85%A5%E6%80%A7%E4%BD%8E%E6%89%A9%E5%B1%95%E6%80%A7%E5%BC%BA%E7%9A%84Android%E6%8D%A2%E8%82%A4%E6%A1%86%E6%9E%B6XSkinLoader%E7%9A%84%E7%94%A8%E6%B3%95%E5%8F%8A%E5%8E%9F%E7%90%86/
