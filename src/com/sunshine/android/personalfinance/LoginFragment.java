package com.sunshine.android.personalfinance;

import com.sunshine.android.personalfinance.dabase.DBHelper;
import com.sunshine.android.personalfinance.dabase.DBManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast") public class LoginFragment extends Fragment {
	private EditText ed_id;
	private EditText ed_pwd;
	private Button btnlogin;
	private Button btnregister;
	private DBManager dbManager;
	private DBHelper database;
	private static final String TAG = "LoginFragment";
			

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		database = new DBHelper(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		ed_id = (EditText) v.findViewById(R.id.ed_user);
		ed_pwd = (EditText) v.findViewById(R.id.ed_pwd);
		
		btnlogin = (Button) v.findViewById(R.id.button_login);
		btnlogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getUser();
			}
		});
		btnregister = (Button) v.findViewById(R.id.button_register);
		btnregister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(),UserRegisterActivity.class);
				startActivity(intent);
			}
		});
		return v;
	}
	//判断是否登录成功
	@SuppressLint("ShowToast") public void getUser(){
		Log.d("TAG", "在判断是否登录成功");
		String sql = "select * from tb1_user where userid = ?";
		Cursor cursor = database.getWritableDatabase().rawQuery(sql, new
				String[]{ed_id.getText().toString()});
		if(cursor.moveToFirst()){
			if(ed_pwd.getText().toString().equals(cursor.getString(cursor.getColumnIndex("userpwd")))){
				//Toast.makeText(getActivity(), "登录成功", 5000).show();
				Intent intent = new Intent(getActivity(),MainActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(getActivity(), "用户名或者密码错误", 5000).show();
			}				
		}
		database.close();
	}
	
	

}
