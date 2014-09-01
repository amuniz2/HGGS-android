package com.amlm.honeygogroceryshopping.model.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amlm.honeygogroceryshopping.interfaces.ICategoryListSerializer;
import com.amlm.honeygogroceryshopping.model.Category;

public class JsonCategoryList implements ICategoryListSerializer  {
    
	public JsonCategoryList() {}
	
	public  ArrayList<Category> parseList(String text) throws JSONException 
	{
		String catIdAsString;
		UUID catId;
		JSONArray jsonCats = new JSONArray(text);
   	 	ArrayList<Category> items = new ArrayList<Category>();
   	 	int len = jsonCats.length();
   	 	Category currentCategory = null;
   	 	JSONObject jsonCat = null;
   	 	for (int i = 0; i < len ; i++)
   	 	{
   	 		jsonCat= jsonCats.getJSONObject(i);
   	 		try
   	 		{	
   	 			catIdAsString = jsonCat.getString(JsonTags.TAG_ID);
   	 		}
   	 		catch(JSONException jsonex)
   	 		{
   	 			catIdAsString = null; 
   	 		}
   	 		catId = catIdAsString == null ? null : UUID.fromString(catIdAsString);
   	 		currentCategory = new Category();
   	 		currentCategory.setName(jsonCat.getString(JsonTags.TAG_NAME));
   	 		currentCategory.setOrder(jsonCat.getInt(JsonTags.TAG_ORDER));
   	 		
   	 		try 
   	 		{
   	 			currentCategory.setIndex(jsonCat.getInt(JsonTags.TAG_INDEX));
   	 		}
   	 		catch(JSONException jsonex){}
   	 	
   	 		currentCategory.setId(catId);
   	        items.add(currentCategory);
   	 	}
   	 	Collections.sort(items);
   	 	return items;

	}
	public  String writeList(Collection<Category> list) throws JSONException, IOException
	{
		JSONObject jsonCatAttributes =null;
		JSONArray jsonCatList = new JSONArray();
        String name;
        for (Category item: list){
        	name = item.getName();
        	if ((name != null) && !name.isEmpty())
        	{
        		jsonCatAttributes = new JSONObject();
        		
        		jsonCatAttributes.put(JsonTags.TAG_NAME, name);
        		jsonCatAttributes.put(JsonTags.TAG_ORDER, String.valueOf(item.getOrder()));
        		jsonCatAttributes.put(JsonTags.TAG_INDEX, String.valueOf(item.getIndex()));
        		//jsonCatAttributes.put(JsonTags.TAG_ID, item.getId().toString());	            	
        		jsonCatList.put(jsonCatAttributes);
        	}
        }
        return jsonCatList.toString();
        
        
	}
		
}
