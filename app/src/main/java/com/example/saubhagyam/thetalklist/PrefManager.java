package com.example.saubhagyam.thetalklist;

import android.content.SharedPreferences;

/**
 * Created by Saubhagyam on 14/04/2017.
 */

public class PrefManager {


    SharedPreferences pref=getPreferances();
    String unm;
    String pass;
    Boolean status;
    int roleId;

    private static final PrefManager ourInstance = new PrefManager();

    public static PrefManager getInstance() {
        return ourInstance;
    }

    private PrefManager() {
    }


public SharedPreferences getPreferances(){
    return pref;
}
public void setPreferance(SharedPreferences pref){
    this.pref=pref;
}



}
