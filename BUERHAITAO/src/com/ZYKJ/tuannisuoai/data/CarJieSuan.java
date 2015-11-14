package com.ZYKJ.tuannisuoai.data;


/**
 * @author csh 2015年11月11日 购物车结算信息
 *
 */
public class CarJieSuan {

	private String store_id;
	private String message;//订单留言
	private String payType;//付款方式
	private String dlyoPickupType;//配送方式
	
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getDlyoPickupType() {
		return dlyoPickupType;
	}
	public void setDlyoPickupType(String dlyoPickupType) {
		this.dlyoPickupType = dlyoPickupType;
	}
}
