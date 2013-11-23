package com.fibrobook;

import com.fibrobook.model.UserDAO;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdatePassForm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_pass);
		
		Button btn = (Button) findViewById(R.id.submit);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText currentPass = (EditText) findViewById(R.id.pass);
				EditText newPass = (EditText) findViewById(R.id.newPass);
				EditText retypePass = (EditText) findViewById(R.id.repeatPass);
				
				if(!currentPass.getText().toString().equals(MainActivity.user.getPassword()))
					Toast.makeText(getApplicationContext(), "The current password is wrong", Toast.LENGTH_SHORT).show();
				else if(!newPass.getText().toString().equals(retypePass.getText().toString()))
					Toast.makeText(getApplicationContext(), "The password don't match", Toast.LENGTH_SHORT).show();
				else{
					MainActivity.user.setPassword(newPass.getText().toString());
					UserDAO dao = new UserDAO(getApplicationContext());
					dao.updatePass(MainActivity.user);
					dao.close();
					
					setResult(-1);
					finish();
				}
			}
		});
	}
}