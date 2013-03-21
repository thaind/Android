package com.example.notificationwiththread;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
	BackgroundThread backgroundThread;
	Handler backgroundHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		backgroundThread = new BackgroundThread();
		backgroundThread.setRunning(true);
		backgroundThread.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		boolean retry = true;
		backgroundThread.setRunning(false);

		while (retry) {
			try {
				backgroundThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class BackgroundThread extends Thread {
		boolean running = false;
		final static String ACTION = "NotifyServiceAction";
		NotificationManager notificationManager;
		Notification myNotification;
		private final String myBlog = "http://android-er.blogspot.com/";
		private static final int MY_NOTIFICATION_ID = 1;

		void setRunning(boolean b) {
			running = b;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (running) {
				try {
					sleep(10000); // send notification in every 10sec.
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Send Notification
				myNotification = new Notification(R.drawable.ic_launcher,
						"Notification!", System.currentTimeMillis());
				Context context = getApplicationContext();
				String notificationTitle = "Exercise of Notification!";
				String notificationText = "http://android-er.blogspot.com/";
				Intent myIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(myBlog));
				PendingIntent pendingIntent = PendingIntent.getActivity(
						getBaseContext(), 0, myIntent,
						Intent.FLAG_ACTIVITY_NEW_TASK);
				myNotification.defaults |= Notification.DEFAULT_SOUND;
				myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				myNotification.setLatestEventInfo(context, notificationTitle,
						notificationText, pendingIntent);
				notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
			}
		}
	}
}
