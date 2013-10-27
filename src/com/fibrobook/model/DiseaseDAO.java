package com.fibrobook.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiseaseDAO extends SQLiteOpenHelper{
	
	private static int VERSION = 1;
	private static String TABLE = "disease";
	private static String[] COLS = {"id","title","active"};

	public DiseaseDAO(Context context) {
		super(context, TABLE, null, VERSION);
	}
	
	public List<Disease> getList(){
		List<Disease> diseases = new ArrayList<Disease>();
		Cursor c = getWritableDatabase().query(TABLE,COLS,null,null,null,null,null);
		while(c.moveToNext()) diseases.add(new Disease(c.getString(1), c.getInt(0), c.getInt(2)));
		c.close();
		
		return diseases;
	}
	
	public long save(Disease d){
		ContentValues values = new ContentValues();
		values.put("id", d.getId());
		values.put("title", d.getTitle());
		values.put("active", d.isActive());
		return getWritableDatabase().insert(TABLE, null, values);
	}
	
	public void firstRun(){
		ArrayList<Disease> d = new ArrayList<Disease>();
		d.add(new Disease("Anxiety",1,1));
		d.add(new Disease("Allergy",2,1));
		d.add(new Disease("Candida",3,1));
		d.add(new Disease("Lack of concentration",4,1));
		d.add(new Disease("Cognitive difficulties",5,1));
		d.add(new Disease("Darkening of joints",6,1));
		d.add(new Disease("Depression",7,1));
		d.add(new Disease("Facial/jaw tenderness",8,1));
		d.add(new Disease("Fatigue",9,1));
		d.add(new Disease("Memory blanks",10,1));
		d.add(new Disease("Migraine",11,1));
		d.add(new Disease("Muscle pain",12,1));
		d.add(new Disease("Muscle Spasm",13,1));
		d.add(new Disease("Muscle tightness",14,1));
		d.add(new Disease("Numbness",15,1));
		d.add(new Disease("Insomnia",16,1));
		d.add(new Disease("Internal organs pain",17,1));
		d.add(new Disease("Irritable bladder",18,1));
		d.add(new Disease("Itchy skin",19,1));
		d.add(new Disease("Panic disorder",20,1));
		d.add(new Disease("Stiffness upon walking",21,1));
		d.add(new Disease("Sensitivity to light",22,1));
		d.add(new Disease("Sensitivity to odors",23,1));
		d.add(new Disease("Sensitivity to noise",24,1));
		d.add(new Disease("Sensitivity to medications",25,1));
		d.add(new Disease("Sensitive skin",26,1));
		d.add(new Disease("Swelling in th joints",27,1));
		d.add(new Disease("Tired the whole day",28,1));
		d.add(new Disease("Tired in the morning",29,1));
		d.add(new Disease("Tired in the afternoon",30,1));
		d.add(new Disease("Tired in the evening",31,1));
		d.add(new Disease("Tingling",32,1));
		d.add(new Disease("Tremor",33,1));
		d.add(new Disease("Family relations",34,1));
		d.add(new Disease("Work relations",35,1));
		d.add(new Disease("Physical exercise",36,1));
		d.add(new Disease("Dietary behavior",37,1));
		d.add(new Disease("Social relations",38,1));
		
		for(int i=0; i<d.size(); i++) save(d.get(i));
	}
	
	public static Disease getDisease(Context context, int id){
		DiseaseDAO dao = new DiseaseDAO(context);
		Cursor c = dao.getWritableDatabase().query(TABLE, COLS, "id="+id, null, null, null, null);
		return (c.moveToLast())? new Disease(c.getString(1),c.getInt(0),c.getInt(2)):null;
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
