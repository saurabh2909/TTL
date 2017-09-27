package com.example.saubhagyam.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.saubhagyam.thetalklist.Adapter.VideoListAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Decorations.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;


public class VideoList extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FragmentManager fragmentManager;
    VideoListAdapter videoListAdapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    SearchView searchView;

    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String videoSearchUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final FragmentStack fragmentStack = FragmentStack.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.videoList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.videoListSwipeRefresh);
        final ImageView recordVid = (ImageView) view.findViewById(R.id.recordVid);
//        fragmentStack.add(new VideoList());
        linearLayout = (LinearLayout) view.findViewById(R.id.videoList_ProgressBar);
        recordVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoRecord videoRecord = new VideoRecord();
                fragmentStack.push(new VideoList());
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                fragmentTransaction.replace(R.id.viewpager, videoRecord).commit();
            }
        });

        queue1 = Volley.newRequestQueue(getContext());

        searchView = (SearchView) view.findViewById(R.id.videosearch_searchView);
        searchView.setQueryHint("1 video-1 topic-1 minute");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

             /*   String newQuery=newText.replace(" ","%20");
                videoSearchUrl="https://www.thetalklist.com/api/videosearch?keyword="+newQuery;
                new VideoSearch(newText).execute();*/
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.threedotprogressbar);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
//                new VideoSearch(query).execute();

                String newQuery=newText.replace(" ","%20");
                final String URL = "https://www.thetalklist.com/api/videosearch?keyword="+newQuery;

                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("video list api URL",URL);
                        Log.e("video list api response","video search  "+ response.toString());

                        try {
                            jsonArray = response.getJSONArray("videos");
                            Log.e("video li response array", jsonArray.toString());
                            setRecycler(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        linearLayout.setVisibility(View.GONE);
                    }
                }
                );
                getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(getRequest);
                dialog.dismiss();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                /*String newQuery=query.replace(" ","%20");
                videoSearchUrl="https://www.thetalklist.com/api/videosearch?keyword="+newQuery;*/
                 dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.threedotprogressbar);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
//                new VideoSearch(query).execute();

                String newQuery=query.replace(" ","%20");
                final String URL = "https://www.thetalklist.com/api/videosearch?keyword="+newQuery;

                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("video list api URL",URL);
                        Log.e("video list api response","video search  "+ response.toString());

                        try {
                            jsonArray = response.getJSONArray("videos");
                            Log.e("video li response array", jsonArray.toString());
                            setRecycler(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        linearLayout.setVisibility(View.GONE);
                    }
                }
                );
                getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(getRequest);
                dialog.dismiss();
                return false;
            }

        });


        preferences = getApplicationContext().getSharedPreferences("videoListResponse", Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (preferences.contains("jsonArray")) {

            try {
                new VideoPlayService().execute();
//                Toast.makeText(getContext(), String.valueOf(preferences.getInt("position",0)), Toast.LENGTH_SHORT).show();


                jsonArray = new JSONArray(preferences.getString("jsonArray", ""));
                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                videoListAdapter = new VideoListAdapter(getContext(), fragmentManager, jsonArray);
                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.scrollToPosition(preferences.getInt("position", 0));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();


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
                        recyclerView.scrollToPosition(0);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(videoListAdapter);
                        videoListAdapter.notifyDataSetChanged();
                    }
                });
                linearLayout.setVisibility(View.GONE);
               /* recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getContext(), "recyclar view touch listener oncreate", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                    }
                }));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            new VideoPlayService().execute();
            linearLayout.setVisibility(View.GONE);
        }
        preferences1 = getContext().getSharedPreferences("videoPlaySelected", Context.MODE_PRIVATE);
        editor1 = preferences1.edit();
        return view;
    }

    SharedPreferences preferences1;
    SharedPreferences.Editor editor1;
    JSONArray jsonArray;
    RequestQueue queue1;

    public class VideoPlayService extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/videolist";

            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("video list api response", response.toString());

                    try {
                        jsonArray = response.getJSONArray("videos");
                        Log.e("video li response array", jsonArray.toString());

setRecycler(jsonArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    linearLayout.setVisibility(View.GONE);
                }
            }
            );
            getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue1.add(getRequest);
            return null;
        }
    }

    public void setRecycler( JSONArray jsonArray){
//        editor1.putString("responseArray", jsonArray.toString()).apply();

       /* if (!preferences.contains("jsonArray")) {
            editor.putString("jsonArray", jsonArray.toString()).apply();*/


          /*  try {
                new VideoPlayService().execute();
                jsonArray = new JSONArray(preferences.getString("jsonArray", ""));*/

                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                videoListAdapter = new VideoListAdapter(getContext(), fragmentManager, jsonArray);
                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.scrollToPosition(preferences.getInt("position", 0));

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();


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

                        recyclerView.scrollToPosition(0);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(videoListAdapter);
                        videoListAdapter.notifyDataSetChanged();
                    }
                });
                linearLayout.setVisibility(View.GONE);
                            /*recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Toast.makeText(getContext(), "recyclar view touch listener", Toast.LENGTH_SHORT).show();
                                    editor.putInt("position",position).apply();
                                }

                                @Override
                                public void onLongClick(View view, int position) {


                                }
                            }));*/
           /* } catch (JSONException e) {
                e.printStackTrace();
            }*/
      /*  } else {
            editor.putString("jsonArray", jsonArray.toString()).apply();
            linearLayout.setVisibility(View.GONE);
        }*/
    }

    public class VideoSearch extends AsyncTask<Void, Void, Void> {

        String query;

        public VideoSearch() {
        }

        public VideoSearch(String query) {
            this.query = query;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {




            String newQuery=query.replace("","%20");
            String URL = "https://www.thetalklist.com/api/videosearch?keyword="+newQuery;

            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("video list api URL",videoSearchUrl);
                    Log.e("video list api response","video search  "+ response.toString());

                    try {
                        jsonArray = response.getJSONArray("videos");
                        Log.e("video li response array", jsonArray.toString());
                        setRecycler(jsonArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    linearLayout.setVisibility(View.GONE);
                }
            }
            );
            getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getApplicationContext()).add(getRequest);
            return null;
        }
    }

}
