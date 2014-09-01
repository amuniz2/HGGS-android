package com.amlm.honeygogroceryshopping.activities;

import java.util.ArrayList;
import java.util.EnumMap;

import roboguice.RoboGuice;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.adapters.GroceryStoreAdapter;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryStore;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryStoreManager;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.google.inject.Injector;

public class MainActivity extends GroceryFileConsumer {
	@InjectView(R.id.GoShopping) Button _btnGoShopping;
	@InjectView(R.id.CreateGroceryList) Button _btnStartList;
	@InjectView(R.id.EditGroceryList) Button _btnContinueList;
//  @InjectView(R.id.groceryStoresAsRadioGroup) RadioGroup _groceryStores;
	  @InjectView(R.id.lvGroceryStores) ListView _lvGroceryStores;
    @InjectView(R.id.add_button) ImageButton _btnAddGroceryStore;
    @InjectView(R.id.edit_item) ImageButton _btnEditGroceryStores;
    @InjectView(R.id.activityButtons) LinearLayout _activityButtons;
    @InjectView(R.id.editButtons) LinearLayout _btnsEdit;
     

    /* data initialization */
    //static protected EnumMap<FileNameConstants, String> FileNames;
	private Intent _pendingIntent;
	private boolean _modified;
	private String _currentGroceryStore;
    private ArrayList<IGroceryStore> _groceryStores;
    private ArrayList<IGroceryStore> _removedGroceryStores;
    
    private IGroceryStoreManager _groceryStoreManager;
    private GroceryStoreAdapter _adapter = null;
    protected String getCurrentGroceryStore() { return _currentGroceryStore; }
    protected void setCurrentGrocyerStore(String storeName){ _currentGroceryStore = storeName; }
	public boolean getModified() {return _modified;}
	public void setModified(boolean value){_modified = value;}
    //public String getSelectedStoreName(){ return _selectedStoreName;}
    //public void setSelectedStoreName(String value) {_selectedStoreName=value;}
    public GroceryStoreAdapter getAdapter() { return _adapter;}
    
    public ArrayList<IGroceryStore> getRemovedGroceryStores(){ return _removedGroceryStores;}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	GroceryFile.setGroceryFilesDir(this.getApplicationContext());
        initializeStoreList();
       
