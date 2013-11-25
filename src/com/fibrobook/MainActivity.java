package com.fibrobook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.fibrobook.model.DailyEventSummaryDAO;
import com.fibrobook.model.SymphtomSummaryDAO;
import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;
import com.fibrobook.viewpager.custom.CardFragment;
import com.fibrobook.viewpager.custom.MyPagerAdapter;

public class MainActivity extends FragmentActivity {
	
	protected PagerSlidingTabStrip tabs;

	private static final int REGISTER_USER = 2;
	private static final int DO_LOGIN = 3;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	public static String date;
	public static User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		prepareEnvironment();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		date = format.format(new Date());

		String[] d = date.split("-");
		setTitle(getTitle().toString() + " - " + d[2] + "/" + d[1] + "/" + d[0]);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
	}

	private void prepareEnvironment() {
		user = UserDAO.getUser(this, 1);
		if (user == null)
			startActivityForResult(new Intent(this, NewUserForm.class), REGISTER_USER);
		else if(user.getPassword().length()>0)
			startActivityForResult(new Intent(this,Login.class), DO_LOGIN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {

			case R.id.action_calendar:
				String[] fullDate = date.split("-");
				fullDate[1] = String.valueOf(Integer.parseInt(fullDate[1]) - 1);
				updateDate(fullDate);
				break;
			case R.id.action_settings:
				startActivity(new Intent(this, Settings.class));
				break;
	
			default: break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==REGISTER_USER)
			Toast.makeText(getApplicationContext(),"Welcome, " + user.getName() + "!", Toast.LENGTH_LONG).show();
		else if(requestCode==DO_LOGIN) finish();
	}

	void updateDate(String[] cd){
		final Dialog dialog = new Dialog(this,com.fibrobook.R.style.FullHeightDialog);
		dialog.setContentView(R.layout.datepicker_dialog);
		DatePicker dp = (DatePicker) dialog.findViewById(R.id.dp);
		dp.setMaxDate(new Date().getTime());
		String[] d = date.split("-");
		dp.updateDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
		dialog.findViewById(R.id.dp_dialog_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePicker dp = (DatePicker) dialog.findViewById(R.id.dp);
				date = String.valueOf(dp.getYear())+"-"+String.valueOf(dp.getMonth()+1)+"-"+String.valueOf(dp.getDayOfMonth());
				SymphtomSummaryDAO dao = new SymphtomSummaryDAO(getApplicationContext());
				DailyEventSummaryDAO des = new DailyEventSummaryDAO(getApplicationContext());
				CardFragment.ds = dao.getSymphtomSummary(date);
				CardFragment.des = des.getDailySummary(date);
				dao.close();
				des.close();
				String[] d = date.split("-");
				String[] title = getTitle().toString().split(" - ");
				setTitle(title[0] + " - " + d[2] + "/" + d[1] + "/" + d[0]);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}