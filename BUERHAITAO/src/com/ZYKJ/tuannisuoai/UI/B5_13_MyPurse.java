package com.ZYKJ.tuannisuoai.UI;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pingplusplus.android.PaymentActivity;
/**
 * 我的钱包 页
 * @author zyk
 *
 */
public class B5_13_MyPurse extends BaseActivity {
	/**
	 *返回
	 */
	ImageButton mypurse_back;
	/**
	 * 钱包余额
	 */
	TextView tv_remaining;
	/**
	 *输入的 充值金额
	 */
	EditText et_chagenumber;
	/**
	 * 立即充值 按钮
	 */
	Button btn_recharge;
	String recharge_numberString;//充值金额
	
    private static final String CHANNEL_WECHAT = "wx";//通过微信充值
    private static final String CHANNEL_ALIPAY = "alipay";//通过支付宝充值
    
    private static final int REQUEST_CODE_PAYMENT = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.b5_13_mypurse);
		mypurse_back = (ImageButton) findViewById(R.id.mypurse_back);
		tv_remaining = (TextView) findViewById(R.id.tv_remaining);
		et_chagenumber = (EditText) findViewById(R.id.et_chagenumber);
		et_chagenumber.setInputType(InputType.TYPE_CLASS_NUMBER);
		btn_recharge = (Button) findViewById(R.id.btn_recharge);
		setListener(mypurse_back,btn_recharge);
		tv_remaining.setText("￥"+getSharedPreferenceValue("predeposit"));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.mypurse_back://返回
			this.finish();
		break;
		case R.id.btn_recharge://立即充值
			recharge_numberString = et_chagenumber.getText().toString().trim();
			if (recharge_numberString.equals("")) {
				Toast.makeText(this, "请输入充值金额", 500).show();
			}else {//跳出来支付方式选择
				UIDialog.ForThreeBtn(this, new String[] { "微信", "支付宝","取消" }, this);
			}
		break;
		case R.id.dialog_modif_1:// 微信
			UIDialog.closeDialog();
			HttpUtils.recharge(res_recharge, getSharedPreferenceValue("key"), recharge_numberString, CHANNEL_WECHAT);
			break;
		case R.id.dialog_modif_2:// 支付宝
			UIDialog.closeDialog();
//			Log.e("key=", getSharedPreferenceValue("key")+"");
//			Log.e("recharge_numberString=", recharge_numberString);
//			Log.e("CHANNEL_ALIPAY=", CHANNEL_ALIPAY);
			HttpUtils.recharge(res_recharge, getSharedPreferenceValue("key"), recharge_numberString, CHANNEL_ALIPAY);
			break;
		case R.id.dialog_modif_3:// 取消
			UIDialog.closeDialog();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 充值回调
	 */
	JsonHttpResponseHandler res_recharge = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Log.e("res_recharge_response=", response+"");
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Intent intent = new Intent();
	            String packageName = getPackageName();
	            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
	            intent.setComponent(componentName);
	            intent.putExtra(PaymentActivity.EXTRA_CHARGE, response.toString());
	            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
		
		
	};
	

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
//                Tools.Log("支付结果="+result);
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);
                if (result.equals("success")) {
					Tools.Notic(B5_13_MyPurse.this, "您已经充值成功", new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent_toMy = new Intent(B5_13_MyPurse.this,B5_MyActivity.class);
							startActivity(intent_toMy);
							B5_13_MyPurse.this.finish();
						}
					});
				}else if (result.equals("fail")) {
					Tools.Notic(B5_13_MyPurse.this, "充值失败，请重试", null);
				}else if (result.equals("cancel")) {
					Tools.Notic(B5_13_MyPurse.this, "充值取消", null);
				}else if (result.equals("invalid")) {
					Tools.Notic(B5_13_MyPurse.this, "充值失败，请重新充值", null);
					
				}
            } else if (resultCode == Activity.RESULT_CANCELED) {
            	Tools.Notic(B5_13_MyPurse.this, "充值取消", null);
            }
        }
    }
    
//    public void showMsg(String title, String msg1, String msg2) {
//    	String str = title;
//    	if (msg1.length() != 0) {
//    		str += "\n" + msg1;
//    	}
//    	if (msg2.length() != 0) {
//    		str += "\n" + msg2;
//    	}
//    	AlertDialog.Builder builder = new Builder(B5_13_MyPurse.this);
//    	builder.setMessage(str);
//    	builder.setTitle("提示");
//    	builder.setPositiveButton("OK", null);
//    	builder.create().show();
//    }
	
}
