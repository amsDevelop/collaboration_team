package com.sinopec.util;

import java.util.ArrayList;
import java.util.List;

import com.sinopec.data.json.FavoriteBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavoriteDao {
	private final String TAG = "com.sinopec.util.FavoriteDao";
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sdb;
	private Cursor cursor;
	private String sql;
	
	public FavoriteDao(Context context) {
		super();
		dbHelper = new DatabaseHelper(context);
	}
	//保存收藏
	public void save(FavoriteBean favoriteBean){
		sdb = dbHelper.getWritableDatabase();
		sdb.beginTransaction();
		try{
			ContentValues cv = new ContentValues();
			cv.put("favoriteid", favoriteBean.getFavoriteId());
			cv.put("projectid", favoriteBean.getProjectId());
			cv.put("projectname", favoriteBean.getProjectName());
			cv.put("userid", favoriteBean.getUserId());
			sdb.insert("favorite", "projectname", cv);
			sdb.setTransactionSuccessful();
			
		}catch(SQLException e){
			Log.v(TAG, "save favorite failed!");
			e.printStackTrace();
		}finally{
			sdb.endTransaction();
			sdb.close();
		}
	}
	
	//根据favoriteId查FavoriteBean
	public FavoriteBean findFavoriteBean(String favoriteId){
		sdb= dbHelper.getWritableDatabase();
		String sql = "select favoriteid,projectid,projectname,userid from favorite where favoriteid=?";
		cursor = sdb.rawQuery(sql, new String[]{favoriteId});
		FavoriteBean fb =null;
		try{
			while(cursor.moveToNext()){
				int favoriteid = cursor.getInt(cursor.getColumnIndex("favoriteid"));
				String projectid = cursor.getString(cursor.getColumnIndex("projectid"));
				String projectname = cursor.getString(cursor.getColumnIndex("projectname"));
				String userid = cursor.getString(cursor.getColumnIndex("userid"));
				if(null != projectid){
					fb = new FavoriteBean(favoriteid, projectid, projectname, userid);
				}
					
			}
			sdb.setTransactionSuccessful();
		}catch(SQLException e){
			Log.v(TAG, "findFavoriteBean failed");
			e.printStackTrace();
		}finally{
			sdb.endTransaction();
			sdb.close();
		}
		
		return fb;
	}
	//查找数据库里面FavoriteBean总个数
	public long favoriteBeanCount(){
//		String sql = "select count(favoriteid) from favorite";
		sdb = dbHelper.getWritableDatabase();
		long count =0;
		try{
			cursor = sdb.query("favorite", new String[]{"count(*)"} , null, null, null, null, null);			 
			cursor.moveToFirst();
			count = cursor.getLong(0);
			sdb.setTransactionSuccessful();
		}catch(SQLException e){
			Log.v(TAG, "favoriteBeanCount failed");
			e.printStackTrace();
		}finally{
			sdb.endTransaction();
			sdb.close();
		}
		return count;
	}
	
	//查找数据库中所有的favorite
	public List<FavoriteBean> getAllFavoriteBean(int offset, int limit){
		List<FavoriteBean> fbList = new ArrayList<FavoriteBean>();
		sdb = dbHelper.getWritableDatabase();
		sdb.beginTransaction();
		try{
			//以offset为基准点，取limit条数据
			cursor = sdb.query("favorite", null, null, null, null, null, "favoriteid", offset+ ","+ limit);
			while(cursor.moveToNext()){
				int favoriteid = cursor.getInt(cursor.getColumnIndex("favoriteid"));
				String projectid = cursor.getString(cursor.getColumnIndex("projectid"));
				String projectname = cursor.getString(cursor.getColumnIndex("projectname"));
				String userid = cursor.getString(cursor.getColumnIndex("userid"));
				fbList.add(new FavoriteBean(favoriteid, projectid, projectname, userid));
			}
			
		}catch(SQLException e){
			Log.v(TAG, "getAllFavoriteBean failed");
			e.printStackTrace();
		}finally{
			sdb.endTransaction();
			sdb.close();
		}
		return fbList;
	}

}
