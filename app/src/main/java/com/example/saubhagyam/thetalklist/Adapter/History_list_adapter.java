package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saubhagyam.thetalklist.FragmentStack;
import com.example.saubhagyam.thetalklist.Pagination.History_model;
import com.example.saubhagyam.thetalklist.R;
import com.example.saubhagyam.thetalklist.TTL;
import com.example.saubhagyam.thetalklist.TabBackStack;
import com.example.saubhagyam.thetalklist.Video_Play;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 01/05/2017.
 */

public class History_list_adapter extends RecyclerView.Adapter<History_list_adapter.MyViewHolder> {

    Context context;
    List<History_model> jsonArray;

    public History_list_adapter(Context context, List<History_model> jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    public void clear() {
        jsonArray.clear();
        notifyDataSetChanged();
    }


    public void add(History_model g) {
        jsonArray.add(g);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
        return new History_list_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType==0)
            holder.history_layout.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));


//            JSONObject obj = (JSONObject) jsonArray.get(position);


            History_model history_model = jsonArray.get(position);

            SharedPreferences pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            holder.userName_TV.setText(history_model.getName());


            holder.date_TV.setText(history_model.getDate());


            holder.credit_TV.setText(String.valueOf(history_model.getRate()));



/*

           if (obj.getDouble("fee")<9.0d) {
            holder.credit_TV.setText("0"+(new DecimalFormat("##.##").format(obj.getDouble("fee"))));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
//                String.valueOf(obj.getDouble("fee")));

            else if (obj.getDouble("fee")==0.0d){
            credit_TV.setText("00.00");
        }else
            credit_TV.setText((new DecimalFormat("##.##").format(obj.getDouble("fee"))));*//**//**//**//**//**//**//**//*String.valueOf(obj.getDouble("fee")));*//**//**//**//*
            *//**//**//**//*String.valueOf(obj.getDouble("fee")));
        credit_TV.setText((new DecimalFormat("##.##").format(obj.getDouble("fee"))));*//**//**//**//*String.valueOf(obj.getDouble("fee")));*//**//**//**//**//*

            */



    }
        @Override
        public int getItemCount () {
            return jsonArray.size();
        }

    /*


    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.history_layout, null);


        TextView userName_TV= (TextView) convertView.findViewById(R.id.userName_TV);
        TextView date_TV= (TextView) convertView.findViewById(R.id.date_TV);
        TextView credit_TV= (TextView) convertView.findViewById(R.id.credit_TV);

        LinearLayout history_layout= (LinearLayout) convertView.findViewById(R.id.history_layout);
        if (position%2!=0)
            history_layout.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));

        try {
            JSONObject obj= (JSONObject) jsonArray.get(position);

           SharedPreferences pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
           if (pref.getInt("roleId",0)==0){
               userName_TV.setText(obj.getString("tFN")+" "+obj.getString("tLN"));
           }else {
               userName_TV.setText(obj.getString("sFN")+" "+obj.getString("sLN"));
           }


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(obj.getString("createAt"));
                date_TV.setText((convertedDate.getMonth()+1)+"/"+convertedDate.getDate()+"/"+(convertedDate.getYear()+1900));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (obj.getDouble("fee")==0.0d){
                credit_TV.setText("0.00");
            }else {

                credit_TV.setText(String.format("%.2f", obj.getDouble("fee")));
            }
*//*            if (obj.getDouble("fee")<9.0d) {
                credit_TV.setText("0"+(new DecimalFormat("##.##").format(obj.getDouble("fee"))));

            }
//                String.valueOf(obj.getDouble("fee")));

            else if (obj.getDouble("fee")==0.0d){
                credit_TV.setText("00.00");
            }else
                credit_TV.setText((new DecimalFormat("##.##").format(obj.getDouble("fee"))));*//**//*String.valueOf(obj.getDouble("fee")));*//*
            *//*String.valueOf(obj.getDouble("fee")));
            credit_TV.setText((new DecimalFormat("##.##").format(obj.getDouble("fee"))));*//*String.valueOf(obj.getDouble("fee")));*//*

        } catch (JSONException e) {
            e.printStackTrace();
        }




        return convertView;
    }

*/

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView userName_TV;
            TextView date_TV;
            TextView credit_TV;

            LinearLayout history_layout;

            public MyViewHolder(View itemView) {
                super(itemView);

                userName_TV = (TextView) itemView.findViewById(R.id.userName_TV);
                date_TV = (TextView) itemView.findViewById(R.id.date_TV);
                credit_TV = (TextView) itemView.findViewById(R.id.credit_TV);
                history_layout = (LinearLayout) itemView.findViewById(R.id.history_layout);
            }
        }

    }
