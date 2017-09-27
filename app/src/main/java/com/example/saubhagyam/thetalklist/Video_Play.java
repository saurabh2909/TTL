package com.example.saubhagyam.thetalklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.saubhagyam.thetalklist.Adapter.VideoListAdapter;
import com.example.saubhagyam.thetalklist.Adapter.VideoPlayAdapter;
import com.example.saubhagyam.thetalklist.Decorations.DividerItemDecoration;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by Saubhagyam on 18/04/2017.
 */

public class Video_Play extends Fragment {
    View view;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    VideoPlayAdapter videoPlayAdapter;
    android.widget.VideoView videoPlay_videoView;
    TextView videoPlay_videoTitle, videoPlay_videoSeenCount, videoPlay_videodescription;
    ImageView videoPlay_fbBtn, videoPlay_msgBtn, videoPlay_VideoCallBtn,like;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FragmentStack fragmentStack;
    LinearLayout controlLayout;

    String name;
    String desc;
    String source = null;
    int views;
    int id;
    MediaController mediaController;
    String jsonObj,jsonAry;

    JSONArray jsonArray/*,ary*/;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getContext().getSharedPreferences("videoPlaySelected", Context.MODE_PRIVATE);
        jsonObj = preferences.getString("response", "");
        jsonAry = preferences.getString("responseArray", "");



        Log.e("videoplay response", jsonObj);

        try {

            jsonArray=new JSONArray(jsonAry);

            JSONObject jsonObject = new JSONObject(jsonObj);
            name = jsonObject.getString("name");
            desc = jsonObject.getString("desc");
            source = jsonObject.getString("source");
            source = "https://www.thetalklist.com/uploads/video/" + source;
            views = jsonObject.getInt("views");
            id = jsonObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    int playing = 0;
    int isLiked=0;
    ProgressBar videoPlay_progressbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facebookSDKInitialize();
        view = inflater.inflate(R.layout.video_play_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.videoPlayList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.videoPLayListSwipeRefresh);
        videoPlay_videodescription = (TextView) view.findViewById(R.id.videoPlay_videoDescription);
        videoPlay_videoTitle = (TextView) view.findViewById(R.id.videoPlay_videoTitle);
        videoPlay_videoSeenCount = (TextView) view.findViewById(R.id.videoPlay_videoSeenCount);
        videoPlay_videoView = (android.widget.VideoView) view.findViewById(R.id.videoPlay_videoView);

        videoPlay_fbBtn = (ImageView) view.findViewById(R.id.videoPlay_fbBtn);
        videoPlay_msgBtn = (ImageView) view.findViewById(R.id.videoPlay_msgBtn);
        videoPlay_VideoCallBtn = (ImageView) view.findViewById(R.id.videoPlay_VideoCallBtn);
        controlLayout = (LinearLayout) view.findViewById(R.id.controlLayout);
        mediaController = new MediaController(getContext());
        like= (ImageView) view.findViewById(R.id.videoPlay_like);
        videoPlay_progressbar= (ProgressBar) view.findViewById(R.id.videoPlay_videoView_progress);

//        source ="https://www.thetalklist.com/uploads/video/"+source;
        videoPlay_videoView.seekTo(100);
        final Uri video1 = Uri.parse(source);




        Log.e("source link", source);
//        source = "https://www.thetalklist.com/uploads/video/2017/01/12/791f324b32efe060e9d619e14e822114.mp4";
//        Uri video = Uri.parse(source);
        videoPlay_videoView.requestFocus();
        videoPlay_videoView.setVideoPath(source);

        videoPlay_videoView.seekTo(1000);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(source,
                MediaStore.Images.Thumbnails.MINI_KIND);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        videoPlay_videoView.setBackgroundDrawable(bitmapDrawable);

        videoPlay_videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                videoPlay_progressbar.setVisibility(View.GONE);

                controlLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (playing == 0) {
                            videoPlay_videoView.start();
                            controlLayout.setBackgroundColor(Color.parseColor("#00000000"));
                            playing = 1;
                        } else {
                            videoPlay_videoView.pause();
                            controlLayout.setBackgroundColor(Color.parseColor("#80000000"));
                            playing = 0;
                        }
                    }
                });

            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLiked==0){
                    like.setImageDrawable(getResources().getDrawable(R.drawable.liked));
                    isLiked=1;
                }else {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                    isLiked=0;
                }

            }
        });

        videoPlay_videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Get some Error");
                builder.setMessage("Want to open in default Video player?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(source));
                        getContext().startActivity(i);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().onBackPressed();
                        videoPlay_videoView.stopPlayback();
                        videoPlay_progressbar.setVisibility(View.GONE);

                    }
                });
                builder.show();

                return true;
            }
        });

//         ary=jsonArray;

        return view;
    }
    ShareDialog shareDialog;
    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getContext());
       CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());

        // this part is optional
        shareDialog.registerCallback(callbackManager, callback);

    }

    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.e(TAG, "Succesfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.e(TAG, "Cancel occured");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.e(TAG, error.getMessage());
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };




    VideoListAdapter videoListAdapter;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentStack = FragmentStack.getInstance();

JSONArray ary=jsonArray;

        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        SpannableStringBuilder str = new SpannableStringBuilder("Definition:- ");
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), flag);

        String sourceString = "<b>" +"Definition:- "  + "</b> ";
        videoPlay_videoTitle.setText(name);
        videoPlay_videoSeenCount.setText(String.valueOf(views));
        videoPlay_videodescription.setText(str + desc);


        for (int i=0;i<ary.length();i++){
            try {
                JSONObject jsonObject=ary.getJSONObject(i);
                if (jsonObject.getInt("id")==id){
                    ary.remove(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("video play Jsonary",jsonArray.toString());
        Log.e("video play ary",ary.toString());

        videoPlayAdapter = new VideoPlayAdapter(getContext());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        videoListAdapter = new VideoListAdapter(getContext(), fragmentManager, ary);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(videoListAdapter);
        videoListAdapter.notifyDataSetChanged();


        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }

            void refreshItems() {
                // Load items
                // ...

                // Load complete
                swipeRefreshLayout.setRefreshing(false);
                // Update the adapter and notify data set changed
                // ...
                // Stop refresh animation
                recyclerView.scrollToPosition(0);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();
            }
        });


        videoPlay_fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "fbshare onclick", Toast.LENGTH_SHORT).show();

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(source))
                        .setImageUrl(Uri.parse(source))
                        .setContentTitle("TheTalkList:- "+name)
                        .setQuote(name)
                        .build();
                ShareDialog.show(getActivity(), content);



//                }

            }
        });

        videoPlay_msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageList messageList = new MessageList();

                fragmentStack.push(new Video_Play());
                fragmentStack.add(new Video_Play());
                fragmentTransaction.replace(R.id.viewpager, messageList).commit();

            }
        });
        videoPlay_VideoCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentStack.push(new Video_Play());
//                fragmentStack.add(new Video_Play());
                AvailableTutor_Fevorite_tablayout availableTutor_fevorite_tablayout = new AvailableTutor_Fevorite_tablayout();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 1);
                bundle.putInt("credit", 0);
                availableTutor_fevorite_tablayout.setArguments(bundle);
                fragmentTransaction.replace(R.id.viewpager, availableTutor_fevorite_tablayout).commit();
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
