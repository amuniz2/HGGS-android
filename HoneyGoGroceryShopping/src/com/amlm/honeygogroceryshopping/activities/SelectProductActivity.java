package com.amlm.honeygogroceryshopping.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectProductActivity extends Activity {

	public static final String PRODUCTS = "Products";
	public static final String PRODUCT_SELECTED= "ProductSelected";
	
	private String getTag()
	{ return "Select Product Acivity";}
	
	//private String _productName;
//	private int _productIndex;
	
/*	public String getProductName() {return _productName;}
	private void setProductName(String value) {_productName = value;}
	public int getProductIndex() {return _productIndex;}
	private void setProductIndex(int index) {_productIndex = index;}
	*/
	private ListView getListView(){return(ListView) findViewById(R.id.productlist);}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        try {
        	
  			setupList();
  		} catch (Exception e) {
  			BaseActivity.reportError(getTag(),e,this);
  			//e.printStackTrace();
  		}    	              
    }

    private void setupList()
    {
    	ListView listView = getListView();
    	ArrayList<String> prodNames= this.getIntent().getStringArrayListExtra(PRODUCTS);
  
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    			//android.R.layout.simple_list_item_multiple_choice,
    			R.layout.list_item_text_view,
    			prodNames);
    	
      //	adapter.setNotifyOnChange(true);
      	
      	
      	listView.setOnItemClickListener(new OnItemClickListener() 
      	{

            public void onItemClick(AdapterView<?> listView, View cell, int position,
                    long id) {
            	selectItem(position);
               
            }
        });    
    	
    	// Assign adapter to ListView
    	listView.setAdapter(adapter);     
    }
    public void selectItem (int position)
    {
    	ListView lv = getListView();
    	Intent intent = this.getIntent();
    	//setProductIndex(position);
    	// setProductName( (String)(lv.getItemAtPosition(position)));
    	intent.putExtra(PRODUCT_SELECTED,(String)(lv.getItemAtPosition(position)) );
    	
    	this.setResult(RESULT_OK, intent);
    	this.finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_product, menu);
        return true;
    }
}
