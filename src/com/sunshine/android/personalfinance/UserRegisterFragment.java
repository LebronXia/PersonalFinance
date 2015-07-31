package com.sunshine.android.personalfinance;

import com.sunshine.android.personalfinance.dabase.DBHelper;
import com.sunshine.android.personalfinance.duotai.Person;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter.LengthFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegisterFragment extends Fragment{
	private EditText edtext;
	private EditText edpwd;
	private EditText edpwd2;
	private Button btnsub;
	private Button btncancel;
	private Person person;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.user_add, container,false);
		btnsub = (Button) v.findViewById(R.id.butten_sub);
		edtext = (EditText) v.findViewById(R.id.ed_userreg);
		edpwd = (EditText) v.findViewById(R.id.ed_pwdreg);
		edpwd2 = (EditText) v.findViewById(R.id.ed_pwdreg2);
		
		btnsub.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setUser();
			}
		});
		
		btncancel = (Button) v.findViewById(R.id.button_cancelr);
		btncancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		return v;
	}
	public void setUser(){
		
		person = new Person(edtext.getText().toString(), edpwd.getText().toString());		
		DBHelper database = new DBHelper(getActivity());
		if(edtext.getText().toString().length() <= 0
				||edpwd.getText().toString().length() <= 0
				||edpwd2.getText().toString().length() <= 0){
			Toast.makeText(getActivity(), "用户名密码不能为空", 5000).show();
			return;
		}
		if(edtext.getText().toString().length() > 0){
			String sql = "select * from tb1_user where userid =?";
			Cursor cursor = database.getWritableDatabase().rawQuery(sql, 
					new String[]{edtext.getText().toString()});
			if(cursor.moveToFirst()){
				Toast.makeText(getActivity(), "用户名已经存在", 5000).show();
				return;
			}
			if(!edpwd.getText().toString().equals(edpwd2.getText().toString())){
				Toast.makeText(getActivity(), "两次输入的密码不同", 5000).show();
				return;
			}
			if(database.addUser(person)){
				Toast.makeText(getActivity(), "注册成功", 5000).show();
				Intent intent = new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
			}
			else{
				Toast.makeText(getActivity(), "用户注册失败", 5000).show();
			}
			database.close();
					
		}
	}
	
}
