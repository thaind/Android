package com.example.simplelistviewusingsimpleadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<Map<String, CharSequence>> asciiPair = new ArrayList<Map<String, CharSequence>>();

		Map<String, CharSequence> asciidata;

		for (int i = 32; i <= 126; i++) {
			asciidata = new HashMap<String, CharSequence>();

			String strCode = String.valueOf(i);

			byte[] data = { (byte) i };
			CharSequence strSymbol = EncodingUtils.getAsciiString(data);

			asciidata.put("Code", strCode);
			asciidata.put("Symbol", strSymbol);
			asciiPair.add(asciidata);
		}

		SimpleAdapter AsciiAdapter = new SimpleAdapter(this, asciiPair,
				R.layout.activity_main, new String[] { "Code", "Symbol" },
				new int[] { R.id.code, R.id.symbol });

		setListAdapter(AsciiAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		String myAscii = l.getItemAtPosition(position).toString();
		Toast.makeText(this, myAscii, Toast.LENGTH_LONG).show();
	}

}
