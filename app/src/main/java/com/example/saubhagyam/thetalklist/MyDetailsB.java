package com.example.saubhagyam.thetalklist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.Adapter.CountrySpinnerAdapter;
import com.example.saubhagyam.thetalklist.Adapter.Spinner_adapter;
import com.example.saubhagyam.thetalklist.Bean.CountryModel;
import com.example.saubhagyam.thetalklist.Services.CountryService;
import com.example.saubhagyam.thetalklist.Services.LoginService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class MyDetailsB extends Fragment {
    int backbtn = 0;
    PopupWindow popupWindow;

    ArrayAdapter stateAdapter;
    ArrayAdapter countryAdapter;
    SharedPreferences pref111;
    ArrayList<String> states;
    SharedPreferences.Editor editor111;
    public MyDetailsB() {
    }
     EditText fname, lname, age,  city, email, phone, password;

     Spinner state,gender, country, lang1, lang2;
     ImageView imageView1,imagesettingflyoutheader;
     TextView myDetailsB_info_txt,MydetailsB_typeTalkist,MydetailsB_tutorName;
    Button submitButtonNotmyDetails;
    View convertview;

    int gen1111;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the studentlayout for this fragment
         convertview = inflater.inflate(R.layout.fragment_my_details_b, container, false);
        pref111=getActivity().getSharedPreferences("loginStatus",Context.MODE_PRIVATE);
        editor111=pref111.edit();
        Intent i = getActivity().getIntent();
        int status = 0;

        status = pref111.getInt("status", 1);
//        status = i.getIntExtra("status", 0);





        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fname = (EditText) convertview.findViewById(R.id.fname);
        lname = (EditText) convertview.findViewById(R.id.lname);
        age = (EditText) convertview.findViewById(R.id.age);
        state = (Spinner) convertview.findViewById(R.id.state);
        city = (EditText) convertview.findViewById(R.id.city);
        email = (EditText) convertview.findViewById(R.id.email);
        email.setFocusable(false);
        phone = (EditText) convertview.findViewById(R.id.phone);
//        password = (EditText) convertview.findViewById(R.id.password);
        lang1 = (Spinner) convertview.findViewById(R.id.lang1MyDetails);
        lang2 = (Spinner) convertview.findViewById(R.id.lang2MyDetails);
        gender = (Spinner) convertview.findViewById(R.id.genderMyDetails);
        country = (Spinner) convertview.findViewById(R.id.countryMyDetails);
        imageView1= (ImageView) convertview.findViewById(R.id.imageView1);
        imagesettingflyoutheader=(ImageView)getActivity().findViewById(R.id.imagesettingflyoutheader);
        myDetailsB_info_txt= (TextView) convertview.findViewById(R.id.myDetailsB_info_txt);
        MydetailsB_typeTalkist= (TextView) convertview.findViewById(R.id.MydetailsB_typeTalkist);
        MydetailsB_tutorName= (TextView) convertview.findViewById(R.id.MydetailsB_tutorName);

        MydetailsB_tutorName.setText(pref111.getString("usernm",""));
        if (pref111.getInt("roleId",0)==1)
        MydetailsB_typeTalkist.setText("Bronze Talk-ist");
        if (pref111.getInt("roleId",0)==2)
            MydetailsB_typeTalkist.setText("Silver Talk-ist");
        if (pref111.getInt("roleId",0)==3)
            MydetailsB_typeTalkist.setText("Gold Talk-ist");
        if (pref111.getInt("roleId",0)==0)
            MydetailsB_typeTalkist.setText("Student Talk-ist");


        email.setText(pref111.getString("email",""));
        city.setText(pref111.getString("city",""));

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (country.getSelectedItem().toString().equalsIgnoreCase("USA")) {
                    ((LinearLayout) convertview.findViewById(R.id.lls6)).setVisibility(View.VISIBLE);
//                    state.setVisibility(View.VISIBLE);
                } else ((LinearLayout)convertview.findViewById(R.id.lls6)).setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SharedPreferences pref=getApplicationContext().getSharedPreferences("firstTime", Context.MODE_PRIVATE);
        SharedPreferences loginpref=getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed=pref.edit();
        if (!pref.contains("firstTime")){
            myDetailsB_info_txt.setVisibility(View.VISIBLE);
            ed.putBoolean("firstTime",false);
            ed.apply();
        }
        if (loginpref.getString("pic","").equals("")) {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView1);
        }else {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/"+loginpref.getString("pic",""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView1);
        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        submitButtonNotmyDetails = (Button) convertview.findViewById(R.id.submitButtonNotmyDetails);



        String gen[] = getResources().getStringArray(R.array.Gender);
        final ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, gen);
        gender.setAdapter(genderAdapter);

        if (pref111.getInt("gender",0)==0)
            gender.setSelection(genderAdapter.getPosition("Female"));
        else gender.setSelection(genderAdapter.getPosition("Male"));


       /* Spinner_adapter spinner_adapter=new Spinner_adapter(getContext(),fname.,gen);
        gender.setAdapter(spinner_adapter);*/

        String languages[] = getResources().getStringArray(R.array.Language);
        ArrayAdapter langAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, languages);
        lang1.setAdapter(langAdapter);
        lang1.setSelection(langAdapter.getPosition(pref111.getString("nativeLanguage","")));
        String languages1[] = getResources().getStringArray(R.array.Language1);
        final ArrayAdapter langAdapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, languages1);
        lang2.setAdapter(langAdapter1);
        lang2.setSelection(langAdapter1.getPosition(pref111.getString("otherLanguage","")));

        {
            RequestQueue queue1 = Volley.newRequestQueue(getContext());
            String URL = "https://www.thetalklist.com/api/countries";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("countries");
                        ArrayList<String> coun = new ArrayList<>();
                        coun.add("Select Country");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("country"));


                        }


                        if (getActivity() != null) {
                            countryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                            country.setAdapter(countryAdapter);

                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);

                                int selectedPos=data.getInt("id");
                                if (selectedPos==pref111.getInt("country",0)){
                                    country.setSelection(countryAdapter.getPosition(data.getString("country")));
                                }
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            queue1.add(getRequest);

           /* CountryService countryService=new CountryService();
            List<CountryModel> coun=countryService.countryServiceCall(getContext());
            CountrySpinnerAdapter countrySpinnerAdapter=new CountrySpinnerAdapter();*/
        }

        {
            RequestQueue queue1 = Volley.newRequestQueue(getContext());
            String URL = "https://www.thetalklist.com/api/states";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("states");
                        states= new ArrayList<>();
                        states.add("Select state");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            states.add(data.getString("provice"));
                        }
                        if (getActivity() != null) {
                            stateAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, states);

                            state.setAdapter(stateAdapter);
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);

                                int selectedPos=data.getInt("id");
                                if (selectedPos==pref111.getInt("province",0)){
                                    state.setSelection(stateAdapter.getPosition(data.getString("provice")));
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            queue1.add(getRequest);
            SharedPreferences preferences1=getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE);

            fname.setText(preferences1.getString("firstName",""));
            lname.setText(preferences1.getString("lastName",""));
            age.setText(String.valueOf(preferences1.getInt("age",0)));
            phone.setText(preferences1.getString("cell",""));
            city.setText(preferences1.getString("city",""));
            lang1.setSelection(langAdapter1.getPosition(preferences1.getString("nativeLanguage","")));
            lang2.setSelection(langAdapter1.getPosition(preferences1.getString("otherLanguage","")));
            if (preferences1.getInt("gender",0)==0)
            gender.setSelection(genderAdapter.getPosition("Female"));
            else gender.setSelection(genderAdapter.getPosition("Male"));

           /* CountryService countryService=new CountryService();
            List<CountryModel> coun=countryService.countryServiceCall(getContext());
            CountrySpinnerAdapter countrySpinnerAdapter=new CountrySpinnerAdapter();*/
        }


     /*   String URL1 = "https://www.thetalklist.com/api/login?email=" +  preferences1.getString("email","")+ "&password=" + preferences1.getString("pass","");


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        final StringRequest sr = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
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
                        Toast.makeText(getContext(), Err, Toast.LENGTH_SHORT).show();

                    } else {


                        JSONObject resultObj = (JSONObject) jsonObject.get("result");
                        int roleId = resultObj.getInt("roleId");
                        String UserName = (String) resultObj.get("username");
                        int userId = resultObj.getInt("id");
                        String mail = resultObj.getString("email");

                        fname.setText(resultObj.getString("firstName"));
                        lname.setText(resultObj.getString("lastName"));
                        age.setText(String.valueOf(resultObj.getInt("age")));
                        phone.setText(String.valueOf(resultObj.getInt("cell")));
                        city.setText(resultObj.getString("city"));
                        lang1.setSelection(langAdapter1.getPosition(resultObj.getString("nativeLanguage")));
                        lang2.setSelection(langAdapter1.getPosition(resultObj.getString("otherLanguage")));
                        gender.setSelection(genderAdapter.getPosition(resultObj.getString("gender")));

                      *//*  state.setSelection(stateAdapter.getPosition(resultObj.getString("province")));
                        country.setSelection(countryAdapter.getPosition(resultObj.getString("country")));*//*


                       *//* editor.putString("LoginWay", "InternalLogin");
                        editor.putString("loginResponse", response);
                        editor.putString("user", UserName);
                        editor.putString("pass", pass);
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
                        editor.putInt("gender", resultObj.getInt("gender"));
                        editor.putInt("country", resultObj.getInt("country"));
                        editor.putFloat("money", 0.0f);
                        editor.putString("email", mail);
                        editor.apply();*//*


                      *//*  Intent i = new Intent(getApplicationContext(), SettingFlyout.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("status", 0);
                        i.putExtra("roleId", roleId);


                        i.putExtra("username", UserName);
                        startActivity(i);*//*
                    }
                } catch(JSONException e){
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);*/



        SharedPreferences preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        String response = preferences.getString("loginResponse", "No Response");

        View view = inflater.inflate(R.layout.popupscreen, null);
        View view1 = inflater.inflate(R.layout.tab_layout_and_viewpager, null);







        if (popupWindow == null)

            if (!(status == 0)) {

                if (popupWindow == null) {


                    popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
                    popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                            if (backbtn == 0) {
                                getActivity().finish();
                                getActivity().moveTaskToBack(true);
                                getActivity().getApplication().onTerminate();
                            }


                        }
                    });
                } /*else*/
