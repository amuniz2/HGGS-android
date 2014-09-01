package com.amlm.honeygogroceryshopping.model.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amlm.honeygogroceryshopping.interfaces.IGroceryItemSerializer;
import com.amlm.honeygogroceryshopping.interfaces.IItemListSerializer;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
import com.google.inject.Inject;

public class JsonItemList implements IItemListSerializer {
    @Inject IGroceryItemSerializer _itemSerializer;

	
	
	public  ArrayList<GroceryItem> parseList(String text) throws JSONException 
	{
		ArrayList<GroceryItem> items = new ArrayList<GroceryItem>();
		if (text != null)
		{
			JSONArray jsonItems = new JSONArray(text);
	   	 	int len = jsonItems.length();
	   	 	GroceryItem currentItem = null;
	   	 	JSONObject jsonItem = null;
	   	 	for (int i = 0; i < len ; i++)
	   	 	{
	   	 		jsonItem= jsonItems.getJSONObject(i);
	   	 		currentItem =JSONGroceryItem.parseFromJson(jsonItem);
	   	        items.add(currentItem);
	   	 	}
	   	 	Collections.sort(items);
		}
		return items;

	}
	
	public  String writeList(Collection<GroceryItem> list) throws JSONException, IOException
	{
		JSONObject jsonItemAttributes =null;
		JSONArray jsonItemList = new JSONArray();
        
        for (GroceryItem item: list)
        {	
        	jsonItemAttributes =  JSONGroceryItem.writeToJson(item);      	
        	jsonItemList.put(jsonItemAttributes);
        }
        return jsonItemList.toString();
        
        
	}
	
}
