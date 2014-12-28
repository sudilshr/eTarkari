package com.etarkari.etarkari;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapFromAsset {
	public Bitmap getBitmapFromAsset(Context ctx, String strName)
    {
        AssetManager assetManager = ctx.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("img/"+strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
}
