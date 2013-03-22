package com.example.alertdialogcustom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AlertDialog.Builder myDialog = new AlertDialog.Builder(
				MainActivity.this);
		myDialog.setTitle("My Dialog");

		ImageView imageView = new ImageView(MainActivity.this);
		imageView.setImageResource(R.drawable.ic_launcher);
		LayoutParams imageViewLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(imageViewLayoutParams);

		TextView textView = new TextView(MainActivity.this);
		textView.setText("It's my custom AlertDialog");
		LayoutParams textViewLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(textViewLayoutParams);

		EditText editText = new EditText(MainActivity.this);
		LayoutParams editTextLayoutParams = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		editText.setLayoutParams(editTextLayoutParams);

		LinearLayout layout = new LinearLayout(MainActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(imageView);
		layout.addView(textView);
		layout.addView(editText);

		myDialog.setView(layout);

		myDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});

		myDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});

		myDialog.show();
	}

}
