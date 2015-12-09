package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B0_StoreInfoAdapter;
import com.ZYKJ.tuannisuoai.adapter.IndexPageAdapter2;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.utils.StringUtil;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.alibaba.fastjson.JSONException;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author lss 2015年6月29日 店铺详情
 *
 */
public class BX_DianPuXiangQingActivity extends BaseActivity implements IXListViewListener  {
	// 返回
	private ImageButton im_storeback;
	private MyListView listvsinf;
	private List<Map<String, Object>> datax = new ArrayList<Map<String, Object>>();
//	private String re;
	private B0_StoreInfoAdapter stoadapter;
	//店铺ID
	private String store_id;
	//key 登录令牌
	private String key;
	//店铺名称
//	private TextView tv_storename;
	//分项
	private ImageView im_storeshare;
	//收藏
	private ImageView im_storeshoucang;
	//轮播图
	private AutoScrollViewPager viewPager;
	//店铺图片
	private ImageView im_xiao_xqback;
	//店铺名称
	private TextView tv_dpxq_storename;
	//店铺评分
	private RatingBar rb_dpxiangqing_rating_bar;
	//店铺地址
	private TextView tv_store_address;
	//店铺电话
	private ImageView im_store_phone;
	private String store_phone=null;
	private String dianpuming;
	/** 当前的位置 */
	private int now_pos = 0;
	/** 滚动层 */
	private ScrollView m_scroll;
	private LinearLayout ll_ditudaohang;//地图导航
	String endlat,endlng,isshoucang="false";
	private int curpage = 1;//当前页
	JSONObject datas = null;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_dianpuxiangqing);

        store_id = getIntent().getStringExtra("store_id");
        key = getSharedPreferenceValue("key");
