package com.amlm.honeygogroceryshopping.activities;

import java.io.IOException;
import java.util.ArrayList;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.adapters.CategoryListAdapter;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.GroceryItem;

public class EditAislesActivity extends BaseListActivity<Category> {
	@InjectView(R.id.aisleSpinner)  Spinner _aisle;
    @InjectView(R.id.categorylist) ListView _typesOfGroceries;
    @InjectView(R.id.search_text) EditText _searchText;
    private String _aisleSelected;
    private ArrayList<String> _aisles;
	
   
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	try
    	{
    		super.onCreate(savedInstanceState);
    	
    		setContentView(R.layout.activity_edit_aisle_setup);
    		getListView().setItemsCanFocus(true);
    		if (!super.IsInitializing())
    		{
    			onFilesInitialized();
    		}
    		
    	}
    	catch (Exception ex)
        {
        	handleException(getTag(), ex);
        }
        
    }
    @Override
	protected void onStart()
    {
    	hideSoftKeyboard();
    	super.onStart();
    	
    	
    }
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_edit_aisles_setup, menu);
	        return true;
	    }
	@Override
	protected void onFilesInitialized()
	{
		super.onFilesInitialized();
        try {
  			setupAisleAdapter();
  		} catch (IOException e) {
  			handleException("Aisle Setup",e);
  			//e.printStackTrace();
  		}    	              
	}
	private ArrayList<String> getAllAisles()
	{
		ArrayList<Category> cats = getCategories();
		ArrayList<String> ret = new ArrayList<String>();
		Integer aisle;
		if (cats != null)
		{
			for (Category cat: cats)
			{
				aisle = cat.getOrder();
				//if (!ret.contains(aisle.toString()) && (aisle != Category.UNKNOWN_AISLE))
				if (!ret.contains(aisle.toString()))
				{
					ret.add(aisle.toString());
				}
			}
		}
		return ret;
	}
	private void addAllAndUnknownOptions(ArrayList<String> aisles)
	{
	//	aisles.add(0,new String(getString(R.string.unknown_aisle)));
		aisles.add(0,new String(getString(R.string.all_aisles)));
	}
	protected void setupAisleAdapter() throws IOException {

     	 _aisles=getAllAisles();
        addAllAndUnknownOptions(_aisles);
      	// First parameter - Context
    	// Second parameter - Layout for the row
    	// Forth - the Array of data
     	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    			//android.R.layout.simple_list_item_multiple_choice,
     			R.layout.spinner_layout, //R.layout.list_item_text_view,
    			_aisles);
     	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
     	//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     	this._aisle.setOnItemSelectedListener(
     			new OnItemSelectedListener() 
      	{

            public void onItemSelected(AdapterView<?> view, View cell, int position,
                    long id) {      
          	    
            	selectAisle((String)(_aisle.getAdapter().getItem(position)));
               
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });    
     	_aisle.setOnTouchListener( new View.OnTouchListener() 
     		{
     		public boolean onTouch(View v, MotionEvent event) 
     		{
    	        if (event.getAction() == MotionEvent.ACTION_UP) {
    	        	onAisleSelectorClicked();
    	    //        doWhatYouWantHere();
    	        }
    	        
    	        return false;
    	    }
    	});
    	
     //	_aisle.setOnKeyListener(aisle_OnKey);
    	// Assign adapter to ListView
    	_aisle.setAdapter(adapter); 
