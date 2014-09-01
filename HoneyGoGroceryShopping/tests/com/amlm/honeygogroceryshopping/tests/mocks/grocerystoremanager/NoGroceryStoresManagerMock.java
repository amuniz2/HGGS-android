package com.amlm.honeygogroceryshopping.tests.mocks.grocerystoremanager;

import java.util.ArrayList;

import android.content.Context;

import com.amlm.honeygogroceryshopping.dataaccess.GroceryFile;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;
import com.amlm.honeygogroceryshopping.interfaces.IGroceryStoreManager;
import com.amlm.honeygogroceryshopping.tests.mocks.grocerystore.NoGroceryStoresMock;
//import com.google.inject.Inject;

public class NoGroceryStoresManagerMock implements IGroceryStoreManager {
	
	String _current = "";
	/*@Override
	public String getCurrent() {
		
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
		IGroceryStore gStore=new NoGroceryStoresMock();
        gStore.setOriginalName(GroceryFile.defaultStoreName);
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
