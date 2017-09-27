package com.example.saubhagyam.thetalklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.example.saubhagyam.thetalklist.Adapter.Biography_subject_adapter;
import com.example.saubhagyam.thetalklist.Config.Config;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.Services.LoginService;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingFlyout extends AppCompatActivity {


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String email, pass;
    SharedPreferences preferences;
    DrawerLayout drawer;
    Switch talkNow;
    public NavigationView navigationView;
    LinearLayout viewPager;
    int roleId;
    int status;
    Context context;
    List<Fragment> fragmentList;

    Toolbar toolbar;
    final FragmentStack fragmentStack = FragmentStack.getInstance();
    TextView credits, num_ttlScore;
    Typeface typeface;
    View v;
    TextView TVuserName;
    ImageView TVusericon;
    View view1;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavigationDrawerItemTitles;
    ActionBarDrawerToggle mDrawerToggle;
    LinearLayout settingFlyout_bottomcontrol_favorites, settingFlyout_bottomcontrol_videosearch, settingFlyout_bottomcontrol_Message,
            settingFlyout_bottomcontrol_payments, settingFlyout_bottomcontrol_tutorSearch, settingFlyout_bottomcontrol;
    TextView bottombar_message_count;

    Boolean MessageFrag;

    BroadcastReceiver countrefresh;

    public SettingFlyout() {
    }

    public SettingFlyout(Boolean messageFrag) {
        MessageFrag = messageFrag;
        setFragmentByVideoCall(new VideoList());
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }
   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_setting_flyout);

        } else {
            setContentView(R.layout.activity_setting_flyout);
        }
    }*/

    RequestQueue queue;


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(countrefresh);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_flyout);

//        view = this.getLayoutInflater().inflate(R.layout.expanded_tutor_toolbar, null);
        v = getLayoutInflater().inflate(R.layout.tutor_actionbar_layout, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ttl = (TTL) getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (LinearLayout) findViewById(R.id.viewpager);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        credits = (TextView) toolbar.findViewById(R.id.num_credits);
        num_ttlScore = (TextView) toolbar.findViewById(R.id.num_ttlScore);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/GothamBookRegular.ttf");
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        view1 = navigationView.getHeaderView(0);
//        imageHeaderBg = (ImageView) view1.findViewById(R.id.img_header_bg);
        TVuserName = (TextView) view1.findViewById(R.id.personTextsettingflyout);
        TVusericon = (ImageView) view1.findViewById(R.id.imagesettingflyoutheader);
        fragmentManager = getSupportFragmentManager();
        bottombar_message_count = (TextView) findViewById(R.id.bottombar_message_count);
        talkNow = (Switch) toolbar.findViewById(R.id.switch1);
//        searchImageView = (ImageView) toolbar.findViewById(R.id.searchImageView);

        settingFlyout_bottomcontrol_videosearch = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_videosearch);
        settingFlyout_bottomcontrol_Message = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_Message);
        settingFlyout_bottomcontrol_tutorSearch = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_tutorSearch);
        settingFlyout_bottomcontrol_payments = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_payments);
        settingFlyout_bottomcontrol_favorites = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_favorites);
        settingFlyout_bottomcontrol = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol);



context=getApplicationContext();

        drawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = drawer.getRootView().getHeight() - drawer.getHeight();

                if (heightDiff > 100) {
                    settingFlyout_bottomcontrol.setVisibility(View.GONE);
                    Log.e("MyActivity", "keyboard opened");
                } else {
                    settingFlyout_bottomcontrol.setVisibility(View.VISIBLE);
                    Log.e("MyActivity", "keyboard closed");
                }
            }
        });

        count();
        /*{
            String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("message count res ",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("unread_count") > 0)
                            bottombar_message_count.setText(String.valueOf(object.getInt("unread_count")));
                        if (object.getInt("unread_count") == 0)
                            findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
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
        }*/


