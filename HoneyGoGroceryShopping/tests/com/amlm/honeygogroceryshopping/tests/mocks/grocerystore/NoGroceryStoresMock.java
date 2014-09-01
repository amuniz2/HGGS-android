package com.amlm.honeygogroceryshopping.tests.mocks.grocerystore;

import com.amlm.honeygogroceryshopping.interfaces.IGroceryStore;

public class NoGroceryStoresMock implements IGroceryStore {

	boolean _share;
	String _originalName;
	String _name;
	
	@Override
	public void setEnableSharing(boolean share) {
		// TODO Auto-generated method stub
		_share = share;
	}

	@Override
	public boolean getEnableSharing() {
		// TODO Auto-generated method stub
		return _share;
	}

	@Override
	public String getOriginalName() {
		// TODO Auto-generated method stub
		return _originalName;
	}

	@Override
	public void setOriginalName(String value) {
		// TODO Auto-generated method stub
		_originalName = value;
		_name = value;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return _name;
	}

	@Override
	public void setName(String value) {
		// TODO Auto-generated method stub
		_name = value;
	}	

}
