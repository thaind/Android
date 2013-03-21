package com.exercise.AndroidInfo;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidInfoOsVersion extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel);
        
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(backOnClickListener);
        
        TextView OSinfo = (TextView) findViewById(R.id.info);
        OSinfo.setText(ReadOSinfo());
        
    }
    
    private String ReadOSinfo()
    {
    	ProcessBuilder cmd;
    	String result="";
    	
    	try{
    		String[] args = {"/system/bin/cat", "/proc/version"};
    		cmd = new ProcessBuilder(args);
    		
    		Process process = cmd.start();
    		InputStream in = process.getInputStream();
    		byte[] re = new byte[1024];
    		while(in.read(re) != -1){
    			System.out.println(new String(re));
    			result = result + new String(re);
    		}
    		in.close();
    	} catch(IOException ex){
    		ex.printStackTrace();
    	}
    	return result;
    }
    
    private Button.OnClickListener backOnClickListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
            intent.setClass(AndroidInfoOsVersion.this, AndroidInfo.class);
            
            startActivity(intent);
            finish();
		}
    	
    };
}
