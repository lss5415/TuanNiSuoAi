package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.Goods;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class B2_GoodsAdapter extends BaseAdapter {

	private List<Goods> list;
    private LayoutInflater inflater;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	
	public B2_GoodsAdapter(Context context, List<Goods> list) {
        inflater = LayoutInflater.from(context);
		this.list=list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Goods getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
        if(convertView == null){
            convertView=inflater.inflate(R.layout.ui_b2_item_goods,null);
            holder=new ViewHolder();
            holder.good_image= (ImageView) convertView.findViewById(R.id.good_image);//图片
            holder.good_name= (TextView) convertView.findViewById(R.id.good_name);//名称
            holder.good_juli= (TextView) convertView.findViewById(R.id.good_juli);//距离
            holder.good_jingle= (TextView) convertView.findViewById(R.id.good_jingle);//简介
            holder.goods_price= (TextView) convertView.findViewById(R.id.goods_price);//价格
            holder.goods_price1= (TextView) convertView.findViewById(R.id.goods_price1);//价格
            holder.im_moreinfoadapter = (ImageView)convertView.findViewById(R.id.im_moreinfoadapter);
            holder.ll_b1a2_moreinfolayout = (LinearLayout)convertView.findViewById(R.id.ll_b1a2_moreinfolayout);
            holder.tv_dianpuming=(TextView) convertView.findViewById(R.id.tv_dianpuming);
            holder.tv_kucun=(TextView) convertView.findViewById(R.id.tv_kucun);
            holder.tv_xiaoliang=(TextView) convertView.findViewById(R.id.tv_xiaoliang);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Goods good = list.get(position);
        
		ImageLoader.getInstance().displayImage(good.getGoods_image_url(), holder.good_image, ImageOptions.getOpstion(), animateFirstListener);
//        ImageOptions.displayImage2Circle(holder.good_image, good.getGoods_image_url(), 10f);
        holder.good_name.setText(good.getGoods_name()+"");
        holder.good_juli.setText(good.getJuli()+"");
        holder.good_jingle.setText(good.getGoods_jingle()+"");
//        goods_promotion_price
        if (good.getIs_special().equals("1")) {
            holder.goods_price.setText(good.getGoods_promotion_price()+"");
        	holder.goods_price1.setVisibility(View.VISIBLE);
			holder.goods_price1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			holder.goods_price1.setText("￥ "+good.getGoods_price());
		}else {
            holder.goods_price.setText(good.getGoods_price()+"");
		}
        
        holder.tv_dianpuming.setText(good.getStore_name()+"");
        holder.tv_kucun.setText(good.getGoods_storage()+"");
        holder.tv_xiaoliang.setText(good.getGoods_salenum()+"");
        holder.im_moreinfoadapter.setOnClickListener(new MoreInfoListener(position,convertView));//更多按钮绑定监听
        holder.ll_b1a2_moreinfolayout.setOnClickListener(new YinCangListener(position,convertView));
		return convertView;
	}

	class YinCangListener implements View.OnClickListener {
		int position;
		View convertView;
		public YinCangListener(int position,View convertView) {
			this.position = position;
			this.convertView = convertView;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ViewHolder ViewHolder=null;
			ViewHolder=new ViewHolder();
			ViewHolder.ll_b1a2_moreinfolayout = (LinearLayout)convertView.findViewById(R.id.ll_b1a2_moreinfolayout);
			ViewHolder.ll_b1a2_moreinfolayout.setVisibility(View.GONE);
		}
	}
	
	class MoreInfoListener implements View.OnClickListener {
		int position;
		View convertView;
		public MoreInfoListener(int position,View convertView) {
			this.position = position;
			this.convertView = convertView;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ViewHolder ViewHolder=null;
			ViewHolder=new ViewHolder();
			ViewHolder.ll_b1a2_moreinfolayout = (LinearLayout)convertView.findViewById(R.id.ll_b1a2_moreinfolayout);
			ViewHolder.ll_b1a2_moreinfolayout.setVisibility(View.VISIBLE);
		}
	}
	
	class ViewHolder{
		public ImageView good_image;//图片
		public TextView good_name;//名称
		public TextView good_juli;//距离
		public TextView good_jingle;//简介
		public TextView goods_price;//价格
		public TextView goods_price1;//价格（横线的）
        public ImageView im_moreinfoadapter;
        public LinearLayout ll_b1a2_moreinfolayout;
        public TextView tv_dianpuming;
        public TextView tv_kucun;
        public TextView tv_xiaoliang;
    }
}