//                    backbuttonEvent();

                SessionManager session = new SessionManager(getActivity());

                if (session.isLoggedIn()) {
                    Map<String, String> User = new HashMap<>();
                    User = session.getUserDetails();

                    List<String> UserDetail = new ArrayList<String>();

                    Iterator iterator = User.keySet().iterator();
                    while (iterator.hasNext()) {

                        String key = (String) iterator.next();
                        String value = User.get(key);
                        UserDetail.add(value);

                    }

                    final String email_id = UserDetail.get(0);

                    ImageView student, tutor;
                    student = (ImageView) view.findViewById(R.id.student);
                    tutor = (ImageView) view.findViewById(R.id.tutor);

                    student.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


//                            Toast.makeText(getContext(), "my details B student imageview clicked", Toast.LENGTH_SHORT).show();
                            FragmentStack fragmentStack = FragmentStack.getInstance();
                            fragmentStack.push(new MyDetailsB());
                            roleIdChange(email_id, 0);

                            editor111.putInt("status",0);
                            editor111.putInt("roleId",0).apply();

                            DesiredTutor desiredTutor = new DesiredTutor();

                            fragmentStack.push(desiredTutor);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.replace(R.id.viewpager, desiredTutor);
                            fragmentTransaction.commit();

                            SharedPreferences pref=getContext().getSharedPreferences("fromSignup",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorpref=pref.edit();
                            editorpref.putBoolean("fromSignup",true).apply();
                            backbtn = 1;
                            popupWindow.dismiss();

                        }
                    });
                    tutor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editor111.putInt("status",0);
                            editor111.putInt("roleId",1).apply();
                            roleIdChange(email_id, 1);

                            FragmentTransaction f=fragmentManager.beginTransaction();
                            f.replace(R.id.viewpager,new Tablayout_with_viewpager()).commit();
                            backbtn = 1;
                            popupWindow.dismiss();

                        }
                    });


                }
            }

