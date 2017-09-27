package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Services.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class EarnCredits extends Fragment {

    EditText earn_credit_paypalEmail,earn_credit_paypalammount,earn_credit_AIESEC_ammount;
    TextView earn_credit_currentBalance,earn_credit_ratePerMinute;
    Button earn_credit_submitBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_earn_credits, container, false);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GothamBookRegular.ttf");

        LoginService loginService=new LoginService();
        loginService.login(getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),getContext());



        earn_credit_paypalEmail= (EditText) view.findViewById(R.id.earn_credit_paypalEmail);
        earn_credit_paypalammount= (EditText) view.findViewById(R.id.earn_credit_paypalaamount);
        earn_credit_AIESEC_ammount= (EditText) view.findViewById(R.id.earn_credit_AIESEC_ammount);
        earn_credit_currentBalance= (TextView) view.findViewById(R.id.earn_credit_currentBalance);
        earn_credit_ratePerMinute= (TextView) view.findViewById(R.id.earn_credit_ratePerMinute);
        earn_credit_submitBtn= (Button) view.findViewById(R.id.earn_credit_submitBtn);

        earn_credit_paypalEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        earn_credit_paypalEmail.setTypeface(typeface);
        earn_credit_paypalammount.setTypeface(typeface);
        earn_credit_AIESEC_ammount.setTypeface(typeface);
        earn_credit_currentBalance.setTypeface(typeface);
        earn_credit_ratePerMinute.setTypeface(typeface);
        earn_credit_submitBtn.setTypeface(typeface);

        TextView tv= (TextView) getActivity().findViewById(R.id.num_credits);
        earn_credit_currentBalance.setText(tv.getText().toString());


        SharedPreferences preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
//        if (preferences.getFloat("hRate", 0.0f) != 0.0) {

            String res="USD $"+String.valueOf(preferences.getFloat("hRate", 0.0f) / 25.0f *60)+"/hr ($"+String.valueOf(preferences.getFloat("hRate", 0.0f) / 25.0f)+"/min)";
            earn_credit_ratePerMinute.setText(res);
     /*   } else {
            earn_credit_ratePerMinute.setText("Please set rate from profile biography");
        }*/




        earn_credit_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String earn_credit_paypalEmail_str=earn_credit_paypalEmail.getText().toString();
                final String earn_credit_paypalaamount_float=earn_credit_paypalammount.getText().toString();
                final String id=String.valueOf(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id",0));



                if (Float.parseFloat(earn_credit_currentBalance.getText().toString())>= Float.parseFloat(earn_credit_paypalammount.getText().toString())){

                    String URL = "https://www.thetalklist.com/api/paypal_cashout";
                    RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();



                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {



                        }
                    }
                    ){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("email", earn_credit_paypalEmail_str);
                            headers.put("trnAmount", earn_credit_paypalaamount_float);
                            headers.put("uid", id);
                            return headers;
                        }
                    };



                    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue1.add(sr);

                }else Toast.makeText(getContext(), "Enter Valid Ammount", Toast.LENGTH_SHORT).show();
            }
        });






        return view;
    }


}
