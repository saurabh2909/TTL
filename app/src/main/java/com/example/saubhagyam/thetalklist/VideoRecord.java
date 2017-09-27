package com.example.saubhagyam.thetalklist;

import android.app.*;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Adapter.VideoRecordAdapter;
import com.example.saubhagyam.thetalklist.util.AndroidMultiPartEntity;
import com.example.saubhagyam.thetalklist.util.Config;
import com.example.saubhagyam.thetalklist.util.FileUploadService;
import com.example.saubhagyam.thetalklist.util.ServiceGenerator;
import com.example.saubhagyam.thetalklist.util.VideoInterface;
import com.example.saubhagyam.thetalklist.util.VideoUpload;
import com.example.saubhagyam.thetalklist.util.VolleyMultipartRequest;
import com.example.saubhagyam.thetalklist.util.VolleySingleton;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoRecord extends Fragment {


    ImageView upload_video, upload_video_gallery;


    final int SELECT_VIDEO = 13210;


    View view1;
    View view;
    Button upload;
    android.widget.VideoView VDOView;
    Dialog dialog;


    private SharedPreferences permissionStatus;

    final String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;


    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 20000;
    public static final MainActivity ActivityContext = null;


    Spinner subject;

    EditText videoRecord_title, videoRecord_desc;
    String current_file_apth;
    TextView timeDuration;
    ProgressBar progressBar;

    String fileuri;


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("video upload", "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");

        return mediaFile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentStack fragmentStack = FragmentStack.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_record_layout, container, false);
        permissionStatus = getContext().getSharedPreferences("permission status", 0);
        checkPermission();

        upload_video = (ImageView) view.findViewById(R.id.upload_video);
        upload_video_gallery = (ImageView) view.findViewById(R.id.upload_video_gallery);
        subject = (Spinner) view.findViewById(R.id.videorecoedspnsubject);
        videoRecord_title = (EditText) view.findViewById(R.id.videoRecord_title);
        videoRecord_desc = (EditText) view.findViewById(R.id.videoRecord_desc);
/*

        String sub[]=getResources().getStringArray(R.array.sub);
        ArrayAdapter subAdapter=new ArrayAdapter(getContext(),R.layout.custom_spinner_textview,sub);
        subject.setAdapter(subAdapter);
*/

        progressBar = new ProgressBar(getContext());

//new subjectApi().execute();
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
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_gray_textview, coun);
                            subject.setAdapter(arrayAdapter);
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
        timeDuration = (TextView) view.findViewById(R.id.timeDuration);

        /*GridView gridView = (GridView) view.findViewById(R.id.uploadedVideo);

        gridView.setAdapter(new VideoRecordAdapter(getContext()));*/

        view1 = inflater.inflate(R.layout.popup_video_upload_layout, null);


        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_video_upload_layout);
//        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.getWindow().setAttributes(lp);


//        upload = (Button) dialog.findViewById(R.id.upload);
        VDOView = (VideoView) dialog.findViewById(R.id.video111);


        upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fileuri= getOutputMediaFileUri(2);
                if (subject.getSelectedItem().toString().equalsIgnoreCase("select subject"))
                {
                    Toast.makeText(getContext(), "Please select subject ", Toast.LENGTH_SHORT).show();
                }
                else if (videoRecord_title.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Please fill Title ", Toast.LENGTH_SHORT).show();
                }
                else if (videoRecord_desc.getText().toString().matches("")) {

                    Toast.makeText(getContext(), "Please fill Description ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ns/");
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.",
                                    Toast.LENGTH_LONG).show();
                            Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                        }
                    }
                    java.util.Date date = new java.util.Date();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(date.getTime());
                    File mediaFile;
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4");
//                Toast.makeText(getContext(), mediaStorageDir.getPath() + File.separator +"VID_" + timeStamp + ".mp4", Toast.LENGTH_SHORT).show();
                    current_file_apth = mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                }

            }
        });

        upload_video_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subject.getSelectedItem().toString().equalsIgnoreCase("select subject"))
                {
                    Toast.makeText(getContext(), "Please select subject ", Toast.LENGTH_SHORT).show();
                }
                else if (videoRecord_title.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Please fill Title ", Toast.LENGTH_SHORT).show();
                }
                else if (videoRecord_desc.getText().toString().matches("")) {

                    Toast.makeText(getContext(), "Please fill Description ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);

                }
            }
        });


        final SharedPreferences pref = getContext().getSharedPreferences("uploadVideo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

       /* upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                launchUploadActivity(pref.getString("videoUpload",""));
            }
        });*/
        return view;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.RECORD_AUDIO)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                editor.apply();


            } else {
                proceedAfterPermission();
            }
        } else {
        }
    }

    private void proceedAfterPermission() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[3])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            final Uri image_uri = data.getData();
            String selectedPath = getPath(image_uri);

