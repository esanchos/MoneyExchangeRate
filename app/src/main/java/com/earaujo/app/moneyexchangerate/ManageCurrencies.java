package com.earaujo.app.moneyexchangerate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 04/07/2016.
 */
public class ManageCurrencies extends AppCompatActivity {

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

        setupToolbar();

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

        countryList = sortList();

        fillList(countryList);
    }

    private void filter(String query) {
        List<CountryItem> countryFiltered = filterList(query.toLowerCase());
        fillList(countryFiltered);
    }

    private List<CountryItem> filterList(String query) {
        List<CountryItem>tempList = new ArrayList<>();

        for (CountryItem ci : countryList) {
            if ((ci.getCurrencyCode().toLowerCase().contains(query)) ||
                    (ci.getCountryName().toLowerCase().contains(query))){
                tempList.add(ci);
            }
        }

        return tempList;
    }

    private List<CountryItem> sortList() {
        List<CountryItem>tempList = new ArrayList<>();

        for (CountryItem ci : countryList) {
            if (!checkExcluded(ci)){
                tempList.add(ci);
            }
        }

        for (CountryItem excl : exclusionCountryList) {
            if (checkPriority(excl)) {
                tempList.add(excl);
            }
        }

        for (CountryItem excl : exclusionCountryList) {
            if (!checkPriority(excl)) {
                tempList.add(excl);
            }
        }

        return tempList;
    }

    private boolean checkExcluded(CountryItem ci) {
        for (CountryItem excl : exclusionCountryList) {
            if (excl.getCurrencyCode().equals(ci.getCurrencyCode())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPriority(CountryItem ci) {
        if ((ci.getCurrencyCode().equals("USD")) ||
                (ci.getCurrencyCode().equals("GBP")) ||
                (ci.getCurrencyCode().equals("JPY")) ||
                (ci.getCurrencyCode().equals("CNY")) ||
                (ci.getCurrencyCode().equals("CAD")) ||
                (ci.getCurrencyCode().equals("EUR")) ||
                (ci.getCurrencyCode().equals("BRL"))) {
            return true;
        }
        return false;
    }

    private void fillList(List<CountryItem> countryToShow) {
        listView = (ListView) findViewById(R.id.listView);

        listAdapter = new ManageAdapter(this, countryToShow,exclusionCountryList);
        if (listView==null) {
            Log.d(TAG, "LisView nulo. :(");
            return;
        }
        listView.setAdapter(listAdapter);

        for (CountryItem ci : countryToShow) {
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

    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.manage_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Search Currency");
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        //searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("NUNES",query);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("NUNES",newText);
                filter(newText);
                return false;
            }
        });
        changeSearchViewTextColor(searchView);

        return true;
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
}
