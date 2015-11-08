package com.zykj.tuannisuoai_seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.adapter.B5_5_OrderStatusAdapter;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.MyListView;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.ToastView;
import com.zykj.tuannisuoai_seller.utils.Tools;

public class B4_OrderActivity extends BaseActivity implements IXListViewListener {
	
//  order_state 订单状态（待发货:20,已发货:30,已完成:40,已取消:0）
	private static final int DAIFAHUO    = 20;
	private static final int YIFAHUO     = 30;
    private static final int YIWANCHENG  = 40;
    private static final int YIQUXIAO    = 0;
    
    private MyListView listview;
    B5_5_OrderStatusAdapter adapter;
    List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
    
    private int status=20;
	View v101,v102,v103,v104;
	
	LinearLayout ll_daifukuan,ll_daifahuo,ll_daishouhuo,ll_yishouhuo;
	
	String key;
	int curpage=1;
	int listSize = 0;
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
			curpage=1;
		}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getSharedPreferenceValue("key");
		initView(R.layout.ui_order);
		initUI();
		setListener(ll_daifukuan,ll_daifahuo,ll_daishouhuo,ll_yishouhuo);
		v101.setVisibility(View.VISIBLE);
		v102.setVisibility(View.GONE);
		v103.setVisibility(View.GONE);
		v104.setVisibility(View.GONE);
		status = DAIFAHUO;
		adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		Log.e("key", key+"");
		Log.e("curpage", curpage+"");
		Log.e("status", status+"");
		curpage=1;
		HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
	}

	private void initUI() {
		// TODO Auto-generated method stub
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
		case R.id.ll_daifukuan://待发货
			curpage=1;
			v101.setVisibility(View.VISIBLE);
			v102.setVisibility(View.GONE);
			v103.setVisibility(View.GONE);
			v104.setVisibility(View.GONE);
			status = DAIFAHUO;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		case R.id.ll_daifahuo://已发货
			curpage=1;
			v101.setVisibility(View.GONE);
			v102.setVisibility(View.VISIBLE);
			v103.setVisibility(View.GONE);
			v104.setVisibility(View.GONE);
			status = YIFAHUO;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		case R.id.ll_daishouhuo://已完成
			curpage=1;
			v101.setVisibility(View.GONE);
			v102.setVisibility(View.GONE);
			v103.setVisibility(View.VISIBLE);
			v104.setVisibility(View.GONE);
			status = YIWANCHENG;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		case R.id.ll_yishouhuo://已取消
			curpage=1;
			v101.setVisibility(View.GONE);
			v102.setVisibility(View.GONE);
			v103.setVisibility(View.GONE);
			v104.setVisibility(View.VISIBLE);
			status = YIQUXIAO;
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
			adapter = new B5_5_OrderStatusAdapter(this,dataList,status,key);
			listview.setAdapter(adapter);
			break;
		default:
			break;
		}
		super.onClick(v);
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
					JSONArray order_list = datas.getJSONArray("order_list");
					listSize = order_list.length();
					for (int j = 0; j < order_list.length(); j++)
					{
						Map<String, Object> map = new HashMap();
						JSONObject orderJsonObject = order_list.getJSONObject(j);
						JSONArray extend_order_goods =  orderJsonObject.getJSONArray("extend_order_goods");
						map.put("extend_order_goods", extend_order_goods);
						map.put("order_id", orderJsonObject.get("order_id"));
						map.put("add_time", orderJsonObject.get("add_time"));
						map.put("goods_total_pay_price", orderJsonObject.get("goods_total_pay_price"));
						map.put("goods_total_num", orderJsonObject.get("goods_total_num"));
						dataList.add(map);
					}
					Log.e("dataList", dataList+"");
					adapter.notifyDataSetChanged();
				} 
				catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else//失败 
			{
//				Tools.Log("res_Points_error="+error+"");
				Tools.Notic(B4_OrderActivity.this, error+"", null);
			}
			
		}
		
		
	};
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		if (listSize>10) {
			curpage++;
			HttpUtils.getOrderList(res_getOrderList, key,curpage+"",status);
		}else {
			Toast.makeText(B4_OrderActivity.this, "只有这些订单", 500).show();
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
}
