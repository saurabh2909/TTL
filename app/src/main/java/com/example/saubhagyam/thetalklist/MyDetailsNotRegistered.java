package com.example.saubhagyam.thetalklist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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


public class MyDetailsNotRegistered extends AppCompatActivity {

    ImageView myDetailsNotRegisteredTutorImg;
    EditText Age,  City, Email;
    Spinner Gender,State, Language1, Language2, Language3, Country;
    Button Submit;
    private  String uploadURL;
    private  Bitmap bitmap;

    int gen111;
    String firstName, lastName;
    int id;


    SessionManager session;
    String UserName;

    final int CAMERA_REQUEST = 1323;
    final int GALLERY_REQUEST = 1342;
    final int CROP_REQUEST = 1352;

    String userChoosenTask;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details_not_registered);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/GothamBookRegular.ttf");
        Age = (EditText) findViewById(R.id.ageNotDetails);
        Age.setTypeface(typeface);
        Gender = (Spinner) findViewById(R.id.genderNotDetails);
        Country = (Spinner) findViewById(R.id.countryNotDetails);
        State = (Spinner) findViewById(R.id.stateNotDetails);
        City = (EditText) findViewById(R.id.cityNotDetails);
        City.setTypeface(typeface);
        Email = (EditText) findViewById(R.id.emailNotDetails);
        Email.setTypeface(typeface);
        Language1 = (Spinner) findViewById(R.id.lang1NotDetails);
        Language2 = (Spinner) findViewById(R.id.lang2NotDetails);
        myDetailsNotRegisteredTutorImg = (ImageView) findViewById(R.id.myDetailsNotRegisteredTutorImg);


        Submit = (Button) findViewById(R.id.submitButtonNotmyDetails);
        Submit.setTypeface(typeface);
        String gender[]=getResources().getStringArray(R.array.Gender);
        ArrayAdapter genderAdapter=new ArrayAdapter(this,R.layout.custom_spinner_textview,gender);
        Gender.setAdapter(genderAdapter);


final LinearLayout stateLayout= (LinearLayout) findViewById(R.id.llsec4);
        stateLayout.setVisibility(View.GONE);
        Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Country.getSelectedItem().toString().equalsIgnoreCase("USA")) {
                    stateLayout.setVisibility(View.VISIBLE);
//                    state.setVisibility(View.VISIBLE);
                } else stateLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



      /*  if (Country.getSelectedItem().toString().equalsIgnoreCase("USA"))
            State.setVisibility(View.VISIBLE);
        else State.setVisibility(View.G);
*/
        String languages[]=getResources().getStringArray(R.array.Language);
        ArrayAdapter langAdapter=new ArrayAdapter(this,R.layout.custom_spinner_textview,languages);
        Language1.setAdapter(langAdapter);
        String languages1[] = getResources().getStringArray(R.array.Language1);
        ArrayAdapter langAdapter1 = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_textview, languages1);
        Language2.setAdapter(langAdapter1);
//        Language3.setAdapter(langAdapter);


       /* final ProgressDialog progressDialog = new ProgressDialog(MyDetailsNotRegistered.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.........");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDialog.setIndeterminate(false);

        progressDialog.setCanceledOnTouchOutside(false);*/
        final Dialog dialog=new Dialog(MyDetailsNotRegistered.this);
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.show();
        String[] gen = new String[]{"Male", "Female"};

       /* ArrayAdapter spnAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, gen);
        Gender.setAdapter(spnAdapter);*/

        Email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        session = new SessionManager(getApplicationContext());


        String email_id = null;
        String password = null;

        {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String URL = "https://www.thetalklist.com/api/countries";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("countries",response.toString());
                    try {
                        JSONArray ary = response.getJSONArray("countries");
                        ArrayList<String> coun = new ArrayList<>();
                        coun.add("Select country");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("country"));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_textview, coun);
                        Country.setAdapter(arrayAdapter);
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
            queue.add(getRequest);


        }



        {
            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
            String URL = "https://www.thetalklist.com/api/states";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("states");
                        ArrayList<String> coun = new ArrayList<>();
                        coun.add("state");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("provice"));
                        }
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_textview, coun);
                            State.setAdapter(arrayAdapter);

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

//                ((TextView) Country.getSelectedView()).setTextColor(Color.BLACK);
//                ((TextView) Country.getChildAt(position)).setTextColor(Color.BLACK);
        Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        myDetailsNotRegisteredTutorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
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

            email_id = UserDetail.get(0);
            password = UserDetail.get(1);
             /* Login Data Response code start*/


            final UserData data = UserData.getInstance();

            data.setEmail(email_id);
            data.setPass(password);

            dialog.show();
            String URL = "https://www.thetalklist.com/api/login?email=" + email_id + "&password=" + password;
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);

                    data.setLoginServResponse(response);


