package com.example.generaterandomnumberinandroid;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Random myRandom = new Random();

		Button buttonGenerate = (Button) findViewById(R.id.generate);
		final TextView textGenerateNumber = (TextView) findViewById(R.id.generatenumber);

		buttonGenerate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textGenerateNumber.setText(String.valueOf(myRandom.nextInt()));
			}
		});
	}
}
