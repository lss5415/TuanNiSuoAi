package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B2_GoodsAdapter;
import com.ZYKJ.tuannisuoai.adapter.B2_ShopsAdapter;
import com.ZYKJ.tuannisuoai.adapter.B4_TextSizeAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.Goods;
import com.ZYKJ.tuannisuoai.data.Shop;
import com.ZYKJ.tuannisuoai.popupwindow.AddPopWindow;
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

/**
 * @author lss 2015年6月18日 搜索页
 *
 */
@SuppressLint("NewApi")
public class B1_a4_SearchActivity extends BaseActivity implements IXListViewListener,OnItemClickListener,TextWatcher,OnEditorActionListener{
	//返回
	ImageButton a4_back;
	//宝贝/店铺
	TextView tv_a4_baobei;
	//默认，销量，价格，好评
	TextView tv_a4_moren,tv_a4_xiaoliang,tv_a4_jiage,tv_a4_haoping,tv_a4_category,tv_a4_assess;
	LinearLayout ly_a4_xiaoliang,ly_a4_jiage,ly_a4_category,ly_a4_assess,ll_tabs,dp_tabs;
	//销量，价格箭头
	ImageView im_a4_xiaoliangstate,im_a4_jiagestate,im_a4_category,im_a4_assess;
	private List<HashMap<String, String>> shopClass1;
	private EditText a4_sydpss;
	//产品列表
	private MyListView a4_seachlist;
	private B4_TextSizeAdapter b4tsa;
	
	public static int CHANNEL = 0;
	private int key = 0;// 按默认排序的状态，0为默认排序，1为销量排序,2为价格排序,3为好评排序
	private int order = 1;// 排序方式的状态,0为降序,1为升序
    private PopupWindow popupWindow;
    private ListView pList;
	
	private Integer page=5,curpage=1;
	private String city_id,keyword,id;
	private String lng,lat;
	private List<Goods> goods;
	private List<Shop> shops;
	private List<HashMap<String, String>> shopClass;
	private B2_GoodsAdapter goodsAdapter;
	private B2_ShopsAdapter shopsAdapter;
	private ImageView im_search;

	/**
	 * 初始化Activity
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_a4_search);
		city_id = getSharedPreferenceValue("cityid");
		lng = getSharedPreferenceValue("lng");
		lat = getSharedPreferenceValue("lat");
		id = getIntent().getStringExtra(CHANNEL == 0?"gc_id":"sc_id");//店铺、宝贝编号
		
		initView();
        initPop();
	}
	
	/**
	 * 加载页面
	 */
	private void initView(){
		a4_back = (ImageButton)findViewById(R.id.a4_back);
		tv_a4_baobei = (TextView)findViewById(R.id.tv_a4_baobei);
		//商品排序
		ll_tabs = (LinearLayout)findViewById(R.id.ll_tabs);
		tv_a4_moren = (TextView)findViewById(R.id.tv_a4_moren);
		tv_a4_xiaoliang = (TextView)findViewById(R.id.tv_a4_xiaoliang);
		tv_a4_jiage = (TextView)findViewById(R.id.tv_a4_jiage);
		tv_a4_haoping = (TextView)findViewById(R.id.tv_a4_haoping);
		im_a4_xiaoliangstate = (ImageView)findViewById(R.id.im_a4_xiaoliangstate);
		im_a4_jiagestate = (ImageView)findViewById(R.id.im_a4_jiagestate);
		ly_a4_xiaoliang = (LinearLayout)findViewById(R.id.ly_a4_xiaoliang);
		ly_a4_jiage = (LinearLayout)findViewById(R.id.ly_a4_jiage);
		a4_seachlist = (MyListView)findViewById(R.id.a4_seachlist);
		//店铺排序
		dp_tabs = (LinearLayout)findViewById(R.id.dp_tabs);
		tv_a4_category = (TextView)findViewById(R.id.tv_a4_category);
		tv_a4_assess = (TextView)findViewById(R.id.tv_a4_assess);
		im_a4_category = (ImageView)findViewById(R.id.im_a4_category);
		im_a4_assess = (ImageView)findViewById(R.id.im_a4_assess);
		ly_a4_category = (LinearLayout)findViewById(R.id.ly_a4_category);
		ly_a4_assess = (LinearLayout)findViewById(R.id.ly_a4_assess);
		im_search = (ImageView)findViewById(R.id.im_search);
		a4_sydpss = (EditText)findViewById(R.id.a4_sydpss);

		a4_seachlist.setPullLoadEnable(true);
		a4_seachlist.setPullRefreshEnable(true);
		a4_seachlist.setXListViewListener(this, 0);
		a4_seachlist.setOnItemClickListener(this);
		a4_seachlist.setRefreshTime();
		tv_a4_baobei.setText(CHANNEL == 0?"宝贝":"店铺");
		tv_a4_baobei.addTextChangedListener(this);
		setListener(a4_back,tv_a4_baobei,tv_a4_moren,tv_a4_haoping,ly_a4_xiaoliang,ly_a4_jiage,ly_a4_category,ly_a4_assess,im_search);
		changeModule();
	}

