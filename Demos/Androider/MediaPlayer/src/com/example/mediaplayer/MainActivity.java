package com.example.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	MediaPlayer mediaPlayer;
	Button buttonPlayPause, buttonQuit;
	TextView textState;

	private int stateMediaPlayer;
	private final int stateMP_NotStarter = 0;
	private final int stateMP_Playing = 1;
	private final int stateMP_Pausing = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonPlayPause = (Button) findViewById(R.id.playpause);
		buttonQuit = (Button) findViewById(R.id.quit);
		textState = (TextView) findViewById(R.id.state);

		buttonPlayPause.setOnClickListener(buttonPlayPauseOnClickListener);
		buttonQuit.setOnClickListener(buttonQuitOnClickListener);

		initMediaPlayer();

	}

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.maybe);
		stateMediaPlayer = stateMP_NotStarter;
		textState.setText("- IDLE -");
	}

	Button.OnClickListener buttonPlayPauseOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (stateMediaPlayer) {
			case stateMP_NotStarter:
				mediaPlayer.start();
				buttonPlayPause.setText("Pause");
				textState.setText("- PLAYING -");
				stateMediaPlayer = stateMP_Playing;
				break;
			case stateMP_Playing:
				mediaPlayer.pause();
				buttonPlayPause.setText("Play");
				textState.setText("- PAUSING -");
				stateMediaPlayer = stateMP_Pausing;
				break;
			case stateMP_Pausing:
				mediaPlayer.start();
				buttonPlayPause.setText("Pause");
				textState.setText("- PLAYING -");
				stateMediaPlayer = stateMP_Playing;
				break;
			}

		}
	};

	Button.OnClickListener buttonQuitOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mediaPlayer.stop();
			mediaPlayer.release();
			finish();
		}
	};
}
