package com.example.statusbarnotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	NotificationManager myNotificationManager;
	private static final int NOTIFICATION_ID = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button myGen = (Button) findViewById(R.id.gen);
		myGen.setOnClickListener(myGenOnClickListener);
		Button myClear = (Button) findViewById(R.id.clear);
		myClear.setOnClickListener(myClearOnClickListener);

	}

	private void GeneratNotification() {

		myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		CharSequence NotificationTicket = "*** Notification";
		CharSequence NotificationTitle = "Attention Please!";
		CharSequence NotificationContent = "- Notification is coming -";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(
				android.R.drawable.btn_star_big_on, NotificationTicket, when);

		Context context = getApplicationContext();

		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, NotificationTitle,
				NotificationContent, contentIntent);

		myNotificationManager.notify(NOTIFICATION_ID, notification);

	}

	Button.OnClickListener myGenOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			GeneratNotification();
		}

	};

	Button.OnClickListener myClearOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			myNotificationManager.cancel(NOTIFICATION_ID);
		}

	};

}
