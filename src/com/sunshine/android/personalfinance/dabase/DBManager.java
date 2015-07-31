package com.sunshine.android.personalfinance.dabase;

import com.sunshine.android.personalfinance.duotai.Person;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context) {
		super();
		helper = new DBHelper(context);
		//�õ�һ���������ݿ�����Ķ���
		db = helper.getWritableDatabase();		
	}
	//������û�
	public boolean addUser(Person person){
		try {
			Person p = new Person(); 
			db.execSQL("insert into tb1_user values(null,?,?)", new Object[]{p.getUserId(),p.getUserPwd()});
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}
	//�ر����ݿ�
	public void closeDB(){
		db.close();
	}
	//ɾ������
		public int delete(long id){
			int affect = 0;
			try{
				Log.i("log","try to delete the data in databases");
				affect = db.delete("tb2_account", "dataid=?", new String[]{id+""});
				Log.i("log", "delete success");
			}catch(Exception ex){
				ex.printStackTrace();
				affect = -1;
				Log.i("log", "delete faile");
			}
			
			return affect;
		}
	
	
	

}
