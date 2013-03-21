package com.example.packageinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttonAboutMe = (Button) findViewById(R.id.aboutme);

		buttonAboutMe.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String strVersion = null;

				PackageInfo packageInfo;
				try {
					packageInfo = getPackageManager().getPackageInfo(
							getPackageName(), 0);
					if (packageInfo.versionName != null
							&& packageInfo.versionCode != 0) {
						strVersion = "Version Name: " + packageInfo.versionName
								+ "\n" + "Version Code: "
								+ String.valueOf(packageInfo.versionCode);

					} else {
						strVersion = "Cannot load Version!";
					}

				} catch (NameNotFoundException e) {
					Log.i("NDT", "NameNotFoundException" + e.toString());
					e.printStackTrace();
					
				}

				new AlertDialog.Builder(MainActivity.this )
						.setTitle("About Me!")
						.setMessage(strVersion)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});
	}
}
