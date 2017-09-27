package com.example.saubhagyam.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Adapter.History_list_adapter;
import com.example.saubhagyam.thetalklist.Pagination.Paginator;
import com.srx.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class History extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        final PullToLoadView listView = (PullToLoadView) view.findViewById(R.id.history_list_layout);

       /* listView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);*/


new Paginator(getContext(),listView);
       /* LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setFocusable(false);*/
//        String URL = "https://www.thetalklist.com/api/history?id=" + 17600;
        /*RequestQueue queue = Volley.newRequestQueue(getContext());
        String URL = "https://www.thetalklist.com/api/history?id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id",0);
        Log.e("history URL",URL);


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("history response",response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getInt("status") == 0) {
                        JSONArray jsonArray = object.getJSONArray("history");
                        History_list_adapter history_list_adapter = new History_list_adapter(getContext(),jsonArray);
                        listView.setAdapter(history_list_adapter);
                        dialog.dismiss();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volly error",error.toString());
            }
        });

        sr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(sr);
*/



        return view;
    }


}