//        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        countrySet(country);







        try {
            Log.e("login response ","in my detailsB "+response);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject obj = (JSONObject) jsonObject.get("result");

          /*  if (obj.getInt("roleId")==0 ||obj.getInt("roleId")==1  ){
                popupWindow.di
                fragmentTransaction.replace(R.id.viewpager,new DesiredTutor()).commit();
            }
*/
//            Toast.makeText(getContext(), "res " + obj.toString(), Toast.LENGTH_SHORT).show();
            Log.e("res ", obj.toString());
            String usernm = obj.getString("firstName");
            String lastName = obj.getString("lastName");
           /* String[] userdetail = usernm.split(" ");*/
            fname.setText(usernm);
//            fname.setText(userdetail[0]);
            lname.setText(lastName);
            if (!obj.getString("age").equals("null"))
                age.setText(obj.getString("age"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SharedPreferences pref11 = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        submitButtonNotmyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fn = fname.getText().toString();
                final String ln = lname.getText().toString();
                final String ageStr = age.getText().toString();
                final String stateStr = state.getSelectedItem().toString();
                final String cityStr = city.getText().toString();
                final String emailStr = email.getText().toString();
                final String phoneStr= phone.getText().toString();
                final String genderStr = gender.getSelectedItem().toString();
                final String countryStr = country.getSelectedItem().toString();
                final String lang1Str = lang1.getSelectedItem().toString();
                final String lang2Str = lang2.getSelectedItem().toString();

                if (lang1Str.equalsIgnoreCase("Select Language 1")/* && lang2Str.equalsIgnoreCase("Select Language 2")*/ ){
                    Toast.makeText(getContext(), "Please select Languages..", Toast.LENGTH_SHORT).show();
                }else if (lang1Str.equalsIgnoreCase("Select Language 1")){
                    Toast.makeText(getContext(), "Please select Languages 1..", Toast.LENGTH_SHORT).show();
                }else if (phoneStr.length()<10){
                    phone.setError("Enter Valid Ph. Number");
                }/*else if (lang2Str.equalsIgnoreCase("Select Language 2")){
                    Toast.makeText(getContext(), "Please select Languages 2..", Toast.LENGTH_SHORT).show();
                }*/else {


                    if (genderStr.equalsIgnoreCase("male"))
                        gen1111 = 1;
                    else gen1111 = 0;




                    Log.e("age",ageStr);
                    Log.e("userid",String.valueOf(pref11.getInt("id", 0)));
                    Log.e("firstName",fn);
                    Log.e("lastName",ln);
                    Log.e("language2",lang2Str);
                    Log.e("city",cityStr);
                    Log.e("cell",phone.getText().toString());
                    Log.e("language1",lang1Str);
                    Log.e("gender",genderStr);
                    Log.e("country",countryStr);
                    Log.e("state",stateStr);






                    String URL = "https://www.thetalklist.com/api/updateProfile?" +
                            "userid=" + pref11.getInt("id", 0) +
                            "&country=" + countryStr.replace(" ","%20")
                            + "&state=" + stateStr.replace(" ","%20")
                            + "&city=" +cityStr.replace(" ","%20")
                            + "&gender=" + gen1111 +
                            "&language1=" + lang1Str.replace(" ","%20")
                            + "&language2=" + lang2Str.replace(" ","%20")
                            + "&age=" + ageStr
                            + "&firstName=" + fn.replace(" ","%20") +
                            "&lastName=" + ln.replace(" ","%20")
                            + "&cell=" + phone.getText().toString();





// String URL = "https://www.thetalklist.com/api/updateProfile";
                    Log.e("updateProf url", URL);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("update Profile",response);
//                            Toast.makeText(getContext(), "Profile updated..!", Toast.LENGTH_SHORT).show();

                            try {
                                JSONObject resObj=new JSONObject(response);

                                if (resObj.getInt("status") == 0) {
                                   /* FragmentTransaction fragmentManager1=getFragmentManager().beginTransaction();
                                    fragmentManager1.replace(R.id.viewpager,new Available_tutor()).commit();*/
                                   age.setText(ageStr);
                                   fname.setText(fn);
                                   lname.setText(ln);
                                    phone.setText(phoneStr);
                                   city.setText(cityStr);
                                   lang2.setSelection(langAdapter1.getPosition(lang2Str));
                                   lang1.setSelection(langAdapter1.getPosition(lang1Str));
                                   gender.setSelection(genderAdapter.getPosition(genderStr));
                                   state.setSelection(stateAdapter.getPosition(stateStr));
                                   country.setSelection(countryAdapter.getPosition(countryStr));

                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    LoginService loginService=new LoginService();
                                    loginService.login(pref111.getString("email",""),pref111.getString("pass",""),getContext());
                                }else if (resObj.getInt("status") == 1 &&resObj.getString("error") .equalsIgnoreCase("please fill up")){
                                    Toast.makeText(getContext(), "Make sure all fields are filled..!", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("age",ageStr);
                        params.put("userid",String.valueOf(pref11.getInt("id", 0)));
                        params.put("firstName",fn);
                        params.put("lastName",ln);
                        params.put("language2",lang2Str);
                        params.put("city",cityStr);
                        params.put("language1",lang1Str);
                        params.put("gender",genderStr);
                        params.put("country",countryStr);
                        params.put("state",stateStr);
                        return params;
                    }
                };
;
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(stringRequest);


                }
            }
        });

        return convertview;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void roleIdChange(String mail, int rId) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());


        String URL = "https://www.thetalklist.com/api/updateRoleID?" + "roleId=" + rId + "&email=" + mail;

        JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }

        );
        queue.add(getRequest);
    }

    String userChoosenTask;
    final int CAMERA_REQUEST = 1323;
    final int GALLERY_REQUEST = 1342;
    final int CROP_REQUEST = 1352;

