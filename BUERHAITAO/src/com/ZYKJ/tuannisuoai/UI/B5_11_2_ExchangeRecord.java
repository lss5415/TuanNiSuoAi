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

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_11_2_ExchangeRecordAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 兑换记录
 * @author zyk
 *
 */
public class B5_11_2_ExchangeRecord extends BaseActivity implements IXListViewListener {
	ImageButton record_back;
	MyListView listview;
	B5_11_2_ExchangeRecordAdapter adapter;
	List<Map<String, String>> data = new ArrayList<Map<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b5_11_2_exchangerecord);
		record_back=(ImageButton) findViewById(R.id.record_back);
		setListener(record_back);
		listview=(MyListView) findViewById(R.id.listview_record);
		adapter = new B5_11_2_ExchangeRecordAdapter(B5_11_2_ExchangeRecord.this,data);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.exchangerecord(res_exchangerecord, getSharedPreferenceValue("key"));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.record_back://返回
			this.finish();
			break;
		default:
			break;
		}
	}
	JsonHttpResponseHandler res_exchangerecord = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_exchangerecord="+response);
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
				data.clear();
				try {
					JSONArray array = datas.getJSONArray("order_list");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("point_goodspoints", jsonItem.getString("point_goodspoints"));
						map.put("point_goodsname", jsonItem.getString("point_goodsname"));
						map.put("point_addtime", jsonItem.getString("point_addtime"));
						map.put("point_goodsimage", jsonItem.getString("point_goodsimage"));
						map.put("pgoods_body", jsonItem.getString("pgoods_body"));
						data.add(map);
					}
					adapter.notifyDataSetChanged();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else//失败 
			{
//				Tools.Log("res_Points_error="+error+"");
				Tools.Notic(B5_11_2_ExchangeRecord.this, "error="+error, null);
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			Tools.Notic(B5_11_2_ExchangeRecord.this, "网络连接失败，请重试", null);
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
	
	};

	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.exchangerecord(res_exchangerecord, getSharedPreferenceValue("key"));
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		HttpUtils.exchangerecord(res_exchangerecord, getSharedPreferenceValue("key"));
	}
}
