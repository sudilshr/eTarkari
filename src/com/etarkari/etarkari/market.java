package com.etarkari.etarkari;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class market extends Fragment {
	
public market(){}
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	// Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> productsList;
    
    // current locale
    private String curLocale;
     
     
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	
    	super.onActivityCreated(savedInstanceState);
    	
    	DatabaseHelper db = new DatabaseHelper(getActivity().getBaseContext());
    	
        // get app current locale
        Configuration config = new Configuration(getResources()
                .getConfiguration());
        curLocale = config.locale.getISO3Language();      

 		
         ListView expGridView = (ListView)getView().findViewById(R.id.market_layout);
  
         // preparing list data
        // prepareListData();
         List<HashMap<String,String>> data = GetSampleData();
         
         
         SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), data, 
				 R.layout.market_list_row, new String[] { "image", "list","market_code","market_id","market_address" },
		 new int[] { R.id.image,R.id.title,R.id.market_code,R.id.market_id,R.id.market_address });
         
  
         // setting list adapter
         expGridView.setAdapter(adapter);
         
         expGridView.setOnItemClickListener(new OnItemClickListener() {			
			
 			@Override
 	        public void onItemClick(AdapterView<?> parent, View view, int position,
 	                long id) {
 	            // display view for selected nav drawer item
 				/*Fragment fragment = null;		
 		        fragment = new pricelist();	
	
 				 if (fragment != null) {
 		            FragmentManager fragmentManager = getFragmentManager();
 		            fragmentManager.beginTransaction()
 		                    .replace(R.id.content_frame, fragment).commit();
 		 
 		     	           
 		        }*/
 				
 				Intent parentIntent1 = new Intent(getActivity().getBaseContext(),ShowPrice.class);
 				
 				HashMap<String, Object> obj = (HashMap<String, Object>) parent.getItemAtPosition(position);
 				
 				parentIntent1.putExtra("MARKET_NAME", (String) obj.get("list"));
 				parentIntent1.putExtra("MARKET_ID", (String) obj.get("market_id"));
 				//Object itmname = parent.getItemAtPosition(position);
 				
 				
 				
 	            
 	            
 		        //Log.d("common  clicked", "clicked -" +parent.getItemAtPosition(position));
 				Log.d("common  clicked", "clicked -" +(String) obj.get("list"));
 		        
 	          	startActivity(parentIntent1);
 	          	
 	            
 	        }
 				
 			});
         
         //Toast.makeText(getActivity().getBaseContext(), selected, Toast.LENGTH_LONG)
         /**
          * CRUD Operations
          * */
         // Inserting Contacts
         //Log.d("Insert: ", "Inserting ..");
         //db.addMarket(new MarketModel("SKBM005", "Test", "test", "12", "14", "http", "a"));
         
          
         // Reading all contacts
         Log.d("Reading: ", "Reading all contacts.."); 
         List<MarketModel> markets = db.getAllMarkets();       
          
         for (MarketModel cn : markets) {
             String log = "Id: "+ cn.getID()+
        		 			" ,Market Name: " + cn.getMarketName() + 
        		 			" ,Market Code: " + cn.getMarketCode() +
        		 			" ,Market Address: " + cn.getMarketAddress()+
        		 			" ,Market GPS Long: " + cn.getMarketGPSLong()+
        		 			" ,Market GPS Lat: " + cn.getMarketGPSLat()+
        		 			" ,Market URL: " + cn.getMarketCode()+
        		 			" ,Market Image: " + cn.getMarketCode();
             
             TextView test = new TextView(getActivity().getBaseContext());
             test.setText(log);
            
             //ml.addView(test);
             //el.addView(test);
             // ml.addView(test);
             
             // Writing Contacts to log
             Log.d("Name: ", log);
         }
    	
    	
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.market, container, false);
        
       return rootView;
    }

    List<HashMap<String,String>> GetSampleData(){
    	int[] pic = new int[]{R.drawable.market};
	    List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	    
	    HashMap<String,String> map = new HashMap<String,String>();
	    
	    // load from db
	    DatabaseHelper db = new DatabaseHelper(getActivity().getBaseContext());
	    List<MarketModel> markets = db.getAllMarkets();       
	    
	    for (MarketModel cn : markets) {
	        String log = "Id: "+cn.getID()+" ,Market Name: " + cn.getMarketName() + " ,Market Code: " + cn.getMarketCode();
	        
	        TextView test = new TextView(getActivity().getBaseContext());
	        test.setText(log);
	       
	        //add to list
	        map = new HashMap<String,String>();
	        map.put("image", String.valueOf(pic[0]));
	        if (curLocale.equals("nep")){
	        	if (en_ne(cn.getMarketCode()).equals("")){
	        		map.put("list", cn.getMarketName());
	        	}else{
	        		map.put("list", en_ne(cn.getMarketCode()));
	        	}	        	
	        }else{
	        	map.put("list", cn.getMarketName());
	        }
	        	
	        map.put("market_code", cn.getMarketCode());
	        map.put("market_id", String.valueOf(cn.getID()));
	        map.put("market_address", String.valueOf(cn.getMarketAddress()));
	        list.add(map);
	        
	        // Writing Contacts to log
	        //Log.d("Name: ", log);
	    }
        Log.d("Current Lang", curLocale);
	    return list;
    }

    
    // en to ne conversion from file
    private String en_ne(String string ) {

    	String itemname;
    	String ne;
    	String neequal = "";
    	
    	try {
    		
    		AssetManager assetManager = getActivity().getAssets();
        	InputStream instream = assetManager.open("marketlist.xml");
        	
            BufferedReader in = new BufferedReader(new InputStreamReader(instream));
            String str;
            
            while ((str = in.readLine()) != null) {
            	
            	String[] ar = str.split(",");
            	itemname = ar[0];
            	ne = ar[1];
            	if (string.trim().equals(itemname)) neequal = ne;
            	Log.d("compare", string.trim()  + "-"+ itemname);            	
            }
            in.close();
        } catch (IOException e) {            
        	Log.d("file: ", "read error");
        }
    	
    	return neequal;	

    }
       

}
