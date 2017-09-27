package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.saubhagyam.thetalklist.FragmentStack;
import com.example.saubhagyam.thetalklist.R;
import com.example.saubhagyam.thetalklist.TTL;
import com.example.saubhagyam.thetalklist.TabBackStack;
import com.example.saubhagyam.thetalklist.VideoList;
import com.example.saubhagyam.thetalklist.Video_Play;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 18/04/2017.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {

    private final Context context;
    //    private final JSONArray array;
    final FragmentManager fragmentManager;
    JSONArray array;


    int withPos = 0;
    int pos;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public VideoListAdapter(Context context, FragmentManager fragmentManager, JSONArray array) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        withPos = 1;
        this.array = array;

    }


    public VideoListAdapter(Context context, JSONArray array, FragmentManager fragmentManager) {
        this.context = context;
//        this.array = array;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public VideoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_layout, parent, false);
        return new VideoListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        String filePath = "http://techslides.com/demos/sample-videos/small.mp4";

/*        Bitmap bmThumbnail=ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        holder.videoThumb.setImageBitmap(bmThumbnail);*/

        Log.e("videoListAdapter array", array.toString());
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) array.get(position);
            final int id = jsonObject.getInt("id");
            final String name = jsonObject.getString("name");
            final String description = jsonObject.getString("desc");
            String firstName = jsonObject.getString("firstName");
            String lastNAme = jsonObject.getString("lastName");
            final String source = jsonObject.getString("source");
            final int views = jsonObject.getInt("views");



            holder.title.setText(name);
            holder.seen.setText(String.valueOf(views));
            holder.tutorName.setText(firstName + " " + lastNAme);
//            if (source!=null) {
                final String videoPath = "https://www.thetalklist.com/uploads/video/" + source+".jpg";

                Glide.with(context).load(videoPath).into(holder.videoThumb);
//            }

           /* new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = null;
                    String videoPath = "https://www.thetalklist.com/uploads/video/" + source+".jpg";
                    MediaMetadataRetriever mediaMetadataRetriever = null;
                    try {
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                        if (Build.VERSION.SDK_INT >= 14)
                            // no headers included
                            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                        else
                            mediaMetadataRetriever.setDataSource(videoPath);
                        //   mediaMetadataRetriever.setDataSource(videoPath);
                        bitmap = mediaMetadataRetriever.getFrameAtTime();
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        if (mediaMetadataRetriever != null)
                            mediaMetadataRetriever.release();
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null)
                        holder.videoThumb.setImageBitmap(bitmap);
                    holder.videoThumb.setVisibility(View.VISIBLE);
                    holder.videoThumb_alter.setVisibility(View.GONE);
                }
            }.execute();*/

           /* Glide.with(context).load(videoPath)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.videoThumb);*/


//           tabBackStack=TabBackStack.getInstance();
            final JSONObject finalJsonObject = jsonObject;
            holder.videoListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTL ttl=(TTL) context.getApplicationContext();
                    ttl.ExitBit=1;

                    Video_Play video_play = new Video_Play();
                    /*bundle.putString("name", name);
                    bundle.putString("description", description);
                    bundle.putString("source", videoPath);
                    bundle.putInt("views", views);
                    bundle.putInt("id", id);*/
//                    tabBackStack.setTabPosition(1);

                    preferences = getApplicationContext().getSharedPreferences("videoListResponse", Context.MODE_PRIVATE);
                    editor=preferences.edit();
                    editor.putInt("position",position).apply();

                    SharedPreferences preferences=context.getSharedPreferences("videoPlaySelected",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("response",finalJsonObject.toString()).apply();

                    FragmentStack fragmentStack = FragmentStack.getInstance();
                    fragmentStack.push(new VideoList());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.viewpager, video_play).commit();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    TabBackStack tabBackStack;


    @Override
    public int getItemCount() {
        return array.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        final ImageView videoThumb/*, videoThumb_alter*/;
        final LinearLayout videoListLayout;
        TextView title, tutorName, seen;


        public MyViewHolder(View itemView) {
            super(itemView);


            videoThumb = (ImageView) itemView.findViewById(R.id.videoThumb);
//            videoThumb_alter = (ImageView) itemView.findViewById(R.id.videoThumb_alter);
            videoListLayout = (LinearLayout) itemView.findViewById(R.id.videoListLayout);
            title = (TextView) itemView.findViewById(R.id.videoList_list_title);
            tutorName = (TextView) itemView.findViewById(R.id.videoList_list_tutorName);
            seen = (TextView) itemView.findViewById(R.id.videoList_list_seen);

        }
    }


}
