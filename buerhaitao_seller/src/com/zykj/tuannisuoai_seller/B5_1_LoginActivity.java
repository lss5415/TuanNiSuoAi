package com.zykj.tuannisuoai_seller;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.Tools;
/**
 * 登录界面
 * @author zyk
 *
 */
public class B5_1_LoginActivity extends BaseActivity {
	private ImageButton login_back;
	private EditText et_login_name,et_passWord;
	private Button btn_login;
	private String login_name,passWord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_login);
		
		login_back=(ImageButton)findViewById(R.id.login_back);
		et_login_name=(EditText) findViewById(R.id.et_login_name);
		et_passWord=(EditText) findViewById(R.id.et_passWord);
		btn_login=(Button) findViewById(R.id.btn_login);
		setListener(login_back,btn_login);
	}
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
			case R.id.login_back:
				this.finish();
				break;
			case R.id.btn_login:
				RequestDailog.showDialog(this, "正在登录");
				login_name=et_login_name.getText().toString().trim();
				passWord=et_passWord.getText().toString().trim();
				HttpUtils.login(res_login, login_name, passWord);
				break;
			default:
				break;
			
		}
	}
	JsonHttpResponseHandler res_login = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("登录"+response);
//			{"datas":{"error":"用户名密码错误"},"code":200}
			String error=null;
			JSONObject datas=null;
//			Tools.Log("res_login="+response);
			try {
				 datas = response.getJSONObject("datas");
				 error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (error==null)//登录成功
			{
				try {
					putSharedPreferenceValue("key", datas.getString("key"));
					putSharedPreferenceValue("store_state", datas.getString("store_state"));
					putSharedPreferenceValue("store_name", datas.getString("store_name"));
					putSharedPreferenceValue("isLogin", "true");
					putSharedPreferenceValue("mobile", login_name);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Tools.Notic(B5_1_LoginActivity.this, "登录成功", new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent  intent_tomainavtivity = new Intent(B5_1_LoginActivity.this, B0_MainActivity.class);
						startActivity(intent_tomainavtivity);
						B5_1_LoginActivity.this.finish();
					}
				});
			}
			else//登录失败 
			{
				putSharedPreferenceValue("isLogin", "false");
				Tools.Notic(B5_1_LoginActivity.this, error+"", null);
			}
			
		}
		
		
	};
}
