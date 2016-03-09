package com.amb.wave_textview;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

/*
 * 创建字体模块
 *
 * 
 */
import java.util.Hashtable;

public class Typefaces {
    private static final String TAG = "Typefaces";
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {//一次只能有一个线程进入
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);//从资源处创建字体
                    cache.put(assetPath, t);//存入创建的映射表中
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }

            return cache.get(assetPath);//返回字体  
        }
    }
}