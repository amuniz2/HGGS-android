package com.amlm.honeygogroceryshopping.dataaccess;

import java.io.File;
import java.util.ArrayList;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.amlm.honeygogroceryshopping.activities.GroceryFileConsumer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class GroceryStoreManager  implements IGroceryStoreManager {
	public @Inject Application _app;
	
	public GroceryStoreManager() 
	{
		
	}
	IGroceryStore createGroceryStore(String name)
	{
		Injector injector = RoboGuice.getInjector(_app);
		
		IGroceryStore newStore = injector.getInstance(IGroceryStore.class);
		newStore.setOriginalName (name);
		return newStore;
	}
	public IGroceryStore CreateGroceryStore(String original, String newName)
	{
		Injector injector = RoboGuice.getInjector(_app);
		
		IGroceryStore newStore = injector.getInstance(IGroceryStore.class);
		newStore.setOriginalName (original);
		newStore.setName (newName);
		return newStore;
	}
	private static String _current;
	public  static String getCurrent(){ return _current;}
	public static void setCurrent(String currentStore){_current = currentStore;}
	
	public ArrayList<IGroceryStore> getGroceryStores()
	{
    	Injector injector = RoboGuice.getInjector(_app);
		ArrayList<IGroceryStore> ret = new ArrayList<IGroceryStore>();
		File groceryFilesFolder = GroceryFile.getGroceryFilesDir();
		String [] groceryStoreNames =  groceryFilesFolder.list();
    	IGroceryStore gStore;
		if (groceryStoreNames.length == 0)
		{
			gStore=injector.getInstance(IGroceryStore.class);
            gStore.setOriginalName(GroceryFile.defaultStoreName);
			ret.add(gStore);
		}
		else
		{
			for (String storeName : groceryStoreNames)
			{
				gStore=injector.getInstance(IGroceryStore.class);
				gStore.setOriginalName(storeName);
				ret.add(gStore);
			}
		}
		return ret;		
	}
	
	public boolean areGroceryFilesShared(Context context, String storeName)
	{
		SharedPreferences prefs = context.getSharedPreferences(GroceryFileConsumer.SHARING_PREFS_NAME, 0);
		return prefs.getBoolean(storeName, false);
		
	}
	public void setGroceryStoreSharing(Context context,boolean sharing) {
		// TODO Auto-generated method stub
		SharedPreferences prefs = context.getSharedPreferences(GroceryFileConsumer.SHARING_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putBoolean(getCurrent(), sharing);		
       
        edit.commit();

		
	}
	
	
}
