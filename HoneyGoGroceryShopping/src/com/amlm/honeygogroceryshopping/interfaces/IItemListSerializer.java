package com.amlm.honeygogroceryshopping.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import com.amlm.honeygogroceryshopping.model.GroceryItem;

public interface IItemListSerializer  {
    

	
	
	public  ArrayList<GroceryItem> parseList(String text) throws JSONException ;
	
	public  String writeList(Collection<GroceryItem> list) throws JSONException, IOException ;
	
}
