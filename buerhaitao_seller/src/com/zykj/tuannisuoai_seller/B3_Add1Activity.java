package com.zykj.tuannisuoai_seller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.tuannisuoai_seller.adapter.GridAdapter;
import com.zykj.tuannisuoai_seller.adapter.GridAdapter.SaveModel;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.AutoListView;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.Tools;
import com.zykj.tuannisuoai_seller.utils.UIDialog;

/**
 * @author lss 2015年7月28日 商品详情编辑
 * 
 */
public class B3_Add1Activity extends BaseActivity implements SaveModel {
	// 选择分类
	private LinearLayout rl_fenlei;
	// 型号表
	private AutoListView lv_grid;
	// 型号1,2
	private LinearLayout ll_xh1, ll_xh2;
	private TextView tv_xinghao1, tv_xinghao2, tv_xinghao11, tv_xinghao22,TextView06;
	String[] str;
	String[] str1;
	String[] str2;
	String[] str3;
	String[] arr;
	String[] arrx;
	private String key;
	// 分类内选中的gc_id
	private String gc_id = "";
	private String gc_name = "";
	private int jia = 0;
	private EditText et_xh1, et_xh2;
	private TextView tv_scbg;
	private List<XingHaoModel> data = new ArrayList<XingHaoModel>();
	private TextView tv_save;
	private String cate_id, cate_name, g_name, spec, sp_name, sp_value,
			type_id, goods_jingle, image_path, image_all;
	private EditText et_chanpinming, et_chanpinjianjie;
	List<XingHaoModel> datamodel = new ArrayList<XingHaoModel>();
	int state = 0;// 默认=0，型号1 state=1,型号2 state=2,型号12 state = 3
	ImageView iv_1,iv_2,iv_3, iv_4, iv_5;
//	String tupian = "04920800982552959.jpg";
//	String tupian1 = "04920800982552959.jpg";
	String imageString="";
	Bitmap photo;
	int first = 0; //第一次上传图片
	String imageString1="";
	int setImageTag = 0;
	private static final int IV_1 = 201;
	private static final int IV_2 = 202;
	private static final int IV_3 = 203;
	private static final int IV_4 = 204;
	private static final int IV_5 = 205;

