package com.ZYKJ.tuannisuoai.adapter;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.loopj.android.http.JsonHttpResponseHandler;



public class B5_3_MyShaiDanQuanPinglunAdapter extends BaseAdapter {

//	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	JSONArray quote = null;
	private Activity c;
	private LayoutInflater listContainer;
	ViewHolder viewHolder = null;
	String commentString;
	String key,circle_id,reply_replyname,reply_id,member_name;
//	ListView lv_comment;
	GridViewAdatper_myshaidanquan photosadapter;

	public B5_3_MyShaiDanQuanPinglunAdapter(Activity c, JSONArray quote ,String key ,String circle_id) {
		this.c = c;
		this.quote = quote;
		listContainer = LayoutInflater.from(c);
		this.key = key;
		this.circle_id = circle_id;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return quote == null ? 0 : quote.length();
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
            convertView = mInflater.inflate(R.layout.ui_b5_3_myshaidanquanlpinglun_items, null);
            
            TextView tv_member_name;//评论者的昵称
			TextView tv_huifu;//“回复”这两个字
			TextView tv_reply_replyname;//被评论者的昵称
			TextView tv_reply_content;//评论的内容
			
            viewHolder.tv_member_name = (TextView) convertView.findViewById(R.id.tv_member_name);//评论者的昵称
            viewHolder.tv_huifu = (TextView) convertView.findViewById(R.id.tv_huifu);//“回复”这两个字
            viewHolder.tv_reply_replyname = (TextView) convertView.findViewById(R.id.tv_reply_replyname);//被评论者的昵称
            viewHolder.tv_reply_content = (TextView) convertView.findViewById(R.id.tv_reply_content);//评论的内容
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        try {
        	member_name = ((JSONObject)quote.get(position)).getString("member_name");
        	String reply_content = ((JSONObject)quote.get(position)).getString("reply_content");
        	reply_replyname = ((JSONObject)quote.get(position)).getString("reply_replyname");
        	reply_id = ((JSONObject)quote.get(position)).getString("reply_id");
        	if (reply_replyname.equals("")) {//如果是直接回复圈主，则不显示“回复”，被回复的人的昵称
        		viewHolder.tv_huifu.setVisibility(View.GONE);
        		viewHolder.tv_reply_replyname.setVisibility(View.GONE);
			}else {
				viewHolder.tv_reply_replyname.setText(reply_replyname);
			}
        	viewHolder.tv_member_name.setText(member_name);
        	viewHolder.tv_reply_content.setText(reply_content);
        	
        } catch (JSONException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        convertView.setOnClickListener(new CommentIt(position,key,reply_id,member_name));//每一条评论的点击事件
		
        
        return convertView;
	}
	
	class CommentIt implements View.OnClickListener {
		int position;
		String key,member_name,reply_id;
		public CommentIt(int position,String key,String reply_id,String member_name ) {
			this.position = position;
			this.key = key;
			this.reply_id = reply_id;
			this.member_name = member_name;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			final AlertDialog.Builder builder = new AlertDialog.Builder(c);
			final AlertDialog aDialog=builder.create();  
			aDialog.show();
			
//			Window window = aDialog.getWindow();
//			window.setContentView(R.layout.dialog_type_in_comment);
//			aDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//呼出输入法
//			window.setGravity(Gravity.BOTTOM);//设置弹出框位于屏幕底部
//			final EditText mEditText=(EditText) window.findViewById(R.id.et_type_in);
//			Button btn_changeName=(Button) window.findViewById(R.id.btn_changeName);
//			btn_changeName.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					commentString=mEditText.getText().toString().trim();
//					Log.e("key=",key);
//					Log.e("circle_id=",circle_id);
//					Log.e("commentString=",commentString);
//					Log.e("reply_replyid=",reply_replyid);
//					Log.e("reply_replyname=",reply_replyname);
//					HttpUtils.comment(res_comment_it, key, circle_id, commentString, reply_replyid, reply_replyname);
//					aDialog.dismiss();
//				}
//			});
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
					Tools.Log("key="+key);
					Tools.Log("circle_id="+circle_id);
					Tools.Log("commentString="+commentString);
					Tools.Log("reply_id="+reply_id);
					Tools.Log("reply_replyname="+reply_replyname);
					HttpUtils.comment(res_comment_it, key, circle_id, commentString, reply_id, member_name);
					aDialog.dismiss();
				}
			});
			
		}
		 /**
			 * 评论
			 */
			JsonHttpResponseHandler res_comment_it = new JsonHttpResponseHandler()
			{
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					RequestDailog.closeDialog();
					Tools.Log("评论123="+response);
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
			TextView tv_member_name;//评论者的昵称
			TextView tv_huifu;//“回复”这两个字
			TextView tv_reply_replyname;//被评论者的昵称
			TextView tv_reply_content;//评论的内容
	    }

}
