package com.example.drag_and_drop;

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
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button button1, button2, button3, button4, buttonTarget;
	Button dragSourceButton;
	TextView comments;

	String commentMsg;

	MyDragEventListener myDragEventListener = new MyDragEventListener();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		buttonTarget = (Button) findViewById(R.id.buttonTarget);
		comments = (TextView) findViewById(R.id.comments);

		// Create and set the tags for the Buttons
		final String BUTTON1_TAG = "button1";
		final String BUTTON2_TAG = "button2";
		final String BUTTON3_TAG = "button3";
		final String BUTTON4_TAG = "button4";
		button1.setTag(BUTTON1_TAG);
		button2.setTag(BUTTON2_TAG);
		button3.setTag(BUTTON3_TAG);
		button4.setTag(BUTTON4_TAG);

		button1.setOnLongClickListener(sourceButtonsLongClickListener);
		button2.setOnLongClickListener(sourceButtonsLongClickListener);
		button3.setOnLongClickListener(sourceButtonsLongClickListener);
		button4.setOnLongClickListener(sourceButtonsLongClickListener);

		button1.setOnDragListener(myDragEventListener);
		button2.setOnDragListener(myDragEventListener);
		button3.setOnDragListener(myDragEventListener);
		button4.setOnDragListener(myDragEventListener);
		buttonTarget.setOnDragListener(myDragEventListener);
	}

	Button.OnLongClickListener sourceButtonsLongClickListener = new Button.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
			String[] clipDescription = { ClipDescription.MIMETYPE_TEXT_PLAIN };
			ClipData dragData = new ClipData((CharSequence) v.getTag(),
					clipDescription, item);
			DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

			v.startDrag(dragData, myShadow, null, 0);

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
					commentMsg += ((Button) v).getText()
							+ " : ACTION_DRAG_STARTED accepted.\n";
					comments.setText(commentMsg);
					return true; // Accept
				} else {
					commentMsg += ((Button) v).getText()
							+ " : ACTION_DRAG_STARTED rejected.\n";
					comments.setText(commentMsg);
					return false; // reject
				}
			case DragEvent.ACTION_DRAG_ENTERED:
				commentMsg += ((Button) v).getText()
						+ " : ACTION_DRAG_ENTERED.\n";
				comments.setText(commentMsg);
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:
				commentMsg += ((Button) v).getText()
						+ " : ACTION_DRAG_LOCATION - " + event.getX() + " : "
						+ event.getY() + "\n";
				comments.setText(commentMsg);
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				commentMsg += ((Button) v).getText()
						+ " : ACTION_DRAG_EXITED.\n";
				comments.setText(commentMsg);
				return true;
			case DragEvent.ACTION_DROP:
				// Gets the item containing the dragged data
				ClipData.Item item = event.getClipData().getItemAt(0);

				commentMsg += ((Button) v).getText() + " : ACTION_DROP"
						+ " - from " + item.getText().toString() + "\n";
				comments.setText(commentMsg);

				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				if (event.getResult()) {
					commentMsg += ((Button) v).getText()
							+ " : ACTION_DRAG_ENDED - success.\n";
					comments.setText(commentMsg);
				} else {
					commentMsg += ((Button) v).getText()
							+ " : ACTION_DRAG_ENDED - fail.\n";
					comments.setText(commentMsg);
				}
				;
				return true;
			default: // unknown case
				commentMsg += ((Button) v).getText() + " : UNKNOWN !!!\n";
				comments.setText(commentMsg);
				return false;

			}

		}
	}

}