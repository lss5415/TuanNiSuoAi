package com.ZYKJ.tuannisuoai.base;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;

import com.ZYKJ.tuannisuoai.utils.Tools;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseApp extends Application {
	private static Stack<Activity> activityStack;
	public static BaseApp singleton;

	public void onCreate() {
		super.onCreate();

		singleton = this;

		initImageLoader();
	}

	public static BaseApp getInstance() {
		return singleton;
	}

	/**
	 * 
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);

		Tools.Log("-----------------------------------");
		for (Activity temp : activityStack) {
			Tools.Log("类名:" + temp.toString() + "地址：" + temp);
		}
		Tools.Log("===================================");
	}

	/**
	 * 
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 *
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 当前界面恢复时的操作
	 */
	public void resumeActivity(Activity activity) {
		if (activityStack.lastElement() == activity) {
			return;
		}
		activityStack.remove(activity);
		activityStack.push(activity);

		Tools.Log("最后一个参数:" + activityStack.lastElement());
	}

	/** 初始化ImageLoader的数据 */
	public void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		ImageLoader.getInstance().init(config);
	}
}
