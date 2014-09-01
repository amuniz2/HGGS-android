package com.amlm.honeygogroceryshopping.model;

import java.io.Serializable;

public abstract class PersistentItem implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  abstract <T extends PersistentItem> void copyTo( T dest);

//	public abstract void copyTo( PersistentItem dest);
}
