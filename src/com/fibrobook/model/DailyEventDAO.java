package com.fibrobook.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DailyEventDAO extends SQLiteOpenHelper{
	
	private static int VERSION = 1;
	private static String TABLE = "daily_event";
	private static String[] COLS = {"id","title","active"};

	public DailyEventDAO(Context context) {
		super(context, TABLE, null, VERSION);
	}
	
	public List<DailyEvent> getList(){
		List<DailyEvent> diseases = new ArrayList<DailyEvent>();
		Cursor c = getWritableDatabase().query(TABLE,COLS,null,null,null,null,null);
		while(c.moveToNext()) diseases.add(new DailyEvent(c.getString(1), c.getInt(0), c.getInt(2)));
		c.close();
		
		return diseases;
	}
	
	public long save(DailyEvent d){
		ContentValues values = new ContentValues();
		values.put("id", d.getId());
		values.put("title", d.getTitle());
		values.put("active", d.isActive());
		return getWritableDatabase().insert(TABLE, null, values);
	}
	
	public void firstRun(){
		ArrayList<DailyEvent> d = new ArrayList<DailyEvent>();
		d.add(new DailyEvent("Family relations",1,1));
		d.add(new DailyEvent("Work relations",2,1));
		d.add(new DailyEvent("Physical exercise",3,1));
		d.add(new DailyEvent("Dietary behavior",4,1));
		d.add(new DailyEvent("Social relations",5,1));
		
		for(int i=0; i<d.size(); i++) save(d.get(i));
	}
	
	public static DailyEvent getDailyEvent(Context context, int id){
		DailyEventDAO dao = new DailyEventDAO(context);
		Cursor c = dao.getWritableDatabase().query(TABLE, COLS, "id="+id, null, null, null, null);
		return (c.moveToLast())? new DailyEvent(c.getString(1),c.getInt(0),c.getInt(2)):null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE +" (id INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(255) NOT NULL,active TINYINT(1) NOT NULL);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		this.onCreate(db);
		
	}

}
