package com.ZYKJ.tuannisuoai.adapter;


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
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class JieSuanShangPinAdapter extends BaseAdapter {
	
	private Activity c;
	JSONArray goodsjsa;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    
	public JieSuanShangPinAdapter(Activity c, JSONArray goodsjsa) {
		this.c = c;
		this.goodsjsa = goodsjsa;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goodsjsa == null ? 0 : goodsjsa.length();
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
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(R.layout.ui_goodsjiesuan_list_items, null);
            viewHolder.childrenNameTV = (TextView) convertView.findViewById(R.id.children_name1);
			viewHolder.im_shangpuimg = (ImageView) convertView.findViewById(R.id.im_shangpuimg1);
			viewHolder.tv_spec = (TextView) convertView.findViewById(R.id.tv_spec1);
			viewHolder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price1);
			viewHolder.tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num1);
            
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
    	try {
			JSONObject jobdata = (JSONObject) goodsjsa.get(position);
			String goods_image_url = jobdata.getJSONObject("nameValuePairs").getString("goods_image_url");
			ImageLoader.getInstance().displayImage(goods_image_url, viewHolder.im_shangpuimg, ImageOptions.getOpstion(), animateFirstListener);//设置商品图片
			viewHolder.childrenNameTV.setText(jobdata.getJSONObject("nameValuePairs").getString("goods_name").toString());//设置商品名称
			viewHolder.tv_goods_price.setText("￥"+jobdata.getJSONObject("nameValuePairs").getString("goods_price").toString());//设置商品价格
			viewHolder.tv_goods_num.setText("X"+jobdata.getJSONObject("nameValuePairs").getString("goods_num").toString());//设置商品数量
			String spe = jobdata.getJSONObject("nameValuePairs").getJSONObject("goods_spec").getJSONObject("nameValuePairs").toString();
			viewHolder.tv_spec.setText(spe.substring(1,spe.length()-1));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	
        
		return convertView;
	}
	/**
	 * 点击立即兑换按钮之后的跳转
	 * @author zyk
	 *
	 */
	class DeletetheorderListener implements View.OnClickListener {
		int position;
		public DeletetheorderListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//		HttpUtils.cancelOrder(res_cancelOrder, key, order_id);
			
		}

	}

	private static class ViewHolder
    {
		TextView childrenNameTV;
		ImageView im_shangpuimg;
		TextView tv_spec;
		TextView tv_goods_price;
		TextView tv_goods_num;
    }
}
