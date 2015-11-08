package com.ZYKJ.tuannisuoai.UI;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_5_OrderStatusAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 订单状态 页
 * 订单状态（待付款:10,待发货:20,待收货:30,已收货:40）
 * @author zyk
 *
 */
public class B5_5_OrderStatus extends BaseActivity implements IXListViewListener {
	
//  order_state 订单状态（待付款:10,待发货:20,待收货:30,已收货:40）
    private static final int DAIFUKUAN  = 10;
    private static final int DAIFAHUO   = 20;
    private static final int DAISHOUHUO = 30;
    private static final int YISHOUHUO  = 40;
    
    private MyListView listview;
    B5_5_OrderStatusAdapter adapter;
    List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
    
    private int status=0;
    
	ImageButton orderstatus_back;
	TextView tv_title;
	View v101,v102,v103,v104;
	
	LinearLayout ll_daifukuan,ll_daifahuo,ll_daishouhuo,ll_yishouhuo;
	
	String key;
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			HttpUtils.getOrderList(res_getOrderList, key, status);
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b5_5_orderstatus);
		iniView();
		setListener(orderstatus_back,ll_daifukuan,ll_daifahuo,ll_daishouhuo,ll_yishouhuo);
		key = getSharedPreferenceValue("key");
		status = getIntent().getIntExtra("STATUS", 0);
		switch (status) {
		case DAIFUKUAN:
			tv_title.setText("待付款");
			v101.setVisibility(View.VISIBLE);
			v102.setVisibility(View.INVISIBLE);
			v103.setVisibility(View.INVISIBLE);
			v104.setVisibility(View.INVISIBLE);
//			HttpUtils.getOrderList(res_getOrderList, key, DAIFUKUAN);
		break;
		case DAIFAHUO:
			tv_title.setText("待发货");
			v101.setVisibility(View.INVISIBLE);
			v102.setVisibility(View.VISIBLE);
			v103.setVisibility(View.INVISIBLE);
			v104.setVisibility(View.INVISIBLE);
		break;
		case DAISHOUHUO:
			tv_title.setText("待收货");
			v101.setVisibility(View.INVISIBLE);
			v102.setVisibility(View.INVISIBLE);
			v103.setVisibility(View.VISIBLE);
			v104.setVisibility(View.INVISIBLE);
		break;
		case YISHOUHUO:
			tv_title.setText("已收货");
			v101.setVisibility(View.INVISIBLE);
			v102.setVisibility(View.INVISIBLE);
			v103.setVisibility(View.INVISIBLE);
			v104.setVisibility(View.VISIBLE);
		break;

		default:
			break;
		}
		
		adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getOrderList(res_getOrderList, key, status);
	}

	private void iniView() {
		// TODO Auto-generated method stub
		orderstatus_back = (ImageButton) findViewById(R.id.orderstatus_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		v101 = findViewById(R.id.v101);
		v102 = findViewById(R.id.v102);
		v103 = findViewById(R.id.v103);
		v104 = findViewById(R.id.v104);
		listview = (MyListView) findViewById(R.id.listview_orderlist);
		ll_daifukuan = (LinearLayout) findViewById(R.id.ll_daifukuan);
		ll_daifahuo = (LinearLayout) findViewById(R.id.ll_daifahuo);
		ll_daishouhuo = (LinearLayout) findViewById(R.id.ll_daishouhuo);
		ll_yishouhuo = (LinearLayout) findViewById(R.id.ll_yishouhuo);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.orderstatus_back:
			this.finish();
			break;
		case R.id.ll_daifukuan://待付款
			tv_title.setText("待付款");
			v101.setVisibility(View.VISIBLE);
			v102.setVisibility(View.INVISIBLE);
			v103.setVisibility(View.INVISIBLE);
			v104.setVisibility(View.INVISIBLE);
			status = DAIFUKUAN;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList, key, status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		case R.id.ll_daifahuo://待发货
			tv_title.setText("待发货");
			v101.setVisibility(View.INVISIBLE);
			v102.setVisibility(View.VISIBLE);
			v103.setVisibility(View.INVISIBLE);
			v104.setVisibility(View.INVISIBLE);
			status = DAIFAHUO;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList,key,status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		case R.id.ll_daishouhuo://待收货
			tv_title.setText("待收货");
			v101.setVisibility(View.INVISIBLE);
			v102.setVisibility(View.INVISIBLE);
			v103.setVisibility(View.VISIBLE);
			v104.setVisibility(View.INVISIBLE);
			status = DAISHOUHUO;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList,key,status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		case R.id.ll_yishouhuo://已收货
			tv_title.setText("已收货");
			v101.setVisibility(View.INVISIBLE);
			v102.setVisibility(View.INVISIBLE);
			v103.setVisibility(View.INVISIBLE);
			v104.setVisibility(View.VISIBLE);
			status = YISHOUHUO;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList, key, status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		default:
			break;
		}
		super.onClick(v);
	}
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getOrderList(res_getOrderList,key,status);
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
//		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getOrderList(res_getOrderList, key, status);
	}
	/**
	 * 获得订单列表
	 */
	JsonHttpResponseHandler res_getOrderList = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("订单列表="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
//				 Tools.Log("datas="+datas);
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				try {
					dataList.clear();
					JSONArray order_group_list = datas.getJSONArray("order_group_list");//
					Tools.Log("order_group_list="+order_group_list);
					for (int j = 0; j < order_group_list.length(); j++) {
						Map<String, Object> map = new HashMap();
						JSONObject order_group_list1 = (JSONObject) order_group_list.get(j);
						JSONArray order_list = order_group_list1.getJSONArray("order_list");
						JSONObject order_list1 =((JSONObject) order_list.get(0));
						
						map.put("store_name", order_list1.getString("store_name"));
						map.put("store_phone", order_list1.getString("store_phone"));
						map.put("order_id", order_list1.getString("order_id"));
						map.put("if_evaluation", order_list1.getString("if_evaluation"));
						map.put("pay_sn", order_list1.getString("pay_sn"));
						map.put("order_amount", order_list1.getString("order_amount"));
						map.put("extend_order_goods", order_list1.getJSONArray("extend_order_goods"));
						dataList.add(map);
					}
//					Tools.Log("dataList="+dataList);
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
	
	
}
