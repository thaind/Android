package com.example.checkbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	CheckBox myOption1, myOption2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myOption1 = (CheckBox) findViewById(R.id.option1);
		myOption2 = (CheckBox) findViewById(R.id.option2);
		Button myOK = (Button) findViewById(R.id.OK);
		myOption2
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Toast.makeText(MainActivity.this,
								(CharSequence) ("Check 2 change"),
								Toast.LENGTH_SHORT).show();
					}
				});
		myOK.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(
						MainActivity.this,
						(CharSequence) ("Option1 = " + myOption1.isChecked()
								+ " " + "Option2 = " + myOption2.isChecked()),
						Toast.LENGTH_LONG).show();
			}

		});

	}
}
