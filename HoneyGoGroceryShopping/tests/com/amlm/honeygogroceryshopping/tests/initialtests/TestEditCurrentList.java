package com.amlm.honeygogroceryshopping.tests.initialtests;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
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
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.activities.BaseListActivity;
import com.amlm.honeygogroceryshopping.activities.EditCurrentItemActivity;
import com.amlm.honeygogroceryshopping.activities.EditCurrentListActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.Inject;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowIntent;


@RunWith(RobolectricTestRunner.class)
public class TestEditCurrentList extends BaseTest {
	  @Inject EditCurrentListActivity _activity;

//	private EditCurrentListActivity _currentListActivity;
	
//	private EditText _etSearch;
//	private ImageButton _btnSearch;
	private ImageButton _btnAdd;
	private ListView _lvCurrentList;
	
	@Before
	public void setup()
	{
	    // Override the default RoboGuice module
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));

		super.setup();

		try {
			//_currentListActivity = (EditCurrentListActivity) super.startCurrentList().getIntentClass().newInstance();
			Intent data = super.continueEditCurrentList();
			_activity = (EditCurrentListActivity) shadowOf(data).getIntentClass().newInstance();
			_activity.setIntent(super.getMainActivity().createEditGroceryListIntent());
			_activity.onCreate(null);

/*			_activity = new EditCurrentListActivity();
			_activity.onCreate(null);
*/
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_lvCurrentList = (ListView) _activity.findViewById(R.id.currentlist);
		
		//_currentListActivity = new EditCurrentListActivity();
		//_currentListActivity.onCreate(null);
	//	_etSearch = (EditText) this._currentListActivity.findViewById(R.id.search_text);
	//	_btnSearch = (Button) _currentListActivity.findViewById(R.id.search_button);
		_btnAdd = (ImageButton) this._activity.findViewById(R.id.add_button);
		//_lvCurrentList = (ListView) this._currentListActivity.findViewById(R.id.currentlist);
  	
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
			assert(_lvCurrentList.getCount() == 0);
		} catch (Exception e) {
			
			fail(e.getMessage());
		}
	}
	@Test
	public void testAddOneItemToEmptyList() 
	{
		try {

			_btnAdd.performClick();
			Intent data = super.targetActivityIsStarted(shadowOf(this._activity), EditCurrentItemActivity.class.getName());
			ShadowIntent shadowIntent = shadowOf(data);

			assert(shadowIntent != null);
			
			GroceryItem item = saveGroceryItemValues(data);
			_activity.processActivityResult(BaseListActivity.ACTIVITY_ADD, Activity.RESULT_OK, data);
			ArrayList<GroceryItem> itemsDisplayed = _activity.getItemsDisplayed();
			
			assertTrue(itemsDisplayed.contains(item));

		} 
		catch (Exception e) 
		{
			String msg = e.getMessage();
			fail((e.getMessage() == null)? "Exception when adding an item to an empty current grocery list" : msg);
		}
	}
	/*
	 * @Test 
	 
	public void testAddItemConfirmed()
	{
		ShadowActivity shadowOfCurrentListActivity = shadowOf(_currentListActivity);
		_btnAdd.performClick();
		Intent intent = super.targetActivityIsStarted(shadowOf(this._currentListActivity), EditCurrentItemActivity.class.getName());
		
		intent.putExtra(EditCurrentItemActivity.EXTRA_INCLUDE_IN_MASTER, false);
		shadowOfCurrentListActivity.o
		shadowOfCurrentListActivity.onActivityResult(0, -1 ,intent );
	}
	
	private void addItem(EditCurrentItemActivity activity)
	{
		BaseTestEditItem editItemTester = new BaseTestEditItem(activity);
		editItemTester.saveNewItem("test item", 1, "box", "Keeblers", false);
		assert(_lvCurrentList.getCount() == 1);
		
	}
	*/
}

