package com.example.saubhagyam.thetalklist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.saubhagyam.thetalklist.Adapter.AvailableTutorRecyclerAdapter;
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
import com.example.saubhagyam.thetalklist.Decorations.DividerItemDecoration;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Available_tutor extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ProgressDialog progressDialog;
    Dialog dialog;
    JSONArray array;
    AvailableTutorRecyclerAdapter availableTutorRecyclerAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences pref, preference;
    SharedPreferences.Editor ed, editor;
    JSONObject resultObj;
    int flag;
    Float credit;
    Button available_tutor_filter;
    String tutorName;
    EditText search_tutor_edittext;
    SearchView searchView;

    LinearLayout linearLayout;

    public Available_tutor() {
    }

    String desire_subject,desire_lang1,desire_lang2,desire_country,desire_state,desire_keyword,desired_gender;

    public Available_tutor(String desire_subject, String desire_lang1, String desire_lang2, String desire_country, String desire_state, String desire_keyword, String desired_gender) {
        this.desire_subject = desire_subject;
        this.desire_lang1 = desire_lang1;
        this.desire_lang2 = desire_lang2;
        this.desire_country = desire_country;
        this.desire_state = desire_state;
        this.desire_keyword = desire_keyword;
        this.desired_gender=desired_gender;

        tutorSearch( desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
    }

    public Available_tutor(int flag, Float credit, String tutorName) {
        this.flag = flag;
        this.credit = credit;
        this.tutorName = tutorName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onPause() {
        super.onPause();
//        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_available_tutor, container, false);


        SharedPreferences pref = getContext().getSharedPreferences("fromSignup", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorpref = pref.edit();

        if (pref.contains("fromSignup")) {
            TextView systemMessageStudent = (TextView) view.findViewById(R.id.systemMsgStudent);
            systemMessageStudent.setVisibility(View.VISIBLE);
            editorpref.clear().apply();
        }


        Button available_tutor_filter= (Button) view.findViewById(R.id.available_tutor_filter);

        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout_yellow));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));



        FragmentStack fragmentStack = FragmentStack.getInstance();
//        fragmentStack.setSize(1);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        available_tutor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
fragmentTransaction.replace(R.id.viewpager,new DesiredTutor()).commit();
            }
        });


        {
            String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("message count res ",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("unread_count") > 0)
                            ((TextView) getActivity().findViewById(R.id.bottombar_message_count)).setText(String.valueOf(object.getInt("unread_count")));
                        if (object.getInt("unread_count") == 0)
                            getActivity().findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(sr);
        }
        if (flag == 1) {

           /* if (){
                AlertDialog.Builder alertDialog= new AlertDialog.Builder(getContext());
                View view1 =inflater.inflate(R.layout.talknow_insufficient_layout,null);
                alertDialog.setView(view1);
                alertDialog.show();
            }*/
            View view1 = inflater.inflate(R.layout.talknow_confirmation_layout, null);
            final PopupWindow popupWindow = new PopupWindow(view1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(false);

            final View view2 = inflater.inflate(R.layout.talknow_insufficient_layout, null);
            final PopupWindow popupWindow1 = new PopupWindow(view2, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


            TextView confirmation_tutorCredits = (TextView) view1.findViewById(R.id.confirmation_tutorCredits);
            TextView confirmation_tutorName = (TextView) view1.findViewById(R.id.confirmation_tutorName);

            confirmation_tutorName.setText(tutorName);
            confirmation_tutorCredits.setText(new DecimalFormat("##.##").format(credit));
//            confirmation_tutorCredits.setText(String.valueOf(credit));



            Button yesbtn = (Button) view1.findViewById(R.id.yesbtn);
            final Button nobtn = (Button) view1.findViewById(R.id.nobtn);

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();
//                    if (Integer.parseInt(((TextView)getActivity().findViewById(R.id.num_credits)).getText().toString()) <= getContext().getSharedPreferences("videoCallTutorDetails",Context.MODE_PRIVATE).getFloat("hRate",0.0f)) {
                    if (getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getFloat("money",0.0f) <= getContext().getSharedPreferences("videoCallTutorDetails",Context.MODE_PRIVATE).getFloat("hRate",0.0f)) {

                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
                        popupWindow1.setFocusable(true);
                        popupWindow1.setOutsideTouchable(false);


                        Button okbtn = (Button) view2.findViewById(R.id.okbtn);

                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow1.dismiss();
//                                getActivity().onBackPressed();
                                TTL ttl = (TTL) getApplicationContext();
                                ttl.ExitBit = 2;
                              startActivity(new Intent(getApplicationContext(),new StripePaymentActivity().getClass()));
                            }
                        });
                    }else {
//                        Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("from", "availabletutor");
                        getContext().startActivity(i);
                        getActivity().onBackPressed();
                    }
                   /* Intent i = new Intent(getContext(), OutgoingCallActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(i);*/


                }
            });
            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTL ttl = (TTL) getApplicationContext();
                    ttl.ExitBit = 2;
                  /*  Bundle bundle=new Bundle();
                    bundle.putInt("flag",0);
                    Tablayout_with_viewpager tablayout_with_viewpager=new Tablayout_with_viewpager();
                    tablayout_with_viewpager.setArguments(bundle);
                    fragmentTransaction.replace(R.id.viewpager, tablayout_with_viewpager).commit();*/
                    popupWindow.dismiss();
                    getActivity().onBackPressed();
                }
            });


        }


       /* progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.........");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/

        linearLayout = (LinearLayout) view.findViewById(R.id.AvailableTutor_ProgressBar);
        available_tutor_filter= (Button) view.findViewById(R.id.available_tutor_filter);

       /* dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/




        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.availableTutorList);

     /*   search_tutor_edittext= (EditText) view.findViewById(R.id.search_tutor_edittext);
        search_tutor_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    Toast.makeText(getContext(), "Edit text focus gone..", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });*/
