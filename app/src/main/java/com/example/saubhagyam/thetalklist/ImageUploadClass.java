package com.example.saubhagyam.thetalklist;

import android.app.*;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 21/06/2017.
 */

public class ImageUploadClass extends AsyncTask<Void, Integer, Void>{


    private String encodedImageString;
    Bitmap bitmap;
    Context context;
    int id;
    Notification.Builder builder;
    NotificationManager mNotifyManager;


        ImageUploadClass(final String encodedImageString, Bitmap bitmap, final Context context, final int id){
            this.encodedImageString=encodedImageString;
            this.bitmap=bitmap;
            this.context=context;
            this.id=id;

        }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
//        builder.setContentText("Image Uploaded").setProgress(100,values[0],false);
        mNotifyManager.notify(1, builder.build());
    }

    @Override
    protected void onPreExecute() {
        mNotifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder=new Notification.Builder(context);
        builder.setContentTitle("Profile Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.mipmap.ttlg2);
        builder.setProgress(100,0,true);
        mNotifyManager.notify(1, builder.build());
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        builder.setContentText("Image Uploaded").setProgress(0,0,false);
        mNotifyManager.notify(1, builder.build());
    }

    @Override
        protected Void doInBackground(Void... params) {
            uploadImage(encodedImageString,bitmap,context,id);
            return null;
        }


    public void uploadImage(final String encodedImageString, Bitmap bitmap, final Context context, final int id) {





        String uploadURL = "https://www.thetalklist.com/api/profile_pic"/*?uid=17430"&image="+encodedImageString*/;
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
               /* if(s.equals("true")){
                    Toast.makeText(getContext(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }*/
//                Toast.makeText(context, "Some error occurred! " + s, Toast.LENGTH_LONG).show();

              /*  LoginService loginService=new LoginService();
                loginService.login(context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),context);
                Intent intent=new Intent(getApplicationContext(),SettingFlyout.class);
                intent.putExtra("fragmant","video");
                context.startActivity(intent);*/


/*                SharedPreferences preferences=context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("pic",)*/


               /*ImageView TVusericon = (ImageView) findViewById(R.id.imagesettingflyoutheader);
                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/"+pic)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);*/


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
}