    /**
     * 管理店铺(分类、排序)
     */
    private void initPop() {
    	HttpUtils.getStoreClass(res_getStoreClass);

        View view=LayoutInflater.from(B1_a4_SearchActivity.this).inflate(R.layout.popu_layout,null);
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
	 * 店铺、宝贝切换筛选导航
	 */
	private void changeModule(){
		if(CHANNEL == 0){
			dp_tabs.setVisibility(View.GONE);
			ll_tabs.setVisibility(View.VISIBLE);
		}else{
			ll_tabs.setVisibility(View.GONE);
			dp_tabs.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 请求数据
	 */
	private void requestData(){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("key", String.valueOf(key));//排序方式 1-销量 2-评价 3-价格 空-按距离排序
		params.put("order", String.valueOf(order));//1-升序 2-降序
		params.put("page", String.valueOf(page));//每页数量
		params.put("curpage", String.valueOf(curpage));//当前页码
		params.put(CHANNEL == 0?"gc_id":"sc_id", id == null?"":id);//店铺、宝贝编号
		params.put("city_id", city_id);//城市id
		params.put("lng", lng);//经度
		params.put("lat", lat);//纬度
		params.put("keyword", keyword == null?"":keyword);//搜索关键字
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		if(CHANNEL == 0){
			HttpUtils.getGoodsList(curpage == 1?res_getGoodsList:res_getMoreGoodsList, HttpUtils.iterateParams(params));
		}else{
			HttpUtils.getStoreList(curpage == 1?res_getStoreList:res_getMoreStoreList, HttpUtils.iterateParams(params));
		}
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
	//商品刷新
	AsyncHttpResponseHandler res_getGoodsList = new EntityHttpResponseHandler<Goods>(Goods.class){
		@Override
		public void onReadSuccess(List<Goods> list) {
			goods = list;
			goodsAdapter = new B2_GoodsAdapter(B1_a4_SearchActivity.this, goods);
			a4_seachlist.setAdapter(goodsAdapter);
		}
	};
	//商品加载
	AsyncHttpResponseHandler res_getMoreGoodsList = new EntityHttpResponseHandler<Goods>(Goods.class){
		@Override
		public void onReadSuccess(List<Goods> list) {
			goods.addAll(list);
			goodsAdapter.notifyDataSetChanged();
		}
	};
	//店铺刷新
	AsyncHttpResponseHandler res_getStoreList = new EntityHttpResponseHandler<Shop>(Shop.class){
		@Override
		public void onReadSuccess(List<Shop> list) {
			shops = list;
			shopsAdapter = new B2_ShopsAdapter(B1_a4_SearchActivity.this, shops);
			a4_seachlist.setAdapter(shopsAdapter);
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
			new ToastView(B1_a4_SearchActivity.this, "获取失败");
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
			new ToastView(B1_a4_SearchActivity.this, "加载失败");
		}
		
	    public abstract void  onReadSuccess(List<T> list);
	}

	@Override
	public void onItemClick(AdapterView<?> groupView, View currentView, int position, long id) {
		/*店铺、宝贝详情*/
		if(CHANNEL == 0){
			Goods good = goods.get(position-1);
//			Toast.makeText(B1_a4_SearchActivity.this, good.getGoods_id(), Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.putExtra("goods_id", good.getGoods_id());
			intent.setClass(B1_a4_SearchActivity.this, Sp_GoodsInfoActivity.class);
			startActivity(intent);
		}else{
			Shop shop = shops.get(position-1);
//			Toast.makeText(B1_a4_SearchActivity.this, shop.getStore_id(), Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.putExtra("store_id", shop.getStore_id());
			intent.setClass(
					B1_a4_SearchActivity.this,
					BX_DianPuXiangQingActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.a4_back:
			B1_a4_SearchActivity.this.finish();
			break;
		case R.id.tv_a4_baobei:
			AddPopWindow addPopWindow = new AddPopWindow(B1_a4_SearchActivity.this,tv_a4_baobei);
			addPopWindow.showAtLocation(tv_a4_baobei, Gravity.LEFT | Gravity.TOP, 110,65 );
//			addPopWindow.showPopupWindow(tv_baobei);
			break;
		case R.id.tv_a4_moren:
			//默认
			if (key != 0) {
				tv_a4_moren.setTextColor(Color.parseColor("#FF5500"));
				tv_a4_xiaoliang.setTextColor(Color.parseColor("#808080"));
				tv_a4_jiage.setTextColor(Color.parseColor("#808080"));
				tv_a4_haoping.setTextColor(Color.parseColor("#808080"));
				im_a4_xiaoliangstate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchmoren));
				im_a4_jiagestate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchmoren));
				
				key = 0;order = 1;
			}else{
				key = 0;order = 1;
			}
			curpage = 1;
			requestData();
			break;
		case R.id.ly_a4_xiaoliang:
			//销量
			tv_a4_moren.setTextColor(Color.parseColor("#808080"));
			tv_a4_xiaoliang.setTextColor(Color.parseColor("#FF5500"));
			tv_a4_jiage.setTextColor(Color.parseColor("#808080"));
			tv_a4_haoping.setTextColor(Color.parseColor("#808080"));
			im_a4_jiagestate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchmoren));
			if (key != 1 || order != 2) {//销量最多
				im_a4_xiaoliangstate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searcjshang));
				key = 1;order = 2;
			}else{//销量最少
				im_a4_xiaoliangstate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchxia));
				key = 1;order = 1;
			}
			curpage = 1;
			requestData();
			break;
		case R.id.ly_a4_jiage:
			//价格
			tv_a4_moren.setTextColor(Color.parseColor("#808080"));
			tv_a4_xiaoliang.setTextColor(Color.parseColor("#808080"));
			tv_a4_jiage.setTextColor(Color.parseColor("#FF5500"));
			tv_a4_haoping.setTextColor(Color.parseColor("#808080"));
			im_a4_xiaoliangstate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchmoren));
			if (key != 3 || order != 2) {//价格最多
				im_a4_jiagestate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searcjshang));
				key = 3;order = 2;
			}else{//价格最少
				im_a4_jiagestate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchxia));
				key = 3;order = 1;
			}
			curpage = 1;
			requestData();
			break;
		case R.id.tv_a4_haoping:
			//好评
			if (key != 2) {
				tv_a4_moren.setTextColor(Color.parseColor("#808080"));
				tv_a4_xiaoliang.setTextColor(Color.parseColor("#808080"));
				tv_a4_jiage.setTextColor(Color.parseColor("#808080"));
				tv_a4_haoping.setTextColor(Color.parseColor("#FF5500"));
				im_a4_xiaoliangstate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchmoren));
				im_a4_jiagestate.setImageDrawable(getResources().getDrawable(R.drawable.a4_searchmoren));
				
				key = 2;order = 1;
			}else{
				key = 2;order = 1;
			}
			curpage = 1;
			requestData();
			break;
		case R.id.ly_a4_category:
			//全部分类
			b4tsa = new B4_TextSizeAdapter(B1_a4_SearchActivity.this, shopClass);
			pList.setAdapter(b4tsa);
            pList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
					order = 1;curpage = 1;keyword = "";
	                if(popupWindow.isShowing()){
	                    popupWindow.dismiss();
	                }
	                id = shopClass.get(position).get("sc_id");
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
			b4tsa = new B4_TextSizeAdapter(B1_a4_SearchActivity.this, shopClass1);
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
		case R.id.im_search:
			keyword = a4_sydpss.getText().toString();
			curpage = 1;
			requestData();
			break;
		default:
			key = 0;order = 0;
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		switch(actionId){
		case EditorInfo.IME_ACTION_SEARCH:
			keyword = view.getText().toString().trim();
			curpage = 1;
			key = 0;order = 0;
			requestData();
		}
		return true;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		curpage = 1;keyword = "";
		key = 0;order = 0;
		changeModule();
		requestData();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}
}