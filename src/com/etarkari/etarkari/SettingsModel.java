package com.etarkari.etarkari;

public class SettingsModel {
	
	//private variables
	int _id;
	String _title;
	String _value;
	
	
	// Empty constructor
	public SettingsModel(){
		
	}
	// constructor
	public SettingsModel(int id, String _title, String _value){
		this._id = id;
		this._title = _title;
		this._value = _value;
	}
	
	// constructor
	public SettingsModel(String _title, String _value){
		this._title = _title;
		this._value = _value;
	}
	
	// constructor
	public SettingsModel(String _title){
		this._title = _title;
	}
	
	
	

	/////////////
	// get
	/////////////
	
	public int getSettingsID(){
		return this._id;
	}
	
	public String getSettingsTitle(){
		return this._title;
	}
	
	public String getSettingsValue(){
		return this._value;
	}
	
	/////////////
	// set
	/////////////
	
	public void setSettingsID(int id){
		this._id = id;
	}
	
	public void setSettingsTitle(String settings_title){
		this._title = settings_title;
	}
	
	public void setSettingsValue(String settings_value){
		this._value = settings_value;
	}
	
}