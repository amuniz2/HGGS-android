package com.amlm.honeygogroceryshopping.tests.TestShoppingList;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;

import com.amlm.honeygogroceryshopping.activities.ShoppingListActivity;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
//import com.amlm.honeygogroceryshopping.tests.modules.MyGroceryListsModule;
import com.amlm.honeygogroceryshopping.tests.modules.MyIosGroceryListsModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestIosShowShoppingList extends BaseTest {
	private ShoppingListActivity _activity;
	

//	private ImageButton _btnAdd;
//	private ImageButton _btnSearch;
//	private ListView _lvList;
//	private EditText _etSearch;
	@Before
	public void setup()
	{
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new MyIosGroceryListsModule()));
		
		super.setup();
	    // Override the default RoboGuice module

		try {
		
			_activity = (ShoppingListActivity) shadowOf(super.goShopping()).getIntentClass().newInstance();
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	//	_btnAdd = (ImageButton) this._activity.findViewById(R.id.add_button);
	//	_btnSearch = (ImageButton) this._activity.findViewById(R.id.search_button);
	//	_lvList = (ListView) _activity.findViewById(R.id.shoppinglist);
		
	//	_etSearch = (EditText) this._activity.findViewById(R.id.search_text);
 	
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
	    	assertEquals(52, _activity.getItemsDisplayed().size());
	    }	    
	   
	   
	   
}
