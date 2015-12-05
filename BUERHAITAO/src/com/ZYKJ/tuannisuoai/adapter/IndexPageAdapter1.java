package com.ZYKJ.tuannisuoai.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 轮播功能的实现
 * 
 * @author bin
 * 
 */
public class IndexPageAdapter1 extends RecyclingPagerAdapter {
//	private List<Carousels> data;
	private JSONObject datsj;
	private Context context;
	// 下载图片的属性
	private DisplayImageOptions m_options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public IndexPageAdapter1(Context context, JSONObject datsj) {
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
				aaa = datsj.getString("0").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (position==1) {
			try {
				aaa = datsj.getString("1").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (position==2) {
			try {
				aaa = datsj.getString("2").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (position==3) {
			try {
				aaa = datsj.getString("3").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (position==4) {
			try {
				aaa = datsj.getString("4").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (position==5) {
			try {
				aaa = datsj.getString("5").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		Carousels carousel = data.get(position);
		// 加载数据
		// VolleyBitmap.getGoodsImg(holder.imageView, carousel.getImgurl());

		// holder.imageView.setDefaultImageResId(R.drawable.pic_default);
		// holder.imageView.setErrorImageResId(R.drawable.pic_default);
		// holder.imageView.setImageUrl(
		// HttpAction.SERVER_IP + carousel.getImgurl(), m_loader);
//		ImageLoader.getInstance().displayImage(aaa, holder.imageView);
		ImageLoader.getInstance().displayImage(aaa, holder.imageView, ImageOptions.getOpstion(), animateFirstListener);
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
