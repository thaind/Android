package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "-MyService.onBind()-", Toast.LENGTH_LONG).show();
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "-MyService.onCreate()-", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "-MyService.onDestroy()-", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "-MyService.onRebind()-", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "-MyService.onStart()-", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "-MyService.onUnbind()-", Toast.LENGTH_LONG)
				.show();
		return false;
	}

	public class LocalBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}

}