package com.earaujo.app.moneyexchangerate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends Activity implements
        CurrencyData.Listener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_INVITE = 8001;
    private static final int MANAGE_CURRENCIES = 8002;

    //UI Objects
    private TextView tvBox1;
    private TextView tvBox2;

    private Spinner spnCountry1;
    private Spinner spnCountry2;

    private TextView timeStamp;

    private boolean loadSpinners;

    //private Button manageCurrencies;

    //Main class to the currency data
    private CurrencyData currencyData;

    //Local control data
    private double rate;

    private String inputNumber = "1";

    private SpinnerAdapter spinnerAdapter;

    private String positionSpin1;
    private String positionSpin2;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9938026363796976~6776883041");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("8FA911DB41DE8629DF3B72C3716E6087").build();
        AdRequest adRequest = new AdRequest.Builder().build();
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

        tvBox1.setText(inputNumber);
        tvBox2.setText("3");

        getFirebaseToken();

        AppRate.with(this)
                .setInstallDays(10) // default 10, 0 means install day.
                .setLaunchTimes(10) // default 10
                .setRemindInterval(5) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);

        deleteFlags();
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
        tvBox1 = (TextView) findViewById(R.id.tvBox1);
        tvBox2 = (TextView) findViewById(R.id.tvBox2);

        spnCountry1 = (Spinner) findViewById(R.id.spnCountry1);
        spnCountry2 = (Spinner) findViewById(R.id.spnCountry2);

        //timeStamp = (TextView) findViewById(R.id.timeStamp);

        findViewById(R.id.tv0).setOnClickListener(this);
        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
        findViewById(R.id.tv4).setOnClickListener(this);
        findViewById(R.id.tv5).setOnClickListener(this);
        findViewById(R.id.tv6).setOnClickListener(this);
        findViewById(R.id.tv7).setOnClickListener(this);
        findViewById(R.id.tv8).setOnClickListener(this);
        findViewById(R.id.tv9).setOnClickListener(this);

        findViewById(R.id.tvClr).setOnClickListener(this);
        findViewById(R.id.tvDel).setOnClickListener(this);
        findViewById(R.id.tvDot).setOnClickListener(this);

        findViewById(R.id.tvAdd).setOnClickListener(this);
        findViewById(R.id.tvShare).setOnClickListener(this);
        findViewById(R.id.tvRate).setOnClickListener(this);
        //manageCurrencies = (Button) findViewById(R.id.manageCurrencies);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
        else if (requestCode == MANAGE_CURRENCIES){
            currencyData.readExcludedList();
            loadSpinners = true;
        }
    }

    private void fillSpinners(List<CountryItem> countryItems) {

        spinnerAdapter = new SpinnerAdapter(this, countryItems);
        //sta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCountry1.setAdapter(spinnerAdapter);

        for (CountryItem ci : countryItems) {
            ci.loadImage();
        }

        spnCountry1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CountryItem cd = spinnerAdapter.getCountryFromId(i);
                currencyData.setFromCountry(cd.getCurrencyCode());
                rate = currencyData.getRate();
                positionSpin1 = cd.getCurrencyCode();
                FileOperations.writeSpinPosition(context,positionSpin1,positionSpin2);
                updateValues();
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
                FileOperations.writeSpinPosition(context,positionSpin1,positionSpin2);
                updateValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onGetCurrencyCompleted() {
        rate = currencyData.getRate();
        updateValues();
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

    public void onManageCurrenciesClick() {

        if (!loadSpinners)
            return;
        loadSpinners=false;

        Intent manageCurrenciesIntent = new Intent(this, ManageCurrencies.class);

        CountriesIntent ci = new CountriesIntent();

        boolean excluded;
        for (CountryItem items : currencyData.getCountryDataList()) {
            excluded = false;
            for (CountryItem exc : currencyData.getExcludedCountryList()){
                if (exc.getCurrencyCode().equals(items.getCurrencyCode())) {
                    excluded = true;
                }
            }
            ci.add(items.getCountryName(),items.getCurrencyCode(),items.getImgUrl(),excluded);  //fill intent
        }

        manageCurrenciesIntent.putExtra("CoutriesList", ci);
        startActivityForResult(manageCurrenciesIntent, MANAGE_CURRENCIES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onGetCountriesNames();
        currencyData.getCurrencies();
    }

    public void deleteFlags() {
        FileOperations.deleteFlags(this);
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
//                .setEmailSubject(getString(R.string.invitation_subject))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    /* KEYBOARD HANDLER */

    private double getDoubleFromInput() {
        String number = inputNumber;

        if (number.indexOf(".")==inputNumber.length()-1) {
            number+="0";
        }
        return Double.valueOf(number);
    }

    private void updateValues() {
        tvBox1.setText(inputNumber);

        double doubleInput = getDoubleFromInput();

        if (doubleInput==0) {
            tvBox2.setText("0");
        }
        else {
            //tvBox2.setText(Double.toString(doubleInput*rate));
            String convertedValue = String.format( "%.2f", doubleInput * rate ).replace(",",".");//Double.toString(value * rate);
            tvBox2.setText(convertedValue);
        }
    }

    private void addInputValue(int value) {
        if (inputNumber.equals("0")) {
            inputNumber = Integer.toString(value);
        }
        else {
            inputNumber += Integer.toString(value);
        }

        updateValues();
    }

    private void clearInput() {
        inputNumber = "0";

        updateValues();
    }

    private void delInput() {

        if (inputNumber.length()>0) {
            inputNumber = inputNumber.substring(0, inputNumber.length() - 1);
        }
        if (inputNumber.length()==0) {
            inputNumber="0";
        }
        updateValues();
    }

    private void addInputDot() {
        if (!inputNumber.contains(".")) {
            inputNumber += ".";
            updateValues();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv0:
                addInputValue(0);
                break;
            case R.id.tv1:
                addInputValue(1);
                break;
            case R.id.tv2:
                addInputValue(2);
                break;
            case R.id.tv3:
                addInputValue(3);
                break;
            case R.id.tv4:
                addInputValue(4);
                break;
            case R.id.tv5:
                addInputValue(5);
                break;
            case R.id.tv6:
                addInputValue(6);
                break;
            case R.id.tv7:
                addInputValue(7);
                break;
            case R.id.tv8:
                addInputValue(8);
                break;
            case R.id.tv9:
                addInputValue(9);
                break;
            case R.id.tvClr:
                clearInput();
                break;
            case R.id.tvDel:
                delInput();
                break;
            case R.id.tvDot:
                addInputDot();
                break;

            case R.id.tvAdd:
                onManageCurrenciesClick();
                break;
            case R.id.tvShare:
                onInviteClicked();
                break;
            case R.id.tvRate:
                AppRate.with(this).showRateDialog(this);
                break;
        }
    }
}
