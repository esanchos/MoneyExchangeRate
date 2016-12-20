package com.earaujo.app.moneyexchangerate;

import android.content.Context;
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
 * Created by Eduardo on 02/07/2016.
 */
public class SpinnerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CountryItem>items = new ArrayList<CountryItem>();

    public SpinnerAdapter(Context context, List items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public CountryItem getItem(int position) {
        if (position>=items.size())
            return null;
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void removeItem(CountryItem ci) {
        items.remove(ci);
    }

    public CountryItem getCountryFromId(int id) {
        if ( (items.size()<=id) || (id<0) )
            return null;

        return items.get(id);
    }

    public List<CountryItem> getAll() {
        return items;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final CountryItem ci = items.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_main, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvLanguage);
            holder.image = (ImageView) convertView.findViewById(R.id.imgLanguage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(fromHtml("<b><font color=black>" + ci.getCurrencyCode() + "</font></b> " + ci.getCountryName()));
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