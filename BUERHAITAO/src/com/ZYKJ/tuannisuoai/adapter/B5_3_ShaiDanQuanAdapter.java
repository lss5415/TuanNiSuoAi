package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.NoScrollGridView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("UseSparseArrays") public class B5_3_ShaiDanQuanAdapter extends BaseAdapter {

	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//	SparseArray<Integer> zanArray = new SparseArray<Integer>();
	private Activity c;
	private LayoutInflater listContainer;
	ViewHolder viewHolder = null;
	String commentString;
	String key;
	Integer zan_number;
//	ListView lv_comment;
	GridViewAdatper_myshaidanquan photosadapter;
	B5_3_MyShaiDanQuanPinglunAdapter adapter;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	public B5_3_ShaiDanQuanAdapter(Activity c, List<Map<String, Object>> data,String key) {
		this.c = c;
		this.data = data;
		this.key = key;
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
//		ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(R.layout.ui_b5_3_shaidanquanl_items, null);

            viewHolder.iv_head_img = (ImageView) convertView.findViewById(R.id.iv_head_img);//头像
            viewHolder.iv_zan = (ImageView) convertView.findViewById(R.id.iv_zan);//点赞按钮
            viewHolder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);//评论按钮
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);//昵称
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);//发布的时间
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);//发布的内容
            viewHolder.tv_num_look = (TextView) convertView.findViewById(R.id.tv_num_look);//多少人看过
            viewHolder.tv_zan = (TextView) convertView.findViewById(R.id.tv_zan);//赞数
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);//评论数
            viewHolder.gridView = (NoScrollGridView) convertView.findViewById(R.id.gridView);
//          viewHolder.comment0 = (LinearLayout) convertView.findViewById(R.id.comment0);
//          viewHolder.tv_checkallcomment = (TextView) convertView.findViewById(R.id.tv_checkallcomment);//查看所有3条评论
//          viewHolder.comment_container = (LinearLayout) convertView.findViewById(R.id.comment_container);//查看所有3条评论
            viewHolder.lv_comment = (ListView) convertView.findViewById(R.id.lv_comment);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        JSONObject obj = (JSONObject) data.get(position).get("image");
//        Tools.Log("obj.length="+obj.length());
        if (obj.length()!=0) {
        	viewHolder.gridView.setVisibility(View.VISIBLE);
        	photosadapter = new GridViewAdatper_myshaidanquan(c,obj);
        	viewHolder.gridView.setAdapter(photosadapter);
		}else {
			Tools.Log("没有上传图片");
		}
        
        //点赞按钮操作
        String circle_id = data.get(position).get("circle_id").toString();
        viewHolder.iv_zan.setOnClickListener(new Zan(position,key,circle_id));
        
        JSONArray array = (JSONArray) data.get(position).get("quote");
        //评论
        viewHolder.iv_comment.setOnClickListener(new Comment(position,key,circle_id));
       
        //评论的adapter
        adapter = new B5_3_MyShaiDanQuanPinglunAdapter(c,array,key,circle_id);
        viewHolder.lv_comment.setAdapter(adapter);
        //根据评论数的多少来确定评论显示的高度
        B5_3_MyShaiDanQuanPinglunAdapter listAdapter = (B5_3_MyShaiDanQuanPinglunAdapter) viewHolder.lv_comment.getAdapter();  
        if (listAdapter == null) { 
            return null; 
        } 
        int totalHeight = 0; 
        for (int i = 0; i < listAdapter.getCount(); i++) { 
            View listItem = listAdapter.getView(i, null, viewHolder.lv_comment); 
            listItem.measure(0, 0); 
            totalHeight += listItem.getMeasuredHeight(); 
        } 
        //设置评论的高度
        ViewGroup.LayoutParams params = viewHolder.lv_comment.getLayoutParams(); 
        params.height = totalHeight + (viewHolder.lv_comment.getDividerHeight() * (listAdapter.getCount())); 
        viewHolder.lv_comment.setLayoutParams(params); 
        
       
        zan_number = Integer.valueOf(data.get(position).get("praise").toString());
//        zanArray.put(position, zan_number);
        
		String replys = data.get(position).get("replys").toString();//评论数
		viewHolder.tv_nickname.setText(data.get(position).get("member_name").toString());//昵称
		viewHolder.tv_content.setText(data.get(position).get("description").toString());//内容
		viewHolder.tv_date.setText(data.get(position).get("addtime").toString());//日期
		viewHolder.tv_zan.setText(zan_number+"");//赞个数
		viewHolder.tv_comment.setText(replys);//评论数
		viewHolder.tv_num_look.setText(data.get(position).get("views")+"人浏览过");//123人浏览过
