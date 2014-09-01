package com.amlm.honeygogroceryshopping.tests.initialtests;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.activities.BaseListActivity;
import com.amlm.honeygogroceryshopping.activities.EditMasterItemActivity;
import com.amlm.honeygogroceryshopping.activities.EditMasterListActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.Inject;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestEditMasterListActivity extends BaseTest {
	 @Inject EditMasterListActivity _activity;
		private ImageButton _btnAdd;
	private ListView _lvMasterList;
	private EditText _etSearch;
//	private ImageButton _btnSearch;
	@Before
	public void setup()
	{
	    // Override the default RoboGuice module
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));

		super.setup();
// todo: need to select the first grocery store?
		try {
			//_currentListActivity = (EditCurrentListActivity) super.startCurrentList().getIntentClass().newInstance();
			Intent data = super.editMasterList();
			_activity = (EditMasterListActivity) shadowOf(data).getIntentClass().newInstance();
			_activity.onCreate(null);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_lvMasterList = (ListView) _activity.findViewById(R.id.masterlist);
		
		_etSearch = (EditText) this._activity.findViewById(R.id.search_text);
	//	_btnSearch = (Button) _currentListActivity.findViewById(R.id.search_button);
		_btnAdd = (ImageButton) this._activity.findViewById(R.id.add_button);
	//	_btnSearch = (ImageButton) this._activity.findViewById(R.id.search_button);
	}
	  
	
	    @After
	    public void teardown() {
	        // Don't forget to tear down our custom injector to avoid polluting other test classes
	       
			_activity.finish();
	        super.teardown();
	       
//super.getMainActivity().finish();
	    }	
	    
	    @Test
		public void testListShouldBeEmptyInitially() {
			
			try {
				assert(_lvMasterList.getCount() == 0);
			} catch (Exception e) {
				
				fail(e.getMessage());
			}
		}
	    
		@Test
		public void testAddOneItemToEmptyList() 
		{
			try {

				_btnAdd.performClick();
				Intent data = super.targetActivityIsStarted(shadowOf(this._activity), EditMasterItemActivity.class.getName());
				assert(data != null);

				GroceryItem item = saveGroceryItemValues(data);
				_activity.processActivityResult(BaseListActivity.ACTIVITY_ADD, Activity.RESULT_OK, data);
				ArrayList<GroceryItem> itemsDisplayed = _activity.getItemsDisplayed();
				
				assertTrue(itemsDisplayed.contains(item));
			} 
			catch (Exception e) 
			{
				String msg = e.getMessage();
				fail((msg == null)? "Exception when adding an item to an empty master grocery list" : msg);
			}
		}	    
		
		@Test
		public void testAddItemSearchedFor() 
		{
			try {
				final String ITEM_NAME_SEARCHED_FOR = "Searched for Item";
				_etSearch.setText(ITEM_NAME_SEARCHED_FOR);	
				_btnAdd.performClick();
				Intent data = super.targetActivityIsStarted(shadowOf(this._activity), EditMasterItemActivity.class.getName());
				assert(data != null);

				GroceryItem item = saveGroceryItemValues(data);
				_activity.processActivityResult(BaseListActivity.ACTIVITY_ADD, Activity.RESULT_OK, data);
				ArrayList<GroceryItem> itemsDisplayed = _activity.getItemsDisplayed();
				
				assertTrue(itemsDisplayed.contains(item));
				assertEquals(ITEM_NAME_SEARCHED_FOR, item.getName());
			} 
			catch (Exception e) 
			{
				String msg = e.getMessage();
				fail((msg == null)? "Exception when adding a master item searched for" : msg);
			}
		}	    
}
