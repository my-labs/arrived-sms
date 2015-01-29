package com.wat.arrivedsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBase {
	static final String DATABASE_NAME="login.db";
	static final int DATABASE_VERSION=1;
	public static final int NAME_COLUMN=1;

	static final String DATABASE_CREATE = "create table "+"LOGIN"+
			"( " +"ID"+" integer primary key autoincrement,"+ "USERNAME text unique ,PASSWORD text); ";
	public  SQLiteDatabase database;

	private final Context context;
	private DBHelper dbHelper;

	public LoginDataBase(Context _context){
		context=_context;
		dbHelper=new DBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	public LoginDataBase open() throws SQLException
	{
		database=dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		database.close();
	}

	public SQLiteDatabase getDatabaseInstance()
	{
		return database;
	}

	public void insertEntry(String name ,String pass)   	//Metoda odpowiadajaca za stworzenie nowego rekordu bazy danych
	{
		ContentValues addNew=new ContentValues();			//Stworzenie nowego obiektu do którego wpisane zostana wymagane przy
															//rejestracji parametry (login i haslo)
		addNew.put("USERNAME",name);						//Wpisanie w miejscu nazwy uzytkownika podanego loginu 
		addNew.put("PASSWORD", pass);						//Wpisanie w odpowiednim miejscu hasla podanego przez uzytkownika

		database.insert("LOGIN", null, addNew);				//Wprowadzenie nowego rekordu do wskazanej tablicy LOGIN

	}

	public String getOneEntry(String name)						//Pobranie rekordu dla konkretnej nazwy u¿ytkowników
	{
		Cursor cursor=database.query("LOGIN", null, "USERNAME=?", new String[]{name}, null, null, null);//Stworzenie kursora i przeszukanie 
																//rekordów aby znaleŸc rekord z wprowadzona nazwa uzytkownika
		if(cursor.getCount()<1)									//Sprwadzenie czy kursor nie jest pusty
		{
			cursor.close();										//Zwolnienie kursora
			return "Nie istnieje";								//Zwrocenie komunikatu o braku rekordu
		}
		cursor.moveToFirst();									//Przesuniecie kursora na pierwszy rekord
		String pass= cursor.getString(cursor.getColumnIndex("PASSWORD"));//Pobranie hasla z kolumny password 
		cursor.close();											//Zwolnienie kursora
		return pass; 											//Zwrocenie przypisanego do loginu hasla
		
	}

	public boolean verification(String _name) {			//Metoda odpowiadajaca za weryfikacje zajetosci danego loginu
		String kolumna="USERNAME = ?";					//Przygotowanie stringa do weryfikacji rekordów w bazie danych
		Cursor c = database.rawQuery("SELECT 1 FROM "+"Login"+" WHERE " +kolumna , new String[] {_name});//Przygotowanie cursora wskazujacego na
		boolean wykorzystanie = c.moveToFirst();		//rekord z loginem takim jak wpisal uzytkownik i przypisanie do zmiennej wykorzystanie 
														//true gdy login jest zajety lub false gdy kursor jest pusty
		c.close();										//Zwolnienie kursora
		return wykorzystanie;							//Zwrócenie wartosci true lub false
	}

}
