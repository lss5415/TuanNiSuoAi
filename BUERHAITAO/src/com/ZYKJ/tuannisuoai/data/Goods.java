package com.ZYKJ.tuannisuoai.data;

import java.io.Serializable;

public class Goods implements Serializable {
	/**
	 * 商品
	 */
	private static final long serialVersionUID = 1L;
	
	private String goods_id;//商品编号
	private String nc_distinct;//
	private String goods_commonid;
	private String store_id;
	private String goods_name;//商品名称
	private String goods_price;//商品价格
	private String goods_marketprice;//商品市场价
	private String goods_image;//图片名称
	private String goods_salenum;//销量
	private String evaluation_good_star;//评价星级
	private String evaluation_count;//评价数
	private String juli;//距离
	private String goods_image_url;//图片地址
	private String goods_jingle;//商品简介
	private String store_name;//店铺名称
	private String goods_storage;//库存
	private String is_special;
	private String goods_promotion_price;//特价
	
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getGoods_storage() {
		return goods_storage;
	}
	public void setGoods_storage(String goods_storage) {
		this.goods_storage = goods_storage;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getNc_distinct() {
		return nc_distinct;
	}
	public void setNc_distinct(String nc_distinct) {
		this.nc_distinct = nc_distinct;
	}
	public String getGoods_commonid() {
		return goods_commonid;
	}
	public void setGoods_commonid(String goods_commonid) {
		this.goods_commonid = goods_commonid;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
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
	public String getGoods_marketprice() {
		return goods_marketprice;
	}
	public void setGoods_marketprice(String goods_marketprice) {
		this.goods_marketprice = goods_marketprice;
	}
	public String getGoods_image() {
		return goods_image;
	}
	public void setGoods_image(String goods_image) {
		this.goods_image = goods_image;
	}
	public String getGoods_salenum() {
		return goods_salenum;
	}
	public void setGoods_salenum(String goods_salenum) {
		this.goods_salenum = goods_salenum;
	}
	public String getEvaluation_good_star() {
		return evaluation_good_star;
	}
	public void setEvaluation_good_star(String evaluation_good_star) {
		this.evaluation_good_star = evaluation_good_star;
	}
	public String getEvaluation_count() {
		return evaluation_count;
	}
	public void setEvaluation_count(String evaluation_count) {
		this.evaluation_count = evaluation_count;
	}
	public String getJuli() {
		return juli;
	}
	public void setJuli(String juli) {
		this.juli = juli;
	}
	public String getGoods_image_url() {
		return goods_image_url;
	}
	public void setGoods_image_url(String goods_image_url) {
		this.goods_image_url = goods_image_url;
	}
	public String getGoods_jingle() {
		return goods_jingle;
	}
	public void setGoods_jingle(String goods_jingle) {
		this.goods_jingle = goods_jingle;
	}
	public String getIs_special() {
		return is_special;
	}
	public void setIs_special(String is_special) {
		this.is_special = is_special;
	}
	public String getGoods_promotion_price() {
		return goods_promotion_price;
	}
	public void setGoods_promotion_price(String goods_promotion_price) {
		this.goods_promotion_price = goods_promotion_price;
	}
	
}