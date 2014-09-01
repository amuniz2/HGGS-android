package com.amlm.honeygogroceryshopping.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import com.amlm.honeygogroceryshopping.model.Category;

public interface ICategoryListSerializer  {
    
	
	public  ArrayList<Category> parseList(String text) throws JSONException ;
	public  String writeList(Collection<Category> list) throws JSONException, IOException;
		
}
