package com.earaujo.app.moneyexchangerate;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 01/07/2016.
 */
public class CurrencyData implements DownloadData.Listener{

    private static final String TAG = "CurrencyData";

    private Listener mListener;
    private Context context;

    private List<CountryItem> countryDataList;
    private List<RateData> rateDataList;
    private List<CountryItem> excludedCountryList;

    private String fromCountry;
    private String toCountry;

    private long timeStamp;

    public CurrencyData(Context c) {
        context = c;
        mListener = (Listener) c;

        countryDataList = new ArrayList<CountryItem>(DownloadData.getDefaultCountries(c));
        rateDataList = new ArrayList<RateData>(DownloadData.getDefaultRates(c));

        readExcludedList();
    }

    public void readExcludedList() {
        excludedCountryList = new ArrayList<>();

        List<CountryItem> tempExcludeList;

        tempExcludeList = FileOperations.readExcludedList(context);
        if (tempExcludeList==null) {
            for(CountryItem ci: countryDataList) {
                if (!ci.getCurrencyCode().equals("USD") &&
                        !ci.getCurrencyCode().equals("BRL") &&
                        !ci.getCurrencyCode().equals("EUR")
                        ) {
                    excludedCountryList.add(ci);
                }
            }
        }
        else {
            for (CountryItem exclude: tempExcludeList) {
                for (CountryItem ci : countryDataList) {
                    if (ci.getCurrencyCode().equals(exclude.getCurrencyCode())) {
                        excludedCountryList.add(ci);
                    }
                }
            }
        }

        if (excludedCountryList.size()>=countryDataList.size()) {
            for (CountryItem ci: countryDataList) {
                if (ci.getCurrencyCode().equals("USD") ||
                        ci.getCurrencyCode().equals("BRL") ||
                        ci.getCurrencyCode().equals("EUR")) {
                    excludedCountryList.remove(ci);
                }
            }
        }
    }

    public void getCountries() {
        DownloadData.getCountries(this,context);
    }

    public void getCurrencies() {
        DownloadData.getCurrencies(this, context);
    }

    public void onGetCountriesNames(List<CountryItem> newItems) {
        countryDataList.clear();
        for (CountryItem ci: newItems) {
            countryDataList.add(ci);
        }
        mListener.onGetCountriesNames();
    }

    public void onGetCurrencyCompleted(List<RateData> newItems) {
        rateDataList.clear();
        for (RateData rd: newItems) {
            rateDataList.add(rd);
        }
        mListener.onGetCurrencyCompleted();
    }

    public List<CountryItem> getCountryDataList() {
        return countryDataList;
    }

    public CountryItem getCountryFromId(int id) {
        return countryDataList.get(id);
    }

    public List<RateData> getRateDataList() {
        return rateDataList;
    }

    public List<CountryItem> getExcludedCountryList() {
        return excludedCountryList;
    }

    public void addExcludedItem(CountryItem itemToExclude) {
        if (!excludedCountryList.contains(itemToExclude)) {
            excludedCountryList.add(itemToExclude);
        }
        FileOperations.writeExcludedList(context,excludedCountryList);
    }

    public void setFromCountry(String fromCountry) {
        //Log.d("MainActivity","fromCountry: " + fromCountry);
        this.fromCountry = fromCountry;
    }

    public void setToCountry(String toCountry) {
        //Log.d("MainActivity","ToCOuntry: " + toCountry);
        this.toCountry = toCountry;
    }

    public double getRate() {
        double uprate;
        double downrate;

        uprate = getRateFromCountryCode(fromCountry);
        downrate = getRateFromCountryCode(toCountry);

        if (uprate==0) return 1d;

        return downrate/uprate;
    }

    public Double getRateFromCountryCode(String cc) {
        String searchCode = "USD" + cc;
        for(RateData rd: rateDataList) {
            //Log.d("NUNES","countryDataList: " + rd.getcCode() + " - " + rd.getcRate().toString());
            if (rd.getcCode().equals(searchCode)) {
                return rd.getcRate();
            }
        }
        return 0d;
    }

    public interface Listener {
        void onGetCurrencyCompleted();
        void onGetCountriesNames();
    }

    /*public void setListener(Listener l) {
        mListener = l;
    }*/

}
