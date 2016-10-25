package com.earaujo.app.moneyexchangerate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 04/07/2016.
 */
public class ManageCurrencies extends Activity {

    private static final String TAG = "ManageCurrencies";

    private List<CountryItem> countryList;
    private List<CountryItem> exclusionCountryList;

    private ManageAdapter listAdapter;
    private ListView listView;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.manage_currencies);

        context = this;

        exclusionCountryList = new ArrayList<CountryItem>();
        countryList = new ArrayList<CountryItem>();

        Intent myIntent  = getIntent();
        CountriesIntent coutriesIntent =(CountriesIntent) myIntent.getExtras().getSerializable("CoutriesList");

        for (CountriesIntent.currencyItem ci : coutriesIntent.getItems()) {
            CountryItem item = new CountryItem(this, ci.getCountryName(), ci.getCurrencyCode(), ci.getFlag());
            countryList.add(item);
            if (ci.isExcluded()) {
                exclusionCountryList.add(item);
            }
        }
        fillList();
    }

    public void fillList() {
        listView = (ListView) findViewById(R.id.listView);

        listAdapter = new ManageAdapter(this, countryList,exclusionCountryList);
        if (listView==null) {
            Log.d(TAG, "LisView nulo. :(");
            return;
        }
        listView.setAdapter(listAdapter);

        for (CountryItem ci : countryList) {
            ci.loadImage(listAdapter);
        }
    }

    public void onAddAllClick(View view) {
        exclusionCountryList.clear();
        FileOperations.writeExcludedList(context, exclusionCountryList);
        listAdapter.notifyDataSetChanged();
    }

    public void onRemoveAllClick(View view) {
        exclusionCountryList.clear();
        exclusionCountryList.addAll(countryList);
        FileOperations.writeExcludedList(context, exclusionCountryList);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (CountryItem ci : countryList) {
            ci.taskCancel();
        }
    }
}
