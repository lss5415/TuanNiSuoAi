package com.ZYKJ.tuannisuoai.data;

/**
 * @author lss 2015年7月14日 购物车商品信息
 *
 */
public class ChildrenItem {
	private String cart_id;
	private String buyer_id;
	private String store_id;
	private String store_name;
	private String goods_id;
	private String goods_name;
	private String goods_price;
	private String goods_num;
	private String state;
	private String goods_jingle;
	private String goods_spec;
	private String goods_freight;
	private String goods_storage;
	private String goods_image_url;
	private String goods_sum;
	private String goods_total;
	
	private boolean isChecked;//是否选中

	public ChildrenItem() {
	}

	
	public ChildrenItem(String cart_id,String buyer_id, String store_id, String store_name, String goods_id, String goods_name, String goods_price, String goods_num, String state, String goods_jingle, String goods_spec, String goods_freight, String goods_storage, String goods_image_url, String goods_sum,String goods_total) {
		this.cart_id = cart_id;
		this.buyer_id = buyer_id;
		this.store_id = store_id;
		this.store_name = store_name;
		this.goods_id = goods_id;
		this.goods_name = goods_name;
		this.goods_price = goods_price;
		this.goods_num = goods_num;
		this.state = state;
		this.goods_jingle = goods_jingle;
		this.goods_spec = goods_spec;
		this.goods_freight = goods_freight;
		this.goods_storage = goods_storage;
		this.goods_image_url = goods_image_url;
		this.goods_sum = goods_sum;
		this.goods_total = goods_total;
	}


	public String getCart_id() {
		return cart_id;
	}


	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
	}


	public String getBuyer_id() {
		return buyer_id;
	}


	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
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


	public String getGoods_id() {
		return goods_id;
	}


	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}


	public String getGoods_name() {
		return goods_name;
	}


	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}


	public String getGoods_price() {
		return goods_price;
	}


	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}


	public String getGoods_num() {
		return goods_num;
	}


	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getGoods_jingle() {
		return goods_jingle;
	}


	public void setGoods_jingle(String goods_jingle) {
		this.goods_jingle = goods_jingle;
	}


	public String getGoods_spec() {
		return goods_spec;
	}


	public void setGoods_spec(String goods_spec) {
		this.goods_spec = goods_spec;
	}


	public String getGoods_freight() {
		return goods_freight;
	}


	public void setGoods_freight(String goods_freight) {
		this.goods_freight = goods_freight;
	}


	public String getGoods_storage() {
		return goods_storage;
	}


	public void setGoods_storage(String goods_storage) {
		this.goods_storage = goods_storage;
	}


	public String getGoods_image_url() {
		return goods_image_url;
	}


	public void setGoods_image_url(String goods_image_url) {
		this.goods_image_url = goods_image_url;
	}


	public String getGoods_sum() {
		return goods_sum;
	}


	public void setGoods_sum(String goods_sum) {
		this.goods_sum = goods_sum;
	}


	public String getGoods_total() {
		return goods_total;
	}


	public void setGoods_total(String goods_total) {
		this.goods_total = goods_total;
	}


	public boolean isChecked() {
		return isChecked;
	}


	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
