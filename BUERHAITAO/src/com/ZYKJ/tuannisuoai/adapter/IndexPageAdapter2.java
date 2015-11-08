package com.ZYKJ.tuannisuoai.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class IndexPageAdapter2 extends RecyclingPagerAdapter {
//	private List<Carousels> data;
	private JSONArray datsj;
	private Context context;
	// 下载图片的属性
	private DisplayImageOptions m_options;

	public IndexPageAdapter2(Context context, JSONArray datsj) {
		this.datsj = datsj;
		this.context = context;
		m_options = ImageOptions.getGoodsOptions(false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = holder.imageView = new ImageView(context);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String aaa=null;
		if (position==0) {
			try {
				aaa = datsj.getString(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (position==1) {
			try {
				aaa = datsj.getString(1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (position==2) {
			try {
				aaa = datsj.getString(2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (position==3) {
			try {
				aaa = datsj.getString(3);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (position==4) {
			try {
				aaa = datsj.getString(4);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (position==5) {
			try {
				aaa = datsj.getJSONObject(5).toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ImageLoader.getInstance().displayImage(aaa, holder.imageView);
		return convertView;
	}

	@Override
	public int getCount() {
		return datsj.length();
	}

	class ViewHolder {
		ImageView imageView;
	}

}
