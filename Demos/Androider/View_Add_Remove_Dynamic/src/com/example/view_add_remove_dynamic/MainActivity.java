package com.example.view_add_remove_dynamic;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	FrameLayout mainLayer;
	View layer1, layer2, layer3;
	CheckBox enableLayer1, enableLayer2, enableLayer3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		enableLayer1 = (CheckBox) findViewById(R.id.enlayer1);
		enableLayer2 = (CheckBox) findViewById(R.id.enlayer2);
		enableLayer3 = (CheckBox) findViewById(R.id.enlayer3);

		mainLayer = (FrameLayout) findViewById(R.id.master);

		LayoutInflater inflater = getLayoutInflater();
		layer1 = inflater.inflate(R.layout.layer_1, null);
		layer2 = inflater.inflate(R.layout.layer_2, null);
		layer3 = inflater.inflate(R.layout.layer_3, null);

		enableLayer1.setOnCheckedChangeListener(enableLayer1ChangeListener);
		enableLayer2.setOnCheckedChangeListener(enableLayer2ChangeListener);
		enableLayer3.setOnCheckedChangeListener(enableLayer3ChangeListener);

	}

	CheckBox.OnCheckedChangeListener enableLayer1ChangeListener = new CheckBox.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				mainLayer.addView(layer1);
			} else {
				mainLayer.removeView(layer1);
			}
		}
	};

	CheckBox.OnCheckedChangeListener enableLayer2ChangeListener = new CheckBox.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				mainLayer.addView(layer2);
			} else {
				mainLayer.removeView(layer2);
			}
		}
	};

	CheckBox.OnCheckedChangeListener enableLayer3ChangeListener = new CheckBox.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				mainLayer.addView(layer3);
			} else {
				mainLayer.removeView(layer3);
			}
		}
	};

}
