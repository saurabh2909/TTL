package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.saubhagyam.thetalklist.R;

/**
 * Created by Saubhagyam on 02/05/2017.
 */

public class Session_Search_images_results_grid_adapter extends BaseAdapter {

    final Context context;

    public Session_Search_images_results_grid_adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
        LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.session_search_image_results_grid_layout,null);



        return convertView;
    }
}
