package com.zykj.tuannisuoai_seller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
 * @version 创建时间：2015-7-25 上午11:41:41 
 */
@SuppressLint("NewApi") public class B5_2_certification extends BaseActivity {
	ImageButton certification_back;
	Button btn_certification;
	EditText et_name_c,et_birthday,et_address;
	ImageView iv_left,iv_right,iv_left_1,iv_right_1;
	String image_name_id = null;
	String image_name_li = null;
	int TAG = 0;
	/**
	 * 上传头像的字段
	 */
	private String timeString;
	private String filename;
	private String cutnameString;
	private static final int PAIZHAO = 14;
    private static final int XIANGCE = 15;
    private static final int CAIJIAN = 16;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_certification);
		initUI();
		setListener(certification_back,btn_certification,iv_left,iv_right,iv_left_1,iv_right_1);
		
	}
	private void initUI() {
		// TODO Auto-generated method stub
		certification_back = (ImageButton) findViewById(R.id.certification_back);
		btn_certification = (Button) findViewById(R.id.btn_certification);
		et_name_c = (EditText) findViewById(R.id.et_name_c);
		et_birthday = (EditText) findViewById(R.id.et_birthday);
		et_address = (EditText) findViewById(R.id.et_address);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_right = (ImageView) findViewById(R.id.iv_right);
		iv_left_1 = (ImageView) findViewById(R.id.iv_left_1);
		iv_right_1 = (ImageView) findViewById(R.id.iv_right_1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.certification_back://返回
			this.finish();
			break;
		case R.id.btn_certification://认证
			String truename = et_name_c.getText().toString().trim();
			String birthday = et_birthday.getText().toString().trim();
			String address = et_address.getText().toString().trim();
			String idcard = image_name_id;
			String license = image_name_li;
			if (truename.isEmpty()) {
				Toast.makeText(B5_2_certification.this, "姓名不能为空", Toast.LENGTH_LONG).show();
			}else if (birthday.isEmpty()) {
				Toast.makeText(B5_2_certification.this, "出生日期不能为空", Toast.LENGTH_LONG).show();
			}else if (address.isEmpty()) {
				Toast.makeText(B5_2_certification.this, "详细地址不能为空", Toast.LENGTH_LONG).show();
			}
			HttpUtils.certificate(res_certificate, getSharedPreferenceValue("key"), truename, birthday, address, idcard, license);
			break;
		case R.id.iv_left:
			TAG =1;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_right:
			TAG =2;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_left_1:
			TAG =3;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_right_1:
			TAG =4;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;

		case R.id.dialog_modif_1:// 相册
			UIDialog.closeDialog();
			/**
			 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
			 * 可以发现里面很多东西,Intent是个很强大的东西，大家一定仔细阅读下
			 */
			Intent intent_toXIANGCE = new Intent(Intent.ACTION_PICK, null);
			/**
			 * 下面这句话，与其它方式写是一样的效果，如果：
			 * intent.setData(MediaStore.Images
			 * .Media.EXTERNAL_CONTENT_URI);
			 * intent.setType(""image/*");设置数据类型
			 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
			 * ："image/jpeg 、 image/png等的类型"
			 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
			 */
			intent_toXIANGCE.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent_toXIANGCE, XIANGCE);
			break;
		case R.id.dialog_modif_2:// 拍照
			UIDialog.closeDialog();
			/**
			 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
			 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
			 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
			 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
			 */
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
				Bitmap photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
				 */
				savaBitmap(photo);
//				img_head.setImageBitmap(photo);//设置头像
//				avatar_head_image.setBackgroundDrawable(drawable);
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
			
			RequestDailog.showDialog(this, "正在上传头像，请稍后");
			//根据是上传的身份证还是执照，请求不同接口
			if (TAG<3)
			{
				HttpUtils.updateCertificationIdCard(res_updateCertificationIdCard,getSharedPreferenceValue("key"),"idcard",f);
			}
			else 
			{
				HttpUtils.updateCertificationLicense(res_updateCertificationLicense,getSharedPreferenceValue("key"),"license",f);
			}
		}
	//*****************************图像处理操作     end******************************************
		JsonHttpResponseHandler res_updateCertificationIdCard = new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				RequestDailog.closeDialog();
				// TODO Auto-generated method stub
				Log.e("response_id", response+"");
				JSONObject datas =null;
				String error = null;
				try {
					datas = response.getJSONObject("datas");
					error = datas.getString("error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (error == null)//成功
				{
					try {
						String image_name = null;
						image_name = datas.getString("image_name");//取到返回的图片名
						if (image_name_id == null) {
							image_name_id = image_name;
						}else {
							image_name_id+= ","+image_name;
						}
						switch (TAG) {
						case 1:
							ImageLoader.getInstance().displayImage("http://115.28.21.137/data/upload/shop/store/"+image_name, iv_left);
							break;
						case 2:
							ImageLoader.getInstance().displayImage("http://115.28.21.137/data/upload/shop/store/"+image_name, iv_right);
							break;

						default:
							break;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else //失败
				{
					Tools.Notic(B5_2_certification.this, "上传失败，请重试", null);
				}
				super.onSuccess(statusCode, headers, response);
			}
		};
		JsonHttpResponseHandler res_updateCertificationLicense = new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				RequestDailog.closeDialog();
				// TODO Auto-generated method stub
				Log.e("response_li", response+"");
				JSONObject datas =null;
				String error = null;
				try {
					datas = response.getJSONObject("datas");
					error = datas.getString("error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (error == null) //成功
				{
					try {
						String image_name = null;
						image_name = datas.getString("image_name");//取到返回的图片名
						if (image_name_li == null) {
							image_name_li = image_name;
						}else {
							image_name_li+= ","+image_name;
						}
						switch (TAG) {
						case 3:
							ImageLoader.getInstance().displayImage("http://115.28.21.137/data/upload/shop/store/"+image_name, iv_left_1);
							break;
						case 4:
							ImageLoader.getInstance().displayImage("http://115.28.21.137/data/upload/shop/store/"+image_name, iv_right_1);
							break;

						default:
							break;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else//失败
				{
					Tools.Notic(B5_2_certification.this, "上传失败，请重试", null);
				}
				super.onSuccess(statusCode, headers, response);
			}
		};
		JsonHttpResponseHandler res_certificate = new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				RequestDailog.closeDialog();
				// TODO Auto-generated method stub
				Log.e("认证", response+"");
				JSONObject datas =null;
				String error = null;
				try {
					datas = response.getJSONObject("datas");
					error = datas.getString("error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (error == null) //成功
				{
					Tools.Notic(B5_2_certification.this, "认证成功", new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							B5_2_certification.this.finish();
						}
					});
				}
				else//失败
				{
					Tools.Notic(B5_2_certification.this, "认证失败，请重试", null);
				}
				super.onSuccess(statusCode, headers, response);
			}
		};
}
