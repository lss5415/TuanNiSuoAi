package com.ZYKJ.tuannisuoai.data;

import com.android.volley.VolleyError;

public class ResultBean {
	/** 返回的数据 */
	private String str_result;
	/** 返回的标识 */
	private int flag;
	/** Volley错误 */
	private VolleyError error;
	/** 成功失败的状态 */
	private boolean succeed;

	public String getStr_result() {
		return str_result;
	}

	public void setStr_result(String str_result) {
		this.str_result = str_result;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public VolleyError getError() {
		return error;
	}

	public void setError(VolleyError error) {
		this.error = error;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

}
