package com.ZYKJ.tuannisuoai.UI;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
/**
 * 登录界面
 * @author zyk
 *
 */
public class B5_1_LoginActivity extends BaseActivity {
	
	private static final String TAG = B5_1_LoginActivity.class.getSimpleName();
	private ImageButton login_back;
	private TextView tv_regist,tv_forgetPassWord;
	private EditText et_login_name,et_passWord;
	private Button btn_login;
	private String login_name,passWord;
	//第三方登录初始化
	UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_login);
		
		login_back=(ImageButton)findViewById(R.id.login_back);
		tv_regist=(TextView) findViewById(R.id.tv_regist);
		tv_forgetPassWord=(TextView) findViewById(R.id.tv_forgetPassWord);
		et_login_name=(EditText) findViewById(R.id.et_login_name);
		et_passWord=(EditText) findViewById(R.id.et_passWord);
		btn_login=(Button) findViewById(R.id.btn_login);
		ImageView login_tv=(ImageView) findViewById(R.id.login_tv);//QQ登录
		ImageView login_tv2=(ImageView) findViewById(R.id.login_tv2);//微信登录
		setListener(login_back,tv_regist,tv_forgetPassWord,btn_login,login_tv,login_tv2);
	}
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
			case R.id.login_back:
//				Intent intent_B0_MainActivity=new Intent();
//				intent_B0_MainActivity.putExtra("TabTAG", 1);
//				intent_B0_MainActivity.setClass(this, B0_MainActivity.class);
//				startActivity(intent_B0_MainActivity);
				this.finish();
				break;
			case R.id.tv_regist://注册
				Intent intent_regist=new Intent();
				intent_regist.setClass(this, B5_1_1_RegistActivity.class);
				intent_regist.putExtra("FIND_OR_REGIST", 1);
				startActivity(intent_regist);
				break;
			case R.id.tv_forgetPassWord://忘记密码
				Intent intent_regist2=new Intent();
				intent_regist2.setClass(this, B5_1_1_RegistActivity.class);
				intent_regist2.putExtra("FIND_OR_REGIST", 2);
				startActivity(intent_regist2);
				break;
			case R.id.btn_login:
				RequestDailog.showDialog(this, "正在登录");
				login_name=et_login_name.getText().toString().trim();
				passWord=et_passWord.getText().toString().trim();
				HttpUtils.login(res_login, login_name, passWord);
				break;
			case R.id.login_tv:
				/*QQ登录*/
				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104913288",
		                "1yMYgvRSvD2RCAXW");
				qqSsoHandler.addToSocialSDK();
				login(SHARE_MEDIA.QQ, 1);
				break;
			case R.id.login_tv2:
				/*微信登录*/
				UMWXHandler wxHandler = new UMWXHandler(this,"wxb49d124a778b0381","03133af7c57f6a16a6e4b59d299e8e6f");
				wxHandler.addToSocialSDK();
				login(SHARE_MEDIA.WEIXIN, 2);
				break;
			default:
				break;
			
		}
	}

	/**
	 * 根据平台登录</br>
	 */
	private void login(final SHARE_MEDIA platform, final int id) {
		mController.doOauthVerify(this, platform, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				Log.e(TAG, "授权开始");
			}
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(B5_1_LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(B5_1_LoginActivity.this, "取消登录", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				String uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					Log.e("platform-----------------", value.toString());
					getUserInfo(platform, uid, id);//获取用户信息
				} else {
					Toast.makeText(B5_1_LoginActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 获取用户信息</br>
	 */
	private void getUserInfo(SHARE_MEDIA platform, final String openid, final int id) {
		mController.getPlatformInfo(this, platform, new UMDataListener() {
			@Override
			public void onStart() {
		        Toast.makeText(B5_1_LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (status == 200 && info != null) {
					if (id == 1) {
						/*QQ登录*/
						String username = (String)info.get("screen_name");
						RequestParams params = new RequestParams();
						params.put("openid", openid);
						params.put("username", username);
						params.put("client", "android");
						params.put("op", "qq");
						HttpUtils.login(res_login, params);
					} else if (id == 2) {
						/*微信登录*/
						String username = (String)info.get("nickname");
						RequestParams params = new RequestParams();
						params.put("openid", openid);
						params.put("username", username);
						params.put("client", "android");
						params.put("op", "weixin");
						HttpUtils.login(res_login, params);
					}
				} else {
			        Toast.makeText(B5_1_LoginActivity.this, "发生错误：" + status, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void onBackPressed() {
//		Intent intent_B0_MainActivity=new Intent();
//		intent_B0_MainActivity.setClass(this, B0_MainActivity.class);
//		startActivity(intent_B0_MainActivity);
		this.finish();
	};
	JsonHttpResponseHandler res_login = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
//			Tools.Log("登录"+response);
//			{"datas":{"error":"用户名密码错误"},"code":200}
			String error=null;
			JSONObject datas=null;
//			Tools.Log("res_login="+response);
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getJSONObject("datas").getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (error==null)//登录成功
			{
				try {
					putSharedPreferenceValue("username", datas.getString("username"));
					putSharedPreferenceValue("userid", datas.getString("userid"));
					putSharedPreferenceValue("key", datas.getString("key"));
					putSharedPreferenceValue("avatar", datas.getString("avatar"));
//					Tools.Log("image="+datas.getString("avatar"));
					putSharedPreferenceValue("member_points", datas.getString("member_points"));
					putSharedPreferenceValue("predeposit", datas.getString("predeposit"));
					/*QQ、微信登录不存在手机号*/
					putSharedPreferenceValue("mobile", datas.getString("mobile"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Tools.Notic(B5_1_LoginActivity.this, "登录成功", null);
				Intent  intent_tomainavtivity = new Intent(B5_1_LoginActivity.this, B0_MainActivity.class);
				startActivity(intent_tomainavtivity);
				B5_1_LoginActivity.this.finish();
			}
			else//登录失败 
			{
				Tools.Notic(B5_1_LoginActivity.this, error+"", null);
			}
			
		}
		
		
	};
}
