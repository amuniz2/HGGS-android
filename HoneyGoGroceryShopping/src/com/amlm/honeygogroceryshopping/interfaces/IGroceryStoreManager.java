package com.amlm.honeygogroceryshopping.interfaces;

import java.util.ArrayList;

import android.content.Context;

public interface IGroceryStoreManager   {
	
	
	//public  String getCurrent() ;
	//public void setCurrent(String value);
	//public ArrayList<IGroceryStore> getGroceryStores(Context context);
	public boolean areGroceryFilesShared(Context context, String storeName);
	public ArrayList<IGroceryStore>  getGroceryStores();
	public void setGroceryStoreSharing(Context context,boolean sharing);	
	
}
