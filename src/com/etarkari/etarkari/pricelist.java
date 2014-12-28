package com.etarkari.etarkari;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class pricelist extends Fragment {
	
public pricelist(){}
List<String> listDataHeader;
HashMap<String, List<String>> listDataChild; 
	
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	
    	super.onActivityCreated(savedInstanceState);
    	
    	getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    	
    	 DatabaseHelper db = new DatabaseHelper(getActivity().getBaseContext());
 
    	 
         //LinearLayout ml = (LinearLayout)getView().findViewById(R.id.linear);
         
         // get the listview
         ListView expListView = (ListView)getView().findViewById(R.id.list);
  
         // preparing list data
        // prepareListData();
         List<HashMap<String,String>> data = GetSampleData1();
         
         
         SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), data, 
				 R.layout.list_row, new String[] { "image", "list" },
		 new int[] { R.id.image,R.id.title });
         
         //ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);
  
         // setting list adapter
         expListView.setAdapter(adapter);
         
         //Toast.makeText(getActivity().getBaseContext(), selected, Toast.LENGTH_LONG)
         /**
          * CRUD Operations
          * */
         // Inserting Contacts
         Log.d("Insert: ", "Inserting ..");
         db.addMarket(new MarketModel("SKBM005", "Test", "test", "12", "14", "http", "a"));
         
          
         // Reading all contacts
         Log.d("Reading: ", "Reading all contacts.."); 
         List<MarketModel> markets = db.getAllMarkets();       
          
         for (MarketModel cn : markets) {
             String log = "Id: "+cn.getID()+" ,Market Name: " + cn.getMarketName() + " ,Market Code: " + cn.getMarketCode();
             
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
  
        View rootView = inflater.inflate(R.layout.price_list, container, false);
        
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
    List<HashMap<String,String>> GetSampleData1()
    {
    	int[] pic = new int[]{R.drawable.ic_launcher};
    List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    
    HashMap<String,String> map = new HashMap<String,String>();
    
    map.put("list", "aalooo");
    map.put("image", String.valueOf(pic[0]));
    list.add(map);
    
    map = new HashMap<String,String>();
    map.put("image", String.valueOf(pic[0]));
    map.put("list", "Markets");
    list.add(map);
    
    map = new HashMap<String,String>();
    map.put("image", String.valueOf(pic[0]));
    map.put("list", "Settings");
    list.add(map);
  
    
   return list;
    }

   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            Intent parentIntent1 = new Intent(getActivity().getBaseContext(),MainActivity.class);
          	  startActivity(parentIntent1);
          	  return true;
            	/*Fragment fragment = null;
 		        fragment = new market();			
 				 if (fragment != null) {
 		            FragmentManager fragmentManager = getFragmentManager();
 		            fragmentManager.beginTransaction()
 		                    .replace(R.id.content_frame, fragment).commit();	           
 		        }*/
               
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
