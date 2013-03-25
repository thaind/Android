package com.example.animation_scale;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

	ImageView image1, image2, image3;
	Animation animationEnlarge, animationShrink;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);

		animationEnlarge = AnimationUtils.loadAnimation(this, R.anim.enlarge);
		animationShrink = AnimationUtils.loadAnimation(this, R.anim.shrink);
		animationEnlarge.setAnimationListener(animationEnlargeListener);
		animationShrink.setAnimationListener(animationShrinkListener);

		image2.startAnimation(animationEnlarge);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		image2.clearAnimation();
	}

	AnimationListener animationEnlargeListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			image2.startAnimation(animationShrink);

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}
	};

	AnimationListener animationShrinkListener = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			image2.startAnimation(animationEnlarge);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}
	};
}
