package com.example.servicealarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private PendingIntent pendingIntent;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Button buttonStart = (Button) findViewById(R.id.startalarm);

		Button buttonCancel = (Button) findViewById(R.id.cancelalarm);

		buttonStart.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO Auto-generated method stub

				Intent myIntent = new Intent(MainActivity.this,
						MyAlarmService.class);

				pendingIntent = PendingIntent.getService(MainActivity.this, 0,
						myIntent, 0);

				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

				Calendar calendar = Calendar.getInstance();

				calendar.setTimeInMillis(System.currentTimeMillis());

				calendar.add(Calendar.SECOND, 10);

				/*
				 * alarmManager.set(AlarmManager.RTC_WAKEUP,
				 * calendar.getTimeInMillis(), pendingIntent);
				 */

				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), 5 * 1000, pendingIntent);
				Toast.makeText(MainActivity.this, "Start Alarm",
						Toast.LENGTH_LONG).show();

			}
		});

		buttonCancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO Auto-generated method stub

				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

				alarmManager.cancel(pendingIntent);

				// Tell the user about what we did.

				Toast.makeText(MainActivity.this, "Cancel!", Toast.LENGTH_LONG)
						.show();

			}
		});

	}

}
