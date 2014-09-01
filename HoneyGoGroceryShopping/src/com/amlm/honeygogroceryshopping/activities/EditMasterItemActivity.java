package com.amlm.honeygogroceryshopping.activities;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;

import com.amlm.honeygogroceryshopping.model.GroceryItem;

public class EditMasterItemActivity extends BaseEditItemActivity {
	@InjectView(R.id.selectByDefault) CheckBox _cbSelectByDefault;		

    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the text view as the activity layout
        setContentView(R.layout.activity_edit_master_item);
         this.setupLocationAdapter(R.id.location_spinner);
    }
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_edit_master_grocery_list, menu);
	        return true;
	    }
	
	   @Override
	    protected void bind()
	    {
		   super.bind();
	    	GroceryItem item = getItem();
	    	if (item != null)
	    	{
	    		this._cbSelectByDefault.setChecked(item.getSelected());
	    		
	    		}
	    }
	  
	   @Override 
	    protected void saveValues()
	    {
		   super.saveValues();
		   GroceryItem item = getItem();
            
			if (item != null)
				{
				item.setSelected(this._cbSelectByDefault.isChecked());
				}

	    }
	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return "Edit Master Item";
	}
 

}
