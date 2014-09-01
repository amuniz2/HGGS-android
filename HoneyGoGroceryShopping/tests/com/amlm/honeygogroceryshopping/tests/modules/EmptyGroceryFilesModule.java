package com.amlm.honeygogroceryshopping.tests.modules;


import com.amlm.honeygogroceryshopping.interfaces.ICategoryListSerializer;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryItemSerializer;
import com.amlm.honeygogroceryshopping.interfaces.IItemListSerializer;
import com.amlm.honeygogroceryshopping.tests.mocks.groceryfile.EmptyGroceryFileMock;
import com.amlm.honeygogroceryshopping.tests.mocks.grocerystore.NoGroceryStoresMock;
import com.amlm.honeygogroceryshopping.tests.mocks.grocerystoremanager.NoGroceryStoresManagerMock;
import com.amlm.honeygogroceryshopping.tests.mocks.serializers.MockCategoryListSerializer;
import com.amlm.honeygogroceryshopping.tests.mocks.serializers.MockItemListSerializer;
import com.amlm.honeygogroceryshopping.tests.mocks.serializers.MockJsonGroceryItem;
import com.google.inject.AbstractModule;


	public class EmptyGroceryFilesModule extends AbstractModule  {

//		static public IDataAccessor getDataAccessor(Context context)
//		{
//			return new DataAccessor(context);
//		}

		
		@Override
		protected void configure() {
			// TODO Auto-generated method stub
			bind(IGroceryFile.class).to(EmptyGroceryFileMock.class);
			bind(ICategoryListSerializer.class).to(MockCategoryListSerializer.class);
			bind(IItemListSerializer.class).to(MockItemListSerializer.class);
			bind(IGroceryItemSerializer.class).to(MockJsonGroceryItem.class);
			bind(IGroceryStore.class).to(NoGroceryStoresMock.class);
			bind(IGroceryStoreManager.class).to(NoGroceryStoresManagerMock.class);
		}
	}	

