package com.zykj.tuannisuoai_seller.utils;



import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zykj.tuannisuoai_seller.R;

/**
 * 一些ui中Dialog中的使用
 * 
 * @author bin
 * 
 */
public class UIDialog {
	public static AlertDialog dialog;

	/** 3按键按钮dialog */
	public static void ForThreeBtn(Context context, String[] showtxt,
			OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_modif);

		Button m_btn_1 = (Button) window.findViewById(R.id.dialog_modif_1);
		Button m_btn_2 = (Button) window.findViewById(R.id.dialog_modif_2);
		Button m_btn_3 = (Button) window.findViewById(R.id.dialog_modif_3);

		m_btn_1.setText(showtxt[0]);
		m_btn_2.setText(showtxt[1]);
		m_btn_3.setText(showtxt[2]);

		m_btn_1.setOnClickListener(lisener);
		m_btn_2.setOnClickListener(lisener);
		m_btn_3.setOnClickListener(lisener);
	}
	/** 5按键按钮dialog */
	public static void ForFiveBtn(Context context, String[] showtxt,
			OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_five_modif);

		Button m_btn_1x = (Button) window.findViewById(R.id.dialog_five_modif_1);
		Button m_btn_2x = (Button) window.findViewById(R.id.dialog_five_modif_2);
		Button m_btn_3x = (Button) window.findViewById(R.id.dialog_five_modif_3);
		Button m_btn_4x = (Button) window.findViewById(R.id.dialog_five_modif_4);
		Button m_btn_5x = (Button) window.findViewById(R.id.dialog_five_modif_5);

		m_btn_1x.setText(showtxt[0]);
		m_btn_2x.setText(showtxt[1]);
		m_btn_3x.setText(showtxt[2]);
		m_btn_4x.setText(showtxt[3]);
		m_btn_5x.setText(showtxt[4]);

		m_btn_1x.setOnClickListener(lisener);
		m_btn_2x.setOnClickListener(lisener);
		m_btn_3x.setOnClickListener(lisener);
		m_btn_4x.setOnClickListener(lisener);
		m_btn_5x.setOnClickListener(lisener);
	}
	/** 2按键按钮dialog */
	public static void ForTwoBtn(Context context, String[] showtxt,
			OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_modif_two);
		
		Button m_btn_1 = (Button) window.findViewById(R.id.dialog_modif_1);
		Button m_btn_2 = (Button) window.findViewById(R.id.dialog_modif_2);
		
		m_btn_1.setText(showtxt[0]);
		m_btn_2.setText(showtxt[1]);
		
		m_btn_1.setOnClickListener(lisener);
		m_btn_2.setOnClickListener(lisener);
	}

	/** notic提示dialog */
	public static void ForNotic(Context context, String text,
			OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_notic);

		Button m_btn_1 = (Button) window.findViewById(R.id.dialog_notic_1);
		if (lisener == null) {
			lisener = new OnClickListener() {
				public void onClick(View v) {
					UIDialog.closeDialog();
				}
			};
		}
		m_btn_1.setOnClickListener(lisener);
		// 设置内容
		TextView m_notic_content = (TextView) window
				.findViewById(R.id.dialog_notic_text);
		m_notic_content.setText(text);
	}
//	/** 展示图片 */
//	public static void ForShowPhoto(Context context, String path) {
//		dialog = new AlertDialog.Builder(context).create();
//		dialog.show();
//		Window window = dialog.getWindow();
//		// *** 主要就是在这里实现这种效果的.
//		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
//		window.setContentView(R.layout.dialog_showphoto);
//		// 设置内容
//		ImageView iv_photo = (ImageView) window.findViewById(R.id.iv_photo);
//		ImageLoader.getInstance().displayImage(path, iv_photo);
//	}
	
	/**
	 * 关闭dialog
	 */
	public static void closeDialog() {
		if (dialog == null || !dialog.isShowing()) {
			return;
		}
		dialog.dismiss();

	}
}
