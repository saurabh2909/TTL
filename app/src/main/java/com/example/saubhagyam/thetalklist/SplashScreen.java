package com.example.saubhagyam.thetalklist;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.saubhagyam.thetalklist.Config.Config;
import com.example.saubhagyam.thetalklist.util.NotificationUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener {

    private static final String TAG = SplashScreen.class.getSimpleName();
    SplashScreen splashScreen;

    ImageView imageView;
    int x;
    int y;
    int Desty;
    LinearLayout SplashRootView;

   
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    private SharedPreferences permissionStatus;

    final String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;

    Dialog dialog;

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.saubhagyam.thetalklist.R.layout.activity_splash_screen);
        CheckInternetConn checkInternetConn = new CheckInternetConn();
        if (!checkInternetConn.isOnline(getApplicationContext())) {

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent i = new Intent(getApplicationContext(), NoInternetConnection.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);
            }
        }



        permissionStatus = getApplicationContext().getSharedPreferences("permission status", 0);
        checkPermission();


        imageView = (ImageView) findViewById(R.id.splashImg);


    }

    private void checkPermission() {
//        Toast.makeText(getContext(), "Check permission method", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Marshmallow+
//            Toast.makeText(getContext(), "Marshmellow ++", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.RECORD_AUDIO)
                        || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(SplashScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                            Toast.makeText(getApplicationContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(SplashScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                editor.apply();
          /* editor.clear();
            editor.apply();*/


            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        } else {
            //below Marshmallow
//            Toast.makeText(getApplicationContext(), "Marshmellow --", Toast.LENGTH_SHORT).show();
        }
    }

 


    private void proceedAfterPermission() {
//        Toast.makeText(getApplicationContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[3])) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
//                Toast.makeText(getContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    BroadcastReceiver mRegistrationBroadcastReceiver;
    BroadcastReceiver FinishActivityReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            checkPermission();

        } else {
            splashScreen = new SplashScreen();


            pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

            x = imageView.getLeft();
            y = imageView.getTop();
            Desty = x - 400;

            FinishActivityReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (intent.getAction().equals("close.Application"));
                    finish();
                }
            };

            


            editor = pref.edit();
            editor.putBoolean("logSta", false);
            editor.apply();

/*            progressDialog = new ProgressDialog(SplashScreen.this*//*, R.style.AppCompatAlertDialogStyle*//*);
        progressDialog.setContentView(R.layout.threedotprogressbar);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading.........");
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));*/
            dialog = new Dialog(SplashScreen.this);
            dialog.setContentView(R.layout.threedotprogressbar);
            dialog.setCanceledOnTouchOutside(false);

            SplashRootView = (LinearLayout) findViewById(R.id.SplashRootView);

           /* TranslateAnimation animation = new TranslateAnimation(0, 0, 0, Desty);
            animation.setDuration(1000);
            animation.setFillAfter(false);
            animation.setAnimationListener(this);

            imageView.startAnimation(animation);*/

            if (pref.contains("user") && pref.getString("LoginWay", "").equals("InternalLogin")) {


                URL = "https://www.thetalklist.com/api/login?email=" + pref.getString("email", "") + "&password=" + pref.getString("pass", "");
//            new myLoginData().execute();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//                String URL = "https://www.thetalklist.com/api/login?email=" + pref.getString("user", "") + "&password=" + pref.getString("pass", "");
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);

                        UserData data = UserData.getInstance();
                        data.setLoginServResponse(response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            Log.e("login response ", response);

                            int status = (int) jsonObject.get("status");
                            if (status == 1) {
                                String Err = (String) jsonObject.get("error");
                                Toast.makeText(getApplicationContext(), Err, Toast.LENGTH_SHORT).show();
                                Login login=new Login();
                                Intent i = new Intent(getApplicationContext(), login.getClass());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                editor.clear();
                                editor.apply();
//                            splashScreen.onDestroy();
                                startActivity(i);
                                if (dialog!=null)
                                    dialog.dismiss();

                            }
                            if (status == 0) {

                                JSONObject resultObj = (JSONObject) jsonObject.get("result");
                                int roleId = resultObj.getInt("roleId");
                                String UserName = (String) resultObj.get("username");
//                            editor.clear().apply();
                                editor.putString("loginResponse", response);
                                editor.putString("user", UserName);
                                editor.putInt("roleId", roleId);
                                editor.putBoolean("logSta", true);
                                editor.putInt("userId", resultObj.getInt("id"));
                                editor.putString("credit_balance", resultObj.getString("credit_balance"));
                                editor.putString("usernm", resultObj.getString("usernm"));
                                editor.putString("pic", resultObj.getString("pic"));
                                editor.putString("firstName", resultObj.getString("firstName"));
                                editor.putString("lastName", resultObj.getString("lastName"));
                                editor.putString("city", resultObj.getString("city"));
                                editor.putString("nativeLanguage", resultObj.getString("nativeLanguage"));
                                editor.putString("otherLanguage", resultObj.getString("otherLanguage"));
                                editor.putInt("id", resultObj.getInt("id"));
                                editor.putInt("gender", resultObj.getInt("gender"));
                                editor.putInt("country", resultObj.getInt("country"));
                                editor.putInt("province", resultObj.getInt("province"));
                                editor.putString("cell", resultObj.getString("cell"));
                                editor.putFloat("hRate", Float.parseFloat(resultObj.getString("hRate")));
                                if (resultObj.getString("avgRate").equals(""))
                                    editor.putFloat("avgRate", 0.0f);
                                else
                                    editor.putFloat("avgRate", Float.parseFloat(resultObj.getString("avgRate")));

                                if (resultObj.getString("ttl_points").equals(""))
                                    editor.putFloat("ttl_points", 0.0f);
                                else
                                    editor.putFloat("ttl_points", Float.parseFloat(resultObj.getString("ttl_points")));
                                editor.putInt("status", 0);
                                editor.putFloat("money", Float.parseFloat(resultObj.getString("money")));
                                editor.putString("email", resultObj.getString("email"));
                                editor.apply();
                                Log.e("result obj", resultObj.toString());

                                SharedPreferences sharedPreferences = getSharedPreferences("roleAndStatus", 0);
                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.putInt("roleId", roleId);
                                editor1.apply();


//                            new firebase_regId_store().execute();


                                Intent i = new Intent(getApplicationContext(), SettingFlyout/*navigationDrawer*/.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("status", 0);
                                i.putExtra("roleId", roleId);
                                i.putExtra("username", UserName);
                                startActivity(i);
                                if (dialog!=null)
                                    dialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

                        String URL="https://www.thetalklist.com/api/signout?uid="+pref.getInt("id",0);
                        StringRequest sr1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "status "+response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "status "+error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Volley.newRequestQueue(getApplicationContext()).add(sr1);




                        Log.d("error", error.toString());
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                queue.add(sr);
            } else if (pref.getString("LoginWay", "").equals("FacebookLogin")) {

                // Code here when the LoginWay is FacebookLogin
                SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                final String url = "https://www.thetalklist.com/api/fblogin?email=" + pref.getString("email", "") + "&facebook_id=" + pref.getInt("facebook_id", 0) + "&firstname=" + pref.getString("first_name", "") + "&lastname=" + pref.getString("last_name", "") + "&gender=" + pref.getString("gender", "") + "&birthdate=" + pref.getString("birthday", "");


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

/*
                    editor.putInt("roleId",roleId);
                    editor.putInt("status",0);
                    Log.e("facebook login url", url);
                    Log.e("facebook login response", response);
//                    Toast.makeText(SplashScreen.this, "Login Sucessfully..!", Toast.LENGTH_SHORT).show();

//                    Snackbar.make(R.id.viewpager, "Login Sucessfully..!", 1000);
                    SettingFlyout settingFlyout = new SettingFlyout();
                    Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);*/

                        try {
                            JSONObject obj=new JSONObject(response);
                            if (obj.getInt("status")==0) {
                                JSONObject resObj=obj.getJSONObject("result");
                                SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                final int roleId = resObj.getInt("roleId");
                                editor.putString("LoginWay", "FacebookLogin");
                                editor.putString("loginResponse", response);
                                editor.putString("email", resObj.getString("username"));
                                editor.putInt("facebook_id", resObj.getInt("facebook_id"));
                                editor.putInt("id", resObj.getInt("id"));
                                editor.putInt("gender", resObj.getInt("gender"));
                                editor.putInt("country", resObj.getInt("country"));
                                editor.putInt("province", resObj.getInt("province"));
                                editor.putString("cell", resObj.getString("cell"));
                                editor.putString("city", resObj.getString("city"));
                                editor.putFloat("hRate", Float.parseFloat(resObj.getString("hRate")));
                                if (resObj.getString("avgRate").equals(""))
                                    editor.putFloat("avgRate", 0.0f);
                                else
                                    editor.putFloat("avgRate", Float.parseFloat(resObj.getString("avgRate")));

                                if (resObj.getString("ttl_points").equals(""))
                                    editor.putFloat("ttl_points", 0.0f);
                                else
                                    editor.putFloat("ttl_points", Float.parseFloat(resObj.getString("ttl_points")));
                                editor.putString("nativeLanguage", resObj.getString("nativeLanguage"));
                                editor.putString("otherLanguage", resObj.getString("otherLanguage"));
                                editor.putInt("roleId",roleId);
                                editor.putInt("status",0);
                            /*editor.putString("firstname", resObj.getString("firstName"));
                            editor.putString("lastname", resObj.getString("lastName"));
                            editor.putString("gender", resObj.getString("fb_gender"));
                            editor.putString("birthdate", resObj.getString("fb_birthday"));*/
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Login Sucessfully..!", Toast.LENGTH_SHORT).show();
                                SettingFlyout settingFlyout = new SettingFlyout();
                                Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SplashScreen.this, "Login Unsucessful..!", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(stringRequest);

            } else {
                Intent i = new Intent(getApplicationContext(), Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }


            // register GCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));

            LocalBroadcastManager.getInstance(this).registerReceiver(FinishActivityReceiver,
                    new IntentFilter("close.Application"));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.PUSH_NOTIFICATION));

            // clear the notification area when the app is opened
            NotificationUtils.clearNotifications(getApplicationContext());
        }
    }

    String URL;

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(FinishActivityReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

       /* if (dialog!=null)
        dialog.show();*/

//        int[] locationOnScreent = new int[2];
//        imageView.getLocationOnScreen(locationOnScreent);
//
//        progressDialog.show();
      /*  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = Desty - 100;
        lp.leftMargin = x;
        imageView.setLayoutParams(lp);*/

       /* if (pref.contains("user") && pref.getString("LoginWay", "").equals("InternalLogin")) {


            URL = "https://www.thetalklist.com/api/login?email=" + pref.getString("email", "") + "&password=" + pref.getString("pass", "");
//            new myLoginData().execute();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//                String URL = "https://www.thetalklist.com/api/login?email=" + pref.getString("user", "") + "&password=" + pref.getString("pass", "");
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);

                    UserData data = UserData.getInstance();
                    data.setLoginServResponse(response);


                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        Log.e("login response ", response);

                        int status = (int) jsonObject.get("status");
                        if (status == 1) {
                            String Err = (String) jsonObject.get("error");
                            Toast.makeText(getApplicationContext(), Err, Toast.LENGTH_SHORT).show();
                            Login login=new Login();
                            Intent i = new Intent(getApplicationContext(), login.getClass());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            editor.clear();
                            editor.apply();
//                            splashScreen.onDestroy();
                            startActivity(i);
                            if (dialog!=null)
                            dialog.dismiss();

                        }
                        if (status == 0) {

                            JSONObject resultObj = (JSONObject) jsonObject.get("result");
                            int roleId = resultObj.getInt("roleId");
                            String UserName = (String) resultObj.get("username");
//                            editor.clear().apply();
                            editor.putString("loginResponse", response);
                            editor.putString("user", UserName);
                            editor.putInt("roleId", roleId);
                            editor.putBoolean("logSta", true);
                            editor.putInt("userId", resultObj.getInt("id"));
                            editor.putString("credit_balance", resultObj.getString("credit_balance"));
                            editor.putString("usernm", resultObj.getString("usernm"));
                            editor.putString("pic", resultObj.getString("pic"));
                            editor.putString("firstName", resultObj.getString("firstName"));
                            editor.putString("lastName", resultObj.getString("lastName"));
                            editor.putString("city", resultObj.getString("city"));
                            editor.putString("nativeLanguage", resultObj.getString("nativeLanguage"));
                            editor.putString("otherLanguage", resultObj.getString("otherLanguage"));
                            editor.putInt("id", resultObj.getInt("id"));
                            editor.putInt("gender", resultObj.getInt("gender"));
                            editor.putInt("country", resultObj.getInt("country"));
                            editor.putInt("province", resultObj.getInt("province"));
                            editor.putString("cell", resultObj.getString("cell"));
                            editor.putFloat("hRate", Float.parseFloat(resultObj.getString("hRate")));
                            if (resultObj.getString("avgRate").equals(""))
                                editor.putFloat("avgRate", 0.0f);
                            else
                                editor.putFloat("avgRate", Float.parseFloat(resultObj.getString("avgRate")));
                            editor.putInt("status", 0);
                            editor.putFloat("money", Float.parseFloat(resultObj.getString("money")));
                            editor.putString("email", resultObj.getString("email"));
                            editor.apply();
                            Log.e("result obj", resultObj.toString());

                            SharedPreferences sharedPreferences = getSharedPreferences("roleAndStatus", 0);
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putInt("roleId", roleId);
                            editor1.apply();


//                            new firebase_regId_store().execute();


                            Intent i = new Intent(getApplicationContext(), SettingFlyout*//*navigationDrawer*//*.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("status", 0);
                            i.putExtra("roleId", roleId);
                            i.putExtra("username", UserName);
                            startActivity(i);
                            if (dialog!=null)
                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

                    String URL="https://www.thetalklist.com/api/signout?uid="+pref.getInt("id",0);
                    StringRequest sr1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "status "+response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "status "+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(sr1);




                    Log.d("error", error.toString());
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    dialog.dismiss();
                }
            });
            queue.add(sr);
        } else if (pref.getString("LoginWay", "").equals("FacebookLogin")) {

            // Code here when the LoginWay is FacebookLogin
            SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            final String url = "https://www.thetalklist.com/api/fblogin?email=" + pref.getString("email", "") + "&facebook_id=" + pref.getInt("facebook_id", 0) + "&firstname=" + pref.getString("first_name", "") + "&lastname=" + pref.getString("last_name", "") + "&gender=" + pref.getString("gender", "") + "&birthdate=" + pref.getString("birthday", "");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

*//*
                    editor.putInt("roleId",roleId);
                    editor.putInt("status",0);
                    Log.e("facebook login url", url);
                    Log.e("facebook login response", response);
//                    Toast.makeText(SplashScreen.this, "Login Sucessfully..!", Toast.LENGTH_SHORT).show();

//                    Snackbar.make(R.id.viewpager, "Login Sucessfully..!", 1000);
                    SettingFlyout settingFlyout = new SettingFlyout();
                    Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);*//*

                    try {
                        JSONObject obj=new JSONObject(response);
                        if (obj.getInt("status")==0) {
                            JSONObject resObj=obj.getJSONObject("result");
                            SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            final int roleId = resObj.getInt("roleId");
                            editor.putString("LoginWay", "FacebookLogin");
                            editor.putString("loginResponse", response);
                            editor.putString("email", resObj.getString("username"));
                            editor.putInt("facebook_id", resObj.getInt("facebook_id"));
                            editor.putInt("id", resObj.getInt("id"));
                            editor.putInt("gender", resObj.getInt("gender"));
                            editor.putInt("country", resObj.getInt("country"));
                            editor.putInt("province", resObj.getInt("province"));
                            editor.putString("cell", resObj.getString("cell"));
                            editor.putString("city", resObj.getString("city"));
                            editor.putFloat("hRate", Float.parseFloat(resObj.getString("hRate")));
                            if (resObj.getString("avgRate").equals(""))
                                editor.putFloat("avgRate", 0.0f);
                            else
                                editor.putFloat("avgRate", Float.parseFloat(resObj.getString("avgRate")));
                            editor.putString("nativeLanguage", resObj.getString("nativeLanguage"));
                            editor.putString("otherLanguage", resObj.getString("otherLanguage"));
                            editor.putInt("roleId",roleId);
                            editor.putInt("status",0);
                            *//*editor.putString("firstname", resObj.getString("firstName"));
                            editor.putString("lastname", resObj.getString("lastName"));
                            editor.putString("gender", resObj.getString("fb_gender"));
                            editor.putString("birthdate", resObj.getString("fb_birthday"));*//*
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "Login Sucessfully..!", Toast.LENGTH_SHORT).show();
                            SettingFlyout settingFlyout = new SettingFlyout();
                            Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SplashScreen.this, "Login Unsucessful..!", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(stringRequest);

        } else {
            Intent i = new Intent(getApplicationContext(), Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }*/
    }


    @Override
    public void onAnimationRepeat(Animation animation) {

    }


   


}