//        settingFlyout_readyToTalk_toggle= (Switch) findViewById(R.id.settingFlyout_readyToTalk_toggle);

        fragmentTransaction = fragmentManager.beginTransaction();

        /*settingFlyout_readyToTalk_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                queue111 = Volley.newRequestQueue(getApplicationContext());



                String URL = "https://www.thetalklist.com/api/tutor_readyToTalk?uid=17431;
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int status = jsonObject.getInt("status");

                            Log.e("video call api response", response);
                            if (status == 0) {

                                JSONObject object = new JSONObject("detail");
                                CallerName = object.getString("firstName");
                                CallerPic = object.getString("pic");
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
                queue111.add(sr);

            }
        });*/



        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("global");


        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(drawer);
                drawer.closeDrawers();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawer);

            }
        };

        queue111 = Volley.newRequestQueue(getApplicationContext());
       /* mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };*/
        displayFirebaseRegId();


        new firebase_regId_store().execute();
        pref = getSharedPreferences("loginStatus", MODE_PRIVATE);
        num_ttlScore.setText(String.valueOf(pref.getFloat("ttl_points",0.0f)));
        editor = pref.edit();
        email = pref.getString("email", "");
        pass = pref.getString("pass", "");
        /*
        final FragmentManager fragmentManager =getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint())
                fragmentStack.push(fragment);
        }*/

        mHandler = new Handler();


        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_item_text_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(drawer);
                drawer.closeDrawers();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawer);

            }
        };


        final FragmentStack fragmentStack = FragmentStack.getInstance();
        final TTL ttl = (TTL) getApplicationContext();


        settingFlyout_bottomcontrol_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new FavoriteTutor()).commit();

                fragmentStack.push(new Available_tutor());

//                ((ImageView)findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout_yellow));
                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout_yellow));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));
            }
        });


        settingFlyout_bottomcontrol_videosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new VideoList()).commit();
                fragmentStack.push(new Available_tutor());
//                ((ImageView)findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch_yellow));

                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch_yellow));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));

            }
        });
        settingFlyout_bottomcontrol_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new MessageList()).commit();
                fragmentStack.push(new Available_tutor());

                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar_yellow));
            }
        });

        settingFlyout_bottomcontrol_tutorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new Available_tutor()).commit();


                ttl.ExitBit = 1;

//                ((ImageView)findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout_yellow));
                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout_yellow));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));
            }
        });

        settingFlyout_bottomcontrol_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();

                fragmentStack.push(new Available_tutor());
//                ((ImageView)findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar_yellow));

                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar_yellow));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));
            }
        });


        DrawerModel[] drawerItem = new DrawerModel[9];
        drawerItem[0] = new DrawerModel(R.drawable.home, "Profile");
        drawerItem[1] = new DrawerModel(R.drawable.desiretour, "Desired Tutor");
        drawerItem[2] = new DrawerModel(R.drawable.paypal, "Payment Options");
//        drawerItem[3] = new DrawerModel(R.drawable.signout, "Tutor Guide");
//        drawerItem[3] = new DrawerModel(R.drawable.desiretour, "Lesson Tracking");
        drawerItem[3] = new DrawerModel(R.drawable.tilt, "TTL Score");
        drawerItem[4] = new DrawerModel(R.drawable.history, "History");
        drawerItem[5] = new DrawerModel(R.drawable.notification, "Notifications");
        drawerItem[6] = new DrawerModel(R.drawable.calendar, "Availability");
        drawerItem[7] = new DrawerModel(R.drawable.support, "Support");
        drawerItem[8] = new DrawerModel(R.drawable.signout, "Sign out");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.customdrawerlayout, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(mDrawerToggle);


        status = pref.getInt("status", 1);


        preferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        String username = preferences.getString("usernm", "");


        TVuserName.setText(username);
        String pic = preferences.getString("pic", "");
        if (!pic.equals("")) {
            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TVusericon);
        } else {
            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TVusericon);
        }



        /*  String loginResponse=pref.getString("loginResponse","");
        JSONObject jsonObject = null;
        try {
            JSONObject resultObj= new JSONObject(loginResponse);
            credits.setText(resultObj.getString("money"));

            TVuserName.setText(resultObj.getString("usernm"));
            String pic1 = resultObj.getString("pic");
            if (!pic1.equals("")) {
                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic1)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);
            } else {
                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        TVusericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (status == 1) {

            roleId = 1;
        } else roleId = preferences.getInt("roleId", 0);


        prefAvailableTutor = getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
        ed = prefAvailableTutor.edit();

        prefVideoList = getSharedPreferences("videoListResponse", Context.MODE_PRIVATE);
        edvideo = prefVideoList.edit();


        tabBackStack = TabBackStack.getInstance();


        fragmentTransaction.addToBackStack(null);
        if (status == 0) {
          /*  if (roleId == 0) {
                View view = toolbar.getRootView();
                view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
                view.findViewById(R.id.tutorToolbar).setVisibility(View.GONE);
                view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

//                fragmentStack.push(new Student_Tab_Layout());
                Student_Tab_Layout withViewpager = new Student_Tab_Layout();
                fragmentTransaction.replace(R.id.viewpager, withViewpager).commit();
            }
            if (roleId == 1 || roleId == 2 || roleId == 3) {*/


