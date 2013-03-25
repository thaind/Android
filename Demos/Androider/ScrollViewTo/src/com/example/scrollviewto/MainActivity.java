package com.example.scrollviewto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class MainActivity extends Activity {

	Button buttonScrollUp, buttonScrollDown, buttonScrollToTop;
	ScrollView myView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonScrollUp = (Button) findViewById(R.id.scrollup);
		buttonScrollDown = (Button) findViewById(R.id.scrolldown);
		buttonScrollToTop = (Button) findViewById(R.id.scrolltotop);
		myView = (ScrollView) findViewById(R.id.myview);

		buttonScrollUp.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myView.scrollBy(0, -20);
			}
		});

		buttonScrollDown.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myView.scrollBy(0, +20);
			}
		});

		buttonScrollToTop.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				myView.scrollTo(0, 0);
			}
		});
	}

}
