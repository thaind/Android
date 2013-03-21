package com.example.readxmlresources;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView myXmlContent = (TextView) findViewById(R.id.my_xml);
		String stringXmlContent;
		try {
			stringXmlContent = getEventsFromAnXML();
			myXmlContent.setText(stringXmlContent);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getEventsFromAnXML() throws XmlPullParserException,
			IOException {
		StringBuffer stringBuffer = new StringBuffer();
		Resources res = getResources();
		XmlResourceParser xpp = res.getXml(R.xml.myxml);
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				stringBuffer.append("--- Start XML ---");
			} else if (eventType == XmlPullParser.START_TAG) {
				stringBuffer.append("\nSTART_TAG: " + xpp.getName());
			} else if (eventType == XmlPullParser.END_TAG) {
				stringBuffer.append("\nEND_TAG: " + xpp.getName());
			} else if (eventType == XmlPullParser.TEXT) {
				stringBuffer.append("\nTEXT: " + xpp.getText());
			}
			eventType = xpp.next();
		}
		stringBuffer.append("\n--- End XML ---");
		return stringBuffer.toString();
	}

}
