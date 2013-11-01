package com.fibrobook;

import java.util.Date;

import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserForm extends ColoredActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_user);
		
		DatePicker dp = (DatePicker) findViewById(R.id.birthday);
		dp.setMaxDate((new Date()).getTime());
		
		Button btn = (Button) findViewById(R.id.submit);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText name = (EditText) findViewById(R.id.name);
				EditText pass = (EditText) findViewById(R.id.pass);
				EditText confirmPass = (EditText) findViewById(R.id.confirmPass);
				DatePicker birthday = (DatePicker) findViewById(R.id.birthday);
				
				if(name.getText().toString().length()==0)
					Toast.makeText(NewUserForm.this, "You must fill correctly your name", Toast.LENGTH_SHORT).show();
				
				else if(!pass.getText().toString().equals(confirmPass.getText().toString()))
					Toast.makeText(NewUserForm.this, "The passwords don't match", Toast.LENGTH_SHORT).show();
				
				else if(pass.getText().length()==0 || confirmPass.getText().length()==0)
					Toast.makeText(NewUserForm.this, "You must fill the two password fields", Toast.LENGTH_SHORT).show();
				
				else{
					User user = new User(name.getText().toString());
					user.setBirthday(birthday.getYear()+"-"+(birthday.getMonth()+1)+"-"+birthday.getDayOfMonth());
					user.setPassword(pass.getText().toString());
					UserDAO dao = new UserDAO(NewUserForm.this);
					dao.add(user);
					MainActivity.user = user;
					dao.close();
					
					setResult(-1);
					finish();
				}
			}
		});
	}
}