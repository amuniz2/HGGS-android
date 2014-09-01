package com.amlm.honeygogroceryshopping.activities;

import java.util.ArrayList;

import org.json.JSONException;

import roboguice.RoboGuice;
import roboguice.inject.InjectView;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.adapters.ItemSummaryListAdapter;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryItemSerializer;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.amlm.honeygogroceryshopping.model.PersistentItem;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class EditMasterListActivity extends BaseListActivity<GroceryItem> {
	@Inject Application _app;
@InjectView(R.id.search_text) EditText _searchText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_master_grocery_list);
        
        if (!super.IsInitializing())
        {
        	onFilesInitialized();
        }
      //  getListView().setBackgroundResource(color.holo_blue_bright);
    }
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_edit_master_grocery_list, menu);
	        return true;
	    }
  
    public String getTag() { return "EditMasterGroceryListActivity";}
    @Override 
    protected void onFilesInitialized()
    {
    	super.onFilesInitialized();
    	try {
    		setupListAdapter();
		} catch (Exception e) {
			handleException(getTag(), e);
		}    	
    	this.hideSoftKeyboard();
    }
	

    @Override
    public IGroceryFile getListFile() {
        return GroceryFile.JsonFiles.get(FileNameConstants.MasterFileName);
    
    }
    @Override
    protected  ListView getListView() { return (ListView) findViewById(R.id.masterlist);}
 
   
    public void addItem(View view)
    {
    	GroceryItem newItem = new GroceryItem();
    
    	newItem.setName( _searchText.getText().toString());
       	_searchText.setText("");

 
    	Intent intent = new Intent(this, EditMasterItemActivity.class);
       	intent.putExtra(BaseEditItemActivity.EXTRA_ITEM, newItem);
       	
    	this.startActivityForResult(intent, ACTIVITY_ADD);
    }
    
    protected void editItem(GroceryItem item)
    {
    	
       	Intent intent = new Intent(this, EditMasterItemActivity.class);
       	intent.putExtra(BaseEditItemActivity.EXTRA_ITEM, item);
       	
    	this.startActivityForResult(intent, ACTIVITY_EDIT);
    }
  
    @Override
    protected boolean keywordsInItem(String keywords, GroceryItem item)
    { 
    	String keywordsInLowerCase = keywords.toLowerCase();
    	return (((item.getName().toLowerCase().contains(keywordsInLowerCase)) ||
			(item.getNotes().toLowerCase().contains(keywordsInLowerCase))));
    }
    
    @Override 
	public void setupListAdapter(ArrayList<GroceryItem> itemsToDisplay) 
	{

    	ListView listView = getListView();
    	
      
      	// First paramenter - Context
    	// Second parameter - Layout for the row
     	// Forth - the Array of data
      	ItemSummaryListAdapter adapter = new ItemSummaryListAdapter(this,
    			//android.R.layout.simple_list_item_multiple_choice,
    			getListLayout(),
    			itemsToDisplay, false);
      	adapter.setNotifyOnChange(true);
      	
      	this.setAdapter(adapter);
    	this.setItemsDisplayed(itemsToDisplay );
    	listView.setOnItemClickListener(new OnItemClickListener() 
      	{

            public void onItemClick(AdapterView<?> listView, View cell, int position,
                    long id) {
            	
            	editItem((GroceryItem)(listView.getItemAtPosition(position)));
               
            }
        });    
    
    	
    	// Assign adapter to ListView
    	listView.setAdapter(adapter); 
    }  
    @Override
    public void saveList(ArrayList<GroceryItem> items) {
    	try 
	    {
	  		asyncSaveGroceryItems(this.getFileNameConstant(),items);
	  		
	  	 	    	
	    }
		catch (Exception e) {
			handleException(getTag(), e);
		}
    }
    /* public for testing purposes! */
    @Override
	public boolean saveList(/*View v*/)
    {
    	boolean ret = false;
    	// save the changes in memory
    	//saveChangesInMemory();
    	if (getModified())
    	{
    		ret = true;
    		// only if there were changes done by the user - and now 
    		// saved in memory - do we save what's in memory to disk
    		//ArrayList<Category> catsToSave = DataCache.getCategories();
    		this.saveList(getItemsDisplayed()); 
    		
    	}
    	return ret;
    	//this.finish();
    	
    }
    
	@Override
	protected void updateItemInOtherList(GroceryItem itemUpdated, Intent data) {
	   	SaveOrUpdateItemInCurrentList currentListUpdater = new SaveOrUpdateItemInCurrentList();
       	try {
    		Injector itemSerializerInjector ;	
    		IGroceryItemSerializer itemSerializer;
       		itemSerializerInjector = RoboGuice.getInjector(this);
       		itemSerializer = itemSerializerInjector.getInstance(IGroceryItemSerializer.class);
       		
       		// todo: upload to dropbox if dropbox api is available
			currentListUpdater.execute(itemSerializer.writeToJsonString(itemUpdated));
		} catch (Exception e) {
			this.handleException("Update Current Item", e);
		} 

	}
	@Override
	protected void addItemToOtherList(GroceryItem itemUpdated, Intent data) {
		updateItemInOtherList(itemUpdated, data);
	}
    private  class SaveOrUpdateItemInCurrentList extends AsyncTask<String, Void, String> {
    	
    //	private String _errorMessage = null;
        @Override
        protected String doInBackground(String... itemsAsJsonStrings) {
          String response = "";
         
          for (String jsonItem : itemsAsJsonStrings) 
          {
            try {
            	// convert the string to a groceryItem
            
            	GroceryItem updatedItem = GroceryItem.fromJSONString(_app, jsonItem);
            	
            	// update or add the item in the current shopping list
            	UpdateOrAddItem(updatedItem);
    			
    		} catch (JSONException e) {
    		
    			setErrorMessage( e.getMessage());
    		}
            break;
          }
          return response;
        }

        @Override
        protected void onPostExecute(String result) {
        	//processGetProductResult(errorMessage, result);
        	//onSaveToOtherListCompleted();
        }


    	
    	
    	/* AWS service
    	 * must subscribe / pay for it, and uses ws-security
    	 * private String wsNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01";
    	 
    	private String itemLookupMethodName = "ItemLookup";
    	*/
    	public void UpdateOrAddItem(GroceryItem updatedItem) 
    	{
    		//IDataAccessor dm = getDM();
    		
    		try {
    			//GroceryFile file = GroceryFile.JsonFiles.get(FileNameConstants.CurrentFileName);
				ArrayList<GroceryItem> items = loadGroceryItems(/*file*/FileNameConstants.CurrentFileName);
				if (items.contains(updatedItem))
				{
			    	PersistentItem itemInList = items.get(items.indexOf(updatedItem));
					updatedItem.copyTo(itemInList);
				}
				else
				{
					
					items.add(updatedItem);
				}
				//saveGroceryItems(FileNameConstants.CurrentFileName, items);
				saveGroceryFile(FileNameConstants.CurrentFileName, _groceryListSerializer.writeList(items));
				
    		} catch (Exception e) {
				
			//	_errorMessage = e.getMessage();
			}
    		
    	}


    }
	@Override
	public FileNameConstants getFileNameConstant() {
		// TODO Auto-generated method stub
		return FileNameConstants.MasterFileName;
	}
  
    /*private int findItemWithName(String name)
    {
    	ArrayList<GroceryItem> itemsListed = this.getItemsDisplayed();
    	GroceryItem itemFound = null;
    	int pos = 0;
    	for (GroceryItem item : itemsListed)
    	{
    		if (item.getName().equals(name))
    		
    		{
    			itemFound = item;
    			break;
    		}
    		else 
    		{
    			pos++;
    		}
    		
    	}
    	if (itemFound == null)
    	{
    		pos = -1;
    	}	
    	return pos;
    }
*/
}
