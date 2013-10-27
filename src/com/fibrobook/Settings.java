package com.fibrobook;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		List<String> options = Arrays.asList("Manage account","About Fibrobook...");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		
		ListView optionList = (ListView) findViewById(R.id.optionList);
		optionList.setAdapter(adapter);
		optionList.setClickable(true);
		optionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				switch(position){
				
					case 0:
						startActivity(new Intent(Settings.this, Account.class));
						break;
						
					case 1:
						startActivity(new Intent(Settings.this, AboutFibrobook.class));
						break;
						
					default:
						break;
				}
				
				
			}
			
		});
	}

}