/*final int i=0;
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                Toast.makeText(getApplicationContext(), i+2, Toast.LENGTH_SHORT).show();
                return null;
            }


        }.execute();*/

        SharedPreferences prefDesired = getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE);
final SharedPreferences.Editor editor=prefDesired.edit();

        preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
        edi = preference.edit();
        searchView = (SearchView) view.findViewById(R.id.tutorsearch_searchView);
        if (prefDesired.contains("subject")){

            Log.e("desired pref ","found");

            desire_subject=prefDesired.getString("subject","");
            desire_lang1=prefDesired.getString("lang1","");
            desire_lang2=prefDesired.getString("lang2","");
            desired_gender=prefDesired.getString("gender","");
            desire_country=prefDesired.getString("country","");
            desire_state=prefDesired.getString("state","");
            desire_keyword=prefDesired.getString("keyword","");

            tutorSearch( desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
//            tutorSearch( desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
        }/*else {*/






            else if (preference.contains("query")) {

//                Toast.makeText(getApplicationContext(), "query text "+preference.getString("query",""), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), "query is here "+preference.getString("query",""), Toast.LENGTH_SHORT).show();
//            searchView.setQuery(preference.getString("query",""));
                new AvailableTutor(preference.getString("query", "")).execute();
                searchView.setQueryHint(preference.getString("query", ""));
            } else {
                searchView.setQueryHint("Ex. Statistics, USA");
                linearLayout.setVisibility(View.GONE);
            new AvailableTutor("").execute();
            }

//        }
        available_tutor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.viewpager, new DesiredTutor()).commit();
                edi.clear().apply();
            }
        });

        searchView.setQuery(preference.getString("query", ""),true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
              /*  //Log.e("onQueryTextChange", "called");
                editor.clear().apply();
                edi.putString("query", newText).apply();
//                tutorSearchFromSearchBar(newText);
                new AvailableTutor(newText).execute();*/
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

//                Toast.makeText(getContext(), "search button clicked..", Toast.LENGTH_SHORT).show();
                // Do your task here
                editor.clear().apply();
                edi.putString("query", query).apply();
//                tutorSearchFromSearchBar(query);
                linearLayout.setVisibility(View.VISIBLE);
//                new AvailableTutor(query).execute();
                tutorSearch( desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, query, desired_gender);

                return false;
            }

        });


        ImageView closeButton = (ImageView) this.searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manage this event.

                editor.clear().apply();
                edi.putString("query", "").apply();
