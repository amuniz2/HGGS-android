package com.amlm.honeygogroceryshopping.tests.initialtests;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.activities.ShoppingListActivity;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.Inject;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;


@RunWith(RobolectricTestRunner.class)
public class TestGoShopping extends BaseTest {
	  @Inject ShoppingListActivity _activity;

//	private EditCurrentListActivity _currentListActivity;
	

	private ListView _lvCurrentList;
	
	@Before
	public void setup()
	{
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));
		super.setup();
	    // Override the default RoboGuice module

		try {
			//_currentListActivity = (EditCurrentListActivity) super.startCurrentList().getIntentClass().newInstance();
			
			_activity = (ShoppingListActivity) shadowOf(super.goShopping()).getIntentClass().newInstance();
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_lvCurrentList = (ListView) _activity.findViewById(R.id.shoppinglist);
		
  	
	}
	  
	
	    @After
	    public void teardown() {
	        // Don't forget to tear down our custom injector to avoid polluting other test classes
	       
			_activity.finish();
	        super.teardown();
	    }	
	    

	@Test
	public void testListShouldBeEmptyInitially() {
		
		try {
			assert(_lvCurrentList.getCount() == 0);
			AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
	        assertTrue(alertDialog.isShowing());
	 
	        Button okButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
	        assertNotNull(okButton);
	 		
	        okButton.performClick();
		
	        assertTrue(shadowOf(_activity).isFinishing());
			
			// show AlertDialog that grocery list must first be created
		} catch (Exception e) {
			
			fail(e.getMessage());
		}
	}
	
}

