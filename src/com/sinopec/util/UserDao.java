package com.sinopec.util;

import com.sinopec.data.json.UserBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDao {
	private final String TAG = "com.sinopec.util.UserDao";
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sdb;
	private Cursor cursor;
	private String sql;
	
	public UserDao(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	//保存一个用户
	public void save(UserBean user){
		sdb = dbHelper.getWritableDatabase();
		//开启一个事务
		sdb.beginTransaction();
		try{
			ContentValues cv = new ContentValues();
			cv.put("userid", user.getUserId());
			cv.put("username", user.getUserName());
			cv.put("password", user.getPassword());
			sdb.insert("user", "username", cv);
			//或者
//			sdb.execSQL(sql, new Object[]{ user.getUserName(),user.getUserName(),user.getUserName(),user.getUserName(),user.getUserName()});
			
			sdb.setTransactionSuccessful();
			
		}catch(SQLException e){
			Log.v(TAG, "save user failed!");
		}finally{
			sdb.endTransaction();
			sdb.close();
		}
	}
	

	public UserBean findUser(String userId){
		sdb = dbHelper.getWritableDatabase();
		//开启一个事务
		sdb.beginTransaction();
		sql = "select userid,username,password from user where userid =?";
		cursor = sdb.rawQuery(sql, new String[]{userId});
		UserBean user = null;
		try{
		while(cursor.moveToNext()){
			String userid = cursor.getString(cursor.getColumnIndex("userid")); 
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String password = cursor.getString(cursor.getColumnIndex("password"));
			if(null != userid){
				user = new UserBean(userid, username, password);
			}
		}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			sdb.endTransaction();
			sdb.close();
		}
		return user;
	}

}
