package com.amlm.honeygogroceryshopping.model;

import java.util.ArrayList;
import java.util.UUID;

import com.amlm.honeygogroceryshopping.dataaccess.DataCache;

public class Category extends PersistentItem implements Comparable<Category> //, Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _name;
	private Integer _order = UNKNOWN_AISLE;
	private Integer _index = 0;
	private UUID _id;
	//private String _aisle;
	
	//public static Integer  MAX_NUMBER_OF_CATEGORIES=100;
	public static Integer UNKNOWN_AISLE = 0; //-1;
	
	public Category(UUID id) 
	{
		super();
		initialize(new String(""), id, UNKNOWN_AISLE);
		
	}
	
	public UUID getId() {
	 
		return _id;
	}
	
	public void setId(UUID id) {
		_id = id;
	}
	
	public Category()
	{
		initialize(new String(""), /*GroceryItem.DEFAULT_CATEGORY_ID*/ null, UNKNOWN_AISLE);
				
	}
	public Category(String name, UUID id, Integer order)
	{
		// must be a new category if we know the name and and order, but not the id
		initialize(name, id, order);
					
	}
	public Category(String name, Integer order)
	{
		// must be a new category if we know the name and and order, but not the id
		initialize(name, /*UUID.randomUUID()*/ null, order);
					
	}
	
	private void initialize(String name,  UUID id, Integer order)
	{
		
		setOrder((order == null) ? Category.UNKNOWN_AISLE : order);
		setName(name);
		//setId(id);

		
	}
	public static String IdToName(UUID catId)
	{
    	ArrayList<Category> cats = DataCache.getCategories();

    	Category catToSearchFor = new Category();
    	catToSearchFor.setId(catId);
    	int index =cats.indexOf(catToSearchFor);
    	if (index >= 0)
    		return cats.get(index).getName();
    		
    	return null;			
    	
	}
	public static Category getCategory(UUID catId)
	{
    	ArrayList<Category> cats = DataCache.getCategories();
    	Category ret = null;
    		
    	Category catToSearchFor = new Category();
    	catToSearchFor.setId(catId);
    	int index =cats.indexOf(catToSearchFor);
    	if (index >= 0)
    	{	
    		ret = cats.get(index);
    	}
    		
    	return ret;			
	}
	public static Category getCategory(String categoryName)
	{
    	ArrayList<Category> cats = DataCache.getCategories();
    		
    	Category catToSearchFor = new Category();
    	catToSearchFor.setName(categoryName);
    	int index =cats.indexOf(catToSearchFor);
    	if (index >= 0)
    		return cats.get(index);
    	
    	return GroceryItem.getDefaultCategory();
	}	
	public String getName() {
		return _name;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public Integer getOrder() {
		return _order;
	}
	public void setOrder(Integer _order) {
		this._order = _order;
	}
	public Integer getIndex() { return _index;}
	public void setIndex(Integer value) { _index = value;}
	
	   @Override
       public boolean equals(Object obj)
       {
		   if (this == obj) 
       		     return true;
       		    
       	   if (!(obj instanceof Category))
       		   return false;
       	   
       	   Category cat = (Category) obj; 
       	   
       	   if ((getId() != null) && (cat.getId() != null))
       		   return this.getId().equals (cat.getId());
       	   
       	   return (this.getName().equals(cat.getName()));       	   
       }
		
	@Override
	public int compareTo(Category another) {
		Integer thisAisleNumber = getOrder();
		int ret = thisAisleNumber.compareTo(another.getOrder());

		if (ret != 0)
			return ret;
		
		if ( (thisAisleNumber == 0 ) && 
			(this.getName().equals(GroceryItem.DEFAULT_CATEGORY_NAME)))					
				return  -1;
			
		return getName().compareTo(another.getName());
		
	}
    public void copyToCategory(Category to)
    {
    	if (to != null)
    	{
    		to.setName(this.getName());
    		to.setOrder(this.getOrder());
    	}
    }
    @Override
    public String toString()
    {            
        return String.format("%s: Aisle %s",getName(), getOrder().toString());
    }

	@Override
	public <T extends PersistentItem> void copyTo(T dest) {
		copyToCategory((Category)dest);
	}
	
	    
}
