package com.amlm.honeygogroceryshopping.webserviceconsumers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class SoapRequest extends SoapObject {

	public SoapRequest(String namespace, String name) {
		super(namespace, name);
		
	}
	public void addProperty(String name, Object type, String value)
	{
		 PropertyInfo prop = new PropertyInfo();
		 prop.setName(name);
		 prop.setType(type);
		 prop.setValue(value);
		 addProperty(prop);

	}
	public SoapSerializationEnvelope wrapInEnvelope()
	{
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);         
		 envelope.dotNet = true;         
		 envelope.setOutputSoapObject(this);   
		 return envelope;
	}
	public String getSoapAction()
	{
		return this.getNamespace() + this.getName();
		
	}
	
	
}
