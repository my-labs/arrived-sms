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

	public void insertEntry(String userName,String password)   	//Metoda odpowiadajaca za stworzenie nowego rekordu bazy danych
	{
		ContentValues newValues=new ContentValues();			//Stworzenie nowego obiektu do którego wpisane zostana wymagane przy
																//rejestracji parametry (login i haslo)
		newValues.put("USERNAME",userName);						//Wpisanie w miejscu nazwy uzytkownika podanego loginu 
		newValues.put("PASSWORD", password);					//Wpisanie w odpowiednim miejscu hasla podanego przez uzytkownika

		db.insert("LOGIN", null, newValues);					//Wprowadzenie nowego rekordu do wskazanej tablicy LOGIN

	}

	public String getSingleEntry(String userName)				//Pobranie rekordu dla konkretnej nazwy u¿ytkowników
	{
		Cursor cursor=db.query("LOGIN", null, "USERNAME=?", new String[]{userName}, null, null, null);//Stworzenie kursora i przeszukanie 
																//rekordów aby znaleŸc rekord z wprowadzona nazwa uzytkownika
		if(cursor.getCount()<1)									//Sprwadzenie czy kursor nie jest pusty
		{
			cursor.close();										//Zwolnienie kursora
			return "Nie istnieje";								//Zwrocenie komunikatu o braku rekordu
		}
		cursor.moveToFirst();									//Przesuniecie kursora na pierwszy rekord
		String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));//Pobranie hasla z kolumny password 
		cursor.close();											//Zwolnienie kursora
		return password; 										//Zwrocenie przypisanego do loginu hasla

	}

	public boolean verification(String _username) {		//Metoda odpowiadajaca za weryfikacje zajetosci danego loginu
		String where="USERNAME = ?";					//Przygotowanie stringa do weryfikacji rekordów w bazie danych
		Cursor c = db.rawQuery("SELECT 1 FROM "+"Login"+" WHERE " +where , new String[] {_username});//Przygotowanie cursora wskazujacego na
		boolean exists = c.moveToFirst();				//rekord z loginem takim jak wpisal uzytkownik i przypisanie do zmiennej exist wartosci
														//true gdy login jest zajety lub false gdy kursor jest pusty
		c.close();										//Zwolnienie kursora
		return exists;									//Zwrócenie wartosci true lub false
	}

}
