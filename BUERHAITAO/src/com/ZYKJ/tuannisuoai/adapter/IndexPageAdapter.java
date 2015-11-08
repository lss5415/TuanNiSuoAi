package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ZYKJ.tuannisuoai.data.Carousels;
import com.ZYKJ.tuannisuoai.data.HttpAction;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 轮播功能的实现
 * 
 * @author bin
 * 
 */
public class IndexPageAdapter extends RecyclingPagerAdapter {
	private List<Carousels> data;
	private Context context;
	// 下载图片的属性
	private DisplayImageOptions m_options;

	public IndexPageAdapter(Context context, List<Carousels> data) {
		this.data = data;
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

		Carousels carousel = data.get(position);
		// 加载数据
		// VolleyBitmap.getGoodsImg(holder.imageView, carousel.getImgurl());

		// holder.imageView.setDefaultImageResId(R.drawable.pic_default);
		// holder.imageView.setErrorImageResId(R.drawable.pic_default);
		// holder.imageView.setImageUrl(
		// HttpAction.SERVER_IP + carousel.getImgurl(), m_loader);
		ImageLoader.getInstance().displayImage(
				HttpAction.SERVER_IP + carousel.getImgurl(), holder.imageView,
				m_options);
		return convertView;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	class ViewHolder {
		ImageView imageView;
	}

}
