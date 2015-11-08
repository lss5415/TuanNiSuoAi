package com.ZYKJ.tuannisuoai.data;

import java.util.Map;

import android.content.Context;

import com.ZYKJ.tuannisuoai.socket.SocketListener;

/**
 * 将当前网络请求的参数封装
 * 
 * @author bin
 * 
 */
public class RequestBean {
	private Context context;
	private Map<String, String> str_parmas;
	private SocketListener listener;
	private String url;
	/** 该url产生的flag */
	private int request_flag;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Map<String, String> getStr_parmas() {
		return str_parmas;
	}

	public void setStr_parmas(Map<String, String> str_parmas) {
		this.str_parmas = str_parmas;
	}

	public SocketListener getListener() {
		return listener;
	}

	public void setListener(SocketListener listener) {
		this.listener = listener;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getRequest_flag() {
		return request_flag;
	}

	public void setRequest_flag(int request_flag) {
		this.request_flag = request_flag;
	}

}
