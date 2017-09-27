package com.example.saubhagyam.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DesiredTutor extends Fragment {


    Spinner spnFirstLang, /*spnSecondlang,*/ spnGen, spnCountry, spnState,spnSubject;
    EditText keyWord;

    Button DesiredTutor_saveButton;
    Dialog dialog;
    FragmentStack fragmentStack;
    SharedPreferences sharedPreferences;
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    boolean bit=false;
    SharedPreferences pref1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.fragment_desired_tutor, container, false);
        fragmentStack = FragmentStack.getInstance();

        /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
*/

       /* convertView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if (!bit){
                        Toast.makeText(getContext(), "Please press once to exit", Toast.LENGTH_SHORT).show();
                        bit = true;
                    }else {
                        getActivity().finish();
                    }

                    return true;
                }
                return false;
            }
        });*/

        preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
        if (preference.contains("query")){
            FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.viewpager, new Available_tutor()).commit();
        }


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        queue = Volley.newRequestQueue(getContext());
        if (getActivity() != null) {

            spnFirstLang = (Spinner) convertView.findViewById(R.id.spnFirstlang);
//            spnSecondlang = (Spinner) convertView.findViewById(R.id.spnSecondlang);
            spnSubject = (Spinner) convertView.findViewById(R.id.spnSubject);
            spnCountry = (Spinner) convertView.findViewById(R.id.spnCountry);
            spnGen = (Spinner) convertView.findViewById(R.id.spnGen);
            spnState = (Spinner) convertView.findViewById(R.id.spnState);
            DesiredTutor_saveButton = (Button) convertView.findViewById(R.id.desiredTutor_save_button);
            keyWord= (EditText) convertView.findViewById(R.id.keyWord);

//            fragmentStack.push(new DesiredTutor());

//            new subjectApi().execute();


            pref1 = getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE);



            keyWord.setText(pref1.getString("keyword",""));

         /*   {
                String URL = "https://www.thetalklist.com/api/subject";
                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ary = response.getJSONArray("subjects");
                            ArrayList<String> coun1 = new ArrayList<>();
                            coun1.add("Select Subject");
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);
                                coun1.add(data.getString("name"));
                            }
                            if (dialog.isShowing())
                                dialog.dismiss();
                            editor.putString("subjects",ary.toString()).apply();
                            arrayAdapter1= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun1);
                            spnSubject.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing())
                            dialog.dismiss();
//                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                );
                queue.add(getRequest);
            }
*/

            String gen[] = getResources().getStringArray(R.array.Gender);
            final ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, gen);
            spnGen.setAdapter(genderAdapter);
            spnGen.setSelection(genderAdapter.getPosition(pref1.getString("gender","")));



            String languages1[] = getResources().getStringArray(R.array.Language1);
            final ArrayAdapter langAdapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, languages1);
            spnFirstLang.setAdapter(langAdapter1);
            spnFirstLang.setSelection(langAdapter1.getPosition(pref1.getString("lang1","")));

            {
                RequestQueue queue1 = Volley.newRequestQueue(getContext());
                String URL = "https://www.thetalklist.com/api/states";
                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ary = response.getJSONArray("states");
                            ArrayList<String> coun = new ArrayList<>();
                            coun.add("State");
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);
                                coun.add(data.getString("provice"));
                            }
                            if (getActivity() != null) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                                spnState.setAdapter(arrayAdapter);
                            }

                            spnState.setSelection(arrayAdapter.getPosition(pref1.getString("state","")));
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
                String URL = "https://www.thetalklist.com/api/subject";
                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ary = response.getJSONArray("subjects");
                            ArrayList<String> coun = new ArrayList<>();
                            coun.add("Select Subject");
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);
                                coun.add(data.getString("subject"));
                            }
                            if (getActivity() != null) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                                spnSubject.setAdapter(arrayAdapter);

                                for (int i = 0; i < ary.length(); i++) {
                                    JSONObject data = ary.getJSONObject(i);

                                        spnSubject.setSelection(arrayAdapter.getPosition(pref1.getString("subject","")));

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
            }

            SettingFlyout settingFlyout=new SettingFlyout();
           SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            int roleId=pref.getInt("roleId", 0);
            if (roleId==1 | roleId==2 | roleId==3){
//                fragmentStack.setSize(fragmentStack.size()+1);
//                settingFlyout.setBit(false);
            }


            Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
           /* if (roleId == 0) {
                View view1 = toolbar.getRootView();
                view1.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
                view1.findViewById(R.id.tutorToolbar).setVisibility(View.GONE);
                view1.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

            }
            if (roleId == 1 || roleId == 2 || roleId == 3) {*/
                View view1 = toolbar.getRootView();
                view1.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
//                view1.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
//                view1.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

//                fragmentStack.setSize(1);
//                }

