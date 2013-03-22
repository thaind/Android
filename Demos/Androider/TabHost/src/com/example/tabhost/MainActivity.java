package com.example.tabhost;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TabHost tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec tabSpecAnalog = tabs.newTabSpec("tagAnalogClock");
		tabSpecAnalog.setContent(R.id.tabAnalogClock);
		tabSpecAnalog.setIndicator("Analog");

		TabHost.TabSpec tabSpecDigital = tabs.newTabSpec("tagDigitalClock");
		tabSpecDigital.setContent(R.id.tabDigitalClock);
		tabSpecDigital.setIndicator("Digitl");

		tabs.addTab(tabSpecAnalog);
		tabs.addTab(tabSpecDigital);
	}
}
