package com.sinopec.dao;

import static android.provider.BaseColumns._ID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sinopec.bean.UserInfo;

public class SqlitService extends DatabaseHelper {

	public SqlitService(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

//	public  boolean login(String username, String userpwd) {
//		SQLiteDatabase sdb = dbHelper.getReadableDatabase();
//		String sql = "select * from user_table where username=?and userpassword=?";
//		Cursor cursor = sdb.rawQuery(sql, new String[] { username, userpwd });
//		if (cursor.moveToFirst() == true) {
//			cursor.close();
//			return true;
//		}
//		return false;
//	}

	
	
	
	public UserInfo findUserInfoByUserPw(String username, String userpwd) {
		UserInfo userInfo = new UserInfo();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(USER_TABLE, USER_ALL_COLUMS, USER_NAME + "=? and " + USER_PASSWORD + "=?", new String[]{username,userpwd}, null,
					null, null, null);
			while (cursor.moveToNext()) {
				userInfo = createRecordItemFromCursorData(cursor);
			}
		} finally {
			closeCursor(cursor);
		}

		return userInfo;
	}

	public void deleteRecordById(String id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(USER_TABLE, _ID + " = ?", new String[] { id });
	}

	public void updateRecordById(UserInfo noteInfo, String id) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = createContentValues(noteInfo);
		db.update(USER_TABLE, values, _ID + " = ?", new String[] { id });
	}

	public void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(USER_TABLE, null, null);

	}

	public void insertRecord(UserInfo userInfo) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = createContentValues(userInfo);
		try {
			db.insertOrThrow(USER_TABLE, null, values);
		} catch (Exception e) {
			Log.e("anyshare", "insertRecord... exception" + e.getMessage());
		}

	}

	private ContentValues createContentValues(UserInfo userInfo) {
		ContentValues values = new ContentValues();
		values.put(USER_NAME, userInfo.getUserName());
		values.put(USER_PASSWORD, userInfo.getUserPassword());

		return values;
	}

	private UserInfo createRecordItemFromCursorData(Cursor cursor) {

		long id = cursor.getLong(0);
		String userName = cursor.getString(1);
		String userPassword = cursor.getString(2);

		UserInfo item = new UserInfo();
		item.setId(String.valueOf(id));
		item.setUserName(userName);
		item.setUserPassword(userPassword);

		return item;
	}

}
