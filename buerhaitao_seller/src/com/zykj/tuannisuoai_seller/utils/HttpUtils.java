package com.zykj.tuannisuoai_seller.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.zykj.tuannisuoai_seller.data.AppValue;
/**
 * @author 作者 zhang
 * @version 创建时间：2015年1月6日 上午10:33:29 类说明 AsyncHttp 异步联网第三方库
 */
public class HttpUtils {
//	public static final String base_url = "http://115.28.21.137/mobile/";
	public static final String base_url = "http://115.28.21.137:89/mobile/";
	
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	static {
		client.setTimeout(5000); // 设置链接超时，如果不设置，默认为15s
		client.setMaxRetriesAndTimeout(3, 5000);
		// client.setEnableRedirects(true);

	}

	public static void initClient(Context c) {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(c);
		client.setCookieStore(myCookieStore);
	}

	public static AsyncHttpClient getClient() {

		return client;
	}

	/**
	 * 1登录
	 * 
	 * @param res
	 * @param loginname
	 * @param pwd
	 */

	public static void login(AsyncHttpResponseHandler res, String loginname,
			String pwd) {
		String url = null;
		url = base_url + "index.php?act=seller_login";
		RequestParams requestParams  = new RequestParams();
		requestParams.put("mobile", loginname);
		requestParams.put("password", pwd);
		requestParams.put("client", AppValue.CLIENT);
		client.post(url,requestParams,res);

	}

	/**
	 * 2注册
	 * 
	 * @param res
	 * @param mobile
	 *            必选 用户名
	 * @param password
	 *            必选 密码
	 * @param verify_code
	 *            必选 验证码
	 * @param client
	 *            必选 客户端类型：android
	 */
	public static void regist(AsyncHttpResponseHandler res, String mobile ,
			String password , String verify_code) {
		String url = null;
		url = base_url + "index.php?act=login&op=register";
		RequestParams requestParams  = new RequestParams();
		requestParams.put("mobile", mobile);
		requestParams.put("password", password);
		requestParams.put("verify_code", verify_code);
		requestParams.put("client", AppValue.CLIENT);
		client.post(url,requestParams,res);
	}
	/**
	 * 注销
	 * @param res
	 * @param mobile
	 *            必选 手机号
	 * @param key 
	 *            必选 当前登录令牌
	 * @param client
	 *            必选 客户端类型：android
	 */
	public static void logout(AsyncHttpResponseHandler res, String mobile ,
			String key) {
		String url = null;
		url = base_url + "index.php?act=seller_logout";
		RequestParams requestParams  = new RequestParams();
		requestParams.put("mobile", mobile);
		requestParams.put("key", key);
		requestParams.put("client", AppValue.CLIENT);
		client.post(url,requestParams,res);
	}

	/**
	 * 获取首页信息
	 * 
	 * @param res
	 */
	public static void getFirstList(AsyncHttpResponseHandler res,String key) {
//		String url = base_url + "getSpecialList";
		String url = null;
		url = base_url + "index.php?act=store_setting&op=index"+"&key="+key;
		client.get(url, res);
	}

