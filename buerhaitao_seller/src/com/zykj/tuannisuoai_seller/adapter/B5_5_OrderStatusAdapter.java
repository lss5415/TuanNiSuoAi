package com.zykj.tuannisuoai_seller.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pingplusplus.android.PaymentActivity;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.B5_5_OrderDetail;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.Tools;
import com.zykj.tuannisuoai_seller.utils.UIDialog;

public class B5_5_OrderStatusAdapter extends BaseAdapter {
	
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private Activity c;
	private int status=0;
	String key;
//  order_state 订单状态（待发货:20,已发货:30,已完成:40,已取消:0）
	private static final int DAIFAHUO    = 20;
	private static final int YIFAHUO     = 30;
    private static final int YIWANCHENG  = 40;
    private static final int YIQUXIAO    = 0;
	
	String order_id,price;                                     //订单金额
    
    B5_5_OrderStatuslistviewAdapter adapter;
	public B5_5_OrderStatusAdapter(Activity c, List<Map<String, Object>> data,int status,String key) {
		this.c = c;
		this.data = data;
		this.status = status;
		this.key = key;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		Log.e("size", data.size()+"");
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(R.layout.ui_b5_5_orderlist_list_items, null);

            viewHolder.tv_addtime = (TextView) convertView.findViewById(R.id.tv_addtime);
            viewHolder.tv_orderprice = (TextView) convertView.findViewById(R.id.tv_orderprice);
            viewHolder.tv_ordergoodsnumber = (TextView) convertView.findViewById(R.id.tv_ordergoodsnumber);
            viewHolder.tv_orderstatus = (TextView) convertView.findViewById(R.id.tv_orderstatus);
            
            viewHolder.btn_deletetheorder = (Button) convertView.findViewById(R.id.btn_deletetheorder);
            viewHolder.btn_paytheorder = (Button) convertView.findViewById(R.id.btn_paytheorder);//接单
            viewHolder.btn_delete_this = (Button) convertView.findViewById(R.id.btn_delete_this);
            viewHolder.btn_tocomment = (Button) convertView.findViewById(R.id.btn_tocomment);
            viewHolder.btn_tuihuanhuo = (Button) convertView.findViewById(R.id.btn_tuihuanhuo);
            viewHolder.btn_querenshouhuo = (Button) convertView.findViewById(R.id.btn_querenshouhuo);
            
            viewHolder.listView = (ListView) convertView.findViewById(R.id.listview_goodslist);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//      订单状态（待付款:10,待发货:20,待收货:30,已收货:40）
        switch (status) {
			case DAIFAHUO:
				 viewHolder.tv_orderstatus.setText("买家已付款");
				 viewHolder.btn_paytheorder.setVisibility(View.VISIBLE);
				 viewHolder.btn_deletetheorder.setVisibility(View.VISIBLE);
				 viewHolder.btn_delete_this.setVisibility(View.INVISIBLE);
				 viewHolder.btn_tocomment.setVisibility(View.INVISIBLE);
				 viewHolder.btn_tuihuanhuo.setVisibility(View.INVISIBLE);
				 viewHolder.btn_querenshouhuo.setVisibility(View.INVISIBLE);
				break;
			case YIFAHUO:
				viewHolder.tv_orderstatus.setText("卖家已发货");
				viewHolder.btn_deletetheorder.setVisibility(View.VISIBLE);
				viewHolder.btn_paytheorder.setVisibility(View.INVISIBLE);
				viewHolder.btn_delete_this.setVisibility(View.INVISIBLE);
				viewHolder.btn_tocomment.setVisibility(View.INVISIBLE);
				viewHolder.btn_tuihuanhuo.setVisibility(View.INVISIBLE);
				viewHolder.btn_querenshouhuo.setVisibility(View.INVISIBLE);
				break;
			case YIWANCHENG:
				viewHolder.tv_orderstatus.setText("买家已确认收货");
				viewHolder.btn_tuihuanhuo.setVisibility(View.INVISIBLE);
				viewHolder.btn_querenshouhuo.setVisibility(View.INVISIBLE);
				viewHolder.btn_deletetheorder.setVisibility(View.INVISIBLE);
				viewHolder.btn_paytheorder.setVisibility(View.INVISIBLE);
				viewHolder.btn_delete_this.setVisibility(View.VISIBLE);
				viewHolder.btn_tocomment.setVisibility(View.INVISIBLE);
				break;
			case YIQUXIAO:
				viewHolder.tv_orderstatus.setText("买家已确认收货");
				viewHolder.btn_delete_this.setVisibility(View.VISIBLE);
				viewHolder.btn_tuihuanhuo.setVisibility(View.INVISIBLE);
				viewHolder.btn_querenshouhuo.setVisibility(View.INVISIBLE);
				viewHolder.btn_deletetheorder.setVisibility(View.INVISIBLE);
				viewHolder.btn_paytheorder.setVisibility(View.INVISIBLE);
				viewHolder.btn_tocomment.setVisibility(View.INVISIBLE);
				break;
	
			default:
				break;
		}
        order_id = data.get(position).get("order_id").toString();
        price = data.get(position).get("goods_total_pay_price").toString();
        viewHolder.tv_addtime.setText(data.get(position).get("add_time").toString());//添加时间
        JSONArray extend_order_goods = null;
        extend_order_goods = (JSONArray) ((Map) data.get(position)).get("extend_order_goods");
        viewHolder.tv_ordergoodsnumber.setText("共有"+extend_order_goods.length()+"件商品");//订单中商品的数量
        viewHolder.tv_orderprice.setText("实付:￥"+price);
        //某些数据没有extend_order_goods这条信息
    	adapter = new B5_5_OrderStatuslistviewAdapter(c,extend_order_goods);
    	viewHolder.listView.setAdapter(adapter);
        //根据商品数的多少来确定评论显示的高度
        B5_5_OrderStatuslistviewAdapter listAdapter = (B5_5_OrderStatuslistviewAdapter) viewHolder.listView.getAdapter();  
        if (listAdapter == null) { 
            return null; 
        } 
        int totalHeight = 0; 
        for (int i = 0; i < listAdapter.getCount(); i++) { 
            View listItem = listAdapter.getView(i, null, viewHolder.listView); 
            listItem.measure(0, 0); 
            totalHeight += listItem.getMeasuredHeight(); 
        } 
        //设置商品显示的的高度
        ViewGroup.LayoutParams params = viewHolder.listView.getLayoutParams(); 
        params.height = totalHeight + (viewHolder.listView.getDividerHeight() * (listAdapter.getCount())); 
        viewHolder.listView.setLayoutParams(params); 
        
        viewHolder.btn_deletetheorder.setOnClickListener(new DeletetheorderListener(position,data.get(position).get("order_id").toString()));
        viewHolder.btn_paytheorder.setOnClickListener(new GetOrderListener(position,data.get(position).get("order_id").toString()));
        //跳转到订单详情
        viewHolder.listView.setOnItemClickListener(new GetOrderDetail(data.get(position).get("order_id").toString(),status,data.get(position).get("add_time").toString()));
        viewHolder.btn_delete_this.setOnClickListener(new DeleteTheOrder(data.get(position).get("order_id").toString()));
        return convertView;
	}
	/**
	 * 取消订单
	 * @author zyk
	 *
	 */
	class DeletetheorderListener implements View.OnClickListener {
		int position;
	    String orderidString;
		public DeletetheorderListener(int position,String orderidString) {
			this.position = position;
			this.orderidString = orderidString;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Tools.Notic(c, "是否取消该订单？", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					UIDialog.closeDialog();
					Log.e("key+id", key+" "+orderidString);
					HttpUtils.cancelOrder(res_cancelOrder, key, orderidString);
				}
			});
		}

	}
	/**
	 * 接单
	 * @author zyk
	 *
	 */
	class GetOrderListener implements View.OnClickListener {
		int position;
		String orderidString;
		public GetOrderListener(int position,String orderidString) {
			this.position = position;
			this.orderidString = orderidString;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Tools.Notic(c, "是否接单？", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					UIDialog.closeDialog();
					HttpUtils.getTheOrder(res_getTheOrder, key, orderidString,"");
				}
			});
		}
		
	}
	/**
	 * 跳转到订单详情
	 * @author zyk
	 */
	class GetOrderDetail implements ListView.OnItemClickListener {
		String order_id;
		int status;
		String addtime;
		public GetOrderDetail(String order_id,int status,String addtime ) {
			this.order_id = order_id;
			this.status = status;
			this.addtime = addtime;
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
//			Log.e("order_id", order_id+"");
//			Log.e("status", status+"");
			Intent intent_to_detail  = new Intent(c,B5_5_OrderDetail.class);
			intent_to_detail.putExtra("order_id", order_id);
			intent_to_detail.putExtra("status", status);
			intent_to_detail.putExtra("addtime", addtime);
			c.startActivity(intent_to_detail);
			
		}
		
	}
	/**
	 * 删除订单
	 * @author zyk
	 */
	class DeleteTheOrder implements View.OnClickListener {
		String order_id;
		public DeleteTheOrder(String order_id) {
			this.order_id = order_id;
		}
		@Override
		public void onClick(View v) {
			Tools.Notic(c, "是否删除该订单", new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					UIDialog.closeDialog();
					HttpUtils.deleteTheOrder(res_deleteTheOrder, key, order_id);
				}
			});
			
		}
		
	}

	private static class ViewHolder
    {
        TextView tv_addtime;
        TextView tv_orderprice;
        TextView tv_ordergoodsnumber;
        TextView tv_orderstatus;
        Button btn_deletetheorder;//取消订单
        Button btn_paytheorder;//接单
        Button btn_delete_this;//删除订单
        Button btn_tocomment;//待评价
        Button btn_tuihuanhuo;//退换货
        Button btn_querenshouhuo;//确认收货
        ListView listView;
    }
	
	/**
	 * 删除订单
	 */
	JsonHttpResponseHandler res_cancelOrder = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("取消订单="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas =response.getJSONObject("datas");
				 error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Tools.Notic(c, "取消成功,请刷新该页面查看剩余订单", null);
				notifyDataSetChanged();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error);
