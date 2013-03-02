package com.example.autolinkandlayoutchange;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HelloAndroid extends Activity {

	private Button okButton;
	Button cancel1Button;
	EditText textName;
	EditText textPhonenumberIs;
	EditText textEmailIs;
	EditText textWebsiteIs;
	EditText textAddressIs;

	Button backButton;
	Button cancel2Button;
	TextView nameField;
	TextView phonenumberField;
	TextView emailField;
	TextView websiteField;
	TextView addressField;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startLayout1();
	}

	private Button.OnClickListener okOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			textName = (EditText) findViewById(R.id.whoareyou);
			CharSequence textName_value = textName.getText();

			textPhonenumberIs = (EditText) findViewById(R.id.phonenumberIs);
			CharSequence textPhonenumberIs_value = textPhonenumberIs.getText();

			textEmailIs = (EditText) findViewById(R.id.emailIs);
			CharSequence textEmailIs_value = textEmailIs.getText();

			textWebsiteIs = (EditText) findViewById(R.id.websiteIs);
			CharSequence textWebsiteIs_value = textWebsiteIs.getText();

			textAddressIs = (EditText) findViewById(R.id.addressIs);
			CharSequence textAddressIs_value = textAddressIs.getText();

			startLayout2();

			nameField = (TextView) findViewById(R.id.name);
			nameField.setText("Hello " + textName_value);

			phonenumberField = (TextView) findViewById(R.id.phonenumber);
			phonenumberField
					.setText("Phone Number: " + textPhonenumberIs_value);

			emailField = (TextView) findViewById(R.id.email);
			emailField.setText("Email: " + textEmailIs_value);

			websiteField = (TextView) findViewById(R.id.website);
			websiteField.setText("Website: " + textWebsiteIs_value);

			addressField = (TextView) findViewById(R.id.address);
			addressField.setText("Address: " + textAddressIs_value);

		}
	};

	private Button.OnClickListener backOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			startLayout1();
		}
	};

	private Button.OnClickListener cancelOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
		
	};

	private void startLayout1() {
		setContentView(R.layout.main);
		okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(okOnClickListener);
		cancel1Button = (Button) findViewById(R.id.cancel_1);
		cancel1Button.setOnClickListener(cancelOnClickListener);
	};

	private void startLayout2() {
		setContentView(R.layout.main2);
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(backOnClickListener);
		cancel2Button = (Button) findViewById(R.id.cancel_2);
		cancel2Button.setOnClickListener(cancelOnClickListener);
	};
}
