package com.example.autocompletetextview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class MainActivity extends Activity {

	AutoCompleteTextView myAutoComplete;
	String item[] = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myAutoComplete = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, item);
		myAutoComplete.setThreshold(1);
		myAutoComplete.setAdapter(adapter);
	}

}
