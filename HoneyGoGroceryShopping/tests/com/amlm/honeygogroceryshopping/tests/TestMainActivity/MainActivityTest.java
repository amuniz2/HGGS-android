package com.amlm.honeygogroceryshopping.tests.TestMainActivity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;


import android.content.Intent;

import com.amlm.honeygogroceryshopping.activities.BaseListActivity;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
//import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.amlm.honeygogroceryshopping.tests.modules.MyGroceryListsModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest extends BaseTest {
/*	private MainActivity _activity;
	private Button _startGroceryListButton;
    private Button _continueGroceryListButton;
    private Button _goShoppingButton;
  */  
	@Before
    public void setup()
    {
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new MyGroceryListsModule()));
		
		super.setup();
		/*_activity = new MainActivity();
		_activity.onCreate(null);
		_startGroceryListButton = (Button) _activity.findViewById(R.id.CreateGroceryList);
		_continueGroceryListButton = (Button) _activity.findViewById(R.id.EditGroceryList);
		_goShoppingButton = (Button) _activity.findViewById(R.id.GoShopping);
    	*/
    }
	
	@After
	public void teardown()
	{
		super.teardown();
		//super.getMainActivity().finish();
	}
/*	@Test
    public void shouldHaveHappySmiles() throws Exception {
        String hello = new MainActivity().getResources().getString(R.string.title_activity_main);
        assertThat(hello, equalTo("Honey, Go Grocery Shopping!"));
    }
*/

	 @Test 
	public void startShouldNavigateToEditCurrentList()
	{
		 Intent intent =super.startCurrentList();
		 assertTrue(intent.getBooleanExtra(BaseListActivity.CREATE_NEW_LIST, false));

/*		_startGroceryListButton.performClick();
		
		ShadowActivity shadowActivity = shadowOf(_activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    ShadowIntent shadowIntent = shadowOf(startedIntent);
	    assertThat(shadowIntent.getComponent().getClassName(), equalTo(EditCurrentListActivity.class.getName()));		
	*/
	}
	 
	 @Test 
	public void continueShouldNavigateToEditCurrentList()
	{
		 
		 Intent intent =super.continueEditCurrentList();
		 assertFalse(intent.getBooleanExtra(BaseListActivity.CREATE_NEW_LIST, false));
		
		 /*
		 * _continueGroceryListButton.performClick();
		 
		ShadowActivity shadowActivity = shadowOf(_activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    ShadowIntent shadowIntent = shadowOf(startedIntent);
	    assertThat(shadowIntent.getComponent().getClassName(), equalTo(EditCurrentListActivity.class.getName()));
		*/
		
	}
	 
	@Test 
	public void goShoppingShouldNavigateToShoppingList()
	{
		 /*
		 _goShoppingButton.performClick();
		ShadowActivity shadowActivity = shadowOf(_activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    ShadowIntent shadowIntent = shadowOf(startedIntent);
	    assertThat(shadowIntent.getComponent().getClassName(), equalTo(ShoppingListActivity.class.getName()));
		*/
		 super.goShopping();
		
	}
	 
	
  

}

