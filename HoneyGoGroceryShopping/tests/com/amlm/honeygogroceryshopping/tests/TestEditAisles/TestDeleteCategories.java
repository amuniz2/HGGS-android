package com.amlm.honeygogroceryshopping.tests.TestEditAisles;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.amlm.honeygogroceryshopping.activities.EditAislesActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAdapterView;

@RunWith(RobolectricTestRunner.class)
public class TestDeleteCategories extends BaseTest {
	private EditAislesActivity _activity;
	

	
	private ListView _lvCatList;
	private Spinner _aisleSpinner;
	private ArrayList<Category> _originalCats;
	
	@Before
	public void setup()
	{
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));
		
		super.setup();
	    // Override the default RoboGuice module

		try {
			_activity = (EditAislesActivity)shadowOf( super.setupAisles()).getIntentClass().newInstance();
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_lvCatList = (ListView) _activity.findViewById(R.id.categorylist);
		_aisleSpinner = (Spinner) _activity.findViewById(R.id.aisleSpinner);
		_originalCats =  new ArrayList<Category>(DataCache.getCategories());
		ShadowAdapterView shadowAisleSpinnerAdapter = shadowOf(_aisleSpinner);
		shadowAisleSpinnerAdapter.performItemClick(0);
		
 	
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
	public void testDeleteCategory()
	{

			 final int posToDelete = 3;
			
			try {
				ArrayList<Category> cats = _activity.loadCategories();
				 int count = cats.size();
				// arbitrary category
				Category catToDelete = _activity.getAdapter().getItem(posToDelete);
				View catView = _lvCatList.getChildAt(posToDelete);
				
				ImageButton delButton = (ImageButton)catView.findViewById(R.id.delButton);
				_activity.deleteItem(delButton);
				/* below code is not working because deleteItem() is not 
				 * in activity with button ?
				 * 
				 * delButton.performClick();
				 * */
				
				
				_activity.saveList();
				cats = _activity.loadCategories();
				assertEquals(count - 1, cats.size());
				assertFalse(cats.contains(catToDelete));
			} 
			 catch (Exception e) 
			 {
				 fail("Exception testing deleting of a category");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
			
	}
	
	@Test
	public void testCannotDeleteReservedCategory()
	{
		
		try {
			ArrayList<Category> cats = _activity.getItemsDisplayed();
//			int count = cats.size();
			Category reservedCat = (new GroceryItem()).getCategory();
			int posOfReservedCategory = cats.indexOf(reservedCat);
			assertTrue(posOfReservedCategory >= 0);
			View catView = _lvCatList.getChildAt(posOfReservedCategory);			
			ImageButton delButton = (ImageButton)catView.findViewById(R.id.delButton);			
			assertEquals(View.INVISIBLE, delButton.getVisibility() );
		} 
		 catch (Exception e) 
		 {
			 fail("Exception testing inability to delete reserved category");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
}

