package com.fibrobook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fibrobook.model.DaySummary;
import com.fibrobook.model.DaySummaryDAO;
import com.fibrobook.model.Disease;
import com.fibrobook.model.DiseaseDAO;
import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;
import com.nevala.calendar.CalendarView;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Diary extends Activity {
    
    private static final int PICK_DATE_REQUEST = 1;
	private static final int REGISTER_USER = 2;
	public static User user;
	public static List<Disease> symphtoms;
	public static Dialog ratingDialog;
	public static String date;
	private static List<DaySummary> ds;
	private static DaySummary ads;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
		prepareEnvironment();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		date = format.format(new Date());
		
    	DaySummaryDAO dao = new DaySummaryDAO(this);
    	ds = dao.getDailySummary(date);
    	dao.close();
		
		DiseaseDAO ddao = new DiseaseDAO(this);
		symphtoms = ddao.getList();
		ddao.close();
		
		ArrayAdapter<Disease> adapter = new ArrayAdapter<Disease>(this, android.R.layout.simple_list_item_1, symphtoms);
		
		ListView symphtomList = (ListView) findViewById(R.id.symphtomList);
		symphtomList.setAdapter(adapter);
		symphtomList.setClickable(true);
		symphtomList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		        ratingDialog = new Dialog(Diary.this, R.style.FullHeightDialog);
		        ratingDialog.setContentView(R.layout.rating_dialog);
		        ratingDialog.setCancelable(true);
		        RatingBar ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialog_ratingbar);
		        
		        int i = 0;
		        boolean exists = false;
		        while(i<ds.size()){
		        	if(symphtoms.get(position).getId()==ds.get(i).getDisease().getId()){
		        		ads = ds.get(i);
		        		ratingBar.setRating(ads.getIntensity());
		        		exists = true;
		        		break;
		        	}
		        	i++;
		        }
		        if(!exists) ads = new DaySummary(user, symphtoms.get(position), date);
		 
		        Button updateButton = (Button) ratingDialog.findViewById(R.id.rank_dialog_button);
		        updateButton.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		            	RatingBar ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialog_ratingbar);
		            	ads.setIntensity(ratingBar.getRating());
		            	DaySummaryDAO dao = new DaySummaryDAO(Diary.this);
		            	dao.save(ads);
		            	ds = dao.getDailySummary(date);
		            	dao.close();
		                ratingDialog.dismiss();
		            }
		        }); 
		        ratingDialog.show();          
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		
			case R.id.action_calendar:
				Intent intent = new Intent(this,CalendarView.class);
				String[] d = date.split("-");
				String dd = d[0]+"-"+String.valueOf(Integer.parseInt(d[1])-1)+"-"+d[2];
				intent.putExtra("date", dd);
				startActivityForResult(intent, PICK_DATE_REQUEST);
				break;
			case R.id.action_settings:
				startActivity(new Intent(this,Settings.class));
				break;
			
			default: break;
		}

	    return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			
				case PICK_DATE_REQUEST:
	            	date = data.getStringExtra("date");
	            	DaySummaryDAO dao = new DaySummaryDAO(this);
	            	ds = dao.getDailySummary(date);
	            	dao.close();
//	            	adjustHelloString();
					break;
					
				case REGISTER_USER:
					Toast.makeText(getApplicationContext(), "Welcome, "+user.getName()+"!", Toast.LENGTH_SHORT).show();
					break;
					
				default: break;
			}
		}
	}
	
	private void prepareEnvironment(){
		user = UserDAO.getUser(this, 1);
		if(user==null) startActivityForResult(new Intent(this,NewUserForm.class), REGISTER_USER);
		
		DiseaseDAO ddao = new DiseaseDAO(this);
		symphtoms = ddao.getList();
		if(symphtoms.isEmpty()) ddao.firstRun();
		ddao.close();
	}
	
	private void adjustHelloString(){
		Date today = new Date();
		@SuppressWarnings("deprecation")
		Date activeDate = new Date(date);
		TextView text = (TextView) findViewById(R.id.textHello);
		if(today.compareTo(activeDate)>=0){
			if(text.getText()!=findViewById(R.string.hello).toString())
				text.setText(findViewById(R.string.hello).toString());
		}
		else{
			DateFormat format = new SimpleDateFormat("mm/dd/yyyy",Locale.US);
			text.setText("How you were in "+format.format(activeDate));
		}
	}

}
