package com.amlm.honeygogroceryshopping.model;


public class DropboxInfo 
{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private String _key;
	private String _secret;
	
	//public static Integer  MAX_NUMBER_OF_CATEGORIES=100;
	
	public DropboxInfo(String key, String secret) 
	{
		super();
		initialize(key, secret);
		
	}
	public DropboxInfo()
	{
		initialize(new String(""), new String(""));
	}
	private void initialize(String key,  String secret)
	{
		
		setKey(key);
		setSecret(secret);
		

		
	}
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	public String getSecret() {
		return _secret;
	}
	public void setSecret(String secret) {
		this._secret = secret;
	}
	
	    
}
