package com.amlm.honeygogroceryshopping.tests.common;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.amlm.honeygogroceryshopping.activities.EditCurrentItemActivity;
import com.amlm.honeygogroceryshopping.activities.R;

public class BaseTestEditItem 
{
	private EditText _etName;
	//private Button _btnScan;
	private EditText _etQuantity;
	private EditText _etUnit;
	private EditText _etAdditionalInfo;
	private CheckBox _chkApplyToMaster;
	//private Button _btnCancel;
	private Button _btnSave;
	//private Button _btnDelete;
	//private Spinner _spCategory;
	private EditCurrentItemActivity _activity;
	
	public BaseTestEditItem()
	{
		super();
		_activity = null;
	}
	public BaseTestEditItem(EditCurrentItemActivity activity)
	{ 
		super();
		_activity = activity;
	}
	public void setup()
	{
		if (_activity == null)
		{
			_activity = new EditCurrentItemActivity();
			
			

		}
		_activity.onCreate(null);

		_etName = (EditText) _activity.findViewById(R.id.edit_name);
		_etQuantity = (EditText) _activity.findViewById(R.id.edit_quantity);
		_etUnit = (EditText) _activity.findViewById(R.id.edit_unitOrSize);
		_etAdditionalInfo =  (EditText) _activity.findViewById(R.id.edit_notes);
		// private Button _btnScan;
		//_spCategory = (Spinner) _activity.findViewById(R.id.location_spinner);
			_chkApplyToMaster = (CheckBox)  _activity.findViewById(R.id.applyToMaster);
		//_btnCancel = (Button)  _activity.findViewById(R.id.button_cancel);
		 _btnSave = (Button)  _activity.findViewById(R.id.button_save);
		//_btnDelete = (Button)  _activity.findViewById(R.id.button_delete);
		
	}
	
	public void saveNewItem(String name, Integer quantity, String unit, String additionalInfo, Boolean applyToMaster)
	{
		_etName.setText(name);
		_etQuantity.setText(quantity.toString());
		_etUnit.setText(unit);
		_etAdditionalInfo.setText(additionalInfo);
		_chkApplyToMaster.setChecked(applyToMaster);
		_btnSave.performClick();
	}
	public void teardown()
	{}
}
