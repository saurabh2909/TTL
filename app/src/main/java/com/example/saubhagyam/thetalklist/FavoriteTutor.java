package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Adapter.FaoriteAdapter;
import com.example.saubhagyam.thetalklist.Decorations.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 07/07/2017.
 */

public class FavoriteTutor extends Fragment {
    View view;
    RecyclerView recyclerView;
    JSONObject resultObj;
    JSONArray array;
    SwipeRefreshLayout swipeRefreshLayout;
    FaoriteAdapter FaoriteAdapter;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref, preference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.favorite_tutor_layout, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_favoritetutor);
        recyclerView = (RecyclerView) view.findViewById(R.id.favoriteTutorList);

        recyclerView.removeAllViews();
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        FavoriteTutor1();

        /*preference = getApplicationContext().getSharedPreferences("FavoriteTutorPref", Context.MODE_PRIVATE);
        if (preference.contains("array")) {
            String array = preference.getString("array", "");
            try {
                 FavoriteTutor1();
                JSONArray jsonArray = new JSONArray(array);
                setRecyclar(jsonArray);
//                linearLayout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
             FavoriteTutor1();
//            linearLayout.setVisibility(View.GONE);
        }*/
      /*  new AvailableTutor().execute();
        JSONArray jsonArray = new JSONArray(array);
        setRecyclar(jsonArray);*/
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void FavoriteTutor1() {


        String URL = "https://www.thetalklist.com/api/list_favourite_tutor?student_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("favorite list res ", response.toString());

                resultObj = response;
                try {
//                    Log.e("favorite array length", String.valueOf(array.length()));
                    if (resultObj.getInt("status")==0) {
                        array = response.getJSONArray("result");
                        setRecyclar(array);
                    }else {
                        swipeRefreshLayout.setVisibility(View.GONE);
                    }


                 /*   SharedPreferences.Editor edi = preference.edit();
                    if (!preference.contains("array")) {
                        setRecyclar(array);
                        edi.putString("array", array.toString()).apply();
//                            linearLayout.setVisibility(View.GONE);
                    } else {
                        setRecyclar(array);
                        edi.putString("array", array.toString()).apply();
//                            linearLayout.setVisibility(View.GONE);
                    }
*/
//                    progressDialog.dismiss();
//                    dialog.dismiss();
//                        linearLayout.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 1000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue1.add(getRequest);

    }


    public void setRecyclar(final JSONArray array) {
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

//        preference=getApplicationContext().getSharedPreferences("AvailableTutorPref",Context.MODE_PRIVATE);


        FaoriteAdapter = new FaoriteAdapter(getContext(), array, fragmentManager);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.scrollToPosition(preference.getInt("position", 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(FaoriteAdapter);
        FaoriteAdapter.notifyDataSetChanged();
        // Stop refresh animation
        initSwipe();
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }

            void refreshItems() {
                // Load items
                // ...

                // Load complete
                swipeRefreshLayout.setRefreshing(false);
                // Update the adapter and notify data set changed
                // ...
                // Stop refresh animation
                FaoriteAdapter = new FaoriteAdapter(getContext(), array, fragmentManager);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(FaoriteAdapter);
                FaoriteAdapter.notifyDataSetChanged();
            }
        });
//        linearLayout.setVisibility(View.GONE);
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {

                    if (array.length() > 0) {
                        try {
                            JSONObject object = (JSONObject) array.get(position);
                            int tutorId = object.getInt("uid");
//                        URL = "https://www.thetalklist.com/api/favourite?student_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&tutor_id=" + tutorId;

//                        new Available_tutor.Favorite(URL).execute();

                            String URL = "https://www.thetalklist.com/api/favourite?student_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&tutor_id=" + tutorId;
                            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    resultObj = response;

                                    Log.e("favorite response", response.toString());
                                    try {
                                        if (response.getInt("status") == 0) {
                                            FavoriteTutor1();
                                            fragmentTransaction.replace(R.id.viewpager, new FavoriteTutor()).commit();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }
                            );
//                            getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            Volley.newRequestQueue(getContext()).add(getRequest);


                           /* Parcelable recyclerViewState;
                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                            // Restore state
                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                            FaoriteAdapter.notifyDataSetChanged();
                            FaoriteAdapter FaoriteAdapter = new FaoriteAdapter(position, getContext(), array, fragmentManager);
                            recyclerView.setAdapter(FaoriteAdapter);*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }




}
