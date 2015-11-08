package com.zykj.tuannisuoai_seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.utils.HttpUtils;
import com.zykj.tuannisuoai_seller.utils.ImageOptions;
/**
 * @author lss 2015年7月28日
 *
 */

public class B2_FenLeiActivity extends BaseActivity {
	private ImageButton fenlei_back;
	private RadioGroup category_list;
	private GridView product_grid;
    private RadioGroup.LayoutParams mRadioParams;
    private List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private String key;
//    private String fenleiname;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_b2_classify);
		initview();
		requestData();
	}
	
	public void initview(){
		fenlei_back = (ImageButton)findViewById(R.id.fenlei_back);
		category_list = (RadioGroup)findViewById(R.id.category_list);
		product_grid = (GridView)findViewById(R.id.product_grid);
        mRadioParams = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		key = getSharedPreferenceValue("key");
		setListener(fenlei_back);
	}
	
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
			case R.id.fenlei_back:
				B2_FenLeiActivity.this.finish();
				break;
			default:
				break;
			
		}
	}
	
	/**
	 * 请求服务器数据----一级分类
	 */
	private void requestData(){
		HttpUtils.getCanUserList(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					int code = response.getInt("code");
					if(code != 200){
						Toast.makeText(B2_FenLeiActivity.this, "请求失败", Toast.LENGTH_LONG).show();
						return;
					}
					JSONObject data = response.getJSONObject("datas");
					JSONArray jsonArray = data.getJSONArray("class_list");
	                for (int i = 0; i < jsonArray.length(); i++) {
	                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
	                    RadioButton radioButton = new RadioButton(B2_FenLeiActivity.this);
	                    radioButton.setId(Integer.valueOf(jsonObject.getString("gc_id")));
	                    radioButton.setText(jsonObject.getString("gc_name"));
	                    radioButton.setTextSize(18f);
	                    radioButton.setPadding(20, 35, 20, 35);
	                    radioButton.setTextColor(getResources().getColorStateList(R.drawable.classify_text));
	                    radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
	                    radioButton.setBackgroundResource(R.drawable.checked_classify);
	                    radioButton.setChecked(i==0?true:false);
	                    category_list.addView(radioButton,mRadioParams);
	                    if(i==0){
//	                    	fenleiname = jsonObject.getString("gc_name");
		                    getTwoCateByOne(jsonObject.getString("gc_id"));
	                    }
	                }
	                category_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	                    @Override
	                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
	                        //oneid = String.valueOf(checkedId);
	                        getTwoCateByOne(String.valueOf(checkedId));
	                    }
	                });
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		},key,"0","1");
	}
	
	/**
	 * 请求服务器数据----二级分类
	 */
	private void getTwoCateByOne(String gc_id){
		HttpUtils.getCanUserList(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					int code = response.getInt("code");
					list.clear();
					if(code != 200){
						Toast.makeText(B2_FenLeiActivity.this, "请求失败", Toast.LENGTH_LONG).show();
						return;
					}
					JSONObject data = response.getJSONObject("datas");
					JSONArray jsonArray = data.getJSONArray("class_list");
	                for (int i = 0; i < jsonArray.length(); i++) {
	                	HashMap<String,String> map = new HashMap<String,String>();
	                    JSONObject twoClassify = jsonArray.getJSONObject(i);
	                    map.put("gc_id", twoClassify.getString("gc_id"));
	                    map.put("gc_name", twoClassify.getString("gc_name"));
	                    map.put("image", twoClassify.getString("image"));
	                    list.add(map);
	                }
	                product_grid.setAdapter(new BaseAdapter() {
						
						@Override
						public int getCount() { return list.size(); }
						
						@Override
						public HashMap<String,String> getItem(int postion) { return list.get(postion); }
						
						@Override
						public long getItemId(int arg0) { return 0; }
						
						@Override
						public View getView(int postion, View convertView, ViewGroup arg2) {
							convertView = LayoutInflater.from(B2_FenLeiActivity.this).inflate(R.layout.ui_b2_item_classify, null);
							
	                        TextView name= (TextView) convertView.findViewById(R.id.classify_name);
	                        ImageView image= (ImageView) convertView.findViewById(R.id.classify_image);
	                        name.setText(list.get(postion).get("gc_name"));
	                        ImageOptions.displayImage2Circle(image, list.get(postion).get("image"), 10f);
	                        
							return convertView;
						}
					});
	                product_grid.setCacheColorHint(0);
	                
	                product_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
							String gc_id = list.get(position).get("gc_id");
							String gc_name = list.get(position).get("gc_name");
//							Toast.makeText(B2_FenLeiActivity.this, gc_id, Toast.LENGTH_LONG).show();
							Intent intent = new Intent(B2_FenLeiActivity.this,B3_AddActivity.class);
							intent.putExtra("gc_id", gc_id);
							intent.putExtra("gc_name",gc_name);
							B2_FenLeiActivity.this.setResult(Activity.RESULT_OK, intent);
							B2_FenLeiActivity.this.finish();
						}
					});
	                
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		},key,gc_id,"2");
	}


}