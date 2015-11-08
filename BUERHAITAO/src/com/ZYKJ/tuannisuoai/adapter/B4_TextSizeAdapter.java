package com.ZYKJ.tuannisuoai.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;

public class B4_TextSizeAdapter extends BaseAdapter {
	private Activity context;
	List<HashMap<String, String>> shopClass;

	public B4_TextSizeAdapter(Activity context, List<HashMap<String, String>> shopClass) {
		this.context = context;
		this.shopClass=shopClass;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopClass == null ? 0 : shopClass.size();
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
			convertView=LinearLayout.inflate(context, R.layout.b4_textsizeadapter, null);
			ViewHolder.text1=(TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(ViewHolder);
		}else{
			ViewHolder=(ViewHolder) convertView.getTag();
		}
		ViewHolder.text1.setText(shopClass.get(position).get("sc_name"));
		return convertView;
	}
	
	public final class ViewHolder {
        public TextView text1;
    }  
}
