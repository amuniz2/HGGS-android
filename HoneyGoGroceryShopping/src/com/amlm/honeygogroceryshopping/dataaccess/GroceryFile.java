package com.amlm.honeygogroceryshopping.dataaccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.EnumMap;

import org.json.JSONException;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.amlm.honeygogroceryshopping.FileNameConstants;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.dropbox.client2.exception.DropboxException;
import com.google.inject.Inject;
//import android.os.DropBoxManager.Entry;

public class GroceryFile extends BaseGroceryFile {
	@Inject Application _app;	
//	private String _errorMessage;
	
	 private static File _filesDir; /// root directory where files  are saved
		
		//private String _dbRev;

		public static void setGroceryFilesDir(Context context)
		{
			// replace this code by determining
			// which is the directory, based on internal / external
			// todo: http://developer.android.com/reference/android/os/Environment.html#getExternalStoragePublicDirectory(java.lang.String)
			if (_filesDir == null)
			{
					_filesDir = (usingExternalStorage()) ? context.getExternalFilesDir(null) : 
					context.getDir(null, 0);
			}	

		}
		public static File getGroceryFilesDir()
		{
			return _filesDir;
		}
				 

	
	public final static String defaultStoreName = "My Grocery Store";
	
	public GroceryFile() {};

	public static void CreateGroceryStoreFolder(String storeName)
	{
		File parentFolder = GroceryFile.getGroceryFilesDir();
		File storeFolder = new File(parentFolder, storeName);
		if (!storeFolder.exists())
		{
			storeFolder.mkdir();
		}
	}
	public static boolean RenameGroceryStore(String from, String to)
	{
		boolean ret;
		File parentFolder = GroceryFile.getGroceryFilesDir();
		File fromParentFolder = new File(parentFolder, from);
		File toParentFolder = new File (parentFolder, to );
		
		if (!toParentFolder.exists())
		{
			toParentFolder.mkdir();
		}
		ret=renameGroceryFile(fromParentFolder, toParentFolder, "category.json") ;
		if (ret){	
			ret = renameGroceryFile(fromParentFolder, toParentFolder, "master_list.json") ;
		}
		if (ret){	
			ret = renameGroceryFile(fromParentFolder, toParentFolder, "current_list.json") ;
		}
		if (ret){	
			ret = renameGroceryFile(fromParentFolder, toParentFolder, "shopping_list.json") ;
		}
		if (ret)
		{
			ret = deleteGroceryStore(from);
		}
		return ret;
	}
	private static boolean renameGroceryFile(File fromStoreFolder, File toStoreFolder, String fileName)
	{
		boolean ret = true;
		File originalFile = new File(fromStoreFolder, fileName);
		if (originalFile.exists())
		{
			File newFile  = new File( toStoreFolder, fileName);
			ret = originalFile.renameTo(newFile);
		}
		return ret;
		
	}
	public static boolean deleteGroceryStore(String storeName)
	{
		File parentFolder = GroceryFile.getGroceryFilesDir();
		File folderToDelete= new File( parentFolder, storeName);
		boolean ret = true;
		// folder must be empty prior to deleting
		File[] storeGroceryFiles = folderToDelete.listFiles();
		for (File groceryFile : storeGroceryFiles)
		{
			
				ret = groceryFile.delete();
		}
		
		if  (ret) 
			{
			return folderToDelete.delete();
			}
		else
			return ret;
	}
	public static EnumMap<FileNameConstants, IGroceryFile> JsonFiles;
	    
	    public static File getFile(IGroceryFile gfile) {
	        // Get the directory for the app's private pictures directory. 
	        String sn = gfile.getStoreName();
	        File parentFolder = GroceryFile.getGroceryFilesDir();
	        //context.getExternalFilesDir(null);
	        if (sn.isEmpty())
	        {
	        	return getFile(parentFolder, gfile.getFileName());
	        }
	        else
	        {
	        	return getFile(parentFolder, sn, gfile.getFileName());
	        }
	        
	    }   
	    private static File getFile(File dir, String fileName)
	    {
	        File retFile = null;
	        	if (!dir.exists())
	        	{
	        		dir.mkdirs();
	        	}
	        	retFile = new File(dir,fileName /*this.getGroceryListFile()*/ );
	        
	        return retFile;	    	
	    }
	    private static File getFile(File parent, String subFolder, String fileName) 
	    {
	        // Get the directory for the app's private pictures directory. 
	  
	    	return getFile(new File(parent, subFolder), fileName);
	        
	    }   
	    private static boolean usingExternalStorage()
	    {
	    	return isExternalStorageWritable();
	    }
	    /* Checks if external storage is available for read and write */
	    private static boolean isExternalStorageWritable() {
	        String state = Environment.getExternalStorageState();
	        if (Environment.MEDIA_MOUNTED.equals(state)) {
	            return true;
	        }
	        return false;
	    }

