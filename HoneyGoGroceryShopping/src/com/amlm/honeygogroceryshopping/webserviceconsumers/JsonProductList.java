package com.amlm.honeygogroceryshopping.webserviceconsumers;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonProductList {
	
	    private static final String  TAG_NAME= "productname";
	  


		
		
		public static ArrayList<Product> getProducts(String text) throws JSONException 
		{
			// this top level json object has properties of names:
			// "0", "1", etc
			ArrayList<Product> items = new ArrayList<Product>();
			JSONObject jsonObject = new JSONObject(text);
	   	 	Product currentItem = null;
	   	 	JSONObject jsonItem = null;
			// for each property, we need to get the information of the json 
			// object within
			Iterator<String> iter = keysIterator(jsonObject);
		    while(iter.hasNext())
		    {
		    	jsonItem = jsonObject.getJSONObject(iter.next());
		    	
	   	 		currentItem = new Product();
	   	 		currentItem.setName(jsonItem.getString(TAG_NAME));
	   	        items.add(currentItem);
	   	 	}
	   	 	return items;

		}





		@SuppressWarnings("unchecked")
		private static Iterator<String> keysIterator(JSONObject jsonObject) {
			return jsonObject.keys();
		}
		
	
	
}
