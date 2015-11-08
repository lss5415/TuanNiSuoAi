package com.ZYKJ.tuannisuoai.UI;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;

/**
 * @author csh
 * 商品分类产品列表
 */
public class B2_ShopByClassifyActivity extends BaseActivity implements OnEditorActionListener{
	
	private ImageButton back,buy;
	private LinearLayout ll_tab1,ll_tab2,ll_tab3,ll_tab4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_b2_classify_product);
		
		String gc_id = getIntent().getStringExtra("gc_id");
		initView();
		requestData(gc_id);
	}
	
	/**
	 * 初始化页面
	 */
	private void initView(){
		back = (ImageButton)findViewById(R.id.back);
		buy = (ImageButton)findViewById(R.id.buy);
		ll_tab1 = (LinearLayout)findViewById(R.id.ll_tab1);
		ll_tab2 = (LinearLayout)findViewById(R.id.ll_tab2);
		ll_tab3 = (LinearLayout)findViewById(R.id.ll_tab3);
		ll_tab4 = (LinearLayout)findViewById(R.id.ll_tab4);

		back.setOnClickListener(this);
		buy.setOnClickListener(this);
		ll_tab1.setOnClickListener(this);
		ll_tab2.setOnClickListener(this);
		ll_tab3.setOnClickListener(this);
		ll_tab4.setOnClickListener(this);
	}
	
	/**
	 * 请求服务器数据----产品列表(默认排序)
	 */
	private void requestData(String gc_id){
		//HttpUtils.getAddress(res, key);
	}

	@Override
	public void onClick(View view) {
        switch (view.getId()){
        case R.id.ll_tab1:
            break;
        case R.id.ll_tab2:
            break;
        case R.id.ll_tab3:
            break;
        case R.id.ll_tab4:
            break;
        case R.id.back:
            break;
        case R.id.buy:
            break;
        default:
        	break;
        }
	}

	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		switch(actionId){
		case EditorInfo.IME_ACTION_SEARCH:
			Toast.makeText(B2_ShopByClassifyActivity.this, "搜索", Toast.LENGTH_LONG).show();
		}
		return true;
	}
	
}