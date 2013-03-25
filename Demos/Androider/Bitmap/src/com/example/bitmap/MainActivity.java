package com.example.bitmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	ImageView image1, image2, image3, image4;
	Button buttonSwitch;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		image4 = (ImageView) findViewById(R.id.image4);
		buttonSwitch = (Button) findViewById(R.id.switchactivity);

		Bitmap bmOriginal = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		image1.setImageBitmap(bmOriginal);

		int width = bmOriginal.getWidth();
		int height = bmOriginal.getHeight();

		final Bitmap bmDulicated2 = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		final Bitmap bmDulicated3 = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		final Bitmap bmDulicated4 = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		int[] srcPixels = new int[width * height];
		bmOriginal.getPixels(srcPixels, 0, width, 0, 0, width, height);
		int[] destPixels = new int[width * height];

		swapGB(srcPixels, destPixels);
		bmDulicated2.setPixels(destPixels, 0, width, 0, 0, width, height);
		image2.setImageBitmap(bmDulicated2);

		swapRB(srcPixels, destPixels);
		bmDulicated3.setPixels(destPixels, 0, width, 0, 0, width, height);
		image3.setImageBitmap(bmDulicated3);

		swapRG(srcPixels, destPixels);
		bmDulicated4.setPixels(destPixels, 0, width, 0, 0, width, height);
		image4.setImageBitmap(bmDulicated4);

		buttonSwitch.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SecondActivity.class);

				Bundle bundle = new Bundle();
				bundle.putParcelable("BITMAP_A", bmDulicated2);
				bundle.putParcelable("BITMAP_B", bmDulicated3);
				bundle.putParcelable("BITMAP_C", bmDulicated4);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	void swapGB(int[] src, int[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = (src[i] & 0xffff0000) | ((src[i] & 0x000000ff) << 8)
					| ((src[i] & 0x0000ff00) >> 8);
		}
	}

	void swapRB(int[] src, int[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = (src[i] & 0xff00ff00) | ((src[i] & 0x000000ff) << 16)
					| ((src[i] & 0x00ff0000) >> 16);
		}
	}

	void swapRG(int[] src, int[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = (src[i] & 0xff0000ff) | ((src[i] & 0x0000ff00) << 8)
					| ((src[i] & 0x00ff0000) >> 8);
		}
	}

}
