package com.example.surfaceview_and_mediaplayer;

import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerActivity extends Activity implements SurfaceHolder.Callback {

	Uri targetUri;

	MediaPlayer mediaPlayer;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean pausing = false;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerlayout);

		TextView mediaUri = (TextView) findViewById(R.id.mediauri);
		targetUri = this.getIntent().getData();
		mediaUri.setText(targetUri.toString());

		Button buttonPlayVideo = (Button) findViewById(R.id.playvideoplayer);
		Button buttonPauseVideo = (Button) findViewById(R.id.pausevideoplayer);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFixedSize(176, 144);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();

		buttonPlayVideo.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pausing = false;

				if (mediaPlayer.isPlaying()) {
					mediaPlayer.reset();
				}

				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDisplay(surfaceHolder);

				try {
					mediaPlayer.setDataSource(getApplicationContext(),
							targetUri);
					mediaPlayer.prepare();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				mediaPlayer.start();

			}
		});

		buttonPauseVideo.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (pausing) {
					pausing = false;
					mediaPlayer.start();
				} else {
					pausing = true;
					mediaPlayer.pause();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mediaPlayer.release();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

}