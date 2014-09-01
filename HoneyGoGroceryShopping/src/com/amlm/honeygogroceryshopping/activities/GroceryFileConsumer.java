package com.amlm.honeygogroceryshopping.activities;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.dataaccess.DataCache;
import com.amlm.honeygogroceryshopping.dataaccess.DropboxFile;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.ICategoryListSerializer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IItemListSerializer;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.google.inject.Inject;

public abstract class GroceryFileConsumer extends BaseActivity  {
	protected static final String DROPBOX_REVISIONS = "DbFileRevisions";
	
	static final FileNameConstants[] _gfilenames = {
		FileNameConstants.CategoryListFileName,
		FileNameConstants.CurrentFileName,
		FileNameConstants.MasterFileName,
		FileNameConstants.ShoppingListFileName	
	};	
	public GroceryFileConsumer(){ super();}
    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
	final static private String APP_KEY = "t7bb846jrivfl07";
	final static private String APP_SECRET = "cmft478dvllzue3";
    final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////
	   // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    final static private String DROPBOX_ENABLED = "DROPBOX_ENABLED";
    public final static String SHARING_PREFS_NAME = "SHARING";

    @Inject   ICategoryListSerializer _categoriesSerializer;
    @Inject   IItemListSerializer _groceryListSerializer;
		     
	// In the class declaration section (should we make this static?)
	private static   DropboxAPI<AndroidAuthSession> _dbApi = null;
//	private boolean _checkIfDropboxHasFiles  = false;
	//private String _errorMessage;
//	protected boolean getCheckIfDropboxHasFiles() {return _checkIfDropboxHasFiles;}
	public  static DropboxAPI<AndroidAuthSession> getDBApi()
	{
		
		return _dbApi;
	}
	 public  static void setDBApi(DropboxAPI<AndroidAuthSession> dbapi)
	 {
		 _dbApi = dbapi;
	 }
	 private static boolean _initializing = false;
	 private static boolean  _initialized = false;
	 protected static  boolean IsInitializing() {return _initializing;}
	 protected static  boolean IsInitialized() {return _initialized;}
	 protected static  void setInitialized(boolean value)
	 { 
		 _initialized = false;
	 }
	 
	 public static void reset()
	 {
		 _initialized = false;
	 }
	// public static void setInitialized(boolean value){_initialized = false;} // only providing this for unit test ?!
	    private static void initGroceryFiles(SharedPreferences prefs) throws IOException, JSONException
	    {
	    	
	    	for (FileNameConstants efn : GroceryFile.JsonFiles.keySet())
	    	{
	    		initGroceryFile(efn, prefs);
	    	}
	    	
	    	IGroceryFile catFile = GroceryFile.JsonFiles.get(FileNameConstants.CategoryListFileName);
	    	if (!catFile.exists())
	    	{
	    	
					catFile.createFromAssets();
				 
	    		
	    	}	
	    }
	    
