package com.amlm.honeygogroceryshopping.dataaccess;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.amlm.honeygogroceryshopping.activities.GroceryFileConsumer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.google.inject.Inject;

public class GroceryStore  implements IGroceryStore {
	public @Inject Application _app;
	
	public GroceryStore() 
	{
		this("");
	}
	GroceryStore(String name)
	{
		setOriginalName ( name);
		setName ( name);
	}
	public GroceryStore(String original, String newName)
	{
		setOriginalName ( original);
		setName ( newName);
		
	}
	
	public static String xCurrent ;
	
		public void setEnableSharing(boolean share)
	{
		SharedPreferences prefs = _app.getSharedPreferences(GroceryFileConsumer.SHARING_PREFS_NAME, 0);
		
		Editor edit = prefs.edit();
        edit.putBoolean(getOriginalName(), share);	
        edit.commit();
		
	}
	public boolean getEnableSharing()
	{
		return areGroceryFilesShared(_app);
		
	}
	public boolean areGroceryFilesShared(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(GroceryFileConsumer.SHARING_PREFS_NAME, 0);
		return prefs.getBoolean(getName(), false);

	}
	public static boolean xareGroceryFilesShared(Context context, String storeName)
	{
		SharedPreferences prefs = context.getSharedPreferences(GroceryFileConsumer.SHARING_PREFS_NAME, 0);
		return prefs.getBoolean(storeName, false);
		
	}
	private String _originalName;
	private String _name ;
	
	public String getOriginalName() { return _originalName;}
	public void setOriginalName(String value) { _originalName = value;
	_name = value;}
	
	public String getName(){ return _name;}
	public void setName(String value){ _name= value;}
	
	
	
}
