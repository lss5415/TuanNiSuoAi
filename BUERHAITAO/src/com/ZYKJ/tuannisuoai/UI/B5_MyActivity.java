package com.ZYKJ.tuannisuoai.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.CircularImage;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.ZYKJ.tuannisuoai.view.ToastView;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("NewApi") public class B5_MyActivity extends BaseActivity implements OnClickListener {

	private CircularImage img_head;
	private Button btn_login,btn_shaidanquan,btn_chackInShape;
	private LinearLayout ll_chackin,ll_NoPay,ll_notransport,ll_noget,ll_haveget,ll_my_points,ll_my_purse;
	private RelativeLayout my_address_page,my_store_page,my_points_page,my_set_page;
	private TextView tv_my_points,my_money;//积分+钱包
	
	public String pl_points;//积分变更 (例:-10)
	public String pl_tatal_points ;//总积分 (例:100)
	public String pl_desc;//积分来源
	public String pl_addtime;//日期
	
    private static final int PAIZHAO = 14;
    private static final int XIANGCE = 15;
    private static final int CAIJIAN = 16;
    
    private static final int DAIFUKUAN = 10;
    private static final int DAIFAHUO = 20;
    private static final int DAISHOUHUO = 30;
    private static final int YISHOUHUO = 40;
	/**
	 * 上传头像的字段
	 */
	private String timeString;
	private String filename;
	private String cutnameString;
	private int FirstLog=0;
	private int ISLOGIN=0;
	
	public String usernameString=null;
	private Button signIn;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isLogin()) {
			ISLOGIN=1;
			HttpUtils.getPointsLog(res_Points, getSharedPreferenceValue("key"));
			String headImgString=getSharedPreferenceValue("headImg_filename");
//			if (headImgString!=null) //如果登录过，显示之前本地的头像
//			{
////			File f = new File(headImgString); 
//			    Bitmap bitmap_head=BitmapFactory.decodeFile(headImgString);
//			    img_head.setImageBitmap(bitmap_head);
//			    
//			}
			//网络加载头像
			ImageLoader.getInstance().displayImage(getSharedPreferenceValue("avatar"), img_head, ImageOptions.getOpstion(), animateFirstListener);
		}else if (FirstLog==0) {//第一次进来，如果没有登录，跳转到登录页面
			Intent intent_login1=new Intent();
			intent_login1.setClass(this, B5_1_LoginActivity.class);
			startActivity(intent_login1);
			FirstLog=1;
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_myactivity);
		
		img_head=(CircularImage) findViewById(R.id.img_head);
		btn_login=(Button) findViewById(R.id.btn_login);
		btn_shaidanquan=(Button) findViewById(R.id.btn_shaidanquan);
		btn_chackInShape=(Button) findViewById(R.id.btn_chackInShape);
		ll_chackin=(LinearLayout) findViewById(R.id.ll_chackin);
		ll_NoPay=(LinearLayout) findViewById(R.id.ll_NoPay);
		ll_notransport=(LinearLayout) findViewById(R.id.ll_notransport);
		ll_noget=(LinearLayout) findViewById(R.id.ll_noget);
		ll_haveget=(LinearLayout) findViewById(R.id.ll_haveget);
		ll_my_points=(LinearLayout) findViewById(R.id.ll_my_points);//积分
		ll_my_purse=(LinearLayout) findViewById(R.id.ll_my_purse);//我的钱包
		my_address_page=(RelativeLayout) findViewById(R.id.my_address_page);
		my_store_page=(RelativeLayout) findViewById(R.id.my_store_page);
		my_points_page=(RelativeLayout) findViewById(R.id.my_points_page);
		my_set_page=(RelativeLayout) findViewById(R.id.my_set_page);
		tv_my_points=(TextView) findViewById(R.id.tv_my_points);
		my_money=(TextView) findViewById(R.id.my_money);
		signIn=(Button) findViewById(R.id.signIn);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
		String time=sdf.format(new java.util.Date());
		String sds = getSharedPreferenceValue("shijian");
		if (getSharedPreferenceValue("shijian").equals(time)) {
			btn_chackInShape.setVisibility(View.GONE);
			signIn.setText("今日已签到！");
		}
		
		if (isLogin()) {
			if (getSharedPreferenceValue("username")!="") {
				btn_login.setText(getSharedPreferenceValue("username"));//设置登录按钮显示用户昵称
				btn_login.setTextColor(0xffFF5500);
			}else{
				btn_login.setText("更改昵称");
			}
			setListener(img_head,btn_shaidanquan,btn_login,
					   ll_chackin,ll_NoPay,ll_notransport,
					   ll_noget,ll_haveget,ll_my_points,ll_my_purse,my_address_page,
					   my_store_page,my_points_page,my_set_page,btn_chackInShape);
		}else{
			setListener(btn_login);
		}
//		Tools.Log("key="+getSharedPreferenceValue("key"));
		HttpUtils.getMoney(res_getMoney, getSharedPreferenceValue("key"));
		
		//显示积分