	/**
	 * 上传头像的字段
	 */
	private String timeString;
	private String filename;
	private String cutnameString;
	private static final int PAIZHAO = 14;
	private static final int XIANGCE = 15;
	private static final int CAIJIAN = 16;
	private String goods_commonid;
	private String newxinghao1="",newxinghao2="";
	private TextView tv_cancel;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_add);
		initview();
	}

	public void initview() {
		rl_fenlei = (LinearLayout) findViewById(R.id.rl_fenlei);
		lv_grid = (AutoListView) findViewById(R.id.lv_grid);
		ll_xh1 = (LinearLayout) findViewById(R.id.ll_xh1);
		ll_xh2 = (LinearLayout) findViewById(R.id.ll_xh2);
		tv_xinghao1 = (TextView) findViewById(R.id.tv_xinghao1);
		tv_xinghao2 = (TextView) findViewById(R.id.tv_xinghao2);
		tv_xinghao11 = (TextView) findViewById(R.id.tv_xinghao11);
		tv_xinghao22 = (TextView) findViewById(R.id.tv_xinghao22);
		et_xh1 = (EditText) findViewById(R.id.et_xh1);
		et_xh2 = (EditText) findViewById(R.id.et_xh2);
		tv_scbg = (TextView) findViewById(R.id.tv_scbg);
		TextView06 = (TextView) findViewById(R.id.TextView06);
		tv_save = (TextView) findViewById(R.id.tv_save);
		et_chanpinming = (EditText) findViewById(R.id.et_chanpinming);
		et_chanpinjianjie = (EditText) findViewById(R.id.et_chanpinjianjie);
		iv_1 = (ImageView) findViewById(R.id.img_1);
		iv_2 = (ImageView) findViewById(R.id.img_2);
		iv_3 = (ImageView) findViewById(R.id.img_3);
		iv_4 = (ImageView) findViewById(R.id.img_4);
		iv_5 = (ImageView) findViewById(R.id.img_5);
		key = getSharedPreferenceValue("key");
		goods_commonid = getIntent().getStringExtra("goods_commonid");
		tv_cancel = (TextView)findViewById(R.id.tv_cancel);

		HttpUtils.getEditProduct(res_getEditProduct,goods_commonid,key);
//		HttpUtils.getEditProduct(res_getEditProduct,goods_commonid,key);

		setListener(rl_fenlei, ll_xh1, ll_xh2, tv_scbg, tv_save, iv_1, iv_2,
				iv_3, iv_4, iv_5,tv_cancel);
	}

	/**
	 * 带来编辑产品的数据
	 */
	JsonHttpResponseHandler res_getEditProduct = new JsonHttpResponseHandler() {

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
				try {
					data.clear();
					JSONObject jobedtproj = datas.getJSONObject("common_info");
					et_chanpinming.setText(jobedtproj.getString("goods_name"));
					et_chanpinjianjie.setText(jobedtproj.getString("goods_jingle"));
					try {
						JSONObject jobspec_value = jobedtproj.getJSONObject("spec_value");
						if (jobspec_value.length()==1) {
							org.json.JSONArray jaspec = jobspec_value.getJSONArray("1");
							for (int i = 0; i < jaspec.length(); i++) {
								JSONObject jsonspec = jaspec.getJSONObject(i);
								newxinghao1 = newxinghao1 + jsonspec.getString("spec_value_name")+",";
							}
							et_xh1.setText(newxinghao1);
						}else if (jobspec_value.length()==2) {
							org.json.JSONArray jaspec = jobspec_value.getJSONArray("1");
							for (int i = 0; i < jaspec.length(); i++) {
								JSONObject jsonspec = jaspec.getJSONObject(i);
								newxinghao1 = newxinghao1 + jsonspec.getString("spec_value_name")+",";
							}
							org.json.JSONArray jaspec1 = jobspec_value.getJSONArray("2");
							for (int i = 0; i < jaspec1.length(); i++) {
								JSONObject jsonspec1 = jaspec1.getJSONObject(i);
								newxinghao2 = newxinghao2 + jsonspec1.getString("spec_value_name")+",";
							}
							et_xh1.setText(newxinghao1);
							et_xh2.setText(newxinghao2);
						}
					} catch (Exception e) {
						
					}
					gc_id = jobedtproj.getString("gc_id");
					gc_name = jobedtproj.getString("gc_name_2");
					TextView06.setText(gc_name);
					
					try {
						org.json.JSONArray jobspec_name = jobedtproj.getJSONArray("spec_name");
						for (int i = 0; i < jobspec_name.length(); i++) {
							JSONObject jsonItem = jobspec_name.getJSONObject(i);
							if (i==0) {
								tv_xinghao1.setText(jsonItem.getString("spec_name"));
								tv_xinghao11.setText(jsonItem.getString("spec_id"));
								state = 1;
							}else if(i==1){
								tv_xinghao2.setText(jsonItem.getString("spec_name"));
								tv_xinghao22.setText(jsonItem.getString("spec_id"));
								state = 3;
							}
						}
						
					} catch (Exception e) {
						state = 0;
					}
					
					try {
						org.json.JSONArray  arry = jobedtproj.getJSONArray("goods_image");
						for (int i = 0; i < arry.length(); i++) {
							String a = arry.getString(i).toString();
							if (i==0) {
								ImageLoader.getInstance().displayImage(arry.getString(i).toString(), iv_1);
								String a1 = arry.getString(i).toString();
								String b = a1.substring(a1.lastIndexOf("/")+1);
								imageString1 = b;
							}else if (i==1) {
								ImageLoader.getInstance().displayImage(arry.getString(i).toString(), iv_2);
								String a1 = arry.getString(i).toString();
								String b = a1.substring(a1.lastIndexOf("/")+1);
								imageString = b; 
							}else if (i==2) {
								ImageLoader.getInstance().displayImage(arry.getString(i).toString(), iv_3);
								String a1 = arry.getString(i).toString();
								String b = a1.substring(a1.lastIndexOf("/")+1);
								imageString = imageString +","+ b; 
							}else if (i==3) {
								ImageLoader.getInstance().displayImage(arry.getString(i).toString(), iv_4);
								String a1 = arry.getString(i).toString();
								String b = a1.substring(a1.lastIndexOf("/")+1);
								imageString = imageString +","+ b; 
							}else if (i==4) {
								ImageLoader.getInstance().displayImage(arry.getString(i).toString(), iv_5);
								String a1 = arry.getString(i).toString();
								String b = a1.substring(a1.lastIndexOf("/")+1);
								imageString = imageString +","+ b; 
							}
						}
					} catch (Exception e) {
						
					}

//					goods_spec_list
					try {
						org.json.JSONArray biaogearry = datas.getJSONArray("goods_spec_list");
						for (int i = 0; i < biaogearry.length(); i++) {
							JSONObject obbiaoge = biaogearry.getJSONObject(i);
							XingHaoModel model = new XingHaoModel();
							try {
								model.setXinghao1(obbiaoge.getString("spec_value_name_1"));
							} catch (Exception e) {
								
							}
							try {
								model.setXinghao2(obbiaoge.getString("spec_value_name_2"));
							} catch (Exception e) {
								// TODO: handle exception
							}
							try {
								model.setGuigezhi1(obbiaoge.getString("spec_value_id_1"));
							} catch (Exception e) {
								// TODO: handle exception
							}
							try {
								model.setGuigezhi2(obbiaoge.getString("spec_value_id_2"));
							} catch (Exception e) {
								// TODO: handle exception
							}
							model.setKucun(obbiaoge.getString("storage"));
							model.setJiage(obbiaoge.getString("price"));
							model.setTejia(obbiaoge.getString("goods_promotion_price"));
							data.add(model);
						}
						if (biaogearry.length()==0) {
							XingHaoModel model = new XingHaoModel();
							model.setXinghao1("");
							model.setXinghao2("");
							model.setKucun(jobedtproj.getString("goods_storage"));
							model.setJiage(jobedtproj.getString("goods_price"));
							model.setTejia(jobedtproj.getString("goods_promotion_price"));
							model.setGuigezhi1("");
							model.setGuigezhi2("");
							data.add(model);
						}
					} catch (Exception e) {
						
					}
					
					
					GridAdapter listAdapter = new GridAdapter(B3_Add1Activity.this, data, B3_Add1Activity.this);
					lv_grid.setAdapter(listAdapter);
					
					
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_fenlei:
			Intent itfenlei = new Intent(B3_Add1Activity.this,
					B2_FenLeiActivity.class);
			startActivityForResult(itfenlei, 0);
			break;
		case R.id.ll_xh1:
			if (gc_id == "" || gc_id == null) {
				Toast.makeText(getApplicationContext(), "请先选择分类",
						Toast.LENGTH_LONG).show();
			} else {
				HttpUtils.getGuiGeList(res_getGuiGeList, key, gc_id);
				if (jia == 0) {
					jia = 1;
				} else {
					new AlertDialog.Builder(B3_Add1Activity.this)
							.setTitle("请选择")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setSingleChoiceItems(str, 0,
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											tv_xinghao1.setText(str[which]);
											tv_xinghao11.setText(str1[which]);
											dialog.dismiss();
										}
									}).setNegativeButton("取消", null).show();
				}
			}
			break;
		case R.id.ll_xh2:
			if (gc_id == "" || gc_id == null) {
				Toast.makeText(getApplicationContext(), "请先选择分类",
						Toast.LENGTH_LONG).show();
			} else {
				HttpUtils.getGuiGeList(res_getGuiGeList, key, gc_id);
				if (jia == 0) {
					jia = 1;
				} else {
					new AlertDialog.Builder(B3_Add1Activity.this)
							.setTitle("请选择")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setSingleChoiceItems(str, 0,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											tv_xinghao2.setText(str[which]);
											tv_xinghao22.setText(str1[which]);
											dialog.dismiss();
										}
									}).setNegativeButton("取消", null).show();
				}
			}
			break;
		case R.id.tv_scbg:
			if (tv_xinghao1.getText().toString().equals("型号")
					&& tv_xinghao2.getText().toString().equals("型号")) {
				data.clear();
				XingHaoModel model = new XingHaoModel();
				model.setXinghao1("");
				model.setXinghao2("");
				model.setKucun("0");
				model.setJiage("0");
				model.setTejia("0");
				data.add(model);
				GridAdapter listAdapter = new GridAdapter(this, data, this);
				state = 0;
				lv_grid.setAdapter(listAdapter);
			} else if (tv_xinghao1.getText().toString().equals("型号")) {
				if (TextUtils.isEmpty(et_xh2.getText())) {
					Toast.makeText(getApplicationContext(), "请填写第二条内的型号",
							Toast.LENGTH_LONG).show();
				} else {// 按照型号2来
					String xinghao2 = et_xh2.getText().toString();
					xinghao2 = xinghao2.replaceAll("，", ",");
					if (xinghao2.substring(xinghao2.length() - 1).toString()
							.equals(",")) {
						String sds = xinghao2.substring(0,
								xinghao2.length() - 1);
						data.clear();
						arrx = xinghao2.split(",");
						HttpUtils.getAddGgz(res_addGgz1xh2, key, sds, gc_id,
								tv_xinghao22.getText().toString());

					} else {
						Toast.makeText(getApplicationContext(),
								"第二条内的型号要以，结尾哦", Toast.LENGTH_LONG).show();
					}
				}
			}

			else if (tv_xinghao2.getText().toString().equals("型号")) {
				if (TextUtils.isEmpty(et_xh1.getText())) {
					Toast.makeText(getApplicationContext(), "请填写第一条内的型号",
							Toast.LENGTH_LONG).show();
				} else {// 按型号1来
					String xinghao1 = et_xh1.getText().toString();
					xinghao1 = xinghao1.replaceAll("，", ",");
					if (xinghao1.substring(xinghao1.length() - 1).toString()
							.equals(",")) {
						String sds = xinghao1.substring(0,
								xinghao1.length() - 1);
						data.clear();
						arr = xinghao1.split(",");
						HttpUtils.getAddGgz(res_addGgzxh1, key, sds, gc_id,
								tv_xinghao11.getText().toString());

					} else {
						Toast.makeText(getApplicationContext(),
								"第一条内的型号要以，结尾哦", Toast.LENGTH_LONG).show();
					}
				}
			}

			else {
				if (TextUtils.isEmpty(et_xh1.getText())) {
					Toast.makeText(getApplicationContext(), "请填写第一条内的型号",
							Toast.LENGTH_LONG).show();
				} else if (TextUtils.isEmpty(et_xh2.getText())) {
					Toast.makeText(getApplicationContext(), "请填写第二条内的型号",
							Toast.LENGTH_LONG).show();
				} else {
					String xinghao1 = et_xh1.getText().toString();
					xinghao1 = xinghao1.replaceAll("，", ",");
					String xinghao2 = et_xh2.getText().toString();
					xinghao2 = xinghao2.replaceAll("，", ",");
					if (xinghao1.substring(xinghao1.length() - 1).toString()
							.equals(",")) {

						String sds = xinghao1.substring(0,
								xinghao1.length() - 1);
						HttpUtils.getAddGgz(res_addGgz, key, sds, gc_id,
								tv_xinghao11.getText().toString());

						arr = xinghao1.split(",");
						if (xinghao2.substring(xinghao2.length() - 1)
								.toString().equals(",")) {

							String sdxs = xinghao2.substring(0,
									xinghao2.length() - 1);
							HttpUtils.getAddGgz(res_addGgz1, key, sdxs, gc_id,
									tv_xinghao22.getText().toString());

							arrx = xinghao2.split(",");
							data.clear();

						} else {
							Toast.makeText(getApplicationContext(),
									"第二条内的型号要以，结尾哦", Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"第一条内的型号要以，结尾哦", Toast.LENGTH_LONG).show();
					}
				}
			}
			break;
		case R.id.tv_save:
			if (TextUtils.isEmpty(et_chanpinming.getText())) {
				Toast.makeText(getApplicationContext(), "请输入产品名称",
						Toast.LENGTH_LONG).show();
			} else if (TextUtils.isEmpty(et_chanpinjianjie.getText())) {
				Toast.makeText(getApplicationContext(), "请输入产品简介",
						Toast.LENGTH_LONG).show();
			} else {
				if (state == 0) {
					// Toast.makeText(getApplicationContext(), "无型号",
					// Toast.LENGTH_LONG).show();
					String cate_id = gc_id;
					String cate_name = gc_name;
					String g_name = et_chanpinming.getText().toString();

					String g_price = datamodel.get(0).getJiage();
					String goods_promotion_price = datamodel.get(0).getTejia();
					String g_storage = datamodel.get(0).getKucun();
					String goods_jingle = et_chanpinjianjie.getText()
							.toString();
					String image_path = imageString1;
					String image_all = imageString;
					// lv_grid.
					HttpUtils.ShangPinFaBuBianji(res_Fabu, key,goods_commonid, cate_id, cate_name,
							g_name, g_price, goods_promotion_price, g_storage,
							goods_jingle, image_path, image_all);
				} else if (state == 1) {
					// Toast.makeText(getApplicationContext(), "型号1",Toast.LENGTH_LONG).show();
					String cate_id = gc_id;
					String cate_name = gc_name;
					String g_name = et_chanpinming.getText().toString();
					// String sp_name 规格名称，参见下方范例
					String jsonStr = "{'" + tv_xinghao11.getText().toString()+ "':'" + tv_xinghao1.getText().toString() + "'}";
					com.alibaba.fastjson.JSONObject sp_name = com.alibaba.fastjson.JSONObject
							.parseObject(jsonStr);

					// String sp_value 规格值，参见下方范例
					String jsonsp_value = "{'"
							+ tv_xinghao11.getText().toString() + "':{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonsp_value = jsonsp_value + "'"
								+ datamodel.get(i).guigezhi1 + "':'"
								+ datamodel.get(i).getXinghao1() + "',";
					}
					jsonsp_value = jsonsp_value.substring(0,
							jsonsp_value.length() - 1)
							+ "}}";
					com.alibaba.fastjson.JSONObject sp_value = com.alibaba.fastjson.JSONObject
							.parseObject(jsonsp_value);

					// String spec 提交的规格，参见下方范例
					String jsonspec = "{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonspec = jsonspec + "'i" + datamodel.get(i).guigezhi1
								+ "':{'price':'" + datamodel.get(i).getJiage()
								+ "','goods_promotion_price':'"
								+ datamodel.get(i).getTejia() + "','stock':'"
								+ datamodel.get(i).getKucun()
								+ "','sp_value':{'"
								+ datamodel.get(i).getGuigezhi1() + "':'"
								+ datamodel.get(i).getXinghao1() + "'}},";
					}
					jsonspec = jsonspec.substring(0, jsonspec.length() - 1)
							+ "}";
					com.alibaba.fastjson.JSONObject spec = com.alibaba.fastjson.JSONObject
							.parseObject(jsonspec);
					// jsonsStr =jsonsStr+
					// "'"+tv_xinghao11.getText().toString()+"':{'"+datamodel.get(i).guigezhi1.toString()+"'"
					// String g_price = datamodel.get(0).getJiage();
					// String goods_promotion_price =
					// datamodel.get(0).getTejia();
					// String g_storage = datamodel.get(0).getKucun();
					String goods_jingle = et_chanpinjianjie.getText()
							.toString();
					String image_path = imageString1;
					String image_all = imageString;
					HttpUtils.ShangPinFaBuBianji1(res_Fabu, key,goods_commonid,cate_id,cate_name,g_name,sp_name,sp_value,spec,goods_jingle,image_path,image_all);
				} else if (state == 2) {
//					Toast.makeText(getApplicationContext(), "型号2",Toast.LENGTH_LONG).show();
					String cate_id = gc_id;
					String cate_name = gc_name;
					String g_name = et_chanpinming.getText().toString();
					// String sp_name 规格名称，参见下方范例
					String jsonStr = "{'"+ tv_xinghao22.getText().toString() + "':'"+ tv_xinghao2.getText().toString() + "'}";
					com.alibaba.fastjson.JSONObject sp_name = com.alibaba.fastjson.JSONObject
							.parseObject(jsonStr);

					// String sp_value 规格值，参见下方范例
					String jsonsp_value = "{'"+ tv_xinghao22.getText().toString() + "':{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonsp_value = jsonsp_value + "'"
								+ datamodel.get(i).guigezhi2 + "':'"
								+ datamodel.get(i).getXinghao2() + "',";
					}
					jsonsp_value = jsonsp_value.substring(0,
							jsonsp_value.length() - 1)
							+ "}}";
					com.alibaba.fastjson.JSONObject sp_value = com.alibaba.fastjson.JSONObject
							.parseObject(jsonsp_value);

					// String spec 提交的规格，参见下方范例
					String jsonspec = "{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonspec = jsonspec + "'i" + datamodel.get(i).guigezhi2
								+ "':{'price':'" + datamodel.get(i).getJiage()
								+ "','goods_promotion_price':'"
								+ datamodel.get(i).getTejia() + "','stock':'"
								+ datamodel.get(i).getKucun()
								+ "','sp_value':{'"
								+ datamodel.get(i).getGuigezhi2() + "':'"
								+ datamodel.get(i).getXinghao2() + "'}},";
					}
					jsonspec = jsonspec.substring(0, jsonspec.length() - 1)
							+ "}";
					com.alibaba.fastjson.JSONObject spec = com.alibaba.fastjson.JSONObject
							.parseObject(jsonspec);
					// jsonsStr =jsonsStr+
					// "'"+tv_xinghao11.getText().toString()+"':{'"+datamodel.get(i).guigezhi1.toString()+"'"
					// String g_price = datamodel.get(0).getJiage();
					// String goods_promotion_price =
					// datamodel.get(0).getTejia();
					// String g_storage = datamodel.get(0).getKucun();
					String goods_jingle = et_chanpinjianjie.getText()
							.toString();
					String image_path = imageString1;
					String image_all = imageString;
					HttpUtils.ShangPinFaBuBianji1(res_Fabu, key,goods_commonid,cate_id,cate_name,g_name,sp_name,sp_value,spec,goods_jingle,image_path,image_all);
				} else if (state == 3) {
//					Toast.makeText(getApplicationContext(), "多个型号",Toast.LENGTH_LONG).show();
					String cate_id = gc_id;
					String cate_name = gc_name;
					String g_name = et_chanpinming.getText().toString();
					// String sp_name 规格名称，参见下方范例
					String jsonStr = "{'" + tv_xinghao11.getText().toString()+ "':'" + tv_xinghao1.getText().toString() + "','"+ tv_xinghao22.getText().toString() + "':'"+ tv_xinghao2.getText().toString() + "'}";
					com.alibaba.fastjson.JSONObject sp_name = com.alibaba.fastjson.JSONObject
							.parseObject(jsonStr);

					// String sp_value 规格值，参见下方范例
					String jsonsp_value = "{'"+ tv_xinghao11.getText().toString() + "':{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonsp_value = jsonsp_value + "'" 
								+ datamodel.get(i).guigezhi1 + "':'"
								+ datamodel.get(i).getXinghao1() + "',";
					}
					jsonsp_value = jsonsp_value.substring(0,
							jsonsp_value.length() - 1)
							+ "},'"+ tv_xinghao22.getText().toString() + "':{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonsp_value = jsonsp_value + "'"
								+ datamodel.get(i).guigezhi2 + "':'"
								+ datamodel.get(i).getXinghao2() + "',";
					}
					jsonsp_value = jsonsp_value.substring(0,
							jsonsp_value.length() - 1)
							+ "}}";
					com.alibaba.fastjson.JSONObject sp_value = com.alibaba.fastjson.JSONObject
							.parseObject(jsonsp_value);

					// String spec 提交的规格，参见下方范例
					String jsonspec = "{";
					for (int i = 0; i < datamodel.size(); i++) {
						jsonspec = jsonspec + "'i" + datamodel.get(i).guigezhi1
								+ "':{'price':'" + datamodel.get(i).getJiage()
								+ "','goods_promotion_price':'"
								+ datamodel.get(i).getTejia() + "','stock':'"
								+ datamodel.get(i).getKucun()
								+ "','sp_value':{'"
								+ datamodel.get(i).getGuigezhi1() + "':'"
								+ datamodel.get(i).getXinghao1() + "','"
								+ datamodel.get(i).getGuigezhi2() + "':'"
								+ datamodel.get(i).getXinghao2() + "'}},";
					}
					jsonspec = jsonspec.substring(0, jsonspec.length() - 1)
							+ "}";
					com.alibaba.fastjson.JSONObject spec = com.alibaba.fastjson.JSONObject
							.parseObject(jsonspec);
					// jsonsStr =jsonsStr+
					// "'"+tv_xinghao11.getText().toString()+"':{'"+datamodel.get(i).guigezhi1.toString()+"'"
					// String g_price = datamodel.get(0).getJiage();
					// String goods_promotion_price =
					// datamodel.get(0).getTejia();
					// String g_storage = datamodel.get(0).getKucun();
					String goods_jingle = et_chanpinjianjie.getText()
							.toString();
					String image_path = imageString1;
					String image_all = imageString;
					HttpUtils.ShangPinFaBuBianji1(res_Fabu, key,goods_commonid,cate_id,cate_name,g_name,sp_name,sp_value,spec,goods_jingle,image_path,image_all);
				}
			}

			/*
			 * String cate_id = gc_id; String cate_name = gc_name; String g_name
			 * = et_chanpinming.getText().toString(); String spec = g_name;
			 * String jsonStr =
			 * "{'"+tv_xinghao11.getText().toString()+"':'"+tv_xinghao1
			 * .getText()
			 * .toString()+"','"+tv_xinghao22.getText().toString()+"':'"
			 * +tv_xinghao2.getText().toString()+"'}";
			 * com.alibaba.fastjson.JSONObject sp_name =
			 * com.alibaba.fastjson.JSONObject.parseObject(jsonStr) ;
			 */

			// XingHaoModel model = new XingHaoModel();
			// datamodel
			// for (int i = 0; i < datamodel.size(); i++) {
			// jsonsStr =jsonsStr+
			// "'"+tv_xinghao11.getText().toString()+"':{'"+datamodel.get(i).guigezhi1.toString()+"'"
			// }
			// String jsonsStr =
			// "{'"+tv_xinghao11.getText().toString()+"':'"+tv_xinghao1.getText().toString()+"','"+tv_xinghao22.getText().toString()+"':'"+tv_xinghao2.getText().toString()+"'}"
			// ;

			/*
			 * String jsonsStr="{'"+tv_xinghao11.getText().toString()+"':{"; for
			 * (int i = 0; i < datamodel.size(); i++){ jsonsStr =
			 * jsonsStr+"'"+i+"':'"+i+"',"; } jsonsStr =
			 * jsonsStr.substring(0,jsonStr
			 * .length()-1)+"},'"+tv_xinghao22.getText().toString()+"':{";
			 */

			// for (int i = 0; i < datamodel.size(); i++){
			// jsonsStr1 = jsonsStr1+"{'"+i+"':'"+i+"'},";
			// }
			// String asss = jsonsStr+jsonsStr1;
			// String sp_value =
			// String type_id =
			// String goods_jingle =
			// String image_path =
			// String image_all =
			// HttpUtils.ShangPinFaBu(res_getGuiGeList,
			// key,cate_id,cate_name,g_name,spec,sp_name,sp_value,type_id,goods_jingle,image_path,image_all);

			break;
		case R.id.img_1:
			// HttpUtils.ShangPinFaBu(res_getGuiGeList, key,cate_id);
			setImageTag = IV_1;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照", "取消" }, this);
			break;
		case R.id.img_2:
			setImageTag = IV_2;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照", "取消" }, this);
			break;
		case R.id.img_3:
			setImageTag = IV_3;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照", "取消" }, this);
			break;
		case R.id.img_4:
			setImageTag = IV_4;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照", "取消" }, this);
			break;
		case R.id.img_5:
			setImageTag = IV_5;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照", "取消" }, this);
			break;

		case R.id.dialog_modif_1:// 相册
			UIDialog.closeDialog();
			/**
			 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
			 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
			 */
			Intent intent_toXIANGCE = new Intent(Intent.ACTION_PICK, null);
			/**
			 * 下面这句话，与其它方式写是一样的效果，如果： intent.setData(MediaStore.Images
			 * .Media.EXTERNAL_CONTENT_URI); intent.setType(""image/*");设置数据类型
			 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如 ："image/jpeg 、 image/png等的类型"
			 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
			 */
			intent_toXIANGCE.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent_toXIANGCE, XIANGCE);
			break;
		case R.id.dialog_modif_2:// 拍照
			UIDialog.closeDialog();
			/**
			 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
			 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
			 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
			 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
			 */
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMddHHmmss");
			timeString = dateFormat.format(date);
			createSDCardDir();
			Intent intent_PAIZHAO = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent_PAIZHAO.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory() + "/DCIM/Camera",
							timeString + ".jpg")));
			startActivityForResult(intent_PAIZHAO, PAIZHAO);
			break;
		case R.id.dialog_modif_3:// 取消
			UIDialog.closeDialog();
			break;
		case R.id.tv_cancel://
			this.finish();
			break;
		default:
			break;
		}
	}

	JsonHttpResponseHandler res_Fabu = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
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
				Toast.makeText(getApplicationContext(), "发布成功",
						Toast.LENGTH_LONG).show();
				B3_Add1Activity.this.finish();
				Intent itpro = new Intent();
				itpro.setClass(B3_Add1Activity.this, B2_ProductActivity.class);
				startActivity(itpro);
				// todo清数据
			} else {
				Tools.Notic(B3_Add1Activity.this, error + "", null);
			}
		}
	};

	JsonHttpResponseHandler res_getGuiGeList = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
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
					org.json.JSONArray jarry = datas.getJSONArray("spec_list");
					str = new String[jarry.length()];
					str1 = new String[jarry.length()];
					for (int i = 0; i < jarry.length(); i++) {
						JSONObject jsonItem = jarry.getJSONObject(i);
						str[i] = jsonItem.getString("sp_name").toString();
						str1[i] = jsonItem.getString("sp_id").toString();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Tools.Notic(B3_Add1Activity.this, error + "", null);
			}
		}
	};
	// 按照型号1
	JsonHttpResponseHandler res_addGgzxh1 = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
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
					org.json.JSONArray jarry = datas.getJSONArray("value_id");
					str2 = new String[jarry.length()];
					for (int i = 0; i < jarry.length(); i++) {
						str2[i] = jarry.getString(i);
					}

					for (int i = 0; i < arr.length; i++) {
						XingHaoModel model = new XingHaoModel();
						model.setXinghao1(arr[i]);
						model.setXinghao2("");
						model.setKucun("0");
						model.setJiage("0");
						model.setTejia("0");
						model.setGuigezhi1(str2[i]);
						model.setGuigezhi2("");
						data.add(model);
					}
					state = 1;
					GridAdapter listAdapter = new GridAdapter(
							B3_Add1Activity.this, data, B3_Add1Activity.this);
					lv_grid.setAdapter(listAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Tools.Notic(B3_Add1Activity.this, error + "", null);
			}
		}
	};
	// 按照型号2
	JsonHttpResponseHandler res_addGgz1xh2 = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
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
					org.json.JSONArray jarry = datas.getJSONArray("value_id");
					str3 = new String[jarry.length()];
					for (int i = 0; i < jarry.length(); i++) {
						str3[i] = jarry.getString(i);
					}

					for (int i = 0; i < arrx.length; i++) {
						XingHaoModel model = new XingHaoModel();
						model.setXinghao1("");
						model.setXinghao2(arrx[i]);
						model.setKucun("0");
						model.setJiage("0");
						model.setTejia("0");
						model.setGuigezhi1("");
						model.setGuigezhi2(str3[i]);
						data.add(model);
					}
					state = 2;
					GridAdapter listAdapter = new GridAdapter(
							B3_Add1Activity.this, data, B3_Add1Activity.this);
					lv_grid.setAdapter(listAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Tools.Notic(B3_Add1Activity.this, error + "", null);
			}
		}
	};

	JsonHttpResponseHandler res_addGgz = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
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
					org.json.JSONArray jarry = datas.getJSONArray("value_id");
					str2 = new String[jarry.length()];
					for (int i = 0; i < jarry.length(); i++) {
						str2[i] = jarry.getString(i);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Tools.Notic(B3_Add1Activity.this, error + "", null);
			}
		}
	};

	JsonHttpResponseHandler res_addGgz1 = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
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
					org.json.JSONArray jarry = datas.getJSONArray("value_id");
					str3 = new String[jarry.length()];
					for (int i = 0; i < jarry.length(); i++) {
						str3[i] = jarry.getString(i);
					}
					for (int i = 0; i < arr.length; i++) {
						for (int j = 0; j < arrx.length; j++) {
							XingHaoModel model = new XingHaoModel();
							model.setXinghao1(arr[i]);
							model.setXinghao2(arrx[j]);
							model.setKucun("0");
							model.setJiage("0");
							model.setTejia("0");
							model.setGuigezhi1(str2[i]);
							model.setGuigezhi2(str3[j]);
							data.add(model);
						}
					}
					state = 3;
					GridAdapter listAdapter = new GridAdapter(
							B3_Add1Activity.this, data, B3_Add1Activity.this);
					lv_grid.setAdapter(listAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Tools.Notic(B3_Add1Activity.this, error + "", null);
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode==-1) {
			try {
				gc_id = data.getStringExtra("gc_id");
				gc_name = data.getStringExtra("gc_name");
				if (gc_name == null || gc_name == "") {

				} else {
					TextView06.setText(gc_name);
				}
			} catch (Exception e) {
				
			}
		}
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case XIANGCE:
			try {
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, "您没有选择任何照片", Toast.LENGTH_LONG)
						.show();
			}
			break;
			// 如果是调用相机拍照时
		case PAIZHAO:
			// File temp = new File(Environment.getExternalStorageDirectory()
			// + "/xiaoma.jpg");
			// 给图片设置名字和路径
			File temp = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
			// 取得裁剪后的图片
		case CAIJIAN:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//*****************************onActivityResult操作     end******************************************
	//*****************************图像处理操作     begin******************************************
		/**
		 * 裁剪图片方法实现
		 * 
		 * @param uri
		 */
		public void startPhotoZoom(Uri uri) {
			/*
			 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
			 * yourself_sdk_path/docs/reference/android/content/Intent.html
			 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
			 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
			 */
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
//			intent.putExtra("aspectX", 1);
//			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, CAIJIAN);
		}
		
	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/DCIM/Camera";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}
		}
	}

		/**
		 * 保存裁剪之后的图片数据
		 * 
		 * @param picdata
		 */
		private void setPicToView(Intent picdata) {
			Bundle extras = picdata.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
				 */
				savaBitmap(photo);
//				switch (setImageTag) {
//				case IV_1:
//					iv_1.setImageBitmap(photo);
//					break;
//				case IV_2:
//					iv_2.setImageBitmap(photo);
//					break;
//				case IV_3:
//					iv_3.setImageBitmap(photo);
//					break;
//				case IV_logo:
//					iv_logo.setImageBitmap(photo);
//					break;
//
//				default:
//					break;
//				}
			}
		}

		// 将剪切后的图片保存到本地图片上！
		public void savaBitmap(Bitmap bitmap) {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMddHHmmss");
			cutnameString = dateFormat.format(date);
			filename = Environment.getExternalStorageDirectory().getPath() + "/"
					+ cutnameString + ".jpg";
			Tools.Log("filename="+filename);
			File f = new File(filename);
	        putSharedPreferenceValue("headImg_filename", filename);
			
			FileOutputStream fOut = null;
			try {
				f.createNewFile();
				fOut = new FileOutputStream(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
			
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			RequestDailog.showDialog(this, "正在上传图片，请稍后");
			HttpUtils.getImageLoad(res_uploadaPhoto,getSharedPreferenceValue("key"),"good_img",f);
		}

		//*****************************图像处理操作     end******************************************
			JsonHttpResponseHandler res_uploadaPhoto = new JsonHttpResponseHandler()
			{

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					first++;
					switch (setImageTag) {
					case IV_1:
						iv_1.setImageBitmap(photo);
						break;
					case IV_2:
						iv_2.setImageBitmap(photo);
						break;
					case IV_3:
						iv_3.setImageBitmap(photo);
						break;
					case IV_4:
						iv_4.setImageBitmap(photo);
						break;
					case IV_5:
						iv_5.setImageBitmap(photo);
						break;
					default:
						break;
					}
					RequestDailog.closeDialog();
					Tools.Log("上传图片返回值="+response);
					JSONObject datas = null;
					String error = null;
					try {
						datas = response.getJSONObject("datas");
						error = datas.getString("error");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					if (error == null) {
						try { 
							imageString1 = datas.getString("image_name");
							if (first>1) 
							{
								imageString = imageString +","+ imageString1; 
							}
							else//第一次进来的时候，不要加“，”进行连接
							{
								imageString = imageString + imageString1; 
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						Tools.Notic(B3_Add1Activity.this, error+"", null);
					}
					
				}
			};

	//
	public void SaveMod(List<XingHaoModel> datamodel) {
		this.datamodel = datamodel;
	}

}