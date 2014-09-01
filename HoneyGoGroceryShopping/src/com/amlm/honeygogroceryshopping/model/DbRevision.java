package com.amlm.honeygogroceryshopping.model;


public class DbRevision extends PersistentItem  implements Comparable<DbRevision> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private UUID _id;
	private String _name;
	private String _description;
	private int _order;
	
	public DbRevision()
	{
		initialize(/*UUID.randomUUID(),*/ new String(""), new String(""), Integer.valueOf(0));
	}
	public DbRevision(/*UUID id,*/ String name, String desc)
	{
		initialize(name, desc, Integer.valueOf(name));
	}
	public DbRevision(/*UUID id,*/ String name, String desc, Integer order)
	{
		initialize(name, desc,  order);
	}
	private void initialize(/*UUID id,*/ String name, String desc, Integer order)
	{
		//setId(id);
		setName(name);
		setDescription(desc);
		setOrder(order);
	}
	public int getOrder () {return _order;}
	public void setOrder(int order) {_order = order;}
	
	/*
	 * public void setId(UUID id)
	 
	{ 
		_id = id;
	}
	public UUID getId() 
	{ return _id;}
	*/
	public void setName(String name)
	{ _name = name;}
	public String getName() { return _name;}
	
	public void setDescription(String desc) {_description = desc;}
	public String getDescription(){return _description;}
	
	@Override
	public <T extends PersistentItem> void copyTo(T dest) {
		copyToAisle((DbRevision)dest);

	}
	@Override
	public int compareTo(DbRevision arg0) {
		int ret = -1;
		// TODO Auto-generated method stub
		if (arg0 != null)
		{
			Integer order = getOrder();
			Integer passedInOrder = arg0.getOrder();
			if (order != null)
			{
				if (passedInOrder != null)
				{
					ret = order.compareTo(passedInOrder);
				}
				else
				{
					ret =-1; 
				}
					
			}
			else if (passedInOrder != null)
			{
				ret = 1;
			}
			else
			{
				String name = getName();
				String namePassedIn = arg0.getName();
				ret = name.compareTo(namePassedIn);
			}
		}
		return ret;
			
	}
	 public void copyToAisle(DbRevision to)
	    {
	    	if (to != null)
	    	{
	    		to.setName(this.getName());
	    		to.setDescription(this.getDescription());
	    	}
	    }
	 @Override
     public boolean equals(Object obj)
     {
     	boolean ret = false;
     	   if (this == obj) {
     		      ret = true;
     		    } 
     	   else if (obj instanceof DbRevision) 
     	   {
     		      DbRevision aisle = (DbRevision) obj;
     		      //ret = (this.getId().equals(aisle.getId()));
     		     ret = (this.getName().equals(aisle.getName()));
     	   }
     	   return ret;
     }
	 @Override
	    public String toString()
	    {            
	        return getName();
	    }	 
}