//                Toast.makeText(this, "rolle id createview "+roleId, Toast.LENGTH_SHORT).show();
             /*   View view = toolbar.getRootView();
                view.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
                view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
                view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);*/


//                fragmentStack.push(new DesiredTutor());
//                FragmentManager fragmentManager = getFragmentManager();

               /* final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences("firstTime", Context.MODE_PRIVATE);
                if (pref1.contains("fromSignUp")) {

                    Tablayout_with_viewpager withViewpager = new Tablayout_with_viewpager();
                    fragmentTransaction.replace(R.id.viewpager, withViewpager);
                    fragmentTransaction.commit();
                    SharedPreferences.Editor editor=pref1.edit();
                    editor.clear().apply();

                } else {*/
            String ofMessage=getIntent().getStringExtra("message");
            String firstName=getIntent().getStringExtra("uname");
            int uid=getIntent().getIntExtra("senderId",0);

       /*     if (getIntent()!=null) {
                if (getIntent().hasExtra("fragmant")) {
                    if (getIntent().getStringExtra("fragmant").equals("video")){
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.viewpager, new MyDetailsB()).commit();
                }
                }
            }*/

            if (ofMessage!=null){
                Log.e("message",ofMessage);
            Log.e("uid", String.valueOf(uid));




                SharedPreferences chatPref=getSharedPreferences("chatPref",Context.MODE_PRIVATE);
                final SharedPreferences.Editor chatPrefEditor=chatPref.edit();

                chatPrefEditor.putString("firstName",firstName);
                chatPrefEditor.putInt("receiverId",uid).apply();

                FragmentStack.getInstance().push(new Available_tutor());

                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.viewpager, new MessageOneToOne()).commit();
            }else
            fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();
//                }

//            }
        } else {

//            fragmentStack.push(new Tablayout_with_viewpager());
            Tablayout_with_viewpager withViewpager = new Tablayout_with_viewpager();
            fragmentTransaction.replace(R.id.viewpager, withViewpager);
            fragmentTransaction.commit();
        }


//        drawer.setDrawerListener(toggle);
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
//        toggle.syncState();


    }


    public void count(){
        String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("message count res ",response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("unread_count") > 0)
                        bottombar_message_count.setText(String.valueOf(object.getInt("unread_count")));
                    if (object.getInt("unread_count") == 0)
                        findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
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

    public void setFragmentByVideoCall(Fragment fragmentByVideoCall) {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.viewpager, fragmentByVideoCall).commit();
    }

    RequestQueue queue111;
    String firebase_regId;

    private class firebase_regId_store extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

//            Log.e("firebaseRegId", firebase_regId);

            String URL = "https://www.thetalklist.com/api/firebase_register?user_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&reg_id=" + firebase_regId;
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int status = jsonObject.getInt("status");

                        if (status == 0) {
//                            Toast.makeText(getApplicationContext(), "Firebase reg id stored "+firebase_regId, Toast.LENGTH_SHORT).show();
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
            queue111.add(sr);


            return null;
        }
    }

