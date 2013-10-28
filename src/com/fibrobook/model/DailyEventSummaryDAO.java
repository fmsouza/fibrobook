package com.fibrobook.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DailyEventSummaryDAO extends SQLiteOpenHelper{
	
	private static int VERSION = 1;
	private static String TABLE = "user_event_log";
	private static String[] COLS = {"user","event","date","intensity","local"};
	private static Context mainContext;

	public DailyEventSummaryDAO(Context context) {
		super(context, TABLE, null, VERSION);
		mainContext = context;
	}
	
	public void save(DailyEventSummary de){
		if(rowExists(de)) update(de);
		else add(de);
	}
	
	public void add(DailyEventSummary de){
		ContentValues values = new ContentValues();
		values.put("user", de.getUser().getId());
		values.put("event", de.getDailyEvent().getId());
		values.put("date", de.getDate());
		values.put("intensity", de.getIntensity());
		values.put("local",de.getLocal());
		getWritableDatabase().insert(TABLE, null, values);
	}
	
	public void update(DailyEventSummary ds){
		ContentValues values = new ContentValues();
		values.put("intensity", ds.getIntensity());
		values.put("local",ds.getLocal());
		String where = "user="+String.valueOf(ds.getUser().getId())+" AND event="+String.valueOf(ds.getDailyEvent().getId())+" AND date='"+ds.getDate()+"'";
		getWritableDatabase().update(TABLE, values, where, null);
	}
	
	public List<DailyEventSummary> getDailySummary(String date){
		List<DailyEventSummary> ds = new ArrayList<DailyEventSummary>();
		Cursor c = getWritableDatabase().query(TABLE,COLS,"date='"+date+"'",null,null,null,null);
		while(c.moveToNext()) ds.add(new DailyEventSummary(UserDAO.getUser(mainContext,c.getInt(0)), DailyEventDAO.getDailyEvent(mainContext,c.getInt(1)), c.getString(2), c.getInt(3),c.getString(4)));
		c.close();
		return ds;
		
	}
	
	public boolean rowExists(DailyEventSummary ss){
		Cursor c = getWritableDatabase().query(TABLE, COLS, "user="+String.valueOf(ss.getUser().getId())+" AND event="+String.valueOf(ss.getDailyEvent().getId())+" AND date='"+ss.getDate()+"'", null, null, null, null);
		int count = c.getCount();
		c.close();
		return (count>0)? true:false;
	}
	
	public List<DailyEventSummary> getList(){
		List<DailyEventSummary> ss = new ArrayList<DailyEventSummary>();
		Cursor c = getWritableDatabase().query(TABLE,COLS,null,null,null,null,null);
		while(c.moveToNext()) ss.add(new DailyEventSummary(UserDAO.getUser(mainContext,c.getInt(0)), DailyEventDAO.getDailyEvent(mainContext,c.getInt(1)), c.getString(2), c.getInt(3),c.getString(4)));
		c.close();
		return ss;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE +" ( user INTEGER NOT NULL, event INTEGER NOT NULL, date DATE NOT NULL, intensity TINYINT(1) NOT NULL, local VARCHAR(255), PRIMARY KEY (user, event));";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		this.onCreate(db);
	}

}
