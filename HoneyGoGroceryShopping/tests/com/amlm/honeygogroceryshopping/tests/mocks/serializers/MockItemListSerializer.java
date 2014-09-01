package com.amlm.honeygogroceryshopping.tests.mocks.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amlm.honeygogroceryshopping.interfaces.IItemListSerializer;
import com.amlm.honeygogroceryshopping.model.GroceryItem;
//import org.json.JSONException;
//import net.sf.json.JSONArray;
//import net.sf.json.*;
//import net.sf.json.JSONObject;

public class MockItemListSerializer implements IItemListSerializer  {
	public MockItemListSerializer() {}
	
	public  ArrayList<GroceryItem> parseList(String text) throws org.json.JSONException 
	{
	   	ArrayList<GroceryItem> items = new ArrayList<GroceryItem>();
		if (text != null)
		{
			JSONParser parser = new JSONParser();
				
			JSONArray jsonItems;
			try 
			{
				jsonItems = (JSONArray) parser.parse(text);
				int len = jsonItems.size();
				GroceryItem currentItem = null;
				JSONObject jsonItem = null;
				for (int i = 0; i < len ; i++)
				{
		   		
		   	 		jsonItem= (JSONObject)jsonItems.get(i); //getJSONObject(i);
		   	 		currentItem =MockJsonGroceryItem.parseFromJson(jsonItem);
		   	        items.add(currentItem);
				}
				Collections.sort(items);
			}
			catch (Exception ex)
			{
				throw new org.json.JSONException(ex.getMessage());
			}
		}
	   	return items;
	}
	public  String writeList(Collection<GroceryItem> list) throws org.json.JSONException, IOException
	{
		JSONArray jsonItemList = new JSONArray();
		try
		{
			JSONObject jsonItemAttributes =null;
	        
	        for (GroceryItem item: list)
	        {	
	        	jsonItemAttributes =  MockJsonGroceryItem.writeToJson(item);      	
	        	addJsonObjectToJsonArray(jsonItemList, jsonItemAttributes);
	        	//jsonItemList.add(jsonItemAttributes);
	        }
		}           
		catch (Exception ex)
		{
			throw new org.json.JSONException(ex.getMessage());
		}
        return jsonItemList.toString();

	}
	@SuppressWarnings("unchecked")
	private boolean addJsonObjectToJsonArray(JSONArray list, JSONObject object)
	{
		return list.add(object);
	}

		
}
