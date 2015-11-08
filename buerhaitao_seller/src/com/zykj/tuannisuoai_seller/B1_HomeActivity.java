package com.zykj.tuannisuoai_seller;

/**
 * 首页界面 lss 6.17
 */

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.adapter.IndexPageAdapter1;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.ToastView;
import com.zykj.tuannisuoai_seller.utils.Tools;

public class B1_HomeActivity extends BaseActivity {
	TextView tv_edit;//编辑
	TextView tv_storename;//店铺名
	TextView tv_storeaddress;//店铺地址（定位）
	TextView tv_address;//店铺地址（显示）
	TextView tv_daifahuo;//待发货订单数
	TextView tv_yifahuo;//已发货订单数
	TextView tv_yiwancheng;//已完成订单数
	TextView tv_jintidingdan;//今日订单
	TextView tc_dingdanzongshu_month;//本月订单总数
	TextView tv_lishiwancheng;//历史订单总数
	
	ImageView iv_phone;//店铺电话
	ImageView iv_logo;//店铺logo
//	ImageView iv_bg;//店铺背景
	
	/** 滚动层 */
	private ScrollView m_scroll;
	private AutoScrollViewPager viewPager;
	/** 当前的位置 */
	private int now_pos = 0;
	
	String key;
	String store_phone,store_name,store_address,area_info,logopath;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_home);
		key = getSharedPreferenceValue("key");
		initView();
		setListener(tv_edit,iv_phone);
		HttpUtils.getFirstList(res_getFirstList,key);
	}
	private void initView() {
		// TODO Auto-generated method stub
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		tv_storename = (TextView) findViewById(R.id.tv_storename);
		tv_storeaddress = (TextView) findViewById(R.id.tv_storeaddress);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_daifahuo = (TextView) findViewById(R.id.tv_daifahuo);
		tv_yifahuo = (TextView) findViewById(R.id.tv_yifahuo);
		tv_yiwancheng = (TextView) findViewById(R.id.tv_yiwancheng);
		tv_jintidingdan = (TextView) findViewById(R.id.tv_jintidingdan);
		tc_dingdanzongshu_month = (TextView) findViewById(R.id.tc_dingdanzongshu_month);
		tv_lishiwancheng = (TextView) findViewById(R.id.tv_lishiwancheng);
		m_scroll = (ScrollView) findViewById(R.id.index_scroll);

		m_scroll.setVisibility(View.GONE);
		viewPager = (AutoScrollViewPager) findViewById(R.id.index_viewpage);
		LayoutParams pageParms = viewPager.getLayoutParams();
		pageParms.width = Tools.M_SCREEN_WIDTH;
		pageParms.height = Tools.M_SCREEN_WIDTH / 2;
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
		
		iv_phone = (ImageView) findViewById(R.id.iv_phone);
		iv_logo = (ImageView) findViewById(R.id.img_head);
//		iv_bg = (ImageView) findViewById(R.id.iv_bg);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_edit:
			Intent intent_edit = new Intent(this,B_1_1_Edit.class);
			intent_edit.putExtra("store_name", store_name);
			intent_edit.putExtra("store_address",store_address);
			intent_edit.putExtra("area_info",area_info);
			intent_edit.putExtra("store_phone",store_phone);
			intent_edit.putExtra("logopath",logopath);
			startActivity(intent_edit);
			break;
		case R.id.iv_phone:
	         // 传入服务， parse（）解析号码
            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store_phone));
            // 通知activtity处理传入的call服务
            startActivity(intent1);
			break;

		default:
			break;
		}
		super.onClick(v);
	}
	JsonHttpResponseHandler res_getFirstList = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Tools.Log("首页="+response);
			JSONObject datas = null;
			String error = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null) {
				try {
					JSONObject store_info = datas.getJSONObject("store_info");
					JSONObject order_info = datas.getJSONObject("order_info");
					
					logopath = store_info.getString("store_label");
					String store_slide = store_info.getString("store_slide");
					JSONArray array = new JSONArray(store_slide);
					store_name = store_info.getString("store_name");
					store_phone = store_info.getString("store_phone");
					Tools.Log("store_phone="+store_phone);
					store_address = store_info.getString("store_address");
					area_info = store_info.getString("area_info");
					
					if (m_scroll.getVisibility() != View.VISIBLE) {
						m_scroll.setVisibility(View.VISIBLE);
					}
					
					JSONArray  jar = store_info.getJSONArray("store_slide");
					// 设置轮播
					viewPager.setAdapter(new IndexPageAdapter1(B1_HomeActivity.this, jar));
					// 设置选中的标识
					LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
					for (int i = 0; i < jar.length(); i++) {
						ImageView pointView = new ImageView(
								B1_HomeActivity.this);
						if (i == 0) {
							pointView
									.setBackgroundResource(R.drawable.feature_point_cur);
						} else
							pointView
									.setBackgroundResource(R.drawable.feature_point);
						pointLinear.addView(pointView);
					}
					// 滚动scrollView到头部
					m_scroll.smoothScrollTo(0, 0);
					
					JSONObject pay_count = order_info.getJSONObject("pay_count");//待发货订单
					JSONObject send_count = order_info.getJSONObject("send_count");//已经发货订单
					JSONObject eval_count  = order_info.getJSONObject("eval_count");//已完成订单
					String today_all_count  = order_info.getString("today_all_count");//今日订单
					String month_eval_count  = order_info.getString("month_eval_count");//本月已完成订单
					String history_eval_count  = order_info.getString("history_eval_count");//历史已完成订单
					
					tv_daifahuo.setText("(今日"+pay_count.getString("today")+"/"+"总数"+pay_count.getString("all")+")");
					tv_yifahuo.setText("(今日"+send_count.getString("today")+"/"+"总数"+send_count.getString("all")+")");
					tv_yiwancheng.setText("(今日"+eval_count.getString("today")+"/"+"总数"+eval_count.getString("all")+")");
					
					tv_jintidingdan.setText("(总数"+today_all_count+")");
					tc_dingdanzongshu_month.setText("(总数"+month_eval_count+")");
					tv_lishiwancheng.setText("(总数"+history_eval_count+")");
					
					tv_storename.setText(store_name);
					tv_address.setText(store_address);
					tv_storeaddress.setText(area_info);
					
					ImageLoader.getInstance().displayImage(logopath, iv_logo);
//					ImageLoader.getInstance().displayImage(array.getString(0), iv_bg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				Tools.Notic(B1_HomeActivity.this, error+"", new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
							Intent intent_to_login = new Intent(B1_HomeActivity.this,B5_1_LoginActivity.class);
							startActivity(intent_to_login);
					}
				});
			}
		}
	};

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
