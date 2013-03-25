package com.example.xml_readfile;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView result = (TextView) findViewById(R.id.result);

		String XmlParseResult = getXmlEvent();
		result.setText(XmlParseResult);
	}

	private String getXmlEvent() {
		String xmlResult = "";

		// Normally, the XML files should be placed under /res/xml/ folder
		// XmlResourceParser xmlResourceParser =
		// getResources().getXml(R.xml.xxx);
		XmlResourceParser xmlResourceParser = getResources().getXml(
				R.layout.activity_main);

		try {
			int eventType;
			do {
				xmlResourceParser.next();
				eventType = xmlResourceParser.getEventType();

				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					xmlResult += "START_DOCUMENT\n";
					break;
				case XmlPullParser.END_DOCUMENT:
					xmlResult += "END_DOCUMENT\n";
					break;
				case XmlPullParser.START_TAG:
					xmlResult += "START_TAG: " + xmlResourceParser.getName()
							+ "\n";
					break;
				case XmlPullParser.END_TAG:
					xmlResult += "END_TAG: " + xmlResourceParser.getName()
							+ "\n";
					break;
				case XmlPullParser.TEXT:
					xmlResult += "TEXT: " + xmlResourceParser.getText() + "\n";
					break;
				}

			} while (eventType != XmlPullParser.END_DOCUMENT);

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xmlResult;
	}

}
