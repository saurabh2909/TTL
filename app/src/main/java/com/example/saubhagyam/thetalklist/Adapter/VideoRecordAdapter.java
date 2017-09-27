package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.saubhagyam.thetalklist.R;

/**
 * Created by Saubhagyam on 18/04/2017.
 */

public class VideoRecordAdapter extends BaseAdapter {

    final Context context;

    public VideoRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
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

        convertView = LayoutInflater.from(context).
                inflate(R.layout.thumblines_layout, parent, false);

        return convertView;
    }
}
