package com.zykj.tuannisuoai_seller;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.umeng.message.PushAgent;
import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
import com.zykj.tuannisuoai_seller.data.AppValue;
import com.zykj.tuannisuoai_seller.utils.Tools;


public class A0_Welcome extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_welcome);
		//消息推送
		PushAgent mPushAgent = PushAgent.getInstance(A0_Welcome.this);
		mPushAgent.enable();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				// Intent intent = new Intent(Welcome.this, MainActivity.class);
				// startActivity(intent);

				String is_intro = getSharedPreferenceValue(AppValue.IS_INTRO);
				boolean should_intro = false;
				int version = Tools.getAppVersion(A0_Welcome.this);
				String save_version = getSharedPreferenceValue(AppValue.VERSION);
				int save_version_int = save_version.equals("") ? -1 : Integer
						.parseInt(save_version);

				if (is_intro.length() > 0 && version == save_version_int) {// 已经进行过指引,且版本号符合
					should_intro = false;
				} else {
					should_intro = true;
				}

//				if (should_intro) {
//
////					Toast.makeText(getApplicationContext(), "11111111", Toast.LENGTH_LONG).show();
//					Intent intent = new Intent(A0_Welcome.this,A1_IntroActivity.class);
//					startActivity(intent);
//				} else {
//					Toast.makeText(getApplicationContext(), "222222222", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(A0_Welcome.this, B0_MainActivity.class);
					startActivity(intent);
//				}
				finish();

			}
		};
		timer.schedule(task, 2000);
	}
}
