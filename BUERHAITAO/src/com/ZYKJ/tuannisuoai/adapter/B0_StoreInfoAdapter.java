package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class B0_StoreInfoAdapter extends BaseAdapter {
	private Activity context;
	List<Map<String, Object>> data;
	String dianpuming;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public B0_StoreInfoAdapter(Activity context, List<Map<String, Object>> data1,String dianpuming) {
		this.context = context;
		this.data=data1;
		this.dianpuming=dianpuming;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
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
		// TODO Auto-generated method stub
		ViewHolder ViewHolder=null;
		if(convertView==null){
			ViewHolder=new ViewHolder();
			convertView=LinearLayout.inflate(context, R.layout.item_b0_storeinfo, null);
			ViewHolder.im_a3_pic1=(ImageView) convertView.findViewById(R.id.im_a3_pic1);
			ViewHolder.tv_a3_storename1=(TextView) convertView.findViewById(R.id.tv_a3_storename1);
			ViewHolder.tv_chanpinjianjie=(TextView) convertView.findViewById(R.id.tv_chanpinjianjie);
			ViewHolder.tv_spzhj=(TextView) convertView.findViewById(R.id.tv_spzhj);
			ViewHolder.tv_spyj=(TextView) convertView.findViewById(R.id.tv_spyj);
			ViewHolder.rl_im_moreinfoadapter = (RelativeLayout)convertView.findViewById(R.id.rl_im_moreinfoadapter);
			ViewHolder.ll_b1a2_moreinfolayout = (LinearLayout)convertView.findViewById(R.id.ll_b1a2_moreinfolayout);
			ViewHolder.tv_dianpuming=(TextView) convertView.findViewById(R.id.tv_dianpuming);
			ViewHolder.tv_kucun=(TextView) convertView.findViewById(R.id.tv_kucun);
			ViewHolder.tv_xiaoliang=(TextView) convertView.findViewById(R.id.tv_xiaoliang);
			convertView.setTag(ViewHolder);
		}else{
			ViewHolder=(ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage((String)data.get(position).get("goods_image_url"), ViewHolder.im_a3_pic1, ImageOptions.getOpstion(), animateFirstListener);
		ViewHolder.tv_a3_storename1.setText(data.get(position).get("goods_name").toString());
		ViewHolder.tv_chanpinjianjie.setText(data.get(position).get("goods_jingle").toString());
//		ViewHolder.comment_rating_bar1.setRating(Float.parseFloat(data.get(position).get("evaluation_good_star")));
		ViewHolder.tv_spzhj.setText("￥"+data.get(position).get("goods_price").toString());
		ViewHolder.tv_spzhj.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG);
		ViewHolder.tv_spyj.setText("￥"+data.get(position).get("goods_promotion_price").toString());
		ViewHolder.tv_dianpuming.setText(dianpuming);
		ViewHolder.tv_kucun.setText(data.get(position).get("goods_storage").toString());
		ViewHolder.tv_xiaoliang.setText(data.get(position).get("goods_salenum").toString());
		ViewHolder.rl_im_moreinfoadapter.setOnClickListener(new MoreInfoListener(position,convertView));//更多按钮绑定监听
		ViewHolder.ll_b1a2_moreinfolayout.setOnClickListener(new YinCangListener(position,convertView));
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
	
	public final class ViewHolder {  
        public ImageView im_a3_pic1;
        public TextView tv_a3_storename1;
        public TextView tv_chanpinjianjie;
        public TextView tv_spzhj;
        public TextView tv_spyj;
        public RelativeLayout rl_im_moreinfoadapter;
        public LinearLayout ll_b1a2_moreinfolayout;
        public TextView tv_dianpuming;
        public TextView tv_kucun;
        public TextView tv_xiaoliang;
    }  
}
