package com.example.rssreader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String FEED_URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
	String streamTitle = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView result = (TextView) findViewById(R.id.result);

		try {
			URL rssUrl = new URL(FEED_URL);
			SAXParserFactory mySAXParserFactory = SAXParserFactory
					.newInstance();
			SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
			XMLReader myXMLReader = mySAXParser.getXMLReader();
			RSSHandler myRSSHandler = new RSSHandler();
			myXMLReader.setContentHandler(myRSSHandler);
			InputSource myInputSource = new InputSource(rssUrl.openStream());
			myXMLReader.parse(myInputSource);

			result.setText(streamTitle);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setText("Cannot connect RSS!");
		}

	}

	private class RSSHandler extends DefaultHandler {
		final int stateUnknown = 0;
		final int stateTitle = 1;
		int state = stateUnknown;

		int numberOfTitle = 0;
		String strTitle = "";
		String strElement = "";

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			strTitle = "--- Start Document ---\n";
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			strTitle += "--- End Document ---";
			streamTitle = "Number Of Title: " + String.valueOf(numberOfTitle)
					+ "\n" + strTitle;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			if (localName.equalsIgnoreCase("title")) {
				state = stateTitle;
				strElement = "Title: ";
				numberOfTitle++;
			} else {
				state = stateUnknown;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			if (localName.equalsIgnoreCase("title")) {
				strTitle += strElement + "\n";
			}
			state = stateUnknown;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			String strCharacters = new String(ch, start, length);
			if (state == stateTitle) {
				strElement += strCharacters;
			}
		}

	}

}
