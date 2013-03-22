package com.example.bluetoothlist;

import java.util.Set;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListPairedDevicesActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ArrayAdapter<String> btArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter
				.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String deviceBTName = device.getName();
				String deviceBTMajorClass = getBTMajorDeviceClass(device
						.getBluetoothClass().getMajorDeviceClass());
				btArrayAdapter.add(deviceBTName + "\n" + deviceBTMajorClass);
			}
		}
		setListAdapter(btArrayAdapter);

	}

	private String getBTMajorDeviceClass(int major) {
		switch (major) {
		case BluetoothClass.Device.Major.AUDIO_VIDEO:
			return "AUDIO_VIDEO";
		case BluetoothClass.Device.Major.COMPUTER:
			return "COMPUTER";
		case BluetoothClass.Device.Major.HEALTH:
			return "HEALTH";
		case BluetoothClass.Device.Major.IMAGING:
			return "IMAGING";
		case BluetoothClass.Device.Major.MISC:
			return "MISC";
		case BluetoothClass.Device.Major.NETWORKING:
			return "NETWORKING";
		case BluetoothClass.Device.Major.PERIPHERAL:
			return "PERIPHERAL";
		case BluetoothClass.Device.Major.PHONE:
			return "PHONE";
		case BluetoothClass.Device.Major.TOY:
			return "TOY";
		case BluetoothClass.Device.Major.UNCATEGORIZED:
			return "UNCATEGORIZED";
		case BluetoothClass.Device.Major.WEARABLE:
			return "AUDIO_VIDEO";
		default:
			return "unknown!";
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
