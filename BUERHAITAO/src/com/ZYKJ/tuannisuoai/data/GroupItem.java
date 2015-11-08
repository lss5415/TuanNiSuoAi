package com.ZYKJ.tuannisuoai.data;

import java.util.List;

/**
 * @author lss 2015年7月14日 购物车店铺信息
 *
 */
public class GroupItem {

	private String store_id;
	private String store_name;
	private List<ChildrenItem> store_list;
	
	
	public GroupItem() {
	}
	
	public GroupItem(String store_id,String store_name,List<ChildrenItem> childrenItems) {
		this.store_id = store_id;
		this.store_name = store_name;
		this.store_list = childrenItems;
	}

	public List<ChildrenItem> getStore_list() {
		return store_list;
	}


	public void setStore_list(List<ChildrenItem> store_list) {
		this.store_list = store_list;
	}


	public String getStore_id() {
		return store_id;
	}


	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}


	public String getStore_name() {
		return store_name;
	}


	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	
	
	
}
