package com.zykj.tuannisuoai_seller.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.Tools;

public class B5_10_MyCollectionAdapter extends BaseAdapter {

	public static List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	public static Map<String, String> map_commonid = new HashMap<String, String>();
	private Activity c;
	private GetMap_id getMap_id;
	private LayoutInflater listContainer;
	String goods_name,goods_image,goods_price,fav_id,key,goods_jingle,goods_storage,goods_salenum,commonid;
	Boolean  isEdit = false;
	public static boolean b_goods[];
//	public static String [] idStrings;
	

	
	public B5_10_MyCollectionAdapter(Activity c, List<Map<String, String>> data,Boolean  isEdit,String key,GetMap_id getMap_id) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.data = data;
		this.isEdit = isEdit;
		this.key = key;
		this.getMap_id = getMap_id;
		listContainer = LayoutInflater.from(c);
		b_goods = new boolean[50];
	}
	public interface GetMap_id {
		
		public abstract Map<String, String> getMap(Map<String, String> map_commonid); 
	}
	public int getCount() {
		// TODO Auto-generated method stub
//		idStrings  = new String [data.size()];
		return data == null ? 0 : data.size();
//		return 5;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
        if (null == convertView)
        {
        	
//        	TextView tv_productName;//产品名
//    		TextView tv_jianjie;//产品简介
//    		TextView tv_productPrice;//产品价格
//    		TextView tv_zongxiaoliang;//总销量
//    		TextView tv_kucun;//库存
//    		
//    		ImageView iv_image;//产品图片
//    		
//    		CheckBox cb_operate;//选择框
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(R.layout.ui_b5_10_collection_list_items, null);
            viewHolder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            viewHolder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            viewHolder.tv_productPrice = (TextView) convertView.findViewById(R.id.tv_productPrice);
            viewHolder.tv_zongxiaoliang = (TextView) convertView.findViewById(R.id.tv_zongxiaoliang);
            viewHolder.tv_kucun = (TextView) convertView.findViewById(R.id.tv_kucun);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.cb_operate = (CheckBox) convertView.findViewById(R.id.cb_operate);
            
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		
		if (isEdit == true) {
			viewHolder.cb_operate.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tag").equals("Product"))//传进来的数据为出售中商品的数据
		{
			goods_name = data.get(position).get("goods_name");//商品名
			goods_price = data.get(position).get("goods_price");//价格
			goods_jingle = data.get(position).get("goods_jingle");//简介
			goods_storage = data.get(position).get("goods_storage");//库存
			goods_image = data.get(position).get("goods_image");//商品图片
			goods_salenum = data.get(position).get("goods_salenum");//总销量
			commonid = data.get(position).get("goods_commonid");//商品id
			
			viewHolder.cb_operate.setChecked(b_goods[position]);
			
			ImageLoader.getInstance().displayImage(goods_image, viewHolder.iv_image);
			viewHolder.tv_productName.setText(goods_name);
			viewHolder.tv_productPrice.setText(goods_price);
			viewHolder.tv_jianjie.setText(goods_jingle);
			viewHolder.tv_kucun.setText("库存  "+goods_storage);
			viewHolder.tv_zongxiaoliang.setText("总销量  "+goods_salenum);
			
			viewHolder.cb_operate.setOnCheckedChangeListener(new CheckIt(commonid,position));
			
		}else if (data.get(position).get("tag").equals("Store")) {
			
		}
		return convertView;
	}
	/**
	 * 选择操作
	 * @author zyk
	 *
	 */
	class CheckIt implements OnCheckedChangeListener{
		String commonid;
		int position;
//		boolean b_goods[];
		public CheckIt(String commonid,int position) {
			this.commonid = commonid;
			this.position = position;
		}
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				map_commonid.put(position+"",commonid);
				Log.e("id", map_commonid+"");
			}else {
				map_commonid.remove(position+"");
			}
			getMap_id.getMap(map_commonid);
		}

	}
	/**
	 * 删除商铺
	 * @author zyk
	 *
	 */
	class DeleteStore implements View.OnClickListener {
		String store_id,key;
		public DeleteStore(String store_id,String key) {
			this.store_id = fav_id;
			this.key = key;
		}
		
		@Override
		public void onClick(View v) {
			HttpUtils.delStore(res_delProduct, store_id, key);
		}
		
	}
	
	/**
	 * 删除订单
	 */
	JsonHttpResponseHandler res_delProduct = new JsonHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				String responseString) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, responseString);
			Log.e("1111111111111111111111111111111");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONArray response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Log.e("22222222222222222222222222222222");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("取消订单="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas =response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Tools.Notic(c, "取消成功,请刷新该页面查看剩余收藏", null);
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error);
				Tools.Notic(c, "取消失败,请重试", null);
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
	};
	class ViewHolder 
	{
		TextView tv_productName;//产品名
		TextView tv_jianjie;//产品简介
		TextView tv_productPrice;//产品价格
		TextView tv_zongxiaoliang;//总销量
		TextView tv_kucun;//库存
		
		ImageView iv_image;//产品图片
		
		CheckBox cb_operate;//选择框
		
	}

}
