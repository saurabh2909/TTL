package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.saubhagyam.thetalklist.Bean.CountryModel;
import com.example.saubhagyam.thetalklist.R;

import java.util.List;

/**
 * Created by Saubhagyam on 06/07/2017.
 */

public class CountrySpinnerAdapter extends BaseAdapter {

    List<CountryModel> coun;
    Context context;
    LayoutInflater inflter;

    public CountrySpinnerAdapter(Context context, List<CountryModel> coun){
        this.context=context;
        this.coun=coun;
        inflter = (LayoutInflater.from(context));

    }


    @Override
    public int getCount() {
        return coun.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.custom_spinner_textview, null);


        CountryModel model=coun.get(position);
        /*TextView country_name= (TextView) convertView.findViewById(R.id.country_name);
        TextView country_code= (TextView) convertView.findViewById(R.id.country_code);
        country_name.setText(model.getCountryName());
        country_code.setText(model.getCountryCode());*/



        return convertView;
    }
}
