package com.example.cachingbitmapswithlrucache;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	public class GalleryBaseAdapter extends BaseAdapter {

		ArrayList<String> GalleryFileList;
		Context context;

		GalleryBaseAdapter(Context cont) {
			context = cont;
			GalleryFileList = new ArrayList<String>();
		}

		@Override
		public int getCount() {
			return GalleryFileList.size();
		}

		@Override
		public Object getItem(int position) {
			return GalleryFileList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Bitmap bm = decodeSampledBitmapFromUri(
					GalleryFileList.get(position), 200, 200);
			LinearLayout layout = new LinearLayout(context);
			layout.setLayoutParams(new Gallery.LayoutParams(250, 250));
			layout.setGravity(Gravity.CENTER);

			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageBitmap(bm);

			layout.addView(imageView);
			return layout;
		}

		public void add(String newitem) {
			GalleryFileList.add(newitem);
		}

	}

	public class CacheGalleryBaseAdapter extends GalleryBaseAdapter {

		CacheGalleryBaseAdapter(Context cont) {
			super(cont);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = new ImageView(context);

			// ---
			// Use the path as the key to LruCache
			final String imageKey = GalleryFileList.get(position);

			final Bitmap bm = getBitmapFromMemCache(imageKey);
			if (bm == null) {
				BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				task.execute(imageKey);
			}

			LinearLayout layout = new LinearLayout(context);
			layout.setLayoutParams(new Gallery.LayoutParams(250, 250));
			layout.setGravity(Gravity.CENTER);

			imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageBitmap(bm);

			layout.addView(imageView);
			return layout;

		}

	}

	GalleryBaseAdapter myGalleryBaseAdapter;
	CacheGalleryBaseAdapter myCacheGalleryBaseAdapter;
	Gallery myPhotoGallery, myFastGallery;

	private LruCache<String, Bitmap> mMemoryCache;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myPhotoGallery = (Gallery) findViewById(R.id.photogallery);
		myFastGallery = (Gallery) findViewById(R.id.fastgallery);

		myGalleryBaseAdapter = new GalleryBaseAdapter(this);
		myCacheGalleryBaseAdapter = new CacheGalleryBaseAdapter(this);

		String ExternalStorageDirectoryPath = Environment
				.getExternalStorageDirectory().getAbsolutePath();

		String targetPath = ExternalStorageDirectoryPath + "/test/";

		File targetDirector = new File(targetPath);

		File[] files = targetDirector.listFiles();
		for (File file : files) {
			myGalleryBaseAdapter.add(file.getPath());
			myCacheGalleryBaseAdapter.add(file.getPath());
		}

		myPhotoGallery.setAdapter(myGalleryBaseAdapter);
		myFastGallery.setAdapter(myCacheGalleryBaseAdapter);

		// Get memory class of this device, exceeding this amount will throw an
		// OutOfMemory exception.
		final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = 1024 * 1024 * memClass / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number
				// of items.
				return bitmap.getByteCount();
			}

		};
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			final Bitmap bitmap = decodeSampledBitmapFromUri(params[0], 200,
					200);
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = (ImageView) imageViewReference
						.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}

	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return (Bitmap) mMemoryCache.get(key);
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

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