    /*    RoboGuice.setBaseApplicationInjector(getApplication(), 
                RoboGuice.DEFAULT_STAGE, new GroceryFileModule());
      */  
 
        
        
    }

    private void initializeFilesForSelectedGroceryStore()
    {
    	String currentlySelectedGroceryStoreName = this.getCurrentlySelectedGroceryStoreName();
    	if (!currentlySelectedGroceryStoreName.equalsIgnoreCase(this.getCurrentGroceryStore()))
    	{
    		identifyGroceryFiles(currentlySelectedGroceryStoreName);
    		super.setInitialized(false);
    		initialize();
    		this.setLastSelectedGroceryStoreName(currentlySelectedGroceryStoreName);
    		this.setCurrentGrocyerStore(currentlySelectedGroceryStoreName);
    	}
    	else
    	{
    	//	identifyGroceryFiles(currentlySelectedGroceryStoreName);
    		onFilesInitialized();
    	}
    	
    }
    private void toggleEditMode(boolean edit)
    {
    	getAdapter().setEditMode(edit);
    	getAdapter().notifyDataSetChanged();
    	this._btnAddGroceryStore.setVisibility((edit) ? View.VISIBLE : View.GONE);
    	this._btnEditGroceryStores.setVisibility((edit) ? View.GONE : View.VISIBLE);
    	this._activityButtons.setVisibility((edit)? View.GONE : View.VISIBLE);
    	this._btnsEdit.setVisibility((edit)? View.VISIBLE : View.GONE );
    	
    	LinearLayout listLayout = (LinearLayout)_lvGroceryStores.getParent() ;
    	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listLayout.getLayoutParams();
    	if (edit)
    	{
    		//in edit mode, the list view can take up as much space as it needs
    		params.height= LayoutParams.WRAP_CONTENT;
    		params.weight=0;
    		
    	}
    	else
    		{
    		params.height=0;
    		params.weight =1;
    		
    		}
    	listLayout.setLayoutParams(params);
    	
    }
    public void cancelEdit(View v)
    {
    	ArrayList<IGroceryStore> stores = getStores();
    	// (async)
    	//  (1) delete getRemovedGroceryStores()
    	for (IGroceryStore store : getRemovedGroceryStores())
    	{
    		stores.add(store);
    	}
    	
    	//add the stores that were removed back
    	toggleEditMode(false);
    }
    public void applyEdit(View v)
    {
    	if ((saveGroceryStoreChangesToMemory()) || (this.getRemovedGroceryStores().size() > 0))
    	{
    		asyncUpdateGroceryStores();
    	}
    	else
    	{
    		toggleEditMode(false);
    	}
    	//return ret;
    }
    public void editStores(View v)
    {
    	toggleEditMode(true);
    }
    public void stopEditingStores()
    {
    	toggleEditMode(false);
    }
    
    public void deleteStore(View v)
    {
    	removeStore((GroceryStore) v.getTag());	
    }
    private void removeStore(GroceryStore storeToRemove)
    {
    	ArrayList<IGroceryStore> displayedItems = getStores();
    	
       	if (displayedItems.contains(storeToRemove))
	    {
       		getRemovedGroceryStores().add(storeToRemove);
       		displayedItems.remove(storeToRemove);
	    	setModified(true);
	    	getAdapter().notifyDataSetChanged();
	    	//this.saveCategories(catsToSave);           	
	    }

    }
    public void addStore(View v)
    {
    /*	RadioButton newRdb = new RadioButton(this);
    	LayoutParams layout= _groceryStores.getChildAt(0).getLayoutParams();
    	newRdb.setLayoutParams(layout);
    	
    	_groceryStores.addView(newRdb);
    	EditText et = new EditText(this);
    	et.setText(R.string.new_grocery_story_name);
    	et.setLayoutParams(new LayoutParams(-2, -2));
    	Intent intent = new Intent(this, EditCurrentItemActivity.class);
       	intent.putExtra(BaseEditItemActivity.EXTRA_ITEM, newItem);
       	
    	this.startActivityForResult(intent, ACTIVITY_ADD);
    	_groceryStores.addView(et);
    	et.setBackgroundResource(drawable.rounded_border);
    	*/

       	/*Intent intent = new Intent(this, CreateGroceryStoreActivity.class);
         intent.putExtra(BaseEditItemActivity.EXTRA_ITEM, _currentGroceryStore);
       	
    	this.startActivityForResult(intent, BaseListActivity.ACTIVITY_ADD)	
    	*/
    	Injector injector = RoboGuice.getInjector(this);
    	saveGroceryStoreChangesToMemory();
    	
    	IGroceryStore newGroceryStore = injector.getInstance(IGroceryStore.class);
 	    ArrayList<IGroceryStore> stores = this.getStores();
 	    stores.add(newGroceryStore);
 	    //todo: scrollTo(stores.size()-1);
 		getAdapter().notifyDataSetChanged();
 				    
 	    
    }
    /*@Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
	   processActivityResult(requestCode, resultCode, data);
    } 
    */  
    public String getCurrentlySelectedGroceryStoreName()
    {
    	return _adapter.getSelectedItem().getName();
    }
    private void setLastSelectedGroceryStoreName(String storeName)
    {    	
    	SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
    	Editor editor = prefs.edit();
    	editor.putString("Selected_Grocery_Name", storeName);
    	editor.apply();

    	
    }
    private IGroceryStore getLastSelectedGroceryStore()
    {
    	IGroceryStore ret = null;
    	SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
    	String storeName= prefs.getString("Selected_Grocery_Name", GroceryFile.defaultStoreName);
    	for (IGroceryStore store : this.getStores())
    	{
    		if (store.getName().equals(storeName))
    		{
    			ret = store;
    			break;
    		}
    	}
    	return ret;
    	
    	
    }
    protected  void identifyGroceryFiles(String storeName)
    {
    	
    	// if using external storage
    	GroceryFile.JsonFiles = new EnumMap<FileNameConstants, IGroceryFile>(FileNameConstants.class); 
    	
    	identifyGroceryFile(storeName, FileNameConstants.MasterFileName, getString(R.string.fn_master_list), true);		
    	identifyGroceryFile(storeName, FileNameConstants.CurrentFileName, getString(R.string.fn_current_list), true);
    	identifyGroceryFile(storeName, FileNameConstants.ShoppingListFileName, getString(R.string.fn_shopping_list),false);
    	identifyGroceryFile(storeName, FileNameConstants.CategoryListFileName,getString(R.string.fn_grocery_sections), true);

    	// todo: set global setting identifying the current grocery store
    	GroceryStoreManager.setCurrent(storeName);
    	
    }
  
    private  void identifyGroceryFile(String storeName, FileNameConstants efn, String fn, boolean share)
    {
	      
	    	Injector injector = RoboGuice.getInjector(this);
	       
	    	IGroceryFile gfile=injector.getInstance(IGroceryFile.class);
	    	
	    	gfile.init( storeName, fn, share);
	    	if (!gfile.exists())
	    	{
	    		try {
					gfile.createFromAssets();
				}
	    		catch (Exception e) {
					
					reportError(getTag(), e,this);
				}
	    	}
	    	GroceryFile.JsonFiles.put(efn, gfile);
	    	
	    	
    }
  
   
   
    public void editGroceryList(View view) 
    {
    
    	startActivity(createEditGroceryListIntent());    	
    	
    }
    public void goShopping(View view) 
    {
       	Intent intent = new Intent(this, ShoppingListActivity.class);
       	//intent.putExtra(BaseListActivity.CREATE_NEW_LIST, false);
    	startActivity(intent);    		
    }
    
    @Override
    public void startActivity(Intent intent)
    {
    	if (_adapter.getSelectedItem() == null)
    		{
this.displayMessage("Select store", "Select a grocery store.");    		
    		}
    	else
    		{
    		_pendingIntent = intent;
    	this.initializeFilesForSelectedGroceryStore();
    		}
    }
    protected void onUpdateFinished(String result)
    {
    	toggleEditMode(false);
    	
    
    }
    @Override
    protected void onFilesInitialized()
	 {
	    super.onFilesInitialized();    
		super.startActivity(_pendingIntent);
		
    	

	 }
    public void createGroceryList(View view) 
    {
       	
    	startActivity(createNewGroceryListIntent());    	
    	
    }
    
    public Intent createEditGroceryListIntent()
    {
       	Intent intent = new Intent(this, EditCurrentListActivity.class);
    	//Intent intent = new Intent(this, EditCurrentListActivity.class);
       	intent.putExtra(BaseListActivity.CREATE_NEW_LIST, false);   
       	return intent;
    }
    public Intent createNewGroceryListIntent()
    {
    	Intent ret = new Intent(this, EditCurrentListActivity.class);
    	//Intent intent = new Intent(this, EditCurrentListActivity.class);
       	ret.putExtra(BaseListActivity.CREATE_NEW_LIST, true);
       	return ret;
    }
    public void exit(View view)
    {
    	this.finish();
    }
    // made public for testing!
    @Override
    public void onResume()
    {
   
    		/*
    		 * todo: when a store name is selected, run the logic below, instead of on resume
    		 * boolean shoppingListExists = GroceryFile.JsonFiles.get(FileNameConstants.CurrentFileName).exists();
  			this._btnContinueList.setEnabled(shoppingListExists);
			this._btnGoShopping.setEnabled(shoppingListExists);
  			*/
     	super.onResume();
    }



	
    protected  ArrayList<IGroceryStore> getStores()
    {    	
    	
    	
	return _groceryStores;
    }	
    protected void setStores(ArrayList<IGroceryStore> value){ _groceryStores = value;}
	private void initializeStoreList()
	{
		Injector injector = RoboGuice.getInjector(this);
		_groceryStoreManager = injector.getInstance(IGroceryStoreManager.class);
		setStores(_groceryStoreManager.getGroceryStores());
		_removedGroceryStores = new ArrayList<IGroceryStore>();
		//_currentGroceryStore = getSelectedGroceryStoreName();
		setupList(getStores());
	}
	protected void setupList(ArrayList<IGroceryStore> itemsToDisplay) {
		
		
		
     	// First parameter - Context
    	// Second parameter - Layout for the row
    	// Forth - the Array of data
		 _adapter = new GroceryStoreAdapter(this,
				 /*android.R.layout.simple_list_item_single_choice,*/
     			R.layout.list_item_text_view,
    			itemsToDisplay);
 	
     	_lvGroceryStores.setAdapter(_adapter);
     	_adapter.setNotifyOnChange(true);
      	_lvGroceryStores.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      	//this.setCurrentGrocyerStore(this.getLastSelectedGroceryStoreName());
      	IGroceryStore selectStore = this.getLastSelectedGroceryStore(); 
      	_adapter.setSelectedItem((selectStore == null)?itemsToDisplay.get(0) : selectStore);
      	// set the selected store per what is in the preferences
    	/*_groceryStores.setOnItemClickListener(new OnItemClickListener() 
    	{
    		public void onItemClick(AdapterView<?> listView, View cell,
    			int position,
                long id) 
    		{      
    			
    			if  (!_adapter.isSelected(position))
    			{
    	        	 _adapter.setSelected(position);
    	        	 _adapter.notifyDataSetChanged();

    			}
    	        		
           
    		}
    	});*/   
    	//_adapter.setSelected(0);

		
	}

	public void selectGroceryStore(View v)
	{
		int position = _lvGroceryStores.getPositionForView(v);
		_lvGroceryStores.setItemChecked(position, true);
		_adapter.setSelected(position);
		
	}
	@Override
	public String getTag() {
		
		return "Main Activity";
	}
	 /*public void processActivityResult (int requestCode, int resultCode, Intent data)
	   {
	       	int pos =Adapter.NO_SELECTION;

	    	if (resultCode != RESULT_CANCELED)
	        {
	        		
				//@SuppressWarnings("unchecked")
	    		_currentGroceryStore = (String)(data.getSerializableExtra(BaseEditItemActivity.EXTRA_ITEM));
	    		this.asyncCreateGroceryStore(_currentGroceryStore);
				//to do update the list!
	           	getAdapter().setSelected(pos);
	           	getAdapter().notifyDataSetChanged();
	           	//saveList(itemsToSave);
	        }
	        
		   
	   }
	   */    
	 private class GroceryStoreUpdater  extends AsyncTask<IGroceryStore,Void,String>
	 {
		 private ProgressDialog _dialog = null;
		 @Override 
	        protected void onPreExecute()
	        {
	        	_dialog = reportAsyncActivityToUser("Updating Grocery Stores...");
	        	
	        }
		@Override
		protected void onPostExecute(String result)
		{
			onUpdateFinished(result);
			_dialog.hide();
		}
		@Override
		protected String doInBackground(IGroceryStore... arg0) {
			String ret = "";
			String storeName;
			
			try {
			  	// (async)
		    	//  (1) delete getRemovedGroceryStores()
		    	for (IGroceryStore store : getRemovedGroceryStores())
		    	{
		    		storeName = store.getName();
		    		if (storeName.equals(getCurrentlySelectedGroceryStoreName()))
		    		{
		    			// select another grocery store
		    			_adapter.setSelected(-1);
		    		}
		    		GroceryFile.deleteGroceryStore(store.getName());
		    		
		    		// if file is being shared, should we delete the dropbox file here?
		    	}
		    	//  (2) walk through each grocery store:
		    	for (IGroceryStore store : arg0)
		    	{
		    		storeName = store.getName();
//		    	     if original name != new name
		    		if (!storeName.equals(store.getOriginalName()))
		    		{
		        	//			if original name is not empty
		    			if (!store.getOriginalName().isEmpty())
		    			{
		    				//				rename store
		    				if (GroceryFile.RenameGroceryStore(store.getOriginalName(), storeName) )
		    				{
		    					//    set the original name to the new name
		    					store.setOriginalName(storeName);
		    					
		    					// if file is being shared, should we update dropbox here?
		    				}
		    			}
		        	    else
		        	    {
		        	    	GroceryFile.CreateGroceryStoreFolder(storeName);
			        	    
			        	    // set preference to not share for the new grocery store
			        	    store.setEnableSharing(false);
		        	    	identifyGroceryFiles(storeName);
		        	    }
		    		}
		    	}
		    	

				
				//gfile.createFromAssets();
				ret = "Stores updated.";
			} catch (Exception e) {
				
				ret = e.getMessage();
			}
			return ret;
		}
		 
	 }
	 public void asyncUpdateGroceryStores() 	{
	    	
		 	
	    	GroceryStoreUpdater storeUpdater = new GroceryStoreUpdater();
	    	ArrayList<IGroceryStore> stores = getStores();
	    	IGroceryStore[] storesToUpdate = new IGroceryStore[stores.size()];
	    	storesToUpdate = stores.toArray(storesToUpdate); 
	    	storeUpdater.execute(storesToUpdate);
	    	//this.saveGroceryFile(efn, JsonItemList.writeList(items));
	    	
			
		}
	 public boolean saveGroceryStoreChangesToMemory()
	 {
		 GroceryStoreAdapter adapter = this.getAdapter();
		 return adapter.saveStoresThatHaveChanged();
		
	 }
}
