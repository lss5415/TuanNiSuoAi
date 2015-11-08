package com.zykj.tuannisuoai_seller.base;

import java.io.File;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;

import com.zykj.tuannisuoai_seller.data.AppValue;
import com.zykj.tuannisuoai_seller.utils.Tools;

public class BaseTabActivity extends TabActivity {
	private SharedPreferences defalut_sp;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		defalut_sp = getSharedPreferences(AppValue.config, Context.MODE_PRIVATE);
	}
	public String getSharedPreferenceValue(String key) {
		String value = defalut_sp.getString(key, "");
		return value;
	}
	public void putSharedPreferenceValue(String key, String value) {
		SharedPreferences.Editor editor;
		editor = defalut_sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 返回按钮
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示")
					.setMessage("您确定退出当前应用")
					.setNegativeButton("取消", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setPositiveButton("确定", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try {
								// 判断是否存在临时创建的文件
								File temp_file = new File(Environment
										.getExternalStorageDirectory()
										+ File.separator
										+ AppValue.FILE_DIR);
								Tools.Log("文件是否存在：" + temp_file.exists());
								if (temp_file.exists()) {
									File[] file_detail = temp_file.listFiles();
									for (File file_del : file_detail) {
										file_del.delete();
									}
									temp_file.delete();
								}

							} catch (Exception e) {

							}
							System.exit(0);
						}
					})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}

							}).show();
		}

		return super.onKeyDown(keyCode, event);
	}

}