//		viewHolder.tv_checkallcomment.setText("查看所有"+replys+"条评论");//查看所有3条评论
//		viewHolder.tv_checkallcomment.setOnClickListener(new Checkallcomment(position));
//		viewHolder.comment0.setOnClickListener(new Comment(position));
		ImageLoader.getInstance().displayImage(data.get(position).get("avatar").toString(), viewHolder.iv_head_img, ImageOptions.getOpstion(), animateFirstListener);
//		ImageLoader.getInstance().displayImage(data.get(position).get("image").toString(), viewHolder.iv_head_img);
		return convertView;
	}
	/**
	 * 点赞
	 * @author zyk
	 *
	 */
	class Zan implements View.OnClickListener {
		int position;
		String key,circle_id;
		
		public Zan(int position ,String key,String circle_id) {
			this.position = position;
			this.key = key;
			this.circle_id = circle_id;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			HttpUtils.zan(res_zan, key, circle_id);
			B5_3_ShaiDanQuanAdapter.this.notifyDataSetChanged();
		}
		/**
		 * 点赞
		 */
		JsonHttpResponseHandler res_zan = new JsonHttpResponseHandler()
		{
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				RequestDailog.closeDialog();
				Tools.Log("点赞="+response);
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
//					zanArray.put(position, zanArray.get(position)+1);
//					B5_3_ShaiDanQuanAdapter.this.notifyDataSetChanged();
					Toast.makeText(c, "点赞成功，请刷新", Toast.LENGTH_LONG).show();
				}
				else//失败 
				{
					Tools.Log("res_Points_error="+error+"");
					Tools.Notic(c, error, null);
				}
				
			}
			
			
		};
	}
	//评论操作
	class Comment implements View.OnClickListener {
		int position;
		String key,circle_id;
		public Comment(int position,String key,String circle_id) {
			this.position = position;
			this.key = key;
			this.circle_id = circle_id;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			final AlertDialog.Builder builder = new AlertDialog.Builder(c);
			final AlertDialog aDialog=builder.create();  
			aDialog.show();
			
			Window window = aDialog.getWindow();
			window.setContentView(R.layout.dialog_type_in_comment);
			window.setGravity(Gravity.BOTTOM);
			aDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//呼出输入法
			final EditText mEditText=(EditText) window.findViewById(R.id.et_type_in);
			Button btn_changeName=(Button) window.findViewById(R.id.btn_changeName);
			btn_changeName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					commentString=mEditText.getText().toString().trim();
//					 if (commentString.equals("")) {
////						 Toast.makeText(c, "昵称不能为空", Toast.LENGTH_LONG).show();
//						 aDialog.dismiss();
//					}else{
////						btn_login.setText(usernameString);//设置昵称并请求服务器
//						aDialog.dismiss();
//						//评论的网络请求
////						HttpUtils.editname(res_editName, usernameString, getSharedPreferenceValue("key"));
////						RequestDailog.showDialog(B5_MyActivity.this, "正在修改昵称，请稍后");
//					}
					HttpUtils.comment(res_comment, key, circle_id, commentString, "", "");
					aDialog.dismiss();
				}
			});
		}
		/**
		 * 评论
		 */
		JsonHttpResponseHandler res_comment = new JsonHttpResponseHandler()
		{
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				RequestDailog.closeDialog();
				Tools.Log("评论="+response);
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
					Tools.Notic(c, "评论成功", null);
				}
				else//失败 
				{
					Tools.Log("res_Points_error="+error+"");
					Tools.Notic(c, error, null);
				}
				
			}
			
			
		};
	}
	
	 private static class ViewHolder
	    {
		    ImageView iv_head_img ;//头像
		    ImageView iv_zan;//点赞操作
		    ImageView iv_comment;//评论操作
			TextView tv_nickname ;//昵称
			TextView tv_date;//发布的时间
			TextView tv_content ;//发布的内容
			TextView tv_num_look ;//多少人看过
			TextView tv_zan ;//赞数
			TextView tv_comment;//评论数
//			TextView tv_checkallcomment ;//查看所有3条评论
			LinearLayout comment_container;//所有评论内容
//			LinearLayout comment0;//所有评论内容
			LinearLayout commentnew;//所有评论内容
			
			NoScrollGridView gridView;//显示上传图片
			ListView lv_comment;//评论
	    }
	     
	 
}
