package com.ZYKJ.tuannisuoai.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.data.AppValue;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.UIDialog;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class B5_5_OrderCommentAdapter extends BaseAdapter {
	
	private Activity c;
	JSONArray extend_order_goods;
	
	
    private static final int PAIZHAO = 14;
    private static final int XIANGCE = 15;
    private static final int CAIJIAN = 16;
	private String timeString;
	private String cutnameString;
	private String filename;
	private int position1;
	String  userid,key,order_id;
    
	public B5_5_OrderCommentAdapter(Activity c, JSONArray extend_order_goods2 , String userid, String key, String order_id) {
		this.c = c;
		this.extend_order_goods = extend_order_goods2;
		this.userid = userid;
		this.key = key;
		this.order_id = order_id;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return extend_order_goods == null ? 0 : extend_order_goods.length();
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
            convertView = mInflater.inflate(R.layout.ui_b5_5_orderlist_list_items_comment, null);
            viewHolder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            viewHolder.tv_goodsprice = (TextView) convertView.findViewById(R.id.tv_goodsprice);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            viewHolder.et_comment = (EditText) convertView.findViewById(R.id.et_comment);
            viewHolder.iv_takePhoto = (ImageView) convertView.findViewById(R.id.iv_takePhoto);
            viewHolder.btn_addComment = (Button) convertView.findViewById(R.id.btn_addComment);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
    	try {
			JSONObject  extend_order_goods1 = (JSONObject) extend_order_goods.get(position);
			String goods_image_url = extend_order_goods1.getString("goods_image_url");
			String goods_id = extend_order_goods1.getString("goods_id").toString();
			ImageLoader.getInstance().displayImage(goods_image_url, viewHolder.iv_product);//设置产品图片
			viewHolder.tv_productName.setText(extend_order_goods1.getString("goods_name").toString());//设置产品名称
			viewHolder.tv_goodsprice.setText("￥"+extend_order_goods1.getString("goods_price").toString());//设置产品价格
			viewHolder.tv_number.setText("X"+extend_order_goods1.getString("goods_num").toString());
			viewHolder.iv_takePhoto.setOnClickListener(new TakePhoto(goods_id));
			String comment = viewHolder.et_comment.getText().toString().trim();
			String score = viewHolder.ratingBar.getRating()+"";
			String image = AppValue.map_photo.get("avatar");
			viewHolder.btn_addComment.setOnClickListener(new Comment(key,goods_id,comment,score,image,order_id));
			Tools.Log("userid"+userid+"avatar"+AppValue.map_photo.get("avatar"));
			ImageLoader.getInstance().displayImage("http://115.28.21.137/data/upload/shop/member/"+userid+"/"+image, viewHolder.iv_takePhoto);
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_takePhoto:
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
	}
	/**
	 * 确认评价
	 * @author zyk
	 *
	 */
	class Comment implements View.OnClickListener {
		String key;
		String goods_id;
		String order_id;
		String comment;
		String score;
		String image;
		public Comment(String key,String goods_id,String comment,String score,String image,String order_id) {
			this.key = key;
			this.goods_id = goods_id;
			this.order_id = order_id;
			this.comment = comment;
			this.score = score;
			this.image = image;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Map<String,Map<String, String>> map = new HashMap<String, Map<String, String>>();
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("comment",comment);
			map1.put("score",score);
			map1.put("image",image);
			map.put(goods_id,map1);
			Gson gson = new Gson();
			String jsonStr = gson.toJson(map);
			Log.e("map", jsonStr);
			HttpUtils.orderEvaluation(res_orderEvaluation, key,order_id, jsonStr);
		}
	}
	private static class ViewHolder
    {
        TextView tv_productName;
        TextView tv_goodsprice;
        TextView tv_number;
        ImageView iv_product;//
        RatingBar ratingBar;
        EditText et_comment;
        ImageView iv_takePhoto;
        Button btn_addComment;
    }
	JsonHttpResponseHandler res_orderEvaluation = new JsonHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			Log.e("订单评价结果", response+"");
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
				Tools.Notic(c, "评价成功", new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						c.finish();
					}
				});
			}
			else//失败 
			{
				Tools.Notic(c, error.toString(), null);
			}
			
		}
		
		
	};
}
