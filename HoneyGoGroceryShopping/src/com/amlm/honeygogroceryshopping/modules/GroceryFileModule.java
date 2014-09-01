package com.amlm.honeygogroceryshopping.modules;

import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryStore;
import com.amlm.honeygogroceryshopping.dataaccess.GroceryStoreManager;
import com.amlm.honeygogroceryshopping.interfaces.ICategoryListSerializer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryItemSerializer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.amlm.honeygogroceryshopping.interfaces.IItemListSerializer;
import com.amlm.honeygogroceryshopping.model.serialization.JsonCategoryList;
import com.amlm.honeygogroceryshopping.model.serialization.JsonItemList;
import com.amlm.honeygogroceryshopping.model.serialization.JSONGroceryItem;
import com.google.inject.AbstractModule;


public class GroceryFileModule extends AbstractModule  {

//	static public IDataAccessor getDataAccessor(Context context)
//	{
//		return new DataAccessor(context);
//	}

	public GroceryFileModule() { super(); }
	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(IGroceryFile.class).to(GroceryFile.class);
		bind(ICategoryListSerializer.class).to(JsonCategoryList.class);
		bind(IItemListSerializer.class).to(JsonItemList.class);
		bind(IGroceryItemSerializer.class).to(JSONGroceryItem.class);
		bind(IGroceryStore.class).to(GroceryStore.class);
		bind(IGroceryStoreManager.class).to(GroceryStoreManager.class);
		//	bind(Context.class).to(Context.class);
	}
}
