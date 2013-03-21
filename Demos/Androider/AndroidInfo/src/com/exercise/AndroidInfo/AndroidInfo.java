package com.exercise.AndroidInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AndroidInfo extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonOsVersion = (Button) findViewById(R.id.OsVersion);
        buttonOsVersion.setOnClickListener(OsVersionOnClickListener);
        Button buttonSysInfo = (Button) findViewById(R.id.SysInfo);
        buttonSysInfo.setOnClickListener(SysInfoOnClickListener);
        Button buttonCpuInfo = (Button) findViewById(R.id.CpuInfo);
        buttonCpuInfo.setOnClickListener(CpuInfoOnClickListener);
        Button buttonScreenInfo = (Button) findViewById(R.id.ScreenInfo);
        buttonScreenInfo.setOnClickListener(ScreenInfoOnClickListener);
        Button buttonMemoryInfo = (Button) findViewById(R.id.MemoryInfo);
        buttonMemoryInfo.setOnClickListener(MemoryInfoOnClickListener);
        Button buttonDiskInfo = (Button) findViewById(R.id.DiskInfo);
        buttonDiskInfo.setOnClickListener(DiskInfoOnClickListener);
        
        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(exitOnClickListener);
    }
    
    private Button.OnClickListener OsVersionOnClickListener =
        new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            SwitchClass(AndroidInfoOsVersion.class);
			}

    };
    
    private Button.OnClickListener SysInfoOnClickListener =
        new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            SwitchClass(AndroidInfoSys.class);
			}

    };
    
    private Button.OnClickListener CpuInfoOnClickListener =
        new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            SwitchClass(AndroidInfoCpu.class);
			}

    };
    
    private Button.OnClickListener ScreenInfoOnClickListener =
        new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            SwitchClass(AndroidInfoScreen.class);
			}

    };
    
    private Button.OnClickListener MemoryInfoOnClickListener =
        new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            SwitchClass(AndroidInfoMemory.class);
			}

    };
    
    private Button.OnClickListener DiskInfoOnClickListener =
        new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            SwitchClass(AndroidInfoDisk.class);
			}

    };
    
    private void SwitchClass(Class<?> targetClass)
    {
    	Intent intent = new Intent();
    	intent.setClass(AndroidInfo.this, targetClass);
    	startActivity(intent);
    	finish();
	}
    
    private Button.OnClickListener exitOnClickListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            finish();
		}
    	
    };
}