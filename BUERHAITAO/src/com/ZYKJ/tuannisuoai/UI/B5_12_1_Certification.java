package com.ZYKJ.tuannisuoai.UI;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 实名认证
 * @author zyk
 *
 */
public class B5_12_1_Certification extends BaseActivity {
	ImageButton certification_back;
	EditText et_name,et_birthday,et_ad;
	ImageView iv_left,iv_right;
	Button btn_certification;
	private int left=0;
	private int right=0;
	private int photocunt=0;
	
    private static final int PAIZHAO = 4;
    private static final int XIANGCE = 5;
    private static final int CAIJIAN = 6;
	/**
	 * 上传头像的字段
	 */
	private String timeString;
	private String filename;
	private String cutnameString;
	private String image_name;//上传身份证图像拼接的字段
	
	private String name,birthday,address;
	
	private Bitmap photo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b5_12_1_certification);
		
		certification_back=(ImageButton) findViewById(R.id.certification_back);
		et_name=(EditText) findViewById(R.id.et_name_c);
		et_birthday=(EditText) findViewById(R.id.et_birthday);
		et_ad=(EditText) findViewById(R.id.et_ad);
		if (getSharedPreferenceValue("certifi_name")!=null) {
			et_name.setText(getSharedPreferenceValue("certifi_name"));
			et_birthday.setText(getSharedPreferenceValue("certifi_birthday"));
			et_ad.setText(getSharedPreferenceValue("certifi_address"));
		}
		btn_certification=(Button) findViewById(R.id.btn_certification);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_right=(ImageView) findViewById(R.id.iv_right);
		setListener(certification_back,btn_certification,iv_left,iv_right);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.certification_back://返回
			this.finish();
			break;
		case R.id.iv_left://点击上传图片正面
			left=1;
			right=0;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.iv_right://点击上传图片反面
			left=0;
			right=1;
			UIDialog.ForThreeBtn(this, new String[] { "相册", "拍照","取消" }, this);
			break;
		case R.id.btn_certification://认证
			name=et_name.getText().toString().trim();
			birthday=et_birthday.getText().toString().trim();
			address=et_ad.getText().toString().trim();
			if (photocunt>1) {
				RequestDailog.showDialog(this, "正在认证，请稍后");
				Tools.Log("key="+getSharedPreferenceValue("key"));
				Tools.Log("name="+name);
				Tools.Log("birthday="+birthday);
				Tools.Log("address="+address);
				Tools.Log("image_name="+image_name);
				HttpUtils.certificate(res_certificate, getSharedPreferenceValue("key"), name, birthday, address, image_name);
			}else {
				Tools.Notic(this, "请先上传身份证正反面照片", null);
			}
			break;
			
		case R.id.dialog_modif_1:// 相册
			UIDialog.closeDialog();
			/**
			 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
			 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
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
				photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
				 */
				savaBitmap(photo);
//				if (left==1&&right==0) {
//					iv_left.setImageBitmap(photo);//设置显示
//				}else if (left==0&&right==1) {
//					iv_right.setImageBitmap(photo);//设置显示
//				}
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
			
			File f = new File(filename);

	        putSharedPreferenceValue("headImg_filename", filename);
			
			FileOutputStream fOut = null;
			try {
				f.createNewFile();
				fOut = new FileOutputStream(f);
				HttpUtils.uploadIDcard(res_uploadIDcard , getSharedPreferenceValue("key"),"avatar",f);//上传身份证接口
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
		}
	//*****************************图像处理操作     end******************************************
		/**
		 * 上传身份证之后的操作
		 */
		JsonHttpResponseHandler res_uploadIDcard  = new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestDailog.closeDialog();
				String error=null;
				JSONObject datas=null;
				Tools.Log("认证="+response+"");
				try {
					 datas = response.getJSONObject("datas");
					 error = datas.getString("error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (error==null)//成功
				{
					photocunt++;
					try {
						if (image_name==null) {
							image_name=datas.getString("image_name");
						}else {
							image_name=image_name+","+datas.getString("image_name");
						}
						Tools.Log("image_name_"+photocunt+"="+image_name+"");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (left==1&&right==0) {
						iv_left.setImageBitmap(photo);//设置显示
					}
					if (left==0&&right==1) {
						iv_right.setImageBitmap(photo);//设置显示
					}
				}
				else//失败 
				{
					Tools.Notic(B5_12_1_Certification.this, error+"", null);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				Tools.Log("res_update=网络连接超时");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		};
		JsonHttpResponseHandler res_certificate  = new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestDailog.closeDialog();
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
					putSharedPreferenceValue("certifi_name",name);
					putSharedPreferenceValue("certifi_birthday",birthday);
					putSharedPreferenceValue("certifi_address",address);
					Tools.Notic(B5_12_1_Certification.this, "信息上传成功，正在审核", new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							putSharedPreferenceValue("isCertificated", "1");
							B5_12_1_Certification.this.finish();
						}
					});
					
				}
				else//失败 
				{
					Tools.Notic(B5_12_1_Certification.this, error+"", null);
				}
				
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
