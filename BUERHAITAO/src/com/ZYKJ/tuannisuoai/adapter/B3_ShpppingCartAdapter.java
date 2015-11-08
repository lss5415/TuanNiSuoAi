package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.UI.B3_ShoppingCartActivity;
import com.ZYKJ.tuannisuoai.data.ChildrenItem;
import com.ZYKJ.tuannisuoai.data.GroupItem;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class B3_ShpppingCartAdapter extends BaseExpandableListAdapter {
	private List<GroupItem> dataList;
	private LayoutInflater inflater;
	// 以选中的子列表项
	private List<String> checkedChildren = new ArrayList<String>();
	// 父列表项的选中状态：value值为1（选中）、2（部分选中）、3（未选中）
	private Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();
	int bianjistate = 0;
	Map<Integer, Boolean> listcarld;
	Activity activity;
	Context context;
//	CheckBox dadcheck;
	int ischecked;
	ChangedPrice changedprice;
	IsAllChecked isallchecked;
	float allprice=0;//总价格
//	CheckBox childrenCB;
	int allcheckstate;
	float qwe = 0;
	int sumtiaoshu;
	JieSuanCount jiesuancount;
	int childcheck=0;
	String key;

	public B3_ShpppingCartAdapter(Context context, List<GroupItem> dataList,int ischecked,int sumtiaoshu,ChangedPrice changedprice,IsAllChecked isallchecked,JieSuanCount jiesuancount,String key) {
		this.dataList = dataList;
		this.context = context;
		this.ischecked =ischecked;
		this.changedprice = changedprice;
		this.isallchecked = isallchecked;
		this.sumtiaoshu = sumtiaoshu;
		this.jiesuancount = jiesuancount;
		this.key = key;
		inflater = LayoutInflater.from(context);	
		listcarld = new HashMap<Integer, Boolean>();
		// 默认设置所有的父列表项和子列表项的选中状态(1为全选)
		setIschecked(ischecked);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
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
		final ChildrenItem childrenItem = (ChildrenItem) getChild(groupPosition,
				childPosition);

		ChildViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.ui_b3_shoppingcartitem,null);
			viewHolder.childrenNameTV = (TextView) convertView.findViewById(R.id.children_name);
			viewHolder.im_shangpuimg = (ImageView) convertView.findViewById(R.id.im_shangpuimg);
			viewHolder.tv_spec = (TextView)convertView.findViewById(R.id.tv_spec);
			viewHolder.tv_editguige = (TextView)convertView.findViewById(R.id.tv_editguige);
			viewHolder.tv_goods_price = (TextView)convertView.findViewById(R.id.tv_goods_price);
			viewHolder.tv_goods_num = (TextView)convertView.findViewById(R.id.tv_goods_num);
			viewHolder.editconunt = (EditText) convertView.findViewById(R.id.editconunt);
			viewHolder.jiabt = (Button)convertView.findViewById(R.id.jiabt);
			viewHolder.jianbt = (Button)convertView.findViewById(R.id.jianbt);
			viewHolder.ll_b3shopmoren = (LinearLayout)convertView.findViewById(R.id.ll_b3shopmoren);
			viewHolder.ll_b3shopedit = (LinearLayout)convertView.findViewById(R.id.ll_b3shopedit);
			viewHolder.childrenCB = (CheckBox) convertView.findViewById(R.id.children_cb);
			viewHolder.tv_del = (TextView)convertView.findViewById(R.id.tv_del);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(context)
				.setMessage("确认要删除该商品么？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						int a = dataList.size();
						HttpUtils.getDelete(res_delete,key,childrenItem.getCart_id());
						Intent itmrhd = new Intent();
						itmrhd.setClass(context, B3_ShoppingCartActivity.class);
						context.startActivity(itmrhd);
//						dataList.remove(childrenItem.getCart_id());
//						childrenItem
//						int b = dataList.size();
//						B3_ShpppingCartAdapter.this.notifyDataSetChanged();
					}
				})
				.setNegativeButton("取消", null)  
				.show(); 
				
			}
		});

		viewHolder.childrenNameTV.setText(childrenItem.getGoods_name());
		ImageLoader.getInstance().displayImage(childrenItem.getGoods_image_url(), viewHolder.im_shangpuimg);
		viewHolder.tv_spec.setText(childrenItem.getGoods_spec());
		viewHolder.tv_editguige.setText(childrenItem.getGoods_spec());
		viewHolder.tv_goods_price.setText(childrenItem.getGoods_price());
		viewHolder.tv_goods_num.setText("x"+childrenItem.getGoods_num());
		viewHolder.editconunt.setText(childrenItem.getGoods_num());
		final String childrenId = childrenItem.getCart_id();
		//编辑功能切换
		if (listcarld.get(groupPosition)==true) {
			viewHolder.ll_b3shopmoren.setVisibility(View.GONE);
			viewHolder.ll_b3shopedit.setVisibility(View.VISIBLE);
		}else {
			viewHolder.ll_b3shopmoren.setVisibility(View.VISIBLE);
			viewHolder.ll_b3shopedit.setVisibility(View.GONE);
		}
		viewHolder.jiabt.setOnClickListener(new JiaBtOnClickListener(viewHolder,childrenItem));
		viewHolder.jianbt.setOnClickListener(new JianBtOnClickListener(viewHolder,childrenItem));
		viewHolder.childrenCB.setOnClickListener(new childrenCBOnClickListener(viewHolder,childrenItem));
		viewHolder.childrenCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {			
						if (isChecked) {
							if (!checkedChildren.contains(childrenId)) {
								checkedChildren.add(childrenId);
							}
						} else {
							checkedChildren.remove(childrenId);
						}
						setGroupItemCheckedState(dataList.get(groupPosition));
						CheckIsAllCheck();
						jiesuancount.JieSuanCount(checkedChildren.size());
						B3_ShpppingCartAdapter.this.notifyDataSetChanged();
					}
				});
		if (checkedChildren.contains(childrenId)) {
			viewHolder.childrenCB.setChecked(true);
		} else {
			viewHolder.childrenCB.setChecked(false);
		}
		return convertView;
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

			GroupViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.group_item, null);
				viewHolder.groupNameTV = (TextView) convertView.findViewById(R.id.group_name);
				viewHolder.groupCBImg = (ImageView) convertView.findViewById(R.id.group_cb_img);
				viewHolder.groupCBLayout = (LinearLayout) convertView.findViewById(R.id.cb_layout);
				viewHolder.tv_bianji = (TextView)convertView.findViewById(R.id.tv_bianji);
				convertView.setClickable(true);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (GroupViewHolder) convertView.getTag();
			}

