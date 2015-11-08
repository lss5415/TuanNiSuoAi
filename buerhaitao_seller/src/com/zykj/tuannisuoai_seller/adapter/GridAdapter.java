package com.zykj.tuannisuoai_seller.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.zykj.tuannisuoai_seller.R;
import com.zykj.tuannisuoai_seller.XingHaoModel;

public class GridAdapter extends BaseAdapter{
	private Activity c;
	List<XingHaoModel> data = new ArrayList<XingHaoModel>();
	SaveModel savemodel;
    
	public GridAdapter(Activity c, List<XingHaoModel> data,SaveModel savemodel) {
		this.c = c;
		this.data = data;
		this.savemodel = savemodel;
	}@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(c);
            convertView = mInflater.inflate(R.layout.ui_griditems, null);
            viewHolder.tv_guige = (EditText) convertView.findViewById(R.id.et_guige);
            viewHolder.tv_guige1 = (EditText) convertView.findViewById(R.id.et_guige1);
            viewHolder.et_jiage = (EditText) convertView.findViewById(R.id.et_jiage);
            viewHolder.et_kucun = (EditText) convertView.findViewById(R.id.et_kucun);
            viewHolder.et_tejia = (EditText) convertView.findViewById(R.id.et_tejia);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_guige.setText(data.get(position).getXinghao1());
        viewHolder.tv_guige1.setText(data.get(position).getXinghao2());
        
        viewHolder.et_jiage.setText(data.get(position).getJiage());
        viewHolder.et_kucun.setText(data.get(position).getKucun());
        viewHolder.et_tejia.setText(data.get(position).getTejia());
        
        viewHolder.et_jiage.addTextChangedListener(new TextWatcherListner(viewHolder,position));
        viewHolder.et_kucun.addTextChangedListener(new TextWatcherListner1(viewHolder,position));
        viewHolder.et_tejia.addTextChangedListener(new TextWatcherListner2(viewHolder,position));
       
        
		return convertView;
	}
	
	class TextWatcherListner implements TextWatcher {
		ViewHolder viewHolder;
		int position;

		public TextWatcherListner(ViewHolder viewHolder,int position) {
			this.viewHolder = viewHolder;
			this.position = position;
		}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			data.get(position).setJiage(viewHolder.et_jiage.getText().toString());
			savemodel.SaveMod(data);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
		
	}
	
	class TextWatcherListner1 implements TextWatcher {
		ViewHolder viewHolder;
		int position;

		public TextWatcherListner1(ViewHolder viewHolder,int position) {
			this.viewHolder = viewHolder;
			this.position = position;
		}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			data.get(position).setKucun(viewHolder.et_kucun.getText().toString());
			savemodel.SaveMod(data);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
	}
	
	class TextWatcherListner2 implements TextWatcher {
		ViewHolder viewHolder;
		int position;

		public TextWatcherListner2(ViewHolder viewHolder,int position) {
			this.viewHolder = viewHolder;
			this.position = position;
		}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			data.get(position).setTejia(viewHolder.et_tejia.getText().toString());
			savemodel.SaveMod(data);
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
	}

	private static class ViewHolder
    {
		EditText tv_guige;
		EditText tv_guige1;
		EditText et_jiage;
		EditText et_kucun;
		EditText et_tejia;
    }
	
	//用于判断结算的条数
	public interface SaveModel{
		void SaveMod(List<XingHaoModel> data);
	}
}