package com.amlm.honeygogroceryshopping.activities;

import roboguice.activity.RoboActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends RoboActivity {

	public BaseActivity(){ super();}
	// private data members
	//@Inject private IDataAccessor _dm;

	
	//protected IDataAccessor getDM() { return _dm; }
	protected String getTag() { return getClass().getName();}
	 
	// activity overrides
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      
        
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main, menu);
	        return true;
	    }
	protected   void displayMessage(String title, String message)
	{
		BaseActivity.messageBox(title, message, this);
		
	}
	protected void handleException(String tag, Throwable e)
	{
		BaseActivity.reportError(tag, e, this);
	}
	  public void displayMessageAndExit(String title, String message)
	    {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setMessage(message);
			dlgAlert.setTitle(title);
			dlgAlert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				          //dismiss the dialog  
				        	dialog.dismiss();
				        	BaseActivity.this.finish();
				        }
				    });    	
			dlgAlert.setCancelable(true);
			dlgAlert.create();
			dlgAlert.show();	
			
	    	
	    	
	    }
	
	 static public void messageBox(String title, String message, Context context)
	    {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
			dlgAlert.setMessage(message);
			dlgAlert.setTitle(title);
			dlgAlert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				          //dismiss the dialog  
				        	dialog.dismiss();
				        }
				    });    	
			dlgAlert.setCancelable(true);
			dlgAlert.create();
			dlgAlert.show();	
			
	    	
	    	
	    }
	    static public  void reportError(String tag, Throwable e, Context context)
	    {
	    	String title;
	    	String message;
			
			title = "Error";
			//from = tag;

			if (e != null)
			{
				message = e.getMessage();
			}
			else
			{
				message = "No error message provided";
			}
	
			BaseActivity.messageBox(title,  message, context);		

	    }
	    public void editMasterGroceryList(MenuItem menuItem)
	    {
	       	Intent intent = new Intent(this, EditMasterListActivity.class);
	       	
	    	startActivity(intent);    	
	    }

	   
	    public void setupDropbox(MenuItem menuItem)
	    {
/*	    	DropboxAPI<AndroidAuthSession> dbSession = getDropboxSession(); 
	    	if (dbSession != null)
	    	{
	    		
	    		this._authenticatingWithDropbox = true;
	    		dbSession.getSession().startAuthentication(this);
	    	}
*/
	       	Intent intent = new Intent(this, SetupDropboxAuthentication.class);
	       	
	    	startActivity(intent);    		

	    	}
	    
	    public void editAisles(MenuItem menuItem)
	    {
	       	Intent intent = new Intent(this, EditAislesActivity.class);
	       	
	    	startActivity(intent);    	
	    	
	    }
	 
	    
		

		@Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	    	boolean ret = false;
	    	switch (item.getItemId())
	    	{
	    	
	    	case R.id.aisle_settings:
	    		this.editAisles(item);
	    		ret = true;
	    		break;
	    	case R.id.main_grocerylist_settings:
	    		this.editMasterGroceryList(item);
	    		ret = true;
	    		break;
	    	case R.id.dropbox_settings:
	    		this.setupDropbox(item);
	    		ret = true;
	    		break;
	    	case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                ret = true;	    		
	    	/*case R.id.categories_settings:
	    		editCategories(item);
	    		ret = true;
	    		break;
	    	*/
	    		}
	    	return ret;
	    }
	    
		protected ProgressDialog reportAsyncActivityToUser(String text)
		{
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(text + "...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
			return dialog;
		}

}
