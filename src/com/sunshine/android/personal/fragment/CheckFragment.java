package com.sunshine.android.personal.fragment;

import java.util.ArrayList;
import java.util.List;
import com.sunshine.android.personalfinance.R;
import com.sunshine.android.personalfinance.dabase.DBHelper;
import com.sunshine.android.personalfinance.duotai.Count;
import com.sunshine.android.personalfinance.duotai.PictureUtils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckFragment extends Fragment {
	private TextView name;
	private TextView type;
	private TextView money;
	private TextView date;
	private TextView who;
	private TextView beizhu;
	private ImageView mPhotoView;
	private Button check_delect;
	private String dataid;
	private String checkdate;
	private String filename;
	private Cursor cursor;
	private DBHelper database;
	private List<Count> countList;
	private TextView texthead;
	private ImageView topButton;
	private static final String DIALOG_IMAGE = "image";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		database = new DBHelper(getActivity());
		countList = new ArrayList<Count>();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.check_acount, container,false);
		texthead = (TextView) view.findViewById(R.id.topTv);
		name = (TextView) view.findViewById(R.id.checkname);
		money = (TextView) view.findViewById(R.id.checkmoney);
		date = (TextView) view.findViewById(R.id.checkdataView);
		who = (TextView) view.findViewById(R.id.checkcounttype);
		beizhu = (TextView) view.findViewById(R.id.checkbeizhu);
		mPhotoView = (ImageView) view.findViewById(R.id.count_imageView);
		type = (TextView) view.findViewById(R.id.checkcountname);
		//check_delect =(Button) view.findViewById(R.id.check_delect);
		topButton = (ImageView) view.findViewById(R.id.topButton);
		
		
		dataid = getActivity().getIntent().getStringExtra("id");
		String checkname= getActivity().getIntent().getStringExtra("name");
		Double checkmoney = getActivity().getIntent().getDoubleExtra("money", 0);
		checkdate = getActivity().getIntent().getStringExtra("date");
		String checkType = getActivity().getIntent().getStringExtra("type");
		String checkBeizhu = getActivity().getIntent().getStringExtra("beizhu");
		String checkCountType = getActivity().getIntent().getStringExtra("who");
		filename = getActivity().getIntent().getStringExtra("filename");
		
		//显示图片
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(filename == null)
					return;
				
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
				ImageFragment.newInstance(path)
				.show(fm, DIALOG_IMAGE);
			}
		});
		
		name.setText(checkname);
		money.setText(String.valueOf(checkmoney));
		type.setText(checkType);
		date.setText(checkdate);
		texthead.setText("账单明细");
		beizhu.setText(checkBeizhu);
		who.setText(checkCountType);
		topButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		//String time= intent.getStringExtra("time");
		return view;
		
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.check_delect:
//			Log.i("CheckFragment", "点击了");
//			cursor = database.selectName(checkdate);
//			countList = database.query(checkdate);
//			Log.d("CheckFragment", "aaaaaaaaaaaaaaaaa");
//			cursor.moveToFirst();
//			int b = cursor.getColumnIndex("dataid");
//			Log.d("CheckFragment", String.valueOf(b));
//			Long a = Long.parseLong(cursor.getString(b));
//			Log.d("CheckFragment", String.valueOf(a));
//			int i = database.delete(a);//删除数据
//			Intent data = new Intent();
//			data.putExtra("count_dataid", b);
//			getActivity().setResult(getActivity().RESULT_OK,data);
//			getActivity().finish();
//			break;
//
//		}
//		
//	}
	//将缩放后的图片设置给imageView视图
	private void showPhoto(){
		BitmapDrawable b =null;
		if(filename != null){
			String path = getActivity()
					.getFileStreamPath(filename).getAbsolutePath();
			b = PictureUtils.getScaleDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		showPhoto();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PictureUtils.cleanImageview(mPhotoView);
	}

}
