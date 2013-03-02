package com.example.simpleyahoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.openymsg.network.AccountLockedException;
import org.openymsg.network.FailedLoginException;
import org.openymsg.network.FireEvent;
import org.openymsg.network.LoginRefusedException;
import org.openymsg.network.ServiceType;
import org.openymsg.network.Session;
import org.openymsg.network.event.SessionEvent;
import org.openymsg.network.event.SessionListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements SessionListener {

	private Logger logger = Logger.getAnonymousLogger();
	private Session session = new Session();
	Button sendButton;
	boolean isLoginsuccess;
	ListView resultTextView;
	EditText editText;
	Handler handler;
	ArrayList<String> replymessage;
	String reply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		replymessage = new ArrayList<String>();
		try {
			session.login("thaikkk10", "55233241");
		} catch (AccountLockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginRefusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailedLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.addSessionListener(this);
		sendButton = (Button) findViewById(R.id.button);
		resultTextView = (ListView) findViewById(R.id.result);
		editText = (EditText) findViewById(R.id.input);
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLogin();
			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0) {
					replymessage.add(reply);
					setListAdapter();
				}
			}
		};

	}

	private void doLogin() {
		try {
			// insert your yahoo id
			// as for this example, im using my yahoo ID "dombaganas"

			session.sendMessage("rain_star_freeze_1988", editText.getText().toString());

		} catch (Exception e) {
			Log.e(e.getMessage(), e.getMessage());

		}
	}

	@Override
	public void dispatch(FireEvent fe) {
		// TODO Auto-generated method stub
		ServiceType type = fe.getType();
		SessionEvent sessionEvent = fe.getEvent();

		if (type == ServiceType.MESSAGE) {
			try {
				// log request message
				reply = sessionEvent.getMessage();
				Log.i("message", "message from " + sessionEvent.getFrom()
						+ " \nmessage " + sessionEvent.getMessage());

				// give an automatic response
				// session.sendMessage(sessionEvent.getFrom(),
				// "hi, you are sending " + sessionEvent.getMessage());
				// session.
				handler.sendEmptyMessage(0);
			} catch (Exception e) {
				Log.e(e.getMessage(), e.getMessage());
			}
		}

	}

	public void setListAdapter() {
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.activity_main, replymessage);
		resultTextView.setAdapter(arrayAdapter);
		resultTextView.setSelection(replymessage.size() - 1);

	}

}
