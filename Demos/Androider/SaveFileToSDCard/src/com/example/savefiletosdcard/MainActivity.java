package com.example.savefiletosdcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	String image_URL = "http://chart.apis.google.com/chart?chs=200x200&cht=qr&chl=http%3A%2F%2Fandroid-er.blogspot.com%2F";

	String extStorageDirectory;

	Bitmap bm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonSave = (Button) findViewById(R.id.save);

		ImageView bmImage = (ImageView) findViewById(R.id.image);
		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		bm = LoadImage(image_URL, bmOptions);
		bmImage.setImageBitmap(bm);

		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		buttonSave.setText("Save to " + extStorageDirectory + "/qr.PNG");
		buttonSave.setOnClickListener(buttonSaveOnClickListener);
	}

	private Bitmap LoadImage(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e1) {
		}
		return bitmap;
	}

	private InputStream OpenHttpConnection(String strURL) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
		}
		return inputStream;
	}

	Button.OnClickListener buttonSaveOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			OutputStream outStream = null;
			File file = new File(extStorageDirectory, "er.PNG");
			try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();

				Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG)
						.show();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(MainActivity.this, e.toString(),
						Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(MainActivity.this, e.toString(),
						Toast.LENGTH_LONG).show();
			}

		}

	};

}
