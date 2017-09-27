package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Saubhagyam on 23/08/2017.
 */

public class Spinner_adapter extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    /*Typeface font = Typeface.createFromAsset(getContext().getAssets(),
            "fonts/Blambot.otf");*/

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public Spinner_adapter(Context context, int resource, String[] items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
//        view.setTypeface(font);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
//        view.setTypeface(font);
        return view;
    }
}