//		tv_storename = (TextView)findViewById(R.id.tv_storename);
		im_storeshare = (ImageView)findViewById(R.id.im_storeshare);
		im_storeshoucang = (ImageView)findViewById(R.id.im_storeshoucang);
		im_xiao_xqback = (ImageView)findViewById(R.id.im_xiao_xqback);
		tv_dpxq_storename = (TextView)findViewById(R.id.tv_dpxq_storename);
		rb_dpxiangqing_rating_bar = (RatingBar)findViewById(R.id.rb_dpxiangqing_rating_bar);
		tv_store_address = (TextView)findViewById(R.id.tv_store_address);
		im_store_phone = (ImageView)findViewById(R.id.im_store_phone);
		im_storeback = (ImageButton) findViewById(R.id.im_storeback);
		ll_ditudaohang = (LinearLayout) findViewById(R.id.ll_ditudaohang);
		m_scroll = (ScrollView) findViewById(R.id.index_scroll);

		viewPager = (AutoScrollViewPager) findViewById(R.id.index_viewpage);
		LayoutParams pageParms = viewPager.getLayoutParams();
		pageParms.width = Tools.M_SCREEN_WIDTH;
		pageParms.height = Tools.M_SCREEN_WIDTH*2/3;

		viewPager.setInterval(2000);
		viewPager.startAutoScroll();

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				// 回调view
				uihandler.obtainMessage(0, arg0).sendToTarget();
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		listvsinf = (MyListView) findViewById(R.id.lv_store_info);
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getStoreInfo(res_storeinfo, store_id, key,String.valueOf(curpage));
		listvsinf.setPullLoadEnable(true);
		listvsinf.setPullRefreshEnable(true);
		listvsinf.setXListViewListener(this, 0);
		listvsinf.setRefreshTime();
		listvsinf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				String goid = datax.get(arg2-1).get("goods_id").toString();
				intent.putExtra("goods_id", goid);
				intent.setClass(BX_DianPuXiangQingActivity.this, Sp_GoodsInfoActivity.class);
				startActivity(intent);
			}
		});
		setListener(im_storeback,im_storeshare,im_storeshoucang,im_store_phone,ll_ditudaohang);
	}

	/**
	 * 收藏成功
	 */
	JsonHttpResponseHandler res_addstore = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();

			String error = null;
			JSONObject datas = null;
			if (error == null)// 成功
			{
				Toast.makeText(BX_DianPuXiangQingActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
				im_storeshoucang.setImageDrawable(getResources().getDrawable(R.drawable.storeyishoucang));
			}
		}
	};

	/**
	 * 取消收藏
	 */
	JsonHttpResponseHandler res_delstore = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();

			String error = null;
			JSONObject datas = null;
			if (error == null)// 成功
			{
				Toast.makeText(BX_DianPuXiangQingActivity.this, "取消收藏成功", Toast.LENGTH_LONG).show();
				im_storeshoucang.setImageDrawable(getResources().getDrawable(R.drawable.storeshoucang));
			}
		}
	};

	/**
	 * 获取店铺详情
	 */
	JsonHttpResponseHandler res_storeinfo = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();

			String error = null;
			try {
				datas = response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException 	e) {
				
				e.printStackTrace();
			} catch (org.json.JSONException e) {
				
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				try {
					JSONObject obj = datas.getJSONObject("store_info");
					dianpuming = obj.getString("store_name");
					tv_dpxq_storename.setText(dianpuming);
					isshoucang = obj.getString("is_favorate");
					if (isshoucang=="false") {
						im_storeshoucang.setImageDrawable(getResources().getDrawable(R.drawable.storeshoucang));
					}else {
						im_storeshoucang.setImageDrawable(getResources().getDrawable(R.drawable.storeyishoucang));
					}
					ImageLoader.getInstance().displayImage(obj.getString("store_avatar"), im_xiao_xqback, ImageOptions.getOpstion(), animateFirstListener);

					rb_dpxiangqing_rating_bar.setRating(Float.parseFloat(obj.getString("store_credit_composite")));
					if (obj.getString("location").equals("null")) {
						tv_store_address.setText("");
					}else {
						tv_store_address.setText(StringUtil.toString(obj.getString("location"), ""));
					}
					endlat = obj.getString("lat");
					endlng = obj.getString("lng");
					store_phone = obj.getString("store_phone");
					if (m_scroll.getVisibility() != View.VISIBLE) {
						m_scroll.setVisibility(View.VISIBLE);
					}
					try {
						JSONArray jjls = obj.getJSONArray("mb_sliders");
						// 设置轮播
						viewPager.setAdapter(new IndexPageAdapter2(BX_DianPuXiangQingActivity.this, jjls));
						// 设置选中的标识
						LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
						for (int i = 0; i < jjls.length(); i++) {
							ImageView pointView = new ImageView(BX_DianPuXiangQingActivity.this);
							if (i == 0) {
								pointView.setBackgroundResource(R.drawable.feature_point_cur);
							} else {
								pointView.setBackgroundResource(R.drawable.feature_point);
								pointLinear.addView(pointView);
							}
								
						}

						// 滚动scrollView到头部
						m_scroll.smoothScrollTo(0, 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
					JSONArray array = datas.getJSONArray("goods_list");
//					datax.clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>(); 
						map.put("goods_id", jsonItem.getString("goods_id"));
						map.put("goods_commend", jsonItem.getString("goods_commend"));
						map.put("goods_jingle", jsonItem.getString("goods_jingle"));
						map.put("goods_name", jsonItem.getString("goods_name"));
						map.put("goods_price", jsonItem.getString("goods_price"));
						map.put("goods_salenum", jsonItem.getString("goods_salenum"));
						map.put("goods_storage", jsonItem.getString("goods_storage"));
						map.put("evaluation_good_star", jsonItem.getString("evaluation_good_star"));
						map.put("goods_promotion_price", jsonItem.getString("goods_promotion_price"));
						map.put("evaluation_count", jsonItem.getString("evaluation_count"));
						map.put("goods_image_url", jsonItem.getString("goods_image_url"));
						map.put("is_special", jsonItem.getInt("is_special"));
						datax.add(map);
					}
					stoadapter = new B0_StoreInfoAdapter(BX_DianPuXiangQingActivity.this,datax,dianpuming);
					listvsinf.setAdapter(stoadapter);
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
		case R.id.im_storeback:
			 BX_DianPuXiangQingActivity.this.finish();
			break;
		case R.id.im_storeshare:
			String fxnr = "团你所爱-消费者的满意生活购物网站，团购的好选择。时装、母婴、数码家电、美妆、日用百货等，方寸之间尽享购物。天天特价、晒单圈、猜你喜欢、每日好店，精彩推送您最感兴趣的。便宜实惠，品质保证，服务专业，团你所爱，我的生活。";
			String fxtp = "http://www.pgyer.com/x8bv";
			FenXiang fx = new FenXiang(getApplicationContext(),BX_DianPuXiangQingActivity.this,fxnr,fxtp);
			break;
		case R.id.im_storeshoucang:
			if (isLogin()) {
			if (isshoucang=="false") {
				isshoucang="true";
				im_storeshoucang.setImageDrawable(getResources().getDrawable(R.drawable.storeshoucang));
				HttpUtils.addStore(res_addstore, store_id, key);
			}else {
				isshoucang="false";
				im_storeshoucang.setImageDrawable(getResources().getDrawable(R.drawable.storeyishoucang));
				HttpUtils.delStore(res_delstore, store_id, key);
			}
			}else {
				Intent intent_login=new Intent();
				intent_login.setClass(this, B5_1_LoginActivity.class);
				startActivity(intent_login);
			}
			break;
		case R.id.im_store_phone:
			
			 if(store_phone.trim().length()!=0)
			    {
			     Intent phoneIntent = new Intent("android.intent.action.CALL",
			       Uri.parse("tel:" + store_phone));
			     //启动
			     startActivity(phoneIntent); 
			    }
			    //否则Toast提示一下
			    else{
			    	Toast.makeText(BX_DianPuXiangQingActivity.this, "该店铺未留电话", Toast.LENGTH_LONG).show();
			    }
			break;
		case R.id.ll_ditudaohang:
			Intent simoit = new Intent(BX_DianPuXiangQingActivity.this, SimpleGPSNaviActivity.class);
			simoit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			simoit.putExtra("endlat", endlat);
			simoit.putExtra("endlng", endlng);
			startActivity(simoit);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh(int id) {
		/*下拉加载*/
//		curpage = 1;
//		HttpUtils.getStoreInfo(res_storeinfo, store_id, key,String.valueOf(curpage));
	}

	@Override
	public void onLoadMore(int id) {
		/*上拉刷新*/
		curpage += 1;
		HttpUtils.getStoreInfo(res_storeinfo, store_id, key,String.valueOf(curpage));
	}

	public void changePointView(int cur) {
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(now_pos);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			now_pos = cur;
		}
	}

	Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:// 滚动的回调
				changePointView((Integer) msg.obj);
				break;
			}

		}

	};

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (viewPager != null) {
			viewPager.startAutoScroll();
		}
		// 重新设置scrollView的位置
		if (m_scroll != null && m_scroll.getVisibility() == View.VISIBLE) {
			m_scroll.smoothScrollTo(0, 0);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (viewPager != null) {
			viewPager.stopAutoScroll();
		}

	}
	
	public boolean isLogin()
	{
		if (getSharedPreferenceValue("userid").equals("")) {
			return false;
		}else {
			return true;
		}
	}
}
