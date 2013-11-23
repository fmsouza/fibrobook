/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fibrobook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.fibrobook.model.SymphtomSummaryDAO;
import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;
import com.fibrobook.viewpager.custom.CardFragment;
import com.fibrobook.viewpager.custom.MyPagerAdapter;
import com.nevala.calendar.CalendarView;

public class MainActivity extends FragmentActivity {
	
	protected PagerSlidingTabStrip tabs;

	private static final int PICK_DATE_REQUEST = 1;
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
			Intent intent = new Intent(this, CalendarView.class);
			String[] d = date.split("-");
			String dd = d[0] + "-" + String.valueOf(Integer.parseInt(d[1]) - 1)
					+ "-" + d[2];
			intent.putExtra("date", dd);
			startActivityForResult(intent, PICK_DATE_REQUEST);
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, Settings.class));
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case PICK_DATE_REQUEST:
				date = data.getStringExtra("date");
				SymphtomSummaryDAO dao = new SymphtomSummaryDAO(this);
				CardFragment.ds = dao.getSymphtomSummary(date);
				dao.close();
				String[] d = date.split("-");
				String[] title = getTitle().toString().split(" - ");
				setTitle(title[0] + " - " + d[2] + "/" + d[1] + "/" + d[0]);
				break;

			case REGISTER_USER:
				Toast.makeText(getApplicationContext(),
						"Welcome, " + user.getName() + "!", Toast.LENGTH_LONG)
						.show();
				break;
				
			case DO_LOGIN:
				

			default:
				break;
			}
		}
		else{
			switch (requestCode) {
			
			case DO_LOGIN:
				finish();
				break;

			default:
				break;
			}
		}
	}

}