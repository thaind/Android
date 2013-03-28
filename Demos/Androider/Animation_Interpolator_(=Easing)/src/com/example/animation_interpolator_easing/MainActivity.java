package com.example.animation_interpolator_easing;

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

		final Animation animAccelerateDecelerate = AnimationUtils
				.loadAnimation(this, R.anim.accelerate_decelerate);
		final Animation animAccelerate = AnimationUtils.loadAnimation(this,
				R.anim.accelerate);
		final Animation animAnticipate = AnimationUtils.loadAnimation(this,
				R.anim.anticipate);
		final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(
				this, R.anim.anticipate_overshoot);
		final Animation animBounce = AnimationUtils.loadAnimation(this,
				R.anim.bounce);
		final Animation animCycle = AnimationUtils.loadAnimation(this,
				R.anim.cycle);
		final Animation animDecelerate = AnimationUtils.loadAnimation(this,
				R.anim.decelerate);
		final Animation animLinear = AnimationUtils.loadAnimation(this,
				R.anim.linear);
		final Animation animOvershoot = AnimationUtils.loadAnimation(this,
				R.anim.overshoot);

		final ImageView image = (ImageView) findViewById(R.id.image);
		Button btnAccelerateDecelerate = (Button) findViewById(R.id.acceleratedecelerate);
		Button btnAccelerate = (Button) findViewById(R.id.accelerate);
		Button btnAnticipate = (Button) findViewById(R.id.anticipate);
		Button btnAnticipateOvershoot = (Button) findViewById(R.id.anticipateovershoot);
		Button btnBounce = (Button) findViewById(R.id.bounce);
		Button btnCycle = (Button) findViewById(R.id.cycle);
		Button btnDecelerate = (Button) findViewById(R.id.decelerate);
		Button btnLinear = (Button) findViewById(R.id.linear);
		Button btnOvershoot = (Button) findViewById(R.id.overshoot);

		btnAccelerateDecelerate
				.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						image.startAnimation(animAccelerateDecelerate);

					}
				});

		btnAccelerate.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animAccelerate);

			}
		});

		btnAnticipate.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animAnticipate);

			}
		});

		btnAnticipateOvershoot.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animAnticipateOvershoot);

			}
		});

		btnBounce.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animBounce);

			}
		});

		btnCycle.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animCycle);

			}
		});

		btnDecelerate.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animDecelerate);

			}
		});

		btnLinear.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animLinear);

			}
		});

		btnOvershoot.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image.startAnimation(animOvershoot);

			}
		});

	}

}
