package com.amlm.honeygogroceryshopping.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.activities.BaseActivity;
import com.amlm.honeygogroceryshopping.activities.R;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.GroceryItem;

public class CategoryListAdapter extends BaseListAdapter<Category> {

	ArrayList<CategoriesViewHolder> _viewHolders;
	public CategoryListAdapter(Context context, int resource, 
			List<Category> objects) {
		super(context, resource,  objects);
		_viewHolders = new ArrayList<CategoriesViewHolder>();
	}

	    
	    public boolean saveCategoriesThatHaveChanged(ListView parent)
    	{
    		boolean changesSaved = false;
    		if (_viewHolders != null)
    		{
    			/* called by the activity prior to saving the list, 
    		
    			 * to save any items that might have been changed and
    			 * not yet saved to the underlying collection
    			 */
    			for (CategoriesViewHolder vh : this._viewHolders)
    			{
    				changesSaved = (vh.saveChangedValues() || changesSaved);
    			}
    			
    		}
    		return changesSaved;
    	}
	    private boolean isReservedCategory(int position)
	    {
	    	
	    	Category item = getItem(position);
	    	return item.getName().equals(GroceryItem.DEFAULT_CATEGORY_NAME);
	    	
	    }	    
	    @Override
	    public int getItemViewType(int position) {
	    	int ret = VIEW_ITEM_NORMAL;
	    	if (isReservedCategory(position))
	    	{ ret= VIEW_ITEM_READONLY; }
	    	else if (isSelected(position))
	    	{ ret = VIEW_ITEM_SELECTED; }
	    	
	        return ret;
	    }
	    @Override
	    public int getViewTypeCount() {
	        return 3;
	    }	    
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        CategoriesViewHolder viewHolder = null;
	        View v = null;

	        int layoutId;
	        int viewType = getItemViewType(position);
	        if (convertView != null)
	        {
	        	viewHolder = (CategoriesViewHolder)convertView.getTag();
	        	viewHolder.saveChangedValues();
	        	v = convertView;	
	        }
	        else 
	        {	             
	        	LayoutInflater li = (LayoutInflater) getContext().getSystemService(
	                     Context.LAYOUT_INFLATER_SERVICE);
	        	layoutId = R.layout.category_item;
	            /*else
	            {
	            	layoutId = R.layout.selected_category_item;
	            	
	            }*/
	            try
	            {
	            	v = li.inflate(layoutId, parent,false);
	            	viewHolder = new CategoriesViewHolder(v, viewType);        
	            	v.setTag(viewHolder);
	            	_viewHolders.add(viewHolder);
		        	

	            }
	            catch (Exception ex)
	            {
	            	BaseActivity.reportError("CategoryListAdapter",ex, getContext());
	            	
	            }
	            
	        } 
	        if (viewType == VIEW_ITEM_SELECTED)
	        {
	        	v.setBackgroundColor(v.getResources().getColor(R.color.LightBlue));
	        }
	        else
	        {
	        	v.setBackgroundColor(v.getResources().getColor(R.color.LightSlateGray));
	        	
	        }
	        Category cat = getItem(position);
	        viewHolder.bindTo(cat);
	        
	        return v;
	    }
	    
		
	


		static class CategoriesViewHolder {
	    	View _v;
	    	int _viewType;
	    	Category _cat;
        	EditText editName;
        	EditText editOrder;
        	ImageButton deleteButton;
        	
	    	CategoriesViewHolder(View v, int viewType)
	    	{
	    		_v = v;
	    		_viewType=viewType;
	
	    			editName=(EditText)v.findViewById(R.id.edit_cat_name)	;
	        	    editOrder=(EditText)v.findViewById(R.id.edit_aisle);
	    			deleteButton=(ImageButton)v.findViewById(R.id.delButton);
	    			if (viewType == VIEW_ITEM_READONLY)
		        	{
	    				editName.setEnabled(false);
	    				deleteButton.setVisibility(View.INVISIBLE);
	    				editOrder.setEnabled(false);
		        	}	
	    			if (viewType == VIEW_ITEM_SELECTED)
	    			{
	    				editName.requestFocus();
	    			}
	    	}
	        public void bindTo(Category cat)
	        {
	        			_cat=cat;
	        				editName.setText( cat.getName());	    			
	        				editOrder.setText(cat.getOrder().toString());
	        					        		
	        				deleteButton.setTag(cat);
	        }
	        
	    	public boolean saveChangedValues()
	    	{
	    		boolean changesSaved = false;
	    		String name = this.editName.getText().toString();
	    		Integer order= Integer.valueOf(this.editOrder.getText().toString());
	    		if (!this._cat.getName().equals(name))
	    		{
	    			_cat.setName(name);
	    			changesSaved= true;
	    		}
	    		if (!this._cat.getOrder().equals( order))
	    		{
	    			_cat.setOrder(order);
	    			changesSaved = true;
	    		}
	    		return changesSaved;
	    	}
	        
	    }
	    
	       
}
	    

