package com.example.saubhagyam.thetalklist;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.*;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by Saubhagyam on 04/04/2017.
 */

public class Available_Tutor_Expanded extends Fragment {

    int liststatus ;
    String LINK = "https://www.thetalklist.com/uploads/video/2013/06/28/howItWorks.mp4";
    android.support.v7.widget.Toolbar toolbar;
    ImageButton msgBtn, videoBtn;
    FragmentManager fragmentManager;
    final FragmentStack fragmentStack = FragmentStack.getInstance();
    FragmentTransaction fragmentTransaction;
    ImageView tutorImage;
    String firstName;

    LinearLayout review_root_biography;

    ExpandableTextView expandableTextView;
    ExpandableTextView expandableTextViewedu;
    ExpandableTextView expandableTextViewpro;

    Button buttonToggle;
    Button buttonToggleedu;
    Button buttonTogglepro;

    //    ListView listView;
    VideoView videoView;
    ImageView minus;
    Button morelist;


    TextView TutorExpanded_tutorin_languages;

    int roleId, roleIdUser;
    String pic, hRate, avgRate;

    //    TextView availableTutorListType;
//    ImageView TutorTypeImg;
    int tutorId;
    View convertView;
    LinearLayout controlLayout;
    TextView firstNameTV;
    TextView availableTutorListCPS;
    RatingBar ratingBar,TutorExpanded_review_ratingBar1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convertView = inflater.inflate(R.layout.available_tutor_expanded, null);
        SharedPreferences preferences1 = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

        roleIdUser = preferences1.getInt("roleId", 0);


        SharedPreferences preferences = getContext().getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
        firstName = preferences.getString("tutorName", "");
        roleId = preferences.getInt("tutorRoleId", 0);
        pic = preferences.getString("tutorPic", "");
        tutorId = preferences.getInt("tutorid", 0);
        hRate = preferences.getString("hRate", "");
        avgRate = preferences.getString("avgRate", "");


        Log.e("pic tutor expanded", pic);

//        availableTutorListType = (TextView) convertView.findViewById(R.id.availableTutorListType);
//        TutorTypeImg = (ImageView) convertView.findViewById(R.id.TutorTypeImg);
       /* toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();*/
//        view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.VISIBLE);
        firstNameTV = (TextView) convertView.findViewById(R.id.expandedToolbartutorName);
        review_root_biography= (LinearLayout) convertView.findViewById(R.id.review_root_expanded);

        expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_personal);
        expandableTextViewedu = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_educational);
        expandableTextViewpro = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_professional);
//        final ExpandableTextView expandableTextViewpro = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_professional);

        buttonToggle = (Button) convertView.findViewById(R.id.more);
        buttonToggleedu = (Button) convertView.findViewById(R.id.moreedu);
        buttonTogglepro = (Button) convertView.findViewById(R.id.moreprof);

//        listView = (ListView) convertView.findViewById(R.id.ratingfeedbacklist);
        videoView = (VideoView) convertView.findViewById(R.id.TutorExpanded_biography_videoView);
        availableTutorListCPS = (TextView) convertView.findViewById(R.id.availableTutorListCPS);

        msgBtn = (ImageButton) convertView.findViewById(R.id.imageButton3);
        videoBtn = (ImageButton) convertView.findViewById(R.id.imageButton6);
        tutorImage = (ImageView) convertView.findViewById(R.id.TutorImg);
        minus = (ImageView) convertView.findViewById(R.id.TutorExpanded_review_minus);
        morelist = (Button) convertView.findViewById(R.id.moreList);
        controlLayout = (LinearLayout) convertView.findViewById(R.id.controlLayout);
        ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TutorExpanded_review_ratingBar1 = (RatingBar) convertView.findViewById(R.id.TutorExpanded_review_ratingBar1);
        if (avgRate.equalsIgnoreCase("")) {
            ratingBar.setRating(0f);
            TutorExpanded_review_ratingBar1.setRating(0f);
        } else {
            Float rate = Float.parseFloat(avgRate);
            ratingBar.setRating(rate);
            TutorExpanded_review_ratingBar1.setRating(rate);
        }

        TutorExpanded_tutorin_languages = (TextView) convertView.findViewById(R.id.TutorExpanded_tutorin_languages);


        buttonTogglepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandableTextViewpro.toggle();
                buttonTogglepro.setText(expandableTextViewpro.isExpanded() ? "more..." : "Less...");
            }
        });

        expandableTextViewpro.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView collapsed");
            }
        });
        expandableTextViewpro.setAnimationDuration(1000L);

