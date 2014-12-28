package com.etarkari.etarkari;

public class MarketModel {
	
	//private variables
	int _id;
	String _market_code;
	String _market_name;
	String _market_address;
	String _market_gps_long;
	String _market_gps_lat;
	String _market_url;
	String _market_image;
	
	
	// Empty constructor
	public MarketModel(){
		
	}
	// constructor
	public MarketModel(int id, String _market_code, String _market_name, String _market_address,String _market_gps_long,String _market_gps_lat,String _market_url,String _market_image){
		this._id = id;
		this._market_code = _market_code;
		this._market_name = _market_name;
		this._market_address = _market_address;
		this._market_gps_long = _market_gps_long;
		this._market_gps_lat = _market_gps_lat;
		this._market_url = _market_url;
		this._market_image = _market_url;
	}
	
	// constructor
	public MarketModel(String _market_code, String _market_name, String _market_address,String _market_gps_long,String _market_gps_lat,String _market_url,String _market_image){
		this._market_code = _market_code;
		this._market_name = _market_name;
		this._market_address = _market_address;
		this._market_gps_long = _market_gps_long;
		this._market_gps_lat = _market_gps_lat;
		this._market_url = _market_url;
		this._market_image = _market_url;
	}
	

	/////////////
	// GET
	/////////////
	
	public int getID(){
		return this._id;
	}
	
	public String getMarketCode(){
		return this._market_code;
	}

	public String getMarketName(){
		return this._market_name;
	}
	
	public String getMarketAddress(){
		return this._market_address;
	}
	
	public String getMarketGPSLong(){
		return this._market_gps_long;
	}
	
	public String getMarketGPSLat(){
		return this._market_gps_lat;
	}
	
	public String getMarketURL(){
		return this._market_url;
	}
	
	public String getMarketImage(){
		return this._market_image;
	}
	
	
	/////////////
	// SET
	/////////////
	
	public void setID(int id){
		this._id = id;
	}

	public void setMarketCode(String market_code){
		this._market_code = market_code;
	}

	public void setMarketName(String market_name){
		this._market_name = market_name;
	}
	
	public void setMarketAddress(String market_address){
		this._market_address = market_address;
	}
	
	
	public void setMarketImage(String market_image){
		this._market_image = market_image;
	}
	
	public void setMarketGPSLong(String market_gps_long){
		this._market_gps_long = market_gps_long;
	}

	public void setMarketGPSLat(String market_gps_lat){
		this._market_gps_lat = market_gps_lat;
	}
	
	public void setMarketURL(String market_url){
		this._market_url = market_url;
	}	

	
}