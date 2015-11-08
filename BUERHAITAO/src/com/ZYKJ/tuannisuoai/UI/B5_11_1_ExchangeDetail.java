package com.ZYKJ.tuannisuoai.UI;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 兑换详情页面
 * @author zyk
 *
 */
public class B5_11_1_ExchangeDetail extends BaseActivity {
	ImageButton set_back;
	RelativeLayout choseAddress;
	ImageView iv_product,iv_addtag,iv_rightarrow;
	TextView tv_producName,tv_productIntro,tv_productPoints,tv_address;
	Button btn_exchange;
	String pgoods_id,address_id ;
	public EditText et_pcart_message;
	
	private int GetAddress=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b5_11_1_exchangedetail);
		
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();

        
		set_back=(ImageButton) findViewById(R.id.set_back);
		choseAddress=(RelativeLayout) findViewById(R.id.choseAddress);
		tv_address=(TextView) findViewById(R.id.tv_address);
		iv_product=(ImageView) findViewById(R.id.iv_product);
		iv_addtag=(ImageView) findViewById(R.id.iv_addtag);
		iv_rightarrow=(ImageView) findViewById(R.id.iv_rightarrow);
		tv_producName=(TextView) findViewById(R.id.tv_producName);
		tv_productIntro=(TextView) findViewById(R.id.tv_productIntro);
		tv_productPoints=(TextView) findViewById(R.id.tv_productPoints);
		btn_exchange=(Button) findViewById(R.id.btn_exchange);
		et_pcart_message=(EditText) findViewById(R.id.et_pcart_message);
		
		
		ImageLoader.getInstance().displayImage(bundle.getString("pgoods_image"), iv_product);
		tv_producName.setText(bundle.getString("pgoods_name"));
		tv_productIntro.setText(bundle.getString("pgoods_body"));
		tv_productPoints.setText(bundle.getString("pgoods_points"));
		pgoods_id=bundle.getString("pgoods_id");
		
		setListener(set_back,choseAddress,btn_exchange);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_back://返回
			this.finish();
			break;
		case R.id.choseAddress://选择收货地址
			Intent i_tochoseAddress = new Intent(B5_11_1_ExchangeDetail.this,B5_9_MyAddressManagement.class);
			i_tochoseAddress.putExtra("ChoseAddress", true);
			startActivityForResult(i_tochoseAddress, GetAddress);
			break;
		case R.id.btn_exchange://立即兑换
			String pcart_message =  et_pcart_message.getText().toString().trim();
			HttpUtils.addPointsOrder(res_addPointsOrder, getSharedPreferenceValue("key"), pgoods_id, pcart_message,address_id);
			Tools.Log("key="+getSharedPreferenceValue("key"));
			Tools.Log("pgoods_id="+pgoods_id);
			Tools.Log("pcart_message="+pcart_message);
			Tools.Log("address_id="+address_id);
			break;

		default:
			break;
		}
	}
	/**
	 * 选择地址之后的回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==GetAddress) {
			iv_addtag.setVisibility(View.GONE);
			iv_rightarrow.setVisibility(View.GONE);
			address_id=data.getStringExtra("address_id");
			tv_address.setText("姓名:"+data.getStringExtra("true_name")+"  电话:"+data.getStringExtra("mob_phone")+"\n"+data.getStringExtra("area_info")+data.getStringExtra("address"));
		}
	}
	JsonHttpResponseHandler res_addPointsOrder = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_addPointsOrder="+response);
//			Toast.makeText(B5_11_1_ExchangeDetail.this, response+"", Toast.LENGTH_LONG).show();
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				Toast.makeText(B5_11_1_ExchangeDetail.this, "兑换成功", Toast.LENGTH_LONG).show();
				B5_11_1_ExchangeDetail.this.finish();
//				Tools.Log("datas="+datas);
/*				try {
					Tools.Notic(B5_11_1_ExchangeDetail.this, "datas="+datas.getString("error"), null);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			else//失败 
			{
//				Tools.Log("res_Points_error="+error+"");
				Tools.Notic(B5_11_1_ExchangeDetail.this, "error="+error, null);
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			Tools.Notic(B5_11_1_ExchangeDetail.this, "网络连接失败，请重试", null);
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
	
	};
}
