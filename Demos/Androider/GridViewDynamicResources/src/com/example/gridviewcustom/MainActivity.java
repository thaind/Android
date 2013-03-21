package com.example.gridviewcustom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final int resourceStart = 17301504;
	final int resourceEnd = 17301655;
	final int ResourceLength = resourceEnd - resourceStart + 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(gridviewOnItemClickListener);
	}

	private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String strResource = arg0.getAdapter().getItem(arg2).toString()
					+ " "
					+ Resources.getSystem().getResourceName(
							arg2 + resourceStart);
			Toast.makeText(MainActivity.this, strResource,
					Toast.LENGTH_LONG).show();
		}
	};

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
			initResourceID();
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return mThumbIds[position];
		}

		public long getItemId(int position) {
			return position;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				// if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

		private int[] mThumbIds = new int[ResourceLength];

		private void initResourceID() {
			for (int i = 0; i < ResourceLength; i++) {
				mThumbIds[i] = i + resourceStart;
			}
		}
	}
}
