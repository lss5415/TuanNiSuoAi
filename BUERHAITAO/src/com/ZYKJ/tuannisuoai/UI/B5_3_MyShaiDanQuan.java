package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.B5_3_MyShaiDanQuanAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.CircularImage;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 晒单圈 （我的）
 * @author zyk
 *
 */
public class B5_3_MyShaiDanQuan extends BaseActivity implements IXListViewListener{

	private ImageButton myshaidanquan_back;
	private Button btn_publish;
	private CircularImage img_head_myshaidanquan;
	private TextView tv_nickname_myshaidanquan;
	private LinearLayout ll_mypublish,ll_mycomment;
	private View v1,v2;
	private MyListView lv_shaidanquan_mypublish;
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	B5_3_MyShaiDanQuanAdapter adapter;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		String headImgString=getSharedPreferenceValue("headImg_filename");
		String username=getSharedPreferenceValue("username");
//		if (headImgString!=null) //如果登录过，显示之前本地的头像
//		{
////			File f = new File(headImgString); 
//		    Bitmap bitmap_head=BitmapFactory.decodeFile(headImgString);
//		    img_head_myshaidanquan.setImageBitmap(bitmap_head);
//		}
		ImageLoader.getInstance().displayImage(getSharedPreferenceValue("avatar"), img_head_myshaidanquan, ImageOptions.getOpstion(), animateFirstListener);

		if (username!=null) {
			tv_nickname_myshaidanquan.setText(username);
		}
		super.onResume();
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b5_3_myshaidanquan);
		initView();
		setListener(myshaidanquan_back,btn_publish,ll_mypublish,ll_mycomment);
		lv_shaidanquan_mypublish = (MyListView) findViewById(R.id.lv_myshaidanquan);
		adapter = new B5_3_MyShaiDanQuanAdapter(B5_3_MyShaiDanQuan.this,data,getSharedPreferenceValue("key"));
		lv_shaidanquan_mypublish.setAdapter(adapter);
		lv_shaidanquan_mypublish.setPullLoadEnable(true);
		lv_shaidanquan_mypublish.setPullRefreshEnable(true);
		lv_shaidanquan_mypublish.setXListViewListener(this, 0);
		lv_shaidanquan_mypublish.setRefreshTime();
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.shaidanquan_mypublish(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
	}
	
	private void initView(){
		myshaidanquan_back=(ImageButton) findViewById(R.id.myshaidanquan_back);
		btn_publish=(Button)findViewById(R.id.btn_publish);
		img_head_myshaidanquan=(CircularImage) findViewById(R.id.img_head_myshaidanquan);
		tv_nickname_myshaidanquan=(TextView) findViewById(R.id.tv_nickname_myshaidanquan);
		ll_mypublish=(LinearLayout) findViewById(R.id.ll_mypublish);
		ll_mycomment=(LinearLayout) findViewById(R.id.ll_mycomment);
		v1=findViewById(R.id.v1);
		v2=findViewById(R.id.v2);
		lv_shaidanquan_mypublish=(MyListView) findViewById(R.id.lv_myshaidanquan);//我的晒单圈listview
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.myshaidanquan_back://返回
			this.finish();
		break;
		case R.id.btn_publish://发布
			Intent fabuIntent=new Intent(this,TestPicActivity.class);
			startActivity(fabuIntent);
		break;
			
		case R.id.ll_mypublish://我发表的
			v2.setVisibility(View.INVISIBLE);
			v1.setVisibility(View.VISIBLE);
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.shaidanquan_mypublish(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
//			Toast.makeText(this, "我发表的",  Toast.LENGTH_LONG).show();
		break;
		case R.id.ll_mycomment://我评论的
			v2.setVisibility(View.VISIBLE);
			v1.setVisibility(View.INVISIBLE);
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.shaidanquan_myquote(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
//			Toast.makeText(this, "我评论的",  Toast.LENGTH_LONG).show();
		break;
		}
	}
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		if (v1.getVisibility()==0) //请求“我发表的”
		{
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.shaidanquan_mypublish(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
		}else {
//			Toast.makeText(this, "我评论的", 500).show();
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.shaidanquan_myquote(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
		}
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		if (v1.getVisibility()==0) //请求“我发表的”
		{
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.shaidanquan_mypublish(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
		}else {
//			Toast.makeText(this, "我评论的", 500).show();
			RequestDailog.showDialog(this, "正在加载数据，请稍后");
			HttpUtils.shaidanquan_myquote(res_shaidanquan_mypublish, getSharedPreferenceValue("key"));
		}
	}
	/**
	 * 晒单圈，我的发布
	 */
	JsonHttpResponseHandler res_shaidanquan_mypublish = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("晒单圈="+response);
			String error=null;
			JSONObject datas=null;
			try {
				 datas = response.getJSONObject("datas");
				 error = response.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (error==null)//成功
			{
				try {
					data.clear();
					org.json.JSONArray array = datas.getJSONArray("circle_list");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, Object> map = new HashMap();
						map.put("addtime", jsonItem.getString("addtime"));
						map.put("replys", jsonItem.getString("replys"));
						map.put("circle_id", jsonItem.getString("circle_id"));
						map.put("views", jsonItem.getString("views"));
						map.put("description", jsonItem.getString("description"));
						map.put("image", jsonItem.getJSONObject("image"));
						Tools.Log("image="+jsonItem.getString("image"));
						map.put("praise", jsonItem.getString("praise"));
						map.put("avatar", jsonItem.getString("avatar"));
						map.put("member_name", jsonItem.getString("member_name"));
						map.put("member_id", jsonItem.getString("member_id"));
						map.put("quote", jsonItem.getJSONArray("quote"));
						data.add(map);
					}
					adapter.notifyDataSetChanged();
				} 
				catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
		
		
	};
}
