package com.earaujo.app.moneyexchangerate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.text.Html.fromHtml;

/**
 * Created by Eduardo on 02/01/2017.
 */

public class DialogCountryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CountryItem> items = new ArrayList<>();
    private Context context;
    private int callbackValue;

    public interface CountryItemListener {
        void onCountryClick(int pos, int callbackValue);
    }

    CountryItemListener mListener;

    public DialogCountryAdapter(Context context, List items, int callbackValue) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.context = context;
        this.callbackValue = callbackValue;

        try {
            this.mListener = (CountryItemListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }

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

    public void updateItem(List items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final CountryItem ci = items.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.country_dialog_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvLanguage);
            holder.image = (ImageView) convertView.findViewById(R.id.imgLanguage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NUNES","DialogCountryAdapter: " + ci.getCurrencyCode());
                mListener.onCountryClick(position, callbackValue);
            }
        });

        holder.name.setText(fromHtml("<b><font color=black>" + ci.getCurrencyCode() + "</font></b> " +  ci.getCountryName()));

        if (ci.getImage() != null) {
            holder.image.setImageBitmap(ci.getImage());
        } else {
            holder.image.setImageResource(R.drawable.no_flag);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
    }
}
