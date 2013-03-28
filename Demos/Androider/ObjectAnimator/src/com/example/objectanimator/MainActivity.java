package com.example.objectanimator;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button animatorButton = (Button) findViewById(R.id.animatorbutton);
		Button animationButton = (Button) findViewById(R.id.animationbutton);

		ObjectAnimator objectAnimatorButton = ObjectAnimator.ofFloat(
				animatorButton, "translationX", 0f, 200f);
		objectAnimatorButton.setDuration(1000);
		objectAnimatorButton.start();

		AnimationSet animSetAnimationButton = new AnimationSet(true);
		TranslateAnimation translateAnimAnimationButton = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animSetAnimationButton.addAnimation(translateAnimAnimationButton);
		animSetAnimationButton.setDuration(500);
		animSetAnimationButton.setFillAfter(true);
		animationButton.setAnimation(animSetAnimationButton);

		animatorButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(),
						"Animator Button Clicked", Toast.LENGTH_SHORT).show();
			}
		});

		animationButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(),
						"Animation Button Clicked", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
