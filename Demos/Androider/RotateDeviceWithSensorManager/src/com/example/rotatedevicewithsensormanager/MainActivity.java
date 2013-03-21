package com.example.rotatedevicewithsensormanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView textviewAzimuth, textviewPitch, textviewRoll;
	private static SensorManager mySensorManager;
	private boolean sersorrunning;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textviewAzimuth = (TextView) findViewById(R.id.textazimuth);
		textviewPitch = (TextView) findViewById(R.id.textpitch);
		textviewRoll = (TextView) findViewById(R.id.textroll);

		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> mySensors = mySensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);

		if (mySensors.size() > 0) {
			mySensorManager.registerListener(mySensorEventListener,
					mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			sersorrunning = true;
			Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG)
					.show();
			sersorrunning = false;
			finish();
		}

	}

	private SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub

			textviewAzimuth.setText("Azimuth: "
					+ String.valueOf(event.values[0]));
			textviewPitch.setText("Pitch: " + String.valueOf(event.values[1]));
			textviewRoll.setText("Roll: " + String.valueOf(event.values[2]));

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (sersorrunning) {
			mySensorManager.unregisterListener(mySensorEventListener);
			Toast.makeText(MainActivity.this,
					"unregisterListener", Toast.LENGTH_SHORT).show();
		}
	}

}
