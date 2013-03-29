package com.example.animation_scale2;

import android.app.Activity;
import android.os.Bundle;
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

		Button buttonAnimateScale = (Button) findViewById(R.id.animatescale);
		final ImageView image = (ImageView) findViewById(R.id.image);

		final Animation animationScale = AnimationUtils.loadAnimation(this,
				R.anim.scale);
		buttonAnimateScale.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				image.startAnimation(animationScale);
			}
		});

	}

}