//    BroadcastReceiver mRegistrationBroadcastReceiver;


    private void displayFirebaseRegId() {

//        Log.e("pref", pref.toString());
        if (pref == null) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
//            storeRegIdInPref(refreshedToken);

            // sending reg id to your server
            myFirebaseInstanceIDService.sendRegistrationToServer(refreshedToken);

            // Notify UI that registration has completed, so the progress indicator can be hidden.
            /*Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/


            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);

            firebase_regId = refreshedToken;
            Log.e("firebase reg id 1111111", "Firebase reg id: " + refreshedToken);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Handler mHandler;

    int online = 1;

    @Override
    protected void onResume() {
        super.onResume();


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
               loginService();
            }
        }, 5000);

        ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
        ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));
        LoginService loginService=new LoginService();
        loginService.login(pref.getString("email",""),pref.getString("pass",""),getApplicationContext());
      /*  String loginResponse=pref.getString("loginResponse","");
        try {
           JSONObject resultObj= new JSONObject(loginResponse);
        credits.setText(resultObj.getString("money"));

            TVuserName.setText(resultObj.getString("usernm"));
            String pic = resultObj.getString("pic");
            if (!pic.equals("")) {
                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);
            } else {
                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


       /* Login login=new Login();
        login.signIn.performClick();*/

    /*   if (ttl.firstTimeLogin==0) {
           ttl.firstTimeLogin=1;*/

    /*    final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 5 seconds*/


//    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        queue = Volley.newRequestQueue(getApplicationContext());


        countrefresh=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("message count res ",response);

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("unread_count") > 0) {
                                findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.VISIBLE);
                                bottombar_message_count.setText(String.valueOf(object.getInt("unread_count")));
                            }
                            if (object.getInt("unread_count") == 0)
                                findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
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
        };
        registerReceiver(countrefresh, new IntentFilter("countrefresh"));

        loginService();
             /*   String URL = "https://www.thetalklist.com/api/login?email=" + pref.getString("email", "") + "&password=" + pref.getString("pass", "");


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


                final StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);

                        UserData data = UserData.getInstance();
                        data.setLoginServResponse(response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);


                            Log.e("response", response);

                            int status = (int) jsonObject.get("status");
                            if (status == 1) {

                                String Err = (String) jsonObject.get("error");

                            } else {


                                JSONObject resultObj = (JSONObject) jsonObject.get("result");
                                int roleId = resultObj.getInt("roleId");
                                String UserName = (String) resultObj.get("username");
                                int userId = resultObj.getInt("id");
                                String mail = resultObj.getString("email");

                                editor.putString("LoginWay", "InternalLogin");
                                editor.putString("loginResponse", response);
                                editor.putString("user", UserName);
                                editor.putInt("roleId", roleId);
                                editor.putBoolean("logSta", true);
                                editor.putString("usernm", resultObj.getString("usernm"));
                                editor.putInt("userId", userId);
                                editor.putString("credit_balance", resultObj.getString("credit_balance"));
                                editor.putString("usernm", resultObj.getString("usernm"));
                                editor.putInt("id", resultObj.getInt("id"));
                                editor.putString("city", resultObj.getString("city"));
                                editor.putString("nativeLanguage", resultObj.getString("nativeLanguage"));
                                editor.putString("otherLanguage", resultObj.getString("otherLanguage"));
                                editor.putInt("status", 0);
//                                editor.putFloat("money", 0.0f);
                                editor.putString("email", mail);
                                editor.apply();

                                credits.setText(resultObj.getString("money"));
                                TVuserName.setText(resultObj.getString("usernm"));
                                String pic = resultObj.getString("pic");
                                if (!pic.equals("")) {
                                    Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(TVusericon);
                                } else {
                                    Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(TVusericon);
                                }


                       *//*    Intent i = new Intent(getApplicationContext(), SettingFlyout.class);
                           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           i.putExtra("status", 0);
                           i.putExtra("roleId", roleId);*//*


                       *//*    i.putExtra("username", UserName);
                           startActivity(i);*//*
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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


                        Log.d("error", error.toString());
                    }
                });
                sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(sr);*/
        /*        handler.postDelayed(this, 10000);
            }
        }, 5000);*/

