package com.example.saubhagyam.thetalklist.Services;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Bean.CountryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saubhagyam on 06/07/2017.
 */

public class CountryService {

    private List<CountryModel> coun;
    public List<CountryModel> countryServiceCall(Context context){

        RequestQueue queue1 = Volley.newRequestQueue(context);
        String URL = "https://www.thetalklist.com/api/countries";
        JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("countries",response.toString());
                try {
                    JSONArray ary = response.getJSONArray("countries");
                    coun=new ArrayList<>();
                    CountryModel model=new CountryModel();

//                        ArrayList<String> coun = new ArrayList<>();
//                        coun.add("Select country");
                    for (int i = 0; i < ary.length(); i++) {

                        if (i == 0) {
                            model.setCountryCode(0);
                            model.setCountryName("Select country");
                            coun.add(i,model);
                        } else {

                            JSONObject data = ary.getJSONObject(i);
                            model.setCountryCode(data.getInt("id"));
                            model.setCountryName(data.getString("country"));
                            coun.add(i,model);
//                                coun.add(data.getString("country"));
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

        return coun;
    }
}
