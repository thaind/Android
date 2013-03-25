package com.example.animation_rotate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonRotateCenter = (Button) findViewById(R.id.rotatecenter);
		Button buttonRotateCorner = (Button) findViewById(R.id.rotatecorner);
		final ImageView floatingImage = (ImageView) findViewById(R.id.floatingimage);

		final Animation animationRotateCenter = AnimationUtils.loadAnimation(
				this, R.anim.rotate_center);

		buttonRotateCenter.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				floatingImage.startAnimation(animationRotateCenter);
			}
		});

		final Animation animationRotateCorner = AnimationUtils.loadAnimation(
				this, R.anim.rotate_corner);
		animationRotateCorner.setAnimationListener(new Animation.AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.i("hehe","hehe");
				floatingImage.startAnimation(animationRotateCorner);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
			
		});
		buttonRotateCorner.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				floatingImage.startAnimation(animationRotateCorner);
				
			}
		});
	}
}
