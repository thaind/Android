package com.example.getsupportedpicturesizesofandroiddevicescamera;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Spinner spinnerSupportedPictureSizes = (Spinner) findViewById(R.id.supportedpicturesizes);

		Camera camera = Camera.open();
		Camera.Parameters cameraParameters = camera.getParameters();
		List<Camera.Size> listSupportedPictureSizes = cameraParameters
				.getSupportedPictureSizes();

		List<String> listStrSupportedPictureSizes = new ArrayList<String>();

		for (int i = 0; i < listSupportedPictureSizes.size(); i++) {

			String strSize = String.valueOf(i) + " : "
					+ String.valueOf(listSupportedPictureSizes.get(i).height)
					+ " x "
					+ String.valueOf(listSupportedPictureSizes.get(i).width);
			listStrSupportedPictureSizes.add(strSize);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				listStrSupportedPictureSizes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSupportedPictureSizes.setAdapter(adapter);

		camera.release();
	}
}
