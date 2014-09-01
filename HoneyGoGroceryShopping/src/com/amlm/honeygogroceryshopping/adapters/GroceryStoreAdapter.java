package com.amlm.honeygogroceryshopping.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amlm.honeygogroceryshopping.activities.BaseActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;

public class GroceryStoreAdapter extends BaseListAdapter<IGroceryStore> {

	private ArrayList<GroceryStoreViewHolder> _viewHolders;
	private boolean _editMode = false;
	
	public GroceryStoreAdapter( Context context, int resource,
			 List<IGroceryStore> objects) {
		super(context, resource,  objects);
		
		_viewHolders = new ArrayList<GroceryStoreViewHolder>();
	}
	public boolean getInEditMode() { return _editMode;}
	public void setEditMode(boolean value)
	{
		_editMode=value;
	}
	@Override
    public int getViewTypeCount() {
        return 3;
    }	    
	@Override
    public int getItemViewType(int position) {
    	int layoutId;
		if (_editMode)
    	{
    		//layoutId=  (isSelected(position) ? VIEW_ITEM_EDITING : VIEW_ITEM_EDIT);
    		layoutId = VIEW_ITEM_EDIT;
    	}
    	else
        {
    		layoutId=  (isSelected(position) ? VIEW_ITEM_SELECTED : VIEW_ITEM_NORMAL);
        }
		return layoutId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroceryStoreViewHolder viewHolder = null;
        View v = null;

        int viewType = getItemViewType(position);
        if (convertView != null)
        {
        	viewHolder = (GroceryStoreViewHolder)convertView.getTag();
        	/* since the view is being re-used, save any values that have been 
        	 * changed for item that was previously displayed in the view, 
        	 * prior to updating the view with the new item's information
        	 */
        	
        	v = convertView;	
        }
        else 
        {	             
        	LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                     Context.LAYOUT_INFLATER_SERVICE);

            try
            {
            	
            	v = li.inflate(getLayoutId(viewType), parent,false);
            	viewHolder = new GroceryStoreViewHolder(v, viewType);        
            	v.setTag(viewHolder);
            	this._viewHolders.add(viewHolder)
;	            	
         
            }
            catch (Exception ex)
            {
            	BaseActivity.reportError("GroceryStoreAdapter",ex, getContext());
            	
            }
            
        } 
        IGroceryStore groceryStore = getItem(position);
        viewHolder.bindTo(groceryStore);
        if (viewType == VIEW_ITEM_SELECTED)
        {
        	
        	v.setBackgroundColor(v.getResources().getColor(R.color.LightBlue));
        	
        }
        else
        {
        	v.setBackgroundColor(v.getResources().getColor(R.color.Seashell));
        	
        }	        
        return v;
    }
	private int getLayoutId(int viewType) {
		int layoutId = R.layout.grocery_store;
		switch(viewType)
		{
		case VIEW_ITEM_NORMAL:
		case VIEW_ITEM_SELECTED:
			layoutId = R.layout.grocery_store;
			break;
		
		case VIEW_ITEM_EDITING:	
		case VIEW_ITEM_EDIT:
			layoutId = R.layout.edit_grocery_store;
			break;
		
		}
		return layoutId;
	}
    	
	static class GroceryStoreViewHolder {
    	View _v;
    	int _viewType;
    	IGroceryStore _groceryStore;
    	TextView txtName;
    	EditText etGroceryStoreName;
    	ImageButton btnEditItem;
    	ImageButton btnDelItem;
    	
    	GroceryStoreViewHolder(View v, int viewType)
    	{
    		_v = v;
    		_viewType=viewType;

    		switch (viewType)
    		{
    		case VIEW_ITEM_NORMAL:
    		case VIEW_ITEM_SELECTED:
    			txtName=(TextView)v.findViewById(R.id.grocery_store_name)	;
    			break;
    		case VIEW_ITEM_EDIT:
    		case VIEW_ITEM_EDITING:
    			etGroceryStoreName = (EditText)v.findViewById(R.id.edit_grocery_store_name);
    			//btnEditItem = (ImageButton)v.findViewById(R.id.edit_item);
    			btnDelItem = (ImageButton)v.findViewById(R.id.delButton);
    			
    			break;
    		
    		}
    		/*chkTxtName.setOnClickListener(new View.OnClickListener() {
    	        public void onClick(View v)
    	        {
    	        	
    	            ((CheckedTextView) v).toggle();
    	        }
    	    });*/
    			
    	}
        public void bindTo(IGroceryStore store)
        {
        			_groceryStore=store;
        			if (txtName!= null)
        				{
        				txtName.setText( _groceryStore.getName());	   
        				}
        			if (this.etGroceryStoreName != null)
        			{
        				etGroceryStoreName.setText(_groceryStore.getName());
        				btnDelItem.setTag(_groceryStore);
        			}
        			//	deleteButton.setTag(cat);
        }
		public boolean saveChangedValues() {
			boolean changesSaved = false;
    		
    		if ((this.etGroceryStoreName != null) && (etGroceryStoreName.isShown())) 
    		{
    			String name =etGroceryStoreName.getText().toString();
    			if (!this._groceryStore.getName().equals(name)) 
    			{
    				_groceryStore.setName(name);
    				changesSaved= true;
    			}
    		}
    		return changesSaved;
		}
        
  
    }

	public boolean saveStoresThatHaveChanged() {
		boolean changesSaved = false;
		if (_viewHolders != null)
		{
			/* called by the activity prior to saving the list, 

			 * to save any items that might have been changed and
			 * not yet saved to the underlying collection
			 */
			for (GroceryStoreViewHolder  vh : this._viewHolders)
			{
				changesSaved = (vh.saveChangedValues() || changesSaved);
			}
		}
		return changesSaved;
	}
}