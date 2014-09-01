package com.amlm.honeygogroceryshopping.tests.mocks.grocerystoremanager;

import java.util.ArrayList;

import android.content.Context;

import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.amlm.honeygogroceryshopping.tests.mocks.grocerystore.MyGroceryStoreMock;

public class MyGroceryStoresManagerMock implements IGroceryStoreManager {
	
	String _current = "";
	/*@Override
	public String getCurrent() {
		// TODO Auto-generated method stub
		return _current;
	}

	@Override
	public void setCurrent(String value) {
		// TODO Auto-generated method stub
		_current = value;
	}
*/
	@Override
	public ArrayList<IGroceryStore> getGroceryStores() {
		// TODO Auto-generated method stub
		ArrayList<IGroceryStore> ret = new ArrayList<IGroceryStore>();
	
		IGroceryStore gStore=new MyGroceryStoreMock();
        gStore.setOriginalName("Publix");
		ret.add(gStore);

		gStore=new MyGroceryStoreMock();
        gStore.setOriginalName("WinnDixie");
		ret.add(gStore);
		
		gStore=new MyGroceryStoreMock();
        gStore.setOriginalName("Costco");
		ret.add(gStore); 
        
		
	
		return ret;
	}

	@Override
	public boolean areGroceryFilesShared(Context context, String storeName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGroceryStoreSharing(Context context, boolean sharing) {
		// TODO Auto-generated method stub
		
	}

	
}
