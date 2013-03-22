package com.example.customviewwithuserinteraction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	public class MyView extends View {
		private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private float initX, initY, radius;
		private boolean drawing = false;

		public MyView(Context context) {
			super(context);
			init();
		}

		public MyView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public MyView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		private void init() {
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setColor(Color.RED);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
					MeasureSpec.getSize(heightMeasureSpec));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if (drawing) {
				canvas.drawCircle(initX, initY, radius, paint);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_MOVE) {
				float x = event.getX();
				float y = event.getY();
				radius = (float) Math.sqrt(Math.pow(x - initX, 2)
						+ Math.pow(y - initY, 2));
			} else if (action == MotionEvent.ACTION_DOWN) {
				initX = event.getX();
				initY = event.getY();
				radius = 1;
				drawing = true;
			} else if (action == MotionEvent.ACTION_UP) {
				drawing = false;
			}
			invalidate();
			return true;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		MyView myView1 = new MyView(this);

		MyView myView2 = new MyView(this);

		mainLayout.addView(myView1);
		mainLayout.addView(myView2);
		setContentView(mainLayout);
	}
}
