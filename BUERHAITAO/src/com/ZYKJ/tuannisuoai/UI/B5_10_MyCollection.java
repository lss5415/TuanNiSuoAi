package com.ZYKJ.tuannisuoai.UI;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_10_MyCollectionAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 我的收藏 页面
 * @author zyk
 *
 */
public class B5_10_MyCollection extends BaseActivity implements IXListViewListener {

	ImageButton mycollection_back;//返回
	TextView tv_edit;//编辑
	LinearLayout ll_product,ll_store;//产品，商铺
//	RelativeLayout rl_delete;//删除按钮
	View v1,v2;
	MyListView listview;
	B5_10_MyCollectionAdapter adapter;
	B5_10_MyCollectionAdapter adapter_store;
	List<Map<String, String>> data = new ArrayList<Map<String,String>>();
	List<Map<String, String>> data_store = new ArrayList<Map<String,String>>();
	Boolean  isEdit = false;
	String key;
	String tagProduct,tagStore;
	int curpage=1,curpage1=1;
	private Handler mHandler = new Handler();//异步加载或刷新
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_5_10_mycolection);
		key = getSharedPreferenceValue("key");
		mycollection_back = (ImageButton) findViewById(R.id.mycollection_back);
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		ll_product = (LinearLayout) findViewById(R.id.ll_product);
		ll_store = (LinearLayout) findViewById(R.id.ll_store);
//		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
		setListener(mycollection_back,tv_edit,ll_product,ll_store);
		v1=findViewById(R.id.v1_s);
		v2=findViewById(R.id.v2_s);
		listview = (MyListView) findViewById(R.id.listview_colllection);
		adapter = new B5_10_MyCollectionAdapter(B5_10_MyCollection.this,data,isEdit,key);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getFavoriteProduct(res_getFavoriteProduct, getSharedPreferenceValue("key"),String.valueOf(curpage));
		tagProduct = "1";
		tagStore   = "0";
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mycollection_back://返回
			this.finish();
			break;
		case R.id.tv_edit://编辑按钮
			if (isEdit == true) {
				tv_edit.setText("编辑");
				isEdit = false;
//				rl_delete.setVisibility(View.GONE);
			}else {
				isEdit = true;
				tv_edit.setText("完成");
//				rl_delete.setVisibility(View.VISIBLE);
//				rl_delete.bringToFront();//显示在最上层
			}
			if (tagProduct.equals("1")) 
			{
				adapter = new B5_10_MyCollectionAdapter(B5_10_MyCollection.this,data,isEdit,key);
				listview.setAdapter(adapter);
			}
			else if (tagStore.equals("1")) {
				adapter_store = new B5_10_MyCollectionAdapter(B5_10_MyCollection.this,data_store,isEdit,key);
				listview.setAdapter(adapter_store);
			}
			break;
		case R.id.ll_product://产品
			tagProduct = "1";
			tagStore   = "0";
			v2.setVisibility(View.INVISIBLE);
			v1.setVisibility(View.VISIBLE);
			listview = (MyListView) findViewById(R.id.listview_colllection);
			adapter = new B5_10_MyCollectionAdapter(B5_10_MyCollection.this,data,isEdit,key);
			listview.setAdapter(adapter);
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getFavoriteProduct(res_getFavoriteProduct, getSharedPreferenceValue("key"),"1");
			
			break;
		case R.id.ll_store://商铺
			tagProduct = "0";
			tagStore   = "1";
			v2.setVisibility(View.VISIBLE);
			v1.setVisibility(View.INVISIBLE);
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			listview = (MyListView) findViewById(R.id.listview_colllection);
			adapter_store = new B5_10_MyCollectionAdapter(B5_10_MyCollection.this,data_store,isEdit,key);
			listview.setAdapter(adapter_store);
			HttpUtils.getFavoriteStore(res_getFavoriteStore, getSharedPreferenceValue("key"),"1");
			break;
//		case R.id.rl_delete://删除
//			String idStrings = "";
//			if (v1.getVisibility()==View.INVISIBLE)//删除产品 
//			{
//				idStrings = adapter.getidStrings()[0];
//			}
//			else 								   //删除店铺
//			{								   
//				idStrings = adapter.getidStrings()[0];
//			}
//			Log.e("idStrings", idStrings+"");
////			RequestDailog.showDialog(this, "正在加载数据，请稍后");
////			HttpUtils.getFavoriteStore(res_getFavoriteStore, getSharedPreferenceValue("key"));
//			break;

		default:
			break;
		}
		super.onClick(v);
	}
	@Override
	public void onRefresh(int id) {
		/**下拉刷新 重建*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (tagProduct.equals("1")) 
				{
					curpage = 1;
					HttpUtils.getFavoriteProduct(res_getFavoriteProduct, getSharedPreferenceValue("key"),String.valueOf(curpage));
					onLoad();
				}
				else if (tagStore.equals("1")) {
					curpage1 = 1;
					HttpUtils.getFavoriteStore(res_getFavoriteStore, getSharedPreferenceValue("key"),String.valueOf(curpage1));
					onLoad();
				}
			}
		}, 1000);

	}
	@Override
	public void onLoadMore(int id) {
		/**上拉加载分页*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (tagProduct.equals("1")) 
				{
					curpage += 1;
					HttpUtils.getFavoriteProduct(res_getFavoriteProduct, getSharedPreferenceValue("key"),String.valueOf(curpage));
					onLoad();
				}
				else if (tagStore.equals("1")) {
					curpage1 += 1;
					HttpUtils.getFavoriteStore(res_getFavoriteStore, getSharedPreferenceValue("key"),String.valueOf(curpage1));
					onLoad();
				}
			}
		}, 1000);		
	}

	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime();
	}
	
	/**
	 * 我收藏的产品
	 */
	JsonHttpResponseHandler res_getFavoriteProduct = new JsonHttpResponseHandler()
	{

		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("收藏产品="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				try {
					if (curpage==1) {
						data.clear();
					}
					org.json.JSONArray array = datas.getJSONArray("favorites_list");//等收藏功能完善之后更改array的名字
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("tag","Product");
						map.put("goods_name", jsonItem.getString("goods_name"));
						map.put("goods_image_url", jsonItem.getString("goods_image_url"));
						map.put("goods_id", jsonItem.getString("goods_id"));
						map.put("goods_price", jsonItem.getString("goods_price"));
						map.put("fav_time", jsonItem.getString("fav_time"));
						map.put("fav_id", jsonItem.getString("fav_id"));
						map.put("store_id", jsonItem.getString("store_id"));
						data.add(map);
					}
					adapter.notifyDataSetChanged();
				} 
				catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
		
		
	};
	/**
	 * 我收藏的店铺
	 */
	JsonHttpResponseHandler res_getFavoriteStore = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("店铺response="+response);
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				try {
					if (curpage1==1) {
						data_store.clear();
					}
					org.json.JSONArray array = datas.getJSONArray("favorites_list");//等收藏功能完善之后更改array的名字
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("tag", "Store");
						map.put("store_id", jsonItem.getString("store_id"));
						map.put("store_name", jsonItem.getString("store_name"));
						map.put("fav_time_text", jsonItem.getString("fav_time_text"));
						map.put("store_avatar_url", jsonItem.getString("store_avatar_url"));
						data_store.add(map);
					}
					adapter_store.notifyDataSetChanged();
				} 
				catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
		
		
	};
	
}
