package com.example.activityforresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView MyCountry;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MyCountry = (TextView) findViewById(R.id.myCountry);
		Button SelectCountryButton = (Button) findViewById(R.id.selectCountryButton);
		SelectCountryButton
				.setOnClickListener(SelectCountryButtonOnClickListener);
	}

	private Button.OnClickListener SelectCountryButtonOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AndroidListActivity.class);

			startActivityForResult(intent, 0);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			switch (resultCode) {
			case RESULT_OK:
				MyCountry.setText(data.getStringExtra("country"));
				break;
			case RESULT_CANCELED:
				break;

			}

		}
	}
}
