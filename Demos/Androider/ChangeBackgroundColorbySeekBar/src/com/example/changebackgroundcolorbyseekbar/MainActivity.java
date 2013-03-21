package com.example.changebackgroundcolorbyseekbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int seekR, seekG, seekB;
	SeekBar redSeekBar, greenSeekBar, blueSeekBar;
	LinearLayout mScreen;
	TextView tvValue;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvValue = (TextView) findViewById(R.id.tvValue);
		mScreen = (LinearLayout) findViewById(R.id.myScreen);
		redSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_R);
		greenSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_G);
		blueSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_B);
		updateBackground();

		redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
		greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
		blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

	}

	private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			updateBackground();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}
	};

	private void updateBackground() {
		seekR = redSeekBar.getProgress();
		seekG = greenSeekBar.getProgress();
		seekB = blueSeekBar.getProgress();
		tvValue.setText("redSeekBar: " + seekR + " greenSeekBar: " + seekG
				+ " blueSeekBar: " + seekB);
		mScreen.setBackgroundColor(0xff000000 + seekR * 0x10000 + seekG * 0x100
				+ seekB);
	}
}
