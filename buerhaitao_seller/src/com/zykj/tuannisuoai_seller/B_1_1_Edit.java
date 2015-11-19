package com.zykj.tuannisuoai_seller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.LocationManagerProxy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.RequestDailog;
import com.zykj.tuannisuoai_seller.utils.Tools;
import com.zykj.tuannisuoai_seller.utils.UIDialog;

/** 
 * 类说明 
 * @author zyk 
 * @version 创建时间：2015-7-25 下午4:05:41 
 */
public class B_1_1_Edit extends BaseActivity {

	ImageView iv_back;
	ImageView iv_1,iv_2,iv_3;//店铺轮播图图片
	ImageView iv_logo;//店铺LOGO
	ImageView iv_getLocation;//定位
	TextView tv_choseaddress;//选择地址
	TextView tv_location;//定位之后显示的地址信息
	Button btn_save;
	EditText et_name_c,et_address,et_store_phone,et_freight;
	Double geoLat;//经度
    Double geoLng;//纬度
    String addressByGPS;
	
	String province,city,area,area_info,province_id,city_id,area_id,logopath;
	
	private static final int IV_1 = 101;
	private static final int IV_2 = 102;
	private static final int IV_3 = 103;
	private static final int IV_logo = 104;
	 
	int setImageTag = 0;
	String slide = ""; //轮播图多张逗号分隔
	String avatar  = null;//店铺图标
	int first = 0; //第一次上传图片
	Bitmap photo;
	String key;
	 
    private static final int PAIZHAO = 14;
    private static final int XIANGCE = 15;
    private static final int CAIJIAN = 16;
    
    private static final int GetArea=1;
    private static final int GetLocation=2;//定位
	/**
	 * 上传头像的字段
	 */
	private String timeString;
	private String filename;
	private String cutnameString;
	
	boolean logoIsUpdated = false;
	boolean areaIsUpdated = false;
	
	
	private LocationManagerProxy mLocationManagerProxy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_edit);
		initUI();
		Intent intent_getData = getIntent();
