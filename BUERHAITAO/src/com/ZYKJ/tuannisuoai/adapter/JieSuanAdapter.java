package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.CarJieSuan;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ta.utdid2.android.utils.StringUtils;

public class JieSuanAdapter extends BaseExpandableListAdapter {

	private JSONArray datas;
	private Context context;
	private LayoutInflater inflater;
	private List<CarJieSuan> object;
	private OnSelectedFreightListener listener;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public JieSuanAdapter(Context context, JSONArray datas, List<CarJieSuan> object, OnSelectedFreightListener listener) {
		this.datas = datas;
		this.context = context;
		this.object = object;
		this.listener = listener;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public JSONObject getChild(int groupPosition, int childPosition) {
		JSONObject jsonParent = datas.getJSONObject(groupPosition);
		return jsonParent.getJSONArray("goods_list").getJSONObject(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(final int groupPosition, int childPosition,boolean isLastChild, View convertView, final ViewGroup parent) {
		JSONObject jsonParent = datas.getJSONObject(groupPosition);//店铺
		JSONObject jsonChild = jsonParent.getJSONArray("goods_list").getJSONObject(childPosition);//商品
		final HolderChild viewHolder;
        if (null == convertView){
            viewHolder = new HolderChild();
            convertView = inflater.inflate(R.layout.ui_goodsjiesuan_list_items, null);
            viewHolder.childrenNameTV = (TextView) convertView.findViewById(R.id.children_name1);
			viewHolder.im_shangpuimg = (ImageView) convertView.findViewById(R.id.im_shangpuimg1);
			viewHolder.tv_spec = (TextView) convertView.findViewById(R.id.tv_spec1);
			viewHolder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price1);
			viewHolder.tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num1);
			//配送方式
			viewHolder.rl_peisongfangshi = (RelativeLayout) convertView.findViewById(R.id.rl_peisongfangshi);//点击配送
			viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);//总价格
			viewHolder.tv_shangpinshu = (TextView) convertView.findViewById(R.id.tv_shangpinshu);//数量
			viewHolder.tv_psfs = (TextView) convertView.findViewById(R.id.tv_psfs);//配送方式
			viewHolder.et_liuyan = (EditText) convertView.findViewById(R.id.et_liuyan);//留言
			viewHolder.et_liuyan.setTag(groupPosition);
			class MyTextWatcher implements TextWatcher {  
                private HolderChild mHolder; 
                public MyTextWatcher(HolderChild holder) {  
                    mHolder = holder;  
                }   
                @Override  
                public void onTextChanged(CharSequence s, int start,  
                        int before, int count) {  
                }  
                @Override  
                public void beforeTextChanged(CharSequence s, int start,  
                        int count, int after) {  
                }  
                @Override  
                public void afterTextChanged(Editable s) {  
                    if (s != null && !"".equals(s.toString())) {  
                        int position = (Integer) mHolder.et_liuyan.getTag();  
                        object.get(position).setMessage(s.toString());// 当EditText数据发生改变的时候存到data变量中  
                    }  
                }  
            }  
			viewHolder.et_liuyan.addTextChangedListener(new MyTextWatcher(viewHolder)); 
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (HolderChild) convertView.getTag();
            viewHolder.et_liuyan.setTag(groupPosition);
        }
		String goods_image_url = jsonChild.getString("goods_image_url");
		ImageLoader.getInstance().displayImage(goods_image_url, viewHolder.im_shangpuimg, ImageOptions.getOpstion(), animateFirstListener);//设置商品图片
		viewHolder.childrenNameTV.setText(jsonChild.getString("goods_name"));//设置商品名称
		viewHolder.tv_goods_price.setText("￥"+jsonChild.getString("goods_price"));//设置商品价格
		viewHolder.tv_goods_num.setText("X"+jsonChild.getString("goods_num"));//设置商品数量
		viewHolder.tv_price.setText(jsonParent.getString("store_goods_total"));//店铺总价
		viewHolder.tv_shangpinshu.setText("共"+jsonParent.getJSONArray("goods_list").size()+"件商品    合计：");//几件商品
//		viewHolder.et_liuyan.setText(object.get(groupPosition).getMessage());//留言
		String freight_price = StringUtils.isEmpty(jsonParent.getString("store_freight_price"))?"0.00":jsonParent.getString("store_freight_price");
		viewHolder.tv_psfs.setText("自提".equals(object.get(groupPosition).getDlyoPickupType())?"自提":"物流配送(配送费："+freight_price+")");
		if(jsonChild.getJSONObject("goods_spec") != null){
			String spe = jsonChild.getJSONObject("goods_spec").values().toString();
			viewHolder.tv_spec.setText(spe.substring(1,spe.length()-1));
		}
		convertView.findViewById(R.id.ll_peisongliuyan).setVisibility(isLastChild?View.VISIBLE:View.GONE);
		viewHolder.rl_peisongfangshi.setOnClickListener(new PeiSongFangShiOnClickListener(viewHolder, groupPosition, freight_price));
		viewHolder.et_liuyan.requestFocus();  
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		JSONObject jsonParent = datas.getJSONObject(groupPosition);//店铺
		return jsonParent.getJSONArray("goods_list").size();
	}

	@Override
	public JSONObject getGroup(int groupPosition) {
		return datas.getJSONObject(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return datas.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group_jiesuan_item, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.group_name);
		tv.setText(datas.getJSONObject(groupPosition).getString("store_name"));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

//	final static class HolderParent {
//		TextView groupNameTV;
//	}

	private class HolderChild{
		TextView childrenNameTV;
		ImageView im_shangpuimg;
		TextView tv_spec;
		TextView tv_goods_price;
		TextView tv_goods_num;

		RelativeLayout rl_peisongfangshi;
		EditText et_liuyan;
		TextView tv_price;
		TextView tv_shangpinshu;
		TextView tv_psfs;
    }

	class PeiSongFangShiOnClickListener implements View.OnClickListener {
		private HolderChild viewHolder;
		private String freight_price;
		private int position;
		public PeiSongFangShiOnClickListener(HolderChild viewHolder, int position, String freight_price) {
			this.viewHolder = viewHolder;
			this.freight_price = freight_price;
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_peisongfangshi:
				UIDialog.ForThreeBtn(context, new String[] {"物流配送(配送费："+freight_price+")", "自提", "取消" }, this);
				break;
			case R.id.dialog_modif_1:
				UIDialog.closeDialog();
				if(!"物流配送".equals(object.get(position).getDlyoPickupType())){
					viewHolder.tv_psfs.setText("物流配送(配送费："+freight_price+")");
					object.get(position).setDlyoPickupType("物流配送");
					listener.setOrderPrice(1, Float.valueOf(freight_price));
				}
				break;
			case R.id.dialog_modif_2:
				UIDialog.closeDialog();
				if(!"自提".equals(object.get(position).getDlyoPickupType())){
					viewHolder.tv_psfs.setText("自提");
					object.get(position).setDlyoPickupType("自提");
					listener.setOrderPrice(0, Float.valueOf(freight_price));
				}
				break;
			case R.id.dialog_modif_3:
				UIDialog.closeDialog();
				break;
			default:
				break;
			}
		}
	}
	
	public interface OnSelectedFreightListener{
		void setOrderPrice(int type, float price);//type 0-减 1-加
	}
}
