package com.ZYKJ.tuannisuoai.adapter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.ChildrenItem;
import com.ZYKJ.tuannisuoai.data.GroupItem;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class B3_ShpppingCartAdapter extends BaseExpandableListAdapter{
	private List<GroupItem> dataList;
	private LayoutInflater inflater;
	Activity activity;
	Context context;
	int ischecked;
	float qwe = 0;
	int childcheck=0;
	
	
	
	private String key;//当前登录令牌
	private float allprice=0;//总价格
	private boolean allcheckstate;//全选状态
	private int sumtiaoshu;//结算数
	private JsonHttpResponseHandler res_delete;
	private JsonHttpResponseHandler res_add;
	private RefreshExpandableList refresh;

	public B3_ShpppingCartAdapter(Context context, List<GroupItem> dataList,int ischecked,int sumtiaoshu,RefreshExpandableList refresh,String key) {
		this.dataList = dataList;//购物车列表
		this.context = context;
		this.ischecked =ischecked;
		this.sumtiaoshu = sumtiaoshu;
		this.refresh = refresh;
		this.key = key;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		/*获取子对象*/
		final GroupItem groupItem = dataList.get(groupPosition);
		if (groupItem == null || groupItem.getStore_list() == null
				|| groupItem.getStore_list().isEmpty()) {
			return null;
		}
		return groupItem.getStore_list().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,boolean isLastChild, View convertView, final ViewGroup parent) {
		final GroupItem groupItem = dataList.get(groupPosition);//购物车中的商铺
		final ChildrenItem childrenItem = groupItem.getStore_list().get(childPosition);//购物车商铺中的产品

		final ChildViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.ui_b3_shoppingcartitem,null);
			viewHolder.childrenNameTV = (TextView) convertView.findViewById(R.id.children_name);
			viewHolder.im_shangpuimg = (ImageView) convertView.findViewById(R.id.im_shangpuimg);
			viewHolder.tv_spec = (TextView)convertView.findViewById(R.id.tv_spec);
			viewHolder.tv_editguige = (TextView)convertView.findViewById(R.id.tv_editguige);
			viewHolder.tv_goods_price = (TextView)convertView.findViewById(R.id.tv_goods_price);
			viewHolder.tv_goods_num = (TextView)convertView.findViewById(R.id.tv_goods_num);
			viewHolder.editconunt = (TextView) convertView.findViewById(R.id.editconunt);
			viewHolder.jiabt = (Button)convertView.findViewById(R.id.jiabt);//加商品
			viewHolder.jianbt = (Button)convertView.findViewById(R.id.jianbt);//减商品
			viewHolder.ll_b3shopmoren = (LinearLayout)convertView.findViewById(R.id.ll_b3shopmoren);//默认
			viewHolder.ll_b3shopedit = (LinearLayout)convertView.findViewById(R.id.ll_b3shopedit);//编辑
			viewHolder.childrenCB = (CheckBox) convertView.findViewById(R.id.children_cb);//选择
			viewHolder.tv_del = (TextView)convertView.findViewById(R.id.tv_del);//删除
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder) convertView.getTag();
		}
		viewHolder.childrenNameTV.setText(childrenItem.getGoods_name());
		ImageLoader.getInstance().displayImage(childrenItem.getGoods_image_url(), viewHolder.im_shangpuimg);
		viewHolder.tv_spec.setText(childrenItem.getGoods_spec());
		viewHolder.tv_editguige.setText(childrenItem.getGoods_spec());
		viewHolder.tv_goods_price.setText(childrenItem.getGoods_price());
		viewHolder.tv_goods_num.setText("x"+childrenItem.getGoods_num());
		viewHolder.editconunt.setText(childrenItem.getGoods_num());
		viewHolder.childrenCB.setChecked(childrenItem.isChecked());//选中
		//编辑功能切换
		if (groupItem.isSelected()) {
			viewHolder.ll_b3shopmoren.setVisibility(View.GONE);
			viewHolder.ll_b3shopedit.setVisibility(View.VISIBLE);
		}else {
			viewHolder.ll_b3shopmoren.setVisibility(View.VISIBLE);
			viewHolder.ll_b3shopedit.setVisibility(View.GONE);
		}
		viewHolder.jiabt.setOnClickListener(new MyOnClickListener(groupItem,childrenItem));//加商品
		viewHolder.jianbt.setOnClickListener(new MyOnClickListener(groupItem,childrenItem));//减商品
		viewHolder.childrenCB.setOnClickListener(new MyOnClickListener(groupItem,childrenItem));
		viewHolder.tv_del.setOnClickListener(new MyOnClickListener(groupItem,childrenItem));
		return convertView;
	}

	/**
	 * 添加购物车商品
	 */
	private AsyncHttpResponseHandler res_getAddGoods(final ChildrenItem childrenItem, final int goodnum) {
		res_add = new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					if(response.getInt("code") == 200){
						int quantity = response.getJSONObject("datas").getInt("quantity");
						if(childrenItem.isChecked()){
							childrenItem.setGoods_num(quantity+"");
							finalTotalPrice();//结算总数据
						}else{
							childrenItem.setGoods_num(quantity+"");
						}
						notifyDataSetChanged();
					}else{
						Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		};
		return res_add;
	}
	
	//增加商品
	class MyOnClickListener implements View.OnClickListener {
		GroupItem groupItem;
		ChildrenItem childrenItem;
		public MyOnClickListener(final GroupItem groupItem, final ChildrenItem childrenItem) {
			this.groupItem = groupItem;
			this.childrenItem = childrenItem;
		}
		@Override
		public void onClick(View view) {
			int goodnum = 0;
			switch (view.getId()) {
			case R.id.jiabt:
				/*加商品*/
				goodnum = Integer.parseInt(childrenItem.getGoods_num());
				HttpUtils.getAddGoods(res_getAddGoods(childrenItem, goodnum+1), key, childrenItem.getCart_id(), (goodnum+1)+"");
				break;
			case R.id.jianbt:
				/*减商品*/
				goodnum = Integer.parseInt(childrenItem.getGoods_num());
				if (goodnum<2) {
					Toast.makeText(context, "受不了了，宝贝不能再减少了哦~", Toast.LENGTH_LONG).show();
				}else{
					HttpUtils.getAddGoods(res_getAddGoods(childrenItem, goodnum-1), key, childrenItem.getCart_id(), (goodnum-1)+"");
				}
				break;
			case R.id.children_cb:
				if (!childrenItem.isChecked()) {
					childrenItem.setChecked(true);
					groupItem.setChecked(true);
					for (int i = 0; i < groupItem.getStore_list().size(); i++) {
						if(!groupItem.getStore_list().get(i).isChecked()){
							groupItem.setChecked(false);
						}
					}
				} else {
					childrenItem.setChecked(false);
					groupItem.setChecked(false);
				}
				B3_ShpppingCartAdapter.this.notifyDataSetChanged();
				finalTotalPrice();//结算总数据
				break;
			case R.id.tv_del:
				new AlertDialog.Builder(context)
				.setMessage("确认要删除该商品么？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						HttpUtils.getDelete(deleteChildrenItem(groupItem, childrenItem), key,childrenItem.getCart_id());
					}
				})
				.setNegativeButton("取消", null)
				.show();
				break;
			case R.id.cb_layout:
				if(groupItem.isChecked()){
					groupItem.setChecked(false);
					for (int i = 0; i < groupItem.getStore_list().size(); i++) {
						groupItem.getStore_list().get(i).setChecked(false);
					}
				}else{
					groupItem.setChecked(true);
					for (int i = 0; i < groupItem.getStore_list().size(); i++) {
						groupItem.getStore_list().get(i).setChecked(true);
					}
				}
				B3_ShpppingCartAdapter.this.notifyDataSetChanged();
				finalTotalPrice();//结算总数据
				break;
			case R.id.tv_bianji:
				if(groupItem.isSelected()){
					groupItem.setSelected(false);
				}else{
					groupItem.setSelected(true);
				}
				B3_ShpppingCartAdapter.this.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 删除购物车商品
	 */
	private AsyncHttpResponseHandler deleteChildrenItem(final GroupItem groupItem, final ChildrenItem childrenItem) {
		if(res_delete == null){
			res_delete = new JsonHttpResponseHandler(){
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					RequestDailog.closeDialog();
					groupItem.getStore_list().remove(childrenItem);
					if(groupItem.getStore_list().size() == 0){
						dataList.remove(groupItem);
					}
					B3_ShpppingCartAdapter.this.notifyDataSetChanged();
				};
			};
		}
		return res_delete;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		final GroupItem groupItem = dataList.get(groupPosition);
		if (groupItem == null || groupItem.getStore_list() == null
				|| groupItem.getStore_list().isEmpty()) {
			return 0;
		}
		return groupItem.getStore_list().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (dataList == null) {
			return null;
		}
		return dataList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if (dataList == null) {
			return 0;
		}
		return dataList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		try {
			final GroupItem groupItem = dataList.get(groupPosition);

			final GroupViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.group_item, null);
				viewHolder.groupNameTV = (TextView) convertView.findViewById(R.id.group_name);
				viewHolder.groupCBImg = (ImageView) convertView.findViewById(R.id.group_cb_img);//选中(店铺选择)
				viewHolder.groupCBLayout = (LinearLayout) convertView.findViewById(R.id.cb_layout);
				viewHolder.tv_bianji = (TextView)convertView.findViewById(R.id.tv_bianji);
				convertView.setClickable(true);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (GroupViewHolder) convertView.getTag();
			}
			viewHolder.tv_bianji.setText(groupItem.isSelected()?"完成":"编辑");
			viewHolder.tv_bianji.setOnClickListener(new MyOnClickListener(groupItem, null));
			
			viewHolder.groupNameTV.setText(groupItem.getStore_name());
			viewHolder.groupCBImg.setSelected(groupItem.isChecked());//设置选中状态
			viewHolder.groupCBLayout.setOnClickListener(new MyOnClickListener(groupItem, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private void finalTotalPrice(){
		sumtiaoshu = 0;//结算数
		allprice = 0f;//总价格
		allcheckstate = true;//是否全选
		for (int i = 0; i < dataList.size(); i++) {
			List<ChildrenItem> childrenList = dataList.get(i).getStore_list();
			for (int j = 0; j < childrenList.size(); j++) {
				ChildrenItem childrenItem = childrenList.get(j);
				if(childrenItem.isChecked()){
					sumtiaoshu += 1;
					allprice += Float.valueOf(childrenItem.getGoods_price())*Integer.valueOf(childrenItem.getGoods_num());
				}else{
					allcheckstate = false;//如果有未选中的,则为false
				}
			}
		}
		refresh.refreshShopCarDate(allprice, allcheckstate, sumtiaoshu);
	}

	final static class GroupViewHolder {
		TextView groupNameTV;
		ImageView groupCBImg;
		LinearLayout groupCBLayout;
		TextView tv_bianji;
	}

	final static class ChildViewHolder {
		TextView childrenNameTV;
		ImageView im_shangpuimg;
		TextView tv_spec;
		TextView tv_editguige;
		TextView tv_goods_price;
		TextView tv_goods_num;
		//单个商品数量
		TextView editconunt;
		//加减数量
		Button jiabt,jianbt;
		//默认layout，编辑layout
		LinearLayout ll_b3shopmoren,ll_b3shopedit;
		CheckBox childrenCB;
		TextView tv_del;
	}
	
	public interface RefreshExpandableList{
		void refreshShopCarDate(float totalprice, boolean allcheck, int count);//刷新购物车数据
	}
}