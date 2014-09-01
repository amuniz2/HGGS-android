package com.amlm.honeygogroceryshopping.interfaces;


public interface IGroceryStore {
	public void setEnableSharing(boolean share);
	public boolean getEnableSharing();
	
	
	public String getOriginalName() ;
	public void setOriginalName(String value) ;
	
	public String getName();
	public void setName(String value);
	
	
}
