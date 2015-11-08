package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_3_ShaiDanQuanAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @author zyk 2015年6月17日 晒单圈
 *
 */
public class B5_3_ShaiDanQuan extends BaseActivity implements IXListViewListener{

	String key;
	ImageView b1_1_back,iv_gotomyshaidanquan;
	TextView tv_moren,tv_benzhou,tv_zongpinglunshu,tv_dianzanshu;
	String tag1,tag2,tag3,tag4;
	MyListView lv_shaidanquan;
	B5_3_ShaiDanQuanAdapter adapter;
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b5_3_shaidanquan);
		initView();
		adapter = new B5_3_ShaiDanQuanAdapter(B5_3_ShaiDanQuan.this,data,key);
		lv_shaidanquan.setAdapter(adapter);
		lv_shaidanquan.setPullLoadEnable(true);
		lv_shaidanquan.setPullRefreshEnable(true);
		lv_shaidanquan.setXListViewListener(this, 0);
		lv_shaidanquan.setRefreshTime();
		
		setListener(b1_1_back,tv_moren,tv_benzhou,tv_zongpinglunshu,tv_dianzanshu,iv_gotomyshaidanquan);
		RequestDailog.showDialog(this, "正在加载数据");
		HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "1");
	}
	
	private void initView(){
		key = getSharedPreferenceValue("key");
		b1_1_back = (ImageView) findViewById(R.id.b1_1_back);
		iv_gotomyshaidanquan = (ImageView) findViewById(R.id.iv_gotomyshaidanquan);
		
		tv_moren = (TextView) findViewById(R.id.tv_moren);
		tv_benzhou = (TextView) findViewById(R.id.tv_benzhou);
		tv_zongpinglunshu = (TextView) findViewById(R.id.tv_zongpinglunshu);
		tv_dianzanshu = (TextView) findViewById(R.id.tv_dianzanshu);
		
		lv_shaidanquan = (MyListView) findViewById(R.id.lv_shaidanquan);
		
		tv_moren.setTextColor(Color.parseColor("#FF5500"));
		tv_benzhou.setTextColor(Color.parseColor("#000000"));
		tv_zongpinglunshu.setTextColor(Color.parseColor("#000000"));
		tv_dianzanshu.setTextColor(Color.parseColor("#000000"));
		
		tag1="1";
		tag2="0";
		tag3="0";
		tag4="0";
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.b1_1_back:
			this.finish();
			break;
		case R.id.tv_moren:
			
			tv_moren.setTextColor(Color.parseColor("#FF5500"));
			tv_benzhou.setTextColor(Color.parseColor("#000000"));
			tv_zongpinglunshu.setTextColor(Color.parseColor("#000000"));
			tv_dianzanshu.setTextColor(Color.parseColor("#000000"));
			
			tag1="1";
			tag2="0";
			tag3="0";
			tag4="0";
			
			RequestDailog.showDialog(this, "正在加载数据");
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "1");
			
			break;
		case R.id.tv_benzhou:
			tv_moren.setTextColor(Color.parseColor("#000000"));
			tv_benzhou.setTextColor(Color.parseColor("#FF5500"));
			tv_zongpinglunshu.setTextColor(Color.parseColor("#000000"));
			tv_dianzanshu.setTextColor(Color.parseColor("#000000"));
			
			tag1="0";
			tag2="1";
			tag3="0";
			tag4="0";
			
			RequestDailog.showDialog(this, "正在加载数据");
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "2");
			break;
		case R.id.tv_zongpinglunshu:
			tv_moren.setTextColor(Color.parseColor("#000000"));
			tv_benzhou.setTextColor(Color.parseColor("#000000"));
			tv_zongpinglunshu.setTextColor(Color.parseColor("#FF5500"));
			tv_dianzanshu.setTextColor(Color.parseColor("#000000"));
			
			tag1="0";
			tag2="0";
			tag3="1";
			tag4="0";
			
			RequestDailog.showDialog(this, "正在加载数据");
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "3");
			break;
		case R.id.tv_dianzanshu:
			tv_moren.setTextColor(Color.parseColor("#000000"));
			tv_benzhou.setTextColor(Color.parseColor("#000000"));
			tv_zongpinglunshu.setTextColor(Color.parseColor("#000000"));
			tv_dianzanshu.setTextColor(Color.parseColor("#FF5500"));
			
			tag1="0";
			tag2="0";
			tag3="0";
			tag4="1";
			
			RequestDailog.showDialog(this, "正在加载数据");
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "4");
			break;
		case R.id.iv_gotomyshaidanquan:
			Intent intent22 = new Intent(B5_3_ShaiDanQuan.this,B5_3_MyShaiDanQuan.class);
			startActivity(intent22);
		}

	}
	
	/**
	 * 获取晒单圈数据
	 */
	JsonHttpResponseHandler res_shaidanquan_shouye = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("晒单圈数据="+response);
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
//				try {
				data.clear();
				JSONArray array;
				try {
					array = datas.getJSONArray("circle_list");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, Object> map = new HashMap();
						map.put("addtime", jsonItem.getString("addtime"));
						map.put("replys", jsonItem.getString("replys"));
						map.put("circle_id", jsonItem.getString("circle_id"));
						map.put("views", jsonItem.getString("views"));
						map.put("description", jsonItem.getString("description"));
						map.put("image", jsonItem.getJSONObject("image"));
						map.put("praise", jsonItem.getString("praise"));
						map.put("avatar", jsonItem.getString("avatar"));
						map.put("member_name", jsonItem.getString("member_name"));
						map.put("member_id", jsonItem.getString("member_id"));
						map.put("quote", jsonItem.getJSONArray("quote"));
						
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
				Tools.Log("res_Points_error="+error+"");
				Tools.Notic(B5_3_ShaiDanQuan.this, error, null);
			}
			
		}
		
		
	};
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		RequestDailog.showDialog(this, "正在加载数据");
		if (tag1.equals("1")) {
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "1");
		}
		else if(tag2.equals("2")) {
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "2");
		}
		else if(tag3.equals("3")) {
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "3");
		}
		else if(tag4.equals("4")) {
			HttpUtils.shaidanquan_shouye(res_shaidanquan_shouye, key, "4");
		}
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		
	}
}
	

