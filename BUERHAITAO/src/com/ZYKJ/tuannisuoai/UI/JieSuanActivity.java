package com.ZYKJ.tuannisuoai.UI;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.JieSuanAdapter;
import com.ZYKJ.tuannisuoai.adapter.JieSuanAdapter.OnSelectedFreightListener;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.CarJieSuan;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.StringUtil;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyExpandableListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pingplusplus.android.PaymentActivity;
import com.ta.utdid2.android.utils.StringUtils;

public class JieSuanActivity extends BaseActivity implements OnSelectedFreightListener{
	// 返回
	private ImageButton im_jiesuan_back;
	// 列表
	private MyExpandableListView listview;
	private JieSuanAdapter adapter;
	private String key;
	// 地址
	private LinearLayout order_show_address,order_add_address;
	private TextView tv_buyer_name, tv_buyer_number, tv_buyer_address;
	// 结算
	private RelativeLayout rl_zhifufangshi,et_buy_address;
	private TextView tv_zffs;
	// 是否支持货到付款
	private String ifshow_offpay;
	private String allcheckinfo;
	private float allpri;
	private TextView tv_sumgoods1;
	private TextView tv_jiesuanqueren;
	private String address_id;
	private static final String CHANNEL_WECHAT = "wx";// 通过微信支付
	private static final String CHANNEL_ALIPAY = "alipay";// 通过支付宝支付
	private static final int REQUEST_CODE_PAYMENT = 1;
	private String pay_sn;
	private int check = 0;
	private ImageView im_uncheck, im_check;
	private int GetAddress=1;
	private List<CarJieSuan> object = new ArrayList<CarJieSuan>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_jiesuaninfo);
		allpri = Float.valueOf(getIntent().getStringExtra("allpri"));
		allcheckinfo = getIntent().getStringExtra("allcheckinfo");
		Log.e("key--------------------------", getSharedPreferenceValue("key"));
		
		iniView();
	}

	private void iniView() {
		im_jiesuan_back = (ImageButton) findViewById(R.id.im_jiesuan_back);
		listview = (MyExpandableListView) findViewById(R.id.list_shoppingcar1);
		listview.setGroupIndicator(null);
		et_buy_address = (RelativeLayout) findViewById(R.id.et_buy_address);// 收货人姓名
		order_show_address = (LinearLayout) findViewById(R.id.order_show_address);// 收货人姓名
		order_add_address = (LinearLayout) findViewById(R.id.order_add_address);// 收货人姓名
		tv_buyer_name = (TextView) findViewById(R.id.tv_buyer_name);// 收货人姓名
		tv_buyer_number = (TextView) findViewById(R.id.tv_buyer_number);// 收货人电话
		tv_buyer_address = (TextView) findViewById(R.id.tv_buyer_address);// 收货地址
		rl_zhifufangshi = (RelativeLayout) findViewById(R.id.rl_zhifufangshi);
		tv_sumgoods1 = (TextView) findViewById(R.id.tv_sumgoods1);// 总价格
		tv_jiesuanqueren = (TextView) findViewById(R.id.tv_jiesuanqueren);// 确认
		im_uncheck = (ImageView) findViewById(R.id.im_uncheck);
		im_check = (ImageView) findViewById(R.id.im_check);
		tv_zffs = (TextView) findViewById(R.id.tv_zffs);
		key = getSharedPreferenceValue("key");
//		tv_sumgoods1.setText(allpri);
		setListener(im_jiesuan_back, rl_zhifufangshi, tv_jiesuanqueren, im_uncheck, im_check,et_buy_address);

		HttpUtils.getBuyFirst(res_ShoppingCarInfo, key, allcheckinfo, "1");
	}

	/**
	 * 购买第一步
	 */
	AsyncHttpResponseHandler res_ShoppingCarInfo = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				String responseString = new String(responseBody, HTTP.UTF_8);
				JSONObject datas = JSONObject.parseObject(responseString);
				JSONObject jsonData = datas.getJSONObject("datas");
				String error = jsonData.getString("error");
				if(StringUtils.isEmpty(error)){
					Object requestAdd = jsonData.get("address_info");//收货地址信息
					if(requestAdd instanceof JSONObject){
						JSONObject address_object = (JSONObject)requestAdd;
						tv_buyer_name.setText("收货人：" + address_object.getString("true_name"));//收货人
						tv_buyer_number.setText(address_object.getString("mob_phone"));//收货人电话
						tv_buyer_address.setText("收货地址：" + address_object.getString("address"));//收货地址
						address_id = address_object.getString("address_id");//地址ID
						order_show_address.setVisibility(View.VISIBLE);
						order_add_address.setVisibility(View.GONE);
					}else{
						order_show_address.setVisibility(View.GONE);
						order_add_address.setVisibility(View.VISIBLE);
					}
					ifshow_offpay = jsonData.getString("ifshow_offpay");//支持货到付款时为true
					JSONArray store_cart_list = jsonData.getJSONArray("store_cart_list");//商品列表
					for (int i = 0; i < store_cart_list.size(); i++) {
						String freight = store_cart_list.getJSONObject(i).getString("store_freight_price");
						allpri += Float.valueOf(StringUtil.toString(freight, "0"));
						JSONObject good = store_cart_list.getJSONObject(i).getJSONArray("goods_list").getJSONObject(0);
						CarJieSuan jiesuan = new CarJieSuan();
						jiesuan.setStore_id(good.getString("store_id"));
						jiesuan.setPayType("online");
						jiesuan.setDlyoPickupType("物流配送");
						object.add(jiesuan);
					}
					tv_sumgoods1.setText(allpri+"");
					adapter = new JieSuanAdapter(JieSuanActivity.this, store_cart_list, object, JieSuanActivity.this);
					listview.setAdapter(adapter);
					//默认展开
					for (int i = 0; i < store_cart_list.size(); i++) {
						listview.expandGroup(i);
					}
					listview.setOnGroupClickListener(new OnGroupClickListener() {
						@Override
						public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
							return true;
						}
					});
				}else{
					Toast.makeText(JieSuanActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			Toast.makeText(JieSuanActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
		}
	};
	private void setPayType(String payType){
		for (int i = 0; i < object.size(); i++) {
			object.get(i).setPayType(payType);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_jiesuan_back:
			JieSuanActivity.this.finish();
			break;
		case R.id.rl_zhifufangshi:
			if (ifshow_offpay == "true") {
				UIDialog.ForFiveBtn(this, new String[] { "微信支付", "支付宝支付", "货到付款", "取消" }, this);
			} else {
				UIDialog.ForFourBtn(this,new String[] { "微信支付", "支付宝支付", "取消" }, this);
			}
			break;
		case R.id.dialog_five_modif_1:
			tv_zffs.setText("微信支付");
			setPayType("online");
			UIDialog.closeDialog();
			break;
		case R.id.dialog_five_modif_2:
			tv_zffs.setText("支付宝支付");
			setPayType("online");
			UIDialog.closeDialog();
			break;
		case R.id.dialog_five_modif_3:
			tv_zffs.setText("货到付款");
			setPayType("offline");
			UIDialog.closeDialog();
			break;
		case R.id.dialog_five_modif_4:
			UIDialog.closeDialog();
			break;
		case R.id.dialog_four_modif_1:
			tv_zffs.setText("微信支付");
			setPayType("online");
			UIDialog.closeDialog();
			break;
		case R.id.dialog_four_modif_2:
			tv_zffs.setText("支付宝支付");
			setPayType("online");
			UIDialog.closeDialog();
			break;
		case R.id.dialog_four_modif_3:
			UIDialog.closeDialog();
			break;
		case R.id.tv_jiesuanqueren:
			if(StringUtils.isEmpty(address_id)){
				Toast.makeText(this, "地址不能为空", Toast.LENGTH_LONG).show();
			}else{
				JSONObject message = new JSONObject();//留言
				JSONObject pickupType = new JSONObject();//配送方式
				JSONObject payType = new JSONObject();//支付方式
				for (int i = 0; i < object.size(); i++) {
					CarJieSuan jiesuan = object.get(i);
					message.put(jiesuan.getStore_id(), jiesuan.getMessage());
					pickupType.put(jiesuan.getStore_id(), jiesuan.getDlyoPickupType());
					payType.put(jiesuan.getStore_id(), jiesuan.getPayType());
				}
				RequestParams params = new RequestParams();
				params.put("key",key);
				params.put("ifcart","1");
				params.put("cart_id",allcheckinfo);
				params.put("address_id",address_id);
				params.put("pay_message",message.toString());
				params.put("dlyo_pickup_type",pickupType.toString());
				params.put("pay_type",payType.toString());
				params.put("pd_pay",check);
				params.put("client_type", "android");
				HttpUtils.getBuySecond(res_BuySecond, params);
			}
			break;
		case R.id.im_uncheck:
			im_check.setVisibility(View.VISIBLE);
			im_uncheck.setVisibility(View.GONE);
			check = 1;
			break;
		case R.id.im_check:
			im_uncheck.setVisibility(View.VISIBLE);
			im_check.setVisibility(View.GONE);
			check = 0;
			break;
		case R.id.et_buy_address:
			Intent i_tochoseAddress = new Intent(JieSuanActivity.this,B5_9_MyAddressManagement.class);
			i_tochoseAddress.putExtra("ChoseAddress", true);
			startActivityForResult(i_tochoseAddress, GetAddress);
			break;
		default:
			break;

		}
	}

	/**
	 * 购买第二步
	 */
	AsyncHttpResponseHandler res_BuySecond = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			RequestDailog.closeDialog();
			String responseString = "";
			try {
				responseString = new String(responseBody, HTTP.UTF_8);
				JSONObject datas = JSONObject.parseObject(responseString);
				JSONObject jsonData = datas.getJSONObject("datas");
				String error = jsonData.getString("error");
				if(StringUtils.isEmpty(error)){
					if (jsonData.getFloat("order_amount") <= 0) {
						Toast.makeText(getApplicationContext(), "用钱包支付成功！", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 20));
						finish();
					}else {
						pay_sn = jsonData.getString("pay_sn");
						if (tv_zffs.getText().toString().equals("微信支付")) {
							HttpUtils.payTheOrder(res_payTheOrder, key, pay_sn, CHANNEL_WECHAT);
						} else if (tv_zffs.getText().toString().equals("支付宝支付")) {
							HttpUtils.payTheOrder(res_payTheOrder, key, pay_sn, CHANNEL_ALIPAY);
						} else if (tv_zffs.getText().toString().equals("货到付款")) {
							startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 20));
							finish();
						}
					}
				}else{
					Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
					Tools.Log("res_Points_error=" + error + "");
				}
			}catch(Exception e){
				Log.e("httpError=====", responseString);
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_LONG).show();
		}
	};

	/**
	 * 付款
	 */
	AsyncHttpResponseHandler res_payTheOrder = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			RequestDailog.closeDialog();
			try {
				String responseString = new String(responseBody, HTTP.UTF_8);
				JSONObject jsonData = JSONObject.parseObject(responseString);
				JSONObject datas = jsonData.getJSONObject("datas");
//				String error = datas.getString("error");
				if(datas == null){
					Intent intent = new Intent();
					String packageName = getPackageName();
					ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
					intent.setComponent(componentName);
					intent.putExtra(PaymentActivity.EXTRA_CHARGE, responseString);
					startActivityForResult(intent, REQUEST_CODE_PAYMENT);
				}else{
					Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_LONG).show();
//					Tools.Log("res_Points_error=" + datas.toString() + "");
					startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 10));
					finish();
				}
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_LONG).show();
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getExtras().getString("pay_result");
				/*
				 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
				// Tools.Log("支付结果="+result);
				// String errorMsg = data.getExtras().getString("error_msg"); //
				// 错误信息
				// String extraMsg = data.getExtras().getString("extra_msg"); //
				// 错误信息
				// showMsg(result, errorMsg, extraMsg);
				if (result.equals("success")) {
					Tools.Notic(this, "您已经付款成功", new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 20));
							finish();
						}
					});
				} else if (result.equals("fail")) {
					Tools.Notic(this, "支付失败，请重试", null);
					startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 10));
					finish();
				} else if (result.equals("cancel")) {
					Tools.Notic(this, "支付取消", null);
					startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 10));
					finish();
				} else if (result.equals("invalid")) {
					Tools.Notic(this, "支付失败，请重新支付", null);
					startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 10));
					finish();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Tools.Notic(this, "支付取消", null);
				startActivity(new Intent(JieSuanActivity.this, B5_5_OrderStatus.class).putExtra("STATUS", 10));
				finish();
			}else if (resultCode==GetAddress && resultCode == Activity.RESULT_OK) {
				order_show_address.setVisibility(View.VISIBLE);
				order_add_address.setVisibility(View.GONE);
				address_id=data.getStringExtra("address_id");
				tv_buyer_name.setText("姓名:"+data.getStringExtra("true_name"));
				tv_buyer_number.setText("  电话:"+data.getStringExtra("mob_phone"));
				tv_buyer_address.setText("地址:"+data.getStringExtra("area_info")+data.getStringExtra("address"));
//				tv_address.setText("姓名:"+data.getStringExtra("true_name")+"  电话:"+data.getStringExtra("mob_phone")+"\n"+data.getStringExtra("area_info")+data.getStringExtra("address"));
			}
		}
	}

	@Override
	public void setOrderPrice(int type, float price) {
		if(type == 0){
			allpri -= price;
		}else{
			allpri += price;
		}
		tv_sumgoods1.setText(allpri+"");
	}
}