//            VideoUpload videoUpload=new VideoUpload(sele);
//            VideoUpload videoUpload=new VideoUpload(getContext());
            if (requestCode == REQUEST_PERMISSION_SETTING) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    proceedAfterPermission();
                }
            }
            if (requestCode == SELECT_VIDEO) {
             /*   System.out.println("SELECT_VIDEO");


                File file = new File(generatePath(image_uri, getContext()));

                long length = file.length();

                length = length / 1024;*/



           /*     MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getContext(), image_uri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                final long timeInMillisec = Long.parseLong(time);


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
                mBuilder.setContentTitle("Video Upload")
                        .setContentText("Upload in progress")
                        .setSmallIcon(R.mipmap.ttlg2);


//                Toast.makeText(getContext(), "image At Location  " + generatePath(image_uri, getContext()), Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(), (int) (TimeUnit.MILLISECONDS.toSeconds(timeInMillisec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillisec))), Toast.LENGTH_LONG).show();


                final String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(timeInMillisec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillisec)),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMillisec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillisec)));

                timeDuration.setText(hms);
*/
               /* String t = timeDuration.getText().toString();
                String[] x = t.split(":");
                String min = x[0];

                if (Integer.parseInt(min) > 1) {
                    Toast.makeText(getContext(), "Video duration is more than limit.", Toast.LENGTH_SHORT).show();
                } else {*/
//                    Toast.makeText(getContext(), videoUpload.uploadVideo(selectedPath), Toast.LENGTH_SHORT).show();
                   /* InputStream inStream = null;
                    try {
                        inStream = getContext().getContentResolver().openInputStream(image_uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    assert inStream != null;
                    int bufferSize = 1 * 1024 * 1024;
                    byte[] buffer = new byte[bufferSize];
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                    int len = 0;
                    try {
                        while ((len = inStream.read(buffer)) != -1) {
                            byteBuffer.write(buffer, 0, len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String encodedVideoUrl = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
//                    Log.e("base64 length", String.valueOf(base64.length));
                    Log.e("encodedVideo after loop", encodedVideoUrl);
                    String sinSaltoFinal2 = encodedVideoUrl.trim();
                    String sinsinSalto2 = sinSaltoFinal2.replaceAll("\n", "");
                    Log.d("VideoData**>  ", sinsinSalto2);

                    String baseVideo = sinsinSalto2;*/


//                    new VideoUpload(encodedVideoUrl);

//                    Log.e("image uri path",image_uri.getPath());
//                    new UploadFileToServer(0,image_uri.getPath());
                   /* SharedPreferences pref=getContext().getSharedPreferences("uploadVideo",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();

                    editor.putString("videoUpload",generatePath(image_uri, getContext()));
                    fileuri=generatePath(image_uri, getContext());*/
                    launchUploadActivity(generatePath(image_uri, getContext()));
//                    uploadFile( generatePath(image_uri, getContext()));

//                    dialog.show();
/*                    dialog.show();
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputStream inStream = null;
                            try {
                                inStream = getContext().getContentResolver().openInputStream(image_uri);
                                assert inStream != null;
                                byte[] video = new byte[inStream.available()];

                                byte[] base64 = Base64.encode(video, Base64.DEFAULT);
                                String encodedVideoUrl = Base64.encodeToString(base64, Base64.DEFAULT);
                                Log.e("base64 length", String.valueOf(base64.length));
                                Log.e("encodedVideo after loop", encodedVideoUrl);

//                                new VideoUpload(encodedVideoUrl).execute();
                                new UploadFileToServer((int) timeInMillisec,generatePath(image_uri, getContext())).execute();

//                            uploadImage(encodedVideoUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                        }
                    });*/


                  /*  VDOView.setVideoPath(generatePath(image_uri, getContext()));
                    MediaController mediaController = new MediaController(dialog.getContext());
                    mediaController.show(2000);
                    VDOView.setMediaController(mediaController);
                    mediaController.setAnchorView(VDOView);
                    mediaController.setMediaPlayer(VDOView);
                    VDOView.setZOrderOnTop(true);
                    VDOView.start();*/


//                }
                }
                if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
              /*  MediaPlayer mp = MediaPlayer.create(getContext(),image_uri);
                float duration = mp.getDuration();*/
//                videoUpload.uploadVideo(selectedPath);


//                Toast.makeText(getContext(), videoUpload.uploadVideo(selectedPath), Toast.LENGTH_SHORT).show();


