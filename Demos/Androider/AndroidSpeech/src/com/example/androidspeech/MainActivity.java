package com.example.androidspeech;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MainActivity extends Activity implements OnInitListener {

	TextToSpeech myTTS;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myTTS = new TextToSpeech(this, this);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

		String myText1 = "Hello Android!";
		String myText2 = "I can speech.";
		myTTS.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
		myTTS.speak(myText2, TextToSpeech.QUEUE_ADD, null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		myTTS.shutdown();
	}
}
