package com.example.drag_and_drop_listviews;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	LinearLayout targetLayout;
	ListView listSource, listTarget;
	TextView comments;

	String commentMsg;

	MyDragEventListener myDragEventListener = new MyDragEventListener();

	String[] month = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" };

	List<String> droppedList;
	ArrayAdapter<String> droppedAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		targetLayout = (LinearLayout) findViewById(R.id.targetlayout);
		listSource = (ListView) findViewById(R.id.sourcelist);
		listTarget = (ListView) findViewById(R.id.targetlist);
		comments = (TextView) findViewById(R.id.comments);

		// Create and set the tags for the Buttons
		final String SOURCELIST_TAG = "listSource";
		final String TARGETLIST_TAG = "listTarget";
		final String TARGETLAYOUT_TAG = "targetLayout";

		listSource.setTag(SOURCELIST_TAG);
		listTarget.setTag(TARGETLIST_TAG);
		targetLayout.setTag(TARGETLAYOUT_TAG);

		listSource.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, month));
		listSource.setOnItemLongClickListener(listSourceItemLongClickListener);

		droppedList = new ArrayList<String>();
		droppedAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, droppedList);
		listTarget.setAdapter(droppedAdapter);

		listSource.setOnDragListener(myDragEventListener);
		targetLayout.setOnDragListener(myDragEventListener);

	}

	OnItemLongClickListener listSourceItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> l, View v, int position,
				long id) {

			// Selected item is passed as item in dragData
			ClipData.Item item = new ClipData.Item(month[position]);

			String[] clipDescription = { ClipDescription.MIMETYPE_TEXT_PLAIN };
			ClipData dragData = new ClipData((CharSequence) v.getTag(),
					clipDescription, item);
			DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

			v.startDrag(dragData, // ClipData
					myShadow, // View.DragShadowBuilder
					month[position], // Object myLocalState
					0); // flags

			commentMsg = v.getTag() + " : onLongClick.\n";
			comments.setText(commentMsg);

			return true;
		}
	};

	private static class MyDragShadowBuilder extends View.DragShadowBuilder {
		private static Drawable shadow;

		public MyDragShadowBuilder(View v) {
			super(v);
			shadow = new ColorDrawable(Color.LTGRAY);
		}

		@Override
		public void onProvideShadowMetrics(Point size, Point touch) {
			int width = getView().getWidth();
			int height = getView().getHeight();

			shadow.setBounds(0, 0, width, height);
			size.set(width, height);
			touch.set(width / 2, height / 2);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			shadow.draw(canvas);
		}

	}

	protected class MyDragEventListener implements View.OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				// All involved view accept ACTION_DRAG_STARTED for
				// MIMETYPE_TEXT_PLAIN
				if (event.getClipDescription().hasMimeType(
						ClipDescription.MIMETYPE_TEXT_PLAIN)) {
					commentMsg += v.getTag()
							+ " : ACTION_DRAG_STARTED accepted.\n";
					comments.setText(commentMsg);
					return true; // Accept
				} else {
					commentMsg += v.getTag()
							+ " : ACTION_DRAG_STARTED rejected.\n";
					comments.setText(commentMsg);
					return false; // reject
				}
			case DragEvent.ACTION_DRAG_ENTERED:
				commentMsg += v.getTag() + " : ACTION_DRAG_ENTERED.\n";
				comments.setText(commentMsg);
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:
				commentMsg += v.getTag() + " : ACTION_DRAG_LOCATION - "
						+ event.getX() + " : " + event.getY() + "\n";
				comments.setText(commentMsg);
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				commentMsg += v.getTag() + " : ACTION_DRAG_EXITED.\n";
				comments.setText(commentMsg);
				return true;
			case DragEvent.ACTION_DROP:
				// Gets the item containing the dragged data
				ClipData.Item item = event.getClipData().getItemAt(0);

				commentMsg += v.getTag() + " : ACTION_DROP" + "\n";
				comments.setText(commentMsg);

				// If apply only if drop on buttonTarget
				if (v == targetLayout) {
					String droppedItem = item.getText().toString();

					commentMsg += "Dropped item - " + droppedItem + "\n";
					comments.setText(commentMsg);

					droppedList.add(droppedItem);
					droppedAdapter.notifyDataSetChanged();

					return true;
				} else {
					return false;
				}

			case DragEvent.ACTION_DRAG_ENDED:
				if (event.getResult()) {
					commentMsg += v.getTag()
							+ " : ACTION_DRAG_ENDED - success.\n";
					comments.setText(commentMsg);
				} else {
					commentMsg += v.getTag() + " : ACTION_DRAG_ENDED - fail.\n";
					comments.setText(commentMsg);
				}
				;
				return true;
			default: // unknown case
				commentMsg += v.getTag() + " : UNKNOWN !!!\n";
				comments.setText(commentMsg);
				return false;

			}
		}
	}

}
