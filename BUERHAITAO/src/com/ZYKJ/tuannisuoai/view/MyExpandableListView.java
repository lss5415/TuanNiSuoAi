package com.ZYKJ.tuannisuoai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.ZYKJ.tuannisuoai.R;

public class MyExpandableListView extends ExpandableListView {
	
	private Context context;

	public MyExpandableListView(Context context) {
		super(context);
		this.context = context;
	}

	public MyExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if (context != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				// 释放焦点
				for (int i = 0; i < getChildCount(); i++) {
					View view = getChildAt(i);
					EditText editText1 = (EditText) view.findViewById(R.id.et_liuyan);
					editText1.clearFocus();
				}
			}
		}
		return super.dispatchKeyEventPreIme(event);
	}
}
