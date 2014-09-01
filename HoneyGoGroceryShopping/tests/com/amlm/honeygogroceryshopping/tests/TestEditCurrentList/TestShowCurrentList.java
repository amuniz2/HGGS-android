package com.amlm.honeygogroceryshopping.tests.TestEditCurrentList;

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
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.activities.EditCurrentListActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.adapters.GroceryListAdapter;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.MyGroceryListsModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@RunWith(RobolectricTestRunner.class)
public class TestShowCurrentList extends BaseTest {
	private EditCurrentListActivity _activity;
	

//	private ImageButton _btnAdd;
	private ImageButton _btnSearch;
	private ListView _lvList;
	private EditText _etSearch;
	@Before
	public void setup()
	{
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new MyGroceryListsModule()));
		
		super.setup();
	    // Override the default RoboGuice module

		try {
			Intent intent = super.continueEditCurrentList();
			_activity = (EditCurrentListActivity) shadowOf(intent).getIntentClass().newInstance();
			ShadowActivity shadow = shadowOf(_activity);
			
			shadow.setIntent(intent);
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	//	_btnAdd = (ImageButton) this._activity.findViewById(R.id.add_button);
		_btnSearch = (ImageButton) this._activity.findViewById(R.id.search_button);
		_lvList = (ListView) _activity.findViewById(R.id.currentlist);
		
		_etSearch = (EditText) this._activity.findViewById(R.id.search_text);
 	
	}
	  
	
	    @After
	    public void teardown() {
	        // Don't forget to tear down our custom injector to avoid polluting other test classes
	       
			_activity.finish();
	        super.teardown();

			//super.getMainActivity().finish();
	    }	

	    
	    
	    @Test
	    public void testShowAllGroceryItems()
	    {
	    	assertEquals(159, _activity.getItemsDisplayed().size());
	    }	    
	    @Test
	    public void testSearchForExistingItemSelectsItemAndRemainsVisible()
	    {
	    	final String itemToSearchFor = "turkey";
	    	_etSearch.setText(itemToSearchFor);
	    	 _btnSearch.performClick();
	    	
	    	GroceryItem selectedItem = (GroceryItem) getSelectedItem();
	    	assertTrue(selectedItem != null);
	    	assertTrue(selectedItem.getName().contains(itemToSearchFor));
	    	ArrayList<GroceryItem> items = _activity.getItemsDisplayed();
	    	int pos = items.indexOf(selectedItem);
	    	assertEquals(pos, shadowOf(_lvList).getSmoothScrolledPosition());

	    	/* 
	    	 * assertTrue( (pos >= _lvMasterList.getFirstVisiblePosition()) &&
	    			    ( pos <= _lvMasterList.getLastVisiblePosition()) );
	    	 *
	    	 */
	    	}	    
	    @Test
	    public void testSearchForExistingItemSelectsItemAndMakesItVisible()
	    {
	    	final String itemToSearchFor = "ice cream";
	    	_etSearch.setText(itemToSearchFor);
	    	 _btnSearch.performClick();
	    	
	    	GroceryItem selectedItem = getSelectedItem();
	    	assertTrue(selectedItem != null);
	    	assertTrue(selectedItem.getName().contains(itemToSearchFor));
	    	ArrayList<GroceryItem> items = _activity.getItemsDisplayed();
	    	int pos = items.indexOf(selectedItem);
	    	assertEquals(pos, shadowOf(_lvList).getSmoothScrolledPosition());
	    }
	    private GroceryItem getSelectedItem()
	    {
	    	 GroceryListAdapter adapter = (GroceryListAdapter) (_lvList.getAdapter());
	    	 return (GroceryItem)adapter.getSelectedItem();
	    }
}
