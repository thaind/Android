package com.example.drawroundrect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(new MyView(this));

	}

	public class MyView extends View {

		public MyView(Context context) {

			super(context);

			// TODO Auto-generated constructor stub

		}

		@Override
		protected void onDraw(Canvas canvas) {

			// TODO Auto-generated method stub

			super.onDraw(canvas);

			float width = (float) getWidth();

			float height = (float) getHeight();

			float radius;

			if (width > height) {

				radius = height / 4;

			} else {

				radius = width / 4;

			}

			Paint paint = new Paint();

			paint.setColor(Color.GREEN);

			paint.setStrokeWidth(5);

			paint.setStyle(Paint.Style.FILL);

			float center_x, center_y;

			center_x = width / 4;

			center_y = height / 4;

			final RectF rect = new RectF();

			rect.set(center_x - radius,

			center_y - radius,

			center_x + radius,

			center_y + radius);

			canvas.drawRoundRect(rect, 50, 50, paint);

			paint.setStyle(Paint.Style.STROKE);

			center_x = width / 2;

			center_y = height / 2;

			rect.set(center_x - radius,

			center_y - radius,

			center_x + radius,

			center_y + radius);

			canvas.drawRoundRect(rect, 100, 50, paint);

			paint.setStyle(Paint.Style.STROKE);

			center_x = width * 3 / 4;

			center_y = height * 3 / 4;

			rect.set(center_x - radius,

			center_y - radius,

			center_x + radius,

			center_y + radius);

			canvas.drawRoundRect(rect, 100, 100, paint);

		}

	}

}
