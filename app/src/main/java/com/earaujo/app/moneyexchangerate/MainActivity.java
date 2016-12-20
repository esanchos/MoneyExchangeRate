package com.earaujo.app.moneyexchangerate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements CurrencyData.Listener, SpinnerAdapter.Listener{

    private static final String TAG = "MainActivity";

    //UI Objects
    private TextView tvFrom;
    private TextView tvTo;

    private EditText etBox1;
    private EditText etBox2;

    private Spinner spnCountry1;
    private Spinner spnCountry2;

    private TextView timeStamp;

    private boolean loadSpinners;

    //private Button manageCurrencies;

    //Main class to the currency data
    private CurrencyData currencyData;

    //Local control data
    private double rate;

    private SpinnerAdapter spinnerAdapter;

    private String positionSpin1;
    private String positionSpin2;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9938026363796976~6776883041");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("8FA911DB41DE8629DF3B72C3716E6087").build();
        //AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        context = this;

        rate=1;

        String tempPosition = FileOperations.readSpinPosition(context);
        if (tempPosition==null) {
            positionSpin1 = "USD";
            positionSpin2 = "BRL";
        }
        else {
            positionSpin1 = tempPosition.substring(0,3);
            positionSpin2 = tempPosition.substring(3,6);
        }

        setObjectsReferences();

        currencyData = new CurrencyData(this);
        currencyData.setFromCountry(positionSpin1);
        currencyData.setToCountry(positionSpin2);

        loadSpinners=true;

        etBox1.setText("1");
        etBox2.setText("3");

        etBox1.addTextChangedListener(textWatcherb1());
        etBox2.addTextChangedListener(textWatcherb2());

        getFirebaseToken();
    }

    private void getFirebaseToken() {
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);
    }

    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    private void setObjectsReferences() {
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);

        etBox1 = (EditText) findViewById(R.id.etBox1);
        etBox2 = (EditText) findViewById(R.id.etBox2);

        spnCountry1 = (Spinner) findViewById(R.id.spnCountry1);
        spnCountry2 = (Spinner) findViewById(R.id.spnCountry2);

        timeStamp = (TextView) findViewById(R.id.timeStamp);

        //manageCurrencies = (Button) findViewById(R.id.manageCurrencies);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        currencyData.readExcludedList();
        loadSpinners = true;
    }

    private void updateUI() {
        CountryItem cd;

        if (spinnerAdapter==null)
            return;

        timeStamp.setText(getDate(DownloadData.getTimeStamp()));

        cd = spinnerAdapter.getCountryFromId(spnCountry1.getSelectedItemPosition());
        tvFrom.setText("1 " + cd.getCountryName() + " equals");
        cd = spinnerAdapter.getCountryFromId(spnCountry2.getSelectedItemPosition());
        tvTo.setText(String.format( "%.2f", rate ).replace(",",".") + " " + cd.getCountryName());

        //etBox1.requestFocus();
        if (etBox2.isFocused()) {
            etBox1.requestFocus();
            etBox1.setText(etBox1.getText());
            etBox2.requestFocus();
            etBox2.setSelection(etBox2.getText().length());
            //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(etBox2, InputMethodManager.SHOW_IMPLICIT);
        }
        else {
            etBox1.setText(etBox1.getText());
            etBox1.setSelection(etBox1.getText().length());
            //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(etBox1, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void fillSpinners(List<CountryItem> countryItems) {

        spinnerAdapter = new SpinnerAdapter(this, countryItems);
        spinnerAdapter.setListener(this);
        //sta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCountry1.setAdapter(spinnerAdapter);

        for (CountryItem ci : countryItems) {
            ci.loadImage(spinnerAdapter);
        }

        spnCountry1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CountryItem cd = spinnerAdapter.getCountryFromId(i);
                currencyData.setFromCountry(cd.getCurrencyCode());
                rate = currencyData.getRate();
                positionSpin1 = cd.getCurrencyCode();
                //Log.d(TAG,"SPINPOS1: " + positionSpin1);
                FileOperations.writeSpinPosition(context,positionSpin1,positionSpin2);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnCountry2.setAdapter(spinnerAdapter);
        spnCountry2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CountryItem cd = spinnerAdapter.getCountryFromId(i);
                currencyData.setToCountry(cd.getCurrencyCode());
                rate = currencyData.getRate();
                positionSpin2 = cd.getCurrencyCode();
                //Log.d(TAG,"SPINPOS2: " + positionSpin2);
                FileOperations.writeSpinPosition(context,positionSpin1,positionSpin2);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private TextWatcher textWatcherb1() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etBox1.isFocused()) {

                    if (s.length() == 0) {
                        etBox2.setText("");
                    } else {
                        double value = Double.parseDouble(String.valueOf(s).replace(",","."));
                        String convertedValue = String.format( "%.2f", value * rate ).replace(",",".");//Double.toString(value * rate);
                        etBox2.setText(convertedValue);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private TextWatcher textWatcherb2() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etBox2.isFocused()) {
                    if (s.length() == 0) {
                        etBox1.setText("");
                    } else {
                        double value = Double.parseDouble(String.valueOf(s).replace(",","."));
                        String convertedValue = String.format( "%.2f", value / rate ).replace(",",".");//Double.toString(value / rate);
                        etBox1.setText(convertedValue);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public void onGetCurrencyCompleted() {
        updateUI();
    }

    public void onItemExcludeClick(CountryItem excludeItem) {
        currencyData.addExcludedItem(excludeItem);

        refreshSpinners(excludeItem);
    }

    public void refreshSpinners(CountryItem excludeItem) {

        String countryCode1 = spinnerAdapter.getItem(spnCountry1.getSelectedItemPosition()).getCurrencyCode();
        String countryCode2 = spinnerAdapter.getItem(spnCountry2.getSelectedItemPosition()).getCurrencyCode();

        spinnerAdapter.removeItem(excludeItem);

        //Adjust the selected items
        int newPosition1=0;
        int newPosition2=0;
        for(int i=0; i<spinnerAdapter.getCount();i++) {
            if (spinnerAdapter.getCountryFromId(i).getCurrencyCode().equals(countryCode1)) {
                newPosition1=i;
            }
            if (spinnerAdapter.getCountryFromId(i).getCurrencyCode().equals(countryCode2)) {
                newPosition2=i;
            }
        }

        //Log.d(TAG,"EXCL: SPN1: " + Integer.toString(newPosition1) + "SPN2: " + Integer.toString(newPosition2));

        if (newPosition1>=0)
            spnCountry1.setSelection(newPosition1);
        if (newPosition2>=0)
            spnCountry2.setSelection(newPosition2);

        positionSpin1 = spinnerAdapter.getCountryFromId(newPosition1).getCurrencyCode();
        positionSpin2 = spinnerAdapter.getCountryFromId(newPosition2).getCurrencyCode();

        FileOperations.writeSpinPosition(this,positionSpin1,positionSpin2);

        currencyData.setFromCountry(positionSpin1);
        currencyData.setToCountry(positionSpin2);
        rate = currencyData.getRate();

        //Log.d(TAG,"ON EXCL SPIN1: " + positionSpin1);
        //Log.d(TAG,"ON EXCL SPIN2: " + positionSpin2);

        updateUI();

        spinnerAdapter.notifyDataSetChanged();
    }

    public void onGetCountriesNames() {

        int spn1pos=0;
        int spn2pos=0;

        List<CountryItem> listCountries = new ArrayList<>();

        listCountries.addAll(currencyData.getCountryDataList());

        // REMOVE THE ITEM TO THE SPINNERS
        for(CountryItem ci: currencyData.getExcludedCountryList()) {
            listCountries.remove(ci);
        }

        //CHECK DEFAULT SPINNER POSITION
        int i=0;
        for(CountryItem cd: listCountries) {
            if (cd.getCurrencyCode().equals(positionSpin1)) {
                spn1pos=i;
            }
            if (cd.getCurrencyCode().equals(positionSpin2)) {
                spn2pos=i;
            }
            i++;
        }

        fillSpinners(listCountries);

        spnCountry1.setSelection(spn1pos);
        spnCountry2.setSelection(spn2pos);
    }

    public void onManageCurrenciesClick(View view) {

        if (!loadSpinners)
            return;
        loadSpinners=false;

        Intent manageCurrenciesIntent = new Intent(this,
                ManageCurrencies.class);

        final int result = 1;

        CountriesIntent ci = new CountriesIntent();

        boolean excluded;
        for (CountryItem items : currencyData.getCountryDataList()) {
            excluded = false;
            for (CountryItem exc : currencyData.getExcludedCountryList()){
                if (exc.getCurrencyCode().equals(items.getCurrencyCode())) {
                    excluded = true;
                }
            }
            items.taskCancel();  //Cancel Flag loading
            ci.add(items.getCountryName(),items.getCurrencyCode(),items.getImgUrl(),excluded);  //fill intent
        }

        manageCurrenciesIntent.putExtra("CoutriesList", ci);
        startActivityForResult(manageCurrenciesIntent, result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onGetCountriesNames();
        currencyData.getCurrencies();
    }

    @Override
    public void onBackPressed() {
        for (CountryItem items : spinnerAdapter.getAll()) {
            items.taskCancel();
        }
        super.onBackPressed();
    }

    public void deleteFlags(View view) {
        FileOperations.deleteFlags(this);
    }
}
