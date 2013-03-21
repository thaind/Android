package com.example.listview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	public class MyCustomAdapter extends ArrayAdapter<String> {

		Bitmap bm;
		String image_URL = "http://4.bp.blogspot.com/_C5a2qH8Y_jk/StYXDpZ9-WI/AAAAAAAAAJQ/sCgPx6jfWPU/S1600-R/android.png";

		public MyCustomAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);

			BitmapFactory.Options bmOptions;
			bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			bm = LoadImage(image_URL, bmOptions);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// return super.getView(position, convertView, parent);
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.activity_main, parent, false);
			}
			TextView label = (TextView) row.findViewById(R.id.weekofday);
			label.setText(DayOfWeek[position]);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);

			if (DayOfWeek[position].equals("Sunday")) {
				icon.setImageResource(R.drawable.icongray);
			} else {
				icon.setImageBitmap(bm);
			}

			return row;
		}

		public Bitmap LoadImage(String URL, BitmapFactory.Options options) {
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

		public InputStream OpenHttpConnection(String strURL) throws IOException {
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

	}

	String[] DayOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday", "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Monday",
			"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday",
			"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
			"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		/*
		 * setListAdapter(new ArrayAdapter<String>(this, R.layout.row,
		 * R.id.weekofday, DayOfWeek));
		 */
		setListAdapter(new MyCustomAdapter(MainActivity.this,
				R.layout.activity_main, DayOfWeek));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		// super.onListItemClick(l, v, position, id);
		String selection = l.getItemAtPosition(position).toString();
		Toast.makeText(this, selection, Toast.LENGTH_LONG).show();
	}

}