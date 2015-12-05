package com.zykj.tuannisuoai_seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.tuannisuoai_seller.adapter.B5_10_MyCollectionAdapter;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.MyListView;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.ToastView;
import com.zykj.tuannisuoai_seller.utils.Tools;

/**
 * 产品页面
 * 
 * @author zyk
 * 
 */
public class B2_ProductActivity extends BaseActivity implements
		IXListViewListener, B5_10_MyCollectionAdapter.GetMap_id{

	TextView tv_edit;// 编辑
	LinearLayout ll_product, ll_store;// 产品，商铺
	CheckBox cb_choseAll;// 全选
	LinearLayout ll_operate;// 点击编辑之后的操作，包括“下架商品”和“删除”
	Button btn_xiajia, btn_delete;
	// RelativeLayout rl_delete;//删除按钮
	View v1, v2;
	MyListView listview;
	B5_10_MyCollectionAdapter adapter;
	B5_10_MyCollectionAdapter adapter_store;
	Map<String, String> map_commonid = new HashMap<String, String>();
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	List<Map<String, String>> data_store = new ArrayList<Map<String, String>>();
	Boolean isEdit = false;
	String key;
	String tagProduct, tagStore;
	int page = 10;// 每页数量
	int curpage = 1;// 当前页码
	TextView tv_chushouzhong, tv_yixiajia;

	public static String commonid = "";// 商品公共编号(多个逗号分隔)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_5_10_mycolection);
		key = getSharedPreferenceValue("key");
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		ll_product = (LinearLayout) findViewById(R.id.ll_selling);
		ll_store = (LinearLayout) findViewById(R.id.ll_outlet);
		cb_choseAll = (CheckBox) findViewById(R.id.cb_choseAll);
		ll_operate = (LinearLayout) findViewById(R.id.ll_operate);
		btn_xiajia = (Button) findViewById(R.id.btn_xiajia);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		tv_chushouzhong = (TextView) findViewById(R.id.tv_chushouzhong);
		tv_yixiajia = (TextView) findViewById(R.id.tv_yixiajia);
		setListener(tv_edit, ll_product, ll_store, btn_xiajia, btn_delete);
		v1 = findViewById(R.id.v1_s);
		v2 = findViewById(R.id.v2_s);
		listview = (MyListView) findViewById(R.id.listview_colllection);
		adapter = new B5_10_MyCollectionAdapter(B2_ProductActivity.this, data,
				isEdit, key, this);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String gonggonggoodid = data.get(arg2-1).get("goods_commonid");
//				Toast.makeText(B2_ProductActivity.this, gonggonggoodid, Toast.LENGTH_LONG).show();
//				HttpUtils.getFavoriteProduct(res_getFavoriteProduct,getSharedPreferenceValue("key"), gonggonggoodid, curpage + "");
				Intent itdayspec = new Intent();
				itdayspec.putExtra("goods_commonid", gonggonggoodid);
				itdayspec.setClass(B2_ProductActivity.this, B3_Add1Activity.class);
				startActivity(itdayspec);
			}
		});
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getFavoriteProduct(res_getFavoriteProduct,
				getSharedPreferenceValue("key"), page + "", curpage + "");
		tagProduct = "1";
		tagStore = "0";
		// 全选按钮
		cb_choseAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) // 选中
				{
					for (int i = 0; i < B5_10_MyCollectionAdapter.data.size(); i++) {
						B5_10_MyCollectionAdapter.b_goods[i] = isChecked;
						B5_10_MyCollectionAdapter.map_commonid.put(
								i + "",
								B5_10_MyCollectionAdapter.data.get(i).get(
										"goods_commonid"));
						// commonid += B5_10_MyCollectionAdapter.data.get(i)
						// .get("goods_commonid") + ",";
					}
					adapter.notifyDataSetChanged();
				} else // 未选中
				{
					for (int i = 0; i < B5_10_MyCollectionAdapter.data.size(); i++) {
						B5_10_MyCollectionAdapter.b_goods[i] = false;
					}
					B5_10_MyCollectionAdapter.map_commonid.clear();// 未全选的时候清空数据
					adapter.notifyDataSetChanged();
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_edit:// 编辑按钮
			if (isEdit == true) {
				tv_edit.setText("编辑");
				isEdit = false;
				cb_choseAll.setVisibility(View.GONE);
				ll_operate.setVisibility(View.GONE);
				// rl_delete.setVisibility(View.GONE);
			} else {
				isEdit = true;
				tv_edit.setText("完成");
				// rl_delete.setVisibility(View.VISIBLE);
				// rl_delete.bringToFront();//显示在最上层
				cb_choseAll.setVisibility(View.VISIBLE);
				ll_operate.setVisibility(View.VISIBLE);
			}
			if (tagStore == "1") // 如果点击了已下架,第一个按钮变成“上架商品”按钮
			{
				btn_xiajia.setText("上架商品");
			}

			// if (tagProduct.equals("1"))
			// {
			adapter = new B5_10_MyCollectionAdapter(B2_ProductActivity.this,
					data, isEdit, key, this);
			listview.setAdapter(adapter);
			// }
			// else if (tagStore.equals("1")) {
			// adapter_store = new
			// B5_10_MyCollectionAdapter(B2_ProductActivity.this,data_store,isEdit,key,this);
			// listview.setAdapter(adapter_store);
			// }
			break;
		case R.id.ll_selling:// 出售中
			btn_xiajia.setText("下架商品");
			tagProduct = "1";
			tagStore = "0";
			tv_chushouzhong.setTextColor(Color.parseColor("#FF5500"));
			tv_yixiajia.setTextColor(Color.parseColor("#000000"));
			v2.setVisibility(View.GONE);
			v1.setVisibility(View.VISIBLE);
			listview = (MyListView) findViewById(R.id.listview_colllection);
			adapter = new B5_10_MyCollectionAdapter(B2_ProductActivity.this,
					data, isEdit, key, this);
			listview.setAdapter(adapter);
			// RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getFavoriteProduct(res_getFavoriteProduct,
					getSharedPreferenceValue("key"), page + "", curpage + "");
			break;
		case R.id.ll_outlet:// 已下架
			btn_xiajia.setText("上架商品");
			tagProduct = "0";
			tagStore = "1";
			tv_chushouzhong.setTextColor(Color.parseColor("#000000"));
			tv_yixiajia.setTextColor(Color.parseColor("#FF5500"));
			v2.setVisibility(View.VISIBLE);
			v1.setVisibility(View.GONE);
			// RequestDailog.showDialog(this, "正在加载数据，请稍后");
			// listview = (MyListView) findViewById(R.id.listview_colllection);
			// adapter_store = new
			// B5_10_MyCollectionAdapter(B2_ProductActivity.this,data_store,isEdit,key,this);
			// listview.setAdapter(adapter_store);
			listview = (MyListView) findViewById(R.id.listview_colllection);
			adapter = new B5_10_MyCollectionAdapter(B2_ProductActivity.this,
					data, isEdit, key, this);
			listview.setAdapter(adapter);
			HttpUtils.getFavoriteStore(res_getFavoriteStore,
					getSharedPreferenceValue("key"), page + "", curpage + "");
			break;
		case R.id.btn_xiajia:// 点击下架or上架
			RequestDailog.showDialog(this, "正在处理，请稍后");
			String commonid_serilize = null;
			// 遍历取出所有的commonid
			for (Map.Entry<String, String> entry : map_commonid.entrySet()) {
				commonid_serilize += entry.getValue() + ",";
			}
			commonid_serilize = commonid_serilize.substring(4);
			commonid_serilize = commonid_serilize.substring(0,
					commonid_serilize.length() - 1);
			if (tagProduct == "1") // 出售中的商品请求下架商品接口
			{
				HttpUtils.goods_unshow(res_goods_unshow,
						getSharedPreferenceValue("key"),
						commonid_serilize.trim());
			} else // 已下架的商品请求上架商品接口
			{
				HttpUtils.goods_show(res_goods_unshow,
						getSharedPreferenceValue("key"),
						commonid_serilize.trim());
			}
			break;

		case R.id.btn_delete:// 点击删除
			RequestDailog.showDialog(this, "正在处理，请稍后");
			String commonid_serilize1 = null;
			// 遍历取出所有的commonid
			for (Map.Entry<String, String> entry : map_commonid.entrySet()) {
				commonid_serilize1 += entry.getValue() + ",";
			}
			commonid_serilize1 = commonid_serilize1.substring(4);
			commonid_serilize1 = commonid_serilize1.substring(0,
					commonid_serilize1.length() - 1);
			HttpUtils.goods_delete(res_goods_unshow,
					getSharedPreferenceValue("key"), commonid_serilize1.trim());
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		if (tagProduct.equals("1")) // 获取出售中的产品信息
		{
			curpage = 1;
			HttpUtils.getFavoriteProduct(res_getFavoriteProduct,
					getSharedPreferenceValue("key"), page + "", curpage + "");
		} else if (tagStore.equals("1")) {// 获取已下架的产品信息
			HttpUtils.getFavoriteStore(res_getFavoriteStore,
					getSharedPreferenceValue("key"), page + "", curpage + "");
		}
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		curpage++;
		if (tagProduct.equals("1")) // 获取出售中的产品信息
		{
			HttpUtils.getFavoriteProduct(res_getFavoriteProduct,
					getSharedPreferenceValue("key"), page + "", curpage + "");
		} else if (tagStore.equals("1")) {// 获取已下架的产品信息
			HttpUtils.getFavoriteStore(res_getFavoriteStore,
					getSharedPreferenceValue("key"), page + "", curpage + "");
		}

	}

	/**
	 * 商品下架
	 */
	JsonHttpResponseHandler res_goods_unshow = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("下架商品=" + response);
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				Tools.Notic(B2_ProductActivity.this, "操作成功", null);
				adapter.notifyDataSetChanged();
				// adapter_store.notifyDataSetChanged();
			} else// 失败
			{
				// Tools.Log("res_Points_error="+error+"");
				Tools.Notic(B2_ProductActivity.this, error + "", null);
			}

		}

	};
	/**
	 * 出售中
	 */
	JsonHttpResponseHandler res_getFavoriteProduct = new JsonHttpResponseHandler() {

		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("出售中产品=" + response);
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				try {
					if (curpage==1) {
						data.clear();
					}
					org.json.JSONArray array = datas.getJSONArray("goods_list");// 等收藏功能完善之后更改array的名字
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("tag", "Product");
						map.put("goods_name", jsonItem.getString("goods_name"));
						map.put("goods_jingle",
								jsonItem.getString("goods_jingle"));
						map.put("goods_price",
								jsonItem.getString("goods_price"));
						map.put("goods_storage",
								jsonItem.getString("goods_storage"));
						map.put("goods_image",
								jsonItem.getString("goods_image"));
						map.put("goods_salenum",
								jsonItem.getString("goods_salenum"));// 总销量
						map.put("goods_commonid",
								jsonItem.getString("goods_commonid"));
						data.add(map);
					}
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
	 * 已下架
	 */
	JsonHttpResponseHandler res_getFavoriteStore = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("已下架=" + response);
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				try {
					data.clear();
					org.json.JSONArray array = datas.getJSONArray("goods_list");// 等收藏功能完善之后更改array的名字
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("tag", "Product");
						map.put("goods_name", jsonItem.getString("goods_name"));
						map.put("goods_jingle",
								jsonItem.getString("goods_jingle"));
						map.put("goods_price",
								jsonItem.getString("goods_price"));
						map.put("goods_storage",
								jsonItem.getString("goods_storage"));
						map.put("goods_image",
								jsonItem.getString("goods_image"));
						map.put("goods_salenum",
								jsonItem.getString("goods_salenum"));// 总销量
						map.put("goods_commonid",
								jsonItem.getString("goods_commonid"));
						data.add(map);
					}
					adapter.notifyDataSetChanged();
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// try {
				// data_store.clear();
				// org.json.JSONArray array =
				// datas.getJSONArray("favorites_list");//等收藏功能完善之后更改array的名字
				// for (int i = 0; i < array.length(); i++) {
				// JSONObject jsonItem = array.getJSONObject(i);
				// Map<String, String> map = new HashMap();
				// map.put("tag", "Store");
				// map.put("goods_name", jsonItem.getString("goods_name"));
				// map.put("goods_jingle", jsonItem.getString("goods_jingle"));
				// map.put("goods_price", jsonItem.getString("goods_price"));
				// map.put("goods_storage",
				// jsonItem.getString("goods_storage"));
				// map.put("goods_image", jsonItem.getString("goods_image"));
				// map.put("goods_salenum",
				// jsonItem.getString("goods_salenum"));//总销量
				// map.put("goods_commonid",
				// jsonItem.getString("goods_commonid"));
				// data_store.add(map);
				// }
				// adapter_store.notifyDataSetChanged();
				// }
				// catch (org.json.JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			} else// 失败
			{
				Tools.Log("res_Points_error=" + error + "");
				// Tools.Notic(B5_MyActivity.this, error+"", null);
			}

		}

	};

	@Override
	public Map<String, String> getMap(Map<String, String> map_commonid) {
		// TODO Auto-generated method stub
		this.map_commonid = map_commonid;
		return null;
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

}
