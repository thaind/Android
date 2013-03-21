package com.example.chronometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Chronometer myChronometer = (Chronometer) findViewById(R.id.chronometer);

		myChronometer.setFormat("Formatted time (%s)");

		myChronometer
				.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						long myElapsedMillis = SystemClock.elapsedRealtime()
								- myChronometer.getBase();
						String strElapsedMillis = "Elapsed milliseconds: "
								+ myElapsedMillis;
						Toast.makeText(getApplicationContext(),
								strElapsedMillis, Toast.LENGTH_SHORT).show();
					}
				});

		Button buttonStart = (Button) findViewById(R.id.buttonstart);
		Button buttonStop = (Button) findViewById(R.id.buttonstop);
		Button buttonReset = (Button) findViewById(R.id.buttonreset);

		buttonStart.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myChronometer.setBase(SystemClock.elapsedRealtime());
				myChronometer.start();
			}
		});

		buttonStop.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myChronometer.stop();

			}
		});

		buttonReset.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myChronometer.setBase(SystemClock.elapsedRealtime());

			}
		});

	}
}
