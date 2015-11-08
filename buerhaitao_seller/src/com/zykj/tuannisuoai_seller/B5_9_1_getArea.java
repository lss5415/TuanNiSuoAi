package com.zykj.tuannisuoai_seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.adapter.B5_9_1_LeftAdapter;
import com.zykj.tuannisuoai_seller.adapter.B5_9_1_MiddleAdapter;
import com.zykj.tuannisuoai_seller.adapter.B5_9_1_RightAdapter;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.ListViewForScroll;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.Tools;
/**
 * 选择区域（省-市-区）
 * @author Administrator
 *
 */
public class B5_9_1_getArea extends BaseActivity {
	private int GetArea=1;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	List<Map<String, String>> dataMiddle = new ArrayList<Map<String, String>>();
	List<Map<String, String>> dataRight = new ArrayList<Map<String, String>>();
	private ListViewForScroll lv_class_left = null;
	private ListViewForScroll lv_class_middle = null;
	private ListViewForScroll lv_class_right = null;
	private int one = 0, two = 0;
	// 以上数据用不到
//	B0_ClassLeftAdapter class_ada_one;
//	B0_ClassMiddle_Adapter class_ada_two;
//	B0_ClassRightAdapter class_ada_three;
	B5_9_1_LeftAdapter class_ada_one;
	B5_9_1_MiddleAdapter class_ada_two;
	B5_9_1_RightAdapter class_ada_three;
	/**
	 * 用来记录点击的listview；一共两种点击状态，点击最左边弹出中间，点击中间弹出第最右边
	 */
	int ON_LISTVIEW = 0;
	private LinearLayout ll_left;
	private LinearLayout ll_middle;
	private LinearLayout ll_right;
	private ScrollView m_scroll;
	
	String parent_id_middle = "";
	String parent_id_right = "";
	
	String province[];//省
	String city[];//市
	String area[];//区
	
	String provincesString,cityString,areaString;
	String city_idString,area_idString;
	
	Intent it;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b5_9_1_getarea);
		
		RequestDailog.showDialog(this, "正在请求数据");
		it=getIntent();
		if (data.size() == 0) {
			HttpUtils.getArea(res_getArea,"");
		}
		m_scroll=(android.widget.ScrollView) findViewById(R.id.ScrollView);
		
		ll_left=(LinearLayout) findViewById(R.id.ll_left);
		ll_middle = (LinearLayout) findViewById(R.id.ll_middle);
		ll_right = (LinearLayout)findViewById(R.id.ll_right);
		
		dataMiddle = new ArrayList<Map<String, String>>();
		dataRight = new ArrayList<Map<String, String>>();
		
		class_ada_one = new B5_9_1_LeftAdapter(this, data);
		class_ada_two = new B5_9_1_MiddleAdapter(this, dataMiddle);
		class_ada_three = new B5_9_1_RightAdapter(this,dataRight);
		
		lv_class_left = (ListViewForScroll) findViewById(R.id.lv_class_left);
		lv_class_middle = (ListViewForScroll) findViewById(R.id.lv_class_middle);
		lv_class_left.setVisibility(View.VISIBLE);
		lv_class_middle.setVisibility(View.GONE);
		lv_class_right = (ListViewForScroll) findViewById(R.id.lv_class_right);
		lv_class_right.setVisibility(View.GONE);
		lv_class_left.setAdapter(class_ada_one);
		lv_class_middle.setAdapter(class_ada_two);
		lv_class_right.setAdapter(class_ada_three);
		
		lv_class_left.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int i,
					long arg3) {
				ON_LISTVIEW = 1;
				parent_id_middle = view.getTag().toString();
				RequestDailog.showDialog(B5_9_1_getArea.this, "正在请求数据,请稍后");
//				bundle.putString("province", province[i]);//bundle1
				provincesString=province[i];
				Log.e("zhong", parent_id_middle+"");
				HttpUtils.getArea(res_getAreaMiddle,parent_id_middle);
				one = i;
				class_ada_one.setItem(one);
				class_ada_one.notifyDataSetChanged();
				// lv_class_middle.setAdapter(class_ada_two);
				lv_class_middle.setVisibility(View.VISIBLE);
				ll_middle.setVisibility(View.VISIBLE);
				ll_right.setVisibility(View.GONE);
				ll_left.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT, 2));
				ll_middle.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT, 1));
			}
		});
		
		lv_class_middle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				two = arg2;
				class_ada_two.setItem(two);
