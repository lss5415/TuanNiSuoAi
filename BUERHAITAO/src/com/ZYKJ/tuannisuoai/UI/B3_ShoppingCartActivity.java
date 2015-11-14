package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B3_ShpppingCartAdapter;
import com.ZYKJ.tuannisuoai.adapter.B3_ShpppingCartAdapter.RefreshExpandableList;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.ChildrenItem;
import com.ZYKJ.tuannisuoai.data.GroupItem;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @author lss 2015年7月1日 购物车
 *
 */
public class B3_ShoppingCartActivity extends BaseActivity implements RefreshExpandableList{
	//标题
// 	private TextView tv_sp_title;
	//购物车list
	private ExpandableListView expandableList;
	//购物车数据
	private B3_ShpppingCartAdapter adapter;
	private List<GroupItem> dataList = new ArrayList<GroupItem>();
	private int groupCount;
	private TextView tv_jiesuan;//结算（0）
	private TextView tv_sumgoods;//总价
	private ImageView im_checkall;//全选
	int ischeck=0;//1是全选，0是取消全选
	String fhgoodsum;
	int sumtiaoshu = 0;
	private String key;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b3_shoppingcart);
		key = getSharedPreferenceValue("key");
		initView();
	}
	
	private void initView(){
		/*tv_sp_title = (TextView)findViewById(R.id.tv_sp_title);
		*/
		expandableList = (ExpandableListView)findViewById(R.id.list_shoppingcar);
		expandableList.setGroupIndicator(null);
		tv_jiesuan = (TextView)findViewById(R.id.tv_jiesuan);//结算
		tv_sumgoods = (TextView)findViewById(R.id.tv_sumgoods);//总价
		im_checkall = (ImageView)findViewById(R.id.im_checkall);//全选
		
		setListener(tv_jiesuan,im_checkall);
//		HttpUtils.getShoppingCarInfoList(res_ShoppingCarInfo,getSharedPreferenceValue("key"));
		HttpUtils.getShoppingCarInfoList(res_ShoppingCarInfo, key);
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
	
		switch (v.getId()) {
		case R.id.tv_jiesuan:
			/*结算*/
			String a = tv_jiesuan.getText().toString();
			if (a.equals("结算（0）")) {
				Toast.makeText(getApplicationContext(), "您还没有选择商品哦！", Toast.LENGTH_LONG).show();
			}else {
				String allcheckinfo = "";
				for (int i = 0; i < dataList.size(); i++) {
					List<ChildrenItem> childrenList = dataList.get(i).getStore_list();
					for (int j = 0; j < childrenList.size(); j++) {
						ChildrenItem childrenItem = childrenList.get(j);
						if(childrenItem.isChecked()){
							allcheckinfo = allcheckinfo+(childrenItem.getCart_id()+"|"+childrenItem.getGoods_num()+",");
						}
					}
				}
				allcheckinfo=allcheckinfo.substring(0, allcheckinfo.length()-1);
				Intent itmrhd = new Intent();
				itmrhd.setClass(B3_ShoppingCartActivity.this, JieSuanActivity.class);
				itmrhd.putExtra("allpri", tv_sumgoods.getText().toString());
				itmrhd.putExtra("allcheckinfo", allcheckinfo);
				startActivity(itmrhd);
			}
//			showCheckedItems();
			break;
		case R.id.im_checkall:
			if (im_checkall.isSelected()) {
				im_checkall.setSelected(false);
				tv_jiesuan.setText("结算（0）");
				tv_sumgoods.setText("0.00");
				for (int i = 0; i < dataList.size(); i++) {
					dataList.get(i).setChecked(false);
					List<ChildrenItem> childrenList = dataList.get(i).getStore_list();
					for (int j = 0; j < childrenList.size(); j++) {
						childrenList.get(j).setChecked(false);
					}
				}
				adapter.notifyDataSetChanged();
			}else {
				im_checkall.setSelected(true);
				float allprice = 0f;sumtiaoshu = 0;
				for (int i = 0; i < dataList.size(); i++) {
					dataList.get(i).setChecked(true);
					List<ChildrenItem> childrenList = dataList.get(i).getStore_list();
					for (int j = 0; j < childrenList.size(); j++) {
						childrenList.get(j).setChecked(true);
						ChildrenItem childrenItem = childrenList.get(j);
						sumtiaoshu += 1;
						allprice += Float.valueOf(childrenItem.getGoods_price())*Integer.valueOf(childrenItem.getGoods_num());
					}
				}
				tv_jiesuan.setText("结算（"+sumtiaoshu+"）");
				tv_sumgoods.setText(allprice+"");
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			
			break;
		}

	}

	/**
	 * 初始化Adapter数据
	 * 
	 * @Title:
	 * @Description:
	 * @return void 返回类型
	 * @author zhangxiaolei
	 * @throws
	 */
	private void initData() {
		for (int i = 0; i < dataList.size(); i++) {
			sumtiaoshu =  sumtiaoshu + dataList.get(i).getStore_list().size();
		}
//		Toast.makeText(getApplicationContext(), ""+sumtiaoshu, Toast.LENGTH_LONG).show();
		adapter = new B3_ShpppingCartAdapter(this, dataList,ischeck,sumtiaoshu,this,getSharedPreferenceValue("key"));
		expandableList.setAdapter(adapter);

		groupCount = expandableList.getCount();

		for (int i = 0; i < groupCount; i++) {

			expandableList.expandGroup(i);

		}
//		List<String> checkedChildren = adapter.getCheckedChildren();
//		Toast.makeText(getApplicationContext(), "结算("+String.valueOf(checkedChildren.size()), Toast.LENGTH_LONG).show();
		/*List<ChildrenItem> list1 = new ArrayList<ChildrenItem>();
		list1.add(new ChildrenItem("1子id", "1子name","1"));
		list1.add(new ChildrenItem("2子id", "2子name","1"));
		list1.add(new ChildrenItem("3子id", "3子name","1"));

		GroupItem groupItem1 = new GroupItem("1组id", "1组name", list1);
		dataList.add(groupItem1);

		List<ChildrenItem> list2 = new ArrayList<ChildrenItem>();
		list2.add(new ChildrenItem("4子id", "4子name","1"));
		list2.add(new ChildrenItem("5子id", "5子name","1"));

		GroupItem groupItem2 = new GroupItem("2组id", "2组name", list2);
		dataList.add(groupItem2);

		List<ChildrenItem> list3 = new ArrayList<ChildrenItem>();
		list3.add(new ChildrenItem("6子id", "6子name","1"));
		list3.add(new ChildrenItem("7子id", "7子name","1"));
		list3.add(new ChildrenItem("8子id", "8子name","1"));

		GroupItem groupItem3 = new GroupItem("3组id", "3组name", list3);
		dataList.add(groupItem3);*/
	}
	
	/**
	 * 获取购物车详细列表
	 */
	JsonHttpResponseHandler res_ShoppingCarInfo = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			Tools.Log("res_shaidanquanfabu="+response);
//			Toast.makeText(PublishedActivity.this, response+"", 400).show();
			try {
				datas = response.getJSONObject("datas");
				 error = response.getJSONObject("datas").getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				try {
					JSONArray array = datas.getJSONArray("cart_list");
					dataList=com.alibaba.fastjson.JSONArray.parseArray(array.toString(), GroupItem.class);
					fhgoodsum = datas.getString("sum");
//					tv_sumgoods.setText(fhgoodsum);
					initData();
					
					
					/*
					for (int i = 0; i < array.length(); i++) {
						
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, String> map = new HashMap();
						map.put("store_id", jsonItem.getString("store_id"));
						map.put("store_name", jsonItem.getString("store_name"));
						data.add(map);
						
						JSONArray storearry = jsonItem.getJSONArray("store_list");
						for (int j = 0; j < storearry.length(); j++) {
							JSONObject jsonItem1 = array.getJSONObject(i);
							Map<String, String> map1 = new HashMap();
							map1.put("cart_id", jsonItem1.getString("cart_id"));
							map1.put("buyer_id", jsonItem1.getString("buyer_id"));
							map1.put("store_id", jsonItem1.getString("store_id"));
							map1.put("store_name", jsonItem1.getString("store_name"));
							map1.put("goods_id", jsonItem1.getString("goods_id"));
							map1.put("goods_name", jsonItem1.getString("goods_name"));
							map1.put("goods_price", jsonItem1.getString("goods_price"));
							map1.put("goods_num", jsonItem1.getString("goods_num"));
							map1.put("state", jsonItem1.getString("state"));
							map1.put("goods_jingle", jsonItem1.getString("goods_jingle"));
							map1.put("goods_spec", jsonItem1.getString("goods_spec"));
							map1.put("goods_freight", jsonItem1.getString("goods_freight"));
							map1.put("goods_storage", jsonItem1.getString("goods_storage"));
							map1.put("goods_image_url", jsonItem1.getString("goods_image_url"));
							map1.put("goods_sum", jsonItem1.getString("goods_sum"));
							map1.put("goods_total", jsonItem1.getString("goods_total"));
							data1.add(map1);
						}
						
						
					}*/
					
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
			
			
		};
		
	};

	@Override
	protected void onResume() {
		super.onResume();
		HttpUtils.getShoppingCarInfoList(res_ShoppingCarInfo, key);
	};

	@Override
	public void refreshShopCarDate(float totalprice, boolean allcheck, int count) {
		tv_sumgoods.setText(Float.toString(totalprice));
		tv_jiesuan.setText("结算（"+count+"）");
		if (allcheck) {
			ischeck=1;
			im_checkall.setSelected(true);
		}else {
			ischeck=0;
			im_checkall.setSelected(false);
		}
	}

}