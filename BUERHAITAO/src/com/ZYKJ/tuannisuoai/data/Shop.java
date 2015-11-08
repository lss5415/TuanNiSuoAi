package com.ZYKJ.tuannisuoai.data;

import java.io.Serializable;

public class Shop implements Serializable {
	/**
	 * 商铺
	 */
	private static final long serialVersionUID = 1L;
	
	private String store_id;// 店铺id
	private String store_name;// 店铺名称
	private String store_evaluate_count;
	private String store_desccredit; 
	private String sc_name;// 分类名
	private String area_info;// 区域信息
	private String store_address;// 地址
	private String store_avatar;// 店铺头像
	private String juli;// 距离
	
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
	public String getStore_evaluate_count() {
		return store_evaluate_count;
	}
	public void setStore_evaluate_count(String store_evaluate_count) {
		this.store_evaluate_count = store_evaluate_count;
	}
	public String getStore_desccredit() {
		return store_desccredit;
	}
	public void setStore_desccredit(String store_desccredit) {
		this.store_desccredit = store_desccredit;
	}
	public String getSc_name() {
		return sc_name;
	}
	public void setSc_name(String sc_name) {
		this.sc_name = sc_name;
	}
	public String getArea_info() {
		return area_info;
	}
	public void setArea_info(String area_info) {
		this.area_info = area_info;
	}
	public String getStore_address() {
		return store_address;
	}
	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}
	public String getStore_avatar() {
		return store_avatar;
	}
	public void setStore_avatar(String store_avatar) {
		this.store_avatar = store_avatar;
	}
	public String getJuli() {
		return juli;
	}
	public void setJuli(String juli) {
		this.juli = juli;
	}
	
	
}