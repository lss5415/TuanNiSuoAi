package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author lss 2015年6月24日 每日好店adapter
 *
 */
public class B1_a3_MeiRiHaoDianAdapter  extends BaseAdapter {
	private Activity context;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public B1_a3_MeiRiHaoDianAdapter(Activity context, List<Map<String, String>> data) {
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
			convertView=LinearLayout.inflate(context, R.layout.b1_a3_item, null);
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
		ImageLoader.getInstance().displayImage((String)data.get(position).get("store_label"), ViewHolder.im_a3_pic);
		ViewHolder.tv_a3_storename.setText(data.get(position).get("store_name"));
		ViewHolder.tv_a3_juli.setText(data.get(position).get("juli"));
		ViewHolder.comment_rating_bar.setRating(Float.parseFloat(data.get(position).get("store_desccredit")));
		ViewHolder.tv_a3_pinglunsum.setText(data.get(position).get("store_evaluate_count"));
		ViewHolder.tv_a3_dpfl.setText(data.get(position).get("sc_name"));
		ViewHolder.tv_a3_address.setText(data.get(position).get("store_address"));
		return convertView;
	}
	
	public final class ViewHolder {  
        public ImageView im_a3_pic;
        public TextView tv_a3_storename;
        public TextView tv_a3_juli;
        public RatingBar comment_rating_bar;
        public TextView tv_a3_pinglunsum;
        public TextView tv_a3_dpfl;
        public TextView tv_a3_address;
    }  
}
