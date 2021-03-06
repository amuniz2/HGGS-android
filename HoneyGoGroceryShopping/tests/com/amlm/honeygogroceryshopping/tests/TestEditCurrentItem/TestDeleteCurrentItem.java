package com.amlm.honeygogroceryshopping.tests.TestEditCurrentItem;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import android.app.AlertDialog;
import android.widget.Button;

import com.amlm.honeygogroceryshopping.activities.BaseEditItemActivity;
import com.amlm.honeygogroceryshopping.activities.EditCurrentItemActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.tests.common.BaseTest;
import com.amlm.honeygogroceryshopping.tests.modules.EmptyGroceryFilesModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;

@RunWith(RobolectricTestRunner.class)
public class TestDeleteCurrentItem extends BaseTest {


	private Button _btnDelete;
	
	
	private EditCurrentItemActivity _activity;
	
	@Before
	public void setup()
	{
		RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new EmptyGroceryFilesModule()));
		
		super.setup();
		try {
			ShadowActivity shadow ;	      
			_activity = new EditCurrentItemActivity();
			shadow = shadowOf(_activity);
			
			shadow.setIntent(createIntentToEditGroceryItem());
			_activity.onCreate(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		_btnDelete = (Button)_activity.findViewById(R.id.button_delete);
		
	}
	
	@After
	public void teardown()
	{
		AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        if (alertDialog.isShowing())
        {
        	alertDialog.dismiss();
        }
		
		super.teardown();
	}
	
	@Test
	public void testDeleteRequestsConfirmation()
	{
		Button confirmButton ;
		_btnDelete.performClick();

		AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertTrue(alertDialog.isShowing());
        assertEquals(_activity.getString(R.string.confirm),(String) shadowOf(alertDialog).getTitle());
        assertEquals(_activity.getString(R.string.confirm_delete_item),(String) shadowOf(alertDialog).getMessage());
        confirmButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        assertNotNull(confirmButton);
        assertNotNull(alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE));
		
	}
	@Test
	public void testDeleteConfirmed()
	{		
		Button confirmButton = null ;
		
		_btnDelete.performClick();

		AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertTrue(alertDialog.isShowing());
 
        confirmButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        assertNotNull(confirmButton);
 		
        confirmButton.performClick();
		
        assertEquals( BaseEditItemActivity.RESULT_DELETE, shadowOf(_activity).getResultCode());

	}
	@Test
	public void testDeleteNotConfirmed()
	{		
		Button notConfirmedButton = null ;
		
		_btnDelete.performClick();

		AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertTrue(alertDialog.isShowing());
 
        notConfirmedButton=alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        assertNotNull(notConfirmedButton);
 		
        notConfirmedButton.performClick();
	
        assertFalse(shadowOf(_activity).isFinishing());

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
