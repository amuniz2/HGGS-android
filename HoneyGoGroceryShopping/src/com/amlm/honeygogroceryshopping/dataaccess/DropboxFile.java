package com.amlm.honeygogroceryshopping.dataaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;

import com.amlm.honeygogroceryshopping.activities.GroceryFileConsumer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

public class DropboxFile {
	
	private IGroceryFile _gfile;
	private Entry _metadata;
	public DropboxFile(IGroceryFile gfile)
	{
		_gfile = gfile;
	}
	public String getRevision()
	{
		String ret = new String("");
		if (_metadata != null)
		{
			ret = _metadata.rev;
		}
		return ret;
	}
    public boolean fileExists() throws DropboxException
    {
    	DropboxAPI<AndroidAuthSession> api = GroceryFileConsumer.getDBApi();
    	boolean ret = false;
    	if (api != null)
    	{
    		if (api.getSession().isLinked())
    		{
    				String path = _gfile.getDbPath();
    				
					_metadata = api.metadata(path, 1, null, false, null);
					ret = ((_metadata != null) && (_metadata.rev != null));
    		}
    	}
    	return ret;
    }
  /*  public void getLatestRevision() throws DropboxException, IOException, JSONException
    {
    	getLatestRevision(_gfile.getOutputStream());
    }
    */
    private FileOutputStream getLocalFileOutputStream() throws FileNotFoundException, IOException, JSONException

    {
    	return new FileOutputStream(_gfile.getFile());
    }
    
    public void getLatestRevision() throws DropboxException, IOException, JSONException
    {
    	getLatestRevision(getLocalFileOutputStream());
    }
    public void getLatestRevision(OutputStream os) throws DropboxException, IOException
    {
    	try
    	{
    		DropboxAPI<AndroidAuthSession> api = GroceryFileConsumer.getDBApi();   	
    	
    		if (api != null)
    		{	
    			String rev = getRevision();
    			api.getFile(_gfile.getDbPath(), rev, os, null);
    			//updateDbRev(context, metadata.rev);
    			_gfile.setRevision(rev);
    		}
    	}
		finally
		{
			if (os != null)
			{
				os.flush();
				os.close();
			}
		}

    }
    public boolean isRevisionCurrent()
    {
    	boolean ret;
    	String dbRev = getRevision();
		String currentFileRev = _gfile.getRevision();
    	if (!dbRev.isEmpty())
    	{
    		ret = dbRev.equals(currentFileRev);
    	}
    	else
    	{
    		ret = ((currentFileRev == null) || (currentFileRev.isEmpty()));
    	}
    	return ret;
    }

    public boolean saveToDropboxAppFolder() throws FileNotFoundException, DropboxException
    {
    	boolean saveCompleted = false;
    	DropboxAPI<AndroidAuthSession> api = GroceryFileConsumer.getDBApi(); //DropboxConsumer.getDBApi();
    	if (api != null)
    	{
    		if (api.getSession().isLinked())
    		{   
    			File file = GroceryFile.getFile(_gfile);
    			InputStream is;
				
					is = new FileInputStream(file);
					
					_metadata = api.putFileOverwrite(_gfile.getDbPath(),  is, file.length(), null);
					
					_gfile.setRevision(_metadata.rev);
					// dbtodo: update rev
					saveCompleted = true;
				 
    		}
    		else
    		{
    			// todo: message to connect to dropbox
    			
    		}
    		
    	}
    	return saveCompleted;
    }
	
}
