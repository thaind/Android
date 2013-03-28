package com.example.timepickerdialog;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity {

	TimePicker myTimePicker;
	Button buttonStartDialog12, buttonStartDialog24;
	TextView info;

	TimePickerDialog timePickerDialog;

	String dialogMsg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		info = (TextView) findViewById(R.id.info);

		buttonStartDialog12 = (Button) findViewById(R.id.startDialog12);
		buttonStartDialog12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openTimePickerDialog(false);

			}
		});

		buttonStartDialog24 = (Button) findViewById(R.id.startDialog24);
		buttonStartDialog24.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openTimePickerDialog(true);

			}
		});
	}

	private void openTimePickerDialog(boolean is24r) {
		Calendar calendar = Calendar.getInstance();
		timePickerDialog = new TimePickerDialog(MainActivity.this,
				onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), is24r);
		timePickerDialog.setTitle("TimePickerDialog Title");
		timePickerDialog.setMessage("TimePickerDialog Message");

		timePickerDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {
				dialogMsg = "OnShow\n";
				info.setText(dialogMsg);
			}
		});

		timePickerDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialogMsg += "OnCancel\n";
				info.setText(dialogMsg);

			}
		});
		timePickerDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				dialogMsg += "OnDismiss\n";
				info.setText(dialogMsg);
			}
		});

		timePickerDialog.show();

	}

	OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dialogMsg += "OnTimeSet " + hourOfDay + " : " + minute + "\n";
			info.setText(dialogMsg);
		}
	};

}
