package com.earaujo.app.moneyexchangerate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.text.Html.fromHtml;

/**
 * Created by Eduardo on 04/07/2016.
 */
public class ManageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CountryItem> items = new ArrayList<CountryItem>();
    private List<CountryItem> exclusionCountryList;
    private Context context;

    public ManageAdapter(Context context, List items, List excludeds) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.exclusionCountryList = excludeds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CountryItem getItem(int position) {
        if (position>=items.size())
            return null;
        return items.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public CountryItem getCountryFromId(int id) {
        return items.get(id);
    }

    public void updateItem(List items, List excludeds) {
        this.items = items;
        this.exclusionCountryList = excludeds;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final CountryItem ci = items.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvLanguage);
            holder.image = (ImageView) convertView.findViewById(R.id.imgLanguage);
            holder.btn = (CheckBox) convertView.findViewById(R.id.button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(fromHtml("<b><font color=black>" + ci.getCurrencyCode() + "</font></b> " +  ci.getCountryName()));

        if (ci.getImage() != null) {
            holder.image.setImageBitmap(ci.getImage());
        } else {
            holder.image.setImageResource(R.drawable.no_flag);
        }

        holder.btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    exclusionCountryList = removeItem(exclusionCountryList,ci);
                }
                else {
                    exclusionCountryList = addItem(exclusionCountryList,ci);
                }
                FileOperations.writeExcludedList(context, exclusionCountryList);
            }
        });

        if (containItem(exclusionCountryList, ci)) {
            holder.btn.setChecked(false);//.setImageResource(R.drawable.cb_unchecked);
        }
        else {
            holder.btn.setChecked(true);//holder.btn.setImageResource(R.drawable.cb_checked);
        }

        return convertView;
    }

    private List<CountryItem> removeItem(List<CountryItem> theList, CountryItem itemToExclude) {
        List<CountryItem>tempList = new ArrayList<>();

        for (CountryItem ci : theList) {
            if (!ci.getCurrencyCode().equals(itemToExclude.getCurrencyCode())) {
                tempList.add(ci);
            }
        }
        return tempList;
    }

    private List<CountryItem> addItem(List<CountryItem> theList, CountryItem itemToAdd) {
        List<CountryItem>tempList = theList;

        for (CountryItem ci : theList) {
            if (ci.getCurrencyCode().equals(itemToAdd.getCurrencyCode())) {
                return tempList;
            }
        }
        tempList.add(itemToAdd);

        return tempList;
    }

    private boolean containItem(List<CountryItem> theList, CountryItem itemToCheck) {
        for (CountryItem ci: theList) {
            if (ci.getCurrencyCode().equals(itemToCheck.getCurrencyCode())) {
                return true;
            }
        }
        return false;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
        CheckBox btn;
    }
}
