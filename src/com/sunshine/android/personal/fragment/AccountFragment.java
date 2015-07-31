package com.sunshine.android.personal.fragment;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.sunshine.android.personalfinance.R;
import com.sunshine.android.personalfinance.CheckActivity;
import com.sunshine.android.personalfinance.dabase.DBHelper;
import com.sunshine.android.personalfinance.dabase.DBManager;
import com.sunshine.android.personalfinance.duotai.Count;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 * @author wuwenjie
 * @description 
 */
public class AccountFragment extends Fragment {
	private TextView date_textview;
	private TextView expand;
	private TextView income;
	private DatePicker datePicker; 		//日期选择控件
	private AlertDialog alert;		//对话框
	private ListView listview;
	private MyAdapter adapter;
	private Cursor cursor = null;
	private Cursor cursor2 = null;
	private List<Count> countList;
	private DBHelper database;
	private DBManager dm;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DBHelper(getActivity());
		countList = new ArrayList<Count>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_account,container,false);
		date_textview = (TextView) view.findViewById(R.id.date_textview);
		expand = (TextView) view.findViewById(R.id.expand_textview);
		income = (TextView) view.findViewById(R.id.income_textview);
		
		date_textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.show();
			}
		});
		//初始化时间
		iniDate();
		//创建日期对话框
		creatalert();
		initAdapter();
		listview = (ListView) view.findViewById(R.id.list_view);
		listview.setAdapter(adapter);
		//设置短按，即监听
		listview.setOnItemClickListener(new myOnItemClickListener());
		listview.setOnCreateContextMenuListener(new myOnCreateContextMenuListener());
		
		acount(countList);
		
		return view;
	}
	public class MyAdapter extends ArrayAdapter<Count>{

		public MyAdapter(List<Count> countList) {
			super(getActivity(), 0, countList);
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.count_item, null);
			}
			
			Count count = getItem(position);
			
			TextView itemTitle = (TextView) convertView.findViewById(R.id.ItemTitle);
			itemTitle.setText(count.getName());
			TextView itemBeizhu = (TextView) convertView.findViewById(R.id.ItemBeizhu);
			itemBeizhu.setText(count.getBeizhu());
			TextView itemMoney = (TextView) convertView.findViewById(R.id.ItemMoney);
			itemMoney.setText(String.valueOf(count.getMoney()));
			return convertView;
		}
		public void removeListItem(int position) {
			// TODO Auto-generated method stub
			
		}
	
	}
	//listview的短按点击事件
	public class myOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Log.i("AccountFragment", "position--->"+position);
			String date = date_textview.getText().toString();
			initAdapter();
			
			int count = cursor.getCount();//个数
						
			cursor.moveToPosition(position);
			Log.i("AccountFragment", cursor.getString(cursor.getColumnIndex("dataid")));
			Log.i("AccountFragment", cursor.getString(cursor.getColumnIndex("name")));
			
			Intent intent = new Intent();
			intent.putExtra("id", cursor.getString(cursor.getColumnIndex("dataid")));
			intent.putExtra("name", cursor.getString(cursor.getColumnIndex("name")));
			intent.putExtra("type", cursor.getString(cursor.getColumnIndex("type")));
			intent.putExtra("money",  cursor.getDouble(cursor.getColumnIndex("money")));
			intent.putExtra("date", cursor.getString(cursor.getColumnIndex("date")));
			intent.putExtra("who", cursor.getString(cursor.getColumnIndex("who")));
			intent.putExtra("beizhu", cursor.getString(cursor.getColumnIndex("beizhu")));
			intent.putExtra("filename", cursor.getString(cursor.getColumnIndex("filename")));
			intent.setClass(getActivity(), CheckActivity.class);
			startActivityForResult(intent, 0);			
		}		
	};
	//长按弹出对话框
	public class myOnCreateContextMenuListener implements OnCreateContextMenuListener{

		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("");
			//设置选项
			Log.i("log", "chooseing menu");
			menu.add(0,0,0,"删除");
		}
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			Log.i("log", "selectItem---->"+item.getItemId());
			try {
				Log.i("AccountFragment", "menuInfo.position" + menuInfo.position);
				cursor.moveToPosition(menuInfo.position);
				Log.i("log", "cursor move success");
				int i = database.delete(Long.parseLong(cursor.getString(cursor.getColumnIndex("dataid"))));//删除数据
				Log.i("AccountFragment", i+"");
				countList.remove(menuInfo.position);
				adapter.removeListItem(menuInfo.position);//删除数据
				adapter.notifyDataSetChanged();//通知数据源，数据已经改变，刷新界面
				expand.setText("支出：" + 0.0);	
				income.setText("收入：" + 0.0);
				acount(countList);
				database.close();
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
			break;

		}
		return super.onContextItemSelected(item);
	}

	//创建日期对话框
	public void creatalert(){
		
		Builder builder = new AlertDialog.Builder(getActivity());
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_date, null);
		datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
		builder.setTitle("请选择日期");
		builder.setView(v);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String date = String.valueOf(datePicker.getYear()) + "-"
						+ String.valueOf(datePicker.getMonth() + 1) + "-"
						+ String.valueOf(datePicker.getDayOfMonth());
				date_textview.setText(date);
				initAdapter();
//				countList = database.query(date);
//				MyAdapter adapter = new MyAdapter(countList);
				listview = (ListView) view.findViewById(R.id.list_view);
				listview.setAdapter(adapter);
				expand.setText("支出：" + 0.0);	
				income.setText("收入：" + 0.0);
				acount(countList);
			}
		});
		alert = builder.create();
		//countList = database.query(date);
	}
	//返回时刷新数据
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null){
			Log.d("AccountFragment", "没有收到返回的值");
			return;
		}
		Log.d("AccountFragment", "有收到返回的值");
		int position = data.getIntExtra("count_dataid", 0);
		Log.d("AccountFragment", "选中的是" + position);
		//String date = date_textview.getText().toString();
		countList.remove(listview.getItemIdAtPosition(position));
		adapter.notifyDataSetChanged();//通知数据源，数据已经改变，刷新界面
		
	}
	public void initAdapter(){
		String date = date_textview.getText().toString();
		cursor = database.selectName(date);  //按时间查获取所有数据
		countList = database.query(date);
				
		adapter = new MyAdapter(countList);
	}
	//初始化时间
	public void iniDate(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		date_textview.setText(year + "-" + month + "-" + day);		
	}

	//算钱记账
	public void acount(List<Count> countList){
		double expandSum = 0;
		double inComeSum = 0;
		for(Count count : countList){				
			if(count.getType().equals("支出")){
				expandSum = expandSum + count.getMoney();
				expand.setText("支出：" + expandSum);	
			} else {
				//expand.setText("支出：" + 0.0);
			}
			 if(count.getType().equals("收入")){
					inComeSum = inComeSum + count.getMoney();
					income.setText("收入:" + inComeSum);					
			} else {
				//income.setText("收入：" + 0.0);
			}												
			}

		}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		cursor.close();
		super.onDestroy();
	}
	}
	
	


