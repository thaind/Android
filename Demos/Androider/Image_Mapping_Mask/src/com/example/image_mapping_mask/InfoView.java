package com.example.image_mapping_mask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class InfoView extends View {
	String info = "";
	float x = 0; // init value
	float y = 0; // init value
	int color = Color.BLACK;

	public InfoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(2);
		paint.setTextSize(30);

		canvas.drawLine(x - 10, y, x + 10, y, paint);
		canvas.drawLine(x, y - 10, x, y + 10, paint);
		canvas.drawText(info, x, y, paint);

	}

	public void updateInfo(String t_info, float t_x, float t_y, int t_c) {
		info = t_info;
		x = t_x;
		y = t_y;
		color = t_c;
		invalidate();
	}

	public void clearInfo() {
		info = "";
		x = 0;
		y = 0;
		invalidate();
	}
}
