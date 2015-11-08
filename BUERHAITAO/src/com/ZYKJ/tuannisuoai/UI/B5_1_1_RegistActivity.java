package com.ZYKJ.tuannisuoai.UI;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.AppValue;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;

public class B5_1_1_RegistActivity extends BaseActivity {
	
	private ImageButton regist_back;
	private Button btn_getRegistCode,btn_regist;
	private EditText et_regist_login_name,et_registCode,et_registPassWord;
	public String mobile,password,verify_code;  
	public TextView textView_title;
	/**
	 * 根据TAG的值判断注册还是找回密码
	 * 1：注册
	 * 2：找回密码
	 */
	public int TAG=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_regist);
//	 	短信验证
		initSmsSDK();
		
		regist_back=(ImageButton) findViewById(R.id.regist_back);
		btn_getRegistCode=(Button) findViewById(R.id.btn_getRegistCode);
		btn_regist=(Button) findViewById(R.id.btn_regist);
		et_regist_login_name=(EditText) findViewById(R.id.et_regist_login_name);
		et_registCode=(EditText) findViewById(R.id.et_registCode);
		et_registPassWord=(EditText) findViewById(R.id.et_registPassWord);
		textView_title=(TextView) findViewById(R.id.textView_regist);
	    
	    
	    Intent intent_getTAG =getIntent();
	    TAG=intent_getTAG.getIntExtra("FIND_OR_REGIST", 0);//用来判断：找回密码   OR 注册
	    
	    if (TAG==2) {
	    	textView_title.setText("重置密码");
	    	btn_regist.setText("重置密码");
	    	
		}else if (TAG==1) {
			textView_title.setText("注册");
			btn_regist.setText("注册");
		}
	    
	    setListener(regist_back,btn_getRegistCode,btn_regist);
	}
	public void setListener(View... view) {
		for (int i = 0; i < view.length; i++) {
			view[i].setOnClickListener(this);
		}
	}
	
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
			case R.id.regist_back:
				this.finish();
				break;
			case R.id.btn_getRegistCode://点击获取验证码
				if(!TextUtils.isEmpty(et_regist_login_name.getText().toString())){
					mobile=et_regist_login_name.getText().toString().trim();
					RequestDailog.showDialog(this, "正在发送验证码，请稍后");
					SMSSDK.getVerificationCode("86",mobile);
				}else {
					Toast.makeText(this, "电话不能为空", 1).show();
				}
				break;
			case R.id.btn_regist://点击注册    
				password=et_registPassWord.getText().toString().trim();//密码
				verify_code=et_registCode.getText().toString().trim();//验证码
				if (TAG==1)//请求注册接口
				{
					if(!TextUtils.isEmpty(et_registCode.getText().toString())){
						RequestDailog.showDialog(this, "正在注册，请稍后");
						HttpUtils.regist(res_regist, mobile, password, verify_code);
//					SMSSDK.submitVerificationCode("86", mobile, et_registCode.getText().toString());
					}else {
						Tools.Notic(B5_1_1_RegistActivity.this, "验证码不能为空", null);
					}
				}
				else if (TAG==2) //请求重置密码接口
				{
					if(!TextUtils.isEmpty(et_registCode.getText().toString())){
						RequestDailog.showDialog(this, "正在重置密码，请稍后");
						HttpUtils.resetPassword(res_restpasswd, mobile, password, verify_code);
//					SMSSDK.submitVerificationCode("86", mobile, et_registCode.getText().toString());
					}else {
						Tools.Notic(B5_1_1_RegistActivity.this, "验证码不能为空", null);
					}
				}
				
				break;
			default:
				break;
			
		}
		
	}
	
	public void initSmsSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, AppValue.APPID_mob, AppValue.APP_SECRE);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
					Message msg = new Message();
					msg.arg1 = event;
					msg.arg2 = result;
					msg.obj = data;
					handler.sendMessage(msg);
			};
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功+可以在这里请求服务器，把手机号+密码+验证码传给服务器", Toast.LENGTH_SHORT).show();
					HttpUtils.regist(res_regist, mobile, password, verify_code);
//					textView2.setText("提交验证码成功");
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					RequestDailog.closeDialog();
//					Toast.makeText(getApplicationContext(), "验证码已经发送，请稍后", Toast.LENGTH_SHORT).show();
//					textView2.setText("验证码已经发送");
				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
					Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
//					countryTextView.setText(data.toString());
					
				}
			} else {
				((Throwable) data).printStackTrace();
//				int resId = getStringRes(MainActivity.this, "smssdk_network_error");
				Toast.makeText(B5_1_1_RegistActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//				if (resId > 0) {
//					Toast.makeText(RegistActivity.this, resId, Toast.LENGTH_SHORT).show();
//				}
			}
			
		}
		
	};
	JsonHttpResponseHandler res_regist = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
//			Tools.Notic(C11_RegistActivity.this, "注册response="+response, null);
			RequestDailog.closeDialog();
//			Tools.Log("注册response="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//注册成功
			{
				Tools.Log("注册成功");
				try {
					putSharedPreferenceValue("username", datas.getString("username"));
					putSharedPreferenceValue("mobile", datas.getString("mobile"));
					putSharedPreferenceValue("userid", datas.getString("userid"));
					putSharedPreferenceValue("key", datas.getString("key"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Tools.Notic(B5_1_1_RegistActivity.this, "注册成功",null);
				Intent intent_tomainavtivity = new Intent(B5_1_1_RegistActivity.this, B0_MainActivity.class);
				startActivity(intent_tomainavtivity);
				B5_1_1_RegistActivity.this.finish();
			}
			else //注册失败
			{
				Tools.Notic(B5_1_1_RegistActivity.this, error+"", null);
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Tools.Log("注册失败"+errorResponse);
		}
		
	};
	
	JsonHttpResponseHandler res_restpasswd=new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//重置密码成功
			{
				Tools.Notic(B5_1_1_RegistActivity.this, "重置密码成功",new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent_tologinavtivity = new Intent(B5_1_1_RegistActivity.this, B5_1_LoginActivity.class);
						startActivity(intent_tologinavtivity);
						B5_1_1_RegistActivity.this.finish();
					}
				});
				
			}
			else //重置密码失败
			{
				Tools.Notic(B5_1_1_RegistActivity.this, error+"", null);
			}
			
		};
		
		
	};
	
}
