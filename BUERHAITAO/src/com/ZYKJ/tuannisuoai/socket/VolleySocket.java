package com.ZYKJ.tuannisuoai.socket;

import java.util.Map;
import java.util.Map.Entry;

import com.ZYKJ.tuannisuoai.base.BaseApp;
import com.ZYKJ.tuannisuoai.data.RequestBean;
import com.ZYKJ.tuannisuoai.data.ResultBean;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/***
 * 数据请求列表
 * 
 * @author bin
 * 
 */
public class VolleySocket {
	public static RequestQueue m_request;
	public static ImageLoader m_loader;

	public static RequestQueue getInstance() {
		if (m_request == null) {
			m_request = Volley.newRequestQueue(BaseApp.getInstance()
					.getApplicationContext());
		}
		return m_request;
	}

	public static ImageLoader getLoader() {
		if (m_loader == null) {
			m_loader = new ImageLoader(VolleySocket.getInstance(),
					new BitmapCache());
		}
		return m_loader;
	}

	/** 使用StringRequest获取数据 */
	public static void getStringRequest(final RequestBean data) {
		RequestQueue queue = VolleySocket.getInstance();
		StringRequest request = new StringRequest(Method.POST, data.getUrl(),
				new Response.Listener<String>() {
					public void onResponse(String arg0) {
						Tools.Log("获取到的json：" + arg0);
						RequestDailog.closeDialog();
						
						// 重新构建result数据
						ResultBean result = new ResultBean();
						result.setFlag(data.getRequest_flag());
						result.setStr_result(arg0);
						result.setSucceed(true);
						data.getListener().response(result);

					}

				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError arg0) {
						Tools.Log("请求异常:" + arg0.getMessage());
						RequestDailog.closeDialog();

						ResultBean result = new ResultBean();
						result.setFlag(data.getRequest_flag());
						result.setError(arg0);
						result.setSucceed(false);
						data.getListener().response(result);

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				if (data.getStr_parmas() != null) {
					for (Entry<String, String> temp_parmas : data
							.getStr_parmas().entrySet()) {
						Tools.Log("Volley   key>>>" + temp_parmas.getKey()
								+ " value>>>>" + temp_parmas.getValue());
					}
				}
				return data.getStr_parmas();
			}

		};

		queue.add(request);
	}

}