//       }


        credits.setText(String.valueOf(pref.getFloat("money", 0.0f)));
        Log.e("money", String.valueOf(pref.getFloat("money", 0.0f)));
        credits.setTypeface(typeface);
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               Fragment fragment= getSupportFragmentManager().findFragmentByTag( fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).toString());
                fragmentStack.push(new Available_tutor());
                /*@SuppressLint("RestrictedApi") List<Fragment> fraList=fragmentManager.getFragments();
                fragmentStack.push(fraList.get(fraList.size()-1));*/
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();
            }
        });
        num_ttlScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment= (Fragment) fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1);
                fragmentStack.push(new Available_tutor());
               /* @SuppressLint("RestrictedApi") List<Fragment> fraList=fragmentManager.getFragments();
                fragmentStack.push(fraList.get(fraList.size()-1));*/
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager, new TTL_Score()).commit();
            }
        });
        talkNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    online = 1;
                } else {
                    online = 0;
                }
                String Url = "https://www.thetalklist.com/api/tutor_online?uid=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&bit=" + online;
                StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(strRequest);


            }
        });


    }
    StringRequest sr;
    public void loginService() {

        String URL = "https://www.thetalklist.com/api/login?email=" + email+ "&password=" + pass;




         sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);

                UserData data = UserData.getInstance();
                data.setLoginServResponse(response);


                try {

                    JSONObject jsonObject = new JSONObject(response);


                    Log.e("response", response);

                    int status = (int) jsonObject.get("status");
                    if (status == 1) {

                        String Err = (String) jsonObject.get("error");

                    } else {


                        JSONObject resultObj = (JSONObject) jsonObject.get("result");
                        int roleId = resultObj.getInt("roleId");
                        String UserName = (String) resultObj.get("username");
                        int userId = resultObj.getInt("id");
                        String mail = resultObj.getString("email");

                        editor.putString("LoginWay", "InternalLogin");
                        editor.putString("loginResponse", response);
                        editor.putString("user", UserName);
                        editor.putInt("roleId", roleId);
                        editor.putBoolean("logSta", true);
                        editor.putString("usernm", resultObj.getString("usernm"));
                        editor.putInt("userId", userId);
                        editor.putString("credit_balance", resultObj.getString("credit_balance"));
                        editor.putString("usernm", resultObj.getString("usernm"));
                        editor.putInt("id", resultObj.getInt("id"));
                        editor.putInt("country", resultObj.getInt("country"));
                        editor.putInt("province", resultObj.getInt("province"));
                        editor.putString("city", resultObj.getString("city"));
                        editor.putString("nativeLanguage", resultObj.getString("nativeLanguage"));
                        editor.putString("otherLanguage", resultObj.getString("otherLanguage"));
                        editor.putInt("status", 0);
//                                editor.putFloat("money", 0.0f);
                        editor.putString("email", mail);
                        editor.apply();

                        credits.setText(resultObj.getString("money"));
                        TVuserName.setText(resultObj.getString("usernm"));
                        String pic = resultObj.getString("pic");
                        if (!pic.equals("")) {
                            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                                    .crossFade()
                                    .thumbnail(0.5f)
                                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(TVusericon);
                        } else {
                            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                    .crossFade()
                                    .thumbnail(0.5f)
                                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(TVusericon);
                        }


                       /*    Intent i = new Intent(getApplicationContext(), SettingFlyout.class);
                           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           i.putExtra("status", 0);
                           i.putExtra("roleId", roleId);*/


                       /*    i.putExtra("username", UserName);
                           startActivity(i);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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


                Log.d("error", error.toString());
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        public void selectItem(int position) {
            SharedPreferences prefAvailableTutor = getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor edavailabe = prefAvailableTutor.edit();

            ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
            ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
            ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
            ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
            ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));
            SharedPreferences prefVideoList = getSharedPreferences("videoListResponse", Context.MODE_PRIVATE);
            SharedPreferences.Editor edvideo = prefVideoList.edit();

            Fragment fragment = null;
            switch (position) {
                case 0:
//                    if (roleId == 1 || roleId == 2 || roleId == 3)
                    LoginService loginService=new LoginService();
                    loginService.login(getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),getApplicationContext());
                    fragment = new Tablayout_with_viewpager();
//                    else fragment = new Student_Tab_Layout();
                    break;
                case 1:
                    fragment = new DesiredTutor();
                    SharedPreferences pref1=getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edi=pref1.edit();
                    edi.clear().apply();
                    break;
                case 2:
                    fragment = new Earn_Buy_tabLayout();
                    break;
               /* case 3:
                    fragment = new TutorGuides();
                    break;*/
                /*`case 3:
                    fragment = new lessonTracking();
                    break;*/
                case 3:
                    fragment = new TTL_Score();
                    break;
                case 4:
                    fragment = new History();
                    break;
                case 5:
                    fragment = new Notification();
                    break;
                case 6:
                    fragment = new Availability_page_fragment();
                    break;
                case 7:
                    fragment = new Support();
                    break;
               /* case 8:
                    fragment = new Support();
                    break;*/

                case 8:

                    SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

                    String URL = "https://www.thetalklist.com/api/signout?uid=" + pref.getInt("id", 0);
                    StringRequest sr1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(getApplicationContext(), "status "+response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "status " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(sr1);
                    pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.clear();
                    editor.apply();
                    fragmentStack.clear();
                    ttl.ExitBit = 1;
                    SharedPreferences pref11 = getApplicationContext().getSharedPreferences("firstTime", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor ed = pref11.edit();
                    ed.clear().apply();

                    edavailabe.clear().apply();
                    edvideo.putInt("position", 0).apply();

                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    break;


                default:
                    break;
            }
            if (fragment != null) {


                final Fragment finalFragment = fragment;
                Runnable mPendingRunnable = new Runnable() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.viewpager, finalFragment);
//                        fragmentStack.push(new Tablayout_with_viewpager());
                        TTL ttl = (TTL) getApplicationContext();
                        ttl.ExitBit = 1;

                      /*  if (roleId == 0) {
                            fragmentStack.push(new Student_Tab_Layout());

                        } else*/
                        fragmentList = fragmentManager.getFragments();

                        if (fragmentStack.size() > 0) {
                            fragmentStack.pop();
                            fragmentStack.push(fragmentList.get(fragmentList.size() - 1));
                        } else fragmentStack.push(new Available_tutor());
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };

                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }

                // show or hide the fab button

                //Closing drawer on item click
                drawer.closeDrawers();

                // refresh toolbar menu
                invalidateOptionsMenu();

//                mDrawerList.setItemChecked(position, true);
//                mDrawerList.setSelection(position);
                setTitle(mNavigationDrawerItemTitles[position]);
//                drawer.closeDrawer(mDrawerList);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
//        toggle.syncState();
    }

    String Cls = " ";
    private boolean bit;


    SharedPreferences prefAvailableTutor;
    SharedPreferences.Editor ed;
    SharedPreferences prefVideoList;
    SharedPreferences.Editor edvideo;
    TabBackStack tabBackStack;
    TTL ttl;

    @Override
    public void onBackPressed() {




        ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
        ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
   /*     if (roleId == 0) {
            View view = toolbar.getRootView();
            view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tutorToolbar).setVisibility(View.GONE);
            view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

            fragmentTransaction1.replace(R.id.viewpager, new Student_Tab_Layout()).commit();
        }
        if (roleId == 1 || roleId == 2 || roleId == 3) {*/

           /* View view = toolbar.getRootView();
            view.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
            view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

            fragmentTransaction1.replace(R.id.viewpager, new DesiredTutor()).commit();*/
//                }

//        }
        SharedPreferences chatPref = getSharedPreferences("chatPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor chatPrefEditor = chatPref.edit();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

           /* if (Cls.equals("class DesiredTutor") || Cls.equals("class Student_Tab_Layout")) {
                if (ttl.ExitBit>0) {
                    if (ttl.ExitBit==1)
                    Toast.makeText(getApplicationContext(), "Please press once to exit", Toast.LENGTH_SHORT).show();
                    ttl.ExitBit--;
                } else {
//                    Toast.makeText(getApplicationContext(), "bit over..", Toast.LENGTH_SHORT).show();
                    ed.clear().apply();
                    tabBackStack.setTabPosition(0);
                    fragmentStack.clear();
                    edvideo.putInt("position", 0).apply();
                    finish();
                }
            } else {*/
//            Toast.makeText(getApplicationContext(), "size " + fragmentStack.size(), Toast.LENGTH_SHORT).show();

            if (fragmentStack.size() > 0) {
                Fragment fragment = fragmentStack.pop();


                if (fragment != null) {

               /* if (fragment.getClass().toString().equals("class DesiredTutor")) {
                    fragmentStack.setSize(2);
                }*/
/*
                    if (fragmentStack.size() == 1) {
                        Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                    }
*/
//                    fragmentTransaction.replace(R.id.viewpager, fragment).commit();

                      /*  if ((roleId == 1 || roleId == 2 || roleId == 3) && fragment.getClass().toString().equals("class DesiredTutor")) {
                            fragmentStack.push(new DesiredTutor());
                        }*//* else if ((roleId == 0) && fragment.getClass().toString().equals("class Student_Tab_Layout")) {
                            fragmentStack.setSize(1);
                        } else {*/
                    Cls = fragment.getClass().toString();
//                        Toast.makeText(getApplicationContext(), "Cls " + Cls, Toast.LENGTH_SHORT).show();
//                       ttl.setExitBit(false);
                    Log.e("class Name :- ", fragment.getClass().toString());
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.viewpager, fragment).commit();
//                        }
//                        }
                } else {
                    if (fragmentStack.size() > 0) {

                        if (fragmentStack.size() == 1) {
                            Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                        }

                    }/*else if (fragmentStack.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                    } */ else {
                        fragmentStack.setSize(0);
                        tabBackStack.setTabPosition(0);
                        if (getSharedPreferences("loginStatus", MODE_PRIVATE).getString("LoginWay", "").equalsIgnoreCase("FacebookLogin")) {
                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();

                            AccessToken.setCurrentAccessToken(null);
                        }
                        finish();
                        chatPrefEditor.clear().apply();
                        ed.clear().apply();
                        edvideo.putInt("position", 0).apply();
                        moveTaskToBack(false);
                        new Login().finish();
                        ttl.ExitBit = 1;
                        new Login().onBackPressed();
                        new SplashScreen().onBackPressed();
                    }
                }
//                    }
                //*if (!bit) {
               /* else {
                        Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                        bit = true;

                    } else {
                        finish();
                        fragmentStack.clear();
                        ed.clear().apply();
                        edvideo.putInt("position", 0).apply();
//                    }
                }*/
                /* Toast.makeText(getApplicationContext(), "fragment null", Toast.LENGTH_SHORT).show();*/
                   /* if (!bit) {
                        Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                        bit = true;
                    } else {
//                    Toast.makeText(getApplicationContext(), "Fragment is null..", Toast.LENGTH_SHORT).show();
                        ed.clear().apply();
                        fragmentStack.clear();
                        edvideo.putInt("position", 0).apply();
                        finish();
                    }*/
            } else {
//                Toast.makeText(getApplicationContext(), "Fragment is null..", Toast.LENGTH_SHORT).show();
               /* int a=fragmentStack.size();
                if (fragmentStack.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();

                   fragmentStack.pop();

                }else
                if (fragmentStack.size()<0){*/


                if (ttl.ExitBit > 0) {
                    if (ttl.ExitBit == 1)
                        Toast.makeText(getApplicationContext(), "Please press once to exit", Toast.LENGTH_SHORT).show();
                    ttl.ExitBit--;
                } else {
//                    Toast.makeText(getApplicationContext(), "bit over..", Toast.LENGTH_SHORT).show();
                   /* ed.clear().apply();
                    ttl.ExitBit = 1;
                    tabBackStack.setTabPosition(0);
                    fragmentStack.clear();
                    chatPrefEditor.clear().apply();
                    edvideo.putInt("position", 0).apply();
                    finish();*/
                    if (getSharedPreferences("loginStatus", MODE_PRIVATE).getString("LoginWay", "").equalsIgnoreCase("FacebookLogin")) {
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager.getInstance().logOut();

                        AccessToken.setCurrentAccessToken(null);
                    }
                    tabBackStack.setTabPosition(0);
                    chatPrefEditor.clear().apply();
                    ed.putInt("position", 0).apply();
                    edvideo.putInt("position", 0).apply();
                    moveTaskToBack(false);
                    new Login().finish();
                    ttl.ExitBit = 1;
                    new Login().onBackPressed();
                    new SplashScreen().onBackPressed();
                    finish();
                }

            }
            /* Toast.makeText(getApplicationContext(), "stack null", Toast.LENGTH_SHORT).show();*/
//            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    public void showFragment(final Fragment fragment1, final String Tag) {
      /*  FragmentTransaction transcation = fragmentManager.beginTransaction();
        transcation.replace(R.id.viewpager, fragment, Tag);
        transcation.addToBackStack(Tag);
        transcation.commit();*/

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = fragment1;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.viewpager, fragment, Tag);
                fragmentStack.push(new Tablayout_with_viewpager());

                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