	    private static void initGroceryFile(FileNameConstants efn, SharedPreferences prefs)
	    {
	    	//IGroceryFile gfile = new GroceryFile(getDBApi(), fn, share);
	    	
	    	//Injector injector = Guice.createInjector(new GroceryFileModule());
	    	//gfile.init(getDBApi(), GroceryFile.defaultStoreName, fn, share);
	   
	    	IGroceryFile gfile = GroceryFile.JsonFiles.get(efn) ;
	    	gfile.setRevision(readDbRev(gfile, prefs));
	    }
			 
	 
	 @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	 }	
	/* protected void onDropboxInitialized()
	 {
	        
		 // now do the fle initialization with dropbox 
		initialize();

	 }
	 */
	 protected void onFilesInitialized()
	 {
	        
		try
		{
			initGroceryFiles(getSharedPreferences(DROPBOX_REVISIONS, 0));
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			BaseActivity.reportError(getTag(), e, this);
		}
		_initializing = false;
        _initialized = true;
		
     	

	 }
	   
	  
	 protected boolean isLoggedIn()
	 {
		 boolean ret = false;
			DropboxAPI<AndroidAuthSession> dbapi = getDBApi();
		
			if (dbapi != null)
		     {
				AndroidAuthSession session = dbapi.getSession();
				if (session != null)
				{
					ret = dbapi.getSession().isLinked();
		            // Display the proper UI state if logged in or not
				}
			}
			return ret;
		 
	 }
    protected void initialize()
    {
    	if (!IsInitializing() && !IsInitialized())
		{
        	_initializing = true;
        	//SharedPreferences prefs = getSharedPreferences(DROPBOX_REVISIONS, 0);
    		FileInitializer task = new FileInitializer(/*prefs*/);
    		task.execute();

			
		}
  

    }
  
    
    
    // always gets cached categories, never from file, which must happen async.
    protected ArrayList<Category> getCategories()
    {
    	
    	return DataCache.getCategories();
    }
    
	    
	    /**
	     * Shows keeping the access keys returned from Trusted Authenticator in a local
	     * store, rather than storing user name & password, and re-authenticating each
	     * time (which is not to be done, ever).
	     *
	     * @return Array of [access_key, access_secret], or null if none stored
	     */
	    private String[] getKeys() {
	        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
	        String key = prefs.getString(ACCESS_KEY_NAME, null);
	        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
	        if (key != null && secret != null) {
	        	String[] ret = new String[2];
	        	ret[0] = key;
	        	ret[1] = secret;
	        	return ret;
	        } else {
	        	return null;
	        }
	    }
		protected void setDropboxOn(Boolean val)
		{
			
	        IGroceryFile gfile;
			SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
	        Editor edit = prefs.edit();
	        edit.putBoolean(DROPBOX_ENABLED, val);		
	        if (!val)
		    {
		    	   // need to reset revision numbers of dropbox files 
		        String initRev = new String("");
		    	for (FileNameConstants fn : _gfilenames)	
				{
					gfile = GroceryFile.JsonFiles.get(fn);
					gfile.setRevision(initRev);
					edit.putString(gfile.getRevisionKey(), initRev);		
					edit.putBoolean(gfile.getDbPendingKey(), false);		
				}	
		    }
	        edit.commit();
	      
		}
		      
		  protected Boolean isDropboxEnabled()
		    {
		    	 SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		         Boolean ret = prefs.getBoolean(DROPBOX_ENABLED, false);
		         return ret;
		    }
		  
		  
		    /**
		     * Shows keeping the access keys returned from Trusted Authenticator in a local
		     * store, rather than storing user name & password, and re-authenticating each
		     * time (which is not to be done, ever).
		     */
		    protected void storeKeys(String key, String secret) {
		        // Save the access key for later
		        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		        Editor edit = prefs.edit();
		        edit.putString(ACCESS_KEY_NAME, key);
		        edit.putString(ACCESS_SECRET_NAME, secret);
		        edit.commit();
		    }

		    protected void clearKeys() {
		        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		        Editor edit = prefs.edit();
		        edit.clear();
		        edit.commit();
		    }
		    
		    protected boolean finishDbAuth() {
				boolean ret = false;
		    	AccessTokenPair tokens;
				AndroidAuthSession authSession = null;
			
				DropboxAPI<AndroidAuthSession> dbapi = getDBApi();
				if (dbapi != null)
				{
					authSession = dbapi.getSession();
					if ((authSession != null) && (authSession.authenticationSuccessful()))
					{
		    	
						authSession.finishAuthentication();
						tokens = authSession.getAccessTokenPair();
						if (tokens != null)
							{
							storeKeys(tokens.key, tokens.secret);
							}
						ret = true;
					}	
					
				}
				return ret;
		    }
		    protected void logOut() {
		        // Remove credentials from the session
		        DropboxAPI<AndroidAuthSession> api = getDBApi();
		        if (api != null)
		        	{
		        	api.getSession().unlink();
		        	}
		        // Clear our stored keys
		        clearKeys();
		        // Change UI state to display logged out version
		       
		    }
		    protected void startLogin()
		    {
		    	DropboxAPI<AndroidAuthSession> api = getDBApi();
		    	if (api == null)
		    	{
	    			api = buildSession();

		    	}
		    	if (api != null)
		    	{
		    		AndroidAuthSession session = api.getSession();
		    		if (session != null)
		    		{
		    			session.startAuthentication(this);
		    		}
		    	}
		    	
		    }
		  
		    protected String readGroceryFile(FileNameConstants fn)
		    {
		    	
		    	
		    	
		    	String ret = new String("");
		    	IGroceryFile gfile = GroceryFile.JsonFiles.get(fn);
		    	
		    	try {
					ret =gfile.read();
					if (gfile.getDBRevNeedsUpdating())
					{
						updateDbRev(gfile);
					}
				} catch (Exception e) {
					setErrorMessage(e.getMessage());
				}
		    	return ret;
		    	
		    }
		    protected void saveGroceryFile(FileNameConstants fn, String contents)
		    {
		    	IGroceryFile gfile = GroceryFile.JsonFiles.get(fn);
		    	try {
		    		
					gfile.save(contents);
			    	if (!gfile.getSaveToDbPending())
			    	{
			    	
			    		// update the dbrev
			    		updateDbRev(gfile);
			    	}
			    	else
			    	{
			    		// update the preferences to indicate that there is save to db pending
			    		updateDbPendingFlag(gfile, true);
			    	}				
			    } 
		    	catch (Exception e) {
					setErrorMessage( e.getMessage());
				}

		    }
		    private void updateDbPendingFlag(IGroceryFile gfile, boolean value)
		    {
				SharedPreferences prefs = this.getSharedPreferences(DROPBOX_REVISIONS, 0);
		        Editor edit = prefs.edit();
		        
		        edit.putBoolean(gfile.getDbPendingKey(), value);		
		        edit.commit();
		    	
		    }
		    protected  static String readDbRev(IGroceryFile gfile, SharedPreferences prefs)
			{
				String ret = new String ("");
				//SharedPreferences prefs = getSharedPreferences(DROPBOX_REVISIONS, 0);
		        
		        if (prefs != null)
		        	{
		        	ret =prefs.getString(gfile.getRevisionKey(), ret);		
		        	}
		        return ret;
		 
			}
		    
		    protected void updateDbRev(IGroceryFile gfile)
			{
				SharedPreferences prefs = this.getSharedPreferences(DROPBOX_REVISIONS, 0);
		        Editor edit = prefs.edit();
		        
		        edit.putString(gfile.getRevisionKey(), gfile.getRevision());		
		        edit.putBoolean(gfile.getDbPendingKey(), false);		

		        edit.commit();

				//todo: update rev file for store
			}
				
			
			 
		
			public ArrayList<GroceryItem> loadGroceryItems( FileNameConstants efn)
					throws IOException {
			      ArrayList<GroceryItem> ret = loadList(efn);;
			
			       ret.trimToSize();     
			       //Collections.sort(ret);
			  
			       return ret;
				
			}
					
			
		
			
			public ArrayList<Category> loadCategories() throws IOException {
			       ArrayList<Category> ret = this.loadCats();
			       ret.trimToSize();
			       //Collections.sort(ret);
			       DataCache.setCategories(ret);
			       return ret;
			 }
	/*		@Override
			public void setContext(Context context) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public Context getContext() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public void storeKeys(DropboxInfo dbInfo) throws IOException,
					JSONException {
				// TODO Auto-generated method stub
				
			}
			@Override
			public DropboxInfo loadKeys() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		*/	
			
			
			// dataAccessor private methods
			   private ArrayList<GroceryItem> loadList(FileNameConstants efn) throws IOException 
			    {
				   
			    	ArrayList<GroceryItem> groceryItems = null;
			     
			         try
			        {
			        	 
					    	
			        	 String fileContents = this.readGroceryFile(efn);
			         	 //gfile.read(this.getContext());
			         	 groceryItems = _groceryListSerializer.parseList(fileContents);  
			       
			        }
			         catch(Exception exception )
			         {
			             groceryItems = new ArrayList<GroceryItem>();
			         }
			      

			         
			     // this.setGroceryList(groceryItems);
			      return groceryItems;

			     
			    }
			    
			   private ArrayList<Category> loadCats() throws IOException 
			    {
			    	ArrayList<Category> cats = null;
			       
			        
			         try
			        {
			          	String fileContents = this.readGroceryFile(FileNameConstants.CategoryListFileName);
			         	cats = ((fileContents == null) || (fileContents.isEmpty()) ? createDefaultCategoryList() : _categoriesSerializer.parseList(fileContents));
			         	 
			        }
			         catch(Exception exception )
			         {
			        	 cats = createDefaultCategoryList();
			         }
			      

			        return cats;

			     
			    } 
			    private ArrayList<Category> createDefaultCategoryList() 
			    {
			    	// todo: replace this with loading default categories.json
			    	// using assetManager
		        	 Category defaultCat = new Category();
		             ArrayList<Category> cats = new ArrayList<Category>();
		             defaultCat.setId(GroceryItem.DEFAULT_CATEGORY_ID);
		             defaultCat.setName(getResources().getString(R.string.default_category_name));
		             //defaultCat.setOrder(0);
		             cats.add(defaultCat);
		             return cats;

			    }
			   void setErrorMessage(String errorMessage) {
				//   _errorMessage = errorMessage;
			}
			   protected  DropboxAPI<AndroidAuthSession> buildSession() {
			        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
			        AndroidAuthSession session;
			        DropboxAPI<AndroidAuthSession> dbapi = getDBApi();
			        

			        if (dbapi == null)
			        {
			        	String[] stored = getKeys();
			        	if (stored != null) {
			        		AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
			        		session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
			        	//	_checkIfDropboxHasFiles = true;
			        	} 
			        	else 
			        	{
			        		session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
			        	}
			        	if (session != null)
			        	{
			        		dbapi = new DropboxAPI<AndroidAuthSession>(session);
			        		setDBApi(dbapi);
				
			        	}
			        }
			        return dbapi;
			    }    
			   private void initializeDropboxFiles()
			   {
	    			DropboxAPI<AndroidAuthSession> 	dbapi = buildSession();
	    			if ((dbapi != null) && dbapi.getSession().isLinked())
	    				{
	    					synchronizeFilesWithDropbox();
	    				} // if dropbox linked
			   		
			   }
				private void synchronizeFilesWithDropbox()
				{
					IGroceryFile gfile;
					for (FileNameConstants fn : _gfilenames)	
					{
						gfile = GroceryFile.JsonFiles.get(fn);
						if (gfile.getSaveToDbPending())
						{
							DropboxFile dbFile = new DropboxFile(gfile);
							// we saved a file locally but were unable to save to 
							// dropbox folder
							// if the db revisions still match (i.e., no one else
							// has updated the dropbox folder), then save the modified file to 
							// dropbox
							// either way, clear the flag
							if (!gfile.revisionsMatch())
							
							{
								try {
									dbFile.saveToDropboxAppFolder();
									//gfile.syncWithDropbox(context);
								} 
								catch (Exception e) 
								{
									// TODO Auto-generated catch block
									//_errorMessage= e.getMessage();
								}
							} // if revisions are not the same
						} // if save to db pending
					} // for each grocer file
				
				} // synchronisefileswithdropbox
   
			private class FileInitializer extends AsyncTask<Void,Void,Void>
				{
				  	private ProgressDialog _dialog = null;
			    	//private String _errorMessage = null;
				  	/*private SharedPreferences _prefs;
					public FileInitializer(SharedPreferences prefs)
					{ _prefs = prefs;}
			        */
				  	@Override 
			        protected void onPreExecute()
			        {
			        	_dialog = reportAsyncActivityToUser(getString(R.string.uploading_latest_files));
			        }
			       
			        @Override
			        protected void onPostExecute(Void result)
			        {
			        	_dialog.hide();
			        	onFilesInitialized();
			        }
			        
					@Override
					protected Void doInBackground(Void... params) 
					{
			    		try 
			    		{
			    			DropboxAPI<AndroidAuthSession> dbapi = getDBApi();
				    			    		
			    			if ( (isDropboxEnabled()) && (dbapi == null))
			    			{

			    				initializeDropboxFiles();
			    			}
			    		}
			    		catch (Exception e)
			    		{
			    			setErrorMessage(e.getMessage());
			    		}
		    			try 
		    			{
		    				
		    						loadCategories();
		    					
		    			} catch (IOException e) 
		    			{
							//	_errorMessage = e.getMessage();
									// TODO Auto-generated catch block
									//e.printStackTrace();
		    			}
						return null;
						
					} // do in backgroud
	
					
				} // inner class
							
		}
	


