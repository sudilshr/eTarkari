package com.etarkari.etarkari;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;


public final class TranslateLang extends Activity  {

    private static TranslateLang instance = new TranslateLang();
    static Context context;    

    public static TranslateLang getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public String en_ne(Context ctx, String string) {
    	
    	String itemname;
    	String ne;
    	String neequal = "asdasd";
    	try{
    		Resources res = ctx.getResources();
	    	AssetManager assetManager = res.getAssets();
	    	
	    	String[] as =assetManager.list("test");
	    	for(int i=0;i<as.length;i++){
	    		Log.d("getassets", as[i]);
	    	}
    	} catch (IOException e) {
            //System.out.println("File Read Error");
        	Log.d("file: ", "read error");
        }
    	/*
    	try {
    		
    		AssetManager assetManager = getAssets();
        	InputStream instream = assetManager.open("itemlist.xml");
        	
            BufferedReader in = new BufferedReader(new InputStreamReader(instream));
            String str;
            str = in.readLine();
            while ((str = in.readLine()) != null) {
            	String[] ar=str.split(",");
            	itemname = ar[0];
            	ne = ar[1];
            	if (string==itemname) neequal = ne;
            	Log.d("itemname: ", itemname);
            	Log.d("ne: ", ne);
                //System.out.println(str);
            }
            in.close();
        } catch (IOException e) {
            //System.out.println("File Read Error");
        	Log.d("file: ", "read error");
        }
    	*/
    	return neequal;	
    	
    }
}