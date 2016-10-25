package com.earaujo.app.moneyexchangerate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 03/07/2016.
 */
public class ExcludedCountries {

    private List<CountryItem> countryItems;

    public ExcludedCountries() {
        countryItems = new ArrayList<CountryItem>();
    }
    public ExcludedCountries(List<CountryItem> items) {
        countryItems = new ArrayList<CountryItem>(items);
    }

    public void addCountry(CountryItem ci) {
        countryItems.add(ci);
    }

    public void removeCountry(CountryItem ci) {
        countryItems.remove(ci);
    }

    public List<CountryItem> getCountryItems() {
        return countryItems;
    }
}
