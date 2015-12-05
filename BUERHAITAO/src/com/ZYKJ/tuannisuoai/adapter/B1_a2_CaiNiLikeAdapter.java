package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author lss 2015年6月24日  猜你喜欢Adapter
 *
 */
public class B1_a2_CaiNiLikeAdapter extends BaseAdapter {
	private Activity context;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public B1_a2_CaiNiLikeAdapter(Activity context, List<Map<String, String>> data) {
		this.context = context;
		this.data=data;
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
			convertView=LinearLayout.inflate(context, R.layout.b1_a2_cainixihuanitem, null);
			ViewHolder.im_a2_pic=(ImageView) convertView.findViewById(R.id.im_a2_pic);
			ViewHolder.tv_a2_chanpinname=(TextView) convertView.findViewById(R.id.tv_a2_chanpinname);
			ViewHolder.tv_a2_juli=(TextView) convertView.findViewById(R.id.tv_a2_juli);
			ViewHolder.tv_a2_chanpinjianjie=(TextView) convertView.findViewById(R.id.tv_a2_chanpinjianjie);
			ViewHolder.tv_a2_jiage=(TextView) convertView.findViewById(R.id.tv_a2_jiage);
			ViewHolder.tv_a2_jiage1=(TextView) convertView.findViewById(R.id.tv_a2_jiage1);
			ViewHolder.im_moreinfoadapter = (ImageView)convertView.findViewById(R.id.im_moreinfoadapter);
			ViewHolder.ll_b1a2_moreinfolayout = (LinearLayout)convertView.findViewById(R.id.ll_b1a2_moreinfolayout);
			ViewHolder.tv_dianpuming=(TextView) convertView.findViewById(R.id.tv_dianpuming);
			ViewHolder.tv_kucun=(TextView) convertView.findViewById(R.id.tv_kucun);
			ViewHolder.tv_xiaoliang=(TextView) convertView.findViewById(R.id.tv_xiaoliang);
			convertView.setTag(ViewHolder);
		}else{
			ViewHolder=(ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage((String)data.get(position).get("goods_image"), ViewHolder.im_a2_pic, ImageOptions.getOpstion(), animateFirstListener);
		ViewHolder.tv_a2_chanpinname.setText(data.get(position).get("goods_name"));
		ViewHolder.tv_a2_juli.setText(data.get(position).get("juli"));
		ViewHolder.tv_a2_chanpinjianjie.setText(data.get(position).get("goods_jingle"));
		ViewHolder.tv_a2_jiage.setText(data.get(position).get("goods_promotion_price"));
		
		if (data.get(position).get("is_special").equals("1")) {
			ViewHolder.tv_a2_jiage1.setVisibility(View.VISIBLE);
			ViewHolder.tv_a2_jiage1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			ViewHolder.tv_a2_jiage1.setText("￥ "+data.get(position).get("goods_price"));
		}
		ViewHolder.tv_dianpuming.setText(data.get(position).get("store_name"));
		ViewHolder.tv_kucun.setText(data.get(position).get("goods_storage"));
		ViewHolder.tv_xiaoliang.setText(data.get(position).get("goods_salenum"));
//		ViewHolder.im_moreinfoadapter.setOnClickListener(new MoreInfoListener(position,convertView));//更多按钮绑定监听
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
        public ImageView im_a2_pic;
        public TextView tv_a2_chanpinname;
        public TextView tv_a2_juli;
        public TextView tv_a2_chanpinjianjie;
        public TextView tv_a2_jiage;
        public TextView tv_a2_jiage1;
        public ImageView im_moreinfoadapter;
        public LinearLayout ll_b1a2_moreinfolayout;
        public TextView tv_dianpuming;
        public TextView tv_kucun;
        public TextView tv_xiaoliang;
    }  
}
