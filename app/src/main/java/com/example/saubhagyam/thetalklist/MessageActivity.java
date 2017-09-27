package com.example.saubhagyam.thetalklist;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        LinearLayout viewpager_messageLiat= (LinearLayout) findViewById(R.id.viewpager);

//        int Extra=getIntent().getIntExtra("senderId",0);
//        MessageOneToOne messageOneToOne=new MessageOneToOne(Extra);
        FragmentTransaction ft1= getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.viewpager, new MessageList());
        ft1.commit();
    }
}
