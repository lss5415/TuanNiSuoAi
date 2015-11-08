package com.ZYKJ.tuannisuoai.UI;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.ChanPinCanShu;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author lss 2015年7月4日选择型号
 * 
 */
public class Sp_a2_XingHao extends BaseActivity {
	// 三个退出
	private LinearLayout ll_sp_a1_back1, tiaomu1_zi, tiaomu2_zi;
	private View view_sp_a1_back2;
	private ImageView im_sp_a1_back3;
	private TextView tv_chanpinprice, tv_kucun, tiaomu1, tiaomu2, tv_qxzlx,
			tv_qxzlx1, tv_addcar,tv_qxzlx_yincang,tv_qxzlx1_yincang,tv_lijigoumai;
	private GridView gview, gview1;
	private String[] iconName;
	// private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
	// "设置", "语音", "天气", "浏览器", "视频" };
	private List<Map<String, Object>> data_list;
	private ArrayAdapter<ChanPinCanShu> sim_adapter;
	private String tiaomu;
	private JSONObject choosejiage,choosexinghao;
	private String goodsid=null;
	private ImageView im_gwtouxiang;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_sp_a2_xinghao);
		initView();
	}

	private void initView() {
		ll_sp_a1_back1 = (LinearLayout) findViewById(R.id.ll_sp_a1_back1);
		view_sp_a1_back2 = (View) findViewById(R.id.view_sp_a1_back2);
		im_sp_a1_back3 = (ImageView) findViewById(R.id.im_sp_a1_back3);
		tv_chanpinprice = (TextView) findViewById(R.id.tv_chanpinprice);
		tv_kucun = (TextView) findViewById(R.id.tv_kucun);
		tv_chanpinprice.setText(getIntent().getStringExtra("chanpinprice"));
		tv_kucun.setText(getIntent().getStringExtra("kucun"));
		tiaomu1 = (TextView) findViewById(R.id.tiaomu1);
		tiaomu2 = (TextView) findViewById(R.id.tiaomu2);
		tiaomu1_zi = (LinearLayout) findViewById(R.id.tiaomu1_zi);
		tiaomu2_zi = (LinearLayout) findViewById(R.id.tiaomu2_zi);
		gview = (GridView) findViewById(R.id.gview);
		gview1 = (GridView) findViewById(R.id.gview1);
		tv_qxzlx = (TextView) findViewById(R.id.tv_qxzlx);
		tv_qxzlx1 = (TextView) findViewById(R.id.tv_qxzlx1);
		tv_qxzlx_yincang = (TextView) findViewById(R.id.tv_qxzlx_yincang);
		tv_qxzlx1_yincang = (TextView) findViewById(R.id.tv_qxzlx1_yincang);
		tv_lijigoumai = (TextView) findViewById(R.id.tv_lijigoumai);
		tiaomu = getIntent().getStringExtra("tiaomu");
		tv_addcar = (TextView) findViewById(R.id.tv_addcar);
		im_gwtouxiang = (ImageView)findViewById(R.id.im_gwtouxiang);
		try {
			ImageLoader.getInstance().displayImage(getIntent().getStringExtra("aaa"), im_gwtouxiang);			
		} catch (Exception e) {
			
		}
		if (tiaomu.equals("1")) {
			tiaomu1.setVisibility(View.VISIBLE);
			tiaomu1_zi.setVisibility(View.VISIBLE);
			tiaomu1.setText(getIntent().getStringExtra("fenlei1"));
			String strarry = getIntent().getStringExtra("arry1");
			final List<ChanPinCanShu> cpcs = JSONArray.parseArray(strarry,
					ChanPinCanShu.class);
			sim_adapter = new ArrayAdapter<ChanPinCanShu>(this,
					R.layout.itemss, R.id.text, cpcs);
			// 配置适配器
			gview.setAdapter(sim_adapter);
			gview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					tv_qxzlx.setText(tiaomu1.getText() + ":"
							+ cpcs.get(position).getName());
					tv_qxzlx_yincang.setText(cpcs.get(position).getId());
					String a = tv_qxzlx_yincang.getText().toString();
					try {
						choosejiage = new JSONObject(getIntent().getStringExtra("choosejiage"));
						JSONObject obj = choosejiage.getJSONObject(a);
						String goods_storage = "库存:"+ obj.getString("goods_storage");
						String goods_price = "￥"+obj.getString("goods_price");
						tv_chanpinprice.setText(goods_price);
						tv_kucun.setText(goods_storage);
					} catch (org.json.JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					Toast.makeText(getApplicationContext(),
//							cpcs.get(position).getId(), Toast.LENGTH_LONG)
//							.show();
				}
			});

		}
		if (tiaomu.equals("2")) {
			tiaomu1.setVisibility(View.VISIBLE);
			tiaomu2.setVisibility(View.VISIBLE);
			tiaomu1_zi.setVisibility(View.VISIBLE);
			tiaomu2_zi.setVisibility(View.VISIBLE);
			tiaomu1.setText(getIntent().getStringExtra("fenlei1"));
			tiaomu2.setText(getIntent().getStringExtra("fenlei2"));

			String strarry = getIntent().getStringExtra("arry1");
			final List<ChanPinCanShu> cpcs = JSONArray.parseArray(strarry,
					ChanPinCanShu.class);
			sim_adapter = new ArrayAdapter<ChanPinCanShu>(this,
					R.layout.itemss, R.id.text, cpcs);
			// 配置适配器
			gview.setAdapter(sim_adapter);
			gview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					tv_qxzlx.setText(tiaomu1.getText() + ":"
							+ cpcs.get(position).getName());
					tv_qxzlx_yincang.setText(cpcs.get(position).getId());
					if (tv_qxzlx1_yincang.getText().length()>0) {
						String a = tv_qxzlx_yincang.getText().toString();
						String b = tv_qxzlx1_yincang.getText().toString();
						String c = a+"|"+b;
						try {
							choosejiage = new JSONObject(getIntent().getStringExtra("choosejiage"));
							JSONObject obj = choosejiage.getJSONObject(c);
							String goods_storage = "库存:"+ obj.getString("goods_storage");
							String goods_price = "￥"+obj.getString("goods_price");
							tv_chanpinprice.setText(goods_price);
							tv_kucun.setText(goods_storage);
						} catch (org.json.JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {

					}
					
				}
			});

			String strarry1 = getIntent().getStringExtra("arry2");
			final List<ChanPinCanShu> cpcs1 = JSONArray.parseArray(strarry1,
					ChanPinCanShu.class);
			sim_adapter = new ArrayAdapter<ChanPinCanShu>(this,
					R.layout.itemss, R.id.text, cpcs1);
			// 配置适配器
			gview1.setAdapter(sim_adapter);
			gview1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					tv_qxzlx1.setText(tiaomu2.getText() + ":"
							+ cpcs1.get(position).getName());
					tv_qxzlx1_yincang.setText(cpcs1.get(position).getId());

					if (tv_qxzlx_yincang.getText().length()>0) {
						String a = tv_qxzlx_yincang.getText().toString();
						String b = tv_qxzlx1_yincang.getText().toString();
						String c = a+"|"+b;
						try {
							choosejiage = new JSONObject(getIntent().getStringExtra("choosejiage"));
							JSONObject obj = choosejiage.getJSONObject(c);
							String goods_storage = "库存:"+ obj.getString("goods_storage");
							String goods_price = "￥"+obj.getString("goods_price");
							tv_chanpinprice.setText(goods_price);
							tv_kucun.setText(goods_storage);
						} catch (org.json.JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						
					}
				}
			});

		}
		setListener(ll_sp_a1_back1, view_sp_a1_back2, im_sp_a1_back3, tv_addcar,tv_lijigoumai);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_sp_a1_back1:
			//xzhdgg 选择后的规格
			Intent intent = new Intent(Sp_a2_XingHao.this,Sp_GoodsInfoActivity.class);
			intent.putExtra("tv_qxzlx1", tv_qxzlx.getText().toString());
			intent.putExtra("tv_qxzlx2", tv_qxzlx1.getText().toString());
			intent.putExtra("xzhdgg", goodsid);
			intent.putExtra("tv_qxzlx_yincang", tv_qxzlx_yincang.getText().toString()+"|"+tv_qxzlx1_yincang.getText().toString());
			intent.putExtra("price", tv_chanpinprice.getText().toString());
			Sp_a2_XingHao.this.setResult(Activity.RESULT_OK, intent);
			Sp_a2_XingHao.this.finish();
			this.overridePendingTransition(R.anim.activity_close, 0);
			break;
		case R.id.view_sp_a1_back2:
			Intent intent1 = new Intent(Sp_a2_XingHao.this,Sp_GoodsInfoActivity.class);
			intent1.putExtra("tv_qxzlx1", tv_qxzlx.getText().toString());
			intent1.putExtra("tv_qxzlx2", tv_qxzlx1.getText().toString());
			intent1.putExtra("xzhdgg", goodsid);
			intent1.putExtra("tv_qxzlx_yincang", tv_qxzlx_yincang.getText().toString()+"|"+tv_qxzlx1_yincang.getText().toString());
			Sp_a2_XingHao.this.setResult(Activity.RESULT_OK, intent1);
			Sp_a2_XingHao.this.finish();
			break;
		case R.id.im_sp_a1_back3:
			Intent intent2 = new Intent(Sp_a2_XingHao.this,Sp_GoodsInfoActivity.class);
			intent2.putExtra("tv_qxzlx1", tv_qxzlx.getText().toString());
			intent2.putExtra("tv_qxzlx2", tv_qxzlx1.getText().toString());
			intent2.putExtra("xzhdgg", goodsid);
			intent2.putExtra("tv_qxzlx_yincang", tv_qxzlx_yincang.getText().toString()+"|"+tv_qxzlx1_yincang.getText().toString());
			Sp_a2_XingHao.this.setResult(Activity.RESULT_OK, intent2);
			Sp_a2_XingHao.this.finish();
			break;
		case R.id.tv_addcar:
			if (tiaomu.equals("1")) {//一个分类
				if (tv_qxzlx.getText().length() > 0) {
					try {
						choosexinghao=new JSONObject(getIntent().getStringExtra("choosexinghao"));
						String a = tv_qxzlx_yincang.getText().toString();
						goodsid = choosexinghao.getString(a);
						
					} catch (org.json.JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpUtils.getAddGouWu(res_addgouwu, getSharedPreferenceValue("key"),goodsid,"1");
				} else {
					Toast.makeText(getApplicationContext(), "请选择型号",
							Toast.LENGTH_LONG).show();
				}
			} else if (tiaomu.equals("2")) {//两个分类
				if (tv_qxzlx.getText().length() > 0) {
					if (tv_qxzlx1.getText().length() > 0) {
						try {
//							choosejiage = new JSONObject(getIntent().getStringExtra("choosejiage"));
							choosexinghao=new JSONObject(getIntent().getStringExtra("choosexinghao"));
							String a = tv_qxzlx_yincang.getText().toString();
							String b = tv_qxzlx1_yincang.getText().toString();
							String c = a+"|"+b;
							goodsid = choosexinghao.getString(c);
							
						} catch (org.json.JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//加入购物车
						HttpUtils.getAddGouWu(res_addgouwu, getSharedPreferenceValue("key"),goodsid,"1");
					} else {
						Toast.makeText(getApplicationContext(),
								"请选择型号2", Toast.LENGTH_LONG).show();
					}
				}else {
					Toast.makeText(getApplicationContext(), "请选择型号",
							Toast.LENGTH_LONG).show();
				}
			} else {//没有分类，直接加入购物车
				//加入购物车
				HttpUtils.getAddGouWu(res_addgouwu, getSharedPreferenceValue("key"),getIntent().getStringExtra("goodsid"),"1");
			}
			break;
		case R.id.tv_lijigoumai:
			if (tiaomu.equals("1")) {//一个分类
				if (tv_qxzlx.getText().length() > 0) {
					try {
						choosexinghao=new JSONObject(getIntent().getStringExtra("choosexinghao"));
						String a = tv_qxzlx_yincang.getText().toString();
						goodsid = choosexinghao.getString(a);
						Intent itmrhd = new Intent();
						itmrhd.setClass(Sp_a2_XingHao.this, JieSuan1Activity.class);
						String ae = tv_chanpinprice.getText().toString();
						itmrhd.putExtra("allpri", ae.substring(1, ae.length()-1));
						itmrhd.putExtra("xzhdgg", goodsid);
						startActivity(itmrhd);
					} catch (org.json.JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					HttpUtils.getAddGouWu(res_addgouwu, getSharedPreferenceValue("key"),goodsid,"1");
				} else {
					Toast.makeText(getApplicationContext(), "请选择型号",
							Toast.LENGTH_LONG).show();
				}
			} else if (tiaomu.equals("2")) {//两个分类
				if (tv_qxzlx.getText().length() > 0) {
					if (tv_qxzlx1.getText().length() > 0) {
						try {
							choosexinghao=new JSONObject(getIntent().getStringExtra("choosexinghao"));
							String a = tv_qxzlx_yincang.getText().toString();
							String b = tv_qxzlx1_yincang.getText().toString();
							String c = a+"|"+b;
							goodsid = choosexinghao.getString(c);
							Intent itmrhd = new Intent();
							itmrhd.setClass(Sp_a2_XingHao.this, JieSuan1Activity.class);
							String aex = tv_chanpinprice.getText().toString();
							itmrhd.putExtra("allpri", aex.substring(1, aex.length()-1));
							itmrhd.putExtra("xzhdgg", goodsid);
							startActivity(itmrhd);
							
						} catch (org.json.JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//加入购物车
//						HttpUtils.getAddGouWu(res_addgouwu, getSharedPreferenceValue("key"),goodsid,"1");
					} else {
						Toast.makeText(getApplicationContext(),
								"请选择型号2", Toast.LENGTH_LONG).show();
					}
				}else {
					Toast.makeText(getApplicationContext(), "请选择型号",
							Toast.LENGTH_LONG).show();
				}
			} else {//没有分类，直接购买
				Intent itmrhd = new Intent();
				itmrhd.setClass(Sp_a2_XingHao.this, JieSuan1Activity.class);
				String a = tv_chanpinprice.getText().toString();
				itmrhd.putExtra("allpri", a.substring(1, a.length()-1));
				itmrhd.putExtra("xzhdgg", getIntent().getStringExtra("goodsid"));
				startActivity(itmrhd);
			}
			break;
		default:
			break;
		}
	}

	JsonHttpResponseHandler res_addgouwu = new JsonHttpResponseHandler()
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
				 error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();		
				Intent itgwc = new Intent(Sp_a2_XingHao.this,
						B3_ShoppingCartActivity.class);
				startActivity(itgwc);					
			}
			else//失败 
			{
				Toast.makeText(getApplicationContext(), "添加失败，"+error, Toast.LENGTH_LONG).show();
			}
		}
	};
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		// 关闭窗体动画显示
		this.overridePendingTransition(R.anim.activity_close, 0);
	}
}