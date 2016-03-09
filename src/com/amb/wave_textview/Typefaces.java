package com.amb.wave_textview;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

/*
 * ��������ģ��
 *
 * 
 */
import java.util.Hashtable;

public class Typefaces {
    private static final String TAG = "Typefaces";
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {//һ��ֻ����һ���߳̽���
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);//����Դ����������
                    cache.put(assetPath, t);//���봴����ӳ�����
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }

            return cache.get(assetPath);//��������  
        }
    }
}