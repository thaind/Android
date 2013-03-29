package com.example.window_animations;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button btnOpenDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnOpenDialog = (Button) findViewById(R.id.opendialog);
		btnOpenDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialog();
			}
		});
	}

	private void openDialog() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setTitle("Animation Dialog");
		dialog.setContentView(R.layout.dialoglayout);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Button btnDismiss = (Button) dialog.getWindow().findViewById(
				R.id.dismiss);

		btnDismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}
