package com.example.alanlogclockusingtext_to_speechfunction;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.TextView;

public class MainActivity extends Activity implements OnInitListener {

	TextToSpeech myTTS;
	AnalogClock MyAnalogClock;
	TextView MyText;

	private int mHour, mMinute;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myTTS = new TextToSpeech(this, this);

		MyAnalogClock = (AnalogClock) findViewById(R.id.myAnalogClock);
		MyAnalogClock.setOnClickListener(MyAnalogClockOnClickListener);
		MyText = (TextView) findViewById(R.id.myText);

	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		myTTS.shutdown();
	}

	private AnalogClock.OnClickListener MyAnalogClockOnClickListener = new AnalogClock.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			final Calendar c = Calendar.getInstance();

			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);

			String myTime = "Now is " + String.valueOf(mHour) + " Hour "
					+ String.valueOf(mMinute) + " Minute";

			MyText.setText(myTime);
			myTTS.speak(myTime, TextToSpeech.QUEUE_FLUSH, null);
		}
	};
}