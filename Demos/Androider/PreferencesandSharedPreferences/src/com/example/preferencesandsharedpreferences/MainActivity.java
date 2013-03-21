package com.example.preferencesandsharedpreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	CheckBox checkBox;
	TextView editTextStatus;
	TextView myListPref;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonSetPreference = (Button) findViewById(R.id.setpreference);
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		editTextStatus = (TextView) findViewById(R.id.edittextstatus);
		myListPref = (TextView) findViewById(R.id.list_pref);
		buttonSetPreference.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						AndroidPreference.class));
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();

		SharedPreferences myPreference = PreferenceManager
				.getDefaultSharedPreferences(this);

		checkBox.setChecked(myPreference.getBoolean("checkbox", true));
		editTextStatus.setText("EditText Status: "
				+ myPreference.getString("edittexvalue", ""));
		String myListPreference = myPreference.getString("listPref",
				"default choice");
		myListPref.setText(myListPreference);

	}
}