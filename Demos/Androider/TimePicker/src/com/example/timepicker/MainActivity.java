package com.example.timepicker;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int myYear, myMonth, myDay, myHour, myMinute;
	static final int ID_DATEPICKER = 0;
	static final int ID_TIMEPICKER = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button datePickerButton = (Button) findViewById(R.id.datepickerbutton);
		Button timePickerButton = (Button) findViewById(R.id.timepickerbutton);
		datePickerButton.setOnClickListener(datePickerButtonOnClickListener);
		timePickerButton.setOnClickListener(timePickerButtonOnClickListener);
	}

	private Button.OnClickListener datePickerButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(ID_DATEPICKER);
		}
	};

	private Button.OnClickListener timePickerButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myHour = c.get(Calendar.HOUR_OF_DAY);
			myMinute = c.get(Calendar.MINUTE);
			showDialog(ID_TIMEPICKER);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case ID_DATEPICKER:
			Toast.makeText(MainActivity.this,
					"- onCreateDialog(ID_DATEPICKER) -", Toast.LENGTH_LONG)
					.show();
			return new DatePickerDialog(this, myDateSetListener, myYear,
					myMonth, myDay);
		case ID_TIMEPICKER:
			Toast.makeText(MainActivity.this,
					"- onCreateDialog(ID_TIMEPICKER) -", Toast.LENGTH_LONG)
					.show();
			return new TimePickerDialog(this, myTimeSetListener, myHour,
					myMinute, false);
		default:
			return null;

		}
	}

	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			String date = "Year: " + String.valueOf(year) + "\n" + "Month: "
					+ String.valueOf(monthOfYear + 1) + "\n" + "Day: "
					+ String.valueOf(dayOfMonth);
			Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG)
					.show();
		}
	};

	private TimePickerDialog.OnTimeSetListener myTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			String time = "Hour: " + String.valueOf(hourOfDay) + "\n"
					+ "Minute: " + String.valueOf(minute);
			Toast.makeText(MainActivity.this, time, Toast.LENGTH_LONG)
					.show();
		}
	};

}
