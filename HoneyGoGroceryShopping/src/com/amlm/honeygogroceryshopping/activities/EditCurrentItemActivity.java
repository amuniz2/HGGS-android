package com.amlm.honeygogroceryshopping.activities;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;

public class EditCurrentItemActivity extends BaseEditItemActivity {	
	@InjectView(R.id.applyToMaster) CheckBox _cbSaveToMaster;

	public final static String EXTRA_INCLUDE_IN_MASTER = "com.amlm.honeygoshopping.INCLUDE_IN_MASTER";

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_edit_master_grocery_list, menu);
	        return true;
	    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the text view as the activity layout
        setContentView(R.layout.activity_edit_current_item);
         this.setupLocationAdapter(R.id.location_spinner);
         _cbSaveToMaster.setChecked(true);
	}
	
    @Override 
    protected void saveValues()
    {
    	super.saveValues();
    	// aside from telling the list view the modified item properties, we need to 
    	// communicate whether the user indicated that the change should be reflected in the master
    	// list as well
    	getIntent().putExtra(EditCurrentItemActivity.EXTRA_INCLUDE_IN_MASTER, this._cbSaveToMaster.isChecked());
     }

	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return "Edit Current Item";
	}

   
}
