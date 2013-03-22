package com.example.customviewmultitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchView extends View {

	private final int TOUCH_COUNTER = 2;

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	float[] x = new float[TOUCH_COUNTER];
	float[] y = new float[TOUCH_COUNTER];
	boolean[] isTouch = new boolean[TOUCH_COUNTER];

	public MultiTouchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MultiTouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MultiTouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (isTouch[0]) {
			paint.setStrokeWidth(1);
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(x[0], y[0], 75f, paint);
		}
		if (isTouch[1]) {
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLUE);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(x[1], y[1], 75f, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		// TODO Auto-generated method stub

		int pointerIndex = ((motionEvent.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT);
		int pointerId = motionEvent.getPointerId(pointerIndex);
		int action = (motionEvent.getAction() & MotionEvent.ACTION_MASK);
		int pointCnt = motionEvent.getPointerCount();

		if (pointCnt <= TOUCH_COUNTER) {
			if (pointerIndex <= TOUCH_COUNTER - 1) {
				for (int i = 0; i < pointCnt; i++) {
					int id = motionEvent.getPointerId(i);
					x[id] = (int) motionEvent.getX(i);
					y[id] = (int) motionEvent.getY(i);
				}

				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
				case MotionEvent.ACTION_MOVE:
					isTouch[pointerId] = true;
					break;
				default:
					isTouch[pointerId] = false;
				}
			}
		}
		invalidate();
		return true;
	}

}