//		if (getSharedPreferenceValue("pl_points")!=null) 
//		{
//			tv_my_points.setText(getSharedPreferenceValue("pl_points"));
//		}
//		else
//		{
			HttpUtils.getPointsLog(res_Points, getSharedPreferenceValue("key"));
//		}
	}
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
			case R.id.img_head://上传头像
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
			case R.id.btn_login://登录
				if (ISLOGIN==1) //如果已经登录,点击之后弹出修改昵称的dialog
				{
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					final AlertDialog aDialog=builder.create();  
					aDialog.show();
					
					Window window = aDialog.getWindow();
					window.setContentView(R.layout.dialog_type_in);
					aDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//呼出输入法
					final EditText mEditText=(EditText) window.findViewById(R.id.et_type_in);
					Button btn_changeName=(Button) window.findViewById(R.id.btn_changeName);
					btn_changeName.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							 usernameString=mEditText.getText().toString().trim();
							 if (usernameString.equals("")) {
								 Toast.makeText(B5_MyActivity.this, "昵称不能为空", Toast.LENGTH_LONG).show();
							}else{
//								btn_login.setText(usernameString);//设置昵称并请求服务器
								aDialog.dismiss();
								//修改昵称网络请求
								HttpUtils.editname(res_editName, usernameString, getSharedPreferenceValue("key"));
								RequestDailog.showDialog(B5_MyActivity.this, "正在修改昵称，请稍后");
							}
							
						}
					});
					
					
					
				}else {
					Intent intent_login=new Intent();
					intent_login.setClass(this, B5_1_LoginActivity.class);
					startActivity(intent_login);
				}
				break;
			case R.id.btn_shaidanquan://晒单圈
//				Toast.makeText(this, "晒单圈", Toast.LENGTH_LONG).show();
//				intent_shaidanquan.setClass(this, MainActivity_we.class);
//				Intent intent_shaidanquan=new Intent();
//				intent_shaidanquan.setClass(this, B5_3_MyShaiDanQuan.class);
//				startActivity(intent_shaidanquan);

				Intent itshaidanquan = new Intent();
				itshaidanquan.setClass(B5_MyActivity.this, B5_3_ShaiDanQuan.class);
				startActivity(itshaidanquan);
				break;
			case R.id.btn_chackInShape://签到

				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
				String shijian=sdf.format(new java.util.Date());   
				putSharedPreferenceValue("shijian", shijian);
				btn_chackInShape.setVisibility(View.GONE);
				signIn.setText("今日已签到！");
				HttpUtils.chackIn(res_chackin, getSharedPreferenceValue("key"));
//				String qiandao = getSharedPreferenceValue("qiandao");
//				if (qiandao.equals("已签到")) {
//					
//				}
//				
//				RequestDailog.showDialog(this, "正在签到，请稍后");
//				btn_chackInShape.setVisibility(View.GONE);
//				signIn.setText("今日已签到！");
//				HttpUtils.chackIn(res_chackin, getSharedPreferenceValue("key"));
				break;
			case R.id.ll_NoPay://待付款
				Intent intent_daifukuan=new Intent();
				intent_daifukuan.setClass(this, B5_5_OrderStatus.class);
				intent_daifukuan.putExtra("STATUS", DAIFUKUAN);
				startActivity(intent_daifukuan);
				break;
			case R.id.ll_notransport://待发货
				Intent intent_daifahuo=new Intent();
				intent_daifahuo.setClass(this, B5_5_OrderStatus.class);
				intent_daifahuo.putExtra("STATUS", DAIFAHUO);
				startActivity(intent_daifahuo);
				break;
			case R.id.ll_noget://待收货
				Intent intent_daishouhuo=new Intent();
				intent_daishouhuo.setClass(this, B5_5_OrderStatus.class);
				intent_daishouhuo.putExtra("STATUS", DAISHOUHUO);
				startActivity(intent_daishouhuo);
				
				break;
			case R.id.ll_haveget://已收货
				Intent intent_yishouhuo=new Intent();
				intent_yishouhuo.setClass(this, B5_5_OrderStatus.class);
				intent_yishouhuo.putExtra("STATUS", YISHOUHUO);
				startActivity(intent_yishouhuo);
				
				break;
			case R.id.ll_my_points://积分
				Intent intent_my_points=new Intent();
				intent_my_points.setClass(this, B5_2_MyPointsDetail.class);
				startActivity(intent_my_points);
				break;
			case R.id.ll_my_purse://我的钱包
				Intent intent_my_purse=new Intent();
				intent_my_purse.setClass(this, B5_13_MyPurse.class);
				startActivity(intent_my_purse);
				break;
			case R.id.my_address_page://收货地址
				Intent intent_my_address_page=new Intent();
				intent_my_address_page.setClass(this, B5_9_MyAddressManagement.class);
				startActivity(intent_my_address_page);
				break;
			case R.id.my_store_page://我的收藏
				Intent intent_my_store_page=new Intent();
				intent_my_store_page.setClass(this, B5_10_MyCollection.class);
				startActivity(intent_my_store_page);
				
				break;
			case R.id.my_points_page://积分商城
				Intent intent_toPointsMall=new Intent();
				intent_toPointsMall.setClass(this, B5_11_PointsMall.class);
				startActivity(intent_toPointsMall);
				
				break;
			case R.id.my_set_page://设置页面
				Intent intent_set=new Intent();
				intent_set.setClass(this, B5_12_SetActivity.class);
				startActivity(intent_set);
				break;
		}
	}
	public boolean isLogin()
	{
		if (getSharedPreferenceValue("userid").equals("")) {
			return false;
		}else {
			return true;
		}
	}
