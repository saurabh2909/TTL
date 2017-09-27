package com.example.saubhagyam.thetalklist.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Saubhagyam on 25/08/2017.
 */

public class SubjectArraySelected_service {
    JSONArray selectedArray;
    Context context;

    public SubjectArraySelected_service(Context context) {
        this.context = context;
    }

    public JSONArray getSelectedArray() {
        return getArray();
    }

    public void setSelectedArray(JSONArray selectedArray) {
        this.selectedArray = selectedArray;
    }

    private JSONArray getArray(){

        String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
        Log.e("subjects url", URL);

        selectedArray=new JSONArray();

        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("subjects tutor expanded", response);
                    if (jsonObject.getInt("status") == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String nativeLang = obj.getString("tutoring_subjects");

                        if (!nativeLang.equalsIgnoreCase("")) {
                            String mStringArray[] = nativeLang.split(",");

                            selectedArray = new JSONArray(Arrays.asList(mStringArray));
                        }else selectedArray = new JSONArray();

//                        Log.e("mJSONArray",selectedArray.toString());

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
        Volley.newRequestQueue(context).add(sr);

        return selectedArray;
    }

}
