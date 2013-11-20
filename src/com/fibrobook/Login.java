package com.fibrobook;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Login extends Activity {
	
	public static int PASSWORD_SIZE = 4;
	private final int color = 0xFFD675DA;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.login);
		LinearLayout l = (LinearLayout) findViewById(R.id.linLayout);
		l.setBackgroundColor(color);
		final EditText et = (EditText) findViewById(R.id.input);
		et.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(et.getText().length()==Login.PASSWORD_SIZE){
					if(et.getText().toString().equals(MainActivity.user.getPassword())){
						setResult(-1);
						finish();
					}
					else{
						Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
						et.setText("");
					}
				}
				return false;
			}
		});
	}

}
