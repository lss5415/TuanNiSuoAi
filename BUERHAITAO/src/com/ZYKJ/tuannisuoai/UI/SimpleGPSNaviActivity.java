package com.ZYKJ.tuannisuoai.UI;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View.OnClickListener;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;

/**
 * 
 * 实时导航界面
 * */
public class SimpleGPSNaviActivity extends BaseActivity implements OnClickListener,
		AMapNaviListener {
	//实时导航按钮
//	private Button mStartNaviButton;

	//起点终点
	private NaviLatLng mNaviStart;
	private NaviLatLng mNaviEnd;
    //起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	String endlng,endlat;
	
//	private ProgressDialog mRouteCalculatorProgressDialog;// 路径规划过程显示状态

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simplegps_navi);
		Double lng = Double.parseDouble(getSharedPreferenceValue("lng"));
		Double lat = Double.parseDouble(getSharedPreferenceValue("lat"));
		mNaviStart = new NaviLatLng(lat, lng);
		Double endlat = Double.parseDouble(getIntent().getStringExtra("endlat"));
		Double endlng = Double.parseDouble(getIntent().getStringExtra("endlng"));
		mNaviEnd = new NaviLatLng(endlat,endlng);
		//语音播报开始
		initView();
		 
	}

	private void initView() {
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);
		
//		mStartNaviButton = (Button) findViewById(R.id.gps_start_navi_button);
//		mStartNaviButton.setOnClickListener(this);
		AMapNavi.getInstance(this).calculateDriveRoute(mStartPoints, mEndPoints, null, AMapNavi.DrivingDefault);
//		mRouteCalculatorProgressDialog=new ProgressDialog(this);
//		mRouteCalculatorProgressDialog.setCancelable(true);
		AMapNavi mapNavi = AMapNavi.getInstance(this);
		mapNavi.setAMapNaviListener(this);
	}
//---------------------导航回调--------------------
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
//		mRouteCalculatorProgressDialog.dismiss();

	}

	@Override
	public void onCalculateRouteSuccess() {
//		mRouteCalculatorProgressDialog.dismiss();
		Intent intent = new Intent(SimpleGPSNaviActivity.this, SimpleNaviActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		Bundle bundle=new Bundle();
		bundle.putInt("activityindex", 2);
		bundle.putBoolean("isemulator", false);
		intent.putExtras(bundle);
		startActivity(intent);
        finish();
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	//返回键处理事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(SimpleGPSNaviActivity.this,
					BX_DianPuXiangQingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		 
			finish();
		}
		return super.onKeyDown(keyCode, event);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy(); 
	  // 删除导航监听
		 
		AMapNavi.getInstance(this).removeAMapNaviListener(this);
	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		  
		// TODO Auto-generated method stub  
		
	}
	
}
