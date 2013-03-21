package com.example.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView myText,myhello;

	public class BackgroundAsyncTask extends AsyncTask<Void, Boolean, Void> {

		boolean myTextOn;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			while (true) {
				myTextOn = !myTextOn;
				publishProgress(myTextOn);
				SystemClock.sleep(1000);
			}
			// return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// it will never been shown in this exercise...
			// The method onPostExecute() will be called after return of
			// doInBackground().

			// You can check my doInBackground() method, there is no return,
			// it's a infinite loop. That's why the onPostExecute() method will
			// not be called.

			Toast.makeText(getApplicationContext(), "onPostExecute",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			myTextOn = true;
			Toast.makeText(getApplicationContext(), "onPreExecute",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			// TODO Auto-generated method stub
			myhello.setText( values[0].toString());
			if (values[0]) {
				myText.setVisibility(View.GONE);
			} else {
				myText.setVisibility(View.VISIBLE);
			}
		}

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myText = (TextView) findViewById(R.id.mytext);
		myhello = (TextView) findViewById(R.id.myhello);
		new BackgroundAsyncTask().execute();
	}

}
