package com.fibrobook;

import java.util.Date;

import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

public class UpdateUserForm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_user);
		
		User user = MainActivity.user;
		
		EditText name = (EditText) findViewById(R.id.name);
		name.setText(user.getName());
		DatePicker birthday = (DatePicker) findViewById(R.id.birthday);
		birthday.setMaxDate((new Date()).getTime());
		String[] birthdate = user.getBirthday().split("-"); 
		birthday.updateDate(Integer.parseInt(birthdate[0]), Integer.parseInt(birthdate[1])-1, Integer.parseInt(birthdate[2]));
		
		LinearLayout cont = (LinearLayout) findViewById(R.id.container);
		cont.removeView(findViewById(R.id.layoutPass));
		cont.removeView(findViewById(R.id.layoutConfirmPass));
		
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
}