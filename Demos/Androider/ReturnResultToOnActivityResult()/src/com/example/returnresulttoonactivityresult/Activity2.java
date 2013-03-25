package com.example.returnresulttoonactivityresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main2);

		Button btnReturnOK = (Button) findViewById(R.id.returnOK);

		btnReturnOK.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				Intent i = new Intent();

				setResult(RESULT_OK, i);

				finish();

			}
		});

		Button btnReturnCancel = (Button) findViewById(R.id.returnCancel);

		btnReturnCancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				Intent i = new Intent();

				setResult(RESULT_CANCELED, i);

				finish();

			}
		});

	}
}
