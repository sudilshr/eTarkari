package com.etarkari.etarkari;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.etarkari.maps.GoogleMapper;
import com.etarkari.maps.Results;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class dashboard extends Fragment {
	
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild; 
	private TextView latituteField;
	private TextView longitudeField;
	private TextView gpsStatus;
	private TextView txtUpdatedOn;
	private TextView txtUpdatedOnString;
	
	private Exception exception;

	
	
	GPSTracker gps;
	private static String get_location_url = "http://maps.google.com/maps/api/directions/json?origin=49.75332,6.50322&destination=49.71482,6.49944&mode=walking&sensor=false";
	
	Document document;
    LatLng fromPosition;   		
    LatLng toPosition;
	
	//public dashboard(){}
	
	
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	
    	super.onActivityCreated(savedInstanceState);
    	
    	latituteField = (TextView) getView().findViewById(R.id.TextView02);
    	longitudeField = (TextView) getView().findViewById(R.id.TextView04);
    	gpsStatus = (TextView) getView().findViewById(R.id.TextGPSStatus);
    	
       
       gps = new GPSTracker(getActivity().getBaseContext());
       //if(gps.isGPSEnabled)
    	//   new LoadGPS().execute();
       // check if GPS enabled     
       
    
       
       //
       
       fromPosition = new LatLng(49.75332, 6.50322);
       toPosition = new LatLng(49.71482,6.49944);
      
       try{
       GMapV2GetRouteDirection v2GetRouteDirection = new GMapV2GetRouteDirection();
       document = v2GetRouteDirection.getDocument(fromPosition,
				toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
	   int cDistance = v2GetRouteDirection.getDistanceValue(document);
	   //gpsStatus.setText(cDistance);
	   //Log.d("distance: ", cDistance);
       } catch (Exception e){
    	   exception = e;
    	   Log.d("log: ","err");
       }
       
       if(gps.isGPSEnabled){
    	   
          // new LoadGPS().execute();
           double latitude = gps.getLatitude();
           double longitude = gps.getLongitude();
           
            
           // \n is for new line
           //Toast.makeText(getActivity().getApplicationContext(), "Your Location is: \nLatitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_LONG).show(); 
           latituteField.setText(Double.toString(latitude));
           longitudeField.setText(Double.toString(longitude));
           //gpsStatus.setText("Success");
           
           
       }else{
           // can't get location
           // GPS or Network is not enabled
           // Ask user to enable GPS/network in settings
           //gps.showSettingsAlert();
           //gps.buildAlertMessageNoGps();
      
           gpsStatus.setText("Cannot retrive location");
       }
       
    	 
    	DatabaseHelper db = new DatabaseHelper(getActivity().getBaseContext());
 
    	//show updated date
    	txtUpdatedOn = (TextView) getView().findViewById(R.id.txtUpdateOnDate);
    	txtUpdatedOnString = (TextView) getView().findViewById(R.id.txtUpdateOnString);
    	Log.d("table:", String.valueOf((db.isTableExists("skbm_settings",true))));
    	if (db.isTableExists("skbm_settings",true)){  
    		if(Integer.valueOf(db.getSettingsCount())>0){
    			try{        		
                	Log.d("db_update_date: ", db.getSettingsValue("UPDATE_DATE"));
                	txtUpdatedOn.setText(db.getSettingsValue("UPDATE_DATE"));
                	            	
            	} catch (SQLException e) {
            		//throw e;
                    //e.printStackTrace();
            		
            		txtUpdatedOnString.setVisibility(View.GONE);
            		txtUpdatedOn.setText(getString(R.string.not_updated));
           		                		
                	Log.d("err", e.getMessage());
               	
                }
    		}else{
    			txtUpdatedOnString.setVisibility(View.GONE);
        		txtUpdatedOn.setText("NOT UPDATED");
    		}
    		
    	} else {
    		txtUpdatedOnString.setVisibility(View.GONE);
    		txtUpdatedOn.setText("NOT UPDATED");
    	}
    	
    	 
         //LinearLayout ml = (LinearLayout)getView().findViewById(R.id.linear);
         
         // get the listview
         ExpandableListView expListView = (ExpandableListView)getView().findViewById(R.id.lvExp);
  
         // preparing list data
         prepareListData();
         
         ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);
  
         // setting list adapter
         expListView.setAdapter(listAdapter);
         
         
         
         //Toast.makeText(getActivity().getBaseContext(), selected, Toast.LENGTH_LONG)
         /**
          * CRUD Operations
          * */
         // Inserting Contacts
         //Log.d("Insert: ", "Inserting ..");
         //db.addMarket(new MarketModel("SKBM005", "Test", "test", "12", "14", "http", "a"));
         
          
         // Reading all market
         Log.d("Reading: ", "Reading all markets..");
         List<MarketModel> markets = db.getAllMarkets();       
          
         for (MarketModel cn : markets) {
             String log = "Id: "+cn.getID()+" ,Market Name: " + cn.getMarketName() + " ,Market Code: " + cn.getMarketCode();
             
             TextView test = new TextView(getActivity().getBaseContext());
             test.setText(log);
            
             //ml.addView(test);
             //el.addView(test);
            // ml.addView(test);
             
                 // Writing Contacts to log
         //Log.d("Name: ", log);
         }    
          // Reading all market
         Log.d("Reading: ", "Reading all pricedata..");
         List<PriceDataModel> pricedata = db.getAllPriceData();       
          
         for (PriceDataModel cn1 : pricedata) {
             String log1 = "Id: "+cn1.getId()+ 
            		 	   " ,Item: " + cn1.getItemName() + 
            		 	   " ,WMIN: " + cn1.getWMin() +
            		 	   " ,WMAX: " + cn1.getWMax() +
            		 	   " ,RMIN: " + cn1.getRMin() +
            		 	   " ,RMAX: " + cn1.getRMax();
             Log.d("PriceData: ", log1);
         }
         
    	
    	
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.dashboard, container, false);
        
       return rootView;
    }
    
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Popular Items");
        listDataHeader.add("Favourite Items");
        listDataHeader.add("Others");
 
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Aalo");
        top250.add("Bhanta");
        top250.add("Syau");
        
 
        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Syau");
        nowShowing.add("Kakro");
 
        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("...");
        comingSoon.add("...");
        comingSoon.add("...");

 
        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
    
    List<HashMap<String,String>> GetSampleData()
	 {
		 final DatabaseHelper db=new DatabaseHelper(getActivity().getBaseContext());
			
		 List<MarketModel> marketlist = db.getAllMarkets();
		 List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		    
		 HashMap<String,String> map;
	        
       for (MarketModel cn : marketlist) {	
	        String mcode =  cn.getMarketCode();
	        String mname = cn.getMarketName();
	        map = new HashMap<String,String>();
	        map.put("mcode", mcode);
	   	    map.put("mname", mname );
	   	    list.add(map);      
        }  
	    return list;
	 }
    
    /**
     * Background Async Task to Load gps
     * */
    class LoadGPS extends AsyncTask<String, String, String> {
               
    	/**
		 * Before starting background thread Show Progress Dialog
		 * */
    	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//pDialog = new ProgressDialog(MainActivity.this);
			//pDialog.setMessage("Loading data from server. Please wait...");
			//pDialog.setIndeterminate(false);
			//pDialog.setCancelable(false);
			//pDialog.show();
		}
		
    	/**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	if(gps.isGPSEnabled){
                
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                
                 
                // \n is for new line
                //Toast.makeText(getActivity().getApplicationContext(), "Your Location is: \nLatitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_LONG).show(); 
                latituteField.setText(Double.toString(latitude));
                longitudeField.setText(Double.toString(longitude));
                gpsStatus.setText("Success");
                
                
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //gps.showSettingsAlert();
                //gps.buildAlertMessageNoGps();
           
                gpsStatus.setText("Cannot retrive location");
            }
    
        	
            return null;
        }
 
        /**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			//pDialog.dismiss();
			// updating UI from Background Thread
			//showToast(getString(R.string.data_succes_from_server));
			//runOnUiThread(new Runnable() {
			//	public void run() {
			//		/**
			//		 * Updating parsed JSON data into ListView
			//		 * */
			//		ListAdapter adapter = new SimpleAdapter(
			//				AllProductsActivity.this, productsList,
			//				R.layout.list_item, new String[] { TAG_PID,
			//						TAG_NAME},
			//				new int[] { R.id.pid, R.id.name });
			//		// updating listview
			//		setListAdapter(adapter);
			//	}
			//});
			

		}
 
    }
    



    
        
}
