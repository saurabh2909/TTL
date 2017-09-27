package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.OpenTokConfig;
import com.example.saubhagyam.thetalklist.R;
import com.example.saubhagyam.thetalklist.WebServiceCoordinator;
import com.opentok.android.Connection;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.SubscriberKit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class New_videocall_activity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        WebServiceCoordinator.Listener,
        Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener {

    private static final String LOG_TAG = New_videocall_activity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")
    private WebServiceCoordinator mWebServiceCoordinator;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        // initialize view objects from your layout
        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);

        requestPermissions();
    }

     /* Activity lifecycle methods */

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }

    }


    SharedPreferences preferences, pref;
    SharedPreferences.Editor editor;

    FrameLayout videoCallRootLayout;
    FrameLayout outgoingCallRootLayout;

    View videocontrols;
    ViewGroup parent;
    ImageView /*btn_mute,*/ btn_cutcall/*, btn_speakers*/;
    FrameLayout surfaceView;

    TextView callerName;
    ImageView callerImg;
    Intent i;

    RequestQueue queue111, queue222;
    String CallerName, CallerPic;


    ImageView callEnd;
    ImageView callMute;
    ImageView callChangeCamera;
    Timer t;

    int cutByUser;
    int TimeCount;

    public void connectionApiCall(String URL) {
        RequestQueue queue333 = Volley.newRequestQueue(getApplicationContext());


        Log.e("firebase reject Call", URL);
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("Call_activity_reject_ca", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue333.add(sr);
    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }

        preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
        pref = getSharedPreferences("loginStatus", MODE_PRIVATE);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
        outgoingCallRootLayout = (FrameLayout) findViewById(R.id.outgoingCallRootLayout);
        videoCallRootLayout = (FrameLayout) findViewById(R.id.videoCallRootLayout);
        videocontrols = findViewById(R.id.videocontrols1);
        parent = (ViewGroup) videocontrols.getParent();


        videoCallRootLayout.setVisibility(View.GONE);
        btn_cutcall = (ImageView)

                findViewById(R.id.outgoing_cutcall);

        callerName = (TextView) outgoingCallRootLayout.findViewById(R.id.callerName);
        callerImg = (ImageView) outgoingCallRootLayout.findViewById(R.id.callerImg);

        surfaceView = (FrameLayout)

                findViewById(R.id.outgoingCallSurfaceView);


        final WebServiceCoordinator mWebServiceCoordinator = new WebServiceCoordinator(this, this);
        i = getIntent();


        if (i.getStringExtra("from").

                equalsIgnoreCase("callActivity"))

        {
            outgoingCallRootLayout.setVisibility(View.GONE);
            videoCallRootLayout.setVisibility(View.VISIBLE);


            mWebServiceCoordinator.fetchSessionConnectionData();
        } else

        {
            queue111 = Volley.newRequestQueue(getApplicationContext());

            final SharedPreferences preferences = getApplicationContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);


            String URL = "https://www.thetalklist.com/api/firebase_call?sender_id=" + pref.getInt("id", 0) + "&receiver_id=" + preferences.getInt("tutorId", 0);
            Log.e("firebase Call url", URL);
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int status = jsonObject.getInt("status");

                        Log.e("video call api response", response);
                        if (status == 0) {


                            int cid = jsonObject.getInt("cid");

                            editor = preferences.edit();
                            editor.putInt("classId", cid).apply();

                            JSONObject object = jsonObject.getJSONObject("detail");
                            mWebServiceCoordinator.fetchSessionConnectionData();


                            CallerName = object.getString("firstName");
                            callerName.setText(object.getString("firstName"));
                            CallerPic = object.getString("pic");

                            if (!CallerPic.equals("")) {
                                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + CallerPic)
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                                        .placeholder(R.drawable.process)
                                        .error(R.drawable.black_person)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(callerImg);
                            } else {
                                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(callerImg);
                            }


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
            btn_cutcall.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    if (mSession != null) {
                        mSession.disconnect();
                        onDisconnected(mSession);
                    }


                    queue222 = Volley.newRequestQueue(getApplicationContext());
                    SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
                    SharedPreferences pref = getSharedPreferences("loginStatus", MODE_PRIVATE);


                    String URL = "https://www.thetalklist.com/api/firebase_rejectcall?sender_id=" + pref.getInt("id", 0) + "&receiver_id=" + preferences.getInt("tutorId", 0) + "&cid=" + preferences.getInt("classId", 0);
                    Log.e("firebase reject Call", URL);
                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue222.add(sr);

//                onBackPressed();


                }
            });

            callEnd = (ImageView) findViewById(R.id.callend);

            callMute = (ImageView) findViewById(R.id.callmute);

            callChangeCamera = (ImageView) findViewById(R.id.callchangecamera);

           /* videocontrols1 = (LinearLayout)

                    findViewById(R.id.videocontrols1);*/

            mSubscriberViewContainer.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    if (layoutVisibilityBit == 1) {
                        videocontrols.setVisibility(View.VISIBLE);
                        LayoputVisibility();
                    }
                }
            });





            requestPermissions();

            callChangeCamera.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    mPublisher.cycleCamera();
                }
            });

            callEnd.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    cutByUser=1;
                    onDisconnected(mSession);
                    mSession.disconnect();


                    if (cutByUser==0){

                    }else {

                    }
                    String URL="https://www.thetalklist.com/api/veesession_disconnect?cid="+ preferences.getInt("classId", 0);
                    connectionApiCall(URL);
                    if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
                        if (t != null)

                            t.cancel();
