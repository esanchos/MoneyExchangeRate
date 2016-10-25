package com.earaujo.app.moneyexchangerate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private BaseAdapter sta;

    private Context contex;

    ImageLoadTask task;

    private static final String PRE_URL = "http://www.geognos.com/api/en/countries/flag/";

    public CountryItem(Context c, String name, String currencyCode, String imgUrl) {
        this.countryName = name;
        this.imgUrl = imgUrl;
        this.currencyCode = currencyCode;
        contex = c;

        this.image =  null;
        this.task = null;
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

    public void taskCancel() {
        if (task!=null) {
            task.cancel(true);
        }
        task=null;
    }

    public void loadImage(BaseAdapter sta) {
        image = FileOperations.readBitmap(contex,imgUrl + ".png");

        // HOLD A REFERENCE TO THE ADAPTER
        this.sta = sta;
        if (imgUrl != null && !imgUrl.equals("") && image==null) {
            task = (ImageLoadTask) new ImageLoadTask().execute(imgUrl);
        }
    }

    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            //Log.i("ImageLoadTask", "Loading image...");
        }

        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {
            if (isCancelled()) return null;
            //Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            String urldisplay = PRE_URL + param[0] + ".png";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                FileOperations.saveBitmap(contex,mIcon11,param[0] + ".png");
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                //Log.i("ImageLoadTask", "Successfully loaded " + countryName + " image");
                image = ret;
                if (sta != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    sta.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + countryName + " image");
            }
        }
    }
}
