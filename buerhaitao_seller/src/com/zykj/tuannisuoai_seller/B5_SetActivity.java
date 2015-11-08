package com.zykj.tuannisuoai_seller;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.ToastView;
import com.zykj.tuannisuoai_seller.utils.Tools;
/**
 * 设置页面
 * @author zyk
 *
 */
public class B5_SetActivity extends BaseActivity{

	RelativeLayout certification,aboutus,aboutapp,updateapp;
	Button btn_logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_set);
		initUI();
		setListener(certification,aboutus,aboutapp,updateapp,btn_logout);
		
	}
	private void initUI() {
		// TODO Auto-generated method stub
		certification = (RelativeLayout) findViewById(R.id.certification);
		aboutus = (RelativeLayout) findViewById(R.id.aboutus);
		aboutapp = (RelativeLayout) findViewById(R.id.aboutapp);
		updateapp = (RelativeLayout) findViewById(R.id.updateapp);
		btn_logout = (Button) findViewById(R.id.btn_logout);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.certification://认证
			//store_state 店铺状态 3:待认证 2:待审核,1:正常,0:已关闭
			if (getSharedPreferenceValue("store_state")=="2") 
			{
				Tools.Notic(this, "待审核，请稍后", null);
			}else {
				Intent intent_to_cetification = new Intent(this,B5_2_certification.class);
				startActivity(intent_to_cetification);
			}
			break;
		case R.id.aboutus://关于我们
			Tools.Notic(this, "团你所爱卖家版", null);
			break;
		case R.id.aboutapp://关于app
			Tools.Notic(this, "团你所爱卖家版app", null);
			break;
		case R.id.updateapp://应用更新
			Tools.Notic(this, "目前已经是最新版本，请留意各大应用市场，及时更新", null);
			break;
		case R.id.btn_logout://登出
			HttpUtils.logout(res_logout, getSharedPreferenceValue("mobile"), getSharedPreferenceValue("key"));
			break;

		default:
			break;
		}
		super.onClick(v);
	}
	JsonHttpResponseHandler res_logout = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("退出登录="+response);
			JSONObject datas = null;
			String error = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null) {
				Tools.Notic(B5_SetActivity.this, "退出登录成功", new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						putSharedPreferenceValue("isLogin", "false");
						Intent intent = new Intent(B5_SetActivity.this,B0_MainActivity.class);
						startActivity(intent);
					}
				});
				
			}else {
				Tools.Notic(B5_SetActivity.this, error, null);
			}
			
		}
		
		
	};
				// 退出操作
				private boolean isExit = false;
				public boolean onKeyDown(int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (isExit == false) {
							isExit = true;
							ToastView toast = new ToastView(getApplicationContext(),
									"再按一次退出程序");
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							Handler mHandler = new Handler() {
								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									isExit = false;
								}
							};
							mHandler.sendEmptyMessageDelayed(0, 3000);
							return true;
						} else {
							android.os.Process.killProcess(android.os.Process.myPid());
							return false;
						}
					}
					return true;
				}
	
}
