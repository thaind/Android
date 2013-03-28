package com.example.image_mapping_mask;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	TouchView myAndroid;
	InfoView infoView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myAndroid = (TouchView) findViewById(R.id.myandroid);
		infoView = (InfoView) findViewById(R.id.infoview);

	}

	public void updateMsg(String t_info, float t_x, float t_y, int t_c) {

		infoView.updateInfo(t_info, t_x, t_y, t_c);

	}

	public void clearMsg() {

		infoView.clearInfo();

	}

}
