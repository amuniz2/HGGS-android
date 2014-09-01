package com.amlm.honeygogroceryshopping.interfaces;

public class ConfirmReturn<T> {

	    DialogReturn dialogReturn;
	    T _objectToDelete;
	    public T getObjectToDelete() {return _objectToDelete;}
	    public void setObjectToDelete(T value) { _objectToDelete = value;}
	    public ConfirmReturn()
	    {
	    	super();
	    }
	    public interface DialogReturn {
	    	
	        void onConfirmed(boolean answer);
	    }

	    public void setListener(DialogReturn dialogReturn) {
	        this.dialogReturn = dialogReturn;
	    }

	    public DialogReturn getListener() {
	        return dialogReturn;

	    }
	
}
