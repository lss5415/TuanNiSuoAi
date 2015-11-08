package com.ZYKJ.tuannisuoai.adapter;

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

import com.ZYKJ.tuannisuoai.R;

public class B5_9_1_MiddleAdapter extends BaseAdapter {

	private Activity context;
	private int item = -1;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public B5_9_1_MiddleAdapter(Activity context,
			List<Map<String, String>> data) {
		this.context = context;
		this.data = data;
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
//		view.setHeight(20);
		view.setGravity(Gravity.CENTER);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		view.setWidth(dm.widthPixels / 3);
		if (item != -1 && position == item) {
			view.setBackgroundResource(R.drawable.b0_class_middleactbg);
		} else {
			view.setBackgroundResource(R.drawable.b0_class_middlebg);
		}

		view.setHeight(200);
		view.setText(data.get(position).get("area_name"));
		view.setTextColor(Color.DKGRAY);
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
