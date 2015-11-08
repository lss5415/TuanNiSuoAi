package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.google.gson.Gson;

public class JieSuanAdapter extends BaseAdapter {

	List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Activity c;
	String key;
	JieSuanShangPinAdapter adapter;

	public JieSuanAdapter(Activity c, List<Map<String, Object>> data1,
			List<Map<String, String>> data, String key) {
		this.c = c;
		this.data = data;
		this.data1 = data1;
		this.key = key;
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
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(c);
			convertView = mInflater.inflate(R.layout.ui_jiesuan_list_items,
					null);
			viewHolder.tv_storejsname = (TextView) convertView
					.findViewById(R.id.tv_storejsname);
			viewHolder.listview_jiesuanlist = (ListView) convertView
					.findViewById(R.id.listview_jiesuanlist);
			viewHolder.rl_peisongfangshi = (RelativeLayout) convertView
					.findViewById(R.id.rl_peisongfangshi);
			viewHolder.et_liuyan = (EditText) convertView
					.findViewById(R.id.et_liuyan);
			viewHolder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			viewHolder.tv_shangpinshu = (TextView) convertView
					.findViewById(R.id.tv_shangpinshu);
			viewHolder.tv_psfs = (TextView) convertView
					.findViewById(R.id.tv_psfs);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_storejsname.setText(data.get(position).get("store_name")
				.toString());// 店铺名
		viewHolder.tv_price.setText(data.get(position).get("store_goods_total")
				.toString());
		Gson gson = new Gson();
		String jsa = gson.toJson(data1.get(position).get("goods_list"));
		try {
			JSONObject dataJson = new JSONObject(jsa);
			org.json.JSONArray array = dataJson.getJSONArray("values");
			adapter = new JieSuanShangPinAdapter(c,array);
			viewHolder.listview_jiesuanlist.setAdapter(adapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String jsonStr = gson.toJson(data1.get(position).get("goods_list"));
		// JSONObject jobdata = (JSONObject)
		// data1.get(position).get("goods_list");

		// adapter = new JieSuanShangPinAdapter(c,jsonStr);
		// viewHolder.listview_jiesuanlist.setAdapter(adapter);
		viewHolder.rl_peisongfangshi
				.setOnClickListener(new PeiSongFangShiOnClickListener(
						viewHolder));
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_storejsname;
		ListView listview_jiesuanlist;
		RelativeLayout rl_peisongfangshi;
		EditText et_liuyan;
		TextView tv_price;
		TextView tv_shangpinshu;
		TextView tv_psfs;
	}

	class PeiSongFangShiOnClickListener implements View.OnClickListener {
		ViewHolder viewHolder;

		public PeiSongFangShiOnClickListener(ViewHolder viewHolder) {
			this.viewHolder = viewHolder;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rl_peisongfangshi:
				UIDialog.ForThreeBtn(c, new String[] { "物流配送", "自提", "取消" },
						this);
				break;
			case R.id.dialog_modif_1:
				UIDialog.closeDialog();
				viewHolder.tv_psfs.setText("物流配送");

				break;
			case R.id.dialog_modif_2:
				UIDialog.closeDialog();
				viewHolder.tv_psfs.setText("自提");

				break;
			case R.id.dialog_modif_3:
				UIDialog.closeDialog();
				viewHolder.tv_psfs.setText("");

				break;
			default:
				break;
			}
		}
	}

}