//            }

            spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spnCountry.getSelectedItem().toString().equalsIgnoreCase("USA")) {
                        spnState.setVisibility(View.VISIBLE);
                    } else spnState.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
           /* if (spnCountry.getSelectedItem().toString().equalsIgnoreCase("USA")){
                spnState.setVisibility(View.VISIBLE);
            }*/
           /* final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading.........");
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
            progressDialog.setIndeterminate(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();*/
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.threedotprogressbar);
            dialog.setCanceledOnTouchOutside(false);


            {
                sharedPreferences = getContext().getSharedPreferences("DesiredTutorCountries", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                if (sharedPreferences.contains("countries")) {
                    desiredapi= (desiredtutorapi) new desiredtutorapi().execute();
                    JSONArray ary= null;
                    try {
                        ary = new JSONArray(sharedPreferences.getString("countries",""));
                        ArrayList<String> coun = new ArrayList<>();
                        coun.add("Select Country");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("country"));
                        }
                        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                        spnCountry.setAdapter(arrayAdapter);


                                spnCountry.setSelection(arrayAdapter.getPosition(pref1.getString("country","")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    dialog.show();
                    desiredapi= (desiredtutorapi) new desiredtutorapi().execute();
                }
            }


            DesiredTutor_saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 /*   *//*if (spnSubject.getSelectedItem().toString().equalsIgnoreCase("Select Subject")){
                        Toast.makeText(getContext(), "Please select Subject", Toast.LENGTH_SHORT).show();
                    }else *//*if (spnFirstLang.getSelectedItem().toString().equalsIgnoreCase("Select Language 1")){
                        Toast.makeText(getContext(), "Please select First Lnaguage ", Toast.LENGTH_SHORT).show();
                    }*//*else if (spnSecondlang.getSelectedItem().toString().equalsIgnoreCase("Select Language 2")){
                        Toast.makeText(getContext(), "Please select Second Lnaguage ", Toast.LENGTH_SHORT).show();
                    }*//**//*else if (spnGen.getSelectedItem().toString().equalsIgnoreCase("Select Subject")){
                        Toast.makeText(getContext(), "Please select Subject", Toast.LENGTH_SHORT).show();
                    }*//*else if (spnCountry.getSelectedItem().toString().equalsIgnoreCase("Select country")){
                        Toast.makeText(getContext(), "Please select Country", Toast.LENGTH_SHORT).show();
                    }*//*else if (spnState.getSelectedItem().toString().equalsIgnoreCase("Select Subject")){
                        Toast.makeText(getContext(), "Please select Subject", Toast.LENGTH_SHORT).show();
                    }*//*else if (keyWord.getText().toString().equalsIgnoreCase("")){
                        keyWord.setError("Fill Something..");
                    }
                    else {*/

                        SharedPreferences.Editor edi = pref1.edit();
                        edi.putString("subject", spnSubject.getSelectedItem().toString());
                        edi.putString("lang1", spnFirstLang.getSelectedItem().toString());
//                        edi.putString("lang2", spnSecondlang.getSelectedItem().toString());
                        edi.putString("gender", spnGen.getSelectedItem().toString());
                        edi.putString("country", spnCountry.getSelectedItem().toString());
//                        edi.putString("state", spnState.getSelectedItem().toString());
                        edi.putString("keyword", keyWord.getText().toString()).apply();

                    preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
                    edi = preference.edit();
                    edi.clear().apply();




                        UserData data = UserData.getInstance();
                        data.setRoleId(0);

                        SharedPreferences preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
//editor.putInt("roleId",0);
                        editor.putInt("status", 0).apply();


                        TTL ttl = (TTL) getActivity().getApplicationContext();
                        ttl.ExitBit = 1;
                   /* SettingFlyout settingFlyout = new SettingFlyout();
                    Intent i = new Intent(getContext(), settingFlyout.getClass());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);*/

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    /*Bundle b=new Bundle();
                    b.putString("from","desiredTutor");*/


//                        fragmentStack.push(new DesiredTutor());
//                    fragmentStack.add(new DesiredTutor());
//                    tablayout_with_viewpager.setArguments(b);
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();

//                    }
                }
            });


        }
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
//        queue = Volley.newRequestQueue(getContext());
        return convertView;
    }






    ArrayAdapter<String> arrayAdapter,arrayAdapter1;
    RequestQueue queue;
    ArrayList<String> coun = new ArrayList<>();
    desiredtutorapi desiredapi;


    @Override
    public void onPause() {
        super.onPause();
        if (desiredapi!=null)
            desiredapi.cancel(true);
    }

    private class desiredtutorapi extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String URL = "https://www.thetalklist.com/api/countries";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("countries");

                        coun.add("Select Country");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("country"));
                        }
                    if (dialog.isShowing())
                        dialog.dismiss();
                        editor.putString("countries",ary.toString()).apply();

                        spnCountry.setAdapter(arrayAdapter);

                        spnCountry.setSelection(arrayAdapter.getPosition(pref1.getString("country","")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (dialog.isShowing())
                    dialog.dismiss();
//                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            queue.add(getRequest);
            return null;
        }
    }

   /* private class subjectApi extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String URL = "https://www.thetalklist.com/api/subject";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("subjects");
                        ArrayList<String> coun1 = new ArrayList<>();
                        coun1.add("Select Subject");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun1.add(data.getString("name"));
                        }
                        if (dialog.isShowing())
                            dialog.dismiss();
                        editor.putString("subjects",ary.toString()).apply();
                        arrayAdapter1= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun1);
                        spnSubject.setAdapter(arrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (dialog.isShowing())
                        dialog.dismiss();
//                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            queue.add(getRequest);
            return null;
        }
    }*/



}

