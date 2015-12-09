package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.IndexPageAdapter1;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author lss 2015年7月1日 商品详情
 * 
 */
public class Sp_GoodsInfoActivity extends BaseActivity {
	// 标题
	private TextView tv_sp_title;
	// 返回
	private ImageView im_sp_back;
	// 购物车
	private ImageView im_sp_gouwuche;
	// List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	String key, goods_id;
	String tv_qxzlx = "", tv_qxzlx1 = "", tv_qxzlx2 = "";
	// 商品名称
	private TextView tv_sp_spname;
	// 销售价
	private TextView tv_xiaoshoujia;
	// 市场价
	private TextView tv_shichangjia;
	// 所属二级分类
	private TextView tv_sp_ssejfl;
	// 已售件数
	private TextView tv_yishoujian;
	// 浏览量
//	private TextView tv_liulancount;
	// 选择规格类型
	private RelativeLayout rl_chooseleixing;
	// 宝贝评价(1731)
	private TextView tv_sp_babypingjia;
	// 总评分：
	private TextView tv_zongpinfen;
	// 用户头像
	private ImageView im_userimage;
	// 用户名称
	private TextView tv_username;
	// 评星
	private RatingBar xiangqing_rating_bar;
	// 商品评价
	private TextView tv_sp_pingjia;
	// 产品参数
	private TextView tv_chanpincanshu;
	// 查看更多评价
	private LinearLayout ll_ckgdpj;
	// 继续拖动，查看图文详情
	private LinearLayout ll_chakantuwen;
	// 评价数
	private String pingjia;
	// 加入购物车
	private LinearLayout ll_sp_addincar;
	// 商品分享
	private LinearLayout ll_goods_fenxiang;
	private String chanpinprice, kucun, xsprice;
	private JSONArray leixing1=null;
	private int pj = 0;
	private JSONObject jsonitemz, xuanxiang;
	private String choosejiage, choosexinghao;
	// 选择规格类型
	private TextView tv_xzgglx;
	// 立即购买
	private LinearLayout ll_ljgm;
	private String goodsid;
	private String xzhdgg = null;// 选择后的goodsid
	private String tv_qxzlx_yincang = null;// 隐藏的控件存了选择了的型号的id
	private String price;
	private Intent itaddcar1;
	private LinearLayout ll_shoucang, ll_dianpu, ll_pinglun;// 收藏,店铺,评论
	private String store_id,aaa="";//店铺ID，选择规格的头像
	/** 滚动层 */
	private ScrollView m_scroll;
	private AutoScrollViewPager viewPager;
	/** 当前的位置 */
	private int now_pos = 0;
	private WebView webView;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_shangpinxiangqing);
		initView();
		HttpUtils.getGoodsInfo(res_goodsinfo, goods_id);
	}

	private void initView() {
		tv_sp_title = (TextView) findViewById(R.id.tv_sp_title);
		im_sp_back = (ImageView) findViewById(R.id.im_sp_back);
		im_sp_gouwuche = (ImageView) findViewById(R.id.im_sp_gouwuche);
		goods_id = getIntent().getStringExtra("goods_id");
		tv_sp_spname = (TextView) findViewById(R.id.tv_sp_spname);
		tv_xiaoshoujia = (TextView) findViewById(R.id.tv_xiaoshoujia);
		tv_shichangjia = (TextView) findViewById(R.id.tv_shichangjia);
		tv_sp_ssejfl = (TextView) findViewById(R.id.tv_sp_ssejfl);
		tv_yishoujian = (TextView) findViewById(R.id.tv_yishoujian);
//		tv_liulancount = (TextView) findViewById(R.id.tv_liulancount);
		rl_chooseleixing = (RelativeLayout) findViewById(R.id.rl_chooseleixing);
		tv_sp_babypingjia = (TextView) findViewById(R.id.tv_sp_babypingjia);
		tv_zongpinfen = (TextView) findViewById(R.id.tv_zongpinfen);
		im_userimage = (ImageView) findViewById(R.id.im_userimage);
		tv_username = (TextView) findViewById(R.id.tv_username);
		xiangqing_rating_bar = (RatingBar) findViewById(R.id.xiangqing_rating_bar);
		tv_sp_pingjia = (TextView) findViewById(R.id.tv_sp_pingjia);
		tv_chanpincanshu = (TextView) findViewById(R.id.tv_chanpincanshu);
		ll_ckgdpj = (LinearLayout) findViewById(R.id.ll_ckgdpj);
		ll_chakantuwen = (LinearLayout) findViewById(R.id.ll_chakantuwen);
		ll_sp_addincar = (LinearLayout) findViewById(R.id.ll_sp_addincar);
		ll_goods_fenxiang = (LinearLayout) findViewById(R.id.ll_goods_fenxiang);
		ll_ljgm = (LinearLayout) findViewById(R.id.ll_ljgm);
		ll_pinglun = (LinearLayout) findViewById(R.id.ll_pinglun);
		// key = getSharedPreferenceValue("key");
		tv_xzgglx = (TextView) findViewById(R.id.tv_xzgglx);
		tv_shichangjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		ll_shoucang = (LinearLayout) findViewById(R.id.ll_shoucang);
		ll_dianpu = (LinearLayout) findViewById(R.id.ll_dianpu);
		m_scroll = (ScrollView) findViewById(R.id.index_scroll);

		webView = (WebView) findViewById(R.id.webView);

		String url = "http://115.28.21.137:89/mobile/index.php?act=goods&op=goods_body&goods_id="
				+ goods_id;
		webView.loadUrl(url);
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;

			}
		});

		m_scroll.setVisibility(View.GONE);
		viewPager = (AutoScrollViewPager) findViewById(R.id.index_viewpage);
		LayoutParams pageParms = viewPager.getLayoutParams();
		pageParms.width = Tools.M_SCREEN_WIDTH;
		pageParms.height = Tools.M_SCREEN_WIDTH;

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

		setListener(im_sp_back, im_sp_gouwuche, ll_ckgdpj, ll_sp_addincar,
				rl_chooseleixing, ll_goods_fenxiang, ll_ljgm, ll_shoucang,
				ll_dianpu);
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

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_sp_back:
			Sp_GoodsInfoActivity.this.finish();
			break;
		case R.id.im_sp_gouwuche:
			if (isLogin()) {
				Intent itgwc = new Intent(Sp_GoodsInfoActivity.this,
						B3_ShoppingCartActivity.class);
				startActivity(itgwc);
			} else {
				Intent intent_login = new Intent();
				intent_login.setClass(this, B5_1_LoginActivity.class);
				startActivity(intent_login);
			}
			break;
		case R.id.ll_ckgdpj:
			Intent iteckpj = new Intent(Sp_GoodsInfoActivity.this,
					Sp_a1_GoodsEvaluation.class);
			iteckpj.putExtra("goods_id", goods_id);
			iteckpj.putExtra("sp_a1_title", pingjia);
			startActivity(iteckpj);
			break;
		case R.id.ll_sp_addincar:
			if (isLogin()) {
				if (leixing1 == null) {
					HttpUtils.getAddGouWu(res_addgouwu,
							getSharedPreferenceValue("key"), goods_id, "1");
				} else {
					XuanZeLeiXing();
				}
			} else {
				Intent intent_login = new Intent();
				intent_login.setClass(this, B5_1_LoginActivity.class);
				startActivity(intent_login);
			}
			break;
		case R.id.ll_ljgm:// 立即购买
			if (isLogin()) {
				if (tv_xzgglx.getText().equals("选择规格类型")
						|| tv_xzgglx.getText().equals("")) {
					if (leixing1 == null) {
						Intent itmrhd = new Intent();
						itmrhd.setClass(Sp_GoodsInfoActivity.this,
								JieSuan1Activity.class);
						itmrhd.putExtra("allpri", xsprice);
						itmrhd.putExtra("xzhdgg", goods_id);
						startActivity(itmrhd);
					} else {
						XuanZeLeiXing();
					}
				} else {
					if (leixing1.length() == 1) {
						try {
							String a = tv_qxzlx_yincang.substring(0,
									tv_qxzlx_yincang.length() - 1);
							JSONObject jso = new JSONObject(choosexinghao);
							goodsid = jso.getString(a);
							Intent itmrhd = new Intent();
							itmrhd.setClass(Sp_GoodsInfoActivity.this,
									JieSuan1Activity.class);
							itmrhd.putExtra("allpri",
									price.substring(1, price.length() - 1));
							itmrhd.putExtra("xzhdgg", xzhdgg);
							startActivity(itmrhd);
						} catch (Exception e) {
						}
					}
					if (leixing1.length() == 2) {
						if (tv_qxzlx1 == null || tv_qxzlx1 == ""
								|| tv_qxzlx2 == null || tv_qxzlx2 == "") {
							XuanZeLeiXing();
						} else {
							// 立即购买
							try {
								String a = tv_qxzlx_yincang;
								JSONObject jso = new JSONObject(choosexinghao);
								goodsid = jso.getString(a);
								Intent itmrhd = new Intent();
								itmrhd.setClass(Sp_GoodsInfoActivity.this,
										JieSuan1Activity.class);
								itmrhd.putExtra("allpri",
										price.substring(1, price.length() - 1));
								itmrhd.putExtra("xzhdgg", goodsid);
								startActivity(itmrhd);

							} catch (org.json.JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			} else {
				Intent intent_login = new Intent();
				intent_login.setClass(this, B5_1_LoginActivity.class);
				startActivity(intent_login);
			}
			break;
		case R.id.rl_chooseleixing:
			if (isLogin()) {
				XuanZeLeiXing();
			} else {
				Intent intent_login = new Intent();
				intent_login.setClass(this, B5_1_LoginActivity.class);
				startActivity(intent_login);
			}
			break;
		case R.id.ll_goods_fenxiang:
			// 设置分享内容
			String fxnr = "团你所爱-消费者的满意生活购物网站，团购的好选择。时装、母婴、数码家电、美妆、日用百货等，方寸之间尽享购物。天天特价、晒单圈、猜你喜欢、每日好店，精彩推送您最感兴趣的。便宜实惠，品质保证，服务专业，团你所爱，我的生活。";
			String fxtp = "http://www.pgyer.com/x8bv";
			FenXiang fx = new FenXiang(getApplicationContext(),
					Sp_GoodsInfoActivity.this, fxnr, fxtp);
			break;
		case R.id.ll_shoucang:
			if (isLogin()) {
				HttpUtils.getShouCang(res_shoucang,
						getSharedPreferenceValue("key"), goods_id);
			} else {
				Intent intent_login = new Intent();
				intent_login.setClass(this, B5_1_LoginActivity.class);
				startActivity(intent_login);
			}
			break;
		case R.id.ll_dianpu:
			Intent intent = new Intent();
			intent.putExtra("store_id", store_id);
			intent.setClass(Sp_GoodsInfoActivity.this,
					BX_DianPuXiangQingActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void XuanZeLeiXing() {
		itaddcar1 = new Intent(Sp_GoodsInfoActivity.this, Sp_a2_XingHao.class);
		if (leixing1==null) {
			Toast.makeText(Sp_GoodsInfoActivity.this,"此产品没有规格，无需选择", Toast.LENGTH_LONG).show();
		}else {
			if (leixing1.length() == 1) {
				LeiXing1();
			}
			if (leixing1.length() == 2) {
				LeiXing2();
			}

			itaddcar1.putExtra("chanpinprice", chanpinprice);
			itaddcar1.putExtra("kucun", kucun);
			itaddcar1.putExtra("goodsid", goodsid);
			itaddcar1.putExtra("choosejiage", choosejiage);
			itaddcar1.putExtra("choosexinghao", choosexinghao);
			itaddcar1.putExtra("aaa", aaa);
			Sp_GoodsInfoActivity.this.startActivityForResult(itaddcar1, 0);
			Sp_GoodsInfoActivity.this.overridePendingTransition(
					R.anim.activity_open, 0);
		}
	}

	public void LeiXing1() {
		itaddcar1.putExtra("tiaomu", "1");// arr.getJSONObject(0)
		try {
			itaddcar1.putExtra("fenlei1",
					leixing1.getJSONObject(0).getString("name").toString());

			// JSONArray jsonarray = xuanxiang.getJSONArray("1");
			JSONArray jsonarray = xuanxiang.getJSONArray(leixing1
					.getJSONObject(0).getString("id").toString());
			/*
			 * List<String> list = new ArrayList<String>(); for (int i = 0; i <
			 * jsonarray.length(); i++) { try {
			 * list.add(jsonarray.getString(i)); } catch (JSONException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); } }
			 */
			itaddcar1.putExtra("arry1", jsonarray.toString());

			// String[] stringArray = list.toArray(new String[list.size()]);
			// itaddcar1.putExtra("arry1", stringArray);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LeiXing2() {
		itaddcar1.putExtra("tiaomu", "2");
		try {
			// Toast.makeText(Sp_GoodsInfoActivity.this,
			// leixing1.getString("1").toString(), Toast.LENGTH_LONG).show();
			itaddcar1.putExtra("fenlei1",
					leixing1.getJSONObject(0).getString("name").toString());
			itaddcar1.putExtra("fenlei2",
					leixing1.getJSONObject(1).getString("name").toString());

			JSONArray jsonarray1 = xuanxiang.getJSONArray(leixing1
					.getJSONObject(0).getString("id").toString());

			itaddcar1.putExtra("arry1", jsonarray1.toString());

			JSONArray jsonarray2 = xuanxiang.getJSONArray(leixing1
					.getJSONObject(1).getString("id").toString());
			itaddcar1.putExtra("arry2", jsonarray2.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	JsonHttpResponseHandler res_goodsinfo = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_getAddress=" + response);
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				try {
					// 每日好店
					JSONObject jsonItem = datas.getJSONObject("goods_info");
					// JSONObject jsonItem = array.getJSONObject(0);
					try {
						jsonitemz = datas.getJSONObject("evaluate");
					} catch (Exception e) {
						pj = 1;
					}

					// JSONObject jsonitemz = arrayz.getJSONObject(0);
					// 商品名称
					tv_sp_spname.setText(jsonItem.getString("goods_name"));
					// 销售价
					xsprice =jsonItem.getString("goods_promotion_price");
					tv_xiaoshoujia.setText( "￥" + xsprice);
					store_id = jsonItem.getString("store_id");
					// 库存
					kucun = "库存：" + jsonItem.getString("goods_storage") + "件";
					// 市场价tv_xiaoshoujia
					chanpinprice = "￥" + jsonItem.getString("goods_price");
					tv_shichangjia.setText(chanpinprice);
					// 所属二级分类
					tv_sp_ssejfl.setText(jsonItem.getString("gc_name"));
					// 已售件数
					tv_yishoujian.setText("已售"
							+ jsonItem.getString("goods_salenum") + "件");
					// 浏览量
//					tv_liulancount.setText("浏览 "
//							+ jsonItem.getString("goods_click"));
					// 选择规格类型
					// rl_chooseleixing
					// 宝贝评价(1731)
					pingjia = "评价(" + jsonItem.getString("evaluation_count")
							+ ")";
					tv_sp_babypingjia.setText("宝贝评价：（"
							+ jsonItem.getString("evaluation_count") + "）");
					// 总评分：
					tv_zongpinfen.setText("总评分："
							+ jsonItem.getString("evaluation_good_star"));
					if (pj == 1) {
						ll_pinglun.setVisibility(View.GONE);
					} else {
						ll_pinglun.setVisibility(View.VISIBLE);
						// 用户头像
						ImageLoader.getInstance().displayImage(
								(String) jsonitemz.getString("geval_avatar"),
								im_userimage, ImageOptions.getOpstion(),
								animateFirstListener);
						// 用户名称
						tv_username.setText(jsonitemz
								.getString("geval_frommembername"));
						// 评星
						xiangqing_rating_bar
								.setRating(Float.parseFloat(jsonitemz
										.getString("geval_scores")));
						// 商品评价
						tv_sp_pingjia.setText(jsonitemz
								.getString("geval_content"));
						// 产品参数
						tv_chanpincanshu.setText(jsonitemz
								.getString("geval_spec"));
					}
					// 查看更多评价
					try {
						leixing1 = jsonItem.getJSONArray("spec_name");
						xuanxiang = jsonItem.getJSONObject("spec_value");
					} catch (Exception e) {
						
					}
					choosexinghao = datas.getJSONObject("spec_list").toString();
					choosejiage = datas.getJSONObject("spec_list_goods")
							.toString();

					if (m_scroll.getVisibility() != View.VISIBLE) {
						m_scroll.setVisibility(View.VISIBLE);
					}

					try {
						JSONObject datsj = datas.getJSONObject("goods_image");
						// 设置轮播
						viewPager.setAdapter(new IndexPageAdapter1(
								Sp_GoodsInfoActivity.this, datsj));
						try {
							aaa = datsj.getString("0").toString();
						} catch (Exception e) {
							
						}
						// 设置选中的标识
						LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
						for (int i = 0; i < datsj.length(); i++) {
							ImageView pointView = new ImageView(
									Sp_GoodsInfoActivity.this);
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
					} catch (Exception e) {
						e.printStackTrace();
					}

					// ll_ckgdpj
					// 继续拖动，查看图文详情
					// ll_chankantuwen
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}/* circle */
			else// 失败
			{
				Tools.Log("res_Points_error=" + error + "");
				// Tools.Notic(B5_MyActivity.this, error+"", null);
			}

		}
	};

	JsonHttpResponseHandler res_shoucang = new JsonHttpResponseHandler() {
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_getAddress=" + response);
			String error = null;
			JSONObject datas = null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				Toast.makeText(Sp_GoodsInfoActivity.this, "收藏成功",
						Toast.LENGTH_LONG).show();
			} else// 失败
			{
				Toast.makeText(Sp_GoodsInfoActivity.this, error,
						Toast.LENGTH_LONG).show();
			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			tv_qxzlx1 = data.getStringExtra("tv_qxzlx1");
			tv_qxzlx2 = data.getStringExtra("tv_qxzlx2");
			tv_qxzlx = tv_qxzlx1 + tv_qxzlx2;
			tv_xzgglx.setText(tv_qxzlx);
			xzhdgg = data.getStringExtra("xzhdgg");
			tv_qxzlx_yincang = data.getStringExtra("tv_qxzlx_yincang");
			price = data.getStringExtra("price");
			break;
		default:
			break;
		}
	}

	JsonHttpResponseHandler res_addgouwu = new JsonHttpResponseHandler() {
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
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error == null)// 成功
			{
				Toast.makeText(getApplicationContext(), "添加成功",
						Toast.LENGTH_LONG).show();
				Intent itgwc = new Intent(Sp_GoodsInfoActivity.this,
						B3_ShoppingCartActivity.class);
				startActivity(itgwc);
			} else// 失败
			{
				Toast.makeText(getApplicationContext(), "添加失败，" + error,
						Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.destroy();
	}

	public boolean isLogin() {
		if (getSharedPreferenceValue("userid").equals("")) {
			return false;
		} else {
			return true;
		}
	}

}
