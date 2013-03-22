package com.example.worldcountriesbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewCountry extends Activity {
	
	   private long rowID;
	   private TextView nameTv;
	   private TextView capTv;
	   private TextView codeTv; 
	   
	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.view_country);
	      
	      setUpViews();
	      Bundle extras = getIntent().getExtras();
	      rowID = extras.getLong(CountryList.ROW_ID); 
	   }
	   
	   private void setUpViews() {
		   nameTv = (TextView) findViewById(R.id.nameText);
		   capTv = (TextView) findViewById(R.id.capText);
		   codeTv = (TextView) findViewById(R.id.codeText);
	   }

	   @Override
	   protected void onResume()
	   {
	      super.onResume();
	      new LoadContacts().execute(rowID);
	   } 
	   
	   private class LoadContacts extends AsyncTask<Long, Object, Cursor> 
	   {
	      DatabaseConnector dbConnector = new DatabaseConnector(ViewCountry.this);
	      
	      @Override
	      protected Cursor doInBackground(Long... params)
	      {
	         dbConnector.open();
	         return dbConnector.getOneContact(params[0]);
	      } 

	      @Override
	      protected void onPostExecute(Cursor result)
	      {
	         super.onPostExecute(result);
	   
	         result.moveToFirst();
	         // get the column index for each data item
	         int nameIndex = result.getColumnIndex("name");
	         int capIndex = result.getColumnIndex("cap");
	         int codeIndex = result.getColumnIndex("code");
	   
	         nameTv.setText(result.getString(nameIndex));
	         capTv.setText(result.getString(capIndex));
	         codeTv.setText(result.getString(codeIndex));
	   
	         result.close();
	         dbConnector.close();
	      }
	   } 
	   
	   
	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) 
	   {
	      super.onCreateOptionsMenu(menu);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.view_country_menu, menu);
	      return true;
	   }
	   
	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId())
	      {
	         case R.id.editItem:
	            Intent addEditContact =
	               new Intent(this, AddEditCountry.class);
	            
	            addEditContact.putExtra(CountryList.ROW_ID, rowID);
	            addEditContact.putExtra("name", nameTv.getText());
	            addEditContact.putExtra("cap", capTv.getText());
	            addEditContact.putExtra("code", codeTv.getText());
	            startActivity(addEditContact); 
	            return true;
	            
	         case R.id.deleteItem:
	            deleteContact();
	            return true;
	            
	         default:
	            return super.onOptionsItemSelected(item);
	      } 
	   }
	   
	   private void deleteContact()
	   {
	     
	      AlertDialog.Builder alert = new AlertDialog.Builder(ViewCountry.this);

	      alert.setTitle(R.string.confirmTitle); 
	      alert.setMessage(R.string.confirmMessage); 

	      alert.setPositiveButton(R.string.delete_btn,
	         new DialogInterface.OnClickListener()
	         {
	            public void onClick(DialogInterface dialog, int button)
	            {
	               final DatabaseConnector dbConnector = 
	                  new DatabaseConnector(ViewCountry.this);

	               AsyncTask<Long, Object, Object> deleteTask =
	                  new AsyncTask<Long, Object, Object>()
	                  {
	                     @Override
	                     protected Object doInBackground(Long... params)
	                     {
	                        dbConnector.deleteContact(params[0]); 
	                        return null;
	                     } 
	                     
	                     @Override
	                     protected void onPostExecute(Object result)
	                     {
	                        finish(); 
	                     }
	                  };

	               deleteTask.execute(new Long[] { rowID });               
	            }
	         }
	      );
	      
	      alert.setNegativeButton(R.string.cancel_btn, null).show();
	   }
	}

