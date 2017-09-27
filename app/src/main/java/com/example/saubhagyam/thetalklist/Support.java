package com.example.saubhagyam.thetalklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;


public class Support extends Fragment {

    ExpandableLinearLayout /*contactus_layout,*/faqs_layout,video_layout;
    ImageView /*contactus_btn,*/faqs_btn,video_btn;
    int contactus_bit,faqs_bits,video_bits;
    Button supportEmailButton;
    LinearLayout /*contactus_btn_layout,*/faqs_btn_layout,videos_btn_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_support, container, false);

//        contactus_layout= (ExpandableLinearLayout) view.findViewById(R.id.contactus_layout);
        video_layout= (ExpandableLinearLayout) view.findViewById(R.id.video1111_layout);
        faqs_layout= (ExpandableLinearLayout) view.findViewById(R.id.faqs_layout);


//        contactus_btn= (ImageView) view.findViewById(R.id.contactus_btn);
        video_btn= (ImageView) view.findViewById(R.id.videos_btn);
        faqs_btn= (ImageView) view.findViewById(R.id.faqs_btn);

        supportEmailButton= (Button) view.findViewById(R.id.supportEmailButton);


//        contactus_btn_layout= (LinearLayout) view.findViewById(R.id.contactus_btn_layout);
        faqs_btn_layout= (LinearLayout) view.findViewById(R.id.faqs_btn_layout);
        videos_btn_layout= (LinearLayout) view.findViewById(R.id.videos_btn_layout);

       /* contactus_layout.toggle();
        video_layout.toggle();
        faqs_layout.toggle();*/


        supportEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                 String[] recipients={"support@thetalklist.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                getActivity().startActivity(intent);*/

                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"support@thetalklist.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
//                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send Mail"));
            }
        });

//        contactus_layout.collapse();
        video_layout.collapse();
        faqs_layout.collapse();


/*        if (video_layout.isExpanded()){
            video_layout.collapse();
        }*/

       /* contactus_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (video_layout.isExpanded()) {
                    video_layout.collapse();
                    video_btn.setImageResource(R.drawable.side_aerrow);
                    video_bits=0;
                }
                if (faqs_layout.isExpanded()) {
                    faqs_layout.collapse();
                    faqs_btn.setImageResource(R.drawable.side_aerrow);
                    faqs_bits=0;
                }

                contactus_layout.toggle();
                if (contactus_bit == 0) {
                    contactus_btn.setImageResource(R.drawable.down_aerrow);
                    contactus_bit = 1;
                } else {
                    contactus_btn.setImageResource(R.drawable.side_aerrow);
                    contactus_bit = 0;
                }
            }
        });*/
        videos_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* if (contactus_layout.isExpanded()) {
                    contactus_layout.collapse();
                    contactus_btn.setImageResource(R.drawable.side_aerrow);
                    contactus_bit=0;
                }*/
                if (faqs_layout.isExpanded()) {
                    faqs_layout.collapse();
                    faqs_btn.setImageResource(R.drawable.side_aerrow);
                    faqs_bits=0;
                }

                video_layout.toggle();
                if (video_bits== 0) {
                   video_btn.setImageResource(R.drawable.down_aerrow);
                    video_bits= 1;
                } else {
                    video_btn.setImageResource(R.drawable.side_aerrow);
                    video_bits= 0;
                }
            }
        });
        faqs_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (video_layout.isExpanded()) {
                    video_layout.collapse();
                    video_btn.setImageResource(R.drawable.side_aerrow);
                    video_bits=0;
                }
                /*if (contactus_layout.isExpanded()) {
                    contactus_layout.collapse();
                    contactus_btn.setImageResource(R.drawable.side_aerrow);
                    contactus_bit=0;
                }*/

                faqs_layout.toggle();
                if (faqs_bits == 0) {
                    faqs_btn.setImageResource(R.drawable.down_aerrow);
                    faqs_bits = 1;
                } else {
                    faqs_btn.setImageResource(R.drawable.side_aerrow);
                    faqs_bits = 0;
                }
            }
        });



        return view;
    }


}
