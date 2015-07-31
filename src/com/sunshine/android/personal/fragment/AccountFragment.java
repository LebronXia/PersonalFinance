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
	private DatePicker datePicker; 		//����ѡ��ؼ�
	private AlertDialog alert;		//�Ի���
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
		//��ʼ��ʱ��
		iniDate();
		//�������ڶԻ���
		creatalert();
		initAdapter();
		listview = (ListView) view.findViewById(R.id.list_view);
		listview.setAdapter(adapter);
		//���ö̰���������
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
	//listview�Ķ̰�����¼�
	public class myOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Log.i("AccountFragment", "position--->"+position);
			String date = date_textview.getText().toString();
			initAdapter();
			
			int count = cursor.getCount();//����
						
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
	//���������Ի���
	public class myOnCreateContextMenuListener implements OnCreateContextMenuListener{

		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("");
			//����ѡ��
			Log.i("log", "chooseing menu");
			menu.add(0,0,0,"ɾ��");
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
				int i = database.delete(Long.parseLong(cursor.getString(cursor.getColumnIndex("dataid"))));//ɾ������
				Log.i("AccountFragment", i+"");
				countList.remove(menuInfo.position);
				adapter.removeListItem(menuInfo.position);//ɾ������
				adapter.notifyDataSetChanged();//֪ͨ����Դ�������Ѿ��ı䣬ˢ�½���
				expand.setText("֧����" + 0.0);	
				income.setText("���룺" + 0.0);
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

	//�������ڶԻ���
	public void creatalert(){
		
		Builder builder = new AlertDialog.Builder(getActivity());
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_date, null);
		datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
		builder.setTitle("��ѡ������");
		builder.setView(v);
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {			
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
				expand.setText("֧����" + 0.0);	
				income.setText("���룺" + 0.0);
				acount(countList);
			}
		});
		alert = builder.create();
		//countList = database.query(date);
	}
	//����ʱˢ������
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null){
			Log.d("AccountFragment", "û���յ����ص�ֵ");
			return;
		}
		Log.d("AccountFragment", "���յ����ص�ֵ");
		int position = data.getIntExtra("count_dataid", 0);
		Log.d("AccountFragment", "ѡ�е���" + position);
		//String date = date_textview.getText().toString();
		countList.remove(listview.getItemIdAtPosition(position));
		adapter.notifyDataSetChanged();//֪ͨ����Դ�������Ѿ��ı䣬ˢ�½���
		
	}
	public void initAdapter(){
		String date = date_textview.getText().toString();
		cursor = database.selectName(date);  //��ʱ����ȡ��������
		countList = database.query(date);
				
		adapter = new MyAdapter(countList);
	}
	//��ʼ��ʱ��
	public void iniDate(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		date_textview.setText(year + "-" + month + "-" + day);		
	}

	//��Ǯ����
	public void acount(List<Count> countList){
		double expandSum = 0;
		double inComeSum = 0;
		for(Count count : countList){				
			if(count.getType().equals("֧��")){
				expandSum = expandSum + count.getMoney();
				expand.setText("֧����" + expandSum);	
			} else {
				//expand.setText("֧����" + 0.0);
			}
			 if(count.getType().equals("����")){
					inComeSum = inComeSum + count.getMoney();
					income.setText("����:" + inComeSum);					
			} else {
				//income.setText("���룺" + 0.0);
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
	
	