	    /* Checks if external storage is available to at least read */
	    /*public static boolean isExternalStorageReadable() {
	        String state = Environment.getExternalStorageState();
	        if (Environment.MEDIA_MOUNTED.equals(state) ||
	            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	            return true;
	        }
	        return false;
	    } 
	    */   
	    public boolean exists(/*Context context*/)
	    {
	    	return getFile().exists();
	    	/*
	    	 * if (GroceryFile.usingExternalStorage())
	    	 
	    	{
	    		ret = getFile().exists();
	    	}
	    	else
	    	{
	    		String[] existingInternalFiles = this._app.getApplicationContext().fileList();
	    		if (existingInternalFiles != null)
	    		{
	    			for (int i = 0; i < existingInternalFiles.length; i++)
	    			{
	    				if (existingInternalFiles[i].equalsIgnoreCase(this.getFileName()))
	    				{
	    					ret = true;
	    					break;
	    				}
	    			}
	    		}
	    		
	    	}
	    	return ret;
	    	*/
	    }
	      
		// if the revision of the dropbox file is different than what we have, 
	    // download the contents of the dropbox file and save it locally
	    public void syncWithDropbox(/*Context context*/) throws FileNotFoundException, IOException, JSONException
	    {
	    	try 
	    	{
	    		DropboxFile dbFile = new DropboxFile(this);
	    		if (dbFile.fileExists())
	    		{
	   				if (!dbFile.isRevisionCurrent())
					{
	   					dbFile.getLatestRevision();
						setDBRevNeedsUpdating(true);
					}
	    		}	
	    	}
	    	catch (Exception e) 
	    	{
				// may just be that file does not yet exist in dropbox
				setErrorMessage( e.toString());
	    	}
	    }
	  
	    public String read(/*Context context*/) throws IOException, Exception 
	    {
	    	
	       
	        InputStreamReader reader = null;
       	 	String fileContents = null;
       	 	InputStream file = null;
       	 
       	 	// prior to reading, check if dropbox has a more recent file
       	 	if (isShared())
       	 		{
       	 		syncWithDropbox();
       	 		}
       	 		
       	 	try
       	 	{
       	 	 

        	   if (this.exists())
	           {
        		   file = new FileInputStream(getFile());
	    	   	   	fileContents = inputStreamToContents(file);	
	           }
	    	   else
	    	   {
	    		   fileContents = createFromAssets();
	    	   }
	    	  
	    	   
       	 	}
       	 	finally
	        {
	            if (reader != null)
	            {
	                reader.close();
	            }
	            if (file != null)
	            {
	            	file.close();
	            	
	            }
	        }    	

	        return fileContents;

	     
	    }		 
	  
	    
	  private String inputStreamToContents(InputStream stream) throws IOException
	  {
		  String contents = null;
   	   	InputStreamReader reader;
   	   	if (stream != null) 
   	   {
   		   reader = new InputStreamReader(stream);
   		   contents = this.readContents(reader);
   		
   	   }
   	   	return contents;
	  
	  }

	    
	   /* public FileOutputStream getOutputStream()  throws FileNotFoundException//, IOException, JSONException

	    {
	    	FileOutputStream ret = null;
	    	if (GroceryFile.usingExternalStorage())
	    	{	           
	        	   ret = new FileOutputStream(this.getExternalFile());
	         }
	    	else
	    	{
	    		ret = _app.getApplicationContext().openFileOutput(getStoreName() + "_" + getFileName(),0 );
	    	}
	    	return ret;
	    }	
	    */
	    public void save( String contents) throws FileNotFoundException, IOException, JSONException
		    {
		        OutputStream file = null;
		        OutputStreamWriter writer = null;
		        try
		        {
		        	 file = new FileOutputStream(this.getFile());
		        	 if (file != null)
		        	 {
		        		 writer = new OutputStreamWriter(file);	
		        		 
		        		 writer.write(contents);
		        		 file.flush();
		        	 }	           
		        }
		        finally
		        {
		            if (writer != null)
		            {
		                writer.close();
		            }
		            if (file != null)
		            {
		            	file.close();
		            	
		            }
		        }
		        if (isShared())
	        	   {
		        	DropboxFile dbFile = new DropboxFile(this);
		        	 try {
						this.setSaveToDbPending(!dbFile.saveToDropboxAppFolder());
					} catch (DropboxException e) {
						// TODO Auto-generated catch block
						throw new IOException(e.getMessage());
					}
	        	   }
		    }
		 public void delete(/*Context context*/)
		 {
	    	if (GroceryFile.isExternalStorageWritable())
	        {
	    		if (exists())
	    		{
	    			File fileToDelete= this.getFile(/*context*/);
	    			if (fileToDelete.exists())
	    			{
	    				fileToDelete.delete();
	    			}
	    		}	
	    	}    	
		 }
		 
		 private String readContents(InputStreamReader reader) throws IOException
		    {
				String str = new String("");
				String line;
		        BufferedReader bReader;
		        bReader = new BufferedReader(reader);
		        while ((line = bReader.readLine()) != null) {   
		            str += line;
		        }
		        return str;
			
		    }
		public String createFromAssets() throws IOException, JSONException
		{
			String contents = new String("");
			InputStream stream = null;
			AssetManager manager = _app.getAssets();
			try {
				
				stream= manager.open(this.getFileName());
				contents = this.inputStreamToContents(stream);
					
					this.save(contents);
			} 
			catch(Exception e)
			{ // ignore, as the asset may not exist
			}
			
			finally
			{
				if (stream != null)
				{
					stream.close();
				}
			}
			return contents;
		}
		private void setErrorMessage(String rrorMessage) {
		//	this._errorMessage = errorMessage;
		}
		
}
