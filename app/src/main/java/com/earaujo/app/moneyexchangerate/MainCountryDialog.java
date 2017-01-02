package com.earaujo.app.moneyexchangerate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 02/01/2017.
 */

public class MainCountryDialog extends DialogFragment {

    private DialogCountryAdapter listAdapter;

    public interface CountryDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    CountryDialogListener mListener;

    public static MainCountryDialog newInstance(CountriesIntent ci, int position) {
        MainCountryDialog frag = new MainCountryDialog();
        Bundle args = new Bundle();
        args.putSerializable("CountriesList", ci);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CountryDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        CountriesIntent ci = (CountriesIntent) getArguments().getSerializable("CountriesList");
        int position = getArguments().getInt("position");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater lInflater = getActivity().getLayoutInflater();
        final View inflater = lInflater.inflate(R.layout.country_dialog, null);
        builder.setView(inflater)
                .setPositiveButton(R.string.dialog_choose_country, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(MainCountryDialog.this);
                    }
                })
                .setCancelable(true);

        fillList(inflater, ci, position);

        return builder.create();
    }

    private void fillList(final View inflator, final CountriesIntent countriesIntent, int position) {
        ListView listView;

        final List<CountryItem> countryListToShow = new ArrayList<>();

        listView = (ListView) inflator.findViewById(R.id.dialogListView);

        for (CountriesIntent.currencyItem ci : countriesIntent.getItems()) {
            CountryItem item = new CountryItem(getActivity(), ci.getCountryName(), ci.getCurrencyCode(), ci.getFlag());
            countryListToShow.add(item);
        }

        listAdapter = new DialogCountryAdapter(getActivity(), countryListToShow, position);
        listView.setAdapter(listAdapter);

        Thread thread = new Thread() {
            @Override
            public void run() {
                for (CountryItem ci : countryListToShow) {
                    ci.loadImage();
                }
            }
        };
        thread.start();
    }
}
