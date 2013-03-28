package com.example.timepicker2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class MainActivity extends Activity {

	TimePicker myTimePicker;
	Button buttonToggle24Hour, buttonGetTime;
	TextView infoByPicker, infoByButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myTimePicker = (TimePicker) findViewById(R.id.picker);
		buttonToggle24Hour = (Button) findViewById(R.id.toggle24Hour);
		buttonGetTime = (Button) findViewById(R.id.gettime);
		infoByPicker = (TextView) findViewById(R.id.infoByPicker);
		infoByButton = (TextView) findViewById(R.id.infoByButton);

		myTimePicker.setOnTimeChangedListener(onTimeChangedListener);
		buttonGetTime.setOnClickListener(buttonGetTimeClickListener);

		buttonToggle24Hour.setOnClickListener(buttonToggle24HourClickListener);
	}

	OnTimeChangedListener onTimeChangedListener = new OnTimeChangedListener() {

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			infoByPicker
					.setText("Triggered by TimePicker.setOnTimeChangedListener: \n"
							+ hourOfDay + " : " + minute + "\n");

		}
	};

	Button.OnClickListener buttonGetTimeClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			infoByButton.setText("Triggered by Button: \n"
					+ myTimePicker.getCurrentHour() + " : "
					+ myTimePicker.getCurrentMinute() + "\n");

		}

	};

	Button.OnClickListener buttonToggle24HourClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			myTimePicker.setIs24HourView(!myTimePicker.is24HourView());
		}

	};

}
