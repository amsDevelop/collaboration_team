package com.sinopec.util;

import com.sinopec.data.json.UserBean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private final static String DB_NAME = "userdb";
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, VERSION);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context){
		super(context, DB_NAME, null, VERSION);
	}
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}
	
	//该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.onCreateUser(db);
//		onCreateFavorite(db);
		Log.v("xuyy", "db onCreate");

	}
	
	//新建用户user表userid,username,password
	public void onCreateUser(SQLiteDatabase db){
		String createUserSql = "create table user(userid text  primary key ,username text, password text)";
		db.execSQL(createUserSql);
		Log.v("xuyy", "db onCreateUser");
	}
	//新建用户user表userid,username,password
	public void onCreateFavorite(SQLiteDatabase db){
		String createUserSql = "create table favorite(favoriteid integer  primary key ,projectid text, projectname text,userid text)";
		db.execSQL(createUserSql);
		Log.v("xuyy", "db onCreateUser");
	}
	public void saveOneUser(SQLiteDatabase db,UserBean user){
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.v("xuyy", "db onUpgrade");

	}

}
