package com.ZYKJ.tuannisuoai.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseTabActivity;

public class B0_MainActivity extends BaseTabActivity {
	public TabHost m_tab;
	private Intent intent_1;
	private Intent intent_2;
	private Intent intent_3;
	private Intent intent_4;
	private Intent intent_5;
	private Intent getTagiIntent;
	
	// 单选按钮组
	private RadioGroup mRadioGroup;
	// 4个单选按钮
	private RadioButton mRadioButton_index;
	private RadioButton mraRadioButton_classify;
	private RadioButton mraRadioButton_shopcar;
	private RadioButton mradRadioButton_store;
	private RadioButton mradRadioButton_my;
	@SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_layout);
        getTagiIntent=getIntent();
        m_tab = getTabHost();
        m_tab.setCurrentTab(0);
        initView();
    }
	private void initView() {
		// 设置圆角边线不启用
		// final TabWidget _widget = m_tab.getTabWidget();
		// _widget.setStripEnabled(false);
		intent_1 = new Intent(this, B1_HomeActivity.class);//相当于IndexActivity
		intent_2 = new Intent(this, B2_ClassifyActivity.class);
		intent_3 = new Intent(this, B3_ShoppingCartActivity.class);
		intent_4 = new Intent(this, B4_StoreActivity.class);
		intent_5 = new Intent(this, B5_MyActivity.class);

		m_tab.addTab(buildTagSpec("test1", 0, intent_1));
		m_tab.addTab(buildTagSpec("test2", 1, intent_2));
		m_tab.addTab(buildTagSpec("test3", 2, intent_3));
		m_tab.addTab(buildTagSpec("test4", 3, intent_4));
		m_tab.addTab(buildTagSpec("test5", 4, intent_5));

		mRadioGroup = (RadioGroup) findViewById(R.id.tab_rgroup);
		mRadioButton_index = (RadioButton) findViewById(R.id.tab_radio1);
		mraRadioButton_classify = (RadioButton) findViewById(R.id.tab_radio2);
		mraRadioButton_shopcar = (RadioButton) findViewById(R.id.tab_radio3);
		mradRadioButton_store = (RadioButton) findViewById(R.id.tab_radio4);
		mradRadioButton_my = (RadioButton) findViewById(R.id.tab_radio5);

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == mRadioButton_index.getId()) {
					m_tab.setCurrentTabByTag("test1");
				} else if (checkedId == mraRadioButton_classify.getId()) {
					m_tab.setCurrentTabByTag("test2");
				} else if (checkedId == mraRadioButton_shopcar.getId()) {
					m_tab.setCurrentTabByTag("test3");
				} else if (checkedId == mradRadioButton_store.getId()) {
					m_tab.setCurrentTabByTag("test4");
				} else if (checkedId == mradRadioButton_my.getId()) {
					m_tab.setCurrentTabByTag("test5");
				}
			}
		});
		

	}
	private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
			Intent content) {
		return m_tab.newTabSpec(tagName).setIndicator(tagLable + "")
				.setContent(content);
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
