package com.amlm.honeygogroceryshopping.interfaces;

import java.io.IOException;

import org.json.JSONException;

import com.amlm.honeygogroceryshopping.model.GroceryItem;

public interface IGroceryItemSerializer {
    
	public  GroceryItem parseFromJsonString(String text) throws JSONException ;
	//public  GroceryItem parseFromJson(JSONObject jsonItem) throws JSONException ;
	public  String writeToJsonString(GroceryItem item) throws JSONException, IOException;
	//public  JSONObject writeToJson(GroceryItem item) throws JSONException, IOException;
}
