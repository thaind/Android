package com.example.objectanimatorforimageview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends Activity {

	TextView info;
	ImageView flingObj;
	FrameLayout mainScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		info = (TextView) findViewById(R.id.info);
		flingObj = (ImageView) findViewById(R.id.flingobject);
		mainScreen = (FrameLayout) findViewById(R.id.mainscreen);

		final GestureDetector myGesture = new GestureDetector(this,
				new MyOnGestureListener());

		flingObj.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return myGesture.onTouchEvent(event);
			}
		});

		flingObj.setClickable(true);

	}

	class MyOnGestureListener implements OnGestureListener {

		int MIN_DIST = 100;

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float e1X = e1.getX();
			float e1Y = e1.getY();
			float e2X = e2.getX();
			float e2Y = e2.getY();
			float distX = e2X - e1X;
			float distY = e2Y - e1Y;

			info.setText("e1X   e1Y : " + String.valueOf(e1X) + " : "
					+ String.valueOf(e1Y) + "\n" + "e2X   e2Y : "
					+ String.valueOf(e2X) + " : " + String.valueOf(e2Y) + "\n"
					+ "velocityX : " + String.valueOf(velocityX) + "\n"
					+ "velocityY : " + String.valueOf(velocityY));

			// Get the Y OFfset
			DisplayMetrics displayMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			int offsetY = displayMetrics.heightPixels
					- mainScreen.getMeasuredHeight();

			int[] location = new int[2];
			flingObj.getLocationOnScreen(location);
			float orgX = location[0];
			float orgY = location[1] - offsetY;

			float stopX = orgX + distX;
			float stopY = orgY + distY;

			if (distX > MIN_DIST) {
				// Fling Right
				ObjectAnimator flingAnimator = ObjectAnimator.ofFloat(flingObj,
						"translationX", orgX, stopX);
				flingAnimator.setDuration(1000);
				flingAnimator.start();
			} else if (distX < -MIN_DIST) {
				// Fling Left
				ObjectAnimator flingAnimator = ObjectAnimator.ofFloat(flingObj,
						"translationX", orgX, stopX);
				flingAnimator.setDuration(1000);
				flingAnimator.start();
			} else if (distY > MIN_DIST) {
				// Fling Down
				ObjectAnimator flingAnimator = ObjectAnimator.ofFloat(flingObj,
						"translationY", orgY, stopY);
				flingAnimator.setDuration(1000);
				flingAnimator.start();
			} else if (distY < -MIN_DIST) {
				// Fling Up
				ObjectAnimator flingAnimator = ObjectAnimator.ofFloat(flingObj,
						"translationY", orgY, stopY);
				flingAnimator.setDuration(1000);
				flingAnimator.start();
			}

			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	};
}
