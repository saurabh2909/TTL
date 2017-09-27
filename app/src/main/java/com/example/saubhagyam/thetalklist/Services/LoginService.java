package com.example.saubhagyam.thetalklist.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.CircleTransform;
import com.example.saubhagyam.thetalklist.UserData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saubhagyam on 12/05/2017.
 */

public class LoginService {
    String result;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    public String login(String email, String pass, final Context context) {

        pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        editor = pref.edit();

        String URL = "https://www.thetalklist.com/api/login?email=" + email+ "&password=" + pass;

        queue = Volley.newRequestQueue(context);


        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
                        editor.putInt("country", resultObj.getInt("country"));
                        editor.putInt("province", resultObj.getInt("province"));
                        editor.putInt("status", 0);
                        editor.putFloat("money", Float.parseFloat(resultObj.getString("money")));
                        editor.putString("email", resultObj.getString("email"));
                        editor.apply();


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
                    Toast.makeText(context,
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
                if (error.getClass().equals(ServerError.class)) {
                    // Show timeout error message
                    Toast.makeText(context,
                            "We are sorry for our Absence..! Wait for a While... We are setting up for you",
                            Toast.LENGTH_LONG).show();
                }


                Log.d("error", error.toString());
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
        return result;
    }

}
