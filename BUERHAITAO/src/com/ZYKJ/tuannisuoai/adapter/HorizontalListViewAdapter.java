package com.ZYKJ.tuannisuoai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZYKJ.tuannisuoai.R;
import com.ZYKJ.tuannisuoai.utils.AnimateFirstDisplayListener;
import com.ZYKJ.tuannisuoai.utils.ImageOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class HorizontalListViewAdapter extends BaseAdapter {

	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Activity context;
    Bitmap iconBitmap;  
//    private int selectIndex = -1;  
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
  
    public HorizontalListViewAdapter(Activity context, List<Map<String, String>> data){  
        this.context = context;  
        this.data = data;
        Log.e("data", data+"");
    }  
    @Override   
    public int getCount() {  
    	return data == null ? 0 : data.size();
    }  
    @Override  
    public Object getItem(int position) {  
        return position;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        ViewHolder holder;  
        if(convertView==null){  
            holder = new ViewHolder();  
            convertView=LinearLayout.inflate(context, R.layout.horizontal_list_item, null);
            holder.iv_avatar=(ImageView)convertView.findViewById(R.id.iv_avatar);  
            holder.iv_image=(ImageView)convertView.findViewById(R.id.iv_image);  
            holder.tv_nickname=(TextView)convertView.findViewById(R.id.tv_nickname);  
            holder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);  
            holder.tv_zannumber=(TextView)convertView.findViewById(R.id.tv_zannumber);  
            holder.tv_commentnumber=(TextView)convertView.findViewById(R.id.tv_commentnumber);  
            convertView.setTag(holder);  
        }else{  
            holder=(ViewHolder)convertView.getTag();  
        }  
        JSONObject jsonObject = ((JSONObject)JSON.parse(data.get(position).get("image")));
        String urlString;
		urlString = jsonObject.getString("0");
		Log.e("urlString", urlString);
		ImageLoader.getInstance().displayImage(urlString, holder.iv_image, ImageOptions.getOpstion(), animateFirstListener);
        ImageLoader.getInstance().displayImage(data.get(position).get("avatar"), holder.iv_avatar, ImageOptions.getOpstion(), animateFirstListener);
        holder.tv_nickname.setText(data.get(position).get("member_name"));
        holder.tv_content.setText(data.get(position).get("description"));
        holder.tv_zannumber.setText("("+data.get(position).get("praise")+")");
        holder.tv_commentnumber.setText("("+data.get(position).get("replys")+")");
  
        return convertView;  
    }  
  
    private static class ViewHolder{  
    	public ImageView iv_avatar;  
    	public ImageView iv_image;  
    	public TextView tv_nickname ;  
    	public TextView tv_content ;  
    	public TextView tv_zannumber ;  
    	public TextView tv_commentnumber ;  
    }  

}
