package com.etarkari.etarkari;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity {


	
	// Within which the entire activity is enclosed
	private DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	private ListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
	private ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	private String mTitle ;
	
	
	// Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> productsList;
    
    
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    
    // market node names
    private static final String TAG_PRODUCTS = "markets";
    private static final String TAG_MID = "id";
    private static final String TAG_M_CODE = "market_code";
    private static final String TAG_M_NAME = "market_name";
    private static final String TAG_M_ADDR = "market_address";
    private static final String TAG_M_GPS_LONG = "market_gps_long";
    private static final String TAG_M_GPS_LAT = "market_gps_lat";    
    private static final String TAG_M_URL = "market_url";
    private static final String TAG_M_IMG = "market_image";
    
    // price data node names
    private static final String TAG_P_PRODUCTS = "pricedata";
    private static final String TAG_PID = "id";
    private static final String TAG_P_MID = "market_id";
    private static final String TAG_P_M_CODE = "market_code";
    private static final String TAG_P_ITEM_NAME = "item_name";
    private static final String TAG_P_ITEM_UNIT = "item_unit";
    private static final String TAG_P_W_MIN = "w_min";
    private static final String TAG_P_W_MAX = "w_max";
    private static final String TAG_P_R_MIN = "r_min";
    private static final String TAG_P_R_MAX = "r_max";
    private static final String TAG_P_ADDEDON = "addedon";
    
    // settings node names
    private static final String TAG_UPDATE_DATE = "update_date";
    //private static final String TAG_SET_ID = "id";
    //private static final String TAG_SET_TITLE = "title";
    //private static final String TAG_SET_VALUE = "value";
    // price data node names
    //private static final String TAG_PRODUCTS1 = "pricedata";
    //private static final String TAG_PID = "id";
    //private static final String TAG_P_M_CODE = "market_code";
 
    // products JSONArray
    JSONArray products = null;
    JSONArray markets = null;
    JSONArray pricedata = null;


	DatabaseHelper controller = new DatabaseHelper(this);
	
	GPSTracker gps;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_main);
		
		mTitle =  getResources().getString(R.string.app_name);
		getActionBar().setTitle(mTitle);
		//getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
		
		
		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}	
		};
		
		// if GPS not enabled show dialog box
		boolean isGPSEnabled = false;
		LocationManager locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
		
		isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!isGPSEnabled){
			
			gps = new GPSTracker(this);
			gps.showSettingsAlert();
			
		}
		// check online or not
    	if (AppStatus.getInstance(MainActivity.this).isOnline()) {
    		//Toast.makeText(MainActivity.this, "You are online!!!!", Toast.LENGTH_LONG).show();    		
    		// Loading products in Background Thread
     		new LoadAllProducts().execute();
    	} else {
    		//Toast t = Toast.makeText(getActivity().getBaseContext(),"You are not online!!!!",Toast.LENGTH_LONG).show();
    		Toast.makeText(MainActivity.this,getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
    		Log.v("Home", "############################You are not online!!!!");
    		
    		
			
    	}
		
		if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
		
			
		
		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Creating an ArrayAdapter to add items to the listview mDrawerList
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), 
				R.layout.drawer_list_item, getResources().getStringArray(R.array.menus));*/
		
		List<HashMap<String,String>> data = GetSampleData();
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, 
				 R.layout.drawer_list_item, new String[] { "image", "list" },
		 new int[] { R.id.image,android.R.id.text1 });
		 
		
		

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			
			
		@Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
			
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/**
     * Event Handling for menu item selected
     * Identify single menu item by it's id
     * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch (item.getItemId())
        {
		
        case R.id.action_search:
            // search action
            return true;
            
        case R.id.action_update:

            Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();
         // check online or not
        	if (AppStatus.getInstance(MainActivity.this).isOnline()) {
        		//Toast.makeText(MainActivity.this, "You are online!!!!", Toast.LENGTH_LONG).show();
        		
        		// Loading products in Background Thread
         		new LoadAllProducts().execute();
        	} else {
        		//Toast t = Toast.makeText(getActivity().getBaseContext(),"You are not online!!!!",Toast.LENGTH_LONG).show();
        		Toast.makeText(MainActivity.this,getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
        		Log.v("Home", "############################You are not online!!!!");
    			
        	}	
            return true;
        case R.id.action_lang_en:
        	// change language to Englist
    		Locale locale0 = new Locale("en");
    		Locale.setDefault(locale0);
    		Configuration config0 = new Configuration();
            config0.locale = locale0;
            getBaseContext().getResources().updateConfiguration(config0, getBaseContext().getResources().getDisplayMetrics());
            refresh();
            Toast.makeText(this, "Language changed to English !", Toast.LENGTH_LONG).show();

            
            return true;
        
        case R.id.action_lang_ne:
        	// change language to Nepali
    		Locale locale1 = new Locale("ne");
    		Locale.setDefault(locale1);
    		Configuration config1 = new Configuration();
            config1.locale = locale1;
            getBaseContext().getResources().updateConfiguration(config1, getBaseContext().getResources().getDisplayMetrics());
            refresh();
            Toast.makeText(this, getString(R.string.language_changed_to_nepali), Toast.LENGTH_LONG).show();
            
            return true;
            
        default:
            return super.onOptionsItemSelected(item);    
            
        }
		
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		menu.findItem(R.id.action_lang_en).setVisible(!drawerOpen);
		menu.findItem(R.id.action_lang_ne).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	// Initiating Menu XML file (menu.xml)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new dashboard();
            break;
        case 1:
        	
        	if (!isDataExists()){
        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);
        		// set title
                alertDialogBuilder.setTitle(getString(R.string.data_not_updated));
             // set dialog message
                alertDialogBuilder
                        .setMessage(getString(R.string.data_sync_from_server))
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
        	
                // if online load data
        		if (AppStatus.getInstance(MainActivity.this).isOnline()) {            	
            		
            		// Loading products in Background Thread
             		new LoadAllProducts().execute();
            	}
        		
        		fragment = new dashboard();

        		
        	}        		
        	else
        		fragment = new market();
            break;

        
 
        default:
            break;
        }
 
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
 
            String[] menuItems = getResources().getStringArray(R.array.menus);
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
			mTitle = menuItems[position];
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("HomeActivity", "Error in creating fragment");
        }
    }
    
    List<HashMap<String,String>> GetSampleData()
    {
    	int[] pic = new int[]{R.drawable.ic_new_float_right,R.drawable.ic_new_stacked_coin,R.drawable.ic_menu_settings_holo_light};
    List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    
    HashMap<String,String> map = new HashMap<String,String>();
    String[] navItems = getResources().getStringArray(R.array.menus);
    map.put("list", navItems[0]); // Dashboard
    map.put("image", String.valueOf(pic[0]));
    list.add(map);
    
    map = new HashMap<String,String>();
    map.put("image", String.valueOf(pic[1]));
    map.put("list", navItems[1]); //Markets
    list.add(map);
    
    map = new HashMap<String,String>();
    map.put("image", String.valueOf(pic[2]));
    map.put("list", navItems[2]); //Settings
    list.add(map);
  
    
   return list;
    }
   
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
               
    	/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading data from server. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			//pDialog.show();
		}
		
    	/**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	
        	DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        	
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ApplicationSettings.url_all_products, "GET", params);
            
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            
            
            
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                
                // Checking for UPDATE_DATE TAG
                String update_date = json.getString(TAG_UPDATE_DATE);
                
            	//int success=1;
 
                if (success == 1) {
                	
                	//Log.d("db_update_date: ", db.getSettingsValue("UPDATE_DATE"));
                	
                	
                	String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                	String db_update_date="";
                	if (db.isTableExists("skbm_settings",true)) 
                		Log.d("table: ", "exists");
                	else
                		Log.d("table: ", "doesn't exists");
                	
                	Log.d("table count: ", String.valueOf(db.getSettingsCount()));
                	Log.d("Reading: ", "Reading all settings.."); 
                    if(Integer.valueOf(db.getSettingsCount())>0){
	                	List<SettingsModel> settings = db.getAllSettings();       
	                     
	                    for (SettingsModel cn : settings) {
	                        String log = "Id: "+ cn.getSettingsID()+
	                   		 			" ,Title: " + cn.getSettingsTitle() + 
	                   		 			" ,Value: " + cn.getSettingsValue();                        
	                       
	                        // Writing Contacts to log
	                        Log.d("Settings: ", log);
	                    }    
	                	//Log.d("get_value", String.valueOf(db.getSettingsValue("UPDATE_DATE")));
	                    try{
	                    	Log.d("db_update_date: ", db.getSettingsValue("UPDATE_DATE"));
	                    	db_update_date = db.getSettingsValue("UPDATE_DATE");
	                    	
	                    	
	                	} catch (SQLException e) {
	                		//throw e;
	                        //e.printStackTrace();
	                		
	                		
	                		                		
	                    	Log.d("err", e.getMessage());
	                    	
	                    	
	                    }
                    }else{
                    	db.addSettings(new SettingsModel("UPDATE_DATE",update_date));
                    }
                    Log.d("err: ", "point");
                	if (update_date.compareTo(db_update_date)>0){
                		
	                    // flush table
	                	//DatabaseHelper flush_db = new DatabaseHelper(getActivity());
                		try {
                			db.flushMarket();
    	                	Log.d("market", "main activity: flushed");
    	                	
    	                	db.flushPriceData();
    	                	Log.d("pricedata", "main activity: flushed");
                		}catch(SQLException e){
                			throw e;
                		}
            
	                	
	                	
	                	
	                	// settings
	                	Log.d("update date: ", update_date);
	                	Log.d("current date: ", currentDateandTime);
	                	//db.insertupdateSettings(new SettingsModel("UPDATE_DATE", update_date));
	                	
	                	try {
	                		db.updateSettings(new SettingsModel("UPDATE_DATE",update_date));
	                	} catch(SQLException e){
	                		db.addSettings(new SettingsModel("UPDATE_DATE",update_date));
	                		//db.updateSettings(new SettingsModel("UPDATE_DATE",update_date));
	                	}
	                		                                             
                        	                        	                     	                    
	    	
	                             
	                    // markets found
	                    // Getting Array of Markets
	                    products = json.getJSONArray(TAG_PRODUCTS);                   
	 
	                    // looping through All Products
	                    for (int i = 0; i < products.length(); i++) {
	                        JSONObject c = products.getJSONObject(i);
	 
	                        // Storing each json item in variable
	                        String mid = c.getString(TAG_MID);
	                        String mcode = c.getString(TAG_M_CODE);
	                        String mname = c.getString(TAG_M_NAME);
	                        String maddr = c.getString(TAG_M_ADDR);
	                        String mgpslong = c.getString(TAG_M_GPS_LONG);
	                        String mgpslat = c.getString(TAG_M_GPS_LAT);
	                        String murl = c.getString(TAG_M_URL);
	                        String mimg = c.getString(TAG_M_IMG);                                                                       
	                        
	 
	                        // creating new HashMap
	                        HashMap<String, String> map = new HashMap<String, String>();
	 
	                        // adding each child node to HashMap key => value
	                        //map.put(TAG_MID, mid);
	                        //map.put(TAG_M_CODE, mcode);
	                        Log.d("mcode: ", mcode);
	                        db.addMarket(new MarketModel(mcode, mname, maddr, mgpslong, mgpslat, murl, mimg));                        
	                        //Toast.makeText(MainActivity.this, "Loaded"+mcode, Toast.LENGTH_LONG).show();
	                        
	                     
	                    }
	                    
	                    // pricedata found
	                    // Getting Array of pricedata
	                    pricedata = json.getJSONArray(TAG_P_PRODUCTS);                   
	 
	                    // looping through All Products
	                    Log.d("Reading: ", "Reading all pricedata from json..");
	                    for (int i = 0; i < pricedata.length(); i++) {
	                        JSONObject c = pricedata.getJSONObject(i);
	 
	                        
	                        // Storing each json item in variable
	                    	String pid = c.getString(TAG_PID);
	                        String pmid = c.getString(TAG_P_MID);
	                        String pmcode = c.getString(TAG_P_M_CODE);
	                        String pitemname = c.getString(TAG_P_ITEM_NAME);
	                        String pitemunit = c.getString(TAG_P_ITEM_UNIT);
	                        String pwmin = c.getString(TAG_P_W_MIN);
	                        String pwmax = c.getString(TAG_P_W_MAX);
	                        String prmin = c.getString(TAG_P_R_MIN);
	                        String prmax = c.getString(TAG_P_R_MAX);
	                        String paddedon = c.getString(TAG_P_ADDEDON);
	                        
	                        
	 
	                       
	 
	                        Log.d("json item name: ", "Id: "+pid+ 
	                 		 	   " ,Item: " + pitemname+ 
	                		 	   " ,WMIN: " + pwmin +
	                		 	   " ,WMAX: " + pwmax +
	                		 	   " ,RMIN: " + prmin +
	                		 	   " ,RMAX: " + prmax);                       
	                        db.addPriceData(new PriceDataModel(pmid, pmcode, pitemname, pitemunit, pwmin, pwmax, prmin, prmax, paddedon));
	                        //Toast.makeText(MainActivity.this, "Loaded"+pitemname, Toast.LENGTH_LONG).show();	                    
	                    }
	                    //data load success message
	                    showToast(getString(R.string.data_succes_from_server));
                     
                    }else {
                    	
                    	Log.d("Status: ", "data up to date");
                    	//Toast.makeText(activity, "No update found!!!", Toast.LENGTH_LONG).show();
                    	showToast(getString(R.string.no_update_found)); 	
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            	//Log.d("err", "err");
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
    
    // refresh app
    private void refresh() {
        finish();
        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(myIntent);
    }
    
    // toast
    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private Boolean isDataExists(){
    	DatabaseHelper db = new DatabaseHelper(MainActivity.this);
    	if (db.isTableExists("skbm_settings",true)){  
    		if(Integer.valueOf(db.getSettingsCount())>0){
    			return true;
    		}else{
    			return false;
    		}
    	}else{
    		return false;
    	}
    }
    
    

}

	
