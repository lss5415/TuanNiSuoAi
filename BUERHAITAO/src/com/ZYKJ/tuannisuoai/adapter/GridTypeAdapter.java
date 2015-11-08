package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.view.TypeDialog;

public class GridTypeAdapter extends BaseAdapter implements OnItemClickListener {
	private Context context;
	private List<Map<String, Object>> data;
	private LayoutInflater mInflater;

	public GridTypeAdapter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = mInflater.inflate(R.layout.gridview_type_item, null);
		ViewHolder holder = new ViewHolder();

		holder.icon = (ImageView) convertView
				.findViewById(R.id.gridview_type_item_icon);
		holder.title = (TextView) convertView
				.findViewById(R.id.gridview_type_item_title);
		Map<String, Object> item = data.get(position);

		// icon.setImageBitmap(BitmapFactory.decodeResource(
		// context.getResources(), (Integer) item.get("image")));
		// holder.icon.setImageBitmap(BitmapFactory.decodeResource(
		// context.getResources(), (Integer) item.get("image")));
		holder.icon.setBackgroundDrawable(context.getResources().getDrawable(
				(Integer) item.get("image")));

		holder.title.setText(context.getResources().getString(
				(Integer) item.get("title")));
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		TypeDialog.closeDialog();
	}
}
