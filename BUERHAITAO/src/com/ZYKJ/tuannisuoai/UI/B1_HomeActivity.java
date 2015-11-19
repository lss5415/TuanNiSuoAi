package com.ZYKJ.tuannisuoai.UI;

/**
 * 首页界面 lss 6.17
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B1_a2_CaiNiLikeAdapter;
import com.ZYKJ.tuannisuoai.adapter.B1_a3_MeiRiHaoDianAdapter;
import com.ZYKJ.tuannisuoai.adapter.HorizontalListViewAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.AutoListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.ToastView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class B1_HomeActivity extends BaseActivity {
	// 首页中间八个大分类
	private ImageView im_b1nvshi, im_b1nanshi, im_b1muying, im_b1huazhuang,
			im_b1shouji, im_b1bangong, im_b1shenghuo, im_b1techan;
	// 天天特价,晒单圈,猜你喜欢，每日好店
	private RelativeLayout rl_b1_a1tttj, b5_3_shaidanquan, rl_b1_a2_cnxh,
			rl_b1_a3_mrhd;
	// 搜索选择
	private RelativeLayout rl_sousuokuang;
	// 每日好店
	private AutoListView list_meirihaodian, list_cainilike;
	private AutoListView listviewHorizontal;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> data1 = new ArrayList<Map<String, String>>();
	// 天天特价
	private ImageView im_b1_a1_pic, im_moreinfo;
	private TextView tv_b1_a1_chanpinname, tv_b1_a1_chanpinjianjie,
			tv_b1_a1_zhehoujia, tv_b1_a1_yuanjia, tv_goodsid;
	private LinearLayout ll_moreinfolayout;
	private RelativeLayout ll_dayspecial;
	private RelativeLayout rl_ditu;
	private String cityname;
	private TextView tv_cityname;// 城市名称
	private TextView tv_updateNumber;// 晒单圈更新数
	private List<Map<String, String>> data2 = new ArrayList<Map<String, String>>();
	private EditText a1_sousuofujin;
	private TextView tv_dianpuming, tv_kucun, tv_xiaoliang;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_index);
		initView();
		tv_cityname = (TextView) findViewById(R.id.tv_cityname);
		if (getIntent().getStringExtra("cityname") == null) {
			putSharedPreferenceValue("lng", "118.338501");
			putSharedPreferenceValue("lat", "35.063786");
			putSharedPreferenceValue("cityid", "235");
			putSharedPreferenceValue("cityname", "临沂");
			HttpUtils.getFirstList(res_getSyList, "235", "118.338501",
					"35.063786");
		} else {
			cityname = getIntent().getStringExtra("cityname");
			tv_cityname.setText(cityname);
			String cityid = getIntent().getStringExtra("cityid");
			Toast.makeText(B1_HomeActivity.this, cityid, Toast.LENGTH_LONG).show();
			String lng = getIntent().getStringExtra("lng");
			String lat = getIntent().getStringExtra("lat");
			putSharedPreferenceValue("cityname", cityname);
			putSharedPreferenceValue("lng", lng);
			putSharedPreferenceValue("lat", lat);
			putSharedPreferenceValue("cityid", cityid);
			HttpUtils.getFirstList(res_getSyList, cityid, lng, lat);
		}
	}

	JsonHttpResponseHandler res_getSyList = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("首页=" + response);
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
				try {
					data.clear();
					// 每日好店
					final org.json.JSONArray array = datas
							.getJSONArray("good_store");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);

						Map<String, String> map = new HashMap<String, String>();
						map.put("store_id", jsonItem.getString("store_id"));
						map.put("store_name", jsonItem.getString("store_name"));
						map.put("sc_name", jsonItem.getString("sc_name"));
						map.put("store_evaluate_count",
								jsonItem.getString("store_evaluate_count"));
						map.put("area_info", jsonItem.getString("area_info"));
						map.put("store_address",
								jsonItem.getString("store_address"));
						map.put("store_label",
								jsonItem.getString("store_label"));
						map.put("store_desccredit",
								jsonItem.getString("store_desccredit"));
						map.put("juli", jsonItem.getString("juli"));
						data.add(map);
					}
					B1_a3_MeiRiHaoDianAdapter goodadapter = new B1_a3_MeiRiHaoDianAdapter(
							B1_HomeActivity.this, data);
					list_meirihaodian.setAdapter(goodadapter);
					list_meirihaodian
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View view, int i, long arg3) {
									Intent intent = new Intent();
									String storeid = data.get(i)
											.get("store_id");
									intent.putExtra("store_id", storeid);
									intent.setClass(B1_HomeActivity.this,
											BX_DianPuXiangQingActivity.class);
									startActivity(intent);
								}
							});

					// 猜你喜欢
					data1.clear();
					org.json.JSONArray array1 = datas
							.getJSONArray("goods_like");
					for (int i = 0; i < array1.length(); i++) {
						JSONObject jsonItem1 = array1.getJSONObject(i);
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("goods_jingle",
								jsonItem1.getString("goods_jingle"));
						map1.put("goods_name",
								jsonItem1.getString("goods_name"));
					/*	map1.put("nc_distinct",
								jsonItem1.getString("nc_distinct"))*/;
						map1.put("juli", jsonItem1.getString("juli"));
						map1.put("goods_price",
								jsonItem1.getString("goods_price"));
						map1.put("goods_image",
								jsonItem1.getString("goods_image"));
						map1.put("goods_id", jsonItem1.getString("goods_id"));
						map1.put("goods_salenum",
								jsonItem1.getString("goods_salenum"));
						map1.put("goods_storage",
								jsonItem1.getString("goods_storage"));
						map1.put("store_name",
								jsonItem1.getString("store_name"));
						map1.put("goods_promotion_price",
								jsonItem1.getString("goods_promotion_price"));
						map1.put("is_special",
								jsonItem1.getString("is_special"));
						data1.add(map1);
					}
					B1_a2_CaiNiLikeAdapter cainilikeadapter = new B1_a2_CaiNiLikeAdapter(
							B1_HomeActivity.this, data1);
					list_cainilike.setAdapter(cainilikeadapter);
					list_cainilike
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									Intent intent = new Intent();
									String goid = data1.get(arg2).get(
											"goods_id");
									intent.putExtra("goods_id", goid);
									intent.setClass(B1_HomeActivity.this,
											Sp_GoodsInfoActivity.class);
									startActivity(intent);
								}
							});

					// 天天特价
					org.json.JSONArray arr = datas.getJSONArray("day_special");
					JSONObject jsonIt = arr.getJSONObject(0);
					ImageLoader.getInstance().displayImage(
							jsonIt.getString("goods_image"), im_b1_a1_pic);
					tv_b1_a1_chanpinname
							.setText(jsonIt.getString("goods_name"));
					tv_b1_a1_chanpinjianjie.setText(jsonIt
							.getString("goods_jingle"));
					tv_b1_a1_zhehoujia.setText(jsonIt
							.getString("goods_promotion_price"));
					tv_b1_a1_yuanjia.setText(jsonIt.getString("goods_price"));
					tv_b1_a1_yuanjia.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG);
					tv_goodsid.setText(jsonIt.getString("goods_id").toString());
					tv_dianpuming.setText(jsonIt.getString("store_name")
							.toString());
					tv_kucun.setText(jsonIt.getString("goods_storage")
							.toString());
					tv_xiaoliang.setText(jsonIt.getString("goods_salenum")
							.toString());

					// 晒单圈
					data2.clear();
					org.json.JSONArray array3 = datas.getJSONArray("circle");
					tv_updateNumber.setText(array3.length() + "条新内容更新");
					for (int i = 0; i < array3.length(); i++) {
						JSONObject jsonItem1 = array3.getJSONObject(i);
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("member_name",
								jsonItem1.getString("member_name"));
						map1.put("description",
								jsonItem1.getString("description"));
						map1.put("praise", jsonItem1.getString("praise"));
						map1.put("replys", jsonItem1.getString("replys"));
						map1.put("image", jsonItem1.getString("image"));
						map1.put("avatar", jsonItem1.getString("avatar"));
						data2.add(map1);
					}
					HorizontalListViewAdapter horizontalListViewAdapter = new HorizontalListViewAdapter(
							B1_HomeActivity.this, data2);
					listviewHorizontal.setAdapter(horizontalListViewAdapter);
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}/* circle */
			else// 失败
			{
				Tools.Log("res_Points_error=" + error + "");
				// Tools.Notic(B5_MyActivity.this, error+"", null);
			}

		}

	};

	private void initView() {
		tv_goodsid = (TextView) findViewById(R.id.tv_goodsid);
		im_b1nvshi = (ImageView) findViewById(R.id.im_b1nvshi);
		im_b1nanshi = (ImageView) findViewById(R.id.im_b1nanshi);
		im_b1muying = (ImageView) findViewById(R.id.im_b1muying);
		im_b1huazhuang = (ImageView) findViewById(R.id.im_b1huazhuang);
		im_b1shouji = (ImageView) findViewById(R.id.im_b1shouji);
		im_b1bangong = (ImageView) findViewById(R.id.im_b1bangong);
		im_b1shenghuo = (ImageView) findViewById(R.id.im_b1shenghuo);
		im_b1techan = (ImageView) findViewById(R.id.im_b1techan);
		rl_b1_a1tttj = (RelativeLayout) findViewById(R.id.rl_b1_a1tttj);
		b5_3_shaidanquan = (RelativeLayout) findViewById(R.id.b5_3_shaidanquan);
		rl_b1_a2_cnxh = (RelativeLayout) findViewById(R.id.rl_b1_a2_cnxh);
		rl_b1_a3_mrhd = (RelativeLayout) findViewById(R.id.rl_b1_a3_mrhd);
		rl_sousuokuang = (RelativeLayout) findViewById(R.id.rl_sousuokuang);
		list_meirihaodian = (AutoListView) findViewById(R.id.list_meirihaodian);
		list_cainilike = (AutoListView) findViewById(R.id.list_cainilike);
		im_b1_a1_pic = (ImageView) findViewById(R.id.im_b1_a1_pic);
		tv_b1_a1_chanpinname = (TextView) findViewById(R.id.tv_b1_a1_chanpinname);
		tv_b1_a1_chanpinjianjie = (TextView) findViewById(R.id.tv_b1_a1_chanpinjianjie);
		tv_b1_a1_zhehoujia = (TextView) findViewById(R.id.tv_b1_a1_zhehoujia);
		tv_b1_a1_yuanjia = (TextView) findViewById(R.id.tv_b1_a1_yuanjia);
		tv_updateNumber = (TextView) findViewById(R.id.tv_updateNumber);
		tv_dianpuming = (TextView) findViewById(R.id.tv_dianpuming);
		tv_kucun = (TextView) findViewById(R.id.tv_kucun);
		tv_xiaoliang = (TextView) findViewById(R.id.tv_xiaoliang);
		ll_dayspecial = (RelativeLayout) findViewById(R.id.ll_dayspecial);
		rl_ditu = (RelativeLayout) findViewById(R.id.rl_ditu);
		im_moreinfo = (ImageView) findViewById(R.id.im_moreinfo);
		ll_moreinfolayout = (LinearLayout) findViewById(R.id.ll_moreinfolayout);
		listviewHorizontal = (AutoListView) findViewById(R.id.horizon_listview);
		a1_sousuofujin = (EditText) findViewById(R.id.a1_sousuofujin);
		setListener(im_b1nvshi, im_b1nanshi, im_b1muying, im_b1huazhuang,
				im_b1shouji, im_b1bangong, im_b1shenghuo, im_b1techan,
				rl_b1_a1tttj, b5_3_shaidanquan, rl_b1_a2_cnxh, rl_b1_a3_mrhd,
				rl_sousuokuang, ll_dayspecial, rl_ditu, im_moreinfo,
				ll_moreinfolayout, a1_sousuofujin);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 男装
		case R.id.im_b1nvshi:
			BaDaFenLei("1106");
			break;
		// 女装
		case R.id.im_b1nanshi:
			BaDaFenLei("1107");
			break;
		// 美食
		case R.id.im_b1muying:
			BaDaFenLei("1108");
			break;
		// 娱乐
		case R.id.im_b1huazhuang:
			BaDaFenLei("1109");
			break;
		// 日用百货
		case R.id.im_b1shouji:
			BaDaFenLei("1110");
			break;
		// 数码办公
		case R.id.im_b1bangong:
			BaDaFenLei("1111");
			break;
		// 生活服务
		case R.id.im_b1shenghuo:
			BaDaFenLei("1112");
			break;
		// 其他
		case R.id.im_b1techan:
			BaDaFenLei("1113");
			break;
		// 天天特价
		case R.id.rl_b1_a1tttj:
			Intent ittttj = new Intent();
			ittttj.setClass(B1_HomeActivity.this, B1_a1_TianTianTeJia.class);
			startActivity(ittttj);
			break;
		// 晒单圈
		case R.id.b5_3_shaidanquan:
			Intent itshaidanquan = new Intent();
			itshaidanquan
					.setClass(B1_HomeActivity.this, B5_3_ShaiDanQuan.class);
			startActivity(itshaidanquan);
			break;
		// 猜你喜欢
		case R.id.rl_b1_a2_cnxh:
			Intent itcnxh = new Intent();
			itcnxh.setClass(B1_HomeActivity.this, B1_a2_CaiNiXiHuan.class);
			startActivity(itcnxh);
			break;

		// 每日好店
		case R.id.rl_b1_a3_mrhd:
			Intent itmrhd = new Intent();
			itmrhd.setClass(B1_HomeActivity.this, B1_a3_MeiRiHaoDian.class);
			startActivity(itmrhd);
			break;
		// 宝贝
		// case R.id.tv_baobei:
		// AddPopWindow addPopWindow = new AddPopWindow(B1_HomeActivity.this);
		// addPopWindow.showAtLocation(tv_baobei, Gravity.LEFT | Gravity.TOP,
		// 110,65 );
		// // addPopWindow.showPopupWindow(tv_baobei);
		// break;
		case R.id.rl_sousuokuang:
			Intent itsydps = new Intent();
			itsydps.setClass(B1_HomeActivity.this, B1_a4_SearchActivity.class);
			startActivity(itsydps);
			break;
		case R.id.a1_sousuofujin:
			Intent itsydps1 = new Intent();
			itsydps1.setClass(B1_HomeActivity.this, B1_a4_SearchActivity.class);
			startActivity(itsydps1);
			break;
		// 首页天天特价
		case R.id.ll_dayspecial:
			Intent itdayspec = new Intent();
			String goid = tv_goodsid.getText().toString();
			itdayspec.putExtra("goods_id", goid);
			itdayspec
					.setClass(B1_HomeActivity.this, Sp_GoodsInfoActivity.class);
			startActivity(itdayspec);
			break;
		// 城市选择
		case R.id.rl_ditu:
			Intent itmap = new Intent();
			itmap.setClass(B1_HomeActivity.this, B1_01_MapActivity.class);
			startActivityForResult(itmap, 0);
			break;
		case R.id.im_moreinfo:
			/*ll_moreinfolayout.setVisibility(View.VISIBLE);*/
			break;
		case R.id.ll_moreinfolayout:
			ll_moreinfolayout.setVisibility(View.GONE);
			break;
		case R.id.error_layout:// 错误页面的点击
			// http请求
			break;
		}

	}

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

	/** 类型选择 */
	public void selectType(int type) {
		// Intent intent = new Intent(this, ShopList.class);
		// intent.putExtra("state", 1);
		// intent.putExtra("type", type);
		// startActivity(intent);
		Toast.makeText(this, type, Toast.LENGTH_LONG).show();
	}

	// 八大分类
	public void BaDaFenLei(String gc_id) {
		B1_a4_SearchActivity.CHANNEL = 0;
		Intent intent = new Intent(B1_HomeActivity.this,
				B1_a4_SearchActivity.class);
		intent.putExtra("gc_id", gc_id);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			cityname = data.getStringExtra("cityname");
			tv_cityname.setText(cityname);
			String cityid = data.getStringExtra("cityid");
			String lng = data.getStringExtra("lng");
			String lat = data.getStringExtra("lat");
			putSharedPreferenceValue("cityname", cityname);
			putSharedPreferenceValue("lng", lng);
			putSharedPreferenceValue("lat", lat);
			putSharedPreferenceValue("cityid", cityid);
			HttpUtils.getFirstList(res_getSyList, cityid, lng, lat);
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			
		}
	}

}
