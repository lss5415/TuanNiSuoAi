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
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author lss 2015年7月3日   商品评价Adapter
 *
 */
public class Sp_a1_EvalutionAdapter extends BaseAdapter {
	private Activity context;
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public Sp_a1_EvalutionAdapter(Activity context, List<Map<String, Object>> data) {
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
			convertView=LinearLayout.inflate(context, R.layout.sp_a1_evalutionitem, null);
			ViewHolder.im_sp_a1_userimage=(ImageView) convertView.findViewById(R.id.im_sp_a1_userimage);
			ViewHolder.tv_sp_a1_username=(TextView) convertView.findViewById(R.id.tv_sp_a1_username);
			ViewHolder.rating_bar_sp_a1_xiangqing=(RatingBar) convertView.findViewById(R.id.rating_bar_sp_a1_xiangqing);
			ViewHolder.tv_sp_a1_pingjia=(TextView) convertView.findViewById(R.id.tv_sp_a1_pingjia);
			ViewHolder.tv_sp_a1_chanpincanshu=(TextView) convertView.findViewById(R.id.tv_sp_a1_chanpincanshu);
			convertView.setTag(ViewHolder);
		}else{
			ViewHolder=(ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage((String)data.get(position).get("geval_avatar"), ViewHolder.im_sp_a1_userimage, ImageOptions.getOpstion(), animateFirstListener);
		ViewHolder.tv_sp_a1_username.setText(data.get(position).get("geval_frommembername").toString());
		ViewHolder.rating_bar_sp_a1_xiangqing.setRating(Float.parseFloat(data.get(position).get("geval_scores").toString()));	
		ViewHolder.tv_sp_a1_pingjia.setText(data.get(position).get("geval_content").toString());
		ViewHolder.tv_sp_a1_chanpincanshu.setText(data.get(position).get("geval_addtime").toString()+" "+data.get(position).get("geval_spec").toString());
		return convertView;
	}
	public final class ViewHolder {  
        public ImageView im_sp_a1_userimage;
        public TextView tv_sp_a1_username;
        public RatingBar rating_bar_sp_a1_xiangqing;
        public TextView tv_sp_a1_pingjia;
        public TextView tv_sp_a1_chanpincanshu;
    }  
}
