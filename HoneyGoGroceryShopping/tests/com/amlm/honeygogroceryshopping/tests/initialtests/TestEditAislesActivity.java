package com.amlm.honeygogroceryshopping.tests.initialtests;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.amlm.honeygogroceryshopping.activities.EditAislesActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAdapterView;

@RunWith(RobolectricTestRunner.class)
public class TestEditAislesActivity extends BaseTest {
	private EditAislesActivity _activity;
	

	private ImageButton _btnAdd;
	private ListView _lvCatList;
	private Spinner _aisleSpinner;
	private ArrayList<Category> _originalCats;
	@Before
	public void setup()
	{
	    // Override the default RoboGuice module
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));

		super.setup();

		try {
			_activity = (EditAislesActivity) shadowOf(super.setupAisles()).getIntentClass().newInstance();
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_lvCatList = (ListView) _activity.findViewById(R.id.categorylist);
		_aisleSpinner = (Spinner) _activity.findViewById(R.id.aisleSpinner);
		
		ShadowAdapterView shadowAisleSpinnerAdapter = shadowOf(_aisleSpinner);
		shadowAisleSpinnerAdapter.performItemClick(0);
		_originalCats =  new ArrayList<Category>(DataCache.getCategories());

		_btnAdd = (ImageButton) this._activity.findViewById(R.id.btnAdd);
  	
	}
	  
	
	    @After
	    public void teardown() {
	        // Don't forget to tear down our custom injector to avoid polluting other test classes
	        
	        DataCache.setCategories(_originalCats);
			_activity.finish();
	        super.teardown();

			//super.getMainActivity().finish();
	    }	
	
	@Test 
	public void testAisleSpinnerHasTwoItems()
	{
			// default categories in assets has 2 aisles, + 1 for "All"
			assertEquals(3,_aisleSpinner.getCount());
		
		
	}
	
	@Test
	public void testAddOneItemToEmptyListTest() 
	{
		try {
			int originalCount = _originalCats.size();
			_btnAdd.performClick();
			assertEquals(originalCount + 1,_lvCatList.getCount());
		} 
		catch (Exception e) 
		{
			String msg = e.getMessage();
			fail((msg == null)? "Exception when adding first grocery section" : msg);
		}
	}
	
}

