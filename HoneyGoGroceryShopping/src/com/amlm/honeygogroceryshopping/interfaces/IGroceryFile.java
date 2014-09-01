package com.amlm.honeygogroceryshopping.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
//import android.os.DropBoxManager.Entry;

public interface IGroceryFile {

	 
	
	//private String _dbRev;
	public String getFileName();
	//public static void setExternalFilesDir(File dir);
	public String getRevision() ;
	public void setRevision(String rev);
	public Boolean getDBRevNeedsUpdating() ;
	public void setDBRevNeedsUpdating(Boolean value);

	public Boolean isShared();
	//public void init(DropboxAPI<AndroidAuthSession> api, String sn, String filename, Boolean shared);
	public void init(String sn, String filename, Boolean shared);
	public String getStoreName() ;
	 

	//public static EnumMap<FileNameConstants, IGroceryFile> JsonFiles;
	public File getFile(/*Context context*/);
	    /* Checks if external storage is available for read and write */
	 //   public static boolean isExternalStorageWritable();

	    /* Checks if external storage is available to at least read */
	  //  public static boolean isExternalStorageReadable();
	    public boolean exists(/*Context context*/);
	    public boolean revisionsMatch();	    // if the revision of the dropbox file is different than what we have, 
	    // download the contents of the dropbox file and save it locally
	    public void syncWithDropbox(/*Context context*/) throws FileNotFoundException, IOException, JSONException;	
	    
	    public String getDbPath();
	    public String read(/*Context context*/) throws IOException, Exception ;		 
	  
	    
	    public boolean getSaveToDbPending();
	    public String getRevisionKey();
	    public String getDbPendingKey( );

	    // public boolean saveToDropboxAppFolder();
	    
	    
	    public void save( String contents) throws FileNotFoundException, IOException, JSONException ;
		public void delete(/*Context context*/);
		
		public String createFromAssets() throws IOException, JSONException;
	/*	  public OutputStream getOutputStream()  throws FileNotFoundException ;
		  public InputStream getInputStream()  throws FileNotFoundException;
	*/
}