//        drawer.closeDrawer(GravityCompat.START);

    }

    int id;

    /*  @SuppressWarnings("StatementWithEmptyBody")
      @Override
      public boolean onNavigationItemSelected(MenuItem item) {

          id = item.getItemId();
          if (id == R.id.profile) {

              Tablayout_with_viewpager tablayout_with_viewpager = new Tablayout_with_viewpager(status, roleId);
              showFragment(tablayout_with_viewpager, "");
              // Handle the camera action
          } else if (id == R.id.desired_tutor) {

              DesiredTutor desiredTutor = new DesiredTutor();
              showFragment(desiredTutor, "");

          } else if (id == R.id.payment_options) {
              Earn_Buy_tabLayout buyCredits = new Earn_Buy_tabLayout();
              showFragment(buyCredits, "");

          } else if (id == R.id.lesson_tracking) {
              lessonTracking lessonTracking = new lessonTracking();
              showFragment(lessonTracking, "");

          } else if (id == R.id.TTL_Score) {
              TTL_Score ttl_score = new TTL_Score();
              showFragment(ttl_score, "");

          } else if (id == R.id.History) {
              History history = new History();
              showFragment(history, "");

          } else if (id == R.id.notification) {
              Notification notification = new Notification();
              showFragment(notification, "");

          } else if (id == R.id.support) {
              Support support = new Support();
              showFragment(support, "");

          } else if (id == R.id.sign_out) {

              pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
              editor = pref.edit();
              editor.clear();
              editor.apply();

              Intent i = new Intent(getApplicationContext(), Login.class);
              i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
              finish();

          } else if (id == R.id.daily_game) {
              TutorGuides dailyGame = new TutorGuides();
              showFragment(dailyGame, "");

          }


          return true;
      }*/
    String userChoosenTask;
    final int CAMERA_REQUEST = 1323;
    final int GALLERY_REQUEST = 1342;
    final int CROP_REQUEST = 1352;
    private String uploadURL;
    private Bitmap bitmap;


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingFlyout.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAMERA_REQUEST)
                onCaptureImageResult(data);
            else if (requestCode == CROP_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                TVusericon.setImageBitmap(imageBitmap);


                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                TVusericon.setImageDrawable(roundedBitmapDrawable);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                assert imageBitmap != null;
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                uploadImage(encodedImageString,imageBitmap);

                /*ImageUploadClass imageUploadClass = new ImageUploadClass(encodedImageString, imageBitmap, getApplication(), pref.getInt("id", 0));
                imageUploadClass.execute();*/
                uploadImage(encodedImageString, imageBitmap, getApplication(), pref.getInt("id", 0));
             /*   Uri uri=data.getData();


//                String path=imageToString(imageBitmap);
                Toast.makeText(getApplicationContext(), "path "+path, Toast.LENGTH_SHORT).show();

                Glide.with(getApplicationContext()).load(path)
                        .crossFade()
                        .centerCrop()

                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);*/
            }

        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//                path = getRealPathFromURI(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TVusericon.setImageBitmap(bm);

        Bitmap bb=getResizedBitmap(bm,500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();

        Intent ui=new Intent(getApplicationContext(), Fragment_cropImage.class);
        ui.putExtra("bitmap",byteArray);
        startActivity(ui);

/*        FragmentTransaction f=fragmentManager.beginTransaction();
        f.replace(R.id.viewpager,new Fragment_cropImage(bm)).commit();*/
//        cropImag(data);
//        uploadImage();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TVusericon.setImageBitmap(thumbnail);
//        cropImag(data);


        galleryIntent();
       /* Bitmap bb=getResizedBitmap(thumbnail,500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bytes.toByteArray();


        Intent ui=new Intent(getApplicationContext(), Fragment_cropImage.class);
        ui.putExtra("bitmap",byteArray);
        startActivity(ui);*/
/*
FragmentTransaction f=fragmentManager.beginTransaction();
        f.replace(R.id.viewpager,new Fragment_cropImage(thumbnail)).commit();*/
//        uploadImage();
    }
    private void cropImag(Intent data) {

        Uri uri = data.getData();
        Intent cropIntent = new Intent(Intent.ACTION_VIEW);
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("outputX", 128);
        cropIntent.putExtra("outputY", 128);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CROP_REQUEST);
    }

    /*private void uploadImage(final String encodedImageString, Bitmap bitmap) {


        String uploadURL = "https://www.thetalklist.com/api/profile_pic"*//*?uid=17430"&image="+encodedImageString*//*;
        Log.e("image uploading url", uploadURL);
        Log.e("image uploading url", uploadURL);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, uploadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               *//* if(s.equals("true")){
                    Toast.makeText(getContext(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }*//*
                Toast.makeText(getApplicationContext(), "Some error occurred! " + s, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred  " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", encodedImageString);
                parameters.put("uid", String.valueOf(pref.getInt("id", 0)));
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(request);
    }*/



    public void uploadImage(final String encodedImageString, final Bitmap bitmap, final Context context, final int id) {





        String uploadURL = "https://www.thetalklist.com/api/profile_pic"/*?uid=17430"&image="+encodedImageString*/;
        Log.e("image uploading url", uploadURL);
        Log.e("image uploading url", uploadURL);
        Log.e("encoded image string ", encodedImageString);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);






        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, uploadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               /* if(s.equals("true")){
                    Toast.makeText(getContext(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }*/
