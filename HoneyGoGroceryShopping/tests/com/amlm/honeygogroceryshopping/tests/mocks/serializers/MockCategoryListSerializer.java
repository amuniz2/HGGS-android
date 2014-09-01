package com.amlm.honeygogroceryshopping.tests.mocks.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
//import org.json.JSONException;
//import org.json.JSONException;
//import net.sf.json.JSONArray;
//import net.sf.json.*;
//import net.sf.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import com.amlm.honeygogroceryshopping.interfaces.ICategoryListSerializer;
import com.amlm.honeygogroceryshopping.model.Category;
import com.amlm.honeygogroceryshopping.model.serialization.JsonTags;

public class MockCategoryListSerializer implements ICategoryListSerializer  {
    
	public MockCategoryListSerializer() {}
	
	public  ArrayList<Category> parseList(String text) throws org.json.JSONException 
	{
		JSONParser parser = new JSONParser();
		ArrayList<Category> items = new ArrayList<Category>();
		try
		{	
			JSONArray jsonCats =  (JSONArray) parser.parse(text);
   	 		Category currentCategory = null;
   	 		JSONObject jsonCat = null;
   	 		String catIdAsString;
   	 		String indexAsString;
   	 		for (Object obj : jsonCats)
   	 		{
	   	 		jsonCat = (JSONObject) obj;
	   	 		currentCategory = new Category();
	   	 		
	   	 		currentCategory.setName((String)jsonCat.get(JsonTags.TAG_NAME));	   	 		
	   	 		currentCategory.setOrder(Integer.valueOf( (String)jsonCat.get(JsonTags.TAG_ORDER) ));
	   	 		
	   	 		
	   	 		indexAsString = (String)jsonCat.get(JsonTags.TAG_INDEX);
	   	 		if ((indexAsString != null) && (!indexAsString.isEmpty()))
	   	 			currentCategory.setIndex(Integer.valueOf( indexAsString ));
	   	 		
	   	 		catIdAsString = (String)jsonCat.get(JsonTags.TAG_ID); 
	   	 		if ((catIdAsString != null) && (!catIdAsString.isEmpty())) 
	   	 			currentCategory.setId(UUID.fromString(catIdAsString));
	   	        
	   	 		items.add(currentCategory);
	   	 	}
		
			}
		catch (Exception ex)
		{
			throw new org.json.JSONException(ex.getMessage());
		}

			return items;

	}
	public  String writeList(Collection<Category> list) throws org.json.JSONException, IOException
	{
		JSONArray jsonCatList = new JSONArray();
		try
		{
			JSONObject jsonCatAttributes =null;
		
	        String name;
	        for (Category item: list){
	        	name = item.getName();
	        	if ((name != null) && !name.isEmpty())
	        	{
	        		jsonCatAttributes = new  JSONObject();
	        		
	        		putJsonAttribute(jsonCatAttributes, JsonTags.TAG_NAME, name);
	        		putJsonAttribute(jsonCatAttributes, JsonTags.TAG_ORDER, String.valueOf(item.getOrder()));
	        		putJsonAttribute(jsonCatAttributes, JsonTags.TAG_INDEX, String.valueOf(item.getIndex()));
	        		//putJsonAttribute(jsonCatAttributes, JsonTags.TAG_ID, item.getId().toString());	            	
	        		addJsonObjectToJsonArray(jsonCatList, jsonCatAttributes);
	        	}
	        }
		}
		catch (Exception ex)
		{
			throw new org.json.JSONException(ex.getMessage());
		}
		return jsonCatList.toString();
        
        
	}

	@SuppressWarnings("unchecked")
	private void putJsonAttribute(JSONObject obj, String tag, String value)
	{
		obj.put(tag, value);
	}
	
	@SuppressWarnings("unchecked")
	private boolean addJsonObjectToJsonArray(JSONArray list, JSONObject object)
	{
		return list.add(object);
	}
}
