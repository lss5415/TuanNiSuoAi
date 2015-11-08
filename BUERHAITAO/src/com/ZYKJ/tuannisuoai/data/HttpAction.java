package com.ZYKJ.tuannisuoai.data;

/**
 * 指令接口
 * 
 * @author bin
 * 
 */
public class HttpAction {
	/** 相关指令数据 */
	public static final String SERVER_IP = "http://albert1258.vicp.cc";

	public static final String command[] = { "/mobile_shopLogin.action", // 0登录接口
			"/mobile_updateByPrimaryKeySelective.action", // 1 完善信息接口
			"/goods_addGoods.action", // 2 发布商品
			"/goods_searchByShopid.action", // 3 所有商品列表
			"/goods_updateByPrimaryKey.action",// 4修改商品
			"/goods_deleteByPrimaryKeyForApp.action", // 5 删除商品
			"/goods_searchAllRecommandForApp.action", // 6首页轮播等
			"/shop_searchByTypeid.action",// 7所有符合的商铺列表
			"/goods_searchByShopid.action", // 8指定类型的商铺列表
			"/goods_selectByPrimaryKey.action", // 9商品详情
			"/shop_queryByParentid.action", // 10亲友团列表
			"/shop_searchDistributionForApp.action",// 11查看所有店铺接口
			"/mobile_updateByPrimaryKeySelective.action",// 12 密码修改
	};

	public static String getCommand(int index) {
		return SERVER_IP + command[index];
	}

}
