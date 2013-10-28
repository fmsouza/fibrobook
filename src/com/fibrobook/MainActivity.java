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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.fibrobook.model.DaySummaryDAO;
import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;
import com.fibrobook.viewpager.custom.CardFragment;
import com.fibrobook.viewpager.custom.MyPagerAdapter;
import com.nevala.calendar.CalendarView;

public class MainActivity extends FragmentActivity {

	private static final int PICK_DATE_REQUEST = 1;
	private static final int REGISTER_USER = 2;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	public static String date;
	public static User user;

	public static Drawable oldBackground = null;
	public static int currentColor = 0xFF5161BC;
	public static Handler handler = new Handler();

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
		changeColor(currentColor);
	}

	private void prepareEnvironment() {
		user = UserDAO.getUser(this, 1);
		if (user == null)
			startActivityForResult(new Intent(this, NewUserForm.class),
					REGISTER_USER);
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
				DaySummaryDAO dao = new DaySummaryDAO(this);
				CardFragment.ds = dao.getDailySummary(date);
				dao.close();
				String[] d = date.split("-");
				String[] title = getTitle().toString().split(" - ");
				setTitle(title[0] + " - " + d[2] + "/" + d[1] + "/" + d[0]);
				break;

			case REGISTER_USER:
				Toast.makeText(getApplicationContext(),
						"Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(
					R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}
		currentColor = newColor;

	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

}