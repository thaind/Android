package com.example.downloadmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	final String DOWNLOAD_FILE = "http://goo.gl/w3XV3";

	final String strPref_Download_ID = "PREF_DOWNLOAD_ID";

	SharedPreferences preferenceManager;
	DownloadManager downloadManager;

	ImageView image;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		Button btnStartDownload = (Button) findViewById(R.id.startdownload);
		btnStartDownload.setOnClickListener(btnStartDownloadOnClickListener);

		image = (ImageView) findViewById(R.id.image);
	}

	Button.OnClickListener btnStartDownloadOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Uri downloadUri = Uri.parse(DOWNLOAD_FILE);
			DownloadManager.Request request = new DownloadManager.Request(
					downloadUri);
			long id = downloadManager.enqueue(request);

			// Save the request id
			Editor PrefEdit = preferenceManager.edit();
			PrefEdit.putLong(strPref_Download_ID, id);
			PrefEdit.commit();

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter intentFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(downloadReceiver);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(preferenceManager.getLong(strPref_Download_ID,
					0));
			Cursor cursor = downloadManager.query(query);
			if (cursor.moveToFirst()) {
				int columnIndex = cursor
						.getColumnIndex(DownloadManager.COLUMN_STATUS);
				int status = cursor.getInt(columnIndex);
				if (status == DownloadManager.STATUS_SUCCESSFUL) {

					// Retrieve the saved request id
					long downloadID = preferenceManager.getLong(
							strPref_Download_ID, 0);

					ParcelFileDescriptor file;
					try {
						file = downloadManager.openDownloadedFile(downloadID);
						FileInputStream fileInputStream = new ParcelFileDescriptor.AutoCloseInputStream(
								file);
						Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
						image.setImageBitmap(bm);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	};

}
