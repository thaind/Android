package com.example.countdowntimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView myCounter = (TextView) findViewById(R.id.mycounter);
		new CountDownTimer(30000, 1000) {

			@Override
			public void onFinish() {
				myCounter.setText("Finished!");
			}

			@Override
			public void onTick(long millisUntilFinished) {
				myCounter.setText("Millisecond Until Finished: "
						+ String.valueOf(millisUntilFinished));
			}

		}.start();
	}

}
