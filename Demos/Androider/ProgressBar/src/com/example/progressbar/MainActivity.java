package com.example.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	ProgressBar myProgressBar;
	int myProgress = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myProgressBar = (ProgressBar) findViewById(R.id.progressbar_Horizontal);

		new Thread(myThread).start();
	}

	private Runnable myThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (myProgress < myProgressBar.getMax()) {
				try {
					myHandle.sendMessage(myHandle.obtainMessage());
					Thread.sleep(100);
				} catch (Throwable t) {
				}
			}
		}

		Handler myHandle = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				myProgress++;
				myProgressBar.setProgress(myProgress);
			}

		};

	};

}