//                new AvailableTutor("").execute();
//                Toast.makeText(getContext(), "Close button clcicked", Toast.LENGTH_SHORT).show();
                tutorSearch( desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, "", desired_gender);
            searchView.setQuery("",true);
            }
        });



       /* linearLayout.setVisibility(View.GONE);
        if (preference.contains("array")) {
            String array = preference.getString("array", "");
            try {
                new AvailableTutor().execute();
                JSONArray jsonArray = new JSONArray(array);
                setRecyclar(jsonArray);
//                linearLayout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            new AvailableTutor().execute();
//            linearLayout.setVisibility(View.GONE);
        }*/

       /* String URL = "https://www.thetalklist.com/api/tutorsearch";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    resultObj = response;

                    array = response.getJSONArray("tutors");

                    final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());




                    availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.scrollToPosition(preference.getInt("position",0));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(availableTutorRecyclerAdapter);
                    availableTutorRecyclerAdapter.notifyDataSetChanged();
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
                            availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(availableTutorRecyclerAdapter);
                            availableTutorRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

//                    progressDialog.dismiss();
//                    dialog.dismiss();
                    linearLayout.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                dialog.dismiss();
                linearLayout.setVisibility(View.GONE);
                if (error.getClass().equals(TimeoutError.class)) {
                    // Show timeout error message
                    Toast.makeText(getApplicationContext(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
                if (error.getClass().equals(ServerError.class)) {
                    // Show timeout error message
                    Toast.makeText(getApplicationContext(),
                            "We are sorry for our Absence..! Wait for a While... We are setting up for you",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);*/


        return view;
    }


    String desireSubject;
    String desireLang1;
    String desireLang2;
    String desireGender;
    String desireCountry;
    String desireState;
    String desireKeyword;
    @Override
    public void onResume() {

        super.onResume();
        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout_yellow));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));




    }

