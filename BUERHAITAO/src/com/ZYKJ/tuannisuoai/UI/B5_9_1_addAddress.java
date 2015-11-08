package com.ZYKJ.tuannisuoai.UI;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 增加收货地址
 * 地址修改
 * @author zyk
 *
 */
public class B5_9_1_addAddress extends BaseActivity {

	private ImageButton address_back;
	private EditText et_name,et_phone,et_postcode,et_addressDetail;
	private TextView tv_choseOrigen,tv_yesorchange,tv_delate;
	private Button btn_setDefaultAddress;
	private RelativeLayout rl_delate,rl_setDefaultAddress;
	private String typeString;//通过typeString来判断是修改还是添加地址 change   add
	
	private int GetArea=1;
	private int ActivityFromChoseArea=0;
	

	String province,city,area;
	String city_id ,area_id,area_info;
	String address_id;
	
//	zip 邮编
	String true_name,address,zip,mob_phone;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent_fromIntent =getIntent();
		typeString=intent_fromIntent.getStringExtra("change");
		if (typeString.equals("change")&&(ActivityFromChoseArea==0)) //第一次进来，修改地址，加载用户信息
		{
			area_id=intent_fromIntent.getStringExtra("area_id");
			true_name=intent_fromIntent.getStringExtra("true_name");
			area_info=intent_fromIntent.getStringExtra("area_info");
			address=intent_fromIntent.getStringExtra("address");
			mob_phone=intent_fromIntent.getStringExtra("mob_phone");
			zip=intent_fromIntent.getStringExtra("zip");
			address_id=intent_fromIntent.getStringExtra("address_id");
			
			
			et_name.setText(true_name);
			et_phone.setText(mob_phone);
			et_postcode.setText(zip);
			et_addressDetail.setText(address);
			tv_choseOrigen.setText(area_info);
		}else if (ActivityFromChoseArea==1) //修改地址之后，加载用户信息
		{
			et_name.setText(true_name);
			et_phone.setText(mob_phone);
			et_postcode.setText(zip);
			et_addressDetail.setText(address);
			tv_choseOrigen.setText(area_info);
			
		}else {//第一次进来，新增加地址
			et_name.setText("");
			et_phone.setText("");
			et_postcode.setText("");
			et_addressDetail.setText("");
			rl_delate.setVisibility(View.GONE);
			rl_setDefaultAddress.setVisibility(View.GONE);
			
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b5_9_1_addaddress);
		
		address_back=(ImageButton) findViewById(R.id.address_back);
		et_name=(EditText) findViewById(R.id.et_name);
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_postcode=(EditText) findViewById(R.id.et_postcode);
		et_addressDetail=(EditText) findViewById(R.id.et_addressDetail);
		tv_choseOrigen=(TextView) findViewById(R.id.tv_choseOrigen);
		tv_yesorchange=(TextView) findViewById(R.id.tv_yesorchange);
		tv_delate=(TextView) findViewById(R.id.tv_delate);
		rl_delate=(RelativeLayout) findViewById(R.id.rl_delate);
		rl_setDefaultAddress=(RelativeLayout) findViewById(R.id.rl_setDefaultAddress);
		btn_setDefaultAddress=(Button) findViewById(R.id.btn_setDefaultAddress);
		
		setListener(address_back,tv_yesorchange,tv_choseOrigen,tv_delate,btn_setDefaultAddress);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.address_back://返回键
			this.finish();
			break;
		case R.id.tv_yesorchange://确定（修改）
			true_name=et_name.getText().toString();
			mob_phone=et_phone.getText().toString();
			zip=et_postcode.getText().toString();
			address=et_addressDetail.getText().toString();
			RequestDailog.showDialog(this, "正在保存地址");
			if (typeString.equals("change")) {
				HttpUtils.changeAddress(res_changeAddress, getSharedPreferenceValue("key"), address_id, true_name, area_id, city_id, address, area_info, "", zip, mob_phone);
			}else {
				HttpUtils.addAddress(res_addAddress, getSharedPreferenceValue("key"), true_name, city_id, area_id, area_info, "", address, zip, mob_phone);
			}
			break;
		case R.id.tv_choseOrigen://选择地区
//			HttpUtils.getArea(res_getArea,getSharedPreferenceValue("key"),"");
			//弹出地址选择页面
			true_name=et_name.getText().toString();
			mob_phone=et_phone.getText().toString();
			zip=et_postcode.getText().toString();
			address=et_addressDetail.getText().toString();
			Intent intent_to_chose =new Intent(this,B5_9_1_getArea.class);
			startActivityForResult(intent_to_chose, GetArea);
			break;
		case R.id.tv_delate://删除收货地址
			RequestDailog.showDialog(this, "正在删除，请稍后");
			HttpUtils.delAddress(res_delAddress, getSharedPreferenceValue("key"),address_id);
			break;
		case R.id.btn_setDefaultAddress://设为默认收货地址
			HttpUtils.setDefaultAddress(res_setDefaultAddress, getSharedPreferenceValue("key"), address_id);
			break;

		default:
			break;
		}
	}
	

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode==GetArea) {
			ActivityFromChoseArea=1;
			province=data.getStringExtra("province");
			city=data.getStringExtra("city");
			area=data.getStringExtra("area");
			area_info=province+city+area;
			city_id=data.getStringExtra("city_id");
			area_id=data.getStringExtra("area_id");
			et_name.setText(true_name);
			et_phone.setText(mob_phone);
			et_postcode.setText(zip);
			et_addressDetail.setText(address);
				
//			tv_choseOrigen.setText(province+" "+city+" "+area);
			tv_choseOrigen.setText(area_info);
			
		}
		
	};
	/**
	 * 增加收货地址
	 */
	JsonHttpResponseHandler res_addAddress = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("res_addAddress_response="+response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				B5_9_1_addAddress.this.finish();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
		
		
	};
	/**
	 * 编辑收货地址
	 */
	JsonHttpResponseHandler res_changeAddress = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("res_addAddress_response="+response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				B5_9_1_addAddress.this.finish();
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
		
		
	};
	/**
	 * 删除收货地址
	 */
	JsonHttpResponseHandler res_delAddress = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("res_addAddress_response="+response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//
			{
				Toast.makeText(B5_9_1_addAddress.this, "删除成功", Toast.LENGTH_LONG).show();
				B5_9_1_addAddress.this.finish();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
		
		
	};
	/**
	 * 设为默认地址
	 */
	JsonHttpResponseHandler res_setDefaultAddress = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("res_setDefaultAddress="+response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//
			{
				Toast.makeText(B5_9_1_addAddress.this, "设置成功", Toast.LENGTH_LONG).show();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
		
		
	};
}
