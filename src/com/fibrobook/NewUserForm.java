package com.fibrobook;

import com.fibrobook.model.User;
import com.fibrobook.model.UserDAO;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewUserForm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_user);
		
		Button btn = (Button) findViewById(R.id.submit);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.name);
				EditText age = (EditText) findViewById(R.id.age);
				
				User user = new User(name.getEditableText().toString());
				user.setAge(Integer.parseInt(age.getEditableText().toString()));
				UserDAO dao = new UserDAO(NewUserForm.this);
				dao.add(user);
				Diary.user = user;
				dao.close();
				
				setResult(-1);
				finish();
			}
		});
	}

}