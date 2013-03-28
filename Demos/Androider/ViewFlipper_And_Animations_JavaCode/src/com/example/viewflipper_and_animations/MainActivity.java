package com.example.viewflipper_and_animations;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	ViewFlipper page;

	AnimationSet animSetFlipInForeward;
	AnimationSet animSetFlipOutForeward;
	AnimationSet animSetFlipInBackward;
	AnimationSet animSetFlipOutBackward;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		page = (ViewFlipper) findViewById(R.id.flipper);

		animSetFlipInForeward = new AnimationSet(true);
		TranslateAnimation translateAnimFlipInForeward = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animSetFlipInForeward.addAnimation(translateAnimFlipInForeward);
		animSetFlipInForeward.setDuration(500);
		animSetFlipInForeward.setInterpolator(new OvershootInterpolator());

		animSetFlipOutForeward = new AnimationSet(true);
		TranslateAnimation translateAnimFlipOutForeward = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animSetFlipOutForeward.addAnimation(translateAnimFlipOutForeward);
		animSetFlipOutForeward.setDuration(500);
		animSetFlipOutForeward.setInterpolator(new OvershootInterpolator());

		animSetFlipInBackward = new AnimationSet(true);
		TranslateAnimation translateAnimFlipInBackward = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animSetFlipInBackward.addAnimation(translateAnimFlipInBackward);
		animSetFlipInBackward.setDuration(500);
		animSetFlipInBackward.setInterpolator(new OvershootInterpolator());

		animSetFlipOutBackward = new AnimationSet(true);
		TranslateAnimation translateAnimFlipOutBackward = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animSetFlipOutBackward.addAnimation(translateAnimFlipOutBackward);
		animSetFlipOutBackward.setDuration(500);
		animSetFlipOutBackward.setInterpolator(new OvershootInterpolator());

	}

	private void SwipeRight() {
		page.setInAnimation(animSetFlipInBackward);
		page.setOutAnimation(animSetFlipOutBackward);
		page.showPrevious();
	}

	private void SwipeLeft() {
		page.setInAnimation(animSetFlipInForeward);
		page.setOutAnimation(animSetFlipOutForeward);
		page.showNext();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}

	SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			float sensitvity = 50;
			if ((e1.getX() - e2.getX()) > sensitvity) {
				SwipeLeft();
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				SwipeRight();
			}

			return true;
		}

	};

	GestureDetector gestureDetector = new GestureDetector(
			simpleOnGestureListener);

}