//                Toast.makeText(context, "Some error occurred! " + s, Toast.LENGTH_LONG).show();

                LoginService loginService=new LoginService();
                loginService.login(context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),context);

/*                SharedPreferences preferences=context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("pic",)*/

//Intent i=new Intent(SettingFlyout.class,SettingFlyout.class);
//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//startActivity(i);



           /*   Intent i=new Intent();
              i.setAction("finish");
              sendBroadcast(i);*/

//onCreate(null);

/*                Intent refresh = new Intent(, SettingFlyout.class);
                startActivity(refresh);*/
//                SettingFlyout.this.notifyAll();
//                getApplicationContext().finish();
//               ImageView TVusericon = (ImageView) findViewById(R.id.imagesettingflyoutheader);

               /* final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms


                Glide.with(context).load("https://www.thetalklist.com/uploads/images/"+context.getSharedPreferences("loginStatus",MODE_PRIVATE).getString("pic",""))
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);
                    }
                }, 600);*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", encodedImageString);
                parameters.put("uid", String.valueOf(id));
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);

    }



    public void profilePicChange(){
        Toast.makeText(getApplicationContext(), "profile pic change method", Toast.LENGTH_SHORT).show();
        String pic = preferences.getString("pic", "");
        Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getApplicationContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(TVusericon);
    }
}

