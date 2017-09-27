package com.example.saubhagyam.thetalklist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.Adapter.Biography_subject_adapter;
import com.example.saubhagyam.thetalklist.Adapter.Biography_videoThumb_adapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Biography extends Fragment {

    int liststatus = 0;
    ExpandableLinearLayout /*biography_layout,*/ /*video_layout,*/ /*ratings_layout,*/ biography_subject_layout;
    ImageView biography_btn, video_btn, ratings_btn, biography_subject_btn, TutorImgBiography; /*videocallButton1biography; msgButton1biography*/
    LinearLayout biography_11, video_11, ratings_11, controlLayout, biography_subject_11;
    TextView biographyFirstName, biographyTutorListType, biographyCPS;
    ImageView biography_rate_edit;
    TextView biography_languages;
    ProgressBar biography_languages_progress, biography_videoview_progress;
    SharedPreferences preferences;


    LinearLayout review_root_biography;


    View view;
    Biography.subjectHandler subHandler;
    Biography.VideoUrlHandler videoUrlHandler;

    int review_bit;

    int id;
    RequestQueue queue;
    RequestQueue queue1;
    ListView biography_subject_recyclerview;
    VideoView biography_videoview;
    ImageView controlBtn;
    RecyclerView biography_video_thum_recycle;
    int edit_bit;
    LinearLayout biography_biographyfrag_layout;
    RatingBar ratingBarbiography;

    Button biography_edit;
    TextView biography_personal, biography_educational, biography_professional, biography_rate_textview;
    EditText biography_personal_edit, biography_educational_edit, biography_professional_edit, biography_rate_edittext;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the studentlayout for this fragment
        String LINK = "http://techslides.com/demos/sample-videos/small.mp4";
        view = inflater.inflate(R.layout.fragment_biography, container, false);

        preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
//        biography_layout = (ExpandableLinearLayout) view.findViewById(R.id.biography_layout);
        biography_btn = (ImageView) view.findViewById(R.id.biography_btn);
//        video_layout = (ExpandableLinearLayout) view.findViewById(R.id.video_layout);
        video_btn = (ImageView) view.findViewById(R.id.video_btn);
//        ratings_layout = (ExpandableLinearLayout) view.findViewById(R.id.ratings_layout);
        biography_subject_layout = (ExpandableLinearLayout) view.findViewById(R.id.biography_subject_layout);
        ratings_btn = (ImageView) view.findViewById(R.id.ratings_btn);
        TutorImgBiography = (ImageView) view.findViewById(R.id.TutorImgBiography);
        biography_subject_btn = (ImageView) view.findViewById(R.id.biography_subject_btn);
//        videocallButton1biography = (ImageView) view.findViewById(R.id.videocallButton1biography);
//        msgButton1biography = (ImageView) view.findViewById(R.id.msgButton1biography);
//        TutorTypeImgbiography = (ImageView) view.findViewById(R.id.TutorTypeImgbiography);
        biographyFirstName = (TextView) view.findViewById(R.id.biographyFirstName);
        biography_rate_edit = (ImageView) view.findViewById(R.id.biography_rate_edit);
        biography_rate_textview = (TextView) view.findViewById(R.id.biography_rate_textview);

        ratingBarbiography= (RatingBar) view.findViewById(R.id.ratingBarbiography);

        biography_video_thum_recycle= (RecyclerView) view.findViewById(R.id.biography_video_thum_recycle);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        biography_video_thum_recycle.setLayoutManager(layoutManager);

        new VideoUrlHandler().execute();
        {
            String url="http://www.thetalklist.com/api/biography_video?uid="+preferences.getInt("id",0);


            StringRequest sr=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
Log.e("video response thumb",response);

                    try {
                        JSONObject resultObj=new JSONObject(response);

                        if (resultObj.getInt("status")==0){
                            JSONArray biography_video_ary=resultObj.getJSONArray("biography_video");
                            biography_video_thum_recycle.setAdapter(new Biography_videoThumb_adapter(getContext(),biography_video_ary,biography_videoview,biography_videoview_progress,controlLayout));

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
            Volley.newRequestQueue(getContext()).add(sr);
        }



        biography_professional = (TextView) view.findViewById(R.id.biography_professional);
        biography_personal = (TextView) view.findViewById(R.id.biography_personal);
        biography_educational = (TextView) view.findViewById(R.id.biography_educational);

        review_root_biography= (LinearLayout) view.findViewById(R.id.review_root_biography);

        biography_professional_edit = (EditText) view.findViewById(R.id.biography_professional_edit);
        biography_personal_edit = (EditText) view.findViewById(R.id.biography_personal_edit);
        biography_educational_edit = (EditText) view.findViewById(R.id.biography_educational_edit);
        biography_rate_edittext = (EditText) view.findViewById(R.id.biography_rate_edittext);
        queue = Volley.newRequestQueue(getActivity());
//        biographyTutorListType = (TextView) view.findViewById(R.id.biographyTutorListType);
//        biographyCPS = (TextView) view.findViewById(R.id.biographyCPS);
//        video_controller_view= view.findViewById(R.id.biography_mediaController);
//        controlBtn = (ImageView) view.findViewById(R.id.controlBtn);
        controlLayout = (LinearLayout) view.findViewById(R.id.controlLayout);


        biographyFirstName.setText(preferences.getString("usernm", ""));
        if (preferences.getFloat("hRate", 0.0f) != 0.0) {

            biography_rate_textview.setText(String.valueOf(preferences.getFloat("hRate", 0.0f) / 25.0f));
            biography_rate_edittext.setText(String.valueOf(preferences.getFloat("hRate", 0.0f) / 25.0f));
        } else {
            biography_rate_textview.setText("0");
            biography_rate_edittext.setText("0");
        }


//        Toast.makeText(getContext(), "average ratings: "+preferences.getFloat("avgRate",0.0F), Toast.LENGTH_SHORT).show();
        if (preferences.getFloat("avgRate",0.0f)!=0.0f)
            ratingBarbiography.setRating(preferences.getFloat("avgRate",0.0f));

//        biographyCPS.setText(preferences.getString("credit_balance", ""));


      /*  if (roleId == 1) {
            TutorTypeImgbiography.setBackgroundResource(R.drawable.red_dot);
            biographyTutorListType.setText("Bronze");
        }
        if (roleId == 2) {
            TutorTypeImgbiography.setBackgroundResource(R.drawable.silver_dot);
            biographyTutorListType.setText("Silver");
        }
        if (roleId == 3) {
            TutorTypeImgbiography.setBackgroundResource(R.drawable.gold_dot);
            biographyTutorListType.setText("Gold");
        }*/


        biography_edit = (Button) view.findViewById(R.id.biography_edit);
//        biography_save = (Button) view.findViewById(R.id.biography_save);

   /*     biography_subject_recyclerview= (ListView) view.findViewById(R.id.biography_subject_languages_recyclerview);


biography_subject_layout.setInRecyclerView(true);

        Biography_subject_adapter subjectAdapter=new Biography_subject_adapter(getContext(),s);
        biography_subject_recyclerview.setAdapter(subjectAdapter);*/


//View v=inflater.inflate(R.layout.biography_subject_layout,null);

    /*    LinearLayout lin= (LinearLayout) view.findViewById(R.id.biography_subject_languages_recyclerview);
for (int i=0;i<5;i++){

    View vi=inflater.inflate(R.layout.biography_subject_recycler_child_layout,null);
    TextView name= (TextView) vi.findViewById(R.id.biography_subject_textview);
    CheckBox check= (CheckBox ) vi.findViewById(R.id.biography_subject_checkbox);
    name.setText(s[i]);
    lin.addView(vi);
}
*/

//        View subject_layout=getLayoutInflater().inflate(R.layout.biography_subject_layout)

        ratings_11= (LinearLayout) view.findViewById(R.id.ratings_11);
        final int height1=review_root_biography.getHeight();
        ratings_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review_root_biography.getVisibility() == View.VISIBLE) {
                    review_root_biography.animate().translationY(0);
                    review_root_biography.setVisibility(View.GONE);
                    ratings_btn.setImageResource(R.drawable.side_aerrow);

                } else {
                    review_root_biography.animate().translationY(height1);
                    review_root_biography.setVisibility(View.VISIBLE);
                    ratings_btn.setImageResource(R.drawable.down_aerrow);
                }


            }
        });
        {
            String URL = "http://www.thetalklist.com/api/reviews?uid="+ + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
            Log.e("review url", URL);
//            new myLoginData().execute();


            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("review response",response);
                        JSONObject res=new JSONObject(response);
                        if (res.getInt("status")==0){
                            JSONArray reviewAry=res.getJSONArray("review");

                            ((TextView)view.findViewById(R.id.biography_totalreview)).setText((String.valueOf(reviewAry.length())));

                            for (int i=0;i<reviewAry.length();i++)
                            {

                                JSONObject obj= (JSONObject) reviewAry.get(i);

                                View convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_tutor_expanded_ratings_feedback, null);

                               ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView9);
                                Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" +obj.getString("pic"))
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView);
                                TextView review_name= (TextView) convertView.findViewById(R.id.review_name);
                                review_name.setText(obj.getString("firstName"));
                                TextView review_rate= (TextView) convertView.findViewById(R.id.review_rate);
                                review_rate.setText(obj.getString("msg"));

                                RatingBar ratingBar1= (RatingBar) convertView.findViewById(R.id.ratingBar1);

                                ratingBar1.setRating(Float.parseFloat(obj.getString("clearReception")));

                                String date=obj.getString("create_at");
                                Date date_txt=null;
                                String[] months={"Jan","Feb","Mar","April","may","June","July","Aug","Sep","Oct","Nov","Dec"};

                                if (date!=null){
                                    date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(date);
                                    int month= Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date_txt));
                                    int day= Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date_txt));
                                    int year= Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(date_txt));

                                    TextView biography_date_review= (TextView) convertView.findViewById(R.id.biography_date_review);

                                    biography_date_review.setText(String.valueOf(day)+"-"+months[month-1]+"-"+String.valueOf(year));
                                }

                                review_root_biography.addView(convertView);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(getContext()).add(sr);
                }

        biography_rate_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_bit == 0) {
                    edit_bit = 1;
                    biography_rate_edit.setImageDrawable(getResources().getDrawable(R.drawable.save));
                    biography_rate_textview.setVisibility(View.GONE);
                    biography_rate_edittext.setVisibility(View.VISIBLE);
                } else {
                    edit_bit = 0;
                    biography_rate_edit.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                    biography_rate_textview.setText(biography_rate_edittext.getText().toString());
                    biography_rate_textview.setVisibility(View.VISIBLE);
                    biography_rate_edittext.setVisibility(View.GONE);

                    if (biography_rate_edittext.getText().toString().equals(null))
                        biography_rate_edittext.setText("00");

                    String Url = "https://www.thetalklist.com/api/minute_rate?uid=" + preferences.getInt("id", 0) + "&rate=" + biography_rate_edittext.getText().toString();
                    StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("rate response ", response);

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getInt("status") == 0) {
                                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                            biography_rate_textview.setText(biography_rate_edittext.getText().toString());
                                        } else {
                                            Toast.makeText(getContext(), "Something went wrong... Please try again..!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "rate api error", Toast.LENGTH_SHORT).show();

                                }
                            });

                    Volley.newRequestQueue(getContext()).add(strRequest);
                }
            }
        });

        biography_biographyfrag_layout = (LinearLayout) view.findViewById(R.id.biography_biographyfrag_layout);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) biography_biographyfrag_layout.getLayoutParams();
