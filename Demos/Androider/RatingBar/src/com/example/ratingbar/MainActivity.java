package com.example.ratingbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final RatingBar ratingBar_Small = (RatingBar) findViewById(R.id.ratingbar_Small);
		final RatingBar ratingBar_Indicator = (RatingBar) findViewById(R.id.ratingbar_Indicator);
		final RatingBar ratingBar_default = (RatingBar) findViewById(R.id.ratingbar_default);

		ratingBar_default
				.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						ratingBar_Small.setRating(rating);
						ratingBar_Indicator.setRating(rating);
						Toast.makeText(MainActivity.this,
								"rating:" + String.valueOf(rating),
								Toast.LENGTH_LONG).show();
					}
				});
	}

}
