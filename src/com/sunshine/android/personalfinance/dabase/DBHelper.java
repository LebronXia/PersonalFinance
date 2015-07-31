package com.sunshine.android.personalfinance.dabase;


import java.util.ArrayList;
import java.util.List;

import com.sunshine.android.personalfinance.duotai.Count;
import com.sunshine.android.personalfinance.duotai.Person;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "user.db";
	private static final int DATABASE_VERSION = 2;
	public static final String ID = "id";    
	public static final String NAME = "NAME";    
    public static final String UerPwd = "userped";
    public static final String CREATE_ACOUNT="create table tb2_account ("
    		+ "dataid integer primary key autoincrement,"
    		+ "name varchar(20),"
    		+ "type varchar(20),"
    		+ "money Double,"
    		+ "date varchar(20),"
    		+ "who varchar(20),"
    		+ "beizhu text,"
    		+ "sum Double,"
    		+ "filename varchar)";
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("create table if not exists tb1_user( id Integer primary key autoincrement, userid varchar(20), userpwd varchar(20))");
		db.execSQL(CREATE_ACOUNT);
		//Log.d("DBHelper", "数据库被创建");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists tb1_user");
		db.execSQL("drop table if exists tb2_account");
		onCreate(db);

	}
	
	//关闭数据库
	public void close(){
		this.getWritableDatabase().close();
	}
	//添加新用户
		public boolean addUser(Person person){
			try {
				//Person p = new Person(); 
				this.getWritableDatabase().execSQL("insert into tb1_user values(null,?,?)", 
						new Object[]{person.getUserId(),person.getUserPwd()});
				//this.execSQL("insert into tb1_user values(null,?,?)", new Object[]{p.getUserId(),p.getUserPwd()});
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}

		}
		
		//
		//删除数据
				public int delete(long id){
					int affect = 0;
					try{
						Log.i("log","try to delete the data in databases");
						affect = this.getWritableDatabase().delete("tb2_account", "dataid=?", new String[]{id+""});
						Log.i("log", "delete success");
					}catch(Exception ex){
						ex.printStackTrace();
						affect = -1;
						Log.i("log", "delete faile");
					}
					
					return affect;
				}
		//根据时间查询显示
		public List<Count> query(String date){
			ArrayList<Count> counts = new ArrayList<Count>();
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(date);
//			int year = calendar.get(Calendar.YEAR);
//			int month = calendar.get(Calendar.MONTH) + 1;
//			int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
//			String datetime = year + "-" + month + "-" + day;
			Cursor c = this.getWritableDatabase().rawQuery("select * from tb2_account where date = ?", 
						new String[]{date});
			while(c.moveToNext()){
				String type = c.getString(c.getColumnIndex("type"));
				String name = c.getString(c.getColumnIndex("name"));
				String beizhu = c.getString(c.getColumnIndex("beizhu"));
				double money = c.getDouble(c.getColumnIndex("money"));
				double sum = c.getDouble(c.getColumnIndex("sum"));
				Count count = new Count(name,type,beizhu,money);
				counts.add(count);
			}
			c.close();
			return counts;
			
		}
		
		//获取列表
		public Cursor selectAll(){
			Cursor cursor = null;
			try{
				String sql = "select * from travels";
				cursor = this.getWritableDatabase().rawQuery(sql, null);
			}catch(Exception ex){
				ex.printStackTrace();
				cursor = null;
			}
			return cursor;
		}
		//按时间查询
	public Cursor selectName(String date){
			
			//String result[] = {};
			Cursor cursor = null;
			try{
				String sql = "select * from tb2_account where date = ?";
				cursor = this.getWritableDatabase().rawQuery(sql, 
						new String[]{date});
			}catch(Exception ex){
				ex.printStackTrace();
				cursor = null;
			}
			
			return cursor;
		}
		public Cursor selectById(int id){
			
			//String result[] = {};
			Cursor cursor = null;
			try{
				String sql = "select * from travels where dataid='" + id +"'";
				cursor = this.getWritableDatabase().rawQuery(sql, null);
			}catch(Exception ex){
				ex.printStackTrace();
				cursor = null;
			}			
			return cursor;
		}
}


