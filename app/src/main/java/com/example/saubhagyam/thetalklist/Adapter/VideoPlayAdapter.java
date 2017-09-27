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

public class VideoPlayAdapter extends BaseAdapter {

    final Context context;

    public VideoPlayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.video_list_layout, parent, false);
        }
        return convertView;

    }
}
