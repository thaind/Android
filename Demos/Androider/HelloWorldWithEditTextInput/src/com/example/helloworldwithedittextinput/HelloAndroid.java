package com.example.helloworldwithedittextinput;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HelloAndroid extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(okOnClickListener);
		Button cancelButton = (Button) findViewById(R.id.cancel);
		cancelButton.setOnClickListener(cancelOnClickListener);
	}

	private Button.OnClickListener okOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText edit_text = (EditText) findViewById(R.id.message_text);
			CharSequence edit_text_value = edit_text.getText();
			setTitle("Hello:" + edit_text_value);
		}
	};

	private Button.OnClickListener cancelOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
