package com.example.generateamirrorimageusingmatrix.postconcat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private final String imageInSD = "/sdcard/er.png";

	CheckBox enableMirror;
	ImageView myImageView;

	Bitmap bitmap;
	int bmpWidth, bmpHeight;

	Matrix matrixMirrorY;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		enableMirror = (CheckBox) findViewById(R.id.enablemirror);
		myImageView = (ImageView) findViewById(R.id.imageview);

		enableMirror
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						drawMatrix();
					}
				});

		bitmap = BitmapFactory.decodeFile(imageInSD);
		bmpWidth = bitmap.getWidth();
		bmpHeight = bitmap.getHeight();

		initMirrorMatrix();

		drawMatrix();
	}

	private void initMirrorMatrix() {
		float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
		matrixMirrorY = new Matrix();
		matrixMirrorY.setValues(mirrorY);
	}

	private void drawMatrix() {
		if (enableMirror.isChecked()) {
			Matrix matrix = new Matrix();
			matrix.postConcat(matrixMirrorY);

			Bitmap mirrorBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
					bmpHeight, matrix, true);
			myImageView.setImageBitmap(mirrorBitmap);
		} else {
			myImageView.setImageBitmap(bitmap);
		}

	}

}
