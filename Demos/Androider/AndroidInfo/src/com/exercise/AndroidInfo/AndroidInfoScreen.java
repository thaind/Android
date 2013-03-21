package com.exercise.AndroidInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidInfoScreen extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel);
        
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(backOnClickListener);
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        String strScreen="";
        strScreen += "Width : " + String.valueOf(dm.widthPixels) + " pixels" + "\n";
        strScreen += "Height : " + String.valueOf(dm.heightPixels) + " pixels" + "\n";
        strScreen += "The Logical Density : " + String.valueOf(dm.density) + "\n";
        strScreen += "X Dimension : " + String.valueOf(dm.xdpi) + " dot/inch" + "\n";
        strScreen += "Y Dimension : " + String.valueOf(dm.ydpi) + " dot/inch" + "\n";

        TextView mScreenSize = (TextView) findViewById(R.id.info);
        mScreenSize.setText(strScreen);
    }
    
    private Button.OnClickListener backOnClickListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
            intent.setClass(AndroidInfoScreen.this, AndroidInfo.class);
            
            startActivity(intent);
            finish();
		}
    	
    };
}