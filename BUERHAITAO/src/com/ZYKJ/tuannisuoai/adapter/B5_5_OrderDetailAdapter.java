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
import com.nostra13.universalimageloader.core.ImageLoader;

public class B5_5_OrderDetailAdapter extends BaseAdapter {
	
	private Activity c;
	JSONArray extend_order_goods;
    
	public B5_5_OrderDetailAdapter(Activity c, JSONArray extend_order_goods) {
		this.c = c;
		this.extend_order_goods = extend_order_goods;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return extend_order_goods == null ? 0 : extend_order_goods.length();
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
            convertView = mInflater.inflate(R.layout.ui_b5_5_orderlist_list_items_1, null);
            viewHolder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            viewHolder.tv_goodsprice = (TextView) convertView.findViewById(R.id.tv_goodsprice);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
//        for (int i = 0; i < extend_order_goods.length(); i++) {
        	try {
				JSONObject  extend_order_goods1 = (JSONObject) extend_order_goods.get(position);
				String goods_image_url = extend_order_goods1.getString("image_60_url");
				ImageLoader.getInstance().displayImage(goods_image_url, viewHolder.iv_product);//设置产品图片
				viewHolder.tv_productName.setText(extend_order_goods1.getString("goods_name").toString());//设置产品名称
				viewHolder.tv_goodsprice.setText("￥"+extend_order_goods1.getString("goods_price").toString());//设置产品价格
				viewHolder.tv_number.setText("X"+extend_order_goods1.getString("goods_num").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
//		}
        
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
        TextView tv_productName;
        TextView tv_goodsprice;
        TextView tv_number;
        ImageView iv_product;//
    }
}
