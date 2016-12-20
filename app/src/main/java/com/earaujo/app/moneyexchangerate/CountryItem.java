package com.earaujo.app.moneyexchangerate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import java.io.InputStream;

/**
 * Created by Eduardo on 02/07/2016.
 */
public class CountryItem {

    private static final String TAG = "CountryItem";

    private String countryName;
    private String imgUrl;
    private String currencyCode;

    private Bitmap image;

    private Context context;

    public CountryItem(Context c, String name, String currencyCode, String imgUrl) {
        this.countryName = name;
        this.imgUrl = imgUrl;
        this.currencyCode = currencyCode;
        context = c;

        this.image =  null;
    }

    public String getCountryName() {
        return countryName;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void loadImage() {
        //image = FileOperations.readBitmap(context,imgUrl + ".png");

        if (imgUrl != null && !imgUrl.equals("") && image==null) {

            String imageResource = imgUrl.toLowerCase();

            if (imageResource.equals("do"))
                imageResource = "dom";

            int checkExistence = context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
            if (checkExistence != 0) {  // the resouce exists...
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), checkExistence);
                image = bitmap;
            } else {  // checkExistence == 0  // the resouce does NOT exist!!
            }
        }
    }
}
