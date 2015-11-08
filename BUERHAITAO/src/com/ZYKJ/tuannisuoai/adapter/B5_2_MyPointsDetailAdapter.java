package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;

public class B5_2_MyPointsDetailAdapter extends BaseAdapter {

	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Activity c;
	private LayoutInflater listContainer;

	public B5_2_MyPointsDetailAdapter(Activity c, List<Map<String, String>> data) {
		this.c = c;
		this.data = data;
		listContainer = LayoutInflater.from(c);
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View cellView, ViewGroup arg2) {
		if (cellView == null) {
			cellView = listContainer.inflate(R.layout.ui_5_2_my_points_detail_items, null);
		}
		TextView add_time = (TextView) cellView.findViewById(R.id.tv_time);//增加时间
		TextView point_change = (TextView) cellView.findViewById(R.id.tv_points);//变化的积分
		TextView originOfPoints = (TextView) cellView.findViewById(R.id.tv_originOfPoints);//积分来源
		TextView totoalPoints = (TextView) cellView.findViewById(R.id.tv_totoalPoints);//总积分
		
		add_time.setText(data.get(position).get("pl_addtime"));
		if (Integer.parseInt(data.get(position).get("pl_points"))>0) {
			point_change.setText("+"+data.get(position).get("pl_points"));
		}else {
			point_change.setText(data.get(position).get("pl_points"));
		}
		
		if (data.get(position).get("pl_desc").length()<6) {
			originOfPoints.setText(data.get(position).get("pl_desc"));
		}else {
			originOfPoints.setText(data.get(position).get("pl_desc").substring(0,4));
		}
		totoalPoints.setText("总积分:"+data.get(position).get("pl_total_points"));
		
		return cellView;
	}

}
