package com.example.androidbrowserwithvisiblegoneurlbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class MainActivity extends Activity {

	final int MENU_VISIBILITY = 0;
	final int MENU_ABOUT = 1;
	final int MENU_EXIT = 2;
	final int MENU_BACKFORD = 3;
	final int MENU_RELOAD = 4;
	final int MENU_FORWARD = 5;

	WebView myBrowser;
	TableLayout myUrlBar;
	EditText gotoUrl;
	Button myUrlButton;
	WebView webview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String myURL = "http://www.google.com";
		myBrowser = (WebView) findViewById(R.id.mybrowser);
 
		myUrlBar = (TableLayout) findViewById(R.id.UrlEntry);
		gotoUrl = (EditText) findViewById(R.id.goUrl);
		myUrlButton = (Button) findViewById(R.id.goUrlButton);
		myUrlButton.setOnClickListener(myUrlButtonOnClickListener);

		/*
		 * By default Javascript is turned off, it can be enabled by this line.
		 */
		myBrowser.getSettings().setJavaScriptEnabled(true);
		myBrowser.setWebViewClient(new WebViewClient());

		myBrowser.loadUrl(myURL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_VISIBILITY, 0, R.string.str_URLbar);
		menu.add(0, MENU_ABOUT, 0, R.string.str_About);
		menu.add(0, MENU_EXIT, 0, R.string.str_Exit);
		menu.add(0, MENU_BACKFORD, 0, R.string.str_Backward);
		menu.add(0, MENU_RELOAD, 0, R.string.str_Reload);
		menu.add(0, MENU_FORWARD, 0, R.string.str_Forward);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case MENU_VISIBILITY:
			ToggleGotoVisibility();
			break;
		case MENU_ABOUT:
			openAboutDialog();
			break;
		case MENU_EXIT:
			openExitDialog();
			break;
		case MENU_BACKFORD:
			if (myBrowser.canGoBack())
				myBrowser.goBack();
			break;
		case MENU_RELOAD:
			myBrowser.reload();
			break;
		case MENU_FORWARD:
			if (myBrowser.canGoForward())
				myBrowser.goForward();
			break;
		}
		return true;
	}

	void ToggleGotoVisibility() {
		if (myUrlBar.getVisibility() == View.GONE) {
			myUrlBar.setVisibility(View.VISIBLE);
		} else {
			myUrlBar.setVisibility(View.GONE);
		}
	}

	private void openAboutDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.str_About)
				.setMessage(R.string.str_about_message)
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						}).show();
	}

	private void openExitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.str_Exit)
				.setMessage(R.string.str_exit_message)
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						})
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								finish();
							}
						}).show();
	}

	private Button.OnClickListener myUrlButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			myBrowser.loadUrl(gotoUrl.getText().toString());
		}

	};

}
