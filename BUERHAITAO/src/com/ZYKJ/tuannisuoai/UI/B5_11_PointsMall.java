package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_11_PointsMallAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 积分商城
 * @author zyk
 *
 */
public class B5_11_PointsMall extends BaseActivity implements IXListViewListener {
	private MyListView listview;
	B5_11_PointsMallAdapter adapter;
	List<Map<String, String>> data = new ArrayList<Map<String,String>>();
	private TextView tv_totlePoints;
	private ImageButton pointsmall_back;
	private TextView tv_record;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_points_mall);
		tv_totlePoints=(TextView) findViewById(R.id.tv_totlePoints);
		pointsmall_back=(ImageButton) findViewById(R.id.pointsmall_back);
		tv_record=(TextView) findViewById(R.id.tv_record);
		listview=(MyListView) findViewById(R.id.listview_pointsMall);
		adapter = new B5_11_PointsMallAdapter(B5_11_PointsMall.this,data);
		listview.setAdapter(adapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this, 0);
		listview.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.pointsMall(res_pointsMall, getSharedPreferenceValue("key"));
		
		setListener(pointsmall_back,tv_record);
		
	}
	public void onClick(android.view.View v) {
		switch (v.getId()) {
		case R.id.pointsmall_back://退出
			this.finish();
			break;
		case R.id.tv_record://兑换记录
//			Toast.makeText(this, "兑换记录", Toast.LENGTH_LONG).show();
			Intent intent_to_record= new Intent(this,B5_11_2_ExchangeRecord.class);
			startActivity(intent_to_record);
			break;
		default:
			break;
		}
		
	};
	/**
	 * 获取积分详情
	 */
	JsonHttpResponseHandler res_pointsMall = new JsonHttpResponseHandler()
	{

		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_pointsMallresponse="+response);
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
				try {
					data.clear();
					tv_totlePoints.setText(datas.getString("member_points"));//我的积分
					org.json.JSONArray array = datas.getJSONArray("pro_list");
					Tools.Log("res_pointsMall_array="+array);
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("pgoods_id", jsonItem.getString("pgoods_id"));
						map.put("pgoods_image_small", jsonItem.getString("pgoods_image_small"));
						map.put("pgoods_image_old", jsonItem.getString("pgoods_image_old"));
						map.put("ex_state", jsonItem.getString("ex_state"));
						map.put("pgoods_points", jsonItem.getString("pgoods_points"));
						map.put("pgoods_body", jsonItem.getString("pgoods_body"));
						map.put("pgoods_name", jsonItem.getString("pgoods_name"));
						map.put("pgoods_image", jsonItem.getString("pgoods_image"));
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
	
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.pointsMall(res_pointsMall, getSharedPreferenceValue("key"));
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
//		Toast.makeText(this, "目前只有这些", Toast.LENGTH_LONG).show();
		HttpUtils.pointsMall(res_pointsMall, getSharedPreferenceValue("key"));
	}

}
