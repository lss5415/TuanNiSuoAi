package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.UI.B5_11_1_ExchangeDetail;
import com.nostra13.universalimageloader.core.ImageLoader;

public class B5_11_PointsMallAdapter extends BaseAdapter {
	
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Activity c;
	private LayoutInflater listContainer;
	
	private String pgoods_name=null;//产品名字
	private String pgoods_body=null;//产品介绍
	private String pgoods_points=null;//产品积分
	
	public B5_11_PointsMallAdapter(Activity c, List<Map<String, String>> data) {
		this.c = c;
		this.data = data;
		listContainer = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	public View getView(int position, View cellView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (cellView == null) {
			cellView = listContainer.inflate(R.layout.ui_points_mall_list_items, null);
		}
		TextView tv_productName=(TextView) cellView.findViewById(R.id.tv_productName);
		TextView tv_productIntro=(TextView) cellView.findViewById(R.id.tv_productIntro);
		TextView tv_productPoints=(TextView) cellView.findViewById(R.id.tv_productPoints);
		Button btn_exchange=(Button)cellView.findViewById(R.id.btn_exchange);	
		btn_exchange.setOnClickListener(new ExchangeListener(position));//立即兑换按钮绑定监听
//		if (data.get(position).get("ex_state").equals("end")) //表示不能兑换，
//		{
//			btn_exchange.setBackgroundColor(R.drawable.shape_btn_background_grey);
//		}
		ImageView iv_product=(ImageView) cellView.findViewById(R.id.iv_product);
		pgoods_name=data.get(position).get("pgoods_name");
		pgoods_body=data.get(position).get("pgoods_body");
		pgoods_points=data.get(position).get("pgoods_points");
		tv_productName.setText(pgoods_name);
		tv_productIntro.setText(pgoods_body);
		tv_productPoints.setText(pgoods_points);
		ImageLoader.getInstance().displayImage(data.get(position).get("pgoods_image"), iv_product);
		return cellView;
	}
	/**
	 * 点击立即兑换按钮之后的跳转
	 * @author zyk
	 *
	 */
	class ExchangeListener implements View.OnClickListener {
		int position;
		public ExchangeListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder=new AlertDialog.Builder(c);
			final AlertDialog alertDialog=builder.create();
			alertDialog.show();
			
			Window window = alertDialog.getWindow();
			window.setContentView(R.layout.dialog_yes_or_no);
			
			Button btn_yesButton=(Button) window.findViewById(R.id.btn_yes);
			Button btn_noButton=(Button) window.findViewById(R.id.btn_no);
			TextView dialog_notic_text=(TextView) window.findViewById(R.id.dialog_notic_text);
			dialog_notic_text.setText("确定使用"+data.get(position).get("pgoods_points")+"积分兑换"+data.get(position).get("pgoods_name"));
			btn_yesButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					Toast.makeText(c, "跳转到兑换详情", Toast.LENGTH_LONG).show();
					Intent intent_to_exchangepoints=new Intent(c,B5_11_1_ExchangeDetail.class);
					
					Bundle bundle =new Bundle();
					bundle.putString("pgoods_image", data.get(position).get("pgoods_image"));
					bundle.putString("pgoods_name", data.get(position).get("pgoods_name"));
					bundle.putString("pgoods_body", data.get(position).get("pgoods_body"));
					bundle.putString("pgoods_points", data.get(position).get("pgoods_points"));
					bundle.putString("pgoods_id", data.get(position).get("pgoods_id"));
					intent_to_exchangepoints.putExtras(bundle);
					
					c.startActivity(intent_to_exchangepoints);
					alertDialog.dismiss();
				}
			});
			btn_noButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
				}
			});
			
		
			
		}

	}

	
	
	

}
