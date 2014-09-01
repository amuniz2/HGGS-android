package com.amlm.honeygogroceryshopping.model;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;
import android.app.Application;
import android.content.Context;

import com.amlm.honeygogroceryshopping.interfaces.IGroceryItemSerializer;
import com.google.inject.Inject;

        /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Owner
 */
public class GroceryItem extends PersistentItem  implements Comparable<GroceryItem> {
	@Inject Application application;
       /**
	 * 
	 */
	//private final  RoboActivity _context = new RoboActivity();
	
	private static final long serialVersionUID = -6612843017004086073L;
	public static final UUID DEFAULT_CATEGORY_ID = UUID.fromString("a6eeabae-491d-4680-ab50-696f7e30769c");
	public static final String DEFAULT_CATEGORY_NAME = "unknown";
	@Inject IGroceryItemSerializer _itemSerializer;
	
	private String _name;
        private Date _lastPurchasedDate;
        private boolean _selected;
        private String _notes;
        private String _notesSeparator;
        private double _quantity;
        private String _unitsOrSize;
        //private UUID _categoryId;
        private String _categoryName;
    //    private UUID _id;
        
     public static Category getDefaultCategory()
     {
    	 
     	return Category.getCategory(GroceryItem.DEFAULT_CATEGORY_ID);

     }
     public GroceryItem()
        {
    	 
       	 initialize(new String(""),  1,new String(""), GroceryItem.DEFAULT_CATEGORY_NAME, true, new String(""));
      }
     
     public GroceryItem(String name, 
    		  double quantity, Boolean selected,
    		 String unitsOrSize,  String notes )
     {
    	 initialize(name, quantity,unitsOrSize, GroceryItem.DEFAULT_CATEGORY_NAME, selected, notes);
     }
     /*public GroceryItem(String name, 
    		 UUID id, int quantity, 
    		 String unitsOrSize, String categoryName, Boolean selected, String notes )
     {
    	 initialize(name, id, quantity,unitsOrSize,categoryName, selected, notes);
     }*/
     public GroceryItem(String name, 
    		 double quantity, 
    		 String unitsOrSize, /*UUID categoryId*/ String categoryName, Boolean selected, String notes )
     {
    	 initialize(name, /*UUID.randomUUID(),*/ quantity,unitsOrSize,categoryName, selected, notes);
     }
     private void initialize(String name, 
    		 /*UUID id,*/ double quantity, 
    		 String unitsOrSize, /*UUID categoryId*/ String categoryName, Boolean selected, String notes)
     {
         _name = name;
         _notes = notes;
         _notesSeparator =" : ";
         _lastPurchasedDate = new Date();
         _quantity = quantity;
         _unitsOrSize =unitsOrSize;
         //_categoryId = categoryId;
         _categoryName = (categoryName != null && categoryName.length() > 0) ? categoryName : GroceryItem.DEFAULT_CATEGORY_NAME;
         _selected = selected;
         //setId(id);

     }
        public boolean getSelected() { return _selected;}
        public void setSelected(boolean value){_selected=value; 
            //OnPropertyChanged("Selected");
        }
        
       
        public Date getLastPurchasedDate() {return _lastPurchasedDate;}
        public void setLastPurchasedDate(Date value){ _lastPurchasedDate = value;}

        public String getName (){return _name;}
        public void setName(String value) {_name=value;}
        
     
        public String getNotes(){return _notes;} 
        public void setNotes(String value){ _notes=value;}
        
        @Override
        public String toString()
        {            
            return String.format("%s: %s (%s)",getName(),getNotes(), getCategory().toString());
        }
        public String getNotesSeparator() {return _notesSeparator;}
        public void setNotesSeparator(String value){ _notesSeparator = value;}
		
        @Override
        public boolean equals(Object obj)
        {
        	boolean ret = false;
        	   if (this == obj) {
        		      ret = true;
        		    } 
        	   else if (obj instanceof GroceryItem) 
        	   {
        		      GroceryItem item = (GroceryItem) obj;
        		      ret = this.getName().toUpperCase().equals(item.getName().toUpperCase());
        	   }
        	   return ret;
        }
        @Override
        public <T extends PersistentItem> void copyTo(T dest) 
        {
        	this.copyGroceryItem((GroceryItem)dest);
        };
		public  void copyGroceryItem(GroceryItem dest) {
        	if  (dest != null)
        	{
        		dest.setLastPurchasedDate(getLastPurchasedDate());
        		dest.setNotes(getNotes());
        		dest.setSelected(getSelected());
        		dest.setName(getName());
        		dest.setQuantity(getQuantity());
        		dest.setUnitsOrSize(getUnitsOrSize());
        		//dest.setCategoryId(getCategoryId());
        		dest.setCategoryName(getCategoryName());
        	}
        }

		@Override
		public int compareTo(GroceryItem another) {
			int ret = -1;
			int catComparison;
			
			if (another != null) 
			{
				catComparison = this.getCategory().compareTo(another.getCategory());
				if (catComparison == 0)
				{
					// both are in the same category
					boolean selected = this.getSelected();
				
					if (another.getSelected() != selected)
					{
						ret = (selected ? -1 : 1);
					}
					else
					{
						String otherName = another.getName();
				
						if ((otherName != null) && !(otherName.isEmpty()))
						{
							ret= this.getName().compareToIgnoreCase(otherName);
						}
					}
				}
				else
				{
					ret = catComparison;
				}
					
			}
			return ret;
    }
		public double getQuantity() {
			return _quantity;
		}
		public void setQuantity(double quantity) {
			this._quantity = quantity;
		}
		public String getUnitsOrSize() {
			return _unitsOrSize;
		}
		public void setUnitsOrSize(String unitsOrSize) {
			this._unitsOrSize = unitsOrSize;
		}
		/*public UUID getCategoryId() {
			return _categoryId;
		}
		public void setCategoryId(UUID categoryId) {
			this._categoryId = categoryId;
		}*/
		public void setCategoryName(String categoryName) {
			this._categoryName = categoryName;
		}
		public String getCategoryName() {
			return _categoryName;
		}
		/*public UUID getId() {
			return _id;
		}
		public void setId(UUID id) {
			this._id = id;
		}
		*/
		public String getId() {
		return _categoryName;
	}
	public void setId(String categoryName) {
		this._categoryName = categoryName;
	}
	
		public String toJSONString() throws JSONException, IOException
		{
			
			RoboInjector injector = RoboGuice.getInjector(application);
			IGroceryItemSerializer serializer = injector.getInstance(IGroceryItemSerializer.class);
			return serializer.writeToJsonString(this);
		}
		 public static  GroceryItem fromJSONString(Context application, String serializedItem) throws JSONException
		{
			//RoboActivity context = new RoboActivity();
			 
			RoboInjector injector = RoboGuice.getInjector(application);
			IGroceryItemSerializer serializer = injector.getInstance(IGroceryItemSerializer.class);
			return serializer.parseFromJsonString(serializedItem);
		}
		public Category getCategory() {
			return Category.getCategory(this.getCategoryName());
		}

	
}   

