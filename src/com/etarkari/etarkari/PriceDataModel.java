package com.etarkari.etarkari;


public class PriceDataModel {
 
	//private variables
	int _id;
	String _market_id;
	String _market_code;
	String _item_name;
	String _item_unit;
	String _w_min;
	String _w_max;
	String _r_min;
	String _r_max;
	String _addedon;
	
 
    // constructors
    public PriceDataModel() {
    }
 
    public PriceDataModel(String _market_id, String _market_code, String _item_name, String _item_unit,String _w_min,String _w_max,String _r_min,String _r_max,String _addedon) {
        this._market_id = _market_id;
        this._market_code = _market_code;
        this._item_name = _item_name;
        this._item_unit = _item_unit;
        this._w_min = _w_min;
        this._w_max = _w_max;
        this._r_min = _r_min;
        this._r_max = _r_max;
        this._addedon = _addedon;
    }
 
    public PriceDataModel(int id, String _market_id, String _market_code, String _item_name, String _item_unit,String _w_min,String _w_max,String _r_min,String _r_max,String _addedon) {
        this._id = id;
        this._market_id = _market_id;
        this._market_code = _market_code;
        this._item_name = _item_name;
        this._item_unit = _item_unit;
        this._w_min = _w_min;
        this._w_max = _w_max;
        this._r_min = _r_min;
        this._r_max = _r_max;
        this._addedon = _addedon;
    }
 
	

	
    // setters
    public void setId(int id) {
        this._id = id;
    }
 
    public void setMarketId(String market_id) {
        this._market_id = market_id;
    }
    
    public void setMarketCode(String market_code) {
        this._market_code = market_code;
    }
    
    public void setItemName(String item_name) {
        this._item_name = item_name;
    }
    
    public void setItemUnit(String item_unit) {
        this._item_unit = item_unit;
    }
    
    public void setWMin(String w_min) {
        this._w_min = w_min;
    }
    
    public void setWMax(String w_max) {
        this._w_max = w_max;
    }
    
    public void setRMin(String r_min) {
        this._r_min = r_min;
    }
    
    public void setRMax(String r_max) {
        this._r_max = r_max;
    }
    
    public void setAddedOn(String addedon) {
        this._addedon = addedon;
    }
  
    // getters
    public long getId() {
        return this._id;
    }
 
    public String getMarketId() {
        return this._market_id;
    }
    
    public String getMarketCode() {
        return this._market_code;
    }
    
    public String getItemName() {
        return this._item_name;
    }
    
    public String getItemUnit() {
        return this._item_unit;
    }
    
    public String getWMin() {
        return this._w_min;
    }
    
    public String getWMax() {
        return this._w_max;
    }
    
    public String getRMin() {
        return this._r_min;
    }
    
    public String getRMax() {
        return this._r_max;
    }
    
    public String getAddedOn() {
        return this._addedon;
    }
 
}