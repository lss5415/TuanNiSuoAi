package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_2_MyPointsDetailAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.alibaba.fastjson.JSONException;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;


public class B5_2_MyPointsDetail extends BaseActivity implements IXListViewListener  {
	private MyListView listview;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	B5_2_MyPointsDetailAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_5_2_my_points_detail);
		listview = (MyListView) findViewById(R.id.listview_points);
		adapter = new B5_2_MyPointsDetailAdapter(B5_2_MyPointsDetail.this,data);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getPointsLog(res_Points, getSharedPreferenceValue("key"));
	}
//*****************************网络请求返回操作******************************************
	/**
	 * 获取积分详情
	 */
	JsonHttpResponseHandler res_Points = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				try {
					org.json.JSONArray array = datas.getJSONArray("ponits_list");
					Tools.Log("res_Points_array="+array);
					data.clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("pl_desc", jsonItem.getString("pl_desc"));
						map.put("pl_points", jsonItem.getString("pl_points"));
						map.put("pl_total_points", jsonItem.getString("pl_total_points"));
						map.put("pl_addtime", jsonItem.getString("pl_addtime"));
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
	public void points_back(View view)
	{
		this.finish();
	}
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getPointsLog(res_Points, getSharedPreferenceValue("key"));
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "只有这么多数据", Toast.LENGTH_LONG).show();
	}
}
