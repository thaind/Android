package com.example.loadbitmapfromsdcard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private final String imageInSD = "/sdcard/er.PNG";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
		ImageView myImageView = (ImageView) findViewById(R.id.imageview);
		myImageView.setImageBitmap(bitmap);

	}
}
