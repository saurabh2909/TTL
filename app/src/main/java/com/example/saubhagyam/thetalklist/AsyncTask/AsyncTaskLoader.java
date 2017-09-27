package com.example.saubhagyam.thetalklist.AsyncTask;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AsyncTaskLoader extends AsyncTask<Void, Void, String> {

    private final Context context;
    private final OnAsyncResult listener;
    private ProgressDialog progressDialog;
    private final String requestUrl;
    private final HashMap<String, String> hashMap;

    public AsyncTaskLoader(Context context, OnAsyncResult listener, HashMap<String, String> hashMap, String url) {
        this.context = context;
        this.listener = listener;
        this.requestUrl = url;
        this.hashMap = hashMap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        URL url;
        String response = "";

        try {
            url = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            if (hashMap != null) {
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                System.out.println("HASHMAP: "+ hashMap);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(hashMap));
                System.out.println("HASHMAP: "+hashMap);
                System.out.println("Connection: " +conn);

                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code:" +responseCode);
            System.out.println("Http Connection Code: "+ HttpsURLConnection.HTTP_OK);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line="";
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                System.out.println("Buffer Reader  : "+ br);
                while ((line=br.readLine()) != null) {
                    response+=line;
                    System.out.println("Line : "+ line +"Response"+response);
                }
            }
            else {
                response = "error";

            }
        } catch (Exception e) {
            response = "error";
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        if(!result.equals("error") && !result.contains("errors"))
        {
            if(listener != null){
                listener.onAsyncResult(result);
            }
        }
        else {
            if(listener != null){
                listener.onAsyncResult("Database not connected....");
            }
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
