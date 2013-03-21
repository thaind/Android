package com.example.simulateanimationusingrunnablethread;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	AnimationDrawable AniFrame;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button MyStartButton = (Button) findViewById(R.id.myStartButton);
		MyStartButton.setOnClickListener(MyStartButtonOnClickListener);
		Button MyStopButton = (Button) findViewById(R.id.myStopButton);
		MyStopButton.setOnClickListener(MyStopButtonOnClickListener);

		ImageView MyImageView = (ImageView) findViewById(R.id.myImageView);
		MyImageView.setBackgroundResource(R.drawable.arrow_animation);
		AniFrame = (AnimationDrawable) MyImageView.getBackground();
	}

	Button.OnClickListener MyStartButtonOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AniFrame.start();
		}
	};

	Button.OnClickListener MyStopButtonOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AniFrame.stop();
		}
	};

}