//                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    try {

                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject resultObj = (JSONObject) jsonObject.get("result");
                        String email = (String) resultObj.get("email");
                        UserName = (String) resultObj.get("username");

                        firstName=resultObj.getString("firstName");
                        lastName=resultObj.getString("lastName");
                        id=resultObj.getInt("id");

                        Email.setText(email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.d("error", error.toString());
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(sr);



        /* Login Data Response code end*/
        }


        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), Gender.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("gender not registered",Gender.getSelectedItem().toString() );
                if (Age.getText().toString().equals("")) {
                    Age.setError("Required");
                }

                else if (City.getText().toString().equals("")) {
                    City.setError("Required");
                }
                else if (Email.getText().toString().equals("")) {
                    Email.setError("Required");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                    Email.setError("Invalid Email Address");
                } else if (Language1.getSelectedItem().toString().equalsIgnoreCase("Select Language 1") && Language2.getSelectedItem().toString().equalsIgnoreCase("Select Language 2") ){
                    Toast.makeText(getApplicationContext(), "Please select Languages..", Toast.LENGTH_SHORT).show();
                }else if (Language1.getSelectedItem().toString().equalsIgnoreCase("Select Language 1")){
                    Toast.makeText(getApplicationContext(), "Please select Languages 1..", Toast.LENGTH_SHORT).show();
                }else if (Language2.getSelectedItem().toString().equalsIgnoreCase("Select Language 2")){
                    Toast.makeText(getApplicationContext(), "Please select Languages 2..", Toast.LENGTH_SHORT).show();
                }else if (Country.getSelectedItem().toString().equalsIgnoreCase("Select country")){
                    Toast.makeText(getApplicationContext(), "Please select country..", Toast.LENGTH_SHORT).show();
                }else {
                    final int status = 1;


                    int gen1111;
                    if (Gender.getSelectedItem().toString().equalsIgnoreCase("male"))
                        gen1111 = 1;
                    else gen1111 = 0;

//                    SharedPreferences preferences=getSharedPreferences("signUpStatus",MODE_PRIVATE);

//                    String URL="https://www.thetalklist.com/api/updateProfile";
                    String URL="https://www.thetalklist.com/api/updateProfile?userid="+id+
                            "&country="+Country.getSelectedItem().toString().replace(" ","%20")+
                            "&state="+State.getSelectedItem().toString().replace(" ","%20")+
                            "&city="+City.getText().toString().replace(" ","%20")+
                            "&gender="+gen1111+
                            "&language1="+Language1.getSelectedItem().toString().replace(" ","%20")+
                            "&language2="+Language2.getSelectedItem().toString().replace(" ","%20")+
                            "&age="+Age.getText().toString()+
                            "&firstName="+firstName.replace(" ","%20")+
                            "&lastName="+lastName.replace(" ","%20") ;
//                            "&cell=" + getSharedPreferences("loginStatus",MODE_PRIVATE).getInt("cell",0);


                    Log.e("not reg Url",URL);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("mysetails not reg resp",response);

                            LoginService loginService=new LoginService();
                            loginService.login(getSharedPreferences("loginStatus",MODE_PRIVATE).getString("email",""),getSharedPreferences("loginStatus",MODE_PRIVATE).getString("pass",""),getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                            Intent sta = new Intent(getApplicationContext(), SettingFlyout.class);
                            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("firstTime", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=pref1.edit();
                            editor.putInt("fromSignUp",1).apply();
                            sta.putExtra("status", status);
                            sta.putExtra("roleId", 1);
                            sta.putExtra("username", UserName);
                            sta.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(sta);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Log.d("error", error.toString());
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();


                            params.put("age",Age.getText().toString());
                            params.put("userid",String.valueOf(id));
                            params.put("firstName",firstName);
                            params.put("lastName",lastName);
                            params.put("language2",Language2.getSelectedItem().toString());
                            params.put("city",City.getText().toString());
                            params.put("language1",Language1.getSelectedItem().toString());
                            params.put("gender",Gender.getSelectedItem().toString());
                            params.put("country",Country.getSelectedItem().toString());
                            params.put("state",State.getSelectedItem().toString());
                            return params;
                        }
                    };
                    queue.add(sr);


                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyDetailsNotRegistered.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAMERA_REQUEST)
                onCaptureImageResult(data);
            else if (requestCode == CROP_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                TVusericon.setImageBitmap(imageBitmap);


                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                myDetailsNotRegisteredTutorImg.setImageDrawable(roundedBitmapDrawable);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                assert imageBitmap != null;
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                uploadImage(encodedImageString,imageBitmap);
                ImageUploadClass imageUploadClass=new ImageUploadClass(encodedImageString,imageBitmap,getApplication(),getSharedPreferences("loginStatus",MODE_PRIVATE).getInt("id",0));
                imageUploadClass.execute();

            }

        }
    }



    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myDetailsNotRegisteredTutorImg.setImageBitmap(bm);


        cropImag(data);
//        uploadImage();
    }

    private void cropImag(Intent data) {

        Uri uri = data.getData();
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
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
        myDetailsNotRegisteredTutorImg.setImageBitmap(thumbnail);
        cropImag(data);
//        uploadImage();
    }

    private void uploadImage(final String encodedImageString, Bitmap bitmap) {




        String uploadURL="https://www.thetalklist.com/api/profile_pic"/*?uid=17430"&image="+encodedImageString*/;
        Log.e("image uploading url",uploadURL);
        Log.e("image uploading url",uploadURL);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, uploadURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
               /* if(s.equals("true")){
                    Toast.makeText(getContext(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }*/
                Toast.makeText(getApplicationContext(), "Some error occurred! "+s, Toast.LENGTH_LONG).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", encodedImageString);
                parameters.put("uid", String.valueOf(getSharedPreferences("loginStatus",MODE_PRIVATE).getInt("id",0)));
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(request);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);

    }
}
