package com.example.colorinandroidbyxml;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final LinearLayout backGround = (LinearLayout) findViewById(R.id.background);
		Button whiteButton = (Button) findViewById(R.id.whitebutton);
		Button redButton = (Button) findViewById(R.id.redbutton);
		Button greenButton = (Button) findViewById(R.id.greenbutton);
		Button blueButton = (Button) findViewById(R.id.bluebutton);

		whiteButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backGround.setBackgroundResource(android.R.color.white);
			}
		});

		redButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backGround.setBackgroundColor(0xff000000 + 0xff0000);
			}
		});

		greenButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backGround.setBackgroundResource(R.color.green);
			}
		});

		blueButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backGround.setBackgroundResource(R.color.blue);
			}
		});
	}

}
