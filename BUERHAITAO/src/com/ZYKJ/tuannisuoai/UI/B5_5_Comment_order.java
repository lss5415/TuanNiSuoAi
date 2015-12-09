package com.ZYKJ.tuannisuoai.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_5_OrderCommentAdapter;
import com.ZYKJ.tuannisuoai.adapter.B5_5_OrderCommentAdapter.OnItemPhotoListener;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.Comment;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.StringUtil;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * 订单评价页
 * 
 * @author zyk
 * 
 */
public class B5_5_Comment_order extends BaseActivity implements
		OnItemPhotoListener {
	ImageView order_back;
	ListView listview_goodslist;
	TextView tv_ordergoodsnumber_c, tv_orderprice_c;
	B5_5_OrderCommentAdapter adapter;
	private HashMap<String, Comment> object = new HashMap<String, Comment>();
	private Button btn_addComment;
	private JSONArray extend_order_goods;
	private String price;
	private String timeString;
	private String cutnameString;
	private String filename;
	private String goods_id;
	private String order_id;
	private int index = 0,imgIndex = 0;
	private Comment comment;

	private static final int PAIZHAO = 14;
	private static final int XIANGCE = 15;
	private static final int CAIJIAN = 16;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b5_5_ordercomment);
		Intent intent = getIntent();
		String array = intent.getStringExtra("extend_order_goods");
		order_id = intent.getStringExtra("order_id");
		price = intent.getStringExtra("price");
		extend_order_goods = JSONArray.parseArray(array);
		initView();
		tv_ordergoodsnumber_c.setText("共" + extend_order_goods.size()+ "件商品");
		tv_orderprice_c.setText("实付:￥" + price);
		Log.e("extend_order_goods", extend_order_goods + "");
		for (int i = 0; i < extend_order_goods.size(); i++) {
			JSONObject goods = extend_order_goods.getJSONObject(i);
			Comment comment = new Comment();
			comment.setGoodid(goods.getString("goods_id"));
			comment.setStars(0f);
			comment.setContent("");
			comment.setFiles(new ArrayList<File>());
			comment.setImages(new ArrayList<Bitmap>());
			object.put(goods.getString("goods_id"), comment);
		}
		adapter = new B5_5_OrderCommentAdapter(this, extend_order_goods,
				getSharedPreferenceValue("userid"),
				getSharedPreferenceValue("key"), order_id, object, this);
		listview_goodslist.setAdapter(adapter);
	}

	private void initView() {
		order_back = (ImageView) findViewById(R.id.order_back);
		listview_goodslist = (ListView) findViewById(R.id.listview_goodslist);
		tv_ordergoodsnumber_c = (TextView) findViewById(R.id.tv_ordergoodsnumber_c);
		tv_orderprice_c = (TextView) findViewById(R.id.tv_orderprice_c);
		btn_addComment = (Button) findViewById(R.id.btn_addComment);
		setListener(order_back, btn_addComment);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_back:
			this.finish();
			break;
		case R.id.btn_addComment:
			index = 0;
			uploadComment(index);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 上传评论图片
	 * @param position
	 */
	private void uploadComment(int position){
		if(index < extend_order_goods.size()){
			String goodid = extend_order_goods.getJSONObject(position).getString("goods_id");
			comment = object.get(goodid);
			if(comment.getFiles().size() > 0){
				//上传图片
				imgIndex = 0;
				HttpUtils.upPhoto(res_upPhoto,getSharedPreferenceValue("key"), "avatar", comment.getFiles().get(imgIndex));
			}else{
				index++;
				uploadComment(index);
			}
		}else{
			submitComments();
		}
	}
	
	/**
	 * 上传评论信息
	 */
	private void submitComments() {
		JSONObject submit = new JSONObject();
		for (Comment goodComment : object.values()) {
			if(goodComment.getStars()>0 || goodComment.getContent().length()>0 || goodComment.getFiles().size()>0){
				String name = StringUtil.toString(goodComment.getImageName(), "");
				JSONObject goodObject = new JSONObject();
				goodObject.put("comment", goodComment.getContent());
				goodObject.put("score", goodComment.getStars());
				goodObject.put("image", name.length()>0?name.substring(0, name.length()-1):"");
				submit.put(goodComment.getGoodid(), goodObject);
			}
		}
		HttpUtils.orderEvaluation(res_orderEvaluation, getSharedPreferenceValue("key"), order_id, submit.toString());
	}
	
	/**
	 * 上传该订单所有的图片
	 */
	AsyncHttpResponseHandler res_upPhoto = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				String responseString = new String(responseBody, HTTP.UTF_8);
				JSONObject datas = JSONObject.parseObject(responseString);
				JSONObject jsonData = datas.getJSONObject("datas");
				String error = datas.getString("error");
				if(StringUtils.isEmpty(error)){
					String imgName = StringUtil.toString(comment.getImageName(), "");
					comment.setImageName(imgName + jsonData.getString("image_name") + ",");
					imgIndex++;
					if(imgIndex < comment.getFiles().size()){
						HttpUtils.upPhoto(res_upPhoto,getSharedPreferenceValue("key"), "avatar", comment.getFiles().get(imgIndex));
					}else{
						index++;
						uploadComment(index);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			Tools.Log("res_update=网络连接超时");
			Toast.makeText(B5_5_Comment_order.this, "请求失败", Toast.LENGTH_SHORT).show();
		}
	};
	
	/**
	 * 提交该订单所有的评论
	 */
	AsyncHttpResponseHandler res_orderEvaluation = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				String responseString = new String(responseBody, HTTP.UTF_8);
				JSONObject datas = JSONObject.parseObject(responseString);
				String error = datas.getString("error");
				if(StringUtils.isEmpty(error)){
					Tools.Notic(B5_5_Comment_order.this, "评价成功", new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							B5_5_Comment_order.this.finish();
						}
					});
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			Tools.Log("res_update=网络连接超时");
			Toast.makeText(B5_5_Comment_order.this, "请求失败", Toast.LENGTH_SHORT).show();
		}
	};

	// *****************************onActivityResult操作
	// begin******************************************
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case XIANGCE:
			try {
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				Toast.makeText(this, "您没有选择任何照片", Toast.LENGTH_LONG).show();
			}
			break;
		// 如果是调用相机拍照时
		case PAIZHAO:
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

	// *****************************onActivityResult操作
	// end******************************************

	// *****************************图像处理操作
	// begin******************************************
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
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//Drawable drawable = new BitmapDrawable(photo);
			/*下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似*/
			savaBitmap(photo);
			// avatar_head_image.setBackgroundDrawable(drawable);
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
		Tools.Log("filename=" + filename);
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
		List<Bitmap> bits = object.get(goods_id).getImages();
		if(bits != null){
			bits.add(bitmap);
		}else{
			ArrayList<Bitmap> bitList = new ArrayList<Bitmap>();
			bitList.add(bitmap);
			object.get(goods_id).setImages(bitList);
		}
		List<File> photos = object.get(goods_id).getFiles();
		if(photos != null){
			photos.add(f);
		}else{
			ArrayList<File> photoList = new ArrayList<File>();
			photoList.add(f);
			object.get(goods_id).setFiles(photoList);
		}
		adapter.notifyDataSetChanged();
	}

	// *****************************图像处理操作
	// end******************************************
	/**
	 * 上传头像之后的操作
	 */

	@Override
	public void addPhoto(String goodid) {
		this.goods_id = goodid;
	}
}