//*****************************网络请求返回操作    begin******************************************
	/**
	 * 获取积分总分
	 */
	JsonHttpResponseHandler res_Points = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			
			Tools.Log("res_Points_response="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas =response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (error==null)//成功
			{
				try {
					pl_tatal_points=datas.getJSONArray("ponits_list").getJSONObject(0).getString("pl_total_points");
					putSharedPreferenceValue("pl_tatal_points", pl_tatal_points);
					tv_my_points.setText(pl_tatal_points);//设置总积分
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Tools.Notic(B5_MyActivity.this, "签到成功", null);
			}
			else//失败 
			{
				tv_my_points.setText(getSharedPreferenceValue("pl_tatal_points"));
				Tools.Log("res_Points_error="+error);
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
	};
	/**
	 * 获取钱包余额
	 */
	JsonHttpResponseHandler res_getMoney = new JsonHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			
			Tools.Log("钱包="+response);
			String error=null;
			JSONObject datas=null;
			try {
				datas =response.getJSONObject("datas");
				error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (error==null)//成功
			{
				try {
					String moneyString = datas.getString("predeposit");
					my_money.setText("￥"+moneyString);
					putSharedPreferenceValue("predeposit", moneyString);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else//失败 
			{
				my_money.setText("￥"+getSharedPreferenceValue("predeposit"));
				
//				Tools.Log("res_Points_error="+error);
			}
			
		}
	};
	/**
	 * 签到
	 */
	JsonHttpResponseHandler res_chackin = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
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
				putSharedPreferenceValue("chacked","1");//保存签到成功的状态，虽然重复签到的检测在服务器端已经做了判断
				String jifen = tv_my_points.getText().toString();
				int jf = Integer.parseInt(jifen)+5;
				tv_my_points.setText(String.valueOf(jf));
				Tools.Notic(B5_MyActivity.this, "签到成功", null);
			}
			else//失败 
			{
				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
		}
		
	};
	/**
	 * 上传头像之后的操作
	 */
	JsonHttpResponseHandler res_uploadavatar  = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_uploadavatar="+response);
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
					avatar=datas.getString("avatar");
					HttpUtils.saveAvatar(res_saveavatar,avatar,getSharedPreferenceValue("key"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else//失败 
			{
				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
//				String url = LandousAppConst.avatar_url_head + avatar_head;
//				ImageLoader.getInstance().displayImage(url, avatar_head_image,
//						BeeFrameworkApp.options_circle);
//			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			Tools.Log("res_update=网络连接超时");
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
	};
	JsonHttpResponseHandler res_saveavatar=new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			String error=null;
			String datas=null;
			Tools.Log("res_saveavatar="+response);
//			{"datas":{"avatar":"http:\/\/115.28.21.137\/data\/upload\/shop\/avatar\/avatar_26.jpg"},"code":200}
			try {
				 datas = response.getString("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error==null)//成功
			{

				Tools.Log("res_saveavatar="+datas);
			}
			else//失败 
			{
				Tools.Log("res_saveavatar_error="+error);
			}
			
		};
		
	};
	/**
	 * 修改昵称
	 */
	JsonHttpResponseHandler res_editName =new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
				btn_login.setText(usernameString);
				btn_login.setTextColor(0xffFF5500);
				putSharedPreferenceValue("username",usernameString);
				Tools.Log("res_editName="+datas);
			}
			else//失败 
			{
				Tools.Notic(B5_MyActivity.this, error.toString(), null);
				Tools.Log("res_editName="+error);
			}
		};
		
	};
//*****************************网络请求返回操作    end******************************************
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
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
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
			img_head.setImageBitmap(photo);//设置头像
//			avatar_head_image.setBackgroundDrawable(drawable);
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
		HttpUtils.update(res_uploadavatar,getSharedPreferenceValue("key"),"avatar",f);
	}
//*****************************图像处理操作     end******************************************
	// 退出操作
	private boolean isExit = false;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				ToastView toast = new ToastView(getApplicationContext(),
						"再按一次退出程序");
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Handler mHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						isExit = false;
					}
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);
				return true;
			} else {
				android.os.Process.killProcess(android.os.Process.myPid());
				return false;
			}
		}
		return true;
	}
	
}
