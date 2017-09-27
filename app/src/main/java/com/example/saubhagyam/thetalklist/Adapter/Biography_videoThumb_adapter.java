package com.example.saubhagyam.thetalklist.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.saubhagyam.thetalklist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saubhagyam on 20/09/2017.
 */

public class Biography_videoThumb_adapter extends RecyclerView.Adapter<Biography_videoThumb_adapter.MyViewHolder> {

    Context context;
    JSONArray biography_video_ary;
    VideoView biography_videoview;
    ProgressBar biography_videoview_progress;
    LinearLayout controlLayout;
    int playing = 0;


    public Biography_videoThumb_adapter(Context context, JSONArray biography_video_ary,VideoView biography_videoview,ProgressBar biography_videoview_progress,LinearLayout controlLayout) {
        this.context = context;
        this.biography_video_ary = biography_video_ary;
        this.biography_videoview=biography_videoview;
        this.biography_videoview_progress=biography_videoview_progress;
        this.controlLayout=controlLayout;
    }


    public Biography_videoThumb_adapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumblines_layout, parent, false);
        return new Biography_videoThumb_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            final JSONObject thumbObj=biography_video_ary.getJSONObject(position);
            Glide.with(context).load("https://www.thetalklist.com/uploads/video/"+thumbObj.getString("source")+".jpg").into(holder.thumb);

            holder.thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    try {
                        biography_videoview.stopPlayback();
                        biography_videoview_progress.setVisibility(View.VISIBLE);
                        String  link = "https://www.thetalklist.com/uploads/video/" + thumbObj.getString("source");
                        biography_videoview.requestFocus();
//                        Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                        final Uri video = Uri.parse(link);
                        biography_videoview.setVideoURI(video);
                        biography_videoview.seekTo(1000);
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(link,
                                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

                        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                        biography_videoview.setBackgroundDrawable(bitmapDrawable);

                        biography_videoview.requestFocus();


                        biography_videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                biography_videoview_progress.setVisibility(View.GONE);
                                controlLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        if (playing == 0) {
                                            biography_videoview.start();
                                            controlLayout.setBackgroundColor(Color.parseColor("#00000000"));
                                            playing = 1;


                                        } else {
                                            biography_videoview.pause();
                                            controlLayout.setBackgroundColor(Color.parseColor("#80000000"));
                                            playing = 0;
                                        }
                                    }
                                });
                            }
                        });

                        final String finalLink = link;
                        final String finalLink1 = link;
                        biography_videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Get some Error");
                                builder.setMessage("Want to open in default Video player?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(finalLink1));
                                        context.startActivity(i);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().onBackPressed();
                                        biography_videoview.stopPlayback();
                                        biography_videoview_progress.setVisibility(View.GONE);

                                    }
                                });
                                builder.show();

                                return true;
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return biography_video_ary.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb;

        public MyViewHolder(View itemView) {
            super(itemView);

            thumb= (ImageView) itemView.findViewById(R.id.thumb);


        }
    }
}
