package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.JieSuanAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pingplusplus.android.PaymentActivity;

public class JieSuan1Activity extends BaseActivity {
	// 返回
	private ImageButton im_jiesuan_back;
	// 列表
	private MyListView listview;
	private JieSuanAdapter adapter;
	private String key;
	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private List<Map<String, String>> dataList1 = new ArrayList<Map<String, String>>();
	// 地址
	private TextView tv_buyer_name, tv_buyer_number, tv_buyer_address;
	// 结算
	private RelativeLayout rl_zhifufangshi;
	private TextView tv_zffs;
	// 是否支持货到付款
	private String ifshow_offpay;
	private String xzhdgg;
	private String allpri;
	private TextView tv_sumgoods1;
	private TextView tv_jiesuanqueren;
	private String address_id;
	private String storeid = "";
	// 钱包，微信，支付宝，货到付款
	private String pay_type1 = "", pay_type2 = "", pay_type3 = "",
			pay_type4 = "";
	private JSONObject jobstoreid;
	private static final String CHANNEL_WECHAT = "wx";// 通过微信支付
	private static final String CHANNEL_ALIPAY = "alipay";// 通过支付宝支付
	private static final int REQUEST_CODE_PAYMENT = 1;
	private String pay_sn;
	private int check = 0;
	private ImageView im_uncheck, im_check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_jiesuaninfo);
		iniView();

	}

	private void iniView() {
		im_jiesuan_back = (ImageButton) findViewById(R.id.im_jiesuan_back);
		listview = (MyListView) findViewById(R.id.list_shoppingcar1);
		tv_buyer_name = (TextView) findViewById(R.id.tv_buyer_name);// 收货人姓名
		tv_buyer_number = (TextView) findViewById(R.id.tv_buyer_number);// 收货人电话
		tv_buyer_address = (TextView) findViewById(R.id.tv_buyer_address);// 收货地址
		rl_zhifufangshi = (RelativeLayout) findViewById(R.id.rl_zhifufangshi);
		tv_sumgoods1 = (TextView) findViewById(R.id.tv_sumgoods1);// 总价格
		tv_jiesuanqueren = (TextView) findViewById(R.id.tv_jiesuanqueren);// 确认
		im_uncheck = (ImageView) findViewById(R.id.im_uncheck);
		im_check = (ImageView) findViewById(R.id.im_check);
		tv_zffs = (TextView) findViewById(R.id.tv_zffs);
		allpri = getIntent().getStringExtra("allpri");
		key = getSharedPreferenceValue("key");
		tv_sumgoods1.setText(allpri);
		xzhdgg = getIntent().getStringExtra("xzhdgg") + "|1";
		HttpUtils.getBuyFirst(res_ShoppingCarInfo, key, xzhdgg, "0");

		adapter = new JieSuanAdapter(this, dataList, dataList1, key);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setRefreshTime();

		setListener(im_jiesuan_back, rl_zhifufangshi, tv_jiesuanqueren,
				im_uncheck, im_check);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_jiesuan_back:
			JieSuan1Activity.this.finish();
			break;
		case R.id.rl_zhifufangshi:
			String a = ifshow_offpay;
			if (ifshow_offpay == "true") {
				UIDialog.ForFiveBtn(this, new String[] { "微信支付", "支付宝支付",
						"货到付款", "取消" }, this);
			} else {
				UIDialog.ForFourBtn(this,
						new String[] { "微信支付", "支付宝支付", "取消" }, this);
			}
			break;
		case R.id.dialog_five_modif_1:
			// tv_zffs.setText("钱包支付");
			// UIDialog.closeDialog();
			tv_zffs.setText("微信支付");
			UIDialog.closeDialog();

			break;
		case R.id.dialog_five_modif_2:
			tv_zffs.setText("支付宝支付");
			UIDialog.closeDialog();

			break;
		case R.id.dialog_five_modif_3:
			tv_zffs.setText("货到付款");
			UIDialog.closeDialog();

			break;
		case R.id.dialog_five_modif_4:
			tv_zffs.setText("支付宝支付");
			UIDialog.closeDialog();

			break;
		// case R.id.dialog_five_modif_5:
		// tv_zffs.setText("");
		// UIDialog.closeDialog();
		//
		// break;
		case R.id.dialog_four_modif_1:
			// tv_zffs.setText("钱包支付");
			// UIDialog.closeDialog();
			tv_zffs.setText("微信支付");
			Object asss = new Object();
			UIDialog.closeDialog();
			break;
		case R.id.dialog_four_modif_2:
			tv_zffs.setText("支付宝支付");
			UIDialog.closeDialog();

			break;
		case R.id.dialog_four_modif_3:
			tv_zffs.setText("支付宝支付");
			UIDialog.closeDialog();

			break;
		// case R.id.dialog_four_modif_4:
		// tv_zffs.setText("");
		// UIDialog.closeDialog();
		//
		// break;
		case R.id.tv_jiesuanqueren:
			// if(tv_zffs.getText().toString().equals("钱包支付")) {
			// String dlyo_pickup_typ =
			// "{"+storeid.substring(1,storeid.length())+"}";
			// String pay_type =
			// "{"+pay_type1.substring(1,pay_type1.length())+"}";
			// try {
			// JSONObject myJsonObject = new JSONObject(dlyo_pickup_typ);
			// JSONObject myJsonObject1 = new JSONObject(pay_type);
			// HttpUtils.getBuySecond(res_BuySecond,key,"0",xzhdgg,address_id,null,myJsonObject,myJsonObject1,"1");
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }else
			if (tv_zffs.getText().toString().equals("微信支付")) {
				String dlyo_pickup_typ = "{"
						+ storeid.substring(1, storeid.length()) + "}";
				String pay_type = "{"
						+ pay_type2.substring(1, pay_type2.length()) + "}";
				try {
					JSONObject myJsonObject = new JSONObject(dlyo_pickup_typ);
					JSONObject myJsonObject1 = new JSONObject(pay_type);
					if (check > 0) {
						HttpUtils.getBuySecond(res_BuySecond, key, "0", xzhdgg,
								address_id, null, myJsonObject, myJsonObject1,
								"1","android");
					} else {
						HttpUtils.getBuySecond(res_BuySecond, key, "0", xzhdgg,
								address_id, null, myJsonObject, myJsonObject1,
								"0","android");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// HttpUtils.payTheOrder(res_payTheOrder, key, pay_sn,
				// CHANNEL_WECHAT);
				// HttpUtils.getBuySecond(res_BuySecond,"3ae653eb52824dbc4ba977de343e2e12","1",allcheckinfo,address_id
				// ,0);
			} else if (tv_zffs.getText().toString().equals("支付宝支付")) {
				String dlyo_pickup_typ = "{"
						+ storeid.substring(1, storeid.length()) + "}";
				String pay_type = "{"
						+ pay_type3.substring(1, pay_type3.length()) + "}";
				try {
					JSONObject myJsonObject = new JSONObject(dlyo_pickup_typ);
					JSONObject myJsonObject1 = new JSONObject(pay_type);
					if (check > 0) {
						HttpUtils.getBuySecond(res_BuySecond, key, "0", xzhdgg,
								address_id, null, myJsonObject, myJsonObject1,
								"1","android");
					} else {
						HttpUtils.getBuySecond(res_BuySecond, key, "0", xzhdgg,
								address_id, null, myJsonObject, myJsonObject1,
								"0","android");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// HttpUtils.payTheOrder(res_payTheOrder, key, pay_sn,
				// CHANNEL_ALIPAY);
			} else if (tv_zffs.getText().toString().equals("货到付款")) {
				String dlyo_pickup_typ = "{"
						+ storeid.substring(1, storeid.length()) + "}";
				String pay_type = "{"
						+ pay_type4.substring(1, pay_type4.length()) + "}";

				try {
					JSONObject myJsonObject = new JSONObject(dlyo_pickup_typ);
					JSONObject myJsonObject1 = new JSONObject(pay_type);
					HttpUtils.getBuySecond(res_BuySecond,key,"1",xzhdgg,address_id,null,myJsonObject,myJsonObject1,"0","android");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
					Toast.makeText(getApplicationContext(), "请选择支付方式",
							Toast.LENGTH_LONG).show();
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
		default:
			break;

		}
	}

	/**
	 * 购买第一步
	 */
	JsonHttpResponseHandler res_ShoppingCarInfo = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				// Tools.Log("datas="+datas);
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				try {
					dataList.clear();
					// address_info
					JSONObject address_object = datas
							.getJSONObject("address_info");
					tv_buyer_name.setText("收货人："
							+ address_object.getString("true_name"));
					tv_buyer_number.setText(address_object
							.getString("mob_phone"));
					tv_buyer_address.setText("收货地址："
							+ address_object.getString("address"));
					address_id = address_object.getString("address_id");
					ifshow_offpay = datas.getString("ifshow_offpay");

					JSONArray store_cart_list = datas
							.getJSONArray("store_cart_list");
					for (int i = 0; i < store_cart_list.length(); i++) {
						JSONObject jsonItem = store_cart_list.getJSONObject(i);
						Map<String, String> map1 = new HashMap();
						Map<String, Object> map = new HashMap();
						map1.put("store_name", jsonItem.getString("store_name"));
						map1.put("store_goods_total",
								jsonItem.getString("store_goods_total"));
						map1.put("store_freight_price",
								jsonItem.getString("store_freight_price"));
						map.put("goods_list",
								jsonItem.getJSONArray("goods_list"));
						// storeid = ;
						storeid = storeid
								+ ","
								+ "\""
								+ jsonItem.getJSONArray("goods_list")
										.getJSONObject(0).getString("store_id")
								+ "\" : \"物流配送\"";
						pay_type1 = pay_type1
								+ ","
								+ "\""
								+ jsonItem.getJSONArray("goods_list")
										.getJSONObject(0).getString("store_id")
								+ "\" : \"offline\"";
						pay_type2 = pay_type2
								+ ","
								+ "\""
								+ jsonItem.getJSONArray("goods_list")
										.getJSONObject(0).getString("store_id")
								+ "\" : \"online\"";
						pay_type3 = pay_type3
								+ ","
								+ "\""
								+ jsonItem.getJSONArray("goods_list")
										.getJSONObject(0).getString("store_id")
								+ "\" : \"online\"";
						pay_type4 = pay_type4
								+ ","
								+ "\""
								+ jsonItem.getJSONArray("goods_list")
										.getJSONObject(0).getString("store_id")
								+ "\" : \"offline\"";
						// {"3" : "自提",}
						dataList.add(map);
						dataList1.add(map1);
					}
					/*
					 * JSONArray order_group_list =
					 * datas.getJSONArray("order_group_list");//
					 * Tools.Log("order_group_list="+order_group_list); for (int
					 * j = 0; j < order_group_list.length(); j++) { Map<String,
					 * Object> map = new HashMap(); JSONObject order_group_list1
					 * = (JSONObject) order_group_list.get(j); JSONArray
					 * order_list =
					 * order_group_list1.getJSONArray("order_list"); JSONObject
					 * order_list1 =((JSONObject) order_list.get(0));
					 * 
					 * map.put("store_name",
					 * order_list1.getString("store_name"));
					 * map.put("store_phone",
					 * order_list1.getString("store_phone"));
					 * map.put("order_id", order_list1.getString("order_id"));
					 * map.put("pay_sn", order_list1.getString("pay_sn"));
					 * map.put("order_amount",
					 * order_list1.getString("order_amount"));
					 * map.put("extend_order_goods",
					 * order_list1.getJSONArray("extend_order_goods"));
					 * dataList.add(map); }
					 */
					adapter.notifyDataSetChanged();
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else// 失败
			{
				Tools.Log("res_Points_error=" + error + "");
				// Tools.Notic(B5_MyActivity.this, error+"", null);
			}
		}
	};

	/**
	 * 购买第二步
	 */
	JsonHttpResponseHandler res_BuySecond = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				// Tools.Log("datas="+datas);
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				try {
					if (datas.getString("order_amount").equals("0")) {
						Toast.makeText(getApplicationContext(), "用钱包支付成功！", Toast.LENGTH_LONG).show();
						Intent itgou = new Intent(JieSuan1Activity.this,B3_ShoppingCartActivity.class);
						startActivity(itgou);
					}else {
						pay_sn = datas.getString("pay_sn");

						if (tv_zffs.getText().toString().equals("微信支付")) {
							HttpUtils.payTheOrder(res_payTheOrder, key, pay_sn,
									CHANNEL_WECHAT);

						} else if (tv_zffs.getText().toString().equals("支付宝支付")) {
							HttpUtils.payTheOrder(res_payTheOrder, key, pay_sn,
									CHANNEL_ALIPAY);

						} else if (tv_zffs.getText().toString().equals("货到付款")) {

						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else// 失败
			{
				Toast.makeText(getApplicationContext(), error,
						Toast.LENGTH_LONG).show();
				Tools.Log("res_Points_error=" + error + "");
			}
		}
	};

	/**
	 * 付款
	 */
	JsonHttpResponseHandler res_payTheOrder = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Log.e("付款", response + "");
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				Intent intent = new Intent();
				String packageName = getPackageName();
				ComponentName componentName = new ComponentName(packageName,
						packageName + ".wxapi.WXPayEntryActivity");
				intent.setComponent(componentName);
				intent.putExtra(PaymentActivity.EXTRA_CHARGE,
						response.toString());
				startActivityForResult(intent, REQUEST_CODE_PAYMENT);
			} else// 失败
			{
				Tools.Log("res_Points_error=" + error + "");
				// Tools.Notic(B5_MyActivity.this, error+"", null);
			}
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
							// TODO Auto-generated method stub
							Intent intent_toMy = new Intent(
									JieSuan1Activity.this, B5_MyActivity.class);
							startActivity(intent_toMy);
							finish();
						}
					});
				} else if (result.equals("fail")) {
					Tools.Notic(this, "支付失败，请重试", null);
				} else if (result.equals("cancel")) {
					Tools.Notic(this, "支付取消", null);
				} else if (result.equals("invalid")) {
					Tools.Notic(this, "支付失败，请重新支付", null);

				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Tools.Notic(this, "支付取消", null);
			}
		}
	}
}