//                        Toast.makeText(getApplicationContext(), "total time " + TimeCount, Toast.LENGTH_SHORT).show();



                        String URL2 = "https://www.thetalklist.com/api/total_cost?cid=" + preferences.getInt("classId", 0) + "&amount=" + getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE).getFloat("hRate", 0.0f) + "&time=" + TimeCount;

//            mSession.unpublish(mPublisher);
//            mSession.disconnect();
            /*  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSession != null)
                            mSession.disconnect();
//                        mCamera.release();
                        finish();
                    }
                }, 5000);*/

                        StringRequest sr = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                Log.e("total cost response",response);

                                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.videocall_hangup_popup, null);
                                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_video_call, null);

                                final PopupWindow popupWindow = new PopupWindow(view,340,470, true);
//                    final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
                     /*   popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(false);

                        Button btn= (Button) view.findViewById(R.id.videocall_hangup_popup_okbtn);
                        TextView videocall_handup_tutorName= (TextView) view.findViewById(R.id.videocall_handup_tutorName);
                        videocall_handup_tutorName.setText(getSharedPreferences("videoCallTutorDetails",Context.MODE_PRIVATE).getString("tutorName",""));
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                                Toast.makeText(VideoCall.this, "total coast " + response, Toast.LENGTH_SHORT).show();
                                Log.e("total coast ", response);
                                Intent i=new Intent(getApplicationContext(),new StudentFeedBack().getClass());
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });*/
                    /*Intent i=new Intent(getApplicationContext(),new StudentFeedBack().getClass());
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);*/


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        Volley.newRequestQueue(getApplicationContext()).add(sr);
                    }







               /* View videocontrols = findViewById(R.id.videocontrols1);
                ViewGroup parent = (ViewGroup) videocontrols.getParent();
                parent.removeView(videocontrols);
                View videocontrols1 = (LinearLayout) getLayoutInflater().inflate(R.layout.videocontrol2, parent, false);
                parent.addView(videocontrols1);*/

             /*   LinearLayout videoControls1 = (LinearLayout) findViewById(R.id.videocontrols1);
                videoControls1.setVisibility(View.GONE);
                LinearLayout videoControls2 = (LinearLayout) findViewById(R.id.videocontrols2);
                videoControls2.setVisibility(View.VISIBLE);*/

              /*  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        *//*if (mSession != null)
                            mSession.disconnect();*//*
                        finish();
                    }
                }, 5000);*/
