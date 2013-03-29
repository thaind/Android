package com.example.linearlayout_custom;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class MainActivity extends Activity {

	MyHorizontalLayout myHorizontalLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myHorizontalLayout = (MyHorizontalLayout) findViewById(R.id.mygallery);

		String ExternalStorageDirectoryPath = Environment
				.getExternalStorageDirectory().getAbsolutePath();

		String targetPath = ExternalStorageDirectoryPath + "/test/";

		Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG)
				.show();
		File targetDirector = new File(targetPath);

		File[] files = targetDirector.listFiles();
		for (File file : files) {
			myHorizontalLayout.add(file.getAbsolutePath());
		}
	}

}
