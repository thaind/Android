package com.exercise.AndroidInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidInfoSys extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel);
        
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(backOnClickListener);
        
        TextView SYSinfo = (TextView) findViewById(R.id.info);
        SYSinfo.setText(ReadSYSinfo());
    }
    
    private static StringBuffer SYSinfoBuffer;
    
    private String ReadSYSinfo()
    {
    	SYSinfoBuffer = new StringBuffer();
    	
    	getProperty("os.name", "os.name", SYSinfoBuffer);
    	getProperty("os.version", "os.version", SYSinfoBuffer);
    	
    	getProperty("java.vendor.url", "java.vendor.url", SYSinfoBuffer);
    	getProperty("java.version", "java.version", SYSinfoBuffer);
    	getProperty("java.class.path", "java.class.path", SYSinfoBuffer);
    	getProperty("java.class.version", "java.class.version", SYSinfoBuffer);
    	getProperty("java.vendor", "java.vendor", SYSinfoBuffer);
    	getProperty("java.home", "java.home", SYSinfoBuffer);
    	
    	getProperty("user.name", "user.name", SYSinfoBuffer);
    	getProperty("user.home", "user.home", SYSinfoBuffer);
    	getProperty("user.dir", "user.dir", SYSinfoBuffer);
    	
    	return SYSinfoBuffer.toString();
    }
    
    private void getProperty(String desc, String property, StringBuffer tBuffer)
    {
    	tBuffer.append(desc);
    	tBuffer.append(" : ");
    	tBuffer.append(System.getProperty(property));
    	tBuffer.append("\n");
    }
    
    private Button.OnClickListener backOnClickListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
            intent.setClass(AndroidInfoSys.this, AndroidInfo.class);
            
            startActivity(intent);
            finish();
		}
    	
    };
}
