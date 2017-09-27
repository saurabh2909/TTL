package com.example.saubhagyam.thetalklist;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.saubhagyam.thetalklist.util.TypefaceUtil;
import com.parse.Parse;

/**
 * Created by Saubhagyam on 16/06/2017.
 */

public class TTL extends Application {


    public int ExitBit=1;
    public int MessageBit=0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(TTL.this);
    }

//            public static final String  PUBLISHABLE_KEY = "pk_test_m2095bSj8vVA0n55nBjcBRDH";
    public static final String  PUBLISHABLE_KEY = "pk_live_cZQHiFEshZyEHQrIzyqc2rA9";
    public static final String APPLICATION_ID = "RKNck9SdN6sqcznBvy5lqnN2ln1FrrSabNcq8YEK";
    public static final String CLIENT_KEY = "zWtkaYFS0Ia91jKkgmIHJql30cARcrDmKUGAXLTY";
    public static final String BACK4PAPP_API = "https://parseapi.back4app.com/";


    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/GothamBookRegular.ttf");
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(BACK4PAPP_API).build());
    }
}
