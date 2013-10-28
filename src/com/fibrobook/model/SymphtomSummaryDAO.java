package com.fibrobook.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SymphtomSummaryDAO extends SQLiteOpenHelper{
	
	private static int VERSION = 1;
	private static String TABLE = "user_disease_log";
	private static String[] COLS = {"user","disease","date","intensity","local"};
	private static Context mainContext;

	public SymphtomSummaryDAO(Context context) {
		super(context, TABLE, null, VERSION);
		mainContext = context;
	}
	
	public void save(SymphtomSummary ds){
		if(rowExists(ds)) update(ds);
		else add(ds);
	}
	
	public void add(SymphtomSummary ds){
		ContentValues values = new ContentValues();
		values.put("user", ds.getUser().getId());
		values.put("disease", ds.getDisease().getId());
		values.put("date", ds.getDate());
		values.put("intensity", ds.getIntensity());
		values.put("local",ds.getLocal());
		getWritableDatabase().insert(TABLE, null, values);
	}
	
	public void update(SymphtomSummary ds){
		ContentValues values = new ContentValues();
		values.put("intensity", ds.getIntensity());
		values.put("local",ds.getLocal());
		String where = "user="+String.valueOf(ds.getUser().getId())+" AND disease="+String.valueOf(ds.getDisease().getId())+" AND date='"+ds.getDate()+"'";
		getWritableDatabase().update(TABLE, values, where, null);
	}
	
	public List<SymphtomSummary> getSymphtomSummary(String date){
		List<SymphtomSummary> ds = new ArrayList<SymphtomSummary>();
		Cursor c = getWritableDatabase().query(TABLE,COLS,"date='"+date+"'",null,null,null,null);
		while(c.moveToNext()) ds.add(new SymphtomSummary(UserDAO.getUser(mainContext,c.getInt(0)), DiseaseDAO.getDisease(mainContext,c.getInt(1)), c.getString(2), c.getInt(3),c.getString(4)));
		c.close();
		return ds;
		
	}
	
	public boolean rowExists(SymphtomSummary ss){
		Cursor c = getWritableDatabase().query(TABLE, COLS, "user="+String.valueOf(ss.getUser().getId())+" AND disease="+String.valueOf(ss.getDisease().getId())+" AND date='"+ss.getDate()+"'", null, null, null, null);
		int count = c.getCount();
		c.close();
		return (count>0)? true:false;
	}
	
	public List<SymphtomSummary> getList(){
		List<SymphtomSummary> ss = new ArrayList<SymphtomSummary>();
		Cursor c = getWritableDatabase().query(TABLE,COLS,null,null,null,null,null);
		while(c.moveToNext()) ss.add(new SymphtomSummary(UserDAO.getUser(mainContext,c.getInt(0)), DiseaseDAO.getDisease(mainContext,c.getInt(1)), c.getString(2), c.getInt(3),c.getString(4)));
		c.close();
		return ss;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE +" ( user INTEGER NOT NULL, disease INTEGER NOT NULL, date DATE NOT NULL, intensity TINYINT(1) NOT NULL, local VARCHAR(255), PRIMARY KEY (user, disease));";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		this.onCreate(db);
	}

}
