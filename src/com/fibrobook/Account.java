package com.fibrobook;

import com.fibrobook.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Account extends Activity {
	
	private static final int UPDATE_USER_DATA_REQUEST = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		
		User user = Diary.user;

		TextView name = (TextView) findViewById(R.id.valueName);
		name.setText(user.getName());
		
		TextView age = (TextView) findViewById(R.id.valueAge);
		age.setText(String.valueOf(user.getAge()));
		
		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Account.this,UpdateUserForm.class),UPDATE_USER_DATA_REQUEST);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_USER_DATA_REQUEST) {
            if (resultCode == RESULT_OK) {
        		
        		User user = Diary.user;

        		TextView name = (TextView) findViewById(R.id.valueName);
        		name.setText(user.getName());
        		
        		TextView age = (TextView) findViewById(R.id.valueAge);
        		age.setText(String.valueOf(user.getAge()));
            	
            }
		}
	}

}