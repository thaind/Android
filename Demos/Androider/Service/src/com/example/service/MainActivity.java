package com.example.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MyService myService;
	private boolean serviceIsBinding;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonStartService = (Button) findViewById(R.id.startservice);
		Button buttonStopService = (Button) findViewById(R.id.stopservice);
		Button buttonBindService = (Button) findViewById(R.id.bindservice);
		Button buttonUnbindService = (Button) findViewById(R.id.unbindservice);

		buttonStartService.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startService();
				Toast.makeText(MainActivity.this, "Start Service",
						Toast.LENGTH_LONG).show();
			}
		});

		buttonStopService.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService();
				Toast.makeText(MainActivity.this, "Stop Service",
						Toast.LENGTH_LONG).show();
			}
		});

		buttonBindService.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bindService();
				Toast.makeText(MainActivity.this, "Bind Service",
						Toast.LENGTH_LONG).show();
			}
		});

		buttonUnbindService.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				unbindService();
				Toast.makeText(MainActivity.this, "Unbind Service",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			myService = ((MyService.LocalBinder) arg1).getService();
			Toast.makeText(MainActivity.this, "onServiceConnected()",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			myService = null;
			Toast.makeText(MainActivity.this, "onServiceDisconnected()",
					Toast.LENGTH_LONG).show();
		}
	};

	private void startService() {
		Intent intentService = new Intent(this, MyService.class);
		this.startService(intentService);
	}

	private void stopService() {
		Intent intentService = new Intent(this, MyService.class);
		this.stopService(intentService);
	}

	private void bindService() {
		Intent intentService = new Intent(this, MyService.class);
		bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
		serviceIsBinding = true;
	}

	private void unbindService() {
		if (serviceIsBinding) {
			unbindService(serviceConnection);
			serviceIsBinding = false;
		}
	}
}
