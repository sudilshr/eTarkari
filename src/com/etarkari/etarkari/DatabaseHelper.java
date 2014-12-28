package com.etarkari.etarkari;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	 

	 // All Static variables
	
	// Logcat tag
	//private static final String LOG = DatabaseHelper.class.getName();
	
   // Database Version
   private static final int DATABASE_VERSION = 1;

   // Database Name
   private static final String DATABASE_NAME = "etarkari";

   // Market table names
   private static final String TABLE_MARKETS = "skbm_market";
   private static final String TABLE_PRICE_DATA = "skbm_price_data";
   private static final String TABLE_SETTINGS = "skbm_settings";
   

   // Market Table Columns names
   private static final String KEY_MKT_ID = "id";
   private static final String KEY_MKT_CODE = "market_code";
   private static final String KEY_MKT_NAME = "market_name";
   private static final String KEY_MKT_ADDR = "market_address";
   private static final String KEY_MKT_GPS_LONG = "market_gps_long";
   private static final String KEY_MKT_GPS_LAT = "market_gps_lat";
   private static final String KEY_MKT_URL = "market_url";
   private static final String KEY_MKT_IMAGE = "market_image";
   
   // pricedata Table Columns names
   private static final String KEY_PD_ID = "id";
   private static final String KEY_PD_MKT_ID = "market_id";
   private static final String KEY_PD_MKT_CODE = "market_code";
   private static final String KEY_PD_ITEM_NAME = "item_name";
   private static final String KEY_PD_ITEM_UNIT = "item_unit";
   private static final String KEY_PD_W_MIN = "w_min";
   private static final String KEY_PD_W_MAX = "w_max";
   private static final String KEY_PD_R_MIN = "r_min";
   private static final String KEY_PD_R_MAX = "r_max";
   private static final String KEY_PD_ADDEDON = "addedon";
   
   // pricedata Table Columns names
   private static final String KEY_SET_ID = "id";
   private static final String KEY_SET_TITLE = "title";
   private static final String KEY_SET_VALUE = "value";
   
   // Table Create Statements
   // Todo table create statement
   private static final String CREATE_TABLE_MARKETS = "CREATE TABLE " + TABLE_MARKETS + "("
           + KEY_MKT_ID + " INTEGER PRIMARY KEY," 
   		   + KEY_MKT_CODE + " TEXT,"
           + KEY_MKT_NAME + " TEXT," 
           + KEY_MKT_ADDR + " TEXT,"
           + KEY_MKT_GPS_LONG + " TEXT,"
           + KEY_MKT_GPS_LAT + " TEXT,"
           + KEY_MKT_URL + " TEXT,"
           + KEY_MKT_IMAGE + " TEXT"                
   		+ ")";
   private static final String CREATE_TABLE_PRICE_DATA = "CREATE TABLE " + TABLE_PRICE_DATA + "("
           + KEY_PD_ID + " INTEGER PRIMARY KEY," 
   		   + KEY_PD_MKT_ID + " TEXT,"
   		   + KEY_PD_MKT_CODE + " TEXT,"
           + KEY_PD_ITEM_NAME + " TEXT," 
           + KEY_PD_ITEM_UNIT + " TEXT,"
           + KEY_PD_W_MIN + " TEXT,"
           + KEY_PD_W_MAX + " TEXT,"
           + KEY_PD_R_MIN + " TEXT,"
           + KEY_PD_R_MAX + " TEXT,"
           + KEY_PD_ADDEDON + " TEXT"                
   		+ ")";
   
   private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS + "("
           + KEY_SET_ID + " INTEGER PRIMARY KEY," 
   		   + KEY_SET_TITLE + " TEXT,"
   		   + KEY_SET_VALUE + " TEXT"                
   		+ ")";

   public DatabaseHelper(Context context) {
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   // Creating Tables
   @Override
   public void onCreate(SQLiteDatabase db) {
	   
	   // creating required tables
       db.execSQL(CREATE_TABLE_MARKETS);
       db.execSQL(CREATE_TABLE_PRICE_DATA);
       db.execSQL(CREATE_TABLE_SETTINGS);
   }

   // Upgrading database
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   
	   // on upgrade drop older tables
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKETS);
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICE_DATA);
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
       

       // create new tables
       onCreate(db);
       
   }
   
   /**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
   

   // Adding new market
	public void addMarket(MarketModel market) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_MKT_CODE, market.getMarketCode()); // Market Code
	    values.put(KEY_MKT_NAME, market.getMarketName()); // Market Name	  
	    values.put(KEY_MKT_ADDR, market.getMarketAddress()); // Market Address
	    values.put(KEY_MKT_GPS_LONG, market.getMarketGPSLong()); // Market GPS Longitude
	    values.put(KEY_MKT_GPS_LAT, market.getMarketGPSLat()); // Market GPS Latitude
	    values.put(KEY_MKT_URL, market.getMarketURL()); // Market URL
	    values.put(KEY_MKT_IMAGE, market.getMarketImage()); // Market Image File Name
	  
	 
	    // Inserting Row
	    db.insert(TABLE_MARKETS, null, values);
	    db.close(); // Closing database connection
	}
	
	// Getting All Markets
	public List<MarketModel> getAllMarkets() {
		List<MarketModel> marketList = new ArrayList<MarketModel>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MARKETS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MarketModel market = new MarketModel();
				market.setID(Integer.parseInt(cursor.getString(0)));					
				market.setMarketCode(cursor.getString(1));				
				market.setMarketName(cursor.getString(2));
				market.setMarketAddress(cursor.getString(3));
				market.setMarketGPSLong(cursor.getString(4));
				market.setMarketGPSLat(cursor.getString(5));
				market.setMarketURL(cursor.getString(6));
				market.setMarketImage(cursor.getString(7));				
				
				// Adding market to list
				marketList.add(market);
			} while (cursor.moveToNext());
		}

		// return contact list
		return marketList;
	}
	
	
	
	// Updating single market
	public int updateMarket(MarketModel market) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MKT_CODE, market.getMarketCode());
		values.put(KEY_MKT_NAME, market.getMarketName());
		/*
		values.put(KEY_MKT_ADDR, market.getMarketAddress());
		values.put(KEY_MKT_GPS_LONG, market.getMarketGPSLong());
		values.put(KEY_MKT_GPS_LAT, market.getMarketGPSLat());
		values.put(KEY_MKT_URL, market.getMarketURL());
		values.put(KEY_MKT_IMAGE, market.getMarketImage());
		*/

		// updating row
		return db.update(TABLE_MARKETS, values, KEY_MKT_ID + " = ?",
				new String[] { String.valueOf(market.getID()) });
	}

	// Deleting single market
	public void deleteMarket(MarketModel market) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MARKETS, KEY_MKT_ID + " = ?",
				new String[] { String.valueOf(market.getID()) });
		db.close();
	}
	
	// flush market
	public void flushMarket() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MARKETS);
		db.close();
	}
	

	// Getting market Count
	public int getMarketsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MARKETS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for pricedata
	 */
   

   // Adding new market
	public void addPriceData(PriceDataModel pricedata) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_PD_MKT_ID, pricedata.getMarketId()); // Market ID
	    values.put(KEY_PD_MKT_CODE, pricedata.getMarketCode()); // Market Code
	    values.put(KEY_PD_ITEM_NAME, pricedata.getItemName()); // Item Name
	    values.put(KEY_PD_ITEM_UNIT, pricedata.getItemUnit()); // Item Unit
	    values.put(KEY_PD_W_MIN, pricedata.getWMin()); // Wholesale Min
	    values.put(KEY_PD_W_MAX, pricedata.getWMax()); // Wholesale Max
	    values.put(KEY_PD_R_MIN, pricedata.getRMin()); // Retail Min
	    values.put(KEY_PD_R_MAX, pricedata.getRMax()); // Retail Max
	    //values.put(KEY_PD_ADDEDON, pricedata.getAddedOn()); // Date Added
	    
	 
	    // Inserting Row
	    db.insert(TABLE_PRICE_DATA, null, values);
	    db.close(); // Closing database connection
	}
	
	// Getting All Price Data
	public List<PriceDataModel> getAllPriceData() {
		List<PriceDataModel> priceList = new ArrayList<PriceDataModel>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PRICE_DATA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PriceDataModel pricedata = new PriceDataModel();
				pricedata.setId(Integer.parseInt(cursor.getString(0)));	
				pricedata.setMarketId(cursor.getString(1));
				pricedata.setMarketCode(cursor.getString(2));
				pricedata.setItemName(cursor.getString(3));
				pricedata.setItemUnit(cursor.getString(4));
				pricedata.setWMin(cursor.getString(5));
				pricedata.setWMax(cursor.getString(6));
				pricedata.setRMin(cursor.getString(7));
				pricedata.setRMax(cursor.getString(8));
				pricedata.setAddedOn(cursor.getString(9));
				
				
				
				// Adding pricedata to list
				priceList.add(pricedata);
			} while (cursor.moveToNext());
		}

		// return contact list
		return priceList;
	}
	
	// Getting All Market Price Data
		public List<PriceDataModel> getMarketPriceData(String market_id) {
			List<PriceDataModel> priceList = new ArrayList<PriceDataModel>();
			
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRICE_DATA + " WHERE " + KEY_PD_MKT_ID;
			Log.d("query: ", selectQuery);
			Log.d("market_id: ", market_id);
			SQLiteDatabase db = this.getWritableDatabase();			
			Cursor cursor = db.rawQuery(selectQuery + "=" + Integer.valueOf(market_id), null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					PriceDataModel pricedata = new PriceDataModel();
					pricedata.setId(Integer.parseInt(cursor.getString(0)));	
					pricedata.setMarketId(cursor.getString(1));
					pricedata.setMarketCode(cursor.getString(2));
					pricedata.setItemName(cursor.getString(3));
					pricedata.setItemUnit(cursor.getString(4));
					pricedata.setWMin(cursor.getString(5));
					pricedata.setWMax(cursor.getString(6));
					pricedata.setRMin(cursor.getString(7));
					pricedata.setRMax(cursor.getString(8));
					pricedata.setAddedOn(cursor.getString(9));
					Log.d("item: ", cursor.getString(3));
					
					
					// Adding pricedata to list
					priceList.add(pricedata);
				} while (cursor.moveToNext());
			}

			// return contact list
			return priceList;
		}
	
	// Updating single pricedata
	public int updatePriceData(PriceDataModel pricedata) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		
		values.put(KEY_PD_MKT_ID, pricedata.getMarketId()); // Market ID
	    values.put(KEY_PD_MKT_CODE, pricedata.getMarketCode()); // Market Code
	    values.put(KEY_PD_ITEM_NAME, pricedata.getItemName()); // Item Name
	    values.put(KEY_PD_ITEM_UNIT, pricedata.getItemUnit()); // Item Unit
	    values.put(KEY_PD_W_MIN, pricedata.getWMin()); // Wholesale Min
	    values.put(KEY_PD_W_MAX, pricedata.getWMax()); // Wholesale Max
	    values.put(KEY_PD_R_MIN, pricedata.getRMin()); // Retail Min
	    values.put(KEY_PD_R_MAX, pricedata.getRMax()); // Retail Max
	    //values.put(KEY_PD_ADDEDON, pricedata.getAddedOn()); // Date Added

		// updating row
		return db.update(TABLE_PRICE_DATA, values, KEY_PD_ID + " = ?",
				new String[] { String.valueOf(pricedata.getId()) });
	}

	// Deleting single pricedata
	public void deletePriceData(PriceDataModel pricedata) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRICE_DATA, KEY_PD_ID + " = ?",
				new String[] { String.valueOf(pricedata.getId()) });
		db.close();
	}
	
	// flush pricedata
	public void flushPriceData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRICE_DATA);
		db.close();
	}
	

	// Getting price Count
	public int getPriceDataCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRICE_DATA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for SETTINGS
	 */
   

   // Adding new settings
	public void addSettings(SettingsModel settings) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_SET_ID, settings.getSettingsID()); // Settings ID
	    values.put(KEY_SET_TITLE, settings.getSettingsTitle()); // Settings Title
	    values.put(KEY_SET_VALUE, settings.getSettingsValue()); // Settings Value

	    
	 
	    // Inserting Row
	    db.insert(TABLE_SETTINGS, null, values);
	    db.close(); // Closing database connection
	}
	
	// Getting All Settings
	public List<SettingsModel> getAllSettings() {
		List<SettingsModel> settingsList = new ArrayList<SettingsModel>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SettingsModel settings = new SettingsModel();
				settings.setSettingsID(Integer.parseInt(cursor.getString(0)));	
				settings.setSettingsTitle(cursor.getString(1));
				settings.setSettingsValue(cursor.getString(2));
								
				
				// Adding settings to list
				settingsList.add(settings);
			} while (cursor.moveToNext());
		}

		// return contact list
		return settingsList;
	}

	// Updating single settings
	public void insertupdateSettings(SettingsModel settings) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		
		values.put(KEY_SET_ID, settings.getSettingsID()); // Settings ID
	    values.put(KEY_SET_TITLE, settings.getSettingsTitle()); // Settings Title
	    values.put(KEY_SET_VALUE, settings.getSettingsValue()); // Settings Value

		
	    int titleCount = getSettingsTitle(new SettingsModel(settings.getSettingsTitle()));
	    Log.d("titlecount: ", String.valueOf(titleCount));
	    if (titleCount<=0){
	    	db.insert(TABLE_SETTINGS, null, values);
	    	Log.d("table: ", "inserted");
	    }else{
	    	db.update(TABLE_SETTINGS, values, KEY_SET_TITLE + " = ?",
					new String[] { String.valueOf(settings.getSettingsTitle()) });
	    	Log.d("table: ", "updated");
	    }
	    
	}
		
	// Updating single settings
	public int updateSettings(SettingsModel settings) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		
		values.put(KEY_SET_ID, settings.getSettingsID()); // Settings ID
	    values.put(KEY_SET_TITLE, settings.getSettingsTitle()); // Settings Title
	    values.put(KEY_SET_VALUE, settings.getSettingsValue()); // Settings Value

		// updating row
		return db.update(TABLE_SETTINGS, values, KEY_SET_TITLE + " = ?",
				new String[] { String.valueOf(settings.getSettingsTitle()) });
	}

	// Deleting single settings
	public void deleteSettings(SettingsModel settings) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SETTINGS, KEY_SET_TITLE + " = ?",
				new String[] { String.valueOf(settings.getSettingsTitle()) });
		db.close();
	}
	
	// flush settings
	public void flushSettings() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_SETTINGS);
		db.close();
	}
	

	// Getting settings Count
	public int getSettingsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SETTINGS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();

		// return count
		return cursor.getCount();
	}
	
	// Getting settings Count
	public int getSettingsTitle(SettingsModel settings) {
		String countQuery = "SELECT  * FROM " + TABLE_SETTINGS + " WHERE " + KEY_SET_TITLE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(countQuery+ "=" + settings._title, null);
		
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
	// Getting settings Value
	public String getSettingsValue(String sTitle) {
		String countQuery = "SELECT  * FROM " + TABLE_SETTINGS + " WHERE " + KEY_SET_TITLE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		sTitle = sTitle.replaceAll("'", "''");
		//Cursor cursor = db.query(TABLE_SETTINGS, null, "title=?", new String[] { sTitle }, null, null, null);
		//Cursor cursor = db.rawQuery(countQuery+ "= '"+sTitle +"'", null);
		Cursor cursor = db.rawQuery(countQuery+ "= '" + sTitle + "'", null);
		
		

		// return count
		
		//SettingsModel settings1 = new SettingsModel();
		//settings1.setSettingsValue(cursor.getString(2));
		cursor.moveToFirst();
		return cursor.getString(2);
		//return settings1._value.toString();
	}
	
	public boolean isTableExists(String tableName, boolean openDb) {
		SQLiteDatabase mDatabase = this.getReadableDatabase();
	    if(openDb) {
	        if(mDatabase == null || !mDatabase.isOpen()) {
	            mDatabase = getReadableDatabase();
	        }

	        if(!mDatabase.isReadOnly()) {
	            mDatabase.close();
	            mDatabase = getReadableDatabase();
	        }
	    }

	    Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	                            cursor.close();
	            return true;
	        }
	                    cursor.close();
	    }
	    return false;
	}
}