package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentFeedBack extends AppCompatActivity {

    Button studentFeedback_submitButton;
    MultiAutoCompleteTextView student_feedback_msg;
    CheckBox report_inappropriate_behaviour;
    RatingBar student_feedback_ratingBar;

    int bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feed_back);

        student_feedback_ratingBar= (RatingBar) findViewById(R.id.student_feedback_ratingBar);
        student_feedback_msg= (MultiAutoCompleteTextView) findViewById(R.id.student_feedback_msg);
        report_inappropriate_behaviour= (CheckBox) findViewById(R.id.report_inappropriate_behaviour);




        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.videocall_hangup_popup, null);
        View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_video_call, null);

       /* final PopupWindow popupWindow = new PopupWindow(view,340,470, true);
//                    final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
        popupWindow.showAtLocation(findViewById(R.id.activity_feed_back), Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

        Button btn= (Button) view.findViewById(R.id.videocall_hangup_popup_okbtn);
        TextView videocall_handup_tutorName= (TextView) view.findViewById(R.id.videocall_handup_tutorName);
        videocall_handup_tutorName.setText(getSharedPreferences("videoCallTutorDetails",Context.MODE_PRIVATE).getString("tutorName",""));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
               *//* Toast.makeText(getApplicationContext(), "total coast " + response, Toast.LENGTH_SHORT).show();
                Log.e("total coast ", response);*//*
                Intent i=new Intent(getApplicationContext(),new StudentFeedBack().getClass());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
*/


        studentFeedback_submitButton= (Button) findViewById(R.id.studentFeedback_submitButton);
        studentFeedback_submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);


                if(report_inappropriate_behaviour.isSelected()==true){
                    bit=1;
                }
                else bit=0;

String URL="https://www.thetalklist.com/api/student_feedback_form?cid="+preferences.getInt("classId",0)+"&sid="+preferences.getInt("studentId",0)+"&tid="+preferences.getInt("tutorId",0)+"&user_given_rating="+student_feedback_ratingBar.getRating()+"&report_inappropriate="+bit+"&feedback_msg="+student_feedback_msg.getText().toString();
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Intent i=new Intent(getApplicationContext(),new SettingFlyout().getClass());
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(sr);

             /*   Intent i=new Intent(getApplicationContext(),new SettingFlyout().getClass());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);*/



            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),new SettingFlyout().getClass());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
