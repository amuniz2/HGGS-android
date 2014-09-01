package com.amlm.honeygogroceryshopping.tests.TestStartCurrentList;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import android.app.Activity;
import android.content.Intent;
import android.widget.ImageButton;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.activities.BaseListActivity;
import com.amlm.honeygogroceryshopping.activities.EditCurrentItemActivity;
import com.amlm.honeygogroceryshopping.activities.EditCurrentListActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@RunWith(RobolectricTestRunner.class)
public class TestSaveCurrentList extends BaseTest {
	private EditCurrentListActivity _activity;
	

	private ImageButton _btnAdd;
	//private ListView _lvMasterList;
//	private EditText _etSearch;
//	private ImageButton _btnSearch;
	@Before
	public void setup()
	{
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));
		
		super.setup();
	    // Override the default RoboGuice module

		try {
			Intent intent = super.startCurrentList();
			_activity = (EditCurrentListActivity) shadowOf(intent).getIntentClass().newInstance();
			ShadowActivity shadow = shadowOf(_activity);
			
			shadow.setIntent(intent);
			_activity.onCreate(null);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		//_lvMasterList = (ListView) _activity.findViewById(R.id.masterlist);
		
		_btnAdd = (ImageButton) this._activity.findViewById(R.id.add_button);		
		
	//	_etSearch = (EditText) this._activity.findViewById(R.id.search_text);

		//	_btnSearch = (Button) _currentListActivity.findViewById(R.id.search_button);

  	
	}
	  
	
	    @After
	    public void teardown() {
	        // Don't forget to tear down our custom injector to avoid polluting other test classes
	        
			_activity.finish();
	        super.teardown();
			//super.getMainActivity().finish();
	    }	

	@Test
	public void testCannotSaveCurrentItemWithoutAName()
	{

			
			ArrayList<GroceryItem> items = _activity.getItemsDisplayed();
			int count = items.size();
			
			try {
				this._btnAdd.performClick();
				
				_activity.saveList();
				items = _activity.loadGroceryItems(FileNameConstants.CurrentFileName);
				assertEquals(count, items.size());
			} 
			 catch (Exception e) 
			 {
				 fail("Exception testing adding of an empty category");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
			
	}
	@Test
	public void testAddItem()
	{

			ArrayList<GroceryItem> items = _activity.getItemsDisplayed();
			int count = items.size();
			
			_btnAdd.performClick();
			
			try {
				
				_btnAdd.performClick();
				Intent data = super.targetActivityIsStarted(shadowOf(this._activity), EditCurrentItemActivity.class.getName());
				assert(data != null);

				GroceryItem item = saveGroceryItemValues(data);
				_activity.processActivityResult(BaseListActivity.ACTIVITY_ADD, Activity.RESULT_OK, data);
				ArrayList<GroceryItem> itemsDisplayed = _activity.getItemsDisplayed();
				
				assertTrue(itemsDisplayed.contains(item));
				_activity.saveList();
				items = _activity.loadGroceryItems(FileNameConstants.CurrentFileName);
				assertEquals(count + 1, items.size());
			
			} 
			 catch (Exception e) 
			 {
				 fail("Exception testing adding of an empty category");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
			
	}
		
	
	
}

