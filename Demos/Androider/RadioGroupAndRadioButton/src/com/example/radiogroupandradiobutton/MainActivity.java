package com.example.radiogroupandradiobutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	RadioButton myOption1, myOption2, myOption3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myOption1 = (RadioButton) findViewById(R.id.option1);
		myOption2 = (RadioButton) findViewById(R.id.option2);
		myOption3 = (RadioButton) findViewById(R.id.option3);
		myOption1.setOnClickListener(myOptionOnClickListener);
		myOption2.setOnClickListener(myOptionOnClickListener);
		myOption3.setOnClickListener(myOptionOnClickListener);
		myOption1.setChecked(true);
	}

	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(
					MainActivity.this,
					"Option 1 : " + myOption1.isChecked() + "\n"
							+ "Option 2 : " + myOption2.isChecked() + "\n"
							+ "Option 3 : " + myOption3.isChecked(),
					Toast.LENGTH_LONG).show();

		}

	};

}
