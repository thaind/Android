package com.example.androidtwitterclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText MyUsername;
	EditText MyPassword;
	EditText MyStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button MySubmitButton = (Button) findViewById(R.id.mySubmit);
		MySubmitButton.setOnClickListener(MySubmitButtonOnClickListener);
		MyUsername = (EditText) findViewById(R.id.username);
		MyPassword = (EditText) findViewById(R.id.password);
		MyStatus = (EditText) findViewById(R.id.whatareyoudoing);
	}

	private Button.OnClickListener MySubmitButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (SendTwitter(MyUsername.getText().toString(), MyPassword
					.getText().toString(), MyStatus.getText().toString())) {
				Toast.makeText(MainActivity.this, "OK",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "ERROR",
						Toast.LENGTH_SHORT).show();
			}

		}

	};

	public static boolean SendTwitter(String twitteruser, String twitterpass,
			String msg) {

		boolean result = true;

		try {

			// String twitteruser = "android_er";
			// String twitterpass = "pass_word";
			URLConnection connection = null;
			String credentials = twitteruser + ":" + twitterpass;

			String encodecredentials = new String(
					Base64.encodeBase64(credentials.getBytes()));

			URL url = new URL("http://twitter.com/statuses/update.xml");
			String encodedData = URLEncoder.encode(msg, "UTF-8");

			connection = url.openConnection();

			connection.setRequestProperty("Authorization", "Basic "
					+ encodecredentials);
			connection.setDoOutput(true);

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("status=" + encodedData);
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while (in.readLine() != null) {
			}
			in.close();
		} catch (Exception e) {
			result = false;
		}

		return result;

	}

}
