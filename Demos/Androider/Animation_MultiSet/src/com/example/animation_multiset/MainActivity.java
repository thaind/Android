package com.example.animation_multiset;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView image = (ImageView) findViewById(R.id.image);
		Animation multiAnim = AnimationUtils.loadAnimation(this,
				R.anim.multianim);
		image.startAnimation(multiAnim);

	}

}
