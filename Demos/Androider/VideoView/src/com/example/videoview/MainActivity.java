package com.example.videoview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		VideoView myVideoView = (VideoView) findViewById(R.id.myvideoview);

		// NEED TRUE DEVICE TO RUN
		// String SrcPath =
		// "rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp";
		// myVideoView.setVideoURI(Uri.parse(SrcPath));
		// myVideoView.setMediaController(new MediaController(this));
		// myVideoView.requestFocus();
		// myVideoView.start();

		myVideoView.setVideoURI(Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.babymaybe));
		myVideoView.setMediaController(new MediaController(this));
		myVideoView.requestFocus();

		myVideoView.start();
	}

}