//				if (two != 0) {
					class_ada_two.notifyDataSetChanged();
					parent_id_right = view.getTag().toString();
//					bundle.putString("city", city[arg2]);//bundle2
					cityString= city[arg2];
//					bundle.putString("city_id", parent_id_right);//城市编号(地址联动的第二级)传给bundle
					city_idString=parent_id_right;
					HttpUtils.getArea(res_getAreaRight,parent_id_right);
					lv_class_right.setVisibility(View.VISIBLE);
					ll_right.setVisibility(View.VISIBLE);
					ll_left.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT, 1));
					ll_middle.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT, 1));
					ll_right.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT, 1));
//				} else {
//					Intent it = new Intent(getActivity(),
//							B1_GoodsListActivity.class);
//					it.putExtra("gc_id", view.getTag() + "");
//					startActivity(it);
//					getActivity().overridePendingTransition(
//							R.anim.push_right_in, R.anim.push_right_out);
//				}
			}
		});
		
		lv_class_right.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
//				bundle.putString("area_id", view.getTag().toString());
				area_idString=view.getTag().toString();
//				bundle.putString("area", area[arg2]);//bundle2
				areaString=area[arg2];
//				Intent it = new Intent(B5_9_1_getArea.this,B5_9_1_addAddress.class);
//				it.putExtras(bundle);
				it.putExtra("province", provincesString);
				it.putExtra("city", cityString);
				it.putExtra("area", areaString);
				it.putExtra("province_id", provincesString);
				it.putExtra("city_id", city_idString);
				it.putExtra("area_id", area_idString);
				setResult(GetArea, it);
				B5_9_1_getArea.this.finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

		
	}
	//左侧list
	JsonHttpResponseHandler res_getArea = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("左边="+response);
			RequestDailog.closeDialog();
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
				JSONArray array;
				try {
					array = datas.getJSONArray("area_list");
				    province=new String [array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("area_id", jsonItem.getString("area_id"));
						map.put("area_name", jsonItem.getString("area_name"));
						
						province[i]=jsonItem.getString("area_name");//省份的名字装到数组里面
						
						data.add(map);
					}
					class_ada_one.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
	};
	//中间list
	JsonHttpResponseHandler res_getAreaMiddle = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("中间="+response);
			m_scroll.smoothScrollTo(0, 0);//跳转到顶部
			RequestDailog.closeDialog();
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
				dataMiddle.clear();
				JSONArray array;
				try {
					array = datas.getJSONArray("area_list");
					city=new String [array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("area_id", jsonItem.getString("area_id"));
						map.put("area_name", jsonItem.getString("area_name"));
						city[i]=jsonItem.getString("area_name");
						dataMiddle.add(map);
					}
					class_ada_two.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
	};
	//右边list
	JsonHttpResponseHandler res_getAreaRight = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
//			Tools.Log("Right_response="+response);
			m_scroll.smoothScrollTo(0, 0);//跳转到顶部
			RequestDailog.closeDialog();
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
				dataRight.clear();
				JSONArray array;
				try {
					array = datas.getJSONArray("area_list");
					area=new String [array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("area_id", jsonItem.getString("area_id"));
						map.put("area_name", jsonItem.getString("area_name"));
						area[i]=jsonItem.getString("area_name");
						dataRight.add(map);
					}
					class_ada_three.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
			}
		}
	};

}
