package com.example.firstlogin;

import java.io.IOException;
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

public class MainActivity extends Activity implements SessionListener {

	private Session session = new Session();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			session.login("thaikkk10", "55233241");
			session.addSessionListener(this);

			String target = "rain_star_freeze_1988";
			String message = "Hello";
			session.sendBuzz(target);
			session.sendMessage(target, message);


		} catch (AccountLockedException e) {
			System.out.println("AccountLockedException: ");
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException: ");
		} catch (LoginRefusedException e) {
			System.out.println("LoginRefusedException: ");
		} catch (FailedLoginException e) {
			System.out.println("FailedLoginException: ");
		} catch (IOException e) {
			System.out.println("IOException: ");
			e.printStackTrace();
		}

	}

	/*
	 * this is my listener method it listen for YM message request
	 */
	@Override
	public void dispatch(FireEvent fe) {

		ServiceType type = fe.getType();
		SessionEvent sessionEvent = fe.getEvent();
		System.out.println("Type: " + type);
		System.out.println("ServiceType.MESSAGE: " + ServiceType.MESSAGE);
		if (type == ServiceType.MESSAGE) {
			try {
				// log request message

				// give an automatic response
				session.sendMessage(sessionEvent.getFrom(),
						"hi, you are sending " + sessionEvent.getMessage());
			} catch (Exception e) {
			}
		}
	}
}
