package com.amlm.honeygogroceryshopping.tests.common;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Random;

import org.junit.After;

import roboguice.RoboGuice;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Button;

import com.amlm.honeygogroceryshopping.activities.BaseEditItemActivity;
import com.amlm.honeygogroceryshopping.activities.BaseListActivity;
import com.amlm.honeygogroceryshopping.activities.EditCurrentListActivity;
import com.amlm.honeygogroceryshopping.activities.EditMasterListActivity;
import com.amlm.honeygogroceryshopping.activities.GroceryFileConsumer;
import com.amlm.honeygogroceryshopping.activities.MainActivity;
import com.amlm.honeygogroceryshopping.activities.EditAislesActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.activities.ShoppingListActivity;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.tester.android.view.TestMenuItem;

public class BaseTest {
	private MainActivity _activity;
	private Button _startGroceryListButton;
    private Button _continueGroceryListButton;
    private Button _goShoppingButton;
    private MenuItem _aisleSetupMenuItem;
    private MenuItem _maasterListSetupMenuItem;
    
    protected MainActivity getMainActivity() {return _activity;}
	//@Before
    public void setup()
    {
		_activity = new MainActivity();
		_activity.onCreate(null);
		_startGroceryListButton = (Button) _activity.findViewById(R.id.CreateGroceryList);
		_continueGroceryListButton = (Button) _activity.findViewById(R.id.EditGroceryList);
		_goShoppingButton = (Button) _activity.findViewById(R.id.GoShopping);
		_aisleSetupMenuItem = new TestMenuItem(R.id.aisle_settings);
		_maasterListSetupMenuItem =new TestMenuItem(R.id.main_grocerylist_settings);
    }
    @After
	public void teardown()
	{
    	 RoboGuice.util.reset();
    	GroceryFileConsumer.reset();
		_activity.finish();
	}
/*	@Test
    public void shouldHaveHappySmiles() throws Exception {
        String hello = new MainActivity().getResources().getString(R.string.title_activity_main);
        assertThat(hello, equalTo("Honey, Go Grocery Shopping!"));
    }
*/

	// @Test 
	public Intent startCurrentList()
	{
		_startGroceryListButton.performClick();
		ShadowActivity shadowActivity = shadowOf(_activity);
	    return targetActivityIsStarted(shadowActivity, EditCurrentListActivity.class.getName());
	}
	public Intent setupAisles()
	{
		_activity.onOptionsItemSelected(this._aisleSetupMenuItem);
		ShadowActivity shadowActivity = shadowOf(_activity);	
		return targetActivityIsStarted(shadowActivity, EditAislesActivity.class.getName());
		
	}
	public Intent editMasterList()
	{
		_activity.onOptionsItemSelected(_maasterListSetupMenuItem);
		ShadowActivity shadowActivity = shadowOf(_activity);	
		return targetActivityIsStarted(shadowActivity, EditMasterListActivity.class.getName());
		
	}
	protected Intent targetActivityIsStarted(ShadowActivity shadowActivity, String targetClassName)
	{
	    Intent startedActivity = shadowActivity.getNextStartedActivity();
	    ShadowIntent shadowOfstartedIntent =shadowOf(startedActivity);
	    
	    assertThat(shadowOfstartedIntent.getComponent().getClassName(), equalTo(targetClassName));
	    return startedActivity;
		//return shadowOfstartedIntent;
	}
	// @Test 
	public Intent continueEditCurrentList()
	{
		_continueGroceryListButton.performClick();
		ShadowActivity shadowActivity = shadowOf(_activity);
	    return targetActivityIsStarted(shadowActivity, EditCurrentListActivity.class.getName());	    
		
	}
	// @Test 
	public Intent goShopping()
	{
		 _goShoppingButton.performClick();
		ShadowActivity shadowActivity = shadowOf(_activity);
	    return targetActivityIsStarted(shadowActivity, ShoppingListActivity.class.getName());	    

		/*	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    ShadowIntent shadowIntent = shadowOf(startedIntent);
	    assertThat(shadowIntent.getComponent().getClassName(), equalTo(ShoppingListActivity.class.getName()));
	*/	
		
	}
	protected GroceryItem saveGroceryItemValues( Intent intent)
	{
        GroceryItem item = (GroceryItem) intent.getSerializableExtra(BaseEditItemActivity.EXTRA_ITEM);

	 	if (item.getName().isEmpty() )
	 	{
	 		item.setName("item added");
	 	}
		item.setNotes("some additional notes");
		item.setQuantity(2);
		item.setUnitsOrSize("packages");
		item.setSelected(true);
		ArrayList<Category> cats = DataCache.getCategories();
		//item.setCategoryId(cats.get(0).getId());
		item.setCategoryName(cats.get(0).getName());
		return item;
	}
	protected Intent createIntentToEditGroceryList(boolean newList)
	{
		Intent intent = new Intent();
		
		intent.putExtra(BaseListActivity.CREATE_NEW_LIST, newList);
		return intent;
		
	}
	protected Intent createIntentToEditGroceryItem()
	{
		Intent intent = new Intent();
		GroceryItem item = createGroceryItemToEdit();
		
		intent.putExtra(BaseEditItemActivity.EXTRA_ITEM, item);
		return intent;
	}
	protected GroceryItem createGroceryItemToEdit()
	{
		final String name = "test name";
		final int quantity = 1;
		final boolean selected = true;
		final String units = "box";
		final String notes = "";
		
		// note: category is set to default
		GroceryItem ret = new GroceryItem(name, quantity, selected, units,  notes);
		return ret;
	}
	protected Category getCategory()
	{
		ArrayList<Category> cats= DataCache.getCategories();
		Random randomNumberGenerator = new Random();
		Category ret = null;
		do
		{			
			ret = cats.get(randomNumberGenerator.nextInt(cats.size()-1));
		}
		while (ret.getName().equals(GroceryItem.DEFAULT_CATEGORY_NAME));
		return ret;
		
	}	
}
