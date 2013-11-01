package com.fibrobook.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDAO extends SQLiteOpenHelper{
	
	private static int VERSION = 1;
	private static String TABLE = "user";
	private static String[] COLS = {"id","name","birthday","password"};

	public UserDAO(Context context) {
		super(context, TABLE, null, VERSION);
	}
	
	public void add(User user){
		ContentValues values = new ContentValues();
		values.put("name", user.getName());
		values.put("birthday", user.getBirthday());
		values.put("id", 1);
		values.put("password", user.getPassword());
		getWritableDatabase().insert(TABLE, null, values);
		close();
	}
	
	public void update(User user){
		ContentValues values = new ContentValues();
		values.put("name", user.getName());
		values.put("birthday", user.getBirthday());
		getWritableDatabase().update(TABLE, values, "id=1", null);
		close();
	}
	
	public void firstRun(){
		User user = new User("User");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		Date today = new Date();
		user.setBirthday(f.format(today));
		user.setPassword("0000");
		add(user);
	}
	
	public User getUser(){
		Cursor c = getWritableDatabase().query(TABLE,COLS,null,null,null,null,null);
		c.moveToLast();
		User user = new User(c.getString(1));
		user.setBirthday(c.getString(2));
		user.setPassword(c.getString(3));
		c.close();
		return user;
	}
	
	public static User getUser(Context context, int id){
		UserDAO dao = new UserDAO(context);
		Cursor c = dao.getWritableDatabase().query(TABLE, COLS, "id="+id, null, null, null, null);
		
		User user = null;
		
		if(c.moveToLast()){
			user = new User(c.getString(1));
			user.setBirthday(c.getString(2));
			user.setPassword(c.getString(3));
		}
		
		dao.close();
		return user;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE +" ( id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, birthday varchar(10), password varchar(10));";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		this.onCreate(db);
		
	}

}
