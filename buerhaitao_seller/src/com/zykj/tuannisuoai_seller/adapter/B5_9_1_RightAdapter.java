package com.zykj.tuannisuoai_seller.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class B5_9_1_RightAdapter extends BaseAdapter {

	
	private Activity context;
	private int item = -1;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	public B5_9_1_RightAdapter(Activity context, List<Map<String, String>> data) {
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
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TextView view = new TextView(context);
		view.setGravity(Gravity.CENTER);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		view.setWidth(dm.widthPixels / 3);
		view.setBackgroundColor(android.graphics.Color.parseColor("#EC9F32"));//最右边的列表颜色
		view.setHeight(200);
//		view.setHeight(dm.heightPixels/10);
//		view.setHeight(200);
		view.setText(data.get(position).get("area_name"));
		view.setTextColor(Color.BLACK);
		view.setTag(data.get(position).get("area_id"));
		return view;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
}
