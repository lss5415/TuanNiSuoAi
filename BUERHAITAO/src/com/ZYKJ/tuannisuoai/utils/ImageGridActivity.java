package com.ZYKJ.tuannisuoai.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.UI.PublishedActivity;
import com.ZYKJ.tuannisuoai.adapter.ImageGridAdapter;
import com.ZYKJ.tuannisuoai.adapter.ImageGridAdapter.TextCallback;
import com.ZYKJ.tuannisuoai.base.BaseActivity;
/**
 * 图片选择页面
 * @author zyk
 *
 */
public class ImageGridActivity extends BaseActivity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	// ArrayList<Entity> dataList;//鐢ㄦ潵瑁呰浇鏁版嵁婧愮殑鍒楄〃
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;// 鑷畾涔夌殑閫傞厤鍣�
	AlbumHelper helper;
	Button bt;
	TextView tv_gridcancel;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
//				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", 400).show();
				Toast.makeText(ImageGridActivity.this, "您已经选择了9张图片", 400).show();
//				Intent intent = new Intent(ImageGridActivity.this,PublishedActivity.class);
//				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.act_bool=true;
		setContentView(R.layout.activity_image_grid);
		tv_gridcancel=(TextView) findViewById(R.id.tv_gridcancel);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		setListener(tv_gridcancel,bt);
//		bt.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				ArrayList<String> list = new ArrayList<String>();
//				Collection<String> c = adapter.map.values();
//				Iterator<String> it = c.iterator();
//				for (; it.hasNext();) {
//					list.add(it.next());
//				}
//
//				if (Bimp.act_bool) {
//					Intent intent = new Intent(ImageGridActivity.this,
//							PublishedActivity.class);
//					startActivity(intent);
//					Bimp.act_bool = false;
//				}
//				for (int i = 0; i < list.size(); i++) {
//					if (Bimp.drr.size() < 9) {
//						Bimp.drr.add(list.get(i));
//					}
//				}
//				finish();
//			}
//
//		});
	}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.tv_gridcancel:
		this.finish();
		break;
	case R.id.bt://点击“完成”
		ArrayList<String> list = new ArrayList<String>();
		Collection<String> c = adapter.map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			list.add(it.next());
		}

		for (int i = 0; i < list.size(); i++) {
			if (Bimp.drr.size() < 9) {
				Bimp.drr.add(list.get(i));
			}
		}
		if (Bimp.act_bool) {
			Intent intent = new Intent(ImageGridActivity.this,PublishedActivity.class);//跳转到发送页面
			startActivity(intent);
			Bimp.act_bool = false;
		}
		finish();
		break;
	default:
		break;
	}
	super.onClick(v);
}
	/**
	 * 鍒濆鍖杤iew瑙嗗浘
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				adapter.notifyDataSetChanged();
			}

		});

	}
}
