package com.example.converttexttolinkwithlinkify;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText editField = (EditText) findViewById(R.id.editfield);
		editField.setOnKeyListener(new EditText.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Linkify.addLinks(editField, Linkify.EMAIL_ADDRESSES
						| Linkify.MAP_ADDRESSES | Linkify.PHONE_NUMBERS
						| Linkify.WEB_URLS);
				return false;
			}
		});
	}
}
