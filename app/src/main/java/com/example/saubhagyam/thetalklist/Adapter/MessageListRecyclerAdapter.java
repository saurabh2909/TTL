package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.Bean.ChatroomModel;
import com.example.saubhagyam.thetalklist.CircleTransform;
import com.example.saubhagyam.thetalklist.FragmentStack;
import com.example.saubhagyam.thetalklist.MessageList;
import com.example.saubhagyam.thetalklist.MessageOneToOne;
import com.example.saubhagyam.thetalklist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saubhagyam on 17/04/2017.
 */

public class MessageListRecyclerAdapter  extends RecyclerView.Adapter<MessageListRecyclerAdapter.MyViewHolder>{

    final Context context;
    List<ChatroomModel> chatroomModelList;
    FragmentManager fragmentManager;



    public MessageListRecyclerAdapter(Context context,List<ChatroomModel> chatroomModelList, FragmentManager fragmentManager) {
        this.context = context;
        this.chatroomModelList=chatroomModelList;
        this.fragmentManager=fragmentManager;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ChatroomModel chatroomModel = chatroomModelList.get(position);

//        Toast.makeText(context, "first name "+chatroomModel.getSenderName(), Toast.LENGTH_SHORT).show();

        holder.senderName.setText(chatroomModel.getSenderName());
        String picPath = chatroomModel.getSenderPic();

        String date=chatroomModel.getLastTime();
        String originalString = "2010-07-14 09:00:02";
        Date date1 = null;
        try {
            if (date!=null) {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(date);
                String newString = new SimpleDateFormat("HH:mm", Locale.US).format(date1); // 9:00
                String newdate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date1); // 9:00

                holder.Messageday.setText(newdate);
                holder.MessageTime.setText(newString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


/*

        Date date1= null;
        Date date2= null;
        try {
            date1 = format.parse(date);
            date2 = format2.parse(date);
            holder.Messageday.setText(date1.getDate());
            holder.MessageTime.setText((int) date2.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
*/




        if (!picPath.equals("")) {
//                Picasso.with(context).load(picPath).placeholder(R.drawable.process).error(R.drawable.errorimage).resize(50, 50).into(holder.TutorImg);
            Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.SenderImg);
        } else {
//                holder.TutorImg.setImageResource(R.drawable.black_person);
            Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.SenderImg);

        }


        SharedPreferences chatPref=context.getSharedPreferences("chatPref",Context.MODE_PRIVATE);
        final SharedPreferences.Editor chatPrefEditor=chatPref.edit();
        holder.chatroom_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatPrefEditor.putString("firstName",chatroomModel.getSenderName());
                chatPrefEditor.putInt("receiverId",chatroomModel.getSenderId()).apply();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                FragmentStack fragmentStack=FragmentStack.getInstance();
//                        fragmentStack.add(new MessageList());
                fragmentStack.push(new MessageList());
                MessageOneToOne messageOneToOne=new MessageOneToOne();
                fragmentTransaction.replace(R.id.viewpager, messageOneToOne).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatroomModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView senderName;
        final TextView Messageday;
        final TextView MessageTime;
        final ImageView SenderImg;
        final LinearLayout chatroom_list_layout;

        public MyViewHolder(View itemView) {
            super(itemView);


            senderName= (TextView) itemView.findViewById(R.id.senderName);
            Messageday= (TextView) itemView.findViewById(R.id.Messageday);
            MessageTime= (TextView) itemView.findViewById(R.id.Messagetime);
            SenderImg= (ImageView) itemView.findViewById(R.id.SenderImg);

            chatroom_list_layout= (LinearLayout) itemView.findViewById(R.id.chatroom_list_layout);
        }
    }

}
