package com.zykj.tuannisuoai_seller.data;

import java.util.HashMap;
import java.util.Map;


public class AppValue {

	/**
	 * 存储的sharePerfence
	 */
	public static final String config = "config";
	/**
	 * 用户名
	 */
	public static final String USER_ID = "user_id";
	/**
	 * 密码
	 */
	public static final String USER_PASS = "user_pass";
	/**
	 * 当前的是否已经进行过指引
	 */
	public static final String IS_INTRO = "is_intro";
	
	/**
	 * 当前应用中存储的版本号
	 */
	public static final String VERSION = "version";
	/**
	 * 当前帐号是否已经登录的标识
	 */
	public static boolean is_login = false;

	/**
	 * 当前应用的存放目录
	 */
	public static final String FILE_DIR = "buerhaitaos";
	/**
	 * 客户端类型
	 */
	public static final String CLIENT = "android";
	/**
	 * mob短信验证APPID
	 */
	public static final String APPID_mob = "7bf8c19274e0";
	/**
	 * mob短信验证SECRE
	 */
	public static final String APP_SECRE = "a9544d5cdd5854a62ba4f5978be3ef6f";
	
	public static  Map<String, String> map_photo = new HashMap<String, String>();
	
	

}
