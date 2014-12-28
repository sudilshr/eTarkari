package com.etarkari.etarkari;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fedorvlasov.lazylist.LazyAdapter;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ShowPrice extends ActionBarActivity {

	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	//market id
	private String p_market_id;
	
	// Title of the action bar
	private String mTitle;
	
	// Progress Dialog
    private ProgressDialog pDialog;
    
    // current locale
    private String curLocale;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		DatabaseHelper db = new DatabaseHelper(this);
		
	
        // get app current locale
        Configuration config = new Configuration(getResources()
                .getConfiguration());
        curLocale = config.locale.getISO3Language();
        
        
		//retrive market id
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String market_value = extras.getString("MARKET_ID");
		    String title_value = extras.getString("MARKET_NAME");
		    p_market_id = market_value;
		    mTitle = title_value;
		    Log.d("retrived market: ", market_value + title_value);
		}
		
		getActionBar().setTitle(mTitle);
		
		
		
		
		try{
			ListView expListView = (ListView)findViewById(R.id.list);
			//ListView expListView = (ListView)findViewById(R.id.list);
			
			List<HashMap<String,String>> data = GetSampleData();
			SimpleAdapter adapter = new SimpleAdapter(this, data, 
					 R.layout.list_row, new String[] { "image", "list", "rmin", "rmax","wmin","wmax" },
			 new int[] {R.id.image, R.id.title, R.id.rmin, R.id.rmax,R.id.wmin,R.id.wmax });
			
			
			//ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);
			
			 
	        // setting list adapter
			
	        expListView.setAdapter(adapter);
	        
			
		}catch (SQLiteException e){
			    if (e.getMessage().toString().contains("no such table")){
			            Log.e("table: ", "table because it doesn't exist!" );
			            // create table
			            // re-run query, etc.
			            Toast.makeText(ShowPrice.this, getString(R.string.no_data_found), Toast.LENGTH_LONG).show();
			    }
			}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            Intent parentIntent1 = new Intent(this,MainActivity.class);
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
    
    List<HashMap<String,String>> GetSampleData(){
    	int[] pic = new int[]{R.drawable.capsicum,R.drawable.apple,R.drawable.banana,R.drawable.asparagus,R.drawable.bitter_gourd};
    	
    	
	    List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	    
	    HashMap<String,String> map = new HashMap<String,String>();
	    
	    // load from db
	    DatabaseHelper db = new DatabaseHelper(ShowPrice.this);
	    
    
	    
		
	    List<PriceDataModel> pricedata = db.getMarketPriceData(p_market_id);       
	    
	    for (PriceDataModel cn : pricedata) {
	        String log = "Id: "+cn.getId()+" ,Item Name: " + cn.getItemName();
	        
	        TextView test = new TextView(ShowPrice.this);
	        test.setText(log);
	        //String itemDesc =  "Retail: " + cn.getRMin()+"(MIN)" + cn.getRMax()+"(MAX)" + "|| Wholsale: "+cn.getWMin()+"(MIN)" + cn.getWMax()+"(MAX)";
	        //add to list
	        map = new HashMap<String,String>();
	         
	        
	        
	        String itemNameEN =cn.getItemName();
	        itemNameEN = itemNameEN.trim();
	        itemNameEN = itemNameEN.toLowerCase();
	        
	        if(itemNameEN.equals("apple"))
	        	map.put("image", String.valueOf(pic[1]));
	        else if (itemNameEN.equals("banana"))
	        	map.put("image", String.valueOf(pic[2]));
	        else if (itemNameEN.equals("asparagus"))
	        	map.put("image", String.valueOf(pic[3]));
	        else if (itemNameEN.equals("bitter gourd"))
	        	map.put("image", String.valueOf(pic[4]));
	        else
	        	map.put("image", String.valueOf(pic[0]));
	        
	        
	        
	        String path = "img/"+itemNameEN+".png";
	    	Drawable d = null;
	    	try {
	    	     //d = Drawable.createFromStream(getAssets().open(path), null);
	    	} catch(Exception e) {

	    	}
	    	
	        //map.put("image", String.valueOf(d));
	        
	        String itemUnitEN =cn.getItemUnit();
	        itemUnitEN = itemUnitEN.trim();
	        itemUnitEN = itemUnitEN.toLowerCase();
	
	        String itemName, itemUnit;
	        Log.d("item: ", en_ne(itemNameEN));
	        if (curLocale.equals("nep")){
	        	if (en_ne(itemNameEN).equals("")){
	        		itemName = cn.getItemName();	        			        	
	        	}else{	     
	        		itemName = en_ne(itemNameEN);	        		
	        	}
	        	
	        	if (en_ne(itemUnitEN).equals("")){	        		
	        		itemUnit = cn.getItemUnit();	        	
	        	}else{	   	        		
	        		itemUnit = en_ne(itemUnitEN);
	        	}
	        }else{
	        	itemName = cn.getItemName();
        		itemUnit = cn.getItemUnit();
	        	//map.put("list", cn.getItemName()+" ("+cn.getItemUnit()+")");
	        }
	        
	        map.put("list", itemName+" ("+itemUnit+")");
	        //map.put("list", cn.getItemName()+" ("+cn.getItemUnit()+")");
	        //map.put("description", itemDesc);
	        /*
	        map.put("rmin", showZero(cn.getRMin()));
	        map.put("rmax", showZero(cn.getRMax()));
	        map.put("wmin", showZero(cn.getWMin()));
	        map.put("wmax", showZero(cn.getWMax()));
	        */
	        map.put("rmin", currency_en_ne(cn.getRMin()));
	        map.put("rmax", currency_en_ne(cn.getRMax()));
	        map.put("wmin", currency_en_ne(cn.getWMin()));
	        map.put("wmax", currency_en_ne(cn.getWMax()));
	        
	        list.add(map);
      	    	       
	        
	        // Writing Contacts to log
	        Log.d("Name: ", log);
	    }
        
	    return list;
    }
    // 0 if no value
    
    private String showZero(String string){
    	if(string.equals(" "))
    		return "0";
    	else if(string.equals(null))
    		return "0";
    	else    		    	
    		return string;
    }
    // en to ne conversion from file
    private String en_ne(String string ) {

    	String itemname;
    	String ne;
    	String neequal = "";
    	
    	try {
    		
    		AssetManager assetManager = getAssets();
        	InputStream instream = assetManager.open("itemlist.xml");
        	
            BufferedReader in = new BufferedReader(new InputStreamReader(instream));
            String str;
            
            while ((str = in.readLine()) != null) {
            	
            	String[] ar = str.split(",");
            	itemname = ar[0];
            	ne = ar[1];
            	string = string.trim();
            	string = string.toLowerCase();
            	if (string.equals(itemname)) neequal = ne;
            	Log.d("compare", string.trim()  + "-"+ itemname);            	
            }
            in.close();
        } catch (IOException e) {            
        	Log.d("file: ", "read error");
        }
    	
    	return neequal;	

    }
	
 // en to ne conversion from file
    private String currency_en_ne(String string ) {
    	char[] split_char = string.toCharArray();
    	char[] split_char_ne=split_char;
    	if (curLocale.equals("nep")){ 
    	 
    		for(int num=0;num<split_char.length;num++){
    			
 	        		split_char_ne[num] = en_ne(String.valueOf(split_char[num])).charAt(0);

    	 
    		}
    		return en_ne("rs.")+String.valueOf(split_char_ne);
    		
    	}
    	return "Rs."+string;
    }
}

	
