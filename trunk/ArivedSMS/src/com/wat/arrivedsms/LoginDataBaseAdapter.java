package com.wat.arrivedsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter {
	static final String DATABASE_NAME="login.db";
	static final int DATABASE_VERSION=1;
	public static final int NAME_COLUMN=1;
	
	 static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME text unique ,PASSWORD text); ";
	public  SQLiteDatabase db;
	
	private final Context context;
	private DBHelper dbHelper;
	
	public LoginDataBaseAdapter(Context _context){
		context=_context;
		dbHelper=new DBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	 
	public LoginDataBaseAdapter open() throws SQLException
	{
		db=dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		db.close();
	}
	
	public SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}
	
	public void insertEntry(String userName,String password)
	{
		ContentValues newValues=new ContentValues();
		
		newValues.put("USERNAME",userName);
		newValues.put("PASSWORD", password);
		
		db.insert("LOGIN", null, newValues);
		
	}
	public int deleteEntry(String UserName)
	{
		String where="USERNAME=?";
		int numberOfEntriesDeleted=db.delete("LOGIN",where , new String[]{UserName});
		
		return numberOfEntriesDeleted;
		
	}


	public String getSingleEntry(String userName)
	{
		Cursor cursor=db.query("LOGIN", null, "USERNAME=?", new String[]{userName}, null, null, null);
		 if(cursor.getCount()<1)
         {
             cursor.close();
             return "NOT EXIST";
         }
         cursor.moveToFirst();
         String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
         cursor.close();
         return password; 
				
	}
	
	public void updateEntry(String userName, String password)
	{
		ContentValues updatedValues=new ContentValues();
		updatedValues.put("USERNAME",userName);
		updatedValues.put("PASSWORD", password);
		
		String where="USERNAME = ?";
		db.update("LOGIN", updatedValues, where, new String[]{userName});
		
	}
	
	public boolean verification(String _username) {
		String where="USERNAME = ?";
	    Cursor c = db.rawQuery("SELECT 1 FROM "+"Login"+" WHERE " +where , new String[] {_username});
	    boolean exists = c.moveToFirst();
	    c.close();
	    return exists;
	}
	
}
