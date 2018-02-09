package com.wind.me.xskinloader.util;

import android.content.Context;
import android.util.Log;

import com.wind.me.xskinloader.entity.SkinConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Windy on 2018/1/11.
 */

public class AssetFileUtils {

    public static boolean copyAssetFile(Context context, String originAssetFileName, String destFileDirectory,
                                        String destFileName) {
        long startTime = System.currentTimeMillis();
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getAssets().open(originAssetFileName);

            File destPathFile = new File(destFileDirectory);
            if(!destPathFile.exists()) {
                destPathFile.mkdirs();
            }

            File destFile = new File(destFileDirectory + File.separator + destFileName);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[256];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (SkinConfig.DEBUG) {
                Log.e("AssetFileUtils","copyAssetFile time = "+(System.currentTimeMillis() - startTime));
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

}