//        biography_layout.setLayoutParams(new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height));

        biography_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_bit == 0) {
                    biography_edit.setText("Save");
                    edit_bit = 1;
                    biography_personal.setVisibility(View.GONE);
                    biography_educational.setVisibility(View.GONE);
                    biography_professional.setVisibility(View.GONE);


                    biography_personal_edit.setVisibility(View.VISIBLE);
                    biography_educational_edit.setVisibility(View.VISIBLE);
                    biography_professional_edit.setVisibility(View.VISIBLE);

                } else /*(edit_bit==1)*/ {
                    edit_bit = 0;
                    biography_edit.setText("Edit");
                    biography_personal.setVisibility(View.VISIBLE);
                    biography_educational.setVisibility(View.VISIBLE);
                    biography_professional.setVisibility(View.VISIBLE);


                    biography_personal_edit.setVisibility(View.GONE);
                    biography_educational_edit.setVisibility(View.GONE);
                    biography_professional_edit.setVisibility(View.GONE);


                    Log.e("id", String.valueOf(preferences.getInt("id", 0)));
                    Log.e("academic", biography_educational_edit.getText().toString());
                    Log.e("professional", biography_professional_edit.getText().toString());
                    Log.e("personal", biography_personal_edit.getText().toString());

                    final String personal_txt = biography_personal_edit.getText().toString();

                    final String educational_txt = biography_educational_edit.getText().toString();
                    final String professional_txt = biography_professional_edit.getText().toString();


                    RequestQueue queue = Volley.newRequestQueue(getContext());


//String URL="https://www.thetalklist.com/api/edit_biogrpy"/*?id="+preferences.getInt("id",0)+"&academic="+personal_txt+"&professional="+professional_txt+"&personal="+personal_txt*/;
//                    String URL = "https://www.thetalklist.com/api/edit_biogrpy?id=" + preferences.getInt("id", 0) + "&academic=" + educational_txt.replace("\n", "") + "&professional=" + professional_txt.replace("\n", "") + "&personal=" + personal_txt.replace("\n", "");
                    String URL = "https://www.thetalklist.com/api/edit_biogrpy"/*?id=" + preferences.getInt("id", 0) + "&academic=" + educational_txt.replace("\n", "") + "&professional=" + professional_txt.replace("\n", "") + "&personal=" + personal_txt.replace("\n", "")*/;
                    URI uri = null;
                    try {
                        uri = new URI(URL.replaceAll(" ", "%20"));
                        StringRequest strRequest = new StringRequest(Request.Method.POST, uri.toString(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
//                                        Toast.makeText(getContext(), "status " + response, Toast.LENGTH_SHORT).show();
                                        biography_personal.setText(biography_personal_edit.getText().toString());
                                        biography_educational.setText(biography_educational_edit.getText().toString());
                                        biography_professional.setText(biography_professional_edit.getText().toString());
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();

                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", String.valueOf(preferences.getInt("id", 0)));
                                params.put("academic", educational_txt);
                                params.put("professional", professional_txt);
                                params.put("personal", personal_txt);
                                return params;
                            }
                        };

                        queue.add(strRequest);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        /*biography_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                biography_personal.setVisibility(View.VISIBLE);
                biography_educational.setVisibility(View.VISIBLE);
                biography_professional.setVisibility(View.VISIBLE);


                biography_personal_edit.setVisibility(View.GONE);
                biography_educational_edit.setVisibility(View.GONE);
                biography_professional_edit.setVisibility(View.GONE);

                Log.e("id",           String.valueOf(preferences.getInt("id",0)));
                Log.e("academic",     biography_educational_edit.getText().toString());
                Log.e("professional", biography_professional_edit.getText().toString());
                Log.e("personal",     biography_personal_edit.getText().toString());

                final String personal_txt=  biography_professional_edit.getText().toString();

                final String educational_txt=biography_educational_edit.getText().toString();
                final String professional_txt=biography_personal_edit.getText().toString();




                RequestQueue queue = Volley.newRequestQueue(getContext());








//String URL="https://www.thetalklist.com/api/edit_biogrpy"*//*?id="+preferences.getInt("id",0)+"&academic="+personal_txt+"&professional="+professional_txt+"&personal="+personal_txt*//*;
String URL="https://www.thetalklist.com/api/edit_biogrpy?id="+preferences.getInt("id",0)+"&academic="+personal_txt+"&professional="+professional_txt+"&personal="+personal_txt;
                URI uri = null;
                try {
                    uri = new URI(URL.replaceAll(" ", "%20"));
                    StringRequest strRequest = new StringRequest(Request.Method.POST, uri.toString(),
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {
                                    Toast.makeText(getContext(), "status "+response, Toast.LENGTH_SHORT).show();
                                    biography_personal.setText(biography_personal_edit.getText().toString());
                                    biography_educational.setText(biography_educational_edit.getText().toString());
                                    biography_professional.setText(biography_professional_edit.getText().toString());
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error)
                                { Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();

                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", String.valueOf(preferences.getInt("id",0)));
                            params.put("academic", educational_txt);
                            params.put("professional", professional_txt);
                            params.put("personal", personal_txt);
                            return params;
                        }
                    };

                    queue.add(strRequest);
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
Log.e("chnage bio  ",URL);


              *//*  Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(preferences.getInt("id",0)));
                params.put("academic", educational_txt);
                params.put("professional", professional_txt);
                params.put("personal", personal_txt);



                JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(), "response -->> " + response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "error -->> " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                JsonObjectRequest.setRetryPolicy(new

                        DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                Volley.newRequestQueue(getContext()).add(JsonObjectRequest);*//*






            }
        });*/



        queue1 = Volley.newRequestQueue(

                getContext());


        if (preferences.getString("pic", "").

                equals(""))

        {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TutorImgBiography);
        } else

        {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + preferences.getString("pic", ""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TutorImgBiography);
        }

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      /*  videocallButton1biography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), VideoCall.class);
                i.putExtra("from", "biography");
                startActivity(i);
            }
        });*/

     /*   msgButton1biography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageList messageList = new MessageList();
                fragmentTransaction.replace(R.id.viewpager, messageList).commit();
                FragmentStack fragmentStack = FragmentStack.getInstance();
                fragmentStack.push(new Tablayout_with_viewpager());
            }
        });*/


       /* final Button morelist = (Button) view.findViewById(R.id.moreList);
        morelist.setText("MORE...");*/
        final int entry = 10;

        biography_11 = (LinearLayout) view.findViewById(R.id.biography_11);
        video_11 = (LinearLayout) view.findViewById(R.id.video_11);
        ratings_11 = (LinearLayout) view.findViewById(R.id.ratings_11);
        biography_subject_11 = (LinearLayout) view.findViewById(R.id.biography_subject_11);

//        final ListView listView = (ListView) view.findViewById(R.id.ratingfeedbacklist);

    /*    subHandler= (subjectHandler) new subjectHandler().execute();
        videoUrlHandler= (VideoUrlHandler) new VideoUrlHandler().execute();
*/

//        mediaController = new MediaController(getContext());



        biography_languages = (TextView) view.findViewById(R.id.biography_languages);
        biography_languages_progress = (ProgressBar) view.findViewById(R.id.biography_languages_progress);


        biography_videoview = (VideoView) view.findViewById(R.id.biography_videoView);
        biography_videoview_progress = (ProgressBar) view.findViewById(R.id.biography_videoView_progress);

        AvailableTutorExpandedAdapter availableTutorExpandedAdapter = new AvailableTutorExpandedAdapter(getContext(), entry);

       /* listView.setAdapter(availableTutorExpandedAdapter);

        Helper.getListViewSize(listView);

        morelist.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (liststatus == 0) {
                    listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, entry * 110));
                    morelist.setText("LESS...");
                    liststatus = 1;
                } else {
                    listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, entry * 110
                    ));
                    morelist.setText("MORE...");
                    liststatus = 0;
                }
            }
        });*/
       final int height=biography_biographyfrag_layout.getHeight();

        biography_11.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {


                if (biography_biographyfrag_layout.getVisibility() == View.VISIBLE) {
                    biography_biographyfrag_layout.animate().translationY(0);
                    biography_biographyfrag_layout.setVisibility(View.GONE);
                    biography_btn.setImageResource(R.drawable.side_aerrow);

                } else {
                    biography_biographyfrag_layout.animate().translationY(height);
                    biography_biographyfrag_layout.setVisibility(View.VISIBLE);
                    biography_btn.setImageResource(R.drawable.down_aerrow);
                }


            }
        });

        final LinearLayout biography_video= (LinearLayout) view.findViewById(R.id.biography_video);
        video_11.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {


                if (biography_video.getVisibility() == View.VISIBLE) {
                    biography_video.animate().translationY(0);
                    biography_video.setVisibility(View.GONE);
                    video_btn.setImageResource(R.drawable.side_aerrow);

                } else {
                    biography_video.animate().translationY(height);
                    biography_video.setVisibility(View.VISIBLE);
                    video_btn.setImageResource(R.drawable.down_aerrow);
                }


            }
        });

       /* video_11.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (biography_layout.isExpanded()) {
                    biography_layout.collapse();
                    biography_btn.setImageResource(R.drawable.side_aerrow);
                }
                if (ratings_layout.isExpanded()) {
                    ratings_layout.collapse();
                    ratings_btn.setImageResource(R.drawable.side_aerrow);
                }
                if (biography_subject_layout.isExpanded()) {
                    biography_subject_layout.collapse();
                    biography_subject_btn.setImageResource(R.drawable.side_aerrow);
                }
                video_layout.toggle();
                if (video_layout.isExpanded()) {
                    videoUrlHandler = (VideoUrlHandler) new VideoUrlHandler().execute();
                    video_btn.setImageResource(R.drawable.side_aerrow);
                }
                else {
                 biography_videoview.stopPlayback();
                    video_btn.setImageResource(R.drawable.down_aerrow);
                }

            }
        });
        ratings_11.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
                if (video_layout.isExpanded()) {
                    video_layout.collapse();
                    video_btn.setImageResource(R.drawable.side_aerrow);
                }
                if (biography_layout.isExpanded()) {
                    biography_layout.collapse();
                    biography_btn.setImageResource(R.drawable.side_aerrow);

                }
                if (biography_subject_layout.isExpanded()) {
                    biography_subject_layout.collapse();
                    biography_subject_btn.setImageResource(R.drawable.side_aerrow);
                }
                ratings_layout.toggle();
                if (ratings_layout.isExpanded())
                    ratings_btn.setImageResource(R.drawable.side_aerrow);
                else ratings_btn.setImageResource(R.drawable.down_aerrow);

            }
        });*/
        biography_subject_11.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
             /*   if (video_layout.isExpanded()) {
                    video_layout.collapse();
                    video_btn.setImageResource(R.drawable.side_aerrow);
                }
                if (biography_layout.isExpanded()) {
                    biography_layout.collapse();
                    biography_btn.setImageResource(R.drawable.side_aerrow);

                }
                if (ratings_layout.isExpanded()) {
                    ratings_layout.collapse();
                    ratings_btn.setImageResource(R.drawable.side_aerrow);
                }
                if (biography_subject_layout.isExpanded()) {
                    biography_subject_btn.setImageResource(R.drawable.side_aerrow);
                }
                else biography_subject_btn.setImageResource(R.drawable.down_aerrow);

                biography_subject_layout.toggle();*/
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                FragmentTransaction t = fragmentManager.beginTransaction();
                FragmentStack.getInstance().push(new Tablayout_with_viewpager());
                t.replace(R.id.viewpager, new Biography_subject_Fragment()).commit();
            }
        });
