package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.Shop;
import com.nostra13.universalimageloader.core.ImageLoader;

public class B2_ShopsAdapter extends BaseAdapter {

	private List<Shop> list;
    private LayoutInflater inflater;
	
	public B2_ShopsAdapter(Context context, List<Shop> list) {
        inflater = LayoutInflater.from(context);
		this.list=list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Shop getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder ViewHolder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.b1_a3_item, null);
			ViewHolder=new ViewHolder();
			ViewHolder.im_a3_pic=(ImageView) convertView.findViewById(R.id.im_a3_pic);
			ViewHolder.tv_a3_storename=(TextView) convertView.findViewById(R.id.tv_a3_storename);
			ViewHolder.tv_a3_juli=(TextView) convertView.findViewById(R.id.tv_a3_juli);
			ViewHolder.comment_rating_bar=(RatingBar) convertView.findViewById(R.id.comment_rating_bar);
			ViewHolder.tv_a3_pinglunsum=(TextView) convertView.findViewById(R.id.tv_a3_pinglunsum);
			ViewHolder.tv_a3_dpfl=(TextView) convertView.findViewById(R.id.tv_a3_dpfl);
			ViewHolder.tv_a3_address=(TextView) convertView.findViewById(R.id.tv_a3_address);
			convertView.setTag(ViewHolder);
		}else{
			ViewHolder=(ViewHolder) convertView.getTag();
		}
        Shop shop = list.get(position);
        
		ImageLoader.getInstance().displayImage(shop.getStore_avatar(), ViewHolder.im_a3_pic);
		ViewHolder.tv_a3_storename.setText(shop.getStore_name());
		ViewHolder.tv_a3_juli.setText(shop.getJuli());
		ViewHolder.comment_rating_bar.setRating(Float.parseFloat(shop.getStore_desccredit()));
		ViewHolder.tv_a3_pinglunsum.setText(shop.getStore_evaluate_count());
		ViewHolder.tv_a3_dpfl.setText(shop.getSc_name());
		ViewHolder.tv_a3_address.setText(shop.getStore_address());
		return convertView;
	}
	
	class ViewHolder{
        ImageView im_a3_pic;//图片
        TextView tv_a3_storename;//名称
        TextView tv_a3_juli;//距离
        RatingBar comment_rating_bar;//评价
        TextView tv_a3_pinglunsum;//评论数
        TextView tv_a3_dpfl;//分类
        TextView tv_a3_address;//地址
    }
}

