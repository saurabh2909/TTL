package com.example.saubhagyam.thetalklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.saubhagyam.thetalklist.Adapter.Session_Search_images_results_grid_adapter;


public class SessionSearchImagaesResults extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_session_search_imagaes_results, container, false);


        GridView gridView= (GridView) view.findViewById(R.id.session_search_grid_view);


        Session_Search_images_results_grid_adapter session_search_images_results_grid_adapter=new Session_Search_images_results_grid_adapter(getContext());
        gridView.setAdapter(session_search_images_results_grid_adapter);


        return view;
    }

}
