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
    private BaseAdapter sta;

    private Context context;

    ImageLoadTask task;

    private static final String PRE_URL = "http://www.geognos.com/api/en/countries/flag/";

    public CountryItem(Context c, String name, String currencyCode, String imgUrl) {
        this.countryName = name;
        this.imgUrl = imgUrl;
        this.currencyCode = currencyCode;
        context = c;

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
        image = FileOperations.readBitmap(context,imgUrl + ".png");

        // HOLD A REFERENCE TO THE ADAPTER
        this.sta = sta;
        if (imgUrl != null && !imgUrl.equals("") && image==null) {

            String imageResource = imgUrl.toLowerCase();

            if (imageResource.equals("do"))
                imageResource = "dom";

            //Log.d("NUNES", "imgUrl: " + imgUrl);
            //if ((imgUrl.equals("GB")) ||
            //    (imgUrl.equals("CN"))) {
                int checkExistence = context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
                if (checkExistence != 0) {  // the resouce exists...
                    Log.d("NUNES", "TRUE");
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), checkExistence);
                    image = bitmap;
                    if (sta != null) {
                        sta.notifyDataSetChanged();
                    }
                } else {  // checkExistence == 0  // the resouce does NOT exist!!
                    Log.d("NUNES", "FALSE");
                    //task = (ImageLoadTask) new ImageLoadTask().execute(imgUrl);
                }
            //}


            //task = (ImageLoadTask) new ImageLoadTask().execute(imgUrl);
        }
    }

    /*// ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class imageReadFromResources extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        // PARAM[0] IS IMG URL
        protected Void doInBackground(String... param) {
            if (isCancelled()) return null;
            String imageName = param[0];
            Bitmap mIcon11 = null;
            try {
                String uri = "drawable/" + imageName;
                int imageResource = contex.getResources().getIdentifier(uri, null, contex.getPackageName());

                Drawable imageList;
                imageList.setImageResource(imageResource);
            }
            catch (Exception e) {

            }
        }

        protected void onPostExecute() {
            if (sta != null) {
                sta.notifyDataSetChanged();
            }
        }
    }*/

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
                FileOperations.saveBitmap(context,mIcon11,param[0] + ".png");
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