id=getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
        return view;
    }




    private class subjectHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
            Log.e("subjects url", URL);
//            new myLoginData().execute();


            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("subjects tutor expanded", response);
//                        Toast.makeText(getContext(), "Subject response " + response, Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getContext(), "subject object " + jsonObject, Toast.LENGTH_SHORT).show();
                        if (jsonObject.getInt("status") == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String nativeLang = obj.getString("tutoring_subjects");
//                            String OtherLang = obj.getString("otherLanguage");


                            biography_personal.setText(obj.getString("personal"));
                            biography_educational.setText(obj.getString("academic"));
                            biography_professional.setText(obj.getString("professional"));


                            biography_professional_edit.setText(obj.getString("professional"));
                            biography_personal_edit.setText(obj.getString("personal"));
                            biography_educational_edit.setText(obj.getString("academic"));


                            if (!nativeLang.equals("")) {
                                String sub = "";
                                JSONArray ar = new JSONArray(nativeLang);
                                for (int i = 0; i < ar.length(); i++) {
                                    if (sub.equals("")) {
                                        sub = ar.getString(i);
                                    } else {
                                        sub = sub + "," + ar.getString(i);
                                    }
                                }
                                biography_languages.setText(sub);
                            }else biography_languages.setText("");
                            view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        biography_languages.setText("");

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue( getContext()).add(sr);
            return null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            biography_videoview.setMediaController(mediaController);
//            new VideoUrlHandler().execute();
//            if (!mediaController.isShowing())
//            mediaController.setVisibility(View.VISIBLE);
//            callService();
        }
    }

    public void callService() {
//        subHandler = (subjectHandler) new subjectHandler().execute();
        String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + id;
        Log.e("subjects url", URL);
//            new myLoginData().execute();


        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("subjects tutor expanded", response);
//                        Toast.makeText(getContext(), "Subject response " + response, Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getContext(), "subject object " + jsonObject, Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("status") == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String nativeLang = obj.getString("tutoring_subjects");
//                            String OtherLang = obj.getString("otherLanguage");


                        biography_personal.setText(obj.getString("personal"));
                        biography_educational.setText(obj.getString("academic"));
                        biography_professional.setText(obj.getString("professional"));


                        biography_professional_edit.setText(obj.getString("professional"));
                        biography_personal_edit.setText(obj.getString("personal"));
                        biography_educational_edit.setText(obj.getString("academic"));


                        if (!nativeLang.equals("")) {
                            String sub = "";
                            JSONArray ar = new JSONArray(nativeLang);
                            for (int i = 0; i < ar.length(); i++) {
                                if (sub.equals("")) {
                                    sub = ar.getString(i);
                                } else {
                                    sub = sub + "," + ar.getString(i);
                                }
                            }
                            biography_languages.setText(sub);
                        }else biography_languages.setText("");
                        view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    biography_languages.setText("");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
            }
        });
       queue.add(sr);
    }


    @Override
    public void onResume() {
        super.onResume();
//        mediaController.setVisibility(View.VISIBLE);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        callService();

        String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + id;
        Log.e("subjects url", URL);
//            new myLoginData().execute();


        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("subjects tutor expanded", response);
//                        Toast.makeText(getContext(), "Subject response " + response, Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getContext(), "subject object " + jsonObject, Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("status") == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String nativeLang = obj.getString("tutoring_subjects");
//                            String OtherLang = obj.getString("otherLanguage");


                        biography_personal.setText(obj.getString("personal"));
                        biography_educational.setText(obj.getString("academic"));
                        biography_professional.setText(obj.getString("professional"));


                        biography_professional_edit.setText(obj.getString("professional"));
                        biography_personal_edit.setText(obj.getString("personal"));
                        biography_educational_edit.setText(obj.getString("academic"));


                        if (!nativeLang.equals("")) {
                            String sub = "";
                            JSONArray ar = new JSONArray(nativeLang);
                            for (int i = 0; i < ar.length(); i++) {
                                if (sub.equals("")) {
                                    sub = ar.getString(i);
                                } else {
                                    sub = sub + "," + ar.getString(i);
                                }
                            }
                            biography_languages.setText(sub);
                        }else biography_languages.setText("");
                        view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    biography_languages.setText("");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subHandler != null)
            subHandler.cancel(true);
        if (videoUrlHandler != null)
            videoUrlHandler.cancel(true);
