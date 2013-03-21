package com.exercise.HelloGallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class HelloGallery extends Activity {
	private List<String> tFileList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (isSDCardAvailable()) {
			final Gallery g = (Gallery) findViewById(R.id.gallery);

			final List<String> SD = ReadSDCard();
			g.setAdapter(new ImageAdapter(this, SD));

			g.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Toast.makeText(getApplicationContext(),
							SD.get(position).toString(), Toast.LENGTH_SHORT)
							.show();
				}
			});
		} else {
			Toast.makeText(this, "SD card Unavailable", Toast.LENGTH_LONG)
					.show();
		}

	}

	private boolean isSDCardAvailable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageAvailable;
	}

	private List<String> ReadSDCard() {
		tFileList = new ArrayList<String>();
		try {
			// It have to be matched with the directory in SDCard
			final String strPathImagesOnSDCard = "/sdcard/Pictures/";
			File f = new File(strPathImagesOnSDCard);

			File[] files = f.listFiles();

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String curFile = file.getPath();
				String ext = curFile.substring(curFile.lastIndexOf(".") + 1,
						curFile.length()).toLowerCase();
				if (ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {
					tFileList.add(file.getPath());
				}

			}

		} catch (Exception e) {
			Log.i("NDT", "Đọc file không thành công!");
		}
		return tFileList;
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		private List<String> FileList;

		public ImageAdapter(Context c, List<String> fList) {
			mContext = c;
			FileList = fList;
			TypedArray a = obtainStyledAttributes(R.styleable.Theme);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.Theme_android_galleryItemBackground, 0);
			a.recycle();
		}

		public int getCount() {
			return FileList.size();
		}

		public Object getItem(int position) {
			return FileList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);

			Bitmap bm = BitmapFactory.decodeFile(FileList.get(position)
					.toString());
			i.setImageBitmap(bm);

			i.setLayoutParams(new Gallery.LayoutParams(150, 100));
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setBackgroundResource(mGalleryItemBackground);
			return i;
		}
	}
}