//         set interpolators for both expanding and collapsing animations
        expandableTextViewpro.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableTextViewpro.setExpandInterpolator(new OvershootInterpolator());
        expandableTextViewpro.setCollapseInterpolator(new OvershootInterpolator());


        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        firstNameTV.setText(firstName);
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();
//        view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.VISIBLE);
//        TextView firstNameTV = (TextView) conv.findViewById(R.id.expandedToolbartutorName);
//        firstNameTV.setText(firstName);


      /*  if (roleId == 1) {
            TutorTypeImg.setBackgroundResource(R.drawable.red_dot);
            availableTutorListType.setTextColor(Color.parseColor("#C62828"));
            availableTutorListType.setText("Bronze");
        }
        if (roleId == 2) {
            TutorTypeImg.setBackgroundResource(R.drawable.silver_dot);
            availableTutorListType.setTextColor(Color.parseColor("#CFD8DC"));
            availableTutorListType.setText("Silver");
        }
        if (roleId == 3) {
            TutorTypeImg.setBackgroundResource(R.drawable.gold_dot);
            availableTutorListType.setTextColor(Color.parseColor("#FFC107"));
            availableTutorListType.setText("Gold");
        }*/


        String h_str = String.format("%.02f", Float.parseFloat(hRate) / 25);
        availableTutorListCPS.setText(h_str);
        if (!pic.equals("")) {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tutorImage);
        } else {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tutorImage);
        }

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences chatPref = getContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor chatPrefEditor = chatPref.edit();
                chatPrefEditor.putString("firstName", firstName);
                chatPrefEditor.putInt("receiverId", tutorId).apply();

                fragmentStack.push(new Available_Tutor_Expanded());
                MessageOneToOne messageList = new MessageOneToOne();
                fragmentTransaction.replace(R.id.viewpager, messageList).commit();
            }
        });


        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToolbar();
                fragmentStack.push(new Available_Tutor_Expanded());
                AvailableTutor_Fevorite_tablayout availableTutor_fevorite_tablayout = new AvailableTutor_Fevorite_tablayout();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 1);
                bundle.putInt("credit", 0);
                availableTutor_fevorite_tablayout.setArguments(bundle);
                fragmentTransaction.replace(R.id.viewpager, availableTutor_fevorite_tablayout).commit();
            }
        });





        final int height1=review_root_biography.getHeight();

        final int entry = 5;
        AvailableTutorExpandedAdapter availableTutorExpandedAdapter = new AvailableTutorExpandedAdapter(getContext(), entry);
//        listView.setAdapter(availableTutorExpandedAdapter);


        morelist.setText("MORE...");


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                pDialog.dismiss();
                /*videoView.start();*/
            }
        });


        subHandler = (subjectHandler) new subjectHandler().execute();
        videoUrlHandler = (VideoUrlHandler) new VideoUrlHandler().execute();

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        morelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (review_root_biography.getVisibility() == View.VISIBLE) {

//                    review_root_biography.animate().translationY(0);
                    review_root_biography.setVisibility(View.GONE);
                    morelist.setText("LESS...");
                } else {
//                    review_root_biography.animate().translationY(height1);
                    review_root_biography.setVisibility(View.VISIBLE);
                    morelist.setText("MORE...");
                }


            }
        });



        {
            String URL = "http://www.thetalklist.com/api/reviews?uid="+ tutorId;
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

                            ((TextView)convertView.findViewById(R.id.TutorExpanded_review_count)).setText((String.valueOf(reviewAry.length())));

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





        expandableTextView.setAnimationDuration(1000L);

//         set interpolators for both expanding and collapsing animations
        expandableTextView.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableTextView.setExpandInterpolator(new OvershootInterpolator());
        expandableTextView.setCollapseInterpolator(new OvershootInterpolator());


        expandableTextViewedu.setAnimationDuration(1000L);

//         set interpolators for both expanding and collapsing animations
        expandableTextViewedu.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableTextViewedu.setExpandInterpolator(new OvershootInterpolator());
        expandableTextViewedu.setCollapseInterpolator(new OvershootInterpolator());


        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandableTextView.toggle();
                buttonToggle.setText(expandableTextView.isExpanded() ? "more..." : "Less...");
            }
        });

        expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView collapsed");
            }
        });


        buttonToggleedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandableTextViewedu.toggle();
                buttonToggleedu.setText(expandableTextViewedu.isExpanded() ? "more..." : "Less...");
            }
        });

        expandableTextViewedu.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView collapsed");
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        super.onAttach(context);
//        View view = toolbar.getRootView();
//        view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.VISIBLE);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();
//        view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();
//        view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);
//        settingFlyout.toolbarLayout.removeView(view);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();
//        view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();
//        view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);
    }


    String finalString;


    private class subjectHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + tutorId;