//        mediaController.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mediaController.setVisibility(View.GONE);
    }

    int playing = 0;

    private class VideoUrlHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            String URL = "https://www.thetalklist.com/api/tutoring_video?tutor_id=" + preferences.getInt("userId", 0);
//            new myLoginData().execute();


            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("videourl tutor expanded", response);
//                    Toast.makeText(getContext(), "video url response " + response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("status") == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("video");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String link = obj.getString("vedio");
                            if (!link.equals("")) {
                                link = "https://www.thetalklist.com/uploads/video/" + link;


                                biography_videoview.requestFocus();
                                final Uri video = Uri.parse(link);
                                biography_videoview.setVideoURI(video);
                                biography_videoview.seekTo(1000);
                                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(link,
                                        MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

                                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                                biography_videoview.setBackgroundDrawable(bitmapDrawable);

                                biography_videoview.requestFocus();


                                biography_videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        biography_videoview_progress.setVisibility(View.GONE);
                                        controlLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                if (playing == 0) {
                                                    biography_videoview.start();
                                                    controlLayout.setBackgroundColor(Color.parseColor("#00000000"));
                                                    playing = 1;


                                                } else {
                                                    biography_videoview.pause();
                                                    controlLayout.setBackgroundColor(Color.parseColor("#80000000"));
                                                    playing = 0;
                                                }
                                            }
                                        });
                                    }
                                });

                                final String finalLink = link;
                                final String finalLink1 = link;
                                biography_videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Get some Error");
                                        builder.setMessage("Want to open in default Video player?");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(finalLink1));
                                                getContext().startActivity(i);
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                        getActivity().onBackPressed();
                                                biography_videoview.stopPlayback();
                                                biography_videoview_progress.setVisibility(View.GONE);

                                            }
                                        });
                                        builder.show();

                                        return true;
                                    }
                                });
                            } else {

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getContext(), "video not getting", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(getContext()).add(sr);
            return null;
        }
    }


}
