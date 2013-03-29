package com.example.scalebitmapefficiently;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView textTargetUri;
	ImageView targetImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonLoadImage = (Button) findViewById(R.id.loadimage);
		textTargetUri = (TextView) findViewById(R.id.targeturi);
		targetImage = (ImageView) findViewById(R.id.targetimage);

		buttonLoadImage.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Uri targetUri = data.getData();
			textTargetUri.setText(targetUri.toString());

			Toast.makeText(
					getApplicationContext(),
					"ImageView: " + targetImage.getWidth() + " x "
							+ targetImage.getHeight(), Toast.LENGTH_LONG)
					.show();

			Bitmap bitmap;
			bitmap = decodeSampledBitmapFromUri(targetUri,
					targetImage.getWidth(), targetImage.getHeight());

			if (bitmap == null) {
				Toast.makeText(getApplicationContext(),
						"the image data could not be decoded",
						Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(
						getApplicationContext(),
						"Decoded Bitmap: " + bitmap.getWidth() + " x "
								+ bitmap.getHeight(), Toast.LENGTH_LONG).show();
				targetImage.setImageBitmap(bitmap);
			}
		}
	}

	/*
	 * How to "Loading Large Bitmaps Efficiently"? Refer:
	 * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
	 */

	public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth,
			int reqHeight) {

		Bitmap bm = null;

		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}

		return bm;
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

}
