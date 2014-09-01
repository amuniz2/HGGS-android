package com.amlm.honeygogroceryshopping.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CreateGroceryStoreActivity extends RoboActivity {
	@InjectView(R.id.new_grocery_store_name) EditText _etStoreName;

	private String _storeName;
	
	 @TargetApi(11)
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_grocery_store);
		//_storeName = (String)(getIntent().getSerializableExtra(BaseEditItemActivity.EXTRA_ITEM));
		// Show the Up button in the action bar.
		  if (Build.VERSION.SDK_INT > 10)
	        {
			  getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_grocery_store, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	  public void cancelEdit(View view)
	    {
	    	this.setResult(RESULT_CANCELED);
	    	this.finish();
	    }
	    public void applyEdit(View view)
	    {
	    	String name = _etStoreName.getText().toString();
	    	
	    	if ((name != null) && (!name.isEmpty()))
	    	{
	    		_storeName= this._etStoreName.getText().toString();
		    	Intent intent = new Intent();
		    	intent.putExtra(BaseEditItemActivity.EXTRA_ITEM, _storeName);
	    		this.setResult(RESULT_OK, intent);
	    		this.finish();
	    	
	    	}
	    	else
	    	{
	    		BaseActivity.messageBox("Enter name", "Please provide a name for the grocery store", this);
	    	}
	    }   
	    
		  

}