//			dadcheck = (CheckBox) convertView.findViewById(R.id.dadcheck);
			viewHolder.tv_bianji.setOnClickListener(new BanJiOnClickListener(viewHolder,groupPosition));
			
			viewHolder.groupCBLayout.setOnClickListener(new GroupCBLayoutOnClickListener(groupItem));
//			viewHolder.groupCBLayout.setOnClickListener(new GroupCBLayoutOnClickListener(groupItem));
			viewHolder.groupNameTV.setText(groupItem.getStore_name());
			int state = groupCheckedStateMap.get(groupItem.getStore_id());
			switch (state) {
			case 1:
//				dadcheck.setChecked(true);
				viewHolder.groupCBImg.setImageResource(R.drawable.ck_checked);
				break;
			case 2:
//				dadcheck.setChecked(false);
//				viewHolder.groupCBImg.setImageResource(R.drawable.ck_partial_checked);
				viewHolder.groupCBImg.setImageResource(R.drawable.ck_unchecked);
				break;
			case 3:
//				dadcheck.setChecked(false);
				viewHolder.groupCBImg.setImageResource(R.drawable.ck_unchecked);
				break;
			default:
				break;
			}
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

	private void setGroupItemCheckedState(GroupItem groupItem) {
		List<ChildrenItem> childrenItems = groupItem.getStore_list();
		if (childrenItems == null || childrenItems.isEmpty()) {
			groupCheckedStateMap.put(groupItem.getStore_id(), 3);
			return;
		}

		int checkedCount = 0;
		for (ChildrenItem childrenItem : childrenItems) {
			if (checkedChildren.contains(childrenItem.getCart_id())) {
				checkedCount++;
			}
		}
		int state = 1;
		if (checkedCount == 0) {
			state = 3;
		} else if (checkedCount == childrenItems.size()) {
			state = 1;
		} else {
			state = 2;
		}

		groupCheckedStateMap.put(groupItem.getStore_id(), state);
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
		EditText editconunt;
		//加减数量
		Button jiabt,jianbt;
		//默认layout，编辑layout
		LinearLayout ll_b3shopmoren,ll_b3shopedit;
		CheckBox childrenCB;
		TextView tv_del;
	}

	//编辑
	class BanJiOnClickListener implements View.OnClickListener {
		GroupViewHolder viewHolder;
		int groupPosition;
		public BanJiOnClickListener(GroupViewHolder viewHolder,int groupPosition) {
			this.viewHolder = viewHolder;
			this.groupPosition = groupPosition;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_bianji:
				if (bianjistate>0) {
					bianjistate=0;
					listcarld.put(groupPosition, false);
					viewHolder.tv_bianji.setText("编辑");
					B3_ShpppingCartAdapter.this.notifyDataSetChanged();
					
//					ll_b3shopmoren.setVisibility(View.VISIBLE);
//					ll_b3shopedit.setVisibility(View.GONE);
				}else {
					bianjistate=1;
					listcarld.put(groupPosition, true);
					viewHolder.tv_bianji.setText("完成");
					B3_ShpppingCartAdapter.this.notifyDataSetChanged();
//					ll_b3shopmoren.setVisibility(View.GONE);
//					ll_b3shopedit.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	}

	
	class childrenCBOnClickListener implements View.OnClickListener {
		ChildViewHolder viewHolder;
		ChildrenItem childrenItem;
		public childrenCBOnClickListener(ChildViewHolder viewHolder,ChildrenItem childrenItem) {
			this.viewHolder = viewHolder;
			this.childrenItem = childrenItem;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.children_cb:
				if (viewHolder.childrenCB.isChecked()) {
					float price = Float.parseFloat(childrenItem.getGoods_price())*Float.parseFloat(childrenItem.getGoods_num());
					allprice = allprice+price;
					changedprice.ChangePr(allprice);
				}else {
					float price = Float.parseFloat(childrenItem.getGoods_price())*Float.parseFloat(childrenItem.getGoods_num());
					allprice = allprice-price;
					changedprice.ChangePr(allprice);
				}
				
				break;
			default:
				break;
			}
		}
	}

	//减少商品
	class JianBtOnClickListener implements View.OnClickListener {
		ChildViewHolder viewHolder;
		ChildrenItem childrenItem;
		public JianBtOnClickListener(ChildViewHolder viewHolder,ChildrenItem childrenItem) {
			this.viewHolder = viewHolder;
			this.childrenItem = childrenItem;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.jianbt:
				int goodnum = Integer.parseInt(childrenItem.getGoods_num());
				if (goodnum<2) {
					Toast.makeText(context, "受不了了，宝贝不能再减少了哦~", Toast.LENGTH_LONG).show();
				}else {
					String nums = (Integer.toString(goodnum-1));
					childrenItem.setGoods_num(nums);
					viewHolder.editconunt.setText(childrenItem.getGoods_num());
					HttpUtils.getAddGoods(res_add,key,childrenItem.getCart_id(),childrenItem.getGoods_num());
					B3_ShpppingCartAdapter.this.notifyDataSetChanged();
					if (viewHolder.childrenCB.isChecked()==true) {
						float price = Float.parseFloat(childrenItem.getGoods_price());
						allprice = allprice-price;
						changedprice.ChangePr(allprice);
					}
				}
				break;
			default:
				break;
			}
		}
	}
	
	//增加商品
	class JiaBtOnClickListener implements View.OnClickListener {
		ChildViewHolder viewHolder;
		ChildrenItem childrenItem;
		public JiaBtOnClickListener(ChildViewHolder viewHolder,ChildrenItem childrenItem) {
			this.viewHolder = viewHolder;
			this.childrenItem = childrenItem;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.jiabt:
				int goodnum = Integer.parseInt(childrenItem.getGoods_num());
				String nums = (Integer.toString(goodnum+1));
				childrenItem.setGoods_num(nums);
				viewHolder.editconunt.setText(childrenItem.getGoods_num());;
				HttpUtils.getAddGoods(res_add,key,childrenItem.getCart_id(),childrenItem.getGoods_num());
				B3_ShpppingCartAdapter.this.notifyDataSetChanged();
				if (viewHolder.childrenCB.isChecked()==true) {
					float price  =Float.parseFloat(childrenItem.getGoods_price());
						allprice = allprice+price;
						changedprice.ChangePr(allprice);
				}
				break;
			default:
				break;
			}
		}
	}
	
	//点击店铺的CheckBox点击 
	public class GroupCBLayoutOnClickListener implements OnClickListener {

		private GroupItem groupItem;

		public GroupCBLayoutOnClickListener(GroupItem groupItem) {
			this.groupItem = groupItem;
		}

		@Override
		public void onClick(View v) {
			List<ChildrenItem> childrenItems = groupItem.getStore_list();
			if (childrenItems == null || childrenItems.isEmpty()) {
				groupCheckedStateMap.put(groupItem.getStore_id(), 3);
//				dadcheck.setChecked(false);
				return;
			}
			int checkedCount = 0;
			for (ChildrenItem childrenItem : childrenItems) {
				if (checkedChildren.contains(childrenItem.getCart_id())) {
					checkedCount++;
				}
			}

			boolean checked = false;
			if (checkedCount == childrenItems.size()) {
				checked = false;
				groupCheckedStateMap.put(groupItem.getStore_id(), 3);
//				dadcheck.setChecked(false);
				//店铺全选的价格修改
				for (int i = 0; i < childrenItems.size(); i++) {
					allprice = allprice - Float.parseFloat(childrenItems.get(i).getGoods_price())*Float.parseFloat(childrenItems.get(i).getGoods_num());
				}
				changedprice.ChangePr(allprice);
				
				
			} else {
				checked = true;
				groupCheckedStateMap.put(groupItem.getStore_id(), 1);
//				dadcheck.setChecked(true);
				//店铺全选的价格修改
				//加上已经选择的

				//减掉已经选择的
				for (int j = 0; j < checkedChildren.size(); j++) {
					for (int i = 0; i < childrenItems.size(); i++) {
						String asas=childrenItems.get(i).getCart_id().toString();
						String asdfsd=checkedChildren.get(j).toString();
						if (asas.equals(asdfsd)) {
							allprice = allprice-Float.parseFloat(childrenItems.get(i).getGoods_price())*Float.parseFloat(childrenItems.get(i).getGoods_num());
						}
					}
				} 
				for (int i = 0; i < childrenItems.size(); i++) {
					allprice = allprice + Float.parseFloat(childrenItems.get(i).getGoods_price())*Float.parseFloat(childrenItems.get(i).getGoods_num());
				}
				changedprice.ChangePr(allprice);
			}

			for (ChildrenItem childrenItem : childrenItems) {
				String holderKey = childrenItem.getCart_id();
				if (checked) {
					if (!checkedChildren.contains(holderKey)) {
						checkedChildren.add(holderKey);
					}
				} else {
					checkedChildren.remove(holderKey);
				}
			}
			CheckIsAllCheck();
			jiesuancount.JieSuanCount(checkedChildren.size());
			B3_ShpppingCartAdapter.this.notifyDataSetChanged();
		}
	}
	
	/**
	 * 删除购物车商品
	 */
	JsonHttpResponseHandler res_delete = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			B3_ShpppingCartAdapter.this.notifyDataSetChanged();
		};
	};
	
	/**
	 * 添加购物车商品
	 */
	JsonHttpResponseHandler res_add = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			
			B3_ShpppingCartAdapter.this.notifyDataSetChanged();
		};
	};
	

	public List<String> getCheckedRecords() {
		return checkedChildren;
	}

	public List<String> getCheckedChildren() {
		return checkedChildren;
	}

	public int getIschecked() {
		return ischecked;
		
	}

	public void setIschecked(int ischecked) {
		int groupCount = getGroupCount();
		for (int groupPosition = 0; groupPosition < groupCount; groupPosition++) {
			try {
				GroupItem groupItem = dataList.get(groupPosition);
				if (groupItem == null || groupItem.getStore_list() == null
						|| groupItem.getStore_list().isEmpty()) {
					groupCheckedStateMap.put(groupItem.getStore_id(), 3);
					continue;
				}	
				if (ischecked==0) {
					groupCheckedStateMap.put(groupItem.getStore_id(), 3);
					List<ChildrenItem> childrenItems = groupItem.getStore_list();
					for (ChildrenItem childrenItem : childrenItems) {
						checkedChildren.clear();		
					}
					listcarld.put(groupPosition,false);					
				}else {
					groupCheckedStateMap.put(groupItem.getStore_id(), 1);
					List<ChildrenItem> childrenItems = groupItem.getStore_list();
					for (ChildrenItem childrenItem : childrenItems) {
						checkedChildren.add(childrenItem.getCart_id());			
					}
					listcarld.put(groupPosition,false);

				}

			} catch (Exception e) {

			}
		}
		this.ischecked = ischecked;
		
	}	
	
	//用于刷新Activity价格
	public interface ChangedPrice{
		void ChangePr(Float totalprice);
		
	}
	//用于判断是否全选
	public interface IsAllChecked{
		void IsAllCheck(int allcheck);
	}
	//用于判断结算的条数
	public interface JieSuanCount{
		void JieSuanCount(int count);
	}
	
	//检查是否全选
	public void CheckIsAllCheck(){
//		int a = checkedChildren.size();
//		Toast.makeText(context, ""+a, Toast.LENGTH_LONG).show();
		if (sumtiaoshu==checkedChildren.size()) {
			isallchecked.IsAllCheck(1);
		}else {
			isallchecked.IsAllCheck(0);
		}
	}
}