package com.example.animation_custom;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	Button myButton;
	ImageView myImage1, myImage2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myButton = (Button) findViewById(R.id.mybutton);
		myImage1 = (ImageView) findViewById(R.id.myimage1);
		myImage2 = (ImageView) findViewById(R.id.myimage2);

		myButton.setOnClickListener(myOnClickListener);
		myImage1.setOnClickListener(myOnClickListener);
		myImage2.setOnClickListener(myOnClickListener);

	}

	OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			applyAnimation(v);

		}
	};

	private void applyAnimation(View v) {
		MyAnimation myAnimation = new MyAnimation();
		myAnimation.setDuration(5000);
		myAnimation.setFillAfter(true);
		myAnimation.setInterpolator(new OvershootInterpolator());

		v.startAnimation(myAnimation);
	}

	public class MyAnimation extends Animation {

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);

			final Matrix matrix = t.getMatrix();
			matrix.setScale(interpolatedTime, interpolatedTime);
		}
	}

}
