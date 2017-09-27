package com.example.saubhagyam.thetalklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.saubhagyam.thetalklist.util.NotificationUtils;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 05/06/2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (!isOnline(context)){

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

               Intent i=new Intent(getApplicationContext(),NoInternetConnection.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);
            }

        }

    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