//    public void tutorSearchFromSearchBar(final String query){
//        String URL="https://www.thetalklist.com/api/tutorsearch";/*?keyword="+query*/;
//
//
//        StringRequest sr = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    resultObj = new JSONObject(response);
//                    Log.e("tutor search desired", resultObj.toString());
//                    if (resultObj.getInt("status")==1){
//
//                        linearLayout.setVisibility(View.GONE);
//                    }else {
//
//                        array = resultObj.getJSONArray("tutors");
//                        setRecyclar(array);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<>();
//                params.put("keyword",query);
//
//
//                return params;
//            }
//        };
//        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        Volley.newRequestQueue(getApplicationContext()).add(sr);
//    }






    public void tutorSearch(final String desire_subject, final String desire_lang1, final String desire_lang2, final String desire_country, final String desire_state, final String desire_keyword, final String desired_gender){
//"https://www.thetalklist.com/api/desired_tutor?subject=asdas&language1=english&language2=french&gender=female&country=India&state=state&keyword=saurabh";



        desireSubject   =desire_subject.replaceAll(" ","_");
        desireLang1     =desire_lang1.replaceAll(" ","_");
        desireLang2     =  desire_lang2.replaceAll(" ","_");
        desireGender     =desired_gender.replaceAll(" ","_");
        desireCountry    =desire_country.replaceAll(" ","_");
        desireState      =desire_state.replaceAll(" ","_");
        desireKeyword      =desire_keyword.replaceAll(" ","_");

//=asdas&language1=Select_Instruction_Language&gender=Male&country=Select_country&state=state&keyword=cultures


        if (desireSubject.equalsIgnoreCase("Select_Subject")) desireSubject="";
        if (desireLang1.equalsIgnoreCase("Select_Second_Language")) desireLang1="";
        if (desireGender.equalsIgnoreCase("Select_Gender")) desireGender="";
        if (desireCountry.equalsIgnoreCase("Select_country")) desireCountry="";
        if (desireState.equalsIgnoreCase("state")) desireState="";

        final String URL = "https://www.thetalklist.com/api/desired_tutor?subject="+desireSubject+"&language1="+desireLang1+/*"&language2="+desireLang2+*/"&gender="+desireGender+"&country="+desireCountry+"&state="+desireState+"&keyword="+desireKeyword+"&id="+getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getInt("id",0);
//        final String URL = "https://www.thetalklist.com/api/desired_tutor"/*?subject="+desire_subject+"&language1="+desire_lang1+"&language2="+desire_lang2+"&gender="+desired_gender+"&country="+desire_country+"&state="+desire_state+"&keyword="+desire_keyword*/;
        Log.e("desired tut search url",URL);
        RequestQueue queue11111 = Volley.newRequestQueue(getApplicationContext());

        StringRequest sr = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    resultObj = new JSONObject(response);
                    Log.e("tutor search desired", resultObj.toString());
                    if (resultObj.getInt("status")==1){
                        new AvailableTutor("").execute();
                        Toast.makeText(getContext(), "No Match Found", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.GONE);
                    }else {

                        array = resultObj.getJSONArray("tutors");
                        setRecyclar(array);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })/*{
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("subject", desireSubject);
                params.put("language1", desireLang1);
//                params.put("language2", desire_lang2);
                params.put("gender", desireGender);
                params.put("country", desireCountry);
                params.put("state", desireState);
                params.put("keyword", desireKeyword);
                params.put("id", String.valueOf(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id",0)));

                return params;
            }}*/;



        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue11111.add(sr);
    }

    String URL;
    SharedPreferences.Editor edi;

    public class AvailableTutor extends AsyncTask<Void, Void, Void> {

        String keyword_search;


        public AvailableTutor() {
        }


        AvailableTutor(String keyword_search) {
            this.keyword_search = keyword_search;

        }

        @Override
        protected Void doInBackground(Void... params) {

            String keyword=keyword_search.replace(" ","");
            Log.e("keyword",keyword_search);
            String URL = "https://www.thetalklist.com/api/tutorsearch?keyword="+keyword+"&id="+getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getInt("id",0);
            Log.e("Available tutor url",URL);
            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("tutor search",response);
                    try {

                        resultObj = new JSONObject(response);

                        if (resultObj.getInt("status")==0) {
                            if (resultObj.getString("tutors").equals("No Results? Check spelling and limit your criteria.")){
                                Toast.makeText(getContext(), "No Tutors found", Toast.LENGTH_SHORT).show();
                                new AvailableTutor("").execute();
                            }
                            else {
                                array = resultObj.getJSONArray("tutors");
                                setRecyclar(array);
                            }
                        }
//                        edi.clear().apply();

//                        edi.clear().apply();
                       /* if (!preference.contains("array")) {
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
//                progressDialog.dismiss();
//                dialog.dismiss();
                    linearLayout.setVisibility(View.GONE);
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getApplicationContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                    if (error.getClass().equals(ServerError.class)) {
                        // Show timeout error message
                        Toast.makeText(getApplicationContext(),
                                "We are sorry for our Absence..! Wait for a While... We are setting up for you",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("keyword", keyword_search);
                    return headers;
                }
            };



            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue1.add(sr);


            return null;
        }
    }

    public void setRecyclar(final JSONArray array) {
        swipeRefreshLayout.setRefreshing(true);
        Log.e("tutor search array",array.toString());
        if (array.equals("")) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.removeAllViews();
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

//        preference=getApplicationContext().getSharedPreferences("AvailableTutorPref",Context.MODE_PRIVATE);


            availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.scrollToPosition(preference.getInt("position", 0));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(availableTutorRecyclerAdapter);
            availableTutorRecyclerAdapter.notifyDataSetChanged();
            // Stop refresh animation
            swipeRefreshLayout.setRefreshing(false);
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
                    availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(availableTutorRecyclerAdapter);
                    availableTutorRecyclerAdapter.notifyDataSetChanged();
                }
            });
            linearLayout.setVisibility(View.GONE);
        }
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


                    try {
                        JSONObject object = (JSONObject) array.get(position);
                        int tutorId = object.getInt("uid");
                        URL = "https://www.thetalklist.com/api/favourite?student_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&tutor_id=" + tutorId;

                        new Favorite(URL).execute();

                        Parcelable recyclerViewState;
                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                        // Restore state
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//                        Toast.makeText(getContext(), "Swipe Right", Toast.LENGTH_SHORT).show();
                        availableTutorRecyclerAdapter.notifyDataSetChanged();
                        AvailableTutorRecyclerAdapter availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(position, getContext(), array, fragmentManager);
                        recyclerView.setAdapter(availableTutorRecyclerAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
       /* if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();

        }*/
    }


    public class Favorite extends AsyncTask<String, Void, JSONObject> {
        JSONObject jsonObject;
        String URL;
        Favorite(String URL){
            this.URL=URL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {

                        jsonObject = new JSONObject(response);

                        if (jsonObject.getInt("status") == 0) {
                            Toast.makeText(getContext(), "Added to favorites.", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(sr);
            return jsonObject;
        }


    }

}

