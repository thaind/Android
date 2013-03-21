package com.example.copyandpasteusingclipboardmanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText src = (EditText) findViewById(R.id.src);
		final TextView dest = (TextView) findViewById(R.id.dest);
		Button copynPaste = (Button) findViewById(R.id.copynpaste);

		final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		copynPaste.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clipBoard.setText(src.getText());
				dest.setText(clipBoard.getText());

			}
		});
	}

}