//                Toast.makeText(getContext(), "current file path : " + current_file_apth, Toast.LENGTH_SHORT).show();
             /*   MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getContext(), image_uri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                final long timeInMillisec = Long.parseLong(time);

                MediaPlayer mp = MediaPlayer.create(getContext(),image_uri);
                float duration = mp.getDuration();


//                Toast.makeText(getContext(), "image At Location  " + generatePath(image_uri, getContext()), Toast.LENGTH_LONG).show();


                final String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(timeInMillisec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillisec)),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMillisec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillisec)));*/
//                timeDuration.setText(String.format("%02d:%02d",duration));

                    /*dialog.show(); */ /*dialog.show();
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputStream inStream = null;
                        try {
                            inStream = getContext().getContentResolver().openInputStream(image_uri);
                            assert inStream != null;
                            byte[] video = new byte[inStream.available()];

                            byte[] base64 = Base64.encode(video, Base64.DEFAULT);
                            String encodedVideoUrl = Base64.encodeToString(base64, Base64.DEFAULT);
                            Log.e("base64 length", String.valueOf(base64.length));
                            Log.e("encodedVideo after loop", encodedVideoUrl);
//                            uploadImage(encodedVideoUrl);
                            new VideoUpload(encodedVideoUrl).execute();
//                            new UploadFileToServer((int) timeInMillisec,generatePath(image_uri, getContext())).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });*/

              /*  InputStream inStream = null;
                try {
                    inStream = getContext().getContentResolver().openInputStream(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    assert inStream != null;
                int bufferSize = 5 * 1024 * 1024;
                byte[] buffer = new byte[bufferSize];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int len = 0;
                try {
                    while ((len = inStream.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                    byte[] video = new byte[inStream.available()];

//                    String base64 = Base64.encode(byteBuffer.toByteArray(), Base64.DEFAULT);
                String encodedVideoUrl = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
//                    Log.e("base64 length", String.valueOf(base64.length));
                Log.e("encodedVideo after loop", encodedVideoUrl);
                String sinSaltoFinal2 = encodedVideoUrl.trim();
                String sinsinSalto2 = sinSaltoFinal2.replaceAll("\n", "");
                Log.d("VideoData**>  ", sinsinSalto2);

                String baseVideo = sinsinSalto2;*/


//                            uploadImage(encodedVideoUrl);
//                    uploadFile(image_uri);
//                    new VideoUpload(encodedVideoUrl).execute();
//                            new UploadFileToServer((int) timeInMillisec,generatePath(image_uri, getContext())).execute();

//                    Log.e("image uri path 2nd",image_uri.getPath());
//                    new UploadFileToServer(totalSize, image_uri.getPath());
                /*    SharedPreferences pref=getContext().getSharedPreferences("uploadVideo",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();

                    editor.putString("videoUpload",generatePath(image_uri, getContext()));
                    fileuri=generatePath(image_uri, getContext());*/
                    launchUploadActivity(generatePath(image_uri, getContext()));

//                    uploadFile( generatePath(image_uri, getContext()));

               /* VDOView.setVideoPath(generatePath(image_uri, getContext()));
                MediaController mediaController = new MediaController(dialog.getContext());
                mediaController.show(2000);
                VDOView.setMediaController(mediaController);
                mediaController.setAnchorView(VDOView);
                mediaController.setMediaPlayer(VDOView);
                VDOView.setZOrderOnTop(true);
                VDOView.start();*/

                }
                getActivity().onBackPressed();
            }

    }


    private void launchUploadActivity(String isImage) {
        Intent i = new Intent(getContext(), UploadActivity.class);
        i.putExtra("filePath", isImage);
        i.putExtra("id", getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0));
        i.putExtra("subject", subject.getSelectedItem().toString());
        i.putExtra("title", videoRecord_title.getText().toString());
        i.putExtra("description", videoRecord_desc.getText().toString());


        startActivity(i);
    }




    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

/*    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        private String filePath = null;

        public UploadFileToServer(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://www.thetalklist.com/api/video_upload_test");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File();

                // Adding file data to http body
                entity.addPart("video", new FileBody(sourceFile));
                entity.addPart("uid", new StringBody(String.valueOf()));
                entity.addPart("subject", new StringBody(subject));
                entity.addPart("title", new StringBody(title));
                entity.addPart("description", new StringBody(description));

                // Extra parameters if you want to pass to server
			*//*	entity.addPart("website",
						new StringBody("www.androidhive.info"));
				entity.addPart("email", new StringBody("abc@gmail.com"));*//*

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("video upload ", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }*/

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }


    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    ArrayAdapter<String> arrayAdapter;

    private class subjectApi extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

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
                        if (dialog.isShowing())
                            dialog.dismiss();
//                        editor.putString("subjects",ary.toString()).apply();
                        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                        subject.setAdapter(arrayAdapter);
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
            Volley.newRequestQueue(getContext()).add(getRequest);
            return null;
        }
    }


}
