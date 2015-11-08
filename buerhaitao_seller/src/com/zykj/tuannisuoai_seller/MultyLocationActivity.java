package com.zykj.tuannisuoai_seller;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.zykj.tuannisuoai_seller.base.BaseActivity;
/** 
 * 类说明 
 * @author zyk 
 * @version 创建时间：2015-7-27 下午6:03:44 
 */
public class MultyLocationActivity extends BaseActivity implements LocationSource, AMapLocationListener {

	private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
	Double geoLat;//经度
    Double geoLng;//纬度
    String addressByGPS;//定位地址
    Button btn_confirm_location;
    
    private static final int GetLocation=2;//定位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multy_location);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        setListener(btn_confirm_location);
    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        btn_confirm_location = (Button) findViewById(R.id.btn_confirm_location);
    }
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }
    
    /**
     * 此方法需存在
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
 
    /**
     * 此方法需存在
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }
 
    /**
     * 此方法需存在
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
 
 
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (mListener != null && amapLocation != null) {
            if (amapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                geoLat = amapLocation.getLatitude();
	            geoLng = amapLocation.getLongitude();  
	            addressByGPS =amapLocation.getAddress();
            }
        }
	}
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		 mListener = listener;
	        if (mAMapLocationManager == null) {
	            mAMapLocationManager = LocationManagerProxy.getInstance(this);
	            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
	            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
	            //在定位结束后，在合适的生命周期调用destroy()方法     
	            //其中如果间隔时间为-1，则定位只定一次
	            mAMapLocationManager.requestLocationData(
	                    LocationProviderProxy.AMapNetwork, -1, 10, this);
	        }
	}
	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		 mListener = null;
	        if (mAMapLocationManager != null) {
	            mAMapLocationManager.removeUpdates(this);
	            mAMapLocationManager.destroy();
	        }
	        mAMapLocationManager = null;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm_location:
			Intent intent =getIntent();
			intent.putExtra("geoLat", geoLat);
			intent.putExtra("geoLng", geoLng);
			intent.putExtra("addressByGPS", addressByGPS);
			setResult(GetLocation, intent);
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;

		default:
			break;
		}
		super.onClick(v);
		
	}
}
