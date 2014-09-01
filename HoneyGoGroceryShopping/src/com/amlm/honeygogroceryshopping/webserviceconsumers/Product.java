package com.amlm.honeygogroceryshopping.webserviceconsumers;

public class Product implements Comparable<Product>
{
	private String _name;
	public String getName() {return _name;}
	public void setName(String name){_name=name;}
	
	
	
	@Override
	public int compareTo(Product arg0) {
		// TODO Auto-generated method stub
		return _name.compareTo(arg0.getName());
	}
	
}