//               Endcall();
                }
            });

            ImageView callendMessage = (ImageView) findViewById(R.id.callendMessage);
            callendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), "Call end button clicked", Toast.LENGTH_SHORT).show();
                }
            });
            callMute.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    if (!audioManager.isMicrophoneMute()) {
                        audioManager.setMicrophoneMute(true);
                        callMute.setImageResource(R.drawable.mute);

                    } else {
                        callMute.setImageResource(R.drawable.unmute);
                        audioManager.setMicrophoneMute(false);
                    }
                }
            });
        }



    int layoutVisibilityBit;
    public void LayoputVisibility() {
          /*To make the layout invisible after 3 sec and when it touch the main layout it will again visible.*/


        if (layoutVisibilityBit == 0) {


            layoutVisibilityBit = 0;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setRationale(getString(R.string.rationale_ask_again))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // if there is no server URL set

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    /* Web Service Coordinator delegate methods */

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "ApiKey: " + apiKey + " SessionId: " + sessionId + " Token: " + token);
     /*   Log.e("videocall act sessionId", sessionId);
        Log.e("videocall act token", token);
        Log.e("videocall act api key", apiKey);
        mApiKey = apiKey;
        mSessionId = sessionId;
        mToken = token;*/

        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
//        Toast.makeText(this, "Web Service error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        finish();

    }

    @Override
    public void onReconnecting(Session session) {

    }

    @Override
    public void changedCamera(Publisher mPublisher, int newCameraId) {

    }


    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: " + session.getSessionId());

        // initialize Publisher and set this object to listen to Publisher events
        /*mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        // set publisher video style to fill view
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
//        mPublisherViewContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);*/


        mPublisher = new Publisher(this);
        mPublisher.setPublisherListener(this);
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);

        Log.e("initializePublisher", "publisher initialization method");
        mSession.publish(mPublisher);

        mSession.setConnectionListener(new Session.ConnectionListener() {
            @Override
            public void onConnectionCreated(Session session, Connection connection) {

            }

            @Override
            public void onConnectionDestroyed(Session session, Connection connection) {


                View videocontrols = findViewById(R.id.videocontrols1);
                ViewGroup parent = (ViewGroup) videocontrols.getParent();
                parent.removeView(videocontrols);
                View videocontrols1 = (LinearLayout) getLayoutInflater().inflate(R.layout.videocontrol2, parent, false);
                parent.addView(videocontrols1);

                mPublisher=null;
                mSubscriber=null;

//                Toast.makeText(getApplicationContext(), "msession disconnect external listener ", Toast.LENGTH_SHORT).show();
                if (i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mSession != null)
                                mSession.disconnect();
//                            finish();
                            Intent i=new Intent(getApplicationContext(),SettingFlyout.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }, 5000);
                }
                else {
                    onDisconnected(mSession);
//                    callEnd.performClick();
                   /* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mSession != null)
                                mSession.disconnect();
                            finish();
                        }
                    }, 5000);*/
                }
            }
        });
    }

    @Override
    public void onDisconnected(Session session) {

        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
        if (!i.getStringExtra("from").equalsIgnoreCase("callActivity") && TimeCount>0) {
            Intent i = new Intent(getApplicationContext(), StudentFeedBack.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
//            finish();
            onBackPressed();
        }
    }
    int minute;
    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber == null) {


            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);

            outgoingCallRootLayout.setVisibility(View.GONE);
            videoCallRootLayout.setVisibility(View.VISIBLE);

            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if (!audioManager.isMicrophoneMute()) {
                audioManager.setMicrophoneMute(true);

            } else {
                audioManager.setMicrophoneMute(false);
            }


            String Url = "https://www.thetalklist.com/api/veesession_connect?cid=" + preferences.getInt("classId", 0);
            connectionApiCall(Url);

            if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {

                final Float money = getApplicationContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getFloat("money", 0.0f);
                SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
                Float creditPerMinute = preferences.getFloat("credit", 0.0f);

                if (money < 10) {

                    minute = (int) (money / creditPerMinute);

                }

                t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                            myTextView.setText("count="+count);
                                TimeCount++;
                                if (money < 10) {
                                    if (TimeCount == minute * 60) {
                                        t.cancel();
//                                        mSession.disconnect();
                                        onDisconnected(mSession);
                                      /*  mPublisherViewContainer.removeAllViews();
                                        mSubscriberViewContainer.removeAllViews();*/
                                        SettingFlyout settingFlyout = new SettingFlyout();
//                                        settingFlyout.setFragmentByVideoCall(new Earn_Buy_tabLayout());
                                        Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);

                                        if ((minute * 60 - TimeCount) == 60) {
                                            Toast.makeText(New_videocall_activity.this, "Sorry, student is out of credits. Call will end in 60 secs.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }
                }, 000, 1000);

            }

            surfaceView.removeAllViews();
            mPublisherViewContainer.removeAllViews();
            mPublisherViewContainer.addView(mPublisher.getView());
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());

        showOpenTokError(opentokError);
    }

    /* Publisher Listener methods */

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    private void showOpenTokError(OpentokError opentokError) {

//        Toast.makeText(this, opentokError.getErrorDomain().name() + ": " + opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        New_videocall_activity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}