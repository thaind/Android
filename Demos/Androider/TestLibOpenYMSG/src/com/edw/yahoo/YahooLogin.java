package com.edw.yahoo;


import org.openymsg.network.FireEvent;
import org.openymsg.network.ServiceType;
import org.openymsg.network.Session;
import org.openymsg.network.event.SessionEvent;
import org.openymsg.network.event.SessionListener;

public class YahooLogin implements SessionListener {

    private Session session = new Session();

    public static void main(String[] args) { 
        YahooLogin yahooLogin = new YahooLogin();
        yahooLogin.doLogin();
    }

    public YahooLogin() {
    }

    private void doLogin() {
        try {
            // insert your yahoo id
            // as for this example, im using my yahoo ID "dombaganas"
            session.login("thaikkk10", "55233241");
            session.addSessionListener(this);

            while (true) {
                System.out.println("Hello");
                String message = "Hello";
                Thread.sleep(2000);
                // logout if message equals to "bye"
                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
                // send message to targeted yahoo id
                String target = "rain_star_freeze_1988";
                session.sendBuzz(target);
                session.sendMessage(target, message);
            }

            // logout from YM
            session.logout();
        } catch (Exception e) {

        }
    }

    /*
     *  this is my listener method
     *  it listen for YM message request
     */
    @Override
    public void dispatch(FireEvent fe) {
    
        ServiceType type = fe.getType();
        SessionEvent sessionEvent = fe.getEvent();
        System.out.println("Type: "+type);
        System.out.println("ServiceType.MESSAGE: "+ServiceType.MESSAGE);
        if (type == ServiceType.MESSAGE) {
            try {
                // log request message
            	
                // give an automatic response
                session.sendMessage(sessionEvent.getFrom(), "hi, you are sending " + sessionEvent.getMessage());
            } catch (Exception e) {
            }
        }
    }
}