package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B1_a2_CaiNiLikeAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.alibaba.fastjson.JSONException;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @author lss 2015年6月17日 猜你喜欢
 * 
 */
public class B1_a2_CaiNiXiHuan extends BaseActivity implements
		IXListViewListener {
	// 返回
	private ImageButton b1_a2_back;
	private MyListView listview_b1_a2_like;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private B1_a2_CaiNiLikeAdapter cainilikeadapter; 
	int curpage=1;
	private Handler mHandler = new Handler();//异步加载或刷新

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b1_a2_cainixihuan);

		b1_a2_back = (ImageButton) findViewById(R.id.b1_a2_back);
		listview_b1_a2_like = (MyListView) findViewById(R.id.listview_b1_a2_like);
		cainilikeadapter = new B1_a2_CaiNiLikeAdapter(B1_a2_CaiNiXiHuan.this,
				data);
		listview_b1_a2_like.setAdapter(cainilikeadapter);
		listview_b1_a2_like.setPullLoadEnable(true);
		listview_b1_a2_like.setPullRefreshEnable(true);
		listview_b1_a2_like.setXListViewListener(this, 0);
		listview_b1_a2_like.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
//		HttpUtils.getCaiNiLike(res_cnlike, "1", "1", "88", "0");
		HttpUtils.getCaiNiLike(res_cnlike,getSharedPreferenceValue("lng"),getSharedPreferenceValue("lat"),getSharedPreferenceValue("cityid"), String.valueOf(curpage));
		listview_b1_a2_like.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				String goid = data.get(arg2-1).get("goods_id");
				intent.putExtra("goods_id", goid);
				intent.setClass(B1_a2_CaiNiXiHuan.this, Sp_GoodsInfoActivity.class);
				startActivity(intent);
			}
		});
		setListener(b1_a2_back);
	}

	/**
	 * 获取积分详情
	 */
	JsonHttpResponseHandler res_cnlike = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();

			String error = null;
			JSONObject datas = null;
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
			if (error == null)// 成功
			{
				try {
					org.json.JSONArray array = datas.getJSONArray("goods_list");
					Tools.Log("res_Points_array=" + array);
					if (curpage>1) {
						
					}else {
						data.clear();
					}
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("nc_distinct",
								jsonItem.getString("nc_distinct"));
						map.put("goods_id", jsonItem.getString("goods_id"));
						map.put("goods_name", jsonItem.getString("goods_name"));
						map.put("goods_jingle",
								jsonItem.getString("goods_jingle"));
						map.put("goods_price",
								jsonItem.getString("goods_price"));
						map.put("goods_image",
								jsonItem.getString("goods_image_url"));
						map.put("juli", jsonItem.getString("juli"));
						map.put("goods_promotion_price",
								jsonItem.getString("goods_promotion_price"));
						map.put("is_special",
								jsonItem.getString("is_special"));
						data.add(map);
					}
					cainilikeadapter.notifyDataSetChanged();
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else// 失败
			{
				Tools.Log("res_Points_error=" + error + "");
			}
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.b1_a2_back:
			B1_a2_CaiNiXiHuan.this.finish();
			break;

		}
	}
	@Override
	public void onRefresh(int id) {
		/**下拉刷新 重建*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
					curpage = 1;
					HttpUtils.getCaiNiLike(res_cnlike,getSharedPreferenceValue("lng"),getSharedPreferenceValue("lat"),getSharedPreferenceValue("cityid"), String.valueOf(curpage));
					onLoad();
			}
		}, 1000);

	}
	@Override
	public void onLoadMore(int id) {
		/**上拉加载分页*/
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
					curpage += 1;
					HttpUtils.getCaiNiLike(res_cnlike,getSharedPreferenceValue("lng"),getSharedPreferenceValue("lat"),getSharedPreferenceValue("cityid"), String.valueOf(curpage));
					onLoad();
			}
		}, 1000);		
	}

	private void onLoad() {
		listview_b1_a2_like.stopRefresh();
		listview_b1_a2_like.stopLoadMore();
		listview_b1_a2_like.setRefreshTime();
	}
}