//		Log.e("intent_getData", intent_getData.getStringExtra("store_name")+"");
		logopath = intent_getData.getStringExtra("logopath");
		et_name_c.setText(intent_getData.getStringExtra("store_name"));
		tv_choseaddress.setText(intent_getData.getStringExtra("area_info")+"(点击此处进行地区选择)");
		et_address.setText(intent_getData.getStringExtra("store_address"));
		et_store_phone.setText(intent_getData.getStringExtra("store_phone"));
		if (logopath!=null) {
			ImageLoader.getInstance().displayImage(logopath, iv_logo);
		}
		key = getSharedPreferenceValue("key");
		setListener(iv_back,iv_1,iv_2,iv_3,iv_logo,tv_choseaddress,iv_getLocation,btn_save);
	}
	private void initUI() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_1 = (ImageView) findViewById(R.id.iv_1);
		iv_2 = (ImageView) findViewById(R.id.iv_2);
		iv_3 = (ImageView) findViewById(R.id.iv_3);
		iv_logo = (ImageView) findViewById(R.id.iv_logo);
		iv_getLocation = (ImageView) findViewById(R.id.iv_getLocation);
		tv_choseaddress = (TextView) findViewById(R.id.tv_choseaddress);
		btn_save = (Button) findViewById(R.id.btn_save);
		et_name_c = (EditText) findViewById(R.id.et_name_c);
		et_address = (EditText) findViewById(R.id.et_address);
		et_store_phone = (EditText) findViewById(R.id.et_store_phone);
		et_freight = (EditText) findViewById(R.id.et_freight);
		tv_location = (TextView) findViewById(R.id.tv_location);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.iv_getLocation://跳转到地图界面
			Intent intent_getlocation = new Intent(B_1_1_Edit.this,MultyLocationActivity.class);
			startActivityForResult(intent_getlocation, GetLocation);
			break;
		case R.id.tv_choseaddress:
			areaIsUpdated = true; 
			Intent choseIntent = new Intent(B_1_1_Edit.this,B5_9_1_getArea.class);
			startActivityForResult(choseIntent, GetArea);
			break;
		case R.id.iv_1:
			setImageTag = IV_1;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_2:
			setImageTag = IV_2;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_3:
			setImageTag = IV_3;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_logo:
			setImageTag = IV_logo;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;

		case R.id.dialog_modif_1:// 相册
			UIDialog.closeDialog();
			Intent intent_toXIANGCE = new Intent(Intent.ACTION_PICK, null);
			intent_toXIANGCE.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent_toXIANGCE, XIANGCE);
			break;
		case R.id.dialog_modif_2:// 拍照
			UIDialog.closeDialog();
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMddHHmmss");
			timeString = dateFormat.format(date);
			createSDCardDir();
			Intent intent_PAIZHAO = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			intent_PAIZHAO.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory()
							+ "/DCIM/Camera", timeString + ".jpg")));
			startActivityForResult(intent_PAIZHAO, PAIZHAO);
			break;
		case R.id.dialog_modif_3:// 取消
			UIDialog.closeDialog();
			break;
		case R.id.btn_save://保存编辑信息
			if (areaIsUpdated == false) {
				Toast.makeText(this, "请选择地区之后再保存", 500).show();
			}else {
				if (logoIsUpdated == false)//未更新头像
				{
					avatar= logopath.substring(logopath.lastIndexOf('/')+1);
					Log.e("avatar",avatar);
				}
				String store_name = et_name_c.getText().toString().trim();
				String store_address = et_address.getText().toString().trim();
				String store_phone = et_store_phone.getText().toString().trim();
				String store_freight_price = et_freight.getText().toString().trim();
				
				HttpUtils.editHomePage(res_editHomePage,key,slide,avatar, 
						store_name,province_id,city_id,area_info,
						store_address,geoLat+"",geoLng+"",addressByGPS, 
						store_phone, store_freight_price);
			}
			break;
			
		default:
			break;
		}
		super.onClick(v);
	}
	//*****************************onActivityResult操作    begin******************************************
		public void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
			switch (requestCode)
			{
				case XIANGCE:
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
				case GetArea://选择省市区
					if (data==null) {
						Tools.Notic(this, "您没有选择省市区", null);
					}else {
						province=data.getStringExtra("province");
						city=data.getStringExtra("city");
						area=data.getStringExtra("area");
						
						area_info=province+city+area;
						province_id=data.getStringExtra("province_id");
						city_id=data.getStringExtra("city_id");
						area_id=data.getStringExtra("area_id");
						tv_choseaddress.setText(area_info);//设置选择的省市区
					}
					break;
				case GetLocation://获取经纬度
					if (data==null) {
						Tools.Notic(this, "定位不成功,请重试", null);
					}else {
						geoLat=data.getDoubleExtra("geoLat", 0);//经度
						geoLng=data.getDoubleExtra("geoLng", 0);//纬度
						addressByGPS = data.getStringExtra("addressByGPS");
						tv_location.setText(addressByGPS);
//						Log.e("geoLat", geoLat+"");
//						Log.e("geoLng", geoLng+"");
//						Log.e("addressByGPS", addressByGPS+"");
					}
					 
					break;
			}
			
			super.onActivityResult(requestCode, resultCode, data);
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
		public void createSDCardDir() {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				// 创建一个文件夹对象，赋值为外部存储器的目录
				File sdcardDir = Environment.getExternalStorageDirectory();
				// 得到一个路径，内容是sdcard的文件夹路径和名字
				String path = sdcardDir.getPath() + "/DCIM/Camera";
				File path1 = new File(path);
				if (!path1.exists()) {
					// 若不存在，创建目录，可以在应用启动的时候创建
					path1.mkdirs();
				}
			}
		}
		/**
		 * 保存裁剪之后的图片数据
		 * 
		 * @param picdata
		 */
		private void setPicToView(Intent picdata) {
			Bundle extras = picdata.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
				 */
				savaBitmap(photo);
//				switch (setImageTag) {
//				case IV_1:
//					iv_1.setImageBitmap(photo);
//					break;
//				case IV_2:
//					iv_2.setImageBitmap(photo);
//					break;
//				case IV_3:
//					iv_3.setImageBitmap(photo);
//					break;
//				case IV_logo:
//					iv_logo.setImageBitmap(photo);
//					break;
//
//				default:
//					break;
//				}
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
	        putSharedPreferenceValue("headImg_filename", filename);
			
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
			
			RequestDailog.showDialog(this, "正在上传图片，请稍后");
			HttpUtils.update(res_uploadaPhoto,getSharedPreferenceValue("key"),"avatar",f);
		}
	//*****************************图像处理操作     end******************************************
		JsonHttpResponseHandler res_uploadaPhoto = new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				first++;
				switch (setImageTag) {
				case IV_1:
					iv_1.setImageBitmap(photo);
					break;
				case IV_2:
					iv_2.setImageBitmap(photo);
					break;
				case IV_3:
					iv_3.setImageBitmap(photo);
					break;
				case IV_logo:
					logoIsUpdated = true;
					iv_logo.setImageBitmap(photo);
					break;

				default:
					break;
				}
				RequestDailog.closeDialog();
				Tools.Log("上传图片返回值="+response);
				JSONObject datas = null;
				String error = null;
				try {
					datas = response.getJSONObject("datas");
					error = datas.getString("error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (error == null) {
					try {
						String imageString = datas.getString("image_name");
						if (setImageTag == IV_logo) //获取头像上传的返回值
						{
							avatar = imageString;//获取服务器返回的头像上传结果
							Log.e("avatar", avatar);
						}
						else //获取轮播图上传的返回值
						{
							if (first>1) 
							{
								slide = slide +","+ imageString; 
							}
							else//第一次进来的时候，不要加“，”进行连接
							{
								slide = slide + imageString; 
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Tools.Notic(B_1_1_Edit.this, error+"", null);
				}
				
			}
		};
		JsonHttpResponseHandler res_editHomePage = new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
//				Toast.makeText(B_1_1_Edit.this, response+"", 500).show();
//				Tools.Log("结果="+response);
				JSONObject datas = null;
				String error = null;
				try {
					datas = response.getJSONObject("datas");
					error = datas.getString("error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (error == null) {
					Tools.Notic(B_1_1_Edit.this, "发布成功", new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent_toHome = new Intent(B_1_1_Edit.this,B0_MainActivity.class);
							startActivity(intent_toHome);
							B_1_1_Edit.this.finish();
						}
					});
				}else {
					
				}
				super.onSuccess(statusCode, headers, response);
			}
			
			
			
		};

}
