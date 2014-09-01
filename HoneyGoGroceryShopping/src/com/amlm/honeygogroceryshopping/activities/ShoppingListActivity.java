package com.amlm.honeygogroceryshopping.activities;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.adapters.ItemSummaryListAdapter;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.model.GroceryItem;

public class ShoppingListActivity extends BaseListActivity<GroceryItem> {
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.activity_shopping_list);
        if (!super.IsInitializing())
        {
        	onFilesInitialized();
        }
    }
    @Override 
    protected void onFilesInitialized()
    {
    	super.onFilesInitialized();
    	  try {
    			setupListAdapter();
    		} catch (Exception e) {
    			handleException(getTag(),e);
    		}    	       
    }
    @Override
    public String getTag(){return "ShoppingListActivity";}
    
   
    boolean isListModified()
    {
    	ListView lv = getListView();
       	boolean selectionsHaveChanged = ((ItemSummaryListAdapter) getAdapter()).saveItemsThatHaveChanged(lv);
    	return selectionsHaveChanged;
    	
    }
	@Override 
    protected boolean saveList()
    {
    	boolean ret = false;
    	
    	if (isListModified())
    	{
    			this.saveList(this.getItemsDisplayed());
    			ret = true;
    	}
    	return ret;
    }
	@Override
	public IGroceryFile getListFile() {
		return GroceryFile.JsonFiles.get(FileNameConstants.ShoppingListFileName);
	}

	@Override
	protected ListView getListView() {
		// TODO Auto-generated method stub
		return (ListView) findViewById(R.id.shoppinglist);

	}
	@Override
	 protected int getListLayout() 
	    {
	    	return android.R.layout.simple_list_item_multiple_choice;	    
	    }
	
	/*
	 * @Override
	 
	protected void editItem(GroceryItem itemEdited) {
		ArrayList<GroceryItem> itemsToSave = this.getItemsDisplayed();
		GroceryItem itemInListToSave = itemsToSave.get(itemsToSave.indexOf(itemEdited));
		itemInListToSave.setSelected(getListView().isItemChecked(getAdapter().getPosition(itemEdited)));
	}
	*/
	private boolean createNew()
	{
			// if file does not exist, create a new one anyway, and save it so we have one
		return !GroceryFile.JsonFiles.get(FileNameConstants.ShoppingListFileName).exists(); 
		
	}
	@Override 
	public void setupListAdapter(ArrayList<GroceryItem> itemsToDisplay) {

    	ListView listView = getListView();
      
      	// First paramenter - Context
    	// Second parameter - Layout for the row
    	// Third parameter - ID of the TextView to which the data is written
    	// Forth - the Array of data
      	ItemSummaryListAdapter adapter = new ItemSummaryListAdapter(this,
    			//android.R.layout.simple_list_item_multiple_choice,
    			getListLayout(),
    			/*R.id.cat_name,*/itemsToDisplay, true);
      	adapter.setNotifyOnChange(true);
      	
      	this.setAdapter(adapter);
    	this.setItemsDisplayed(itemsToDisplay );

    	
    	// Assign adapter to ListView
    	listView.setAdapter(adapter); 
    }  
	
	   @Override 
	    protected ArrayList<GroceryItem> determineItemsToDisplay() throws IOException, JSONException
	    {
	    	ArrayList<GroceryItem> ret = null;
	    			
	    	if (createNew())
	    	{	
	     		LoadNewList loader = new LoadNewList();
	     		loader.execute();
		    }
	     	else
	     	{
	     		ret= super.determineItemsToDisplay();
	     		
	     	}
	    	return ret;
	    }
	   @Override
	   protected void saveList(ArrayList<GroceryItem> items) 
	   {
		   	if (items != null)
		   	{
		   		ItemSummaryListAdapter adapter = (ItemSummaryListAdapter) getAdapter();
		   		
		   		try 
		   		{
		   			ListView lv = getListView();
		   			adapter.saveItemsThatHaveChanged(lv);
		   			asyncSaveGroceryItems(getFileNameConstant(), items);
		   		} 
		   		catch (Exception e) 
		   		{
		   			handleException(getTag(),e);
		   		}	
		   	}
		   
	   }
	   /* only used  when ScrollView of LiniearLayouts were used
	    * (vs. ListView)
	   private void loadSelections()
	   {
		   ArrayAdapter<GroceryItem> adapter = this.getAdapter();
		   ArrayList<GroceryItem> itemsDisplayed = this.getItemsDisplayed();
		   ListView lv = this.getListView();
		   int position;
		   for (GroceryItem item : itemsDisplayed)
		   {
			   position = adapter.getPosition(item);
			   lv.setItemChecked(position, item.getSelected());
		   }
	   }
	   */
	   @Override
	    protected boolean keywordsInItem(String keywords, GroceryItem item)
	    { 
		   String keywordsInLowerCase= keywords.toLowerCase();
		   return (((item.getName().toLowerCase().contains(keywordsInLowerCase)) ||
				(item.getNotes().toLowerCase().contains(keywordsInLowerCase))));
	    }
	@Override
	public FileNameConstants getFileNameConstant() {
		// TODO Auto-generated method stub
		return FileNameConstants.ShoppingListFileName;
	}
	private void onFinishedLoadingNewList(String errorMessage, String serializedList)
	{
	       //	Product product = null;
       	if (errorMessage != null)
       	{
       		displayMessage("Error loading the shopping list", errorMessage);	
       	}
       			
       	else if ((serializedList != null) && !serializedList.isEmpty())
		{
       		ArrayList<GroceryItem> ret = new ArrayList<GroceryItem>();
       		ArrayList<GroceryItem> currentList = null;
       		try {
       			
       			currentList = _groceryListSerializer.parseList(serializedList);
			   	
	    		for (GroceryItem item : currentList)
	    		{
	    			if (item.getSelected())
	    			{	
	    				item.setSelected(false);
	    				ret.add(item);  
	    			}
	    		}
	    		setupListAdapter(ret);
	    		
       		} catch (JSONException e) {
				// TODO Auto-generated catch block
				super.handleException(getTag(), e);
			}
		}	
       	else
       	{
       		this.displayMessageAndExit(getString(R.string.no_shopping_list), getString(R.string.create_shopping_list_first));
       		// messageBox(getString(R.string.no_shopping_list), getString(R.string.create_shopping_list_first), this);
       	
       		//finish();
       		
       	}
	}
	private  class LoadNewList extends AsyncTask<Void, Void, String> {
    	private ProgressDialog _dialog = null;
    	private String _errorMessage = null;
    	
        @Override
        protected String doInBackground(Void... params) {
          String response = "";
    
            try {
    			response = LoadItems();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			_errorMessage = e.getMessage();
   		}
          return response;
        }
        @Override 
        protected void onPreExecute()
        {
        	_dialog = reportAsyncActivityToUser("Loading shopping list...");
        }
        @Override
        protected void onPostExecute(String result) {
        	_dialog.hide();
        	onFinishedLoadingNewList(_errorMessage, result);
        }


    	
    	
    	
    	public String LoadItems() throws Exception 
    	{
    		String ret = "";
    		if (GroceryFile.JsonFiles.get(FileNameConstants.CurrentFileName).exists())
    		{
    			ret= readGroceryFile(FileNameConstants.CurrentFileName);
    		}
    		else
    		{
    			ret = readGroceryFile(FileNameConstants.MasterFileName);
    		}
    		
      		
     		return ret;
    		  
    	}
		


    }
}
