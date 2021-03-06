package com.earaujo.app.moneyexchangerate;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

import static android.text.Html.fromHtml;

public class MainActivity extends Activity implements
        CurrencyData.Listener, View.OnClickListener,
        MainCountryDialog.CountryDialogListener,
        DialogCountryAdapter.CountryItemListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_INVITE = 8001;
    private static final int MANAGE_CURRENCIES = 8002;

    //UI Objects
    private TextView tvBox1;
    private TextView tvBox2;

    private ImageView imgCountry1;
    private TextView tvCountry1;
    private ImageView imgCountry2;
    private TextView tvCountry2;

    private boolean loadSpinners;

    //Main class to the currency data
    private CurrencyData currencyData;

    private double rate;

    private String inputNumber = "1";

    private String positionSpin1;
    private String positionSpin2;

    List<CountryItem> selectedCountries;
    MainCountryDialog newFragment;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9938026363796976~6776883041");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("8FA911DB41DE8629DF3B72C3716E6087").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        context = this;

        selectedCountries = new ArrayList<>();

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

        setupComponents();

        currencyData = new CurrencyData(this);
        currencyData.setFromCountry(positionSpin1);
        currencyData.setToCountry(positionSpin2);
        rate = currencyData.getRate();

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

        String msg = getString(R.string.msg_token_fmt, token);
        //Log.d(TAG, msg);
    }

    private void setupComponents() {
        tvBox1 = (TextView) findViewById(R.id.tvBox1);
        tvBox2 = (TextView) findViewById(R.id.tvBox2);

        imgCountry1 = (ImageView) findViewById(R.id.imgCountry1);
        tvCountry1 = (TextView) findViewById(R.id.tvCountry1);
        imgCountry2 = (ImageView) findViewById(R.id.imgCountry2);
        tvCountry2 = (TextView) findViewById(R.id.tvCountry2);

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

        findViewById(R.id.upCurrency).setOnClickListener(this);
        findViewById(R.id.downCurrency).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

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

    private void updateCurrencyUI() {
        int countryPosition1=0;
        int countryPosition2=0;
        int i=0;
        for(CountryItem cd: selectedCountries) {
            if (cd.getCurrencyCode().equals(positionSpin1)) {
                countryPosition1=i;
            }
            if (cd.getCurrencyCode().equals(positionSpin2)) {
                countryPosition2=i;
            }
            i++;
        }
        updateCurrencyUI(selectedCountries.get(countryPosition1), selectedCountries.get(countryPosition2));
    }

    private void updateCurrencyUI(CountryItem countryItem1, CountryItem countryItem2) {

        imgCountry1.setImageBitmap(countryItem1.getImage());
        tvCountry1.setText(fromHtml("<b><font color=black>" + countryItem1.getCurrencyCode() + "</font></b> "));// +  countryItem1.getCountryName()));
        imgCountry2.setImageBitmap(countryItem2.getImage());
        tvCountry2.setText(fromHtml("<b><font color=black>" + countryItem2.getCurrencyCode() + "</font></b> "));// +  countryItem2.getCountryName()));
    }

    public void onGetCurrencyCompleted() {
        rate = currencyData.getRate();
        updateValues();
    }

    public void onGetCountriesNames() {

        int countryPosition1=-1;
        int countryPosition2=-1;

        selectedCountries.clear();

        selectedCountries.addAll(currencyData.getCountryDataList());

        // REMOVE THE ITEM TO THE SPINNERS
        for(CountryItem ci: currencyData.getExcludedCountryList()) {
            selectedCountries.remove(ci);
        }

        //CHECK DEFAULT SPINNER POSITION
        int i=0;
        for(CountryItem cd: selectedCountries) {
            if (cd.getCurrencyCode().equals(positionSpin1)) {
                countryPosition1=i;
            }
            if (cd.getCurrencyCode().equals(positionSpin2)) {
                countryPosition2=i;
            }
            i++;
            cd.loadImage();
        }

        if (countryPosition1==-1) {
            countryPosition1=0;
            positionSpin1=selectedCountries.get(0).getCurrencyCode();
            currencyData.setFromCountry(positionSpin1);
            rate = currencyData.getRate();
        }
        if (countryPosition2==-1) {
            countryPosition2=0;
            positionSpin2=selectedCountries.get(0).getCurrencyCode();
            currencyData.setToCountry(positionSpin2);
            rate = currencyData.getRate();
        }

        updateCurrencyUI(selectedCountries.get(countryPosition1), selectedCountries.get(countryPosition2));
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

        manageCurrenciesIntent.putExtra("CountriesList", ci);
        startActivityForResult(manageCurrenciesIntent, MANAGE_CURRENCIES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onGetCountriesNames();
        currencyData.getCurrencies();
        updateValues();
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
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    /* SWAP */
    private void onSwapCurrenciesClick() {
        String spinTemp;

        spinTemp = positionSpin1;
        positionSpin1 = positionSpin2;
        positionSpin2 = spinTemp;

        currencyData.setFromCountry(positionSpin1);
        currencyData.setToCountry(positionSpin2);
        rate = currencyData.getRate();

        updateCurrencyUI();
        updateValues();
    }

    /* Choose Country Dialog Listener */

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        onManageCurrenciesClick();
    }

    @Override
    public void onCountryClick(int pos, int callbackValue) {

        if (newFragment!=null) newFragment.dismiss();

        switch (callbackValue) {
            case 0:
                positionSpin1 = selectedCountries.get(pos).getCurrencyCode();
                break;
            case 1:
                positionSpin2 = selectedCountries.get(pos).getCurrencyCode();
                break;
        }
        currencyData.setFromCountry(positionSpin1);
        currencyData.setToCountry(positionSpin2);
        rate = currencyData.getRate();

        updateCurrencyUI();
        FileOperations.writeSpinPosition(context,positionSpin1,positionSpin2);
        updateValues();
    }

    private void showChooseCurrencyDialog(int position) {
        CountriesIntent ci = new CountriesIntent();

        for (CountryItem items : selectedCountries) {
            ci.add(items.getCountryName(),items.getCurrencyCode(),items.getImgUrl(),false);
        }
        newFragment = MainCountryDialog.newInstance(ci, position);
        newFragment.show(getFragmentManager(), "dialog");
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
            case R.id.upCurrency:
                showChooseCurrencyDialog(0);
                break;
            case R.id.downCurrency:
                showChooseCurrencyDialog(1);
                break;
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
                onSwapCurrenciesClick();
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
