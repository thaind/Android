package com.example.viewanimation;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button myStartButton1 = (Button) this.findViewById(R.id.start1);
		myStartButton1.setOnClickListener(myStartButton1OnClickListener);
		Button myStartButton2 = (Button) this.findViewById(R.id.start2);
		myStartButton2.setOnClickListener(myStartButton2OnClickListener);
	}

	private Button.OnClickListener myStartButton1OnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LinearLayout myAnimation = (LinearLayout) findViewById(R.id.mylayout);
			myAnimation.startAnimation(new ViewAnimation());
		}
	};

	private Button.OnClickListener myStartButton2OnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AnalogClock myAnimation = (AnalogClock) findViewById(R.id.myClock);
			myAnimation.startAnimation(new ViewAnimation());
		}
	};

	public class ViewAnimation extends Animation {
		int centerX, centerY;

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			setDuration(5000);
			setFillAfter(true);
			setInterpolator(new LinearInterpolator());
			centerX = width / 2;
			centerY = height / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			// TODO Auto-generated method stub
			final Matrix matrix = t.getMatrix();
			matrix.setScale(interpolatedTime, interpolatedTime);
		}
	}

}
