package com.fibrobook;

import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class UpdateUserForm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_user);
		changeColor(MainActivity.currentColor);
		
		User user = MainActivity.user;
		
		EditText name = (EditText) findViewById(R.id.name);
		name.setText(user.getName());
		DatePicker birthday = (DatePicker) findViewById(R.id.birthday);
		String[] birthdate = user.getBirthday().split("-"); 
		birthday.updateDate(Integer.parseInt(birthdate[0]), Integer.parseInt(birthdate[1])-1, Integer.parseInt(birthdate[2]));
		
		Button btn = (Button) findViewById(R.id.submit);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.name);
				DatePicker birthday = (DatePicker) findViewById(R.id.birthday);
				
				User user = new User(name.getEditableText().toString());
				user.setBirthday(birthday.getYear()+"-"+birthday.getMonth()+"-"+birthday.getDayOfMonth());
				UserDAO dao = new UserDAO(UpdateUserForm.this);
				dao.update(user);
				MainActivity.user = user;
				dao.close();
				
				setResult(-1);
				finish();
			}
		});
	}

	private void changeColor(int newColor) {

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(
					R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable });

			if (MainActivity.oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						MainActivity.oldBackground, ld });

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

			MainActivity.oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}
		MainActivity.currentColor = newColor;

	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			MainActivity.handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			MainActivity.handler.removeCallbacks(what);
		}
	};

}