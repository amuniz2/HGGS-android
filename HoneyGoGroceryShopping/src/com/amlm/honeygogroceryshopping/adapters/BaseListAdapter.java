package com.amlm.honeygogroceryshopping.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class BaseListAdapter<T> extends ArrayAdapter<T> 
{
	//fields
	protected static final int VIEW_ITEM_NORMAL = 0;
	protected static final int VIEW_ITEM_SELECTED = 1;
	protected static final int VIEW_ITEM_READONLY = 2;
	protected static final int VIEW_ITEM_EDIT = 2;
	protected static final int VIEW_ITEM_EDITING= 3;
	
	private T _selectedItem;
	
	// field accessors
	public T getSelectedItem()
	{ return _selectedItem; }
	public void setSelectedItem(T value)
	{ _selectedItem = value ; }
	
	public BaseListAdapter(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	public BaseListAdapter(Context context, int resource,
			List<T> objects) {
		super(context, resource, android.R.id.text1, objects);
		// TODO Auto-generated constructor stub
	}
	public boolean isSelected(int position)
    {
    	boolean ret = false;
    	T selectedItem = getSelectedItem();
    	if (selectedItem != null)
    	{
    		ret=selectedItem.equals(getItem(position));
    	}
    	return ret;
    }
    public void setSelected(int position)
    {
    	if (position == NO_SELECTION)
    		setSelectedItem(null);
    	else
    		setSelectedItem((T)getItem(position));
    	
    }	
    @Override
    public int getItemViewType(int position) {
    	return  (isSelected(position) ? VIEW_ITEM_SELECTED : VIEW_ITEM_NORMAL);
        
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }	    
    

}
