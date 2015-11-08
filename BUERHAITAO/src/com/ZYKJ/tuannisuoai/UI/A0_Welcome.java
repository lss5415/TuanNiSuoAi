package com.ZYKJ.tuannisuoai.UI;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.ZYKJ.tuannisuoai.data.AppValue;
import com.ZYKJ.tuannisuoai.utils.Tools;
import com.umeng.message.PushAgent;


public class A0_Welcome extends BaseActivity {
//	public LocationClient mLocationClient = null;
//	public BDLocationListener mLocationListener = new MyLocationListener();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_welcome);
//		initLocation();
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

				if (should_intro) {

//					Toast.makeText(getApplicationContext(), "11111111", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(A0_Welcome.this,A1_IntroActivity.class);
					startActivity(intent);
				} else {
//					Toast.makeText(getApplicationContext(), "222222222", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(A0_Welcome.this, B0_MainActivity.class);
					startActivity(intent);
				}
				finish();

			}
		};
		timer.schedule(task, 2000);
	}
//	private void initLocation() {
//		mLocationClient = new LocationClient(this);
//		mLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mLocationListener);
//		LocationClientOption option = new LocationClientOption();
//		option.setCoorType("bd09ll");
//		option.setIsNeedAddress(true);
//		option.setOpenGps(true);
//		option.setScanSpan(1000);
//		
//		mLocationClient.setLocOption(option);
//	}
//	private class MyLocationListener implements BDLocationListener {
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			location.getLatitude();
//			location.getLongitude();
//			Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
//			}
//	}	
	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		//开启定位
//		if(!mLocationClient.isStarted())
//			mLocationClient.start();
//	}
}