//            new myLoginData().execute();
            RequestQueue queue = Volley.newRequestQueue(getContext());

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

                            expandableTextView.setText(obj.getString("personal"));
                            expandableTextViewedu.setText(obj.getString("academic"));
                            expandableTextViewpro.setText(obj.getString("professional"));


                            /*String nativeLang = obj.getString("nativeLanguage");
                            String OtherLang = obj.getString("otherLanguage");


                            expandableTextView.setText(obj.getString("personal"));
                            expandableTextViewedu.setText(obj.getString("academic"));
                            expandableTextViewpro.setText(obj.getString("professional"));

                            if (nativeLang.equals("") && OtherLang.length() > 0) {
                                finalString = OtherLang + ".";
                            } else if (OtherLang.equals("") && nativeLang.length() > 0)
                                finalString = nativeLang + ".";
                            else if (nativeLang.equals("") && OtherLang.equals(""))
                                finalString = "Sorry.. No Subject specify yet.";
                            else finalString = nativeLang + ", " + OtherLang + ".";
//                                obj.getString("nativeLanguage") + ", " + obj.getString("otherLanguage") + "."
                            TutorExpanded_tutorin_languages.setText(finalString);*/

                            if (obj.getString("tutoring_subjects").equalsIgnoreCase("")) {
                                TutorExpanded_tutorin_languages.setText("No subjects Here");
                                convertView.findViewById(R.id.TutorExpanded_tutorin_languages_progress).setVisibility(View.GONE);
                            } else {


                                String nativeLang = obj.getString("tutoring_subjects");
                                String sub ="";
                                JSONArray ar=new JSONArray(nativeLang);
                                for (int i=0;i<ar.length();i++)
                                {
                                    if (sub.equals("")){
                                        sub=ar.getString(i);
                                    }else {
                                        sub=sub+","+ar.getString(i);
                                    }
                                }
                                TutorExpanded_tutorin_languages.setText(sub);
                                convertView.findViewById(R.id.TutorExpanded_tutorin_languages_progress).setVisibility(View.GONE);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(sr);
            return null;
        }
    }

    int playing = 0;

    private class VideoUrlHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            String URL = "https://www.thetalklist.com/api/tutoring_video?tutor_id=" + tutorId;
//            new myLoginData().execute();
            RequestQueue queue1 = Volley.newRequestQueue(getContext());

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
                                final Uri video = Uri.parse(link);
                                videoView.setVideoURI(video);
                                videoView.seekTo(1000);
                                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(link,
                                        MediaStore.Images.Thumbnails.MINI_KIND);

                                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                                videoView.setBackgroundDrawable(bitmapDrawable);


                                videoView.requestFocus();

                                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        convertView.findViewById(R.id.TutorExpanded_biography_videoView_progress).setVisibility(View.GONE);
                                        controlLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                if (playing == 0) {
                                                    videoView.start();
                                                    controlLayout.setBackgroundColor(Color.parseColor("#00000000"));
                                                    playing = 1;
                                                } else {
                                                    videoView.pause();
                                                    controlLayout.setBackgroundColor(Color.parseColor("#80000000"));
                                                    playing = 0;
                                                }
                                            }
                                        });
                                    }
                                });

                                final String finalLink = link;
                                /*videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(finalLink));
                                        getContext().startActivity(i);

                                        return true;
                                    }
                                });*/
                            } else {
                               /* Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.errorimage);
                                videoView.setBackgroundDrawable(new BitmapDrawable(largeIcon));*/
                                convertView.findViewById(R.id.TutorExpanded_biography_videoView_progress).setVisibility(View.GONE);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "video not getting", Toast.LENGTH_SHORT).show();
                }
            });
            queue1.add(sr);
            return null;
        }
    }

    subjectHandler subHandler;
    VideoUrlHandler videoUrlHandler;

    public void setToolbar() {
        /*if (roleIdUser == 0) {
            View view = toolbar.getRootView();
            view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tutorToolbar).setVisibility(View.GONE);
            view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

        }
        if (roleIdUser == 1 || roleIdUser == 2 || roleIdUser == 3) {*/
        View view = toolbar.getRootView();
        view.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
//            view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

//                }

//        }
    }

    @Override
    public void onPause() {
        super.onPause();


        if (subHandler != null)
            subHandler.cancel(true);
        if (videoUrlHandler != null)
            videoUrlHandler.cancel(true);

    }
}

class AvailableTutorExpandedAdapter extends BaseAdapter {
    final Context context;
    final int entry;
    ImageView imageView;

    public AvailableTutorExpandedAdapter(Context context, int entry) {
        this.context = context;
        this.entry = entry;
    }

    @Override
    public int getCount() {
        return entry;
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


            convertView = LayoutInflater.from(context).inflate(R.layout.available_tutor_expanded_ratings_feedback, parent, false);

            imageView = (ImageView) convertView.findViewById(R.id.imageView9);
            Glide.with(context).load("https://ak6.picdn.net/shutterstock/videos/6351593/thumb/1.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        return convertView;
    }


}