//    	setupListAdapter(categories);
	}  	
	
	private ArrayList<Category> getCategoriesInAisle(String aisle)
	{
		ArrayList<Category> cats = getCategories();
		ArrayList<Category> ret = new ArrayList<Category>();
		boolean includeAllGrocerySections = getString(R.string.all_aisles).equals(aisle);
		if (cats != null)
		{
			if (includeAllGrocerySections)
			{
				ret.addAll(cats);
			}
			else
			{
				Integer aisleNumber = Integer.valueOf(aisle);
			
				/*if (getString(R.string.unknown_aisle).equals(aisle))
				{
					aisleNumber = Category.UNKNOWN_AISLE;
				}
				else
				{
					aisleNumber = Integer.valueOf(aisle);
				}
				*/			
				if (aisleNumber != null)
				{
					for (Category cat : cats)
					{
						if (cat.getOrder() == aisleNumber)
						{
							ret.add(cat);
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	 public void selectAisle (String aisle)
	    {		 
		 this.setAisleSelected(aisle);
		 saveChangesInMemory();
		 try {
			setupListAdapter();
		} catch (Exception e) {
			handleException(getTag(),e);
		}

	   }
	 @Override 
	 protected void setupListAdapter()
	 {
		 ArrayList<Category> itemsToDisplay = getCategoriesInAisle(this.getAisleSelected());  
		 setupListAdapter(itemsToDisplay);
	 }
	 @Override
		protected void setupListAdapter(ArrayList<Category> itemsToDisplay) {
			
		 	      	// First paramenter - Context
	    	// Second parameter - Layout for the row
	    	// Forth - the Array of data
	    	CategoryListAdapter adapter = new CategoryListAdapter(this,
	    			//android.R.layout.simple_list_item_multiple_choice,
	    			R.layout.aisle_category_item,
	    			itemsToDisplay);
	      	adapter.setNotifyOnChange(true);
	      	setAdapter(adapter);
	      	_typesOfGroceries.setAdapter(adapter);
	      	super.setItemsDisplayed(itemsToDisplay);
	      	
	      	_typesOfGroceries.setOnItemClickListener(new OnItemClickListener() 
	      	{

	            public void onItemClick(AdapterView<?> listView, View cell, int position,
	                    long id) {      
	            	
	            	
	            	CategoryListAdapter myAdapter = (CategoryListAdapter) _typesOfGroceries.getAdapter();
	            	if ((!myAdapter.getItem(position).equals(GroceryItem.getDefaultCategory()))
	            	&& (!myAdapter.isSelected(position)))
	        	       {	 	
	        	        	 myAdapter.setSelected(position);
	        	        	// myAdapter.notifyDataSetChanged();

	        	       }
	        	        		
	               
	            }
	        });    
	    	
	    	// Assign adapter to ListView
	      	_typesOfGroceries.setAdapter(adapter); 
	    }  
	    
	    public void deleteItem(View view)
	    {
	    	//confirmDeletionOfItem((Category) view.getTag());
	    	removeCategory((Category) view.getTag());
	    }	 
	    
	    private void removeCategory(Category catToDelete)
	    {
	    	ArrayList<Category> displayedItems = getItemsDisplayed();
	    	
	       	if (displayedItems.contains(catToDelete))
		    {
	       		ArrayList<Category> catsToSave = getCategories();
		    	catsToSave.remove(catToDelete);		 
		    	displayedItems.remove(catToDelete);
		    	setModified(true);
		    	((CategoryListAdapter)(this._typesOfGroceries.getAdapter())).notifyDataSetChanged();
		    	//this.saveCategories(catsToSave);           	
		    }

	    }
	    
	    // only making this public so I can test!
	    @Override
	    public boolean saveList(/*View v*/)
	    {
	    	boolean ret = false;
	    	// save the changes in memory
	    	saveChangesInMemory();
	    	if (getModified())
	    	{
	    		ret = true;
	    		// only if there were changes done by the user - and now 
	    		// saved in memory - do we save what's in memory to disk
	    		ArrayList<Category> catsToSave = DataCache.getCategories();
	    		this.saveList(catsToSave); 
	    		setModified(false);
	    	}
	    	return ret;
	    	
	    }
	    
	   
	    // saves the changes in memory
	    public void saveChangesInMemory()
	    {
	    	if (!getModified())
	    	{
	    		CategoryListAdapter adapter = (CategoryListAdapter) this._typesOfGroceries.getAdapter();
		    		
		    	if (adapter != null)
		    	{	
		    		// have the adapter tell us if the list was modified
		    		setModified(adapter.saveCategoriesThatHaveChanged(_typesOfGroceries));
		    	}
	    	}	
	    }
	    
	   
	    /*public void cancel(View v)
	    {
	    	// because categories are cached and we modified the 
	    	// cache, we need to get a fresh copy in the cache
	    	  try
	          {
	          	loadCategories();
	          }
	          catch (IOException ex)
	          {
	        	  handleException("Save aisles",ex);
	          }
	    	this.finish();
	    }
	    */	    
	    private Integer getCurrentAisleNumber()
	    {
	    
	    	Integer retValue;
	    	try
	    	{
	    		retValue = Integer.valueOf(this.getAisleSelected());
	    	}
	    	catch (NumberFormatException ex)
	    	{
	    		retValue = Category.UNKNOWN_AISLE;
	    	}
	    	return retValue;
	    	
	    	
	    }
	    public void addItemType(View view)
	    {
	    	ArrayList<Category> itemsDisplayed;
	    	Category newItem = new Category(new String(""),  getCurrentAisleNumber());
	   		ArrayList<Category> cats = getCategories();
	   		
	   		saveChangesInMemory();
	   		itemsDisplayed = getItemsDisplayed();
	   		
	   	   	newItem.setName(_searchText.getText().toString());
	       	_searchText.setText("");
	   		
			cats.add(newItem);
			itemsDisplayed.add(newItem);
			super.scrollTo(itemsDisplayed.size()-1);
			//getAdapter().setSelected(position)
			((BaseAdapter)(this._typesOfGroceries.getAdapter())).notifyDataSetChanged();
				    
	    }
	    public void onAisleSelectorClicked(/*View v*/)
	    {
	    	// update the aisle list to show the latest aisles
	    	@SuppressWarnings("unchecked")
			ArrayAdapter<String> adapter =(ArrayAdapter<String>) _aisle.getAdapter();
	    	ArrayList<Category> itemsDisplayed;
	    	saveChangesInMemory();
	    	itemsDisplayed = this.getItemsDisplayed();
	    	
		
			String aisle;
				for (Category cat: itemsDisplayed)
				{
					aisle = cat.getOrder().toString();
					if (aisleNotListed(adapter, aisle))
					{
						this._aisles.add(aisle);
						adapter.notifyDataSetChanged();
						
					}
				}
			
	    }
	    private boolean aisleNotListed(ArrayAdapter<String> adapter, String aisle)
	    {
	    	boolean ret = true;
	    	for (int pos = 0 ; (pos < _aisles.size()) && (ret); pos++ )
	    	{
	    		ret = !adapter.getItem(pos).equals(aisle);
	    	}
	    	return ret;
	    }
		@Override
		public IGroceryFile getListFile() {
			return GroceryFile.JsonFiles.get(FileNameConstants.CategoryListFileName);
		}
		@Override
		public ListView getListView() {
			
			return this._typesOfGroceries;
		}
		
		
		
		
		
		@Override
		protected boolean keywordsInItem(String keywords, Category item) {
			return ((item.getName().toLowerCase().contains(keywords.toLowerCase())) );
		}
		
		@Override
		protected void saveList(ArrayList<Category> cats) {
			try 
	   		{
				super.asynncSaveCategories(cats);
	   	      		
	   		}
	    	catch (Exception e) 
	       	{
	       			reportError("Save Aisles",e, this);
	       	} 	    	
			
		}
		@Override
		public String getTag() {
		
			return new String("Set up aisles");
		}
		private String getAisleSelected() {
			return _aisleSelected;
		}
		private void setAisleSelected(String _aisleSelected) {
			this._aisleSelected = _aisleSelected;
		}
		@Override
		public FileNameConstants getFileNameConstant() {
			// TODO Auto-generated method stub
			return FileNameConstants.CategoryListFileName;
		}
		
}