//    private Bitmap bitmap;


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAMERA_REQUEST)
                onCaptureImageResult(data);
            else if (requestCode == CROP_REQUEST) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
//                TVusericon.setImageBitmap(imageBitmap);


                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                imageView1.setImageDrawable(roundedBitmapDrawable);
                imagesettingflyoutheader.setImageDrawable(roundedBitmapDrawable);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                assert imageBitmap != null;
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                final String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                final ImageUploadClass imageUploadClass=new ImageUploadClass(encodedImageString,imageBitmap,getContext(),pref111.getInt("id",0));


                imageUploadClass.execute();



//                new UploadFileToServer(imageBitmap,pref111.getInt("id",0),getContext()).execute();
            }

        }
    }
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView1.setImageBitmap(bm);

        imagesettingflyoutheader.setImageBitmap(bm);
        cropImag(data);

    }
    private void cropImag(Intent data) {

        Uri uri = data.getData();
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("outputX", 128);
        cropIntent.putExtra("outputY", 128);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CROP_REQUEST);
    }






    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
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
        imageView1.setImageBitmap(thumbnail);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), thumbnail);

        roundedBitmapDrawable.setCornerRadius(80.0f);
        roundedBitmapDrawable.setAntiAlias(true);


        /*LoginService loginService=new LoginService();
        loginService.login(pref111.getString("email",""),pref111.getString("pass",""),getContext());

        SettingFlyout settingFlyout=new SettingFlyout();
        settingFlyout.profilePicChange();*/


        imagesettingflyoutheader.setImageBitmap(thumbnail);
       /* Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView1);*/
        cropImag(data);
//        uploadImage();
    }



}


