package com.wat.arrivedsms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory , int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase _db) {
		// TODO Auto-generated method stub
		_db.execSQL(LoginDataBase.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// TODO Auto-generated method stub
		// Log the version upgrade.
		Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");

		// Upgrade the existing database to conform to the new version. Multiple
		// previous versions can be handled by comparing _oldVersion and _newVersion
		// values.
		// The simplest case is to drop the old table and create a new one.
		_db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
		// Create a new one.
		onCreate(_db);

	}
}
