package com.example.calendar;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView prompt = new TextView(this);
		setContentView(prompt);
		prompt.setText(getCalendarPrompt());
	}

	private String getCalendarPrompt() {
		Calendar rightNow = Calendar.getInstance();

		String p = rightNow
				+ "\n\n"
				+ rightNow.get(Calendar.YEAR)
				+ "-"
				+ rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT,
						Locale.US) + "-" + rightNow.get(Calendar.DATE) + "\n"
				+ "DAY_OF_YEAR: " + rightNow.get(Calendar.DAY_OF_YEAR) + "\n"
				+ "DAY_OF_MONTH: " + rightNow.get(Calendar.DAY_OF_MONTH) + "\n"
				+ "DAY_OF_WEEK: " + rightNow.get(Calendar.DAY_OF_WEEK) + "\n"
				+ "\n" + "getTime(): " + rightNow.getTime() + "\n"
				+ "getTimeInMillis(): " + rightNow.getTimeInMillis() + "\n"
				+ "\n";

		return p;
	}

}