	/**
	 * 4首页商品列表
	 * 
	 * @param res
	 */
	public static void getHomeGoods(AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{

		String url = base_url + "getHomeGoods";
		client.get(url, res);
		Log.i("landousurl", url);
	}
	/**
	 * 首页编辑
	 * @param res_getGoodsList
	 * @param user_name
	 * @param key
	 */
	public static void editHomePage(AsyncHttpResponseHandler res,String key,String slide,String avatar,String store_name,
			String province_id,String city_id,String area_info,String store_address,String lng,String lat ,String location,
			String store_phone,String store_freight_price) {
		String url = base_url + "index.php?act=store_setting&op=save";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("slide",slide);
		params.put("avatar",avatar);
		params.put("store_name",store_name);
		params.put("province_id",province_id);
		params.put("city_id",city_id);
		params.put("area_info",area_info);
		params.put("store_address",store_address);
		params.put("lng",lng);
		params.put("lat",lat);
		params.put("location",location);
		params.put("store_phone",store_phone);
		params.put("store_freight_price",store_freight_price);
		client.post(url, params, res);
	}

	/**
	 * 5获取商品分类 gc_id默认为0
	 * 
	 * @param res
	 * @param gc_id
	 */
	public static void getGoodsClass(AsyncHttpResponseHandler res,String gc_id) {
		String url = base_url + "index.php?act=goods_class"+(gc_id != null?"&gc_id="+gc_id:"");
		client.get(url, res);
	}

	/**
	 * 6获取商品列表
	 * 
	 * @param res_getGoodsList
	 * 
	 * @param gc_id
	 *            类别排序
	 * @param gc_id
	 *            店铺
	 */
	public static void getGoodsList(AsyncHttpResponseHandler res_getGoodsList, String params) {
		String url = base_url + "index.php?act=goods&op=goods_list"+params;
		client.get(url, res_getGoodsList);
	}
	/**
	 * 修改昵称
	 * @param res_getGoodsList
	 * @param user_name
	 * @param key
	 */
	public static void editname(AsyncHttpResponseHandler res,String user_name,String key) {
		String url = base_url + "index.php?act=member_index&op=editname";
		RequestParams params = new RequestParams();
		params.put("user_name", user_name);
		params.put("key", key);
		client.post(url, params, res);
	}
	

	/**
	 * 7获取商品详情
	 * 
	 * @param goods_id
	 * @param getGoodsDetail
	 */
	public static void getGoodsDetail(String goods_id,
			AsyncHttpResponseHandler getGoodsDetail) {
		String url = base_url + "getGoodsDetail&goods_id=" + goods_id;
		client.get(url, getGoodsDetail);
	}

	/**
	 * 8获取商品评价
	 * 
	 * @param res
	 * @param goods_id
	 */
	public static void getGoodsComments(AsyncHttpResponseHandler res,
			String goods_id) {
		String url = base_url + "getGoodsComments&goods_id=" + goods_id;
		client.get(url, res);
	}

	/**
	 * 9获取商店列表
	 * 
	 * @param params
	 */
	public static void getStoreList(AsyncHttpResponseHandler res_getStoreList, String params) {
		String url = base_url + "index.php?act=store&op=store_list"+params;
		client.get(url, res_getStoreList);
	}

	/**
	 * 10获取店铺分类商品列表。。
	 * 
	 * @param res
	 * @param store_id
	 */
	public static void getStoreClass(AsyncHttpResponseHandler res) {
		String url = base_url + "index.php?act=store&op=store_class";
		client.get(url, res);
	}

	/**
	 * 11添加商品收藏
	 * 
	 * @param res
	 * @param goods_id
	 */
	public static void addFavoriteGoods(AsyncHttpResponseHandler res,
			String goods_id) {
		String url = base_url + "addFavoriteGoods&goods_id=" + goods_id;
		client.get(url, res);
	}

	/**
	 * 12取消商品收藏
	 * 
	 * @param res
	 * @param goods_id
	 */
	public static void delFavoriteGoods(AsyncHttpResponseHandler res,
			String goods_id) {
		String url = base_url + "delFavoriteGoods&goods_id=" + goods_id;
		client.get(url, res);
	}
	/**
	 * 删除商品收藏
	 * 
	 * @param res
	 * @param key 
	 */
	public static void delProduct(AsyncHttpResponseHandler res,String key,String fav_id) {
		String url = base_url + "index.php?act=member_favorites&op=favorites_del";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("fav_id", fav_id);
		client.post(url, params, res);
	}
	/**
	 * 删除店铺收藏
	 * 
	 * @param res
	 * @param key 
	 */
	public static void delStore(AsyncHttpResponseHandler res,String store_id,String key) {
		String url = base_url + "index.php?act=member_favorites_store&op=favorites_del";
		RequestParams params = new RequestParams();
		params.put("store_id",store_id);
		params.put("key", key);
		client.post(url, params, res);
	}

	/**
	 * 13查询商品收藏
	 * 
	 * @param res
	 * @param page
	 * @param per_page
	 */
	public static void getFavoriteGoods(AsyncHttpResponseHandler res,
			String page, String per_page) {
		String url = base_url + "getFavoriteGoods&page=" + page + "&per_page="
				+ per_page;
		client.get(url, res);
	}

	/**
	 * 14添加商店收藏
	 * 
	 * @param res
	 * @param store_id
	 */
	public static void addFavoriteStore(AsyncHttpResponseHandler res,
			String store_id) {
		String url = base_url + "addFavoriteStore&store_id=" + store_id;
		client.get(url, res);
	}

	/**
	 * 15取消商店收藏
	 * 
	 * @param res
	 * @param store_id
	 */
	public static void delFavoriteStore(AsyncHttpResponseHandler res,
			String store_id) {
		String url = base_url + "delFavoriteStore&store_id=" + store_id;
		client.get(url, res);
	}

	/**
	 * 出售中
	 * 
	 * @param res
	 * @param page 每页数量
	 * @param curpage 当前页码
	 */
	public static void getFavoriteProduct(AsyncHttpResponseHandler res,String key,String page,String curpage) {
//		String url = base_url + "index.php?act=seller_goods&op=goods_online"+"&key"+key+"&page"+page+"&curpage"+curpage;
//		client.get(url, res);
		String url = base_url + "index.php?act=seller_goods&op=goods_online";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("page", page);
		params.put("curpage", curpage);
		client.post(url, params, res);
	}
	/**
	 * 已下架
	 * 
	 * @param res
	 * @param page 每页数量
	 * @param curpage 当前页码
	 */
	public static void getFavoriteStore(AsyncHttpResponseHandler res,String key,String page,String curpage) {
		String url = base_url + "index.php?act=seller_goods&op=goods_offline";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("page",page);
		params.put("curpage",curpage);
		client.post(url, params, res);
	}

	/**
	 * 获取地区列表
	 * 
	 * @param res
	 * @param parent_id
	 */
	public static void getArea(AsyncHttpResponseHandler res, String area_id) {
		String url = base_url + "index.php?act=area&op=area_list"+"&area_id="+area_id; 
		client.get(url, res);
	}

	/**
	 * 地址添加
	 * 
	    key 当前登录令牌
	    true_name 姓名
	    city_id 城市编号(地址联动的第二级)
	    area_id 地区编号(地址联动的第三级)
	    area_info 地区信息，例：天津 天津市 红桥区
	    street 街道 例如:南大街
	    address 地址信息，例：水游城8层
	    zip 邮编
	    mob_phone 手机
	 */
	public static void addAddress(AsyncHttpResponseHandler res,
			String key, String true_name, String city_id, String area_id,String area_info,String street,String address,String zip,String mob_phone) {
		String url = base_url + "index.php?act=member_address&op=address_add";
		RequestParams params =new RequestParams();
		params.put("key", key);
		params.put("true_name", true_name);
		params.put("city_id", city_id);
		params.put("area_id", area_id);
		params.put("area_info", area_info);
		params.put("street", street);
		params.put("address", address);
		params.put("zip", zip);
		params.put("mob_phone", mob_phone);
		client.post(url, params, res);
	}

	/**
	 * 19修改地址
	 * 


    key 当前登录令牌
    address_id 地址编号
    true_name 姓名
    area_id 地区编号
    city_id 城市编号
    area_info 地区信息，例：天津 天津市 红桥区
    street 街道 例:南大街
    address 地址信息，例：水游城8层
    zip 邮编
    mob_phone 手机


	 */
	public static void changeAddress(AsyncHttpResponseHandler res,String key,String address_id,
			String true_name, String area_id,String city_id, String address, String area_info,
			String street,String zip,String mob_phone) {
		String url = base_url + "index.php?act=member_address&op=address_edit";
		RequestParams params =new RequestParams();
		params.put("key", key);
		params.put("address_id", address_id);
		params.put("true_name", true_name);
		params.put("area_id", area_id);
		params.put("city_id", city_id);
		params.put("area_info", area_info);
		params.put("street", street);
		params.put("address", address);
		params.put("zip", zip);
		params.put("mob_phone", mob_phone);
		client.post(url, params, res);
	}

	/**
	 * 删除地址
	 * 
	 * @param res
	 * @param address_id
	 *            地址id
	 */
	public static void delAddress(AsyncHttpResponseHandler res,String key,String address_id) {
		String url = base_url + "index.php?act=member_address&op=address_del";
		RequestParams pareParams =new RequestParams();
		pareParams.put("key", key);
		pareParams.put("address_id", address_id);
		client.post(url, pareParams, res);
	}

	/**
	 * 21收货地址
	 * 
	 * @param res
	 */
	public static void getAddress(AsyncHttpResponseHandler res,String key) {
		String url = base_url + "index.php?act=member_address&op=address_list"+"&key="+key;
		client.get(url, res);
	}

	/**
	 * 设置默认地址
	 * 
	 * @param res
	 * @param key
	 * @param address_id
	 */
	public static void setDefaultAddress(AsyncHttpResponseHandler res,String key,String address_id) {
		String url = base_url + "index.php?act=member_address&op=address_default";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("address_id", address_id);
		client.post(url, params, res);
	}

	/**
	 * 23添加购物车
	 * 
	 * @param res
	 * @param goods_id
	 *            商品id，必选
	 * @param count
	 *            商品数量，默认为1
	 */
	public static void addCart(AsyncHttpResponseHandler res, String goods_id,
			String count) {
		count = count == null ? "1" : count;
		String url = base_url + "addCart&goods_id=" + goods_id + "&count="
				+ count;
		client.get(url, res);

	}

	/**
	 * 24修改购物车
	 * 
	 * @param res
	 * @param cart_id
	 *            购物车id，必选
	 * @param count
	 *            商品数量，必选
	 */
	public static void updateCart(AsyncHttpResponseHandler res, String cart_id,
			int count) {
		String url = base_url + "updateCart&cart_id=" + cart_id + "&count="
				+ count;
		client.get(url, res);

	}

	/**
	 * 25删除购物车
	 * 
	 * @param res
	 * @param cart_id
	 *            购物车id，必选，可以多个，例如：1,2,3
	 */
	public static void delCart(AsyncHttpResponseHandler res, String cart_id) {
		String url = base_url + "delCart&cart_id=" + cart_id;
		client.get(url, res);
	}

	/**
	 * 26查询购物车
	 * 
	 * @param res
	 */
	public static void getCartList(AsyncHttpResponseHandler res, String cart_id) {
		String url = base_url + "getCartList&cart_id=" + cart_id;
		client.get(url, res);
		Log.i("landouurl", url);
	}

	/**
	 * 27订单确认
	 * 
	 * @param res
	 * @param cart_id
	 */
	public static void getOrderConfirm(AsyncHttpResponseHandler res,
			String cart_id) {
		String url = base_url + "getOrderConfirm&cart_id=" + cart_id;
		client.get(url, res);
		Log.i("landousjsonurl", url);
	}

	/**
	 * 27立即购买
	 * 
	 * @param res
	 * @param goods_id
	 */
	public static void BuyNow(AsyncHttpResponseHandler res, String goods_id,
			int count) {
		String url = base_url + "getOrderConfirm&goods_id=" + goods_id
				+ "&count=" + count;
		client.get(url, res);
		Log.i("landousjsonurl", url);
	}

	/**
	 * 28 提交订单 所有都是必选
	 * 
	 * @param res
	 * @param ship_method
	 * @param pay_method
	 * @param address_id
	 * @param cart_id
	 */
	public static void addOrder(AsyncHttpResponseHandler res,
			String ship_method, String pay_method, String address_id,
			String cart_id) {
		String url = base_url + "addOrder&ship_method=" + ship_method
				+ "&pay_method=" + pay_method + "&address_id=" + address_id
				+ "&cart_id=" + cart_id;
		client.get(url, res);
		Log.i("landousjsonurl", url);
	}

	/**
	 * 28 立即付款提交订单
	 * 
	 * @param res
	 * @param ship_method
	 * @param pay_method
	 * @param address_id
	 * @param goods_id
	 */
	public static void addOrderNow(AsyncHttpResponseHandler res,
			String ship_method, String pay_method, String address_id,
			String goods_id, String count) {
		String url = base_url + "addOrder&ship_method=" + ship_method
				+ "&pay_method=" + pay_method + "&address_id=" + address_id
				+ "&goods_id=" + goods_id + "&count=" + count;
		client.get(url, res);
	}

	/**
	 * 订单列表
	 * 
	 * @param res
	 * @param key 
	 * 
	 * @param order_state
	 *            订单状态（待付款:10,待发货:20,待收货:30,已收货:40）
	 */
	public static void getOrderList(AsyncHttpResponseHandler res,String key,String curpage,int order_state) {
		String url = base_url + "index.php?act=seller_order&op=index";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("curpage", curpage);
		params.put("order_state", order_state);
		client.post(url, params, res);
//		String url = base_url + "index.php?act=seller_order&op=index"+"&key="+key+"&curpage="+curpage+"&order_state="+order_state;
//		client.get(url, res);
	}

	/**
	 * 30 更新移动支付减免
	 * 
	 * @param res
	 * @param pay_sn
	 * @param pay_method
	 * @param discount
	 */
	public static void updateMobileDiscount(AsyncHttpResponseHandler res,
			String pay_sn, String pay_method, String discount) {
		String url = base_url + "updateMobileDiscount&pay_sn=" + pay_sn
				+ "&pay_method=" + pay_method + "&discount=" + discount;
		client.get(url, res);
		Log.i("landousjsonurl", url);
	}

	/**
	 * 取消订单(未付款)
	 * 
	 * @param res
	 * @param order_id
	 * @param extend_msg
	 */
	public static void cancelOrder(AsyncHttpResponseHandler res,
			String key, String order_id) {
		String url = base_url + "index.php?act=seller_order&op=cancle";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("order_id", order_id);
		client.post(url, params, res);
	}
	/**
	 * 接单
	 * 
	 * @param res
	 * @param order_id
	 * @param extend_msg
	 */
	public static void getTheOrder(AsyncHttpResponseHandler res,
			String key, String order_id,String order_seller_message) {
		String url = base_url + "index.php?act=seller_order&op=jiedan";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("order_id", order_id);
		params.put("order_seller_message", order_seller_message);
		client.post(url, params, res);
	}
	/**
	 * 取消订单(已付款)
	 * 
	 * @param res
	 * @param order_id
	 * @param extend_msg
	 */
	public static void cancelOrder_paid(AsyncHttpResponseHandler res,
			String key, String order_id) {
		String url = base_url + "index.php?act=member_order&op=order_cancel_refund";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("order_id", order_id);
		client.post(url, params, res);
	}

	/**
	 * 订单确认收货
	 * 
	 * @param res
	 * @param key
	 * @param order_id
	 */
	public static void receiveGoods(AsyncHttpResponseHandler res,String key,String order_id) {
		String url = base_url + "index.php?act=member_order&op=order_receive";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("order_id", order_id);
		client.post(url, params, res);
	}
	/**
	 * 删除订单
	 * 
	 * @param res
	 * @param key
	 * @param order_id
	 */
	public static void deleteTheOrder(AsyncHttpResponseHandler res,String key,String order_id) {
		String url = base_url + "index.php?act=seller_order&op=order_delete";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("order_id", order_id);
		client.post(url, params, res);
	}

	/**
	 * 33 查看物流
	 * 
	 * @param res
	 * @param order_id
	 */
	public static void getExpress(AsyncHttpResponseHandler res, String order_id) {
		String url = base_url + "getExpress&order_id=" + order_id;
		client.get(url, res);
	}
	/**
	 * 商品下架
	 * 
	 * @param res
	 * @param key 
	 * @param order_id
	 */
	public static void goods_unshow(AsyncHttpResponseHandler res, String key, String commonid ) {
//		String url = base_url + "index.php?act=seller_goods&op=goods_unshow";
//		RequestParams params = new RequestParams();
//		params.put("key",key);
//		params.put("commonid",commonid);
//		client.post(url,params,res);
		String url = base_url + "index.php?act=seller_goods&op=goods_unshow"+"&key="+key+"&commonid="+commonid;
		client.get(url, res);
	}
	/**
	 * 商品上架
	 * 
	 * @param res
	 * @param key 
	 * @param order_id
	 */
	public static void goods_show(AsyncHttpResponseHandler res, String key, String commonid ) {
//		String url = base_url + "index.php?act=seller_goods&op=goods_unshow";
//		RequestParams params = new RequestParams();
//		params.put("key",key);
//		params.put("commonid",commonid);
//		client.post(url,params,res);
		String url = base_url + "index.php?act=seller_goods&op=goods_show"+"&key="+key+"&commonid="+commonid;
		client.get(url, res);
	}
	/**
	 * 商品删除
	 * 
	 * @param res
	 * @param key 
	 * @param commonid 
	 */
	public static void goods_delete(AsyncHttpResponseHandler res, String key, String commonid ) {
		String url = base_url + "index.php?act=seller_goods&op=drop_goods"+"&key="+key+"&commonid="+commonid;
		client.get(url, res);
	}

	/**
	 * 34 退款
	 * 
	 * @param res
	 * @param order_id
	 * @param rec_id
	 * @param refund_type
	 * @param refund_amount
	 * @param goods_num
	 * @param extend_msg
	 */
	public static void refund(AsyncHttpResponseHandler res, String order_id,
			String rec_id, String refund_type, String refund_amount,
			String goods_num, String extend_msg) {
		String url = base_url + "refund&order_id=" + order_id + "&rec_id="
				+ rec_id + "&refund_type=" + refund_type + "&refund_amount="
				+ refund_amount + "&goods_num=" + goods_num + "&extend_msg="
				+ extend_msg;
		client.get(url, res);
		Log.i("landousurl", url);
	}

	/**
	 * 订单评价
	 * 
	 * @param res
	 * @param key 
	 * @param order_id 订单id 
	 * @param goods 商品评价
	 */
	public static void orderEvaluation(AsyncHttpResponseHandler res,
			String key, String order_id, String goods) {
		String url = base_url + "index.php?act=member_evaluate&op=save";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("order_id", order_id);
		params.put("goods", goods);
		client.post(url, params, res);
	}

	/**
	 * 36 获取专题
	 * 
	 * @param res
	 * @param special_id
	 */
	public static void getSpecial(AsyncHttpResponseHandler res,
			String special_id) {
		String url = base_url + "getSpecial&special_id=" + special_id;
		Log.i("get-special_url", url);
		client.get(url, res);
	}

	/**
	 * 积分商城
	 * 
	 * @param res
	 * @param key
	 * 
	 */
	public static void pointsMall(AsyncHttpResponseHandler res, String key) {
		String url = base_url + "index.php?act=member_points&op=goods_list"+"&key="+key;
		client.get(url, res);
	}

	/**
	 * 积分兑换
	 * 
	 * @param res
	 * @param key           登录令牌
	 * @param pgoods_id     商品id
	 * @param address_id    收货地址id
	 * @param pcart_message 留言
	 * 
	 */
	public static void addPointsOrder(AsyncHttpResponseHandler res,
			String key, String pgoods_id, String pcart_message,
			String address_id) {

		String url = null;
		url = base_url + "index.php?act=member_points&op=exchange";
		RequestParams requestParams  = new RequestParams();
		requestParams.put("key", key);
		requestParams.put("pgoods_id", pgoods_id);
		requestParams.put("pcart_message", pcart_message);
		requestParams.put("address_id", address_id);
		client.post(url,requestParams,res);
	}
	/**
	 * 积分兑换记录
	 * @param res
	 * @param key
	 */
	public static void exchangerecord(AsyncHttpResponseHandler res,String key) {
		
		String url = null;
		url = base_url + "index.php?act=member_points&op=record"+"&key="+key;
		client.get(url, res);
	}

	/**
	 * 39积分订单付款完成
	 * 
	 * @param res
	 * @param point_orderid
	 */
	public static void payPointsOrder(AsyncHttpResponseHandler res,
			String point_orderid) {
		String url = base_url + "payPointsOrder&point_orderid=" + point_orderid;
		client.get(url, res);

	}

	/**
	 * 40 查询积分订单列表
	 * 
	 * @param res
	 * @param page
	 * @param per_page
	 */
	public static void getPointsOrder(AsyncHttpResponseHandler res,
			String page, String per_page) {
		String url = base_url + "getPointsOrder&page=" + page + "&per_page="
				+ per_page;
		client.get(url, res);
	}

	/**
	 * 用户积分
	 * @param res
	 * @param page
	 * @param per_page
	 */
	public static void getPointsLog(AsyncHttpResponseHandler res,String key) {
		String url = base_url + "index.php?act=member_points&op=points_log"+"&key="+key;
		client.get(url, res);
//		String url = base_url + "index.php?act=member_points&op=points_log";
//		RequestParams requestParams =new RequestParams();
//		requestParams.put("key", key);
//		client.post(url, requestParams, res);
		
	}
	/**
	 * 用户钱包
	 * @param res
	 * @param page
	 * @param per_page
	 */
	public static void getMoney(AsyncHttpResponseHandler res,String key) {
		String url = base_url + "index.php?act=member_predeposit&op=view";
		RequestParams params = new RequestParams();
		params.put("key", key);
		client.post(url, params, res);
//		String url = base_url + "index.php?act=member_points&op=points_log";
//		RequestParams requestParams =new RequestParams();
//		requestParams.put("key", key);
//		client.post(url, requestParams, res);
		
	}

	/**
	 * 上传头像保存
	 * 
	 * @param res
	 * @param avatar
	 * @param key
	 */
	public static void saveAvatar(AsyncHttpResponseHandler res,String avatar,String key ) {
		String url = base_url + "index.php?act=member_index&op=avatar_save";
		RequestParams params =new RequestParams();
		params.put("avatar", avatar);
		params.put("key", key);
		client.post(url, params, res);
	}

	/**
	 * 43 分享获得积分
	 * 
	 * @param res
	 */
	public static void addSharePoints(AsyncHttpResponseHandler res) {
		String url = base_url + "addSharePoints";
		client.get(url, res);
	}

	/**
	 * 44 签到
	 * 
	 * @param res
	 */
	public static void chackIn(AsyncHttpResponseHandler res,String key) {
		String url = base_url + "index.php?act=member_index&op=signin";
		RequestParams requestParams = new RequestParams();
		requestParams.put("key", key);
		client.post(url, requestParams, res);
	}
	/**
	 * 减钻石
	 * 
	 * @param res
	 */
	public static void subtractCheckPoints(AsyncHttpResponseHandler res, double goods_price) {
		String url = base_url + "subtractCheckPoints&goods_price="+goods_price;
		client.get(url, res);
	}
	/**
	 * 充值接口
	 * 
	 * @param res
	 */
	public static void recharge(AsyncHttpResponseHandler res, String key, String pdr_amount,String channel) {
		String url = base_url + "index.php?act=member_predeposit&op=recharge_add";
		RequestParams requestParams = new RequestParams();
		requestParams.put("key", key);
		requestParams.put("channel", channel );
		requestParams.put("pdramount", pdr_amount);
		client.post(url, requestParams, res);
		
	}
	/**
	 * 订单支付
	 * 
	 * @param res
	 */
	public static void payTheOrder(AsyncHttpResponseHandler res, String key, String pay_sn,String channel ) {
		String url = base_url + "index.php?act=member_payment&op=pay"+"&key="+key+"&pay_sn="+pay_sn+"&channel="+channel;
		client.get(url, res);
//		String url = base_url + "index.php?act=member_payment&op=pay";
//		RequestParams params = new RequestParams();
//		params.put("key", key);
//		params.put("pay_sn", pay_sn);
//		params.put("channel", channel);
//		client.post(url, params, res);
	}
	/**
	 * 45 积分订单确认收货
	 * 
	 * @param res
	 * @param point_orderid
	 */
	public static void receivePointsOrder(AsyncHttpResponseHandler res,
			String point_orderid) {
		String url = base_url + "receivePointsOrder&point_orderid="+ point_orderid;
		client.get(url, res);
	}
	/**
	 *  获取支付宝秘钥
	 * 
	 * @param res
	 */
	public static void getAlipay(AsyncHttpResponseHandler res) {
		String url = base_url + "getAlipay";
		client.get(url, res);

	}

	/**
	 * 47.获取版本
	 * 
	 * @param res
	 */
	public static void getAppVersion(AsyncHttpResponseHandler res) {
		String url = base_url + "getAppVersion&platform=" + "android";
		client.get(url, res);
	}

	/**
	 * 48 忘记密码
	 * 
	 * @param res
	 * @param mobile 手机号
	 * @param verify_code 验证码
	 * @param client 客户端类型(android ios )
	 * 
	 */
	public static void resetPassword(AsyncHttpResponseHandler res,String mobile, String password,String verify_code) {
		String url = base_url + "index.php?act=login&op=restpasswd";
		RequestParams requestParams  = new RequestParams();
		requestParams.put("mobile", mobile);
		requestParams.put("password", password);
		requestParams.put("verify_code", verify_code);
		requestParams.put("client", AppValue.CLIENT);
		client.post(url, requestParams, res);
	}

	/**
	 * 49. 获取订单详情
	 * 
	 * @param res
	 * @param order_id
	 */
	public static void getOrderDetail(AsyncHttpResponseHandler res,String key,
			String order_id) {
		String url = base_url + "index.php?act=seller_order&op=show_order"+"&key="+key+"&order_id="+order_id;
		client.get(url, res);

	}

	/**
	 * 50 获取手机减免额度
	 * 
	 * @param res
	 */
	public static void getDiscountSetting(AsyncHttpResponseHandler res) {
		String url = base_url + "getDiscountSetting";
		client.get(url, res);
	}

	/**
	 * 51.取消积分订单
	 * 
	 * @param res
	 * @param point_orderid
	 */
	public static void cancelPointsOrder(AsyncHttpResponseHandler res,
			String point_orderid) {
		String url = base_url + "cancelPointsOrder&point_orderid="
				+ point_orderid;
		client.get(url, res);
	}

	/**
	 * 52 订单完成支付
	 * 
	 * @param res
	 * @param pay_sn
	 */
	public static void payOrder(AsyncHttpResponseHandler res, String pay_sn) {
		String url = base_url + "payOrder&pay_sn=" + pay_sn;
		client.get(url, res);
	}
	/**
	 * 上传头像
	 * @param res
	 * @param key
	 * @param name
	 */

	public static void update(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("avatar",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=store_setting&op=upload_image";
		client.post(url, params, res);
	}
	/**
	 * 上传认证证件（身份证）
	 * @param res
	 * @param key
	 * @param name
	 */
	
	public static void updateCertificationIdCard(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("idcard",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=seller_identification&op=image_upload";
		client.post(url, params, res);
	}
	/**
	 * 上传认证证件（执照）
	 * @param res
	 * @param key
	 * @param name
	 */
	
	public static void updateCertificationLicense(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("license",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=seller_identification&op=image_upload";
		client.post(url, params, res);
	}
	/**
	 * 上传评论图片
	 * @param res
	 * @param key
	 * @param name
	 */
	
	public static void upPhoto(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("avatar",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=member_evaluate&op=image_upload";
		client.post(url, params, res);
	}
	
	/**
	 * 54 猜你喜欢
	 * @param lng 经度
	 * @param lat 纬度
	 * @param city_id 城市id
	 * @param curpage 当前页码
	 */
	public static void getCaiNiLike(AsyncHttpResponseHandler res,String lng,String lat,String city_id,String curpage) {
		String url = base_url + "index.php?act=goods&op=goods_like_list"+"&lng="+lng+"&lat="+lat+"&city_id="+city_id+"&curpage="+curpage;
		client.get(url, res);
//		String url = base_url + "index.php?act=member_points&op=points_log";
//		RequestParams requestParams =new RequestParams();
//		requestParams.put("key", key);
//		client.post(url, requestParams, res);
		
	}
	/**
	 * 55 每日好店
	 * @param page 每页数量
	 * @param curpage 当前页码
	 * @param city_id 城市id
	 * @param lng 经度
	 * @param lat 纬度
	 */
	public static void getGoodStore(AsyncHttpResponseHandler res,String page,String curpage,String city_id,String lng,String lat) {
		String url = base_url + "index.php?act=store&op=good_store"+"&page="+page+"&curpage="+curpage+"&city_id="+city_id+"&lng="+lng+"&lat="+lat;
		client.get(url, res);
		
	}
	
	/**
	 * 上传晒单圈图片
	 * @param res
	 * @param key
	 * @param name
	 */
	
	public static void uploadshaidanquan(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("avatar",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=member_circle&op=image_upload";
//		String url = base_url + "index.php?act=member_index&op=avatar_upload";
		client.post(url, params, res);
	}
/**
 * 晒单圈-发布
 * @param res
 * @param key
 * @param description
 * @param image
 */
	
	public static void shaidanquanfabu(AsyncHttpResponseHandler res, String key,String description,String image) {
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("description", description);
		params.put("image",image);
		String url = base_url + "index.php?act=member_circle&op=publish";
		client.post(url, params, res);
	}
	/**
	 * 晒单圈-首页
	 * @param res
	 * @param key
	 */
	public static void shaidanquan_shouye(AsyncHttpResponseHandler res, String key,String order ) {
		String url = base_url + "index.php?act=member_circle&op=list"+"&key="+key+"&order="+order;
		client.get(url, res);
	}
	/**
	 * 晒单圈-我发布的
	 * @param res
	 * @param key
	 */
	public static void shaidanquan_mypublish(AsyncHttpResponseHandler res, String key) {
		String url = base_url + "index.php?act=member_circle&op=my_publish"+"&key="+key;
		client.get(url, res);
	}
	/**
	 * 晒单圈-我评论的
	 * @param res
	 * @param key
	 */
	public static void shaidanquan_myquote(AsyncHttpResponseHandler res, String key) {
		String url = base_url + "index.php?act=member_circle&op=my_quote"+"&key="+key;
		client.get(url, res);
	}
	/**
	 * 晒单圈-点赞
	 * @param res
	 * @param key
	 */
	public static void zan(AsyncHttpResponseHandler res, String key,String circle_id) {
		String url = base_url + "index.php?act=member_circle&op=praise";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("circle_id", circle_id);
		client.post(url, params, res);
	}
	/**
	 * 晒单圈-评论
	 * @param res
	 * @param key
	 */
	public static void comment(AsyncHttpResponseHandler res, String key,
			String circle_id,String content,String reply_replyid,String reply_replyname) {
		String url = base_url + "index.php?act=member_circle&op=quote";
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("circle_id", circle_id);
		params.put("content", content );
		params.put("reply_replyid", reply_replyid);
		params.put("reply_replyname", reply_replyname);
		client.post(url, params, res);
	}
	/**
	 * 上传身份证
	 * @param res
	 * @param key
	 * @param name
	 */
	public static void uploadIDcard(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("avatar",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=member_index&op=idcard_upload";
		client.post(url,params,res);
	}
	/**
	 * 卖家实名认证
	 * @param res
	 * @param key
	 * @param truename
	 * @param birthday
	 * @param address
	 * @param idcard
	 */
	public static void certificate(AsyncHttpResponseHandler res, String key,String truename ,String birthday,String address,String idcard,String license) {
		String url = base_url + "index.php?act=seller_identification&op=save";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("truename",truename);
		params.put("birthday",birthday);
		params.put("address",address);
		params.put("idcard",idcard);
		params.put("license",license);
		client.post(url, params,res);
	}
	/**
	 *  天天特价
	 * @param curpage 当前页码
	 * @param city_id 城市id
	 * @param lng 经度
	 * @param lat 纬度
	 */
	public static void getDaySpecial(AsyncHttpResponseHandler res,String curpage,String city_id,String lng,String lat) {
		String url = base_url + "index.php?act=goods&op=day_special"+"&curpage="+curpage+"&city_id="+city_id+"&lng="+lng+"&lat="+lat;
		client.get(url, res);
		
	}
	
	/**
	 *  店铺详情
	 * @param store_id 店铺ID
	 * @param key 登录令牌（可选），登录后提交令牌返回是否已经收藏该店铺
	 */
	public static void getStoreInfo(AsyncHttpResponseHandler res,String store_id,String key) {
		String url = base_url + "index.php?act=store&op=store_info"+"&store_id="+store_id+"&key="+key;
		client.get(url, res);
	}
	
	/**
	 *  商品详情
	 * @param goods_id 商品编号
	 * @param
	 */
	public static void getGoodsInfo(AsyncHttpResponseHandler res,String goods_id) {
		String url = base_url + "index.php?act=goods&op=goods_detail"+"&goods_id="+goods_id;
		client.get(url, res);
	}
	
	/**
	 *  商品评价
	 * @param goods_id 商品编号
	 * @param
	 */
	public static void getGoodsEvaluation(AsyncHttpResponseHandler res,String goods_id) {
		String url = base_url + "index.php?act=goods&op=more_evaluate"+"&goods_id="+goods_id;
		client.get(url, res);
	}
	
	/**
	 *  城市列表
	 * @param goods_id 商品编号
	 * @param
	 */
	public static void getCityList(AsyncHttpResponseHandler res) {
		String url = base_url + "index.php?act=area&op=city_list";
		client.get(url, res);
	}
	
	/**
	 *  城市名称
	 * @param goods_id 商品编号
	 * @param
	 */
	public static void getCityName(AsyncHttpResponseHandler res,String lng,String lat) {
		String url = base_url + "index.php?act=area&op=location"+"&lng="+lng+"&lat="+lat;
		client.get(url, res);
	}
	
	/**
	 *  购物车详情
	 * @param key 当前登录令牌
	 * @param
	 */
	public static void getShoppingCarInfoList(AsyncHttpResponseHandler res, String key) {
		String url = base_url + "index.php?act=member_cart&op=cart_list";
		RequestParams params = new RequestParams();
		params.put("key",key);
		client.post(url, params,res);
	}
	
	/**
	 *  购物车删除
	 * @param key 当前登录令牌
	 * @param cart_id 购物车编号
	 */
	public static void getDelete(AsyncHttpResponseHandler res, String key,String cart_id) {
		String url = base_url + "index.php?act=member_cart&op=cart_del";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("cart_id",cart_id);
		client.post(url, params,res);
	}
	
	/**
	 *  购物车删除
	 * @param key 当前登录令牌
	 * @param cart_id 购物车编号
	 * @param quantity 新的购买数量
	 */
	public static void getAddGoods(AsyncHttpResponseHandler res, String key,String cart_id,String quantity) {
		String url = base_url + "index.php?act=member_cart&op=cart_edit_quantity";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("cart_id",cart_id);
		params.put("quantity",quantity);
		client.post(url, params,res);
	}
	
	/**
	 *  商家可用商品分类
	 * @param key 当前登录令牌
	 * @param gc_id 父级分类编号，顶级分类为0
	 * @param deep 请求分类深度，一级-1、二级-2
	 */
	public static void getCanUserList(AsyncHttpResponseHandler res, String key,String gc_id,String deep) {
		String url = base_url + "index.php?act=seller_goods_class&op=class_list";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("gc_id",gc_id);
		params.put("deep",deep);
		client.post(url, params,res);
	}
	
	/**
	 *  规格属性
	 * @param key 当前登录令牌
	 * @param gc_id 父级分类编号，顶级分类为0
	 */
	public static void getGuiGeList(AsyncHttpResponseHandler res, String key,String gc_id) {
		String url = base_url + "index.php?act=seller_goods_class&op=type_info";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("gc_id",gc_id);
		client.post(url, params,res);
	}
	
	/**
	 *  无型号商品发布
	 * @param key 当前登录令牌key 商家登录令牌
	 * @param cate_id 商品分类编号
	 * @param cate_name 商品分类名称 例：服饰鞋帽&gt;女装
	 * @param g_name 商品名称
	 * @param g_price 商品价格
	 * @param goods_promotion_price 天天特价
	 * @param g_storage 商品库存
	 * @param goods_jingle 商品简介
	 * @param image_path 商品主图文件名称
	 * @param image_all 其它商品图文件名，用逗号分隔
	 */
	public static void ShangPinFaBu(AsyncHttpResponseHandler res, String key,String cate_id,String cate_name,String g_name,String g_price,String goods_promotion_price,String g_storage,String goods_jingle,String image_path,String image_all) {
		String url = base_url + "index.php?act=seller_goods&op=goods_add";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("cate_id",cate_id);
		params.put("cate_name",cate_name);
		params.put("g_name",g_name);
		params.put("g_price",g_price);
		params.put("goods_promotion_price",goods_promotion_price);
		params.put("g_storage",g_storage);
		params.put("goods_jingle",goods_jingle);
		params.put("image_path",image_path);
		params.put("image_all",image_all);
		client.post(url, params,res);
	}
	
	/**
	 * 单型号商品发布
	 * @param key 当前登录令牌key 商家登录令牌
	 * @param cate_id 商品分类编号
	 * @param cate_name 商品分类名称 例：服饰鞋帽&gt;女装
	 * @param g_name 商品名称
	 * @param sp_name 规格名称，参见下方范例
	 * @param sp_value 规格值，参见下方范例
	 * @param spec 提交的规格，参见下方范例
	 * @param goods_jingle 商品简介
	 * @param image_path 商品主图文件名称
	 * @param image_all 其它商品图文件名，用逗号分隔
	 */
	public static void ShangPinFaBu1(AsyncHttpResponseHandler res, String key,String cate_id,String cate_name,String g_name,com.alibaba.fastjson.JSONObject sp_name,com.alibaba.fastjson.JSONObject sp_value,com.alibaba.fastjson.JSONObject spec,String goods_jingle,String image_path,String image_all) {
		String url = base_url + "index.php?act=seller_goods&op=goods_add";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("cate_id",cate_id);
		params.put("cate_name",cate_name);
		params.put("g_name",g_name);
		params.put("sp_name",sp_name);
		params.put("sp_value",sp_value);
		params.put("spec",spec);
		params.put("goods_jingle",goods_jingle);
		params.put("image_path",image_path);
		params.put("image_all",image_all);
		client.post(url, params,res);
	}
	/**
	 *  规格属性
	 * @param key 当前登录令牌
	 * @param name 规格值名(多个逗号分隔)
	 * @param gc_id 最后一级分类id
	 * @param sp_id 规格id
	 */
	public static void getAddGgz(AsyncHttpResponseHandler res, String key,String name,String gc_id,String sp_id) {
		String url = base_url + "index.php?act=seller_goods&op=add_spec";
		RequestParams params = new RequestParams();
		params.put("key",key);
		params.put("name",name);
		params.put("gc_id",gc_id);
		params.put("sp_id",sp_id);
		client.post(url, params,res);
	}
	/**
	 *  商品图片上传
	 * @param key 当前登录令牌
	 * @param name FILES字段名
	 * @param file 图片
	 */
	public static void getImageLoad(AsyncHttpResponseHandler res, String key,String name,File file ) {
		RequestParams params = new RequestParams();
		try {
			params.put("key", key);
			params.put("name", name);
			params.put("good_img",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Upload a File
		String url = base_url + "index.php?act=seller_album&op=image_upload";
		client.post(url, params, res);
	}

	public static String iterateParams(HashMap<String,String> params){
		String parameter = "";
		Iterator<String> iterator = params.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			parameter += "&"+key+"="+params.get(key);
		}
		return parameter;
	}
	
}
