package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.Goods;
import com.ZYKJ.tuannisuoai.data.HttpAction;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页推荐商品的adapter
 * 
 * @author bin
 * 
 */
public class IndexAdapter extends BaseAdapter implements OnItemClickListener {
	private List<Goods> m_list_goods;
	private Context context;

	private LayoutInflater mInflater;
	private DisplayImageOptions m_options;

	public IndexAdapter(Context context, List<Goods> goods) {
		m_list_goods = goods;
		this.context = context;
		mInflater = LayoutInflater.from(context);

		m_options = ImageOptions.getGoodsOptions(true);
	}

	public int getCount() {
		return m_list_goods.size();
	}

	@Override
	public Object getItem(int position) {
		return m_list_goods.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_index, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.item_index_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.item_index_title);
			holder.shop_name = (TextView) convertView
					.findViewById(R.id.item_index_shopname);
			holder.shop_address = (TextView) convertView
					.findViewById(R.id.item_index_address);
			holder.shop_phone = (TextView) convertView
					.findViewById(R.id.item_index_phone);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Goods goods = m_list_goods.get(position);

		ImageLoader.getInstance().displayImage(
				HttpAction.SERVER_IP + goods.getGoods_image_url(), holder.icon,
				m_options);
		// // 重新设置商品的显示
		// int temp_width = holder.icon.getDrawable().getIntrinsicWidth();
		// int temp_height = holder.icon.getDrawable().getIntrinsicHeight();

		// int width = Tools.getTargetWidth(holder.icon);
		// Tools.Log("width:" + width);
		//
		// // 重新设置商品图片的显示大小
		// LayoutParams goods_layout = holder.icon.getLayoutParams();
		// // 设置宽度
		// goods_layout.width = Tools.dp2px(context, 100);
		// goods_layout.height = (int) (((float) Tools.dp2px(context, 100) /
		// temp_width) * temp_height);

		holder.title.setText(goods.getGoods_name());
		// holder.price.setText("￥ "+goods.getPrice());
//		holder.shop_name.setText(goods.getShop().getNick());
//		holder.shop_phone.setText("电话:" + goods.getShop().getMobile());
//		holder.shop_address.setText("地址:" + goods.getShop().getAddress());

		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
		TextView price;
		TextView shop_name;
		TextView shop_address;
		TextView shop_phone;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 跳转到商品的详情显示
//		Intent intent = new Intent(context, GoodsActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("goods", m_list_goods.get(position));
//
//		Tools.Log("获取的数据:" + m_list_goods.get(position).getGoodsname());
//		bundle.putInt("state", 0);
//		intent.putExtras(bundle);
//		context.startActivity(intent);
		Toast.makeText(context, position, Toast.LENGTH_LONG).show();
		
	}

}
