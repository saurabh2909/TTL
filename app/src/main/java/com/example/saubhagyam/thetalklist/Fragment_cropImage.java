package com.example.saubhagyam.thetalklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.*;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edmodo.cropper.CropImageView;
import com.example.saubhagyam.thetalklist.Services.LoginService;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saubhagyam on 22/09/2017.
 */

public class Fragment_cropImage extends AppCompatActivity {

    Bitmap uri;
    BroadcastReceiver finish1;

    public Fragment_cropImage(Bitmap uri) {
        this.uri = uri;
    }

    public Fragment_cropImage() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(finish1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image_fragment);

        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(350, 400);
//        cropImageView.setFixedAspectRatio(false);
        cropImageView.setGuidelines(2);
        Button crop= (Button) findViewById(R.id.crop);

//        cropImageView.rotateImage(90);
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        uri= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


        /*finish1=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                finish();
                onBackPressed();
            }
        };

        registerReceiver(finish1,new IntentFilter("finish"));*/

        cropImageView.setImageBitmap(uri);

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap imageBitmap = cropImageView.getCroppedImage();
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                ImageView imageCrop=((ImageView)findViewById(R.id.imageCrop));
                imageCrop.setRotation(-90);
                imageCrop.setImageBitmap(imageBitmap);
                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);
//                TVusericon.setImageDrawable(roundedBitmapDrawable);

                Bitmap bb=((BitmapDrawable)imageCrop.getDrawable()).getBitmap();
                Bitmap cc=RotateBitmap(bb,-90);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                cc.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                uploadImage(encodedImageString,imageBitmap);

                /*ImageUploadClass imageUploadClass = new ImageUploadClass(encodedImageString, imageBitmap, getApplication(), pref.getInt("id", 0));
                imageUploadClass.execute();*/
                SettingFlyout settingFlyout=new SettingFlyout();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                uploadImage(encodedImageString, imageBitmap, getApplicationContext(), pref.getInt("id", 0));


                onBackPressed();
            }
        });

    }



    public void uploadImage(final String encodedImageString, final Bitmap bitmap, final Context context, final int id) {





        String uploadURL = "https://www.thetalklist.com/api/profile_pic"/*?uid=17430"&image="+encodedImageString*/;
        Log.e("image uploading url", uploadURL);
        Log.e("image uploading url", uploadURL);
        Log.e("encoded image string ", encodedImageString);

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

                LoginService loginService=new LoginService();
                loginService.login(context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),context);

/*                SharedPreferences preferences=context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("pic",)*/

//Intent i=new Intent(SettingFlyout.class,SettingFlyout.class);
//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//startActivity(i);



           /*   Intent i=new Intent();
              i.setAction("finish");
              sendBroadcast(i);*/

//onCreate(null);

//                onBackPressed();


/*                Intent refresh = new Intent(, SettingFlyout.class);
                startActivity(refresh);*/
//                SettingFlyout.this.notifyAll();
//                getApplicationContext().finish();
//               ImageView TVusericon = (ImageView) findViewById(R.id.imagesettingflyoutheader);

               /* final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms


                Glide.with(context).load("https://www.thetalklist.com/uploads/images/"+context.getSharedPreferences("loginStatus",MODE_PRIVATE).getString("pic",""))
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(TVusericon);
                    }
                }, 600);*/

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

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.crop_image_fragment,container,false);

        return view;
    }*/
}
