package com.example.notificationupdated;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	EditText targetAddr;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonStopService = (Button) findViewById(R.id.stopservice);
		Button buttonSendNotification1 = (Button) findViewById(R.id.sendnotification_1);
		Button buttonSendNotification2 = (Button) findViewById(R.id.sendnotification_2);
		targetAddr = (EditText) findViewById(R.id.targetaddr);

		// Start the service in App start, instead of clicking button
		Intent intent = new Intent(MainActivity.this,
				  NotifyService.class);
		MainActivity.this.startService(intent);

		buttonStopService.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(NotifyService.ACTION);
				intent.putExtra("RQS", NotifyService.RQS_STOP_SERVICE);
				sendBroadcast(intent);
			}
		});

		buttonSendNotification1
				.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setAction(NotifyService.ACTION);
						intent.putExtra("RQS",
								NotifyService.RQS_SEND_NOTIFICATION_1);
						intent.putExtra("TARGET", targetAddr.getText()
								.toString());
						sendBroadcast(intent);
					}
				});

		buttonSendNotification2
				.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setAction(NotifyService.ACTION);
						intent.putExtra("RQS",
								NotifyService.RQS_SEND_NOTIFICATION_2);
						intent.putExtra("TARGET", targetAddr.getText()
								.toString());
						sendBroadcast(intent);
					}
				});

	}
}
