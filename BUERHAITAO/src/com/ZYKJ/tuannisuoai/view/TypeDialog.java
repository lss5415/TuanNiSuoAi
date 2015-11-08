package com.ZYKJ.tuannisuoai.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.adapter.GridTypeAdapter;
import com.ZYKJ.tuannisuoai.base.BaseApp;

/**
 * 类型弹出框
 * 
 * @author bin
 * 
 */
public class TypeDialog extends Dialog {

	public static TypeDialog m_dialog;

	// 所有类型
	public static final int[] type_drawable = new int[] {
			R.drawable.type_dialog_1, R.drawable.type_dialog_2,
			R.drawable.type_dialog_3, R.drawable.type_dialog_4,
			R.drawable.type_dialog_5, R.drawable.type_dialog_6,
			R.drawable.type_dialog_7, R.drawable.type_dialog_8,
			R.drawable.type_dialog_9, R.drawable.type_dialog_10,
			R.drawable.type_dialog_11, R.drawable.type_dialog_12,
			R.drawable.type_dialog_13, R.drawable.type_dialog_14,
			R.drawable.type_dialog_15 };

	// 所有名称
	public static final int[] type_name = new int[] { 
		    R.string.dialog_type_1,
			R.string.dialog_type_2, R.string.dialog_type_3,
			R.string.dialog_type_4, R.string.dialog_type_5,
			R.string.dialog_type_6, R.string.dialog_type_7,
			R.string.dialog_type_8, R.string.dialog_type_9,
			R.string.dialog_type_10, R.string.dialog_type_11,
			R.string.dialog_type_12, R.string.dialog_type_13,
			R.string.dialog_type_14, R.string.dialog_type_15 
			};

	public TypeDialog(Context context, OnItemClickListener listener) {
		super(context, R.style.TypeDialog);

		this.setContentView(R.layout.dialog_type);

		setCanceledOnTouchOutside(true);

		LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_type_bg);

		WindowManager windowManager = BaseApp.getInstance().currentActivity()
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHight = metrics.heightPixels;

		LayoutParams params = (LayoutParams) layout.getLayoutParams();
		params.width = screenWidth / 7 * 6;
		params.height = screenHight / 7 * 4;

		// 设置gridView内容的显示
		GridView gridView = (GridView) findViewById(R.id.dialog_type_gridview);

		List<Map<String, Object>> m_grid_items = new ArrayList<Map<String, Object>>();
		Map<String, Object> temp_map;
		for (int i = 0; i < type_drawable.length; i++) {
			temp_map = new HashMap<String, Object>();
			temp_map.put("image", type_drawable[i]);
			temp_map.put("title", type_name[i]);
			m_grid_items.add(temp_map);
		}

		// SimpleAdapter adapter = new SimpleAdapter(context, m_grid_items,
		// R.layout.gridview_type_item,
		// new String[]{"image","title"}, new
		// int[]{R.id.gridview_type_item_rd,R.id.gridview_type_item_rd});
		// gridView.setAdapter(adapter);

		GridTypeAdapter adapter = new GridTypeAdapter(context, m_grid_items);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(listener);
	}

	public TypeDialog(Context context, String msg) {
		super(context, R.style.RequestDialog);

	}

	public static void showDialog(Context context, OnItemClickListener listener) {
		m_dialog = new TypeDialog(context, listener);
		m_dialog.show();
	}

	/** 关闭dialog框 */
	public static void closeDialog() {
		if (m_dialog == null) {
			return;
		}
		m_dialog.dismiss();
		m_dialog = null;
	}

}
