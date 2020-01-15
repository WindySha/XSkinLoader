package com.wind.me.xskinloader_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.me.xskinloader.SkinInflaterFactory;
import com.wind.me.xskinloader.SkinManager;
import com.wind.me.xskinloader.util.AssetFileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private ImageView mImageMipMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 实现activity中xml布局中的换肤，一定要在setContentView之前执行
        SkinInflaterFactory.setFactory(this);

        SkinManager.get().setWindowStatusBarColor(this.getWindow(), R.color.statusBarColor);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.my_textView);
        mImageView = findViewById(R.id.my_imageView);
        mImageMipMap = findViewById(R.id.my_image_mipmap);

        SkinManager.get().setImageDrawable(mImageView, R.drawable.my_image);
        SkinManager.get().setTextViewColor(mTextView, R.color.myTextColor);
    }

    //相应按钮点击事件
    public void clickButton(View v) {
        switch (v.getId()) {
            case R.id.change_skin_button:
                changeSkin();
                break;
            case R.id.recovery_skin_button:
                restoreDefaultSkin();
                break;
        }
    }

    private void changeSkin() {
        //将assets目录下的皮肤文件拷贝到data/data/.../cache目录下
        String saveDir = getCacheDir().getAbsolutePath() + "/skins";
        String savefileName = "/skin1.skin";
        String asset_dir = "skins/mipmap.apk";
//        String asset_dir = "skins/xskinloader-skin-apk-debug.apk";
        File file = new File(saveDir + File.separator + savefileName);
        if (!file.exists()) {
            AssetFileUtils.copyAssetFile(this, asset_dir, saveDir, savefileName);
        }
        SkinManager.get().loadSkin(file.getAbsolutePath());
    }

    private void restoreDefaultSkin() {
        SkinManager.get().restoreToDefaultSkin();
    }
}
