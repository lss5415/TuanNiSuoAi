package com.ZYKJ.tuannisuoai.UI;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 设置页面
 * 
 * @author zyk
 * 
 */
public class B5_12_SetActivity extends BaseActivity {

	private RelativeLayout cetification, resetpasswd, aboutus, appupdate;
	private Button btn_logout;
	private ImageButton set_back;
	String mobile = null;
	String key = null;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mobile = getSharedPreferenceValue("mobile");
		key = getSharedPreferenceValue("key");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_set);
		initView();
		setListener(cetification, resetpasswd, aboutus, appupdate, btn_logout,
				set_back);
	}

	private void initView() {
		// TODO Auto-generated method stub
		cetification = (RelativeLayout) findViewById(R.id.set_1);// 实名认证
		resetpasswd = (RelativeLayout) findViewById(R.id.RelativeLayout01);// 重置密码
		aboutus = (RelativeLayout) findViewById(R.id.RelativeLayout02);// 关于我们
		appupdate = (RelativeLayout) findViewById(R.id.RelativeLayout04);// 版本跟新
		btn_logout = (Button) findViewById(R.id.btn_logout);// 退出登录
		set_back = (ImageButton) findViewById(R.id.set_back);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_1:// 实名认证
			Intent intent_cet = new Intent();
			intent_cet.setClass(this, B5_12_1_Certification.class);
			startActivity(intent_cet);
			break;
		case R.id.RelativeLayout01:// 重置密码
			Intent intent_reset = new Intent();
			intent_reset.setClass(this, B5_1_1_RegistActivity.class);
			intent_reset.putExtra("FIND_OR_REGIST", 2);
			startActivity(intent_reset);
			break;
		case R.id.RelativeLayout02:// 关于我们
			Tools.Notic(
					this,
					"团你所爱是临沂都始电子商务有限公司推出，“足不出户，逛遍全城”的购物理念。领先全城团购市场，零风险、高品质超低折扣。打造最受欢迎的生活休闲娱乐购物平台。",
					null);
			break;
		case R.id.RelativeLayout04:// 版本跟新
			// Toast.makeText(this, "目前为最新版本", Toast.LENGTH_LONG).show();
			Tools.Notic(this, "目前为最新版本", null);
			break;
		case R.id.btn_logout:// 退出登录
			RequestDailog.showDialog(this, "正在退出登录，请稍后");
			HttpUtils.logout(res_logout, mobile, key);
			break;
		case R.id.set_back:// 退出设置页面
			this.finish();
			break;

		default:
			break;
		}
	}

	JsonHttpResponseHandler res_logout = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null) // 注销成功
			{
				putSharedPreferenceValue("userid", "");
				putSharedPreferenceValue("username", "");
				putSharedPreferenceValue("mobile", "");
				putSharedPreferenceValue("key", "");
				Tools.Notic(B5_12_SetActivity.this, "注销成功",
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent_tomainavtivity = new Intent(
										B5_12_SetActivity.this,
										B0_MainActivity.class);
								startActivity(intent_tomainavtivity);
							}
						});
			}
			else //注销失败
			{
				if (error.equals("请登录")) {
					Tools.Notic(B5_12_SetActivity.this, "请叫我请登录", null);
					putSharedPreferenceValue("userid", "");
					putSharedPreferenceValue("username", "");
					putSharedPreferenceValue("mobile", "");
					putSharedPreferenceValue("key", "");
					Tools.Notic(B5_12_SetActivity.this, "注销成功", new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent  intent_tomainavtivity = new Intent(B5_12_SetActivity.this, B0_MainActivity.class);
							startActivity(intent_tomainavtivity);
						}
					});
				}else {
					Tools.Notic(B5_12_SetActivity.this, error+"", null);
				}
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Tools.Log("logout-errorResponse=" + errorResponse);
		}

	};

}
