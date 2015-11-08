package com.zykj.tuannisuoai_seller.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zykj.tuannisuoai_seller.R;

/** 异步加载图片属性的设置 */
public class ImageOptions {
	
    public static SimpleImageLoadingListener listener;

	public static DisplayImageOptions getLogoOptions(boolean round) {
		DisplayImageOptions.Builder m_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.logo_default)
				.showImageForEmptyUri(R.drawable.logo_default)
				.showImageOnFail(R.drawable.logo_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true);
		if (round) {// 如果是圆角
			m_options.displayer(new RoundedBitmapDisplayer(5));
		}
		return m_options.build();
	}

	/**
	 * 
	 * @param round
	 *            true是圆角
	 * @return
	 */
	public static DisplayImageOptions getGoodsOptions(boolean round) {
		DisplayImageOptions.Builder m_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.logo_default)
				.showImageForEmptyUri(R.drawable.logo_default)
				.showImageOnFail(R.drawable.logo_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true);
		if (round) {// 如果是圆角
			m_options.displayer(new RoundedBitmapDisplayer(5));
		}
		return m_options.build();
	}
	
    /**
     * @author Administrator获取圆角图片
     * @param container
     * @param url
     * @param roundPxe
     */
    public static <T extends ImageView> void displayImage2Circle(T container, String url, final float roundPxe) {
        if (listener == null) {
            listener = new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					((ImageView)view).setImageBitmap(getCircleCornerBitmap(loadedImage, roundPxe));
				}
            };
        }
        ImageLoader.getInstance().displayImage(url, container, listener);
    }
    
    /**
     * 获得圆角图片
     */
    public static Bitmap getCircleCornerBitmap(Bitmap bitmap, float roundPx) {
        // 圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        // 构建一个bitmap
    	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
                .getHeight(), Config.ARGB_8888); 
        // new一个Canvas，在output上画图 
        Canvas canvas = new Canvas(output); 
        Paint paint = new Paint();
        // 设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true); 
        // 宽高相等，即正方形
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect); 
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
        // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, rect, rect, paint);
   
        return output;
    }
}
