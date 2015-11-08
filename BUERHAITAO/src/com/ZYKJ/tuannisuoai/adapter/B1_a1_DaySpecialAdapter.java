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
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author lss 2015年6月26日 天天特价Adapter
 *
 */
public class B1_a1_DaySpecialAdapter extends BaseAdapter {
	private Activity context;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public B1_a1_DaySpecialAdapter(Activity context, List<Map<String, String>> data) {
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
			convertView=LinearLayout.inflate(context, R.layout.b1_a1_dayspecial, null);
			ViewHolder.im_b1_a1_pic1=(ImageView) convertView.findViewById(R.id.im_b1_a1_pic1);
			ViewHolder.tv_b1_a1_chanpinname1=(TextView) convertView.findViewById(R.id.tv_b1_a1_chanpinname1);
			ViewHolder.tv_b1_a1_chanpinjianjie1=(TextView) convertView.findViewById(R.id.tv_b1_a1_chanpinjianjie1);
			ViewHolder.tv_b1_a1_zhehoujia1=(TextView) convertView.findViewById(R.id.tv_b1_a1_zhehoujia1);
			ViewHolder.tv_b1_a1_yuanjia1=(TextView) convertView.findViewById(R.id.tv_b1_a1_yuanjia1);
			convertView.setTag(ViewHolder);
		}else{
			ViewHolder=(ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage((String)data.get(position).get("goods_image"), ViewHolder.im_b1_a1_pic1);
		ViewHolder.tv_b1_a1_chanpinname1.setText(data.get(position).get("goods_name"));
		ViewHolder.tv_b1_a1_chanpinjianjie1.setText(data.get(position).get("goods_jingle"));
		ViewHolder.tv_b1_a1_zhehoujia1.setText("￥"+data.get(position).get("goods_promotion_price"));
		ViewHolder.tv_b1_a1_yuanjia1.setText("￥"+data.get(position).get("goods_price"));
		ViewHolder.tv_b1_a1_yuanjia1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		
		return convertView;
	}
	public final class ViewHolder {  
        public ImageView im_b1_a1_pic1;
        public TextView tv_b1_a1_chanpinname1;
        public TextView tv_b1_a1_chanpinjianjie1;
        public TextView tv_b1_a1_zhehoujia1;
        public TextView tv_b1_a1_yuanjia1;
    }  
}
