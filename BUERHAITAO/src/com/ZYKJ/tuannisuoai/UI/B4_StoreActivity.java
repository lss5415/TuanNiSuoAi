package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B2_ShopsAdapter;
import com.ZYKJ.tuannisuoai.adapter.B4_TextSizeAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.Shop;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.ToastView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

@SuppressLint("NewApi")
public class B4_StoreActivity extends BaseActivity implements IXListViewListener,OnItemClickListener{
	
	private int page = 5;//每页数量
	private int curpage = 1;//当前页
	private int key = 0;//  1-智能 2-好评 3-离我最近 空-按时间排序  	
	private int order = 1;// 排序方式的状态,1-升序 2-降序
	private String city_id,keyword,sc_id;
	private String lng,lat;

	private LinearLayout ly_a4_category,ly_a4_assess;
    private PopupWindow popupWindow;
	private List<HashMap<String, String>> shopClass;
	private List<HashMap<String, String>> shopClass1;
    private ListView pList;
	private TextView cl_address;
	private RelativeLayout rl_sousuokuang;
	private MyListView a4_storelist;
    
	private List<Shop> shops;
	private B2_ShopsAdapter shopsAdapter;
	private B4_TextSizeAdapter b4tsa;
	private TextView tv_a4_category;//全部分类
	private TextView tv_a4_assess;//智能排序
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b4_storeactivity);
		city_id = getSharedPreferenceValue("cityid");
		lng = getSharedPreferenceValue("lng");
		lat = getSharedPreferenceValue("lat");
		initView();
        initPop();
	}

	/**
	 * 初始化页面
	 */
	private void initView(){
		cl_address = (TextView)findViewById(R.id.classify_address);//地址选择
		rl_sousuokuang = (RelativeLayout)findViewById(R.id.rl_sousuokuang);//搜索框
		a4_storelist = (MyListView)findViewById(R.id.a4_storelist);//店铺列表list
		
		ly_a4_category = (LinearLayout)findViewById(R.id.ly_a4_category);
		ly_a4_assess = (LinearLayout)findViewById(R.id.ly_a4_assess);
		tv_a4_assess = (TextView)findViewById(R.id.tv_a4_assess);
		tv_a4_category = (TextView)findViewById(R.id.tv_a4_category);
		cl_address.setText(getSharedPreferenceValue("cityname"));
		
        a4_storelist.setPullLoadEnable(true);
        a4_storelist.setPullRefreshEnable(true);
        a4_storelist.setXListViewListener(this, 0);
        a4_storelist.setOnItemClickListener(this);
        a4_storelist.setRefreshTime();

		setListener(cl_address,rl_sousuokuang,ly_a4_category,ly_a4_assess);
	}

    /**
     * 管理店铺(分类、排序)
     */
    private void initPop() {
    	HttpUtils.getStoreClass(res_getStoreClass);

        View view=LayoutInflater.from(B4_StoreActivity.this).inflate(R.layout.popu_layout,null);
        pList = (ListView) view.findViewById(R.id.lv_content);
        if(popupWindow == null){
            popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            ColorDrawable cd = new ColorDrawable(-0000);
            popupWindow.setBackgroundDrawable(cd);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }
		requestData();
    }

	/**
	 * 请求服务器数据----店铺列表
	 */
	private void requestData(){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("key", String.valueOf(key));//排序方式 1-销量 2-评价 3-价格 空-按距离排序
		params.put("order", String.valueOf(order));//1-升序 2-降序
		params.put("page", String.valueOf(page));//每页数量
		params.put("curpage", String.valueOf(curpage));//当前页码
		params.put("sc_id", sc_id == null?"":sc_id);//店铺编号
		params.put("city_id", city_id);//城市id
		params.put("lng", lng);//经度
		params.put("lat", lat);//纬度
		params.put("keyword", keyword == null?"":keyword);//搜索关键字
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getStoreList(curpage == 1?res_getStoresList:res_getMoreStoreList, HttpUtils.iterateParams(params));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_sousuokuang:
			B1_a4_SearchActivity.CHANNEL=1;
			startActivity(new Intent().setClass(B4_StoreActivity.this, B1_a4_SearchActivity.class));
			break;
		case R.id.ly_a4_category:
            //全部分类
			b4tsa = new B4_TextSizeAdapter(B4_StoreActivity.this, shopClass);
			pList.setAdapter(b4tsa);
            pList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
					order = 1;curpage = 1;keyword = "";
	                if(popupWindow.isShowing()){
	                    popupWindow.dismiss();
	                }
	                sc_id = shopClass.get(position).get("sc_id");
	                tv_a4_category.setText(shopClass.get(position).get("sc_name").toString());
	    			requestData();
				}
			});
            popupWindow.showAsDropDown(v);
			break;
		case R.id.ly_a4_assess:
            //智能排序
			shopClass1 = new ArrayList<HashMap<String,String>>();
			for(int i = 0; i < 3; i++){
				HashMap<String, String> map = new HashMap<String, String>();
				if (i==0) {
					map.put("sc_name", "智能排序");
				}else if (i==1) {
					map.put("sc_name", "好评优先");
				}else{
					map.put("sc_name", "离我最近");
				}
				shopClass1.add(map);
			}
			b4tsa = new B4_TextSizeAdapter(B4_StoreActivity.this, shopClass1);
			pList.setAdapter(b4tsa);
            pList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					key = position+1;curpage = 1;keyword = "";
					if (position==0) {
						order = 1;
					}if (position==1) {
						order = 2;
					}if (position==2) {
						order = 1;
					}
	                if(popupWindow.isShowing()){
	                    popupWindow.dismiss();
	                }
	                tv_a4_assess.setText(shopClass1.get(position).get("sc_name"));
	    			requestData();
				}
			});
            popupWindow.showAsDropDown(v);
			break;
		}
	}

	//店铺刷新
	AsyncHttpResponseHandler res_getStoresList = new EntityHttpResponseHandler<Shop>(Shop.class){
		@Override
		public void onReadSuccess(List<Shop> list) {
			shops = list;
			shopsAdapter = new B2_ShopsAdapter(B4_StoreActivity.this, shops);
			a4_storelist.setAdapter(shopsAdapter);
		}
	};
	//店铺加载
	AsyncHttpResponseHandler res_getMoreStoreList = new EntityHttpResponseHandler<Shop>(Shop.class){
		@Override
		public void onReadSuccess(List<Shop> list) {
			shops.addAll(list);
			shopsAdapter.notifyDataSetChanged();
		}
	};
	//店铺分类
	AsyncHttpResponseHandler res_getStoreClass = new JsonHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
			try {
				org.json.JSONArray jsonArray = response.getJSONObject("datas").getJSONArray("class_list");
				shopClass = new ArrayList<HashMap<String,String>>();
				for(int i = 0; i < jsonArray.length(); i++){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("sc_id", ((org.json.JSONObject)jsonArray.get(i)).getString("sc_id"));
					map.put("sc_name", ((org.json.JSONObject)jsonArray.get(i)).getString("sc_name"));
					shopClass.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, org.json.JSONObject errorResponse) {
			RequestDailog.closeDialog();
			new ToastView(B4_StoreActivity.this, "获取失败");
		}
	};
	
	abstract class EntityHttpResponseHandler<T> extends AsyncHttpResponseHandler{
		
		private Class<T> clzz;
		
		public EntityHttpResponseHandler(Class<T> clzz){this.clzz = clzz;}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
			RequestDailog.closeDialog();
			JSONObject response = (JSONObject)JSON.parse(responseString);
			JSONObject datas = response.getJSONObject("datas");
			try {
				JSONArray jsonArray = datas.getJSONArray("goods_list");
				List<T> list = JSONArray.parseArray(jsonArray.toString(), clzz);
				onReadSuccess(list);	
			} catch (Exception e) {
				onReadSuccess(new ArrayList<T>());
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {
			RequestDailog.closeDialog();
			new ToastView(B4_StoreActivity.this, "加载失败");
		}
		
	    public abstract void  onReadSuccess(List<T> list);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		/*店铺、宝贝详情*/
		Shop shop = shops.get(position-1);
//		Toast.makeText(B4_StoreActivity.this, shop.getSc_name(), Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		String storeid = shops.get(position-1).getStore_id();
		intent.putExtra("store_id", storeid);
		intent.setClass(B4_StoreActivity.this,BX_DianPuXiangQingActivity.class);
		startActivity(intent);
	}

	@Override
	public void onRefresh(int id) {
		/*下拉加载*/
		curpage = 1;
		requestData();
	}

	@Override
	public void onLoadMore(int id) {
		/*上拉刷新*/
		curpage += 1;
		requestData();
	}
}
