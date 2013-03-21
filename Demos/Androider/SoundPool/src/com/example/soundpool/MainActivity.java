package com.example.soundpool;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	SoundPool soundPool;
	HashMap<Integer, Integer> soundPoolMap;
	int soundID = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		// soundPoolMap.put(soundID, soundPool.load(this, R.raw.midi_sound, 1));
		soundPoolMap.put(soundID, soundPool.load(this, R.raw.fallbackring, 1));

		Button buttonPlay = (Button) findViewById(R.id.play);
		Button buttonPause = (Button) findViewById(R.id.pause);
		buttonPlay.setOnClickListener(buttonPlayOnClickListener);
		buttonPause.setOnClickListener(buttonPauseOnClickListener);
	}

	Button.OnClickListener buttonPlayOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			float curVolume = audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float leftVolume = curVolume / maxVolume;
			float rightVolume = curVolume / maxVolume;
			int priority = 1;
			int no_loop = 0;
			float normal_playback_rate = 1f;
			soundPool.play(soundID, leftVolume, rightVolume, priority, no_loop,
					normal_playback_rate);

			Toast.makeText(MainActivity.this, "soundPool.play()",
					Toast.LENGTH_LONG).show();
		}
	};

	Button.OnClickListener buttonPauseOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			soundPool.pause(soundID);
			Toast.makeText(MainActivity.this, "soundPool.pause()",
					Toast.LENGTH_LONG).show();
		}
	};
}
