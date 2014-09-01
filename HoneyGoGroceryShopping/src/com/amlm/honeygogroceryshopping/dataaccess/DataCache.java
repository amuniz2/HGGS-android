package com.amlm.honeygogroceryshopping.dataaccess;

import java.util.ArrayList;
import java.util.Collections;

import com.amlm.honeygogroceryshopping.model.Category;

public class DataCache {
	  private static  ArrayList<Category> _categories;
	    public static ArrayList<Category> getCategories() {return _categories;}
	    public static void setCategories(ArrayList<Category> cats) { 
	    	if (cats != null)
	    		{
	    		Collections.sort(cats);
	    		}
	    	_categories = cats;}

}
