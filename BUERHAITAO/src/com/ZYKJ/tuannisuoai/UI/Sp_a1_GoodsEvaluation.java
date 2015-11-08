package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.Sp_a1_EvalutionAdapter;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.utils.HttpUtils;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.ZYKJ.tuannisuoai.view.MyListView;
import com.ZYKJ.tuannisuoai.view.RequestDailog;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @author lss 2015年7月3日 商品评论
 * 
 */
public class Sp_a1_GoodsEvaluation extends BaseActivity implements IXListViewListener{
	//评论列表
	private MyListView list_sp_a1_pinglun;
	//返回按钮
	private ImageView im_sp_a1_back;
	//列表Adapter
	private Sp_a1_EvalutionAdapter evalutionadapter;
	//列表数据
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	//商品ID
	private String goods_id;
	private TextView tv_sp_a1_title;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_sp_a1_goodsevaluation);
		initView();
		HttpUtils.getGoodsEvaluation(res_goodsevaluation, goods_id);
//		HttpUtils.getGoodsEvaluation(res_goodsevaluation, "100024");
	}

	private void initView() { 
		//获取商品ID
		goods_id = getIntent().getStringExtra("goods_id");
		im_sp_a1_back = (ImageView) findViewById(R.id.im_sp_a1_back);
		tv_sp_a1_title = (TextView)findViewById(R.id.tv_sp_a1_title);
		tv_sp_a1_title.setText(getIntent().getStringExtra("sp_a1_title"));
		list_sp_a1_pinglun = (MyListView) findViewById(R.id.list_sp_a1_pinglun);
		evalutionadapter = new Sp_a1_EvalutionAdapter(Sp_a1_GoodsEvaluation.this,data);
		list_sp_a1_pinglun.setAdapter(evalutionadapter);
		list_sp_a1_pinglun.setPullLoadEnable(true);
		list_sp_a1_pinglun.setPullRefreshEnable(true);
		list_sp_a1_pinglun.setXListViewListener(this, 0);
		list_sp_a1_pinglun.setRefreshTime();
		setListener(im_sp_a1_back);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_sp_a1_back:
				Sp_a1_GoodsEvaluation.this.finish();
			break;
		default:
			break;
		}

	}

	JsonHttpResponseHandler res_goodsevaluation = new JsonHttpResponseHandler()
	{
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			RequestDailog.closeDialog();
			Tools.Log("res_getAddress="+response);
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
					//评论列表
					org.json.JSONArray array = datas.getJSONArray("evaluate_list");
					data.clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonItem = array.getJSONObject(i);
						Map<String, Object> map = new HashMap();
						map.put("geval_scores",jsonItem.getString("geval_scores"));
						map.put("geval_content", jsonItem.getString("geval_content"));
						map.put("geval_addtime", jsonItem.getString("geval_addtime"));
						map.put("geval_frommembername",jsonItem.getString("geval_frommembername"));
						map.put("geval_image",jsonItem.getString("geval_image"));
						map.put("geval_spec",jsonItem.getString("geval_spec"));
						map.put("geval_avatar",jsonItem.getString("geval_avatar"));
						data.add(map);
					}
					evalutionadapter.notifyDataSetChanged();
				} 
				catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}/*circle*/
			else//失败 
			{
				Tools.Log("res_Points_error="+error+"");
//				Tools.Notic(B5_MyActivity.this, error+"", null);
			}
			
		}
		
	};
	
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		RequestDailog.showDialog(this, "正在加载数据，请稍后");
		HttpUtils.getGoodsEvaluation(res_goodsevaluation, goods_id);		
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		
	}
}
