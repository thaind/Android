package com.example.worldcountriesbooks;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditCountry extends Activity {
	
	 private long rowID; 
	 private EditText nameEt;
	 private EditText capEt;
	 private EditText codeEt;
	   
	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	      super.onCreate(savedInstanceState); 
	      setContentView(R.layout.add_country);

	      nameEt = (EditText) findViewById(R.id.nameEdit);
	      capEt = (EditText) findViewById(R.id.capEdit);
	      codeEt = (EditText) findViewById(R.id.codeEdit);
	      
	      Bundle extras = getIntent().getExtras(); 
	      
	      if (extras != null)
	      {
	         rowID = extras.getLong("row_id");
	         nameEt.setText(extras.getString("name"));  
	         capEt.setText(extras.getString("cap"));  
	         codeEt.setText(extras.getString("code"));  
	      }
	      
	      Button saveButton =(Button) findViewById(R.id.saveBtn);
	      saveButton.setOnClickListener(new OnClickListener() {
			
	    	  public void onClick(View v) 
	          {
	             if (nameEt.getText().length() != 0)
	             {
	                AsyncTask<Object, Object, Object> saveContactTask = 
	                   new AsyncTask<Object, Object, Object>() 
	                   {
	                      @Override
	                      protected Object doInBackground(Object... params) 
	                      {
	                         saveContact();
	                         return null;
	                      }
	          
	                      @Override
	                      protected void onPostExecute(Object result) 
	                      {
	                         finish();
	                      }
	                   }; 
	                  
	                saveContactTask.execute((Object[]) null); 
	             }
	             
	             else
	             {
	                AlertDialog.Builder alert = new AlertDialog.Builder(AddEditCountry.this);
	                alert.setTitle(R.string.errorTitle); 
	                alert.setMessage(R.string.errorMessage);
	                alert.setPositiveButton(R.string.errorButton, null); 
	                alert.show();
	             }
	          } 
		 });
	   }
	   
	   private void saveContact() 
	   {
	      DatabaseConnector dbConnector = new DatabaseConnector(this);

	      if (getIntent().getExtras() == null)
	      {
	    	  dbConnector.insertContact(nameEt.getText().toString(),
	    			  capEt.getText().toString(),
	    			  codeEt.getText().toString());
	      }
	      else
	      {
	         dbConnector.updateContact(rowID,
	            nameEt.getText().toString(),
	            capEt.getText().toString(), 
	            codeEt.getText().toString());
	      }
	   }
	}
