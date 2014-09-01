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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
public class TestSaveCategories extends BaseTest {
	private EditAislesActivity _activity;
	

	private ImageButton _btnAdd;
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
			_activity = (EditAislesActivity) shadowOf(super.setupAisles()).getIntentClass().newInstance();
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_lvCatList = (ListView) _activity.findViewById(R.id.categorylist);
		_aisleSpinner = (Spinner) _activity.findViewById(R.id.aisleSpinner);
		_originalCats =  new ArrayList<Category>(DataCache.getCategories());
		ShadowAdapterView shadowAisleSpinnerAdapter = shadowOf(_aisleSpinner);
		shadowAisleSpinnerAdapter.performItemClick(0);
		
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
	public void testAssignCategoryToNewAisle()
	{
//		ShadowActivity shadowActivity = shadowOf(_activity);
		ArrayList<Category> cats = _activity.getItemsDisplayed();
		boolean aisleInList = false;
		int count = cats.size();
		String val;
	
		_btnAdd.performClick();
	
		try {
			View catView = _lvCatList.getChildAt(count);
			EditText etCatName = (EditText)catView.findViewById(R.id.edit_cat_name);
			EditText etAisle = (EditText)catView.findViewById(R.id.edit_aisle);
			
			etCatName.setText("some name");
			etAisle.setText("1");
			_activity.onAisleSelectorClicked();
			SpinnerAdapter adapter =_aisleSpinner.getAdapter();
			for (int pos = 0; (pos < adapter.getCount()) && (!aisleInList); pos++)
			{
				val = (String)adapter.getItem(pos);
				aisleInList = val.equals("1");
			}
			
			assertEquals(true, aisleInList);
		} 
		catch (Exception e) 
		{
			fail("Exception testing adding of an empty category");
		}	
	}

	@Test
	public void testCannotSaveCategoryWithoutAName()
	{

			
			ArrayList<Category> cats = _activity.getItemsDisplayed();
			int count = cats.size();
			
			try {
				this._btnAdd.performClick();
				
				_activity.saveList();
				cats = _activity.loadCategories();
				assertEquals(count, cats.size());
			} 
			 catch (Exception e) 
			 {
				 fail("Exception testing adding of an empty category");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
			
	}
	@Test
	public void testAddCategory()
	{

			ArrayList<Category> cats = _activity.getItemsDisplayed();
			int count = cats.size();
			
			_btnAdd.performClick();
			
			try {
				View catView = _lvCatList.getChildAt(count);
				EditText etCatName = (EditText)catView.findViewById(R.id.edit_cat_name);
				etCatName.setText("some name");
				
				_activity.saveList();
				cats = _activity.loadCategories();
				assertEquals(count + 1, cats.size());
			} 
			 catch (Exception e) 
			 {
				 fail("Exception testing adding of an empty category");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
			
	}
		
	@Test
	public void testCannotEditReservedCategory()
	{
		
		try {
			ArrayList<Category> cats = _activity.getItemsDisplayed();
//			int count = cats.size();
			Category reservedCat = (new GroceryItem()).getCategory();
			int posOfReservedCategory = cats.indexOf(reservedCat);
			assertTrue(posOfReservedCategory >= 0);
			View catView = _lvCatList.getChildAt(posOfReservedCategory);			
			EditText etName = (EditText)catView.findViewById(R.id.edit_cat_name);
			EditText etAisle = (EditText)catView.findViewById(R.id.edit_aisle);
			
			assertFalse(etName.isEnabled() );
			assertFalse(etAisle.isEnabled());
		} 
		 catch (Exception e) 
		 {
			 fail("Exception testing inability to edit reserved category");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	
}

