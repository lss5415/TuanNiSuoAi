package com.ZYKJ.tuannisuoai.socket;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 图片资源的缓存
 * 
 * @author bin
 * 
 */
public class BitmapCache implements ImageCache {
	private LruCache<String, Bitmap> mCache;

	public BitmapCache() {
		int maxSize = 3 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public Bitmap getBitmap(String arg0) {
//		Tools.Log("获取存在的图片:" + arg0);
		return mCache.get(arg0);
	}

	@Override
	public void putBitmap(String arg0, Bitmap arg1) {
		if (arg1 != null) {
			mCache.put(arg0, arg1);
		}
	}

}