//				Tools.Notic(c, "取消失败,请重试", null);
				Tools.Notic(c, error+"", null);
			}
			
		}
	};
	/**
	 * 删除订单
	 */
	JsonHttpResponseHandler res_getTheOrder = new JsonHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode,headers,response);
			RequestDailog.closeDialog();
			Tools.Log("接单="+response);
			String error=null;
			JSONObject datas=null;
			try {
				datas =response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Tools.Notic(c, "接单成功,请刷新该页面查看剩余订单", null);
				notifyDataSetChanged();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error);
				Tools.Notic(c, "接单失败,请重试", null);
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
	};
	/**
	 * 订单确认收货
	 */
	JsonHttpResponseHandler res_receiveGoods = new JsonHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Log.e("确认收货", response+"");
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Tools.Notic(c, "您已经确认收货", new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						c.finish();
					}
				});
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
				Tools.Notic(c, error+"", null);
			}
			
		}
		
		
	};
	/**
	 * 删除订单
	 */
	JsonHttpResponseHandler res_deleteTheOrder = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
//			Toast.makeText(c, response+"", 500).show();
			Log.e("删除订单", response+"");
			String error=null;
			JSONObject datas=null;
			try {
				datas = response.getJSONObject("datas");
				error = datas.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{
				Tools.Notic(c, "删除订单成功,请刷新该页面查看剩余订单", null);
				notifyDataSetChanged();
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error);
				Tools.Notic(c, "接单失败,请重试", null);
			}
			
		}
		
		
	};
}
