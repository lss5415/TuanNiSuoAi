package com.ZYKJ.tuannisuoai.UI;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_5_OrderCommentAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.AppValue;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 订单评价页
 * @author zyk
 *
 */
public class B5_5_Comment_order extends BaseActivity {
	ImageView order_back;
	ListView listview_goodslist;
	TextView tv_ordergoodsnumber_c,tv_orderprice_c;
	B5_5_OrderCommentAdapter adapter;
//	Button btn_addComment;
	JSONArray extend_order_goods;
	String price;
	private String timeString;
	private String cutnameString;
	private String filename;
	private String goods_id;
	private String order_id;
	
    private static final int PAIZHAO = 14;
    private static final int XIANGCE = 15;
    private static final int CAIJIAN = 16;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b5_5_ordercomment);
		Intent intent = getIntent();
		String array = intent.getStringExtra("extend_order_goods");
		order_id = intent.getStringExtra("order_id");
		price    =  intent.getStringExtra("price");
		try {
			extend_order_goods = new JSONArray(array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initView();
		tv_ordergoodsnumber_c.setText("共"+extend_order_goods.length()+"件商品");
		tv_orderprice_c.setText("实付:￥"+price);
		Log.e("extend_order_goods", extend_order_goods+"");
		adapter = new B5_5_OrderCommentAdapter(this,extend_order_goods,getSharedPreferenceValue("userid"),getSharedPreferenceValue("key"),order_id);
		listview_goodslist.setAdapter(adapter);
		setListener(order_back);
	}
	private void initView() {
		// TODO Auto-generated method stub
		order_back = (ImageView) findViewById(R.id.order_back);
		listview_goodslist = (ListView) findViewById(R.id.listview_goodslist);
		tv_ordergoodsnumber_c = (TextView) findViewById(R.id.tv_ordergoodsnumber_c);
		tv_orderprice_c = (TextView) findViewById(R.id.tv_orderprice_c);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_back:
			this.finish();
			break;

		default:
			break;
		}
	}
	//*****************************onActivityResult操作    begin******************************************
			public void onActivityResult(int requestCode, int resultCode, Intent data) 
			{
				super.onActivityResult(requestCode, resultCode, data);
				switch (requestCode)
				{
					case XIANGCE:
//						Log.e("goods_id", goods_id);
						try {
							startPhotoZoom(data.getData());
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(this, "您没有选择任何照片", Toast.LENGTH_LONG)
									.show();
						}
						break;
						// 如果是调用相机拍照时
					case PAIZHAO:
//						Log.e("goods_id", goods_id);
						// File temp = new File(Environment.getExternalStorageDirectory()
						// + "/xiaoma.jpg");
						// 给图片设置名字和路径
						File temp = new File(Environment.getExternalStorageDirectory()
								.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
						startPhotoZoom(Uri.fromFile(temp));
						break;
						// 取得裁剪后的图片
					case CAIJIAN:
						/**
						 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
						 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
						 * 
						 */
						if (data != null) {
							setPicToView(data);
						}
						break;
				}
			}
		//*****************************onActivityResult操作     end******************************************
			
			//*****************************图像处理操作     begin******************************************
			/**
			 * 裁剪图片方法实现
			 * 
			 * @param uri
			 */
			public void startPhotoZoom(Uri uri) {
				/*
				 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
				 * yourself_sdk_path/docs/reference/android/content/Intent.html
				 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
				 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
				 */
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");
				// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
				intent.putExtra("crop", "true");
				// aspectX aspectY 是宽高的比例
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				// outputX outputY 是裁剪图片宽高
				intent.putExtra("outputX", 150);
				intent.putExtra("outputY", 150);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CAIJIAN);
			}	
			
			/**
			 * 保存裁剪之后的图片数据
			 * 
			 * @param picdata
			 */
			private void setPicToView(Intent picdata) {
				Bundle extras = picdata.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					Drawable drawable = new BitmapDrawable(photo);
					/**
					 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
					 */
					savaBitmap(photo);
//					img_head.setImageBitmap(photo);//设置头像
//					avatar_head_image.setBackgroundDrawable(drawable);
				}
			}
			// 将剪切后的图片保存到本地图片上！
			public void savaBitmap(Bitmap bitmap) {
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"'IMG'_yyyyMMddHHmmss");
				cutnameString = dateFormat.format(date);
				filename = Environment.getExternalStorageDirectory().getPath() + "/"
						+ cutnameString + ".jpg";
				Tools.Log("filename="+filename);
				File f = new File(filename);
				
				FileOutputStream fOut = null;
				try {
					f.createNewFile();
					fOut = new FileOutputStream(f);
				} catch (Exception e) {
					e.printStackTrace();
				}
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				RequestDailog.showDialog(this, "正在上传图片,请稍后");
				HttpUtils.upPhoto(res_upPhoto,getSharedPreferenceValue("key"),"avatar",f);
			}
		//*****************************图像处理操作     end******************************************
		/**
		 * 上传头像之后的操作
		 */
			JsonHttpResponseHandler res_upPhoto  = new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					RequestDailog.closeDialog();
					Tools.Log("头像="+response);
					String error=null;
					JSONObject datas=null;
					String avatar =null;
					try {
						 datas = response.getJSONObject("datas");
						 error = datas.getString("error");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (error==null)//成功
					{
						try {
							Tools.Log("上传成功");
							avatar=datas.getString("image_name");
//							Log.e("image_name", avatar);
//							Log.e("goods_id",goods_id);
							AppValue.map_photo.put("avatar",avatar);
//							adapter.setMap_photo(goods_id,avatar);
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else//失败 
					{
						Tools.Notic(B5_5_Comment_order.this, error+"", null);
					}
//						String url = LandousAppConst.avatar_url_head + avatar_head;
//						ImageLoader.getInstance().displayImage(url,avatar_head_image,BeeFrameworkApp.options_circle);
//					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					Tools.Log("res_update=网络连接超时");
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
			};
}
