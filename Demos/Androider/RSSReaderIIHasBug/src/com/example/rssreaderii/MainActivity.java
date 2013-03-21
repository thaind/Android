package com.example.rssreaderii;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	private RSSFeed myRssFeed = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			URL rssUrl = new URL(
					"http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest");
			SAXParserFactory mySAXParserFactory = SAXParserFactory
					.newInstance();
			SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
			XMLReader myXMLReader = mySAXParser.getXMLReader();
			RSSHandler myRSSHandler = new RSSHandler();
			myXMLReader.setContentHandler(myRSSHandler);
			InputSource myInputSource = new InputSource(rssUrl.openStream());
			myXMLReader.parse(myInputSource);

			myRssFeed = myRSSHandler.getFeed();
			if (myRssFeed != null) {
				TextView feedTitle = (TextView) findViewById(R.id.feedtitle);
				TextView feedDescribtion = (TextView) findViewById(R.id.feeddescribtion);
				TextView feedPubdate = (TextView) findViewById(R.id.feedpubdate);
				TextView feedLink = (TextView) findViewById(R.id.feedlink);
				if (myRssFeed.getTitle() != null)
					feedTitle.setText(myRssFeed.getTitle());
				if (myRssFeed.getDescription() != null)
					feedDescribtion.setText(myRssFeed.getDescription());
				if (myRssFeed.getPubdate() != null)
					feedPubdate.setText(myRssFeed.getPubdate());
				if (myRssFeed.getLink() != null)
					feedLink.setText(myRssFeed.getLink());

				ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(this,
						android.R.layout.simple_list_item_1,
						myRssFeed.getList());

				setListAdapter(adapter);
			} else {
				Log.i("NDT", "myRssFeed == null");
			}
		} catch (MalformedURLException e) {
			Log.i("NDT", "MalformedURLException");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.i("NDT", "ParserConfigurationException");
			e.printStackTrace();
		} catch (SAXException e) {
			Log.i("NDT", "SAXException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("NDT", "IOException");
			e.printStackTrace();
		} catch (Exception e) {
			Log.i("NDT", "Exception");
			e.printStackTrace();
		}

	}
}
