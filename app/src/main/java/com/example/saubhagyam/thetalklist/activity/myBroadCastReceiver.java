package com.example.saubhagyam.thetalklist.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Saubhagyam on 21/04/2017.
 */

public class myBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getAction();
        Toast.makeText(context, "Action   "+action, Toast.LENGTH_SHORT).show();

    }
}
