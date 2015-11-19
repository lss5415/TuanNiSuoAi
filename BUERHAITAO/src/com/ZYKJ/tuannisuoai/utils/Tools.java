package com.ZYKJ.tuannisuoai.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;

import com.ZYKJ.tuannisuoai.view.UIDialog;

/**
 * 工具类
 * 
 * @author bin
 * 
 */
public class Tools {
	public static boolean DEBUG = true;

	public static ProgressDialog m_dialog;

	public static int M_SCREEN_WIDTH;

	public static int M_SCREEN_HEIGHT;

	public static String CURRENTCITY = "北京";//默认城市

	/**
	 * log输出
	 * 
	 * @param msg
	 */
	public static void Log(String msg) {
		if (DEBUG) {
			android.util.Log.d("debug输出：", msg);
		}
	}

	/**
	 * Exception 信息提示
	 */
	public static void Notic(Context context, String notic,
			OnClickListener listener) {
		UIDialog.ForNotic(context,notic,listener);
	}

	/**
	 * Dialog显示
	 */
	public static void showDialog(Context context) {
		m_dialog = new ProgressDialog(context);
		m_dialog.setCanceledOnTouchOutside(false);
		m_dialog.setMessage("正在加载");
		m_dialog.show();
	}

	/** 图片无损压缩并存储 */
	public static void compressBitmap(Bitmap image, File file) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** onCreate方法中获取view的高度 */
	public static int getTargetHeight(View v) {

		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(
					((View) v.getParent()).getMeasuredWidth(),
					MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED));
		} catch (Exception e) {

		}
		return v.getMeasuredHeight();
	}

	/** onCreate方法中获取view的宽度 */
	public static int getTargetWidth(View v) {

		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(
					((View) v.getParent()).getMeasuredWidth(),
					MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED));
		} catch (Exception e) {

		}
		return v.getMeasuredWidth();
	}

	/**
	 * 将dp转化为像素值
	 */

	public static int dp2px(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 获取当前应用的版本号
	 */

	public static int getAppVersion(Context context) {
		int version = 0;
		try {
			PackageInfo packinfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = packinfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return version;
		}

		return version;
	}

	/**
	 * 判断SD卡是否存在
	 */

	public static boolean existSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 判断sd卡的剩余空间
	 */

	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

}
