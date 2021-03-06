package com.example.ipfromdomain;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText inputHostName;
	Button btnCheck;
	TextView textInetAddress;

	final static String DEFAULT_HOST_NAME = "www.google.com";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inputHostName = (EditText) findViewById(R.id.hostname);
		btnCheck = (Button) findViewById(R.id.check);
		textInetAddress = (TextView) findViewById(R.id.InetAddress);

		inputHostName.setText(DEFAULT_HOST_NAME);
		btnCheck.setOnClickListener(btnCheckOnClickListener);
	}

	Button.OnClickListener btnCheckOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String hostName = inputHostName.getText().toString();
			try {

				InetAddress[] hostInetAddress = InetAddress
						.getAllByName(hostName);
				String all = "";
				for (int i = 0; i < hostInetAddress.length; i++) {
					all = all + String.valueOf(i) + " : "
							+ hostInetAddress[i].toString() + "\n";
				}
				textInetAddress.setText(all);

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}

	};

}
