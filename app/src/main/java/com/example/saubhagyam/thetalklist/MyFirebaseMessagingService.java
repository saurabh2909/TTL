package com.example.saubhagyam.thetalklist;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.saubhagyam.thetalklist.Adapter.MessageRecyclarAdapter;
import com.example.saubhagyam.thetalklist.Bean.MessageModel;
import com.example.saubhagyam.thetalklist.Config.Config;
import com.example.saubhagyam.thetalklist.util.NotificationUtils;
import com.example.saubhagyam.thetalklist.util.VideoUpload;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * Created by Saubhagyam on 10/04/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private NotificationUtils notificationUtils;
    public String msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null) {
            Log.e("xyz                    ", "message null");
            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());

            Log.e("msg", msg);
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");


            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);


            if (title.equalsIgnoreCase("msg")) {
            /*    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    MessageOneToOne messageOneToOne=new MessageOneToOne();
                    MessageModel messageModel=new MessageModel();
                    messageModel.setMsg_text(data.getString("message"));
                    messageModel.setSender_name(data.getString("uname"));
                    messageModel.setSender_id(data.getInt("uid"));

                    Intent i = new Intent();
                    i.setAction("appendChatScreenMsg");
                    i.putExtra("sender_id",data.getInt("uid"));
                    i.putExtra("message", data.getString("message"));
                    i.putExtra("firstName", data.getString("uname"));
                    this.sendBroadcast(i);


                    Intent iq = new Intent();
                    i.setAction("countrefresh");

                    this.sendBroadcast(iq);


                    messageOneToOne.messageModelList.add(messageModel);
//                    messageOneToOne.messageRecyclarAdapter=new MessageRecyclarAdapter(getApplicationContext(),messageOneToOne.messageModelList,"");
                    messageOneToOne.recyclerView.setAdapter(messageOneToOne.messageRecyclarAdapter);
                    messageOneToOne.messageRecyclarAdapter.notifyDataSetChanged();
//                    messageOneToOne.RefreshFragment();

                } else {*/
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    final int icon = R.mipmap.ttlg2;



                    Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);

                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    notificationIntent.putExtra("message","yes");
                    notificationIntent.putExtra("senderId", data.getInt("uid"));
                    notificationIntent.putExtra("firstName", data.getString("uname") );
               /* notificationIntent.putExtra("senderName",data.getString("uname"));
                notificationIntent.putExtra("msg", message);*/
//                    notificationIntent.putExtra("url", URL);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker(title).setWhen(0)
                                    .setAutoCancel(true)
                                    .setContentTitle("TheTalkList")
                                    .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                    .setStyle(inboxStyle)
                                    .setContentIntent(contentIntent)
                                    .setWhen(System.currentTimeMillis())
//                    .setWhen(getTimeMilliSec(timeStamp))
                                    .setSmallIcon(R.mipmap.ttlg2)
                                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                                    .setContentText(data.getString("uname") + " says: " + message);
//                    .setNumber(notificationId)
//                    .setAutoCancel(true)
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(100, mBuilder.build());
                Intent iq = new Intent();
                iq.setAction("countrefresh");
                this.sendBroadcast(iq);




                Intent i = new Intent();
                i.setAction("appendChatScreenMsg");
                i.putExtra("sender_id",data.getInt("uid"));
                i.putExtra("message", data.getString("message"));
                i.putExtra("firstName", data.getString("uname"));
                this.sendBroadcast(i);




//                }
            }
            else

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent ie = new Intent();
                ie.setAction("callEnd");
//                Intent intent = new Intent("close.Application");
                if (title.equalsIgnoreCase("rejectCall")) {


                    Log.e("rejecttttttttt","rejecttttttttttttttttttttttttttttttttttttttt");
                 /*   Intent i=new Intent(*//*"CallActivity"*//*);
                    i.setClass(this,CallActivity.class);
                    i.putExtra("close","close");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(i);*/
                    /*((Activity) getApplicationContext()).onBackPressed();
                    ((Activity) getApplicationContext()).onBackPressed();*/

                    SharedPreferences p=getSharedPreferences("videocallrole",MODE_PRIVATE);

                    if (p.getString("videocallrole","").equalsIgnoreCase("subscriber")){
                       /* CallActivity callActivity1=new CallActivity();
                        callActivity1.finish();*/
//                        sendBroadcast(intent);
                        this.sendBroadcast(ie);
                    }else {



                        this.sendBroadcast(ie);
                       /* VideoCall videoCall = new VideoCall(false);
                        Intent i = new Intent();
                        i.setClass(this, videoCall.getClass());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(i);*/


//                        sendBroadcast(intent);

                        //videoCall.closeActivity();
                    }
                    Log.e("title is rejectCall ", "in if condition");



                   /* ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
                    ar.topActivity.;*/

                } else {
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    Intent i = new Intent(getApplicationContext(), CallActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                }
            } /*else {*/
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                Log.e("yyyyyyyyyyyyyyyyyy", "messaging service");


                int not_id = new Random().nextInt();

                Intent notificationIntent;
                if (title.equalsIgnoreCase("call")) {

                    boolean isBackground = data.getBoolean("is_background");
                    String timestamp = data.getString("timestamp");
                    JSONObject payload = data.getJSONObject("payload");
                    int id = data.getInt("ID");
                    int cid = data.getInt("cid");
                    String name = data.getString("name");

                    Log.e(TAG, "isBackground: " + isBackground);
                    Log.e(TAG, "payload: " + payload.toString());
                    Log.e(TAG, "imageUrl: " + imageUrl);
                    Log.e(TAG, "timestamp: " + timestamp);
                    Log.e(TAG, "name: " + name);
                    Log.e(TAG, "senderId ID: " + id);
                    Log.e(TAG, "cid: " + cid);
                    Log.e(TAG, "tutorId : " + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0));

                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("classId", cid);
                    editor.putString("callSenderName", name);
                    editor.putInt("studentId", getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0));
                    editor.putInt("tutorId", id).apply();


//                        notificationIntent = new Intent(this, CallActivity.class);
                    Log.e("title is call ", "in if condition");
                       /* Intent launchIntent = new Intent();
                        launchIntent.setClassName("com.example.saubhagyam.thetalklist", ".CallActivity");
                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);*/ //null pointer check in case package name was not found
//                                getPackageManager().getLaunchIntentForPackage("CallActivity");
                    Intent i = new Intent(/*"CallActivity"*/);
                    i.setClass(this, CallActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(i);
                }/* else {
                    notificationIntent = new Intent(this, SplashScreen.class);
                    notificationIntent.putExtra("msg", message);
//                    notificationIntent.putExtra("url", URL);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, not_id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    final int icon = R.mipmap.ttlg2;

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker(title).setWhen(0)
                                    .setAutoCancel(true)
                                    .setContentTitle(title)
                                    .setContentIntent(contentIntent)
                                    .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                    .setStyle(inboxStyle)
                                    .setWhen(System.currentTimeMillis())
//                    .setWhen(getTimeMilliSec(timeStamp))
                                    .setSmallIcon(R.mipmap.ttlg2)
                                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                                    .setContentText(message);
//                    .setNumber(notificationId)
//                    .setAutoCancel(true)


                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(not_id, mBuilder.build());
                }*/
            } else {
                // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder().setService(MyJobService.class).setTag("my-job-tag").build();
        dispatcher.schedule(myJob);

    }


}
