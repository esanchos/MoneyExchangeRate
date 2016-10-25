package com.earaujo.app.moneyexchangerate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 04/07/2016.
 */
public class CountriesIntent implements Serializable {

    private List<currencyItem> items = new ArrayList<currencyItem>();

    public void add(String cn, String cc, String fg, boolean excl) {
        items.add(new currencyItem(cn,cc,fg,excl));
    }

    public List<currencyItem> getItems() {
        return items;
    }

    public class currencyItem implements Serializable {
        private String countryName;
        private String currencyCode;
        private String flag;

        private boolean excluded;

        public currencyItem(String cn, String cc, String fg, boolean excl) {
            countryName = cn;
            currencyCode = cc;
            flag = fg;
            excluded = excl;
        }

        public String getCountryName() {
            return countryName;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getFlag() {
            return flag;
        }

        public boolean isExcluded() {
            return excluded;
        }
    }
}
