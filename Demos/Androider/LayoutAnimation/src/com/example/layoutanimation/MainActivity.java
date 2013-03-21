package com.example.layoutanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadScreen();
	}

	private Button.OnClickListener MyRestartButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			loadScreen();
		}
	};

	private void loadScreen() {
		setContentView(R.layout.activity_main);
		SetupListView();

		Button MyRestartButton = (Button) findViewById(R.id.myRestartButton);
		MyRestartButton.setOnClickListener(MyRestartButtonOnClickListener);
	}

	private void SetupListView() {
		String[] listItems = new String[] { "Hello!",
				"It's a Demo to use Layout Animation", "Is it Great?",
				"android-er.blogspot.com" };

		ArrayAdapter<String> listItemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);

		ListView lv = (ListView) this.findViewById(R.id.myListView);
		lv.setAdapter(listItemAdapter);
	}

}
