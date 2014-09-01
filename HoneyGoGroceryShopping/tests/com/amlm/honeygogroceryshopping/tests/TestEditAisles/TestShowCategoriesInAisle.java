package com.amlm.honeygogroceryshopping.tests.TestEditAisles;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.amlm.honeygogroceryshopping.activities.EditAislesActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.adapters.CategoryListAdapter;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.mocks.groceryfile.EmptyGroceryFileMock;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAdapterView;

@RunWith(RobolectricTestRunner.class)
public class TestShowCategoriesInAisle extends BaseTest {

	
	private EditAislesActivity _activity;
	

	//private ImageButton _btnAdd;
	private EditText _etSearch;
	private ImageButton _btnSearch;
	private Spinner _aisleSpinner;
	private ArrayList<Category> _originalCats;
	private ListView _lvCats;
	
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
		_aisleSpinner = (Spinner) _activity.findViewById(R.id.aisleSpinner);
		_originalCats =  new ArrayList<Category>(DataCache.getCategories());
		_etSearch = (EditText) _activity.findViewById(R.id.search_text);
		_btnSearch = (ImageButton) _activity.findViewById(R.id.search_button);
		_lvCats = _activity.getListView();
		
		ShadowAdapterView shadowAisleSpinnerAdapter = shadowOf(_aisleSpinner);
		shadowAisleSpinnerAdapter.performItemClick(0);
		
		//_btnAdd = (ImageButton) this._activity.findViewById(R.id.btnAdd);
  	
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
	    public void testCategoriesDisplayedBelongInSingleSelectedAisle()
	    {
	    	final int  selectedAisle = 2;
	    	this._aisleSpinner.setSelection(selectedAisle);
	    	ArrayList<Category> itemsDisplayed = this._activity.getItemsDisplayed();
	    	//assertEquals(3,itemsDisplayed.size());
	    	assertEquals(EmptyGroceryFileMock.numberOfSectionsPerAisle[selectedAisle-1], itemsDisplayed.size());
	    }
	    
	    @Test
	    public void testShowAllCategoriesDisplaysSectionsInAllAisles()
	    {
	    	int totalNumberOfGrocerySections = 0;
	    	this._aisleSpinner.setSelection(0);
	    	ArrayList<Category> itemsDisplayed = this._activity.getItemsDisplayed();
	    	//assertEquals(3,itemsDisplayed.size());
	    	for (int pos = 0; pos < EmptyGroceryFileMock.numberOfSectionsPerAisle.length; pos++)
	    	{
	    		totalNumberOfGrocerySections += EmptyGroceryFileMock.numberOfSectionsPerAisle[pos];
	    	}
	    	assertEquals(totalNumberOfGrocerySections, itemsDisplayed.size());
	    }	    
	    
	    @Test
	    public void testShowAllUnknownCategoriesInDefaultAisle()
	    {
	    	int totalNumberOfGrocerySections = 0;
	    	this._aisleSpinner.setSelection(0);
	    	ArrayList<Category> itemsDisplayed = this._activity.getItemsDisplayed();
	    	//assertEquals(3,itemsDisplayed.size());
	    	for (int pos = 0; pos < EmptyGroceryFileMock.numberOfSectionsPerAisle.length; pos++)
	    	{
	    		totalNumberOfGrocerySections += EmptyGroceryFileMock.numberOfSectionsPerAisle[pos];
	    	}
	    	assertEquals(totalNumberOfGrocerySections, itemsDisplayed.size());
	    }	 
	    // todo: add following tests:
	    @Test
	    public void testSearchForExistingItemSelectsItemAndRemainsVisible()
	    {
	    	final String itemToSearchFor = "candy";
	    	_etSearch.setText(itemToSearchFor);
	    	 _btnSearch.performClick();
	    	
	    	Category selectedItem = (Category) getSelectedItem();
	    	assertTrue(selectedItem != null);
	    	assertTrue(selectedItem.getName().contains(itemToSearchFor));
	    	ArrayList<Category> items = _activity.getItemsDisplayed();
	    	int pos = items.indexOf(selectedItem);
	    	assertEquals(pos, shadowOf(_lvCats).getSmoothScrolledPosition());

	    	/* 
	    	 * assertTrue( (pos >= _lvMasterList.getFirstVisiblePosition()) &&
	    			    ( pos <= _lvMasterList.getLastVisiblePosition()) );
	    	 *
	    	 */
	    	}	    
	    @Test
	    public void testSearchForExistingItemSelectsItemAndMakesItVisible()
	    {
	    	final String itemToSearchFor = "frozen";
	    	_etSearch.setText(itemToSearchFor);
	    	 _btnSearch.performClick();
	    	
	    	Category selectedItem = getSelectedItem();
	    	assertTrue(selectedItem != null);
	    	assertTrue(selectedItem.getName().contains(itemToSearchFor));
	    	ArrayList<Category> items = _activity.getItemsDisplayed();
	    	int pos = items.indexOf(selectedItem);
	    	assertEquals(pos, shadowOf(_lvCats).getSmoothScrolledPosition());
	    }
	    private Category getSelectedItem()
	    {
	    	 CategoryListAdapter adapter = (CategoryListAdapter) (_lvCats.getAdapter());
	    	 return (Category)adapter.getSelectedItem();
	    }
}
