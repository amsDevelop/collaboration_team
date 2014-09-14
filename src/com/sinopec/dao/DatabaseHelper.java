package com.sinopec.dao;

import static android.provider.BaseColumns._ID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DatabaseHelper extends SQLiteOpenHelper implements DatabaseConstants{
    public static final String db_name = "userdb";
    public static final int db_version = 2;
    
    protected static final String USER_TABLE = "user";
    protected static final String USER_NAME = "user_name";
    protected static final String USER_PASSWORD = "user_password";
    
    protected static final String SAVE_TABLE = "save";
    protected static final String SAVE_ID = "project_id";
    protected static final String USER_ID = "user_id";
    

    protected static final String[] USER_ALL_COLUMS = { _ID, USER_NAME, USER_PASSWORD}; 
    protected static final String[] SAVE_ALL_COLUMS = { _ID, USER_ID, SAVE_ID};
    

    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
       
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("anyshare", "sqllite data base is created.....");
//        onCreateUserBean(db);
//        onCreateSaveBean(db);
        db.execSQL("CREATE TABLE " + USER_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT NOT NULL, " +
                USER_PASSWORD + " TEXT NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE " + SAVE_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " TEXT NOT NULL, " +
                SAVE_ID + " TEXT NOT NULL" +
                ");"); 
        
        
    }

    private void onCreateSaveBean(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table save(saveid Integer primary key autoincrement,projectid Integer,userid Integer)";
		db.execSQL(sql);
	}

	private void onCreateUserBean(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table user(userid Integer primary key autoincrement,username text,userpassword text)";
		db.execSQL(sql);
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SAVE_TABLE);
        onCreate(db);
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
