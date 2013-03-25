package com.example.drawonview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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

			int width = getWidth();

			int height = getHeight();

			int radius;

			if (width > height) {

				radius = height / 2;

			} else {

				radius = width / 2;

			}

			Paint paint = new Paint();

			paint.setStyle(Style.FILL);

			paint.setColor(Color.RED);

			canvas.drawPaint(paint);

			paint.setColor(Color.BLUE);

			canvas.drawCircle(width / 2, height / 2, radius, paint);

		}

	}

}
