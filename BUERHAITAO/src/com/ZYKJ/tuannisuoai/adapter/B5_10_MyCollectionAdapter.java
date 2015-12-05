package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.activeandroid.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class B5_10_MyCollectionAdapter extends BaseAdapter {

	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Activity c;
	private LayoutInflater listContainer;
	String goods_name,goods_image_url,goods_price,fav_time,fav_id,key;
	Boolean  isEdit = false;
//	public static String [] idStrings;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	
	public B5_10_MyCollectionAdapter(Activity c, List<Map<String, String>> data,Boolean  isEdit,String key) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.data = data;
		this.isEdit = isEdit;
		this.key = key;
		listContainer = LayoutInflater.from(c);
	}
//	public String [] getidStrings()
//	{
//		return idStrings;
//	}
	@Override
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
	public View getView(int position, View cellView, ViewGroup arg2) {
		if (cellView == null) {
			cellView = listContainer.inflate(R.layout.ui_b5_10_collection_list_items, null);
		}
		
		TextView tv_productName=(TextView) cellView.findViewById(R.id.tv_productName);
		TextView tv_collectiondate=(TextView) cellView.findViewById(R.id.tv_collectiondate);
		TextView tv_productPoints=(TextView) cellView.findViewById(R.id.tv_productPoints);
		ImageView iv_product_collection=(ImageView)cellView.findViewById(R.id.iv_product_collection);	
		ImageView iv_delete=(ImageView)cellView.findViewById(R.id.iv_delete);	
//		CheckBox cb_edit = (CheckBox) cellView.findViewById(R.id.cb_edit);
		
//		if (cb_edit.isChecked()) //用数组存储这一条是否被选中的状态
//		{
//			idStrings[position] = data.get(position).get("fav_id");
//		}else {
//			idStrings[position] = "0";
//		}
//		
		if (isEdit == true) {
			iv_delete.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tag").equals("Product")) {
			goods_name = data.get(position).get("goods_name");
			goods_image_url = data.get(position).get("goods_image_url");
			goods_price = data.get(position).get("goods_price");
			fav_time = data.get(position).get("fav_time");
			ImageLoader.getInstance().displayImage(goods_image_url, iv_product_collection, ImageOptions.getOpstion(), animateFirstListener);
			tv_productName.setText(goods_name);
			tv_productPoints.setText("￥"+goods_price);
			tv_collectiondate.setText(fav_time+"的收藏");
			iv_delete.setOnClickListener(new DeleteProduct(data.get(position).get("fav_id"),key,position));
		}else if (data.get(position).get("tag").equals("Store")) {
			tv_productPoints.setText("");
			ImageLoader.getInstance().displayImage(data.get(position).get("store_avatar_url"), iv_product_collection, ImageOptions.getOpstion(), animateFirstListener);
			tv_productName.setText(data.get(position).get("store_name"));
			tv_collectiondate.setText(data.get(position).get("fav_time_text")+"的收藏");
			iv_delete.setOnClickListener(new DeleteStore(data.get(position).get("store_id"),key,position));
//			iv_delete.setOnClickListener(new DeleteStore());
		}
		return cellView;
	}
	/**
	 * 删除产品
	 * @author zyk
	 *
	 */
	class DeleteProduct implements View.OnClickListener {
		String fav_id,key;
		int position;
		public DeleteProduct(String fav_id,String key,int position) {
			this.fav_id = fav_id;
			this.key = key;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			RequestDailog.showDialog(c, "正在删除，请稍后");
//			Tools.Log("key="+key+"|fav_id="+fav_id);
//			Log.e("key", key);
//			Log.e("fav_id", fav_id);
			data.remove(position);
			HttpUtils.delProduct(res_delProduct, key, fav_id);
			
		}

	}
	/**
	 * 删除商铺
	 * @author zyk
	 *
	 */
	class DeleteStore implements View.OnClickListener {
		String store_id,key;
		int position;
		public DeleteStore(String store_id,String key,int position) {
			this.store_id = fav_id;
			this.key = key;
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			data.remove(position);
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
				Tools.Notic(c, "取消收藏成功", null);

				notifyDataSetChanged();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error);
				Tools.Notic(c, "取消失败,请重试", null);
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
	};

}
