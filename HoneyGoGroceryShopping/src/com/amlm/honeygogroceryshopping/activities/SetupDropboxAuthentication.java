package com.amlm.honeygogroceryshopping.activities;

import java.util.Map;

import roboguice.RoboGuice;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.dataaccess.DropboxFile;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryStoreManager;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.dropbox.client2.exception.DropboxException;
import com.google.inject.Injector;

public  class SetupDropboxAuthentication extends GroceryFileConsumer //implements ConfirmReturn.DialogReturn 
{
	@InjectView(R.id.auth_button)  Button _submit;
	@InjectView(R.id.sharing_on) CheckBox _cbSharingOn;
	@InjectView(R.id.prompt_user_section) LinearLayout _fileTransferPrompt;

	public static final int RESULT_OVERWRITE = RESULT_FIRST_USER;
	
	public SetupDropboxAuthentication(){ super();}
    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

	private Boolean _authenticatingWithDropbox = false;
	//private ConfirmReturn<Object> _confirmDialog;
	
    // final static private String DROPBOX_ENABLED = "DROPBOX_ENABLED";
    private static final String dbAuthState = "AuthenticatingWithDropBox"; 

   
    private boolean _loggedIn = false;
    private IGroceryStoreManager _groceryStoreManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_dropbox_authentication);
    
		Injector injector = RoboGuice.getInjector(this);
		_groceryStoreManager = injector.getInstance(IGroceryStoreManager.class);

        _fileTransferPrompt.setVisibility(View.INVISIBLE);
        	restoreState(savedInstanceState);
        	
        	// _confirmDialog = new ConfirmReturn<Object>();
            // _confirmDialog.setListener(this);     
        	_cbSharingOn.setText(String.format(getString(R.string.share_store_lists),GroceryStoreManager.getCurrent()));

        
    }
    /*public void confirmOverwrite()
    {
    	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		dlgAlert.setMessage(R.string.confirm_overwrite);
		dlgAlert.setTitle(R.string.confirm);
		dlgAlert.setPositiveButton(R.string.yes,
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	_confirmDialog.getListener().onConfirmed(true); 
			        }
			    });    	
		dlgAlert.setNegativeButton(R.string.no, 
			    new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	_confirmDialog.getListener().onConfirmed(false); 
	        }
	        });
		dlgAlert.setCancelable(true);
		dlgAlert.create();
		dlgAlert.show();			    	
    
    }    
*/
	private void restoreState(Bundle savedInstanceState) {
		if (savedInstanceState != null)
		{
			_authenticatingWithDropbox= savedInstanceState.getBoolean(dbAuthState, false);
		}
		updateViewPerCurrentState();
        

	}
	private void updateViewPerCurrentState()
	{
		updateViewPerCurrentState(super.isDropboxEnabled());
	}

	private void updateViewPerCurrentState(boolean dbOn )
	{
		
		//Boolean loggedIn = false;
		/*if (dbOn)
		{
			DropboxAPI<AndroidAuthSession> dbapi = getDBApi();
			if (dbapi == null)
			{
				
	            dbapi = buildSession();
			}
			if (dbapi != null)
		     {
				AndroidAuthSession session = dbapi.getSession();
				if (session != null)
				{
					loggedIn = dbapi.getSession().isLinked();
		            // Display the proper UI state if logged in or not
				}
			}
			
		}*/
		
		_submit.setEnabled(!dbOn || (dbOn && !areAnyFilesShared())); //should not be available if ANY files are being shared
		_cbSharingOn.setVisibility((dbOn) ? View.VISIBLE : View.INVISIBLE); // files can only be shared if dropbox is linked/enabled
		if (dbOn)
		{
			_submit.setText(R.string.dropbox_logout);
    		
  
			_cbSharingOn.setChecked(_groceryStoreManager.areGroceryFilesShared(this,GroceryStoreManager.getCurrent())); // need to determine if files are being shared for the current grocery store
			//setLoggedIn(super.isLoggedIn());  
		}
		else
		{
	  		_submit.setText(R.string.setup_dropbox_auth);
	  		 			//setLoggedIn(false);
		}
         
        
 	}
	
	protected void setDropboxOn(Boolean val)
	{
		
		super.setDropboxOn(val);
		
        updateViewPerCurrentState();
	}
	public  boolean areAnyFilesShared()
	{
		boolean ret = false;
		SharedPreferences prefs = getSharedPreferences(GroceryFileConsumer.SHARING_PREFS_NAME, 0);
		Map<String, ?> sharingPrefs = prefs.getAll();
		for (Map.Entry<String, ?> entry : sharingPrefs.entrySet())
		{
			Boolean val = (Boolean)entry.getValue();
			if (val)
			{
				ret = true;
				break;
			}
		}
		

		return ret;
	}
		
		
	
	private void startSharingFiles()
	{
        	StartSharing task = new StartSharing();
        	task.execute();
        	// if we have just turned dropbox ON, we need to:
        	// (1) determine if there are files available in the dropbox folder
        	// (2) if not, upload the local files to dropbox
        	// (3) if so, ask the user whether to download the files currently 
        	// in dropbox.  :
        	//     (3.a) If the user chooses to download, do normal read; 
        	//         (3.a.1) how do we update lists already in memory?
        	//     (3.b) If the user does NOT choose to download, 
        	//          (3.b.1) write files to dropbox folder and continue
        	// this has to happen async...
		
	}

	@Override
	public void onStop()
	{
		/*if (!this.isDropboxEnabled())
		{
			logOut();
			
		}
		*/
		super.onStop();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_setup_dropbox_authentication, menu);
        return true;
    }

    


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
  	  // Save UI state changes to the savedInstanceState.
		  // This bundle will be passed to onCreate if the process is
		  // killed and restarted.
		  savedInstanceState.putBoolean(dbAuthState, _authenticatingWithDropbox);
		  
		  // do we need to do the same for the sharing floag for the current grocery store?
		  super.onSaveInstanceState(savedInstanceState);
    }
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  this._authenticatingWithDropbox = savedInstanceState.getBoolean(dbAuthState);
	 
	}
	 @Override
	    protected void onResume() {
		 	boolean authSucceeded = false;
		 	
		 	super.onResume();
	    	if (_authenticatingWithDropbox)
	    	{
	    		
	    		authSucceeded = finishDbAuth();
	    		
	    		_authenticatingWithDropbox = false;
	    		updateViewPerCurrentState(authSucceeded);
	        }
	    	
	        
	    }	
 
	 @Override
	 protected void onPause()
	 {
		 // save the sharing preference for this grocery store
		 _groceryStoreManager.setGroceryStoreSharing(this,_cbSharingOn.isChecked());
		 
		 super.onPause();
	 }
   

    /**
     * Convenience function to change UI state based on being logged in
     */
    /*private void setLoggedIn(boolean loggedIn) {
    	_loggedIn = loggedIn;
    	if (loggedIn) {
    		_submit.setText(R.string.dropbox_logout);
    		updateViewPerCurrentState(true);
            //mDisplay.setVisibility(View.VISIBLE);
    	} else {
    		_submit.setText(R.string.setup_dropbox_auth);
    		updateViewPerCurrentState(false);
            //mDisplay.setVisibility(View.GONE);
       }
    	
    }
*/
       
   /* private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
*/
   
  
     @Override
    protected boolean finishDbAuth() {
		boolean ret = super.finishDbAuth();
		if (ret)
		{
			//setLoggedIn(true);
			this.setDropboxOn(true);
			this.updateViewPerCurrentState(true);
			// clear the cached categories so that the cache is refreshed
			//DataCache.setCategories(null);
		}
		return ret;
  	}
    public void onLinkOrUnlinkDropbox(View v)
	{
		  // This logs you out if you're logged in, or vice versa
        if (_loggedIn) {
            super.logOut();
            updateViewPerCurrentState(false);
        } else {
            // Start the remote authentication
            this._authenticatingWithDropbox = true;
            super.startLogin();
            
        }			
	}
    public void onToggleSharing(View v)
    {
    	//this._dropboxOn=this._cbSharingOn.isChecked();
    	//instead, toggle the sharing for this grocery store
    	//this.setDropboxOn(_dropboxOn);
    	if (this._cbSharingOn.isChecked())
    	{
    		_fileTransferPrompt.setVisibility(View.INVISIBLE);
    			startSharingFiles();
    	}
    	else
    	{
   		 _groceryStoreManager.setGroceryStoreSharing(this,false);
   		 updateViewPerCurrentState(true);
    	}
    }

    private void onFinishedFindingDbFiles(Boolean dbFilesExist, String errorMessage)
    {
    	if (errorMessage != null)
    	{
    		displayMessage(getTag(),errorMessage);
    		
            //updateViewPerCurrentState(false);
    	}
    	else if (dbFilesExist)
    	{
    		_fileTransferPrompt.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		updateViewPerCurrentState(true);
    	}
       
       
    }
    public void onProceed(View v)
    {
    	RadioGroup options = (RadioGroup) findViewById(R.id.enable_dropbox_options);
    	switch (options.getCheckedRadioButtonId())
    	{
    	case R.id.upload_dropbox_files:
    		// async task to upload files
    		{
    			FileUploader task = new FileUploader();
    			task.execute();
    		}
    		break;
    	case R.id.download_dropbox_files:
    		// async task to download files
    		{
    			FileDownloader task = new FileDownloader();
    			task.execute();
    		}
    		break;
    	case R.id.unlink_from_dropbox:
    		//sending the wrong v, but does this matter?
    		_cbSharingOn.setChecked(false); 
    		_fileTransferPrompt.setVisibility(View.INVISIBLE);
    		break;
    	}
    }
    protected void onFinishedFileTransfer(String err, boolean success)
    {
    	// disable the section to proceed
    	if (err != null)
    	{	
    		displayMessage("File Transfer", err);
    	}
    	else
    	{
    		
    		_groceryStoreManager.setGroceryStoreSharing(this,true);
   	
    		_fileTransferPrompt.setVisibility(View.INVISIBLE);
    		this.finish();
       		
    	}
    	
    }
    private class FileUploader extends AsyncTask<Void,Void,Boolean>
    {
    	private String _errorMessage = null;
  	private ProgressDialog _dialog;
    	
    	@Override
    	protected void onPreExecute()
    	{
        	_dialog = reportAsyncActivityToUser("Getting grocery lists...");
   		
    	}
	   	
		@Override
		protected Boolean doInBackground(Void... arg0) 
		{
			boolean noErrors = true;
	    	IGroceryFile gfile;
	    	for (FileNameConstants fn : _gfilenames)	
			{
	    	
				gfile = GroceryFile.JsonFiles.get(fn);
				if (gfile.isShared())
				{	
					DropboxFile dbFile = new DropboxFile(gfile);
					try {
						if (!dbFile.saveToDropboxAppFolder())
						{	
							noErrors = false; 
							break;
						}
						updateDbRev(gfile);
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						noErrors = false;
						_errorMessage = e.getMessage();
					}
				}
						
			}			
			return noErrors;
		}
		@Override
		protected void onPostExecute(Boolean result)
		{
			_dialog.hide();
   			onFinishedFileTransfer(_errorMessage, result);
		}

    }
    private class FileDownloader extends AsyncTask<Void,Void,Boolean>
    {
    	private String _errorMessage = null;
    	private ProgressDialog _dialog;
    	
    	@Override
    	protected void onPreExecute()
    	{
        	_dialog = reportAsyncActivityToUser("Getting grocery lists...");
   		
    	}
		@Override
		protected Boolean doInBackground(Void... arg0) 
		{
			
			boolean noErrors = true;
	    	IGroceryFile gfile;
	    	for (FileNameConstants fn : _gfilenames)	
			{
	    	
				gfile = GroceryFile.JsonFiles.get(fn);
				if (gfile.isShared())
				{	
					DropboxFile dbFile = new DropboxFile(gfile);
					try 
					{
						dbFile.getLatestRevision();
						updateDbRev(gfile);
						loadCategories();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						_errorMessage = e.getMessage();
					}
					
				}
						
			}			
			return noErrors;		}
		@Override
		protected void onPostExecute(Boolean result)
		{
			_dialog.hide();
   			onFinishedFileTransfer(_errorMessage, result);
		}

    }
    private class StartSharing extends AsyncTask<Void,Void,Boolean>
    {
    	String _errorMessage ;
     	
    
	   	
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
	    	IGroceryFile gfile;
	    	DropboxFile dbFile ; 
	    	Boolean dbFilesExist = false;
	    	for (FileNameConstants fn : _gfilenames)	
			{
				gfile = GroceryFile.JsonFiles.get(fn);
				if (gfile.isShared())
				{					
					// if we have just turned dropbox ON, we need to:
					// (1) determine if there are files available in the dropbox folder
					dbFile = new DropboxFile(gfile);
					try {
						if (!dbFile.fileExists())
						{
							// (2) if not, upload the local files to dropbox
							try {
								dbFile.saveToDropboxAppFolder();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								_errorMessage = e.getMessage();
								break;
							}
						}
						else
						{
							// (3) if so, determine if the file is empty
							// if not, ask the user whether to download the files currently 
							// in dropbox.  (in ui thread)
							dbFilesExist = true;
							break;
						}
					} catch (DropboxException e) {
						// TODO Auto-generated catch block
						_errorMessage = e.getMessage();
						logOut();
					}
				}
				
			}
	    	return dbFilesExist;	

		}
		@Override
		protected void onPostExecute(Boolean result)
		{
        	//     (3.a) If the user chooses to download, do normal read; 
        	//         (3.a.1) how do we update lists already in memory?
        	//     (3.b) If the user does NOT choose to download, 
        	//          (3.b.1) write files to dropbox folder and continue
			onFinishedFindingDbFiles(result, _errorMessage);
		}
    }
	/*@Override
	public void onConfirmed(boolean answer) {
	     if(answer)
         {
         	// start an async task to copy the files from dropbox to our local folder
         	copyFilesFromDropbox();
         	
         }   
		
	}
*/
}

