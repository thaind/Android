package com.example.autocompletetextviewmulti;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* AutoCompleteTextView */
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		String[] Names = new String[] { "Anil", "Anusha", "Amrutha", "Swathi",
				"Swapna", "swetha", "Srinivas" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Names);
		textView.setThreshold(1);
		textView.setAdapter(adapter);

		/* MultiAutoCompleteTextView */
		MultiAutoCompleteTextView textView1 = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
		textView1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		String[] Months = new String[] { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Months);
		textView1.setThreshold(1);
		textView1.setAdapter(adapter1);

	}
}
