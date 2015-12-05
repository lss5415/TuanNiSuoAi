package com.ZYKJ.tuannisuoai.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.Comment;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class B5_5_OrderCommentAdapter extends BaseAdapter {
	
	private Activity c;
	JSONArray extend_order_goods;
	
	
    private static final int PAIZHAO = 14;
    private static final int XIANGCE = 15;
	private String timeString;
	String  userid,key,order_id;
	private OnItemPhotoListener listener;
	private HashMap<String, Comment> object;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    
	public B5_5_OrderCommentAdapter(Activity c, JSONArray extend_order_goods2 , String userid, String key, 
			String order_id, HashMap<String, Comment> object, OnItemPhotoListener listener) {
		this.c = c;
		this.extend_order_goods = extend_order_goods2;
		this.userid = userid;
		this.key = key;
		this.order_id = order_id;
		this.object = object;
		this.listener = listener;
	}

	
	@Override
	public int getCount() {
		return extend_order_goods == null ? 0 : extend_order_goods.size();
	}
	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(R.layout.ui_b5_5_orderlist_list_items_comment, null);
            viewHolder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            viewHolder.tv_goodsprice = (TextView) convertView.findViewById(R.id.tv_goodsprice);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            viewHolder.et_comment = (EditText) convertView.findViewById(R.id.et_comment);
            viewHolder.iv_takePhoto = (ImageView) convertView.findViewById(R.id.iv_takePhoto);
            viewHolder.btn_addComment = (GridView) convertView.findViewById(R.id.btn_addComment);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		JSONObject  extend_order_goods1 = (JSONObject) extend_order_goods.get(position);
		String goods_image_url = extend_order_goods1.getString("goods_image_url");
		final String goods_id = extend_order_goods1.getString("goods_id").toString();
		ImageLoader.getInstance().displayImage(goods_image_url, viewHolder.iv_product, ImageOptions.getOpstion(), animateFirstListener);//设置产品图片
		viewHolder.tv_productName.setText(extend_order_goods1.getString("goods_name").toString());//设置产品名称
		viewHolder.tv_goodsprice.setText("￥"+extend_order_goods1.getString("goods_price").toString());//设置产品价格
		viewHolder.tv_number.setText("X"+extend_order_goods1.getString("goods_num").toString());
		viewHolder.iv_takePhoto.setOnClickListener(new TakePhoto(goods_id));
		viewHolder.ratingBar.setRating(object.get(goods_id).getStars());
		viewHolder.et_comment.setText(object.get(goods_id).getContent());
		viewHolder.btn_addComment.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				ArrayList<Bitmap> bits = object.get(goods_id).getImages();
				return bits == null?0:bits.size();
			}
			@Override
			public Object getItem(int index) {
				return object.get(goods_id).getImages().get(index);
			}
			@Override
			public long getItemId(int index) {
				return index;
			}
			@Override
			public View getView(int index, View photoView, ViewGroup parent) {
				if(photoView == null){
					photoView = LayoutInflater.from(c).inflate(R.layout.goodspecial_item, null);
				}
				ImageView imageView = (ImageView) photoView.findViewById(R.id.im_b1_a1_pic);
				imageView.setImageBitmap(object.get(goods_id).getImages().get(index));
				return photoView;
			}
		});
		viewHolder.ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				object.get(goods_id).setStars(rating);
			}
		});
		viewHolder.et_comment.addTextChangedListener(new TextWatcher() {
            @Override  
            public void onTextChanged(CharSequence s, int start,  
                    int before, int count) {  
            }  
            @Override  
            public void beforeTextChanged(CharSequence s, int start,  
                    int count, int after) {  
            }  
            @Override  
            public void afterTextChanged(Editable s) {  
            	object.get(goods_id).setContent(s.toString());  
            } 
		});
		return convertView;
	}
	/**
	 * 点击立即兑换按钮之后的跳转
	 * @author zyk
	 *
	 */
	class TakePhoto implements View.OnClickListener {
		String goods_id;
		public TakePhoto(String goods_id) {
			this.goods_id = goods_id;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_takePhoto:
				B5_5_OrderCommentAdapter.this.listener.addPhoto(goods_id);
				UIDialog.ForThreeBtn(c, new String[] { "相册", "拍照","取消" }, this);
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
				c.startActivityForResult(intent_toXIANGCE, XIANGCE);
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
				c.startActivityForResult(intent_PAIZHAO, PAIZHAO);
				break;
			case R.id.dialog_modif_3:// 取消
				UIDialog.closeDialog();
				break;
			default:
				break;
			}
		}
	}
	
	private static class ViewHolder{
        TextView tv_productName;
        TextView tv_goodsprice;
        TextView tv_number;
        ImageView iv_product;//
        RatingBar ratingBar;
        EditText et_comment;
        ImageView iv_takePhoto;
        GridView btn_addComment;
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
	
	public interface OnItemPhotoListener{
		void addPhoto(String goodid);//上传图片
	}
}
