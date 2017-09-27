package com.example.saubhagyam.thetalklist.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by saubhagyam on 2/9/2017.
 */

public class AsynTaskLoaderSignUp extends AsyncTask<String,Void,String> {

    private final Context context;
    private final OnAsyncResult OnListen;
    private ProgressDialog progressDialog;
    private final String requestURL;
    private final LinkedHashMap<String,String> linkedHashMap;


    public AsynTaskLoaderSignUp(Context context, OnAsyncResult asyncResult,LinkedHashMap<String,String> hashMap,String requestURL){
        this.context=context;
        this.OnListen=asyncResult;
        this.linkedHashMap=hashMap;
        this.requestURL=requestURL;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url= null;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            if(linkedHashMap!=null){
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                System.out.println("HASHMAP: "+ linkedHashMap);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(linkedHashMap));
                System.out.println("HASHMAP: "+linkedHashMap);
                System.out.println("Connection: " +httpURLConnection);

                writer.flush();
                writer.close();
                outputStream.close();


                int responseCode = httpURLConnection.getResponseCode();
                System.out.println("Response Code:" +responseCode);
                System.out.println("Http Connection Code: "+ HttpsURLConnection.HTTP_OK);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line="";
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    System.out.println("Buffer Reader  : "+ br);
                    while ((line=br.readLine()) != null) {
                        response+=line;
                        System.out.println("Line : "+ line +"Response"+response);
                    }
                }
                else {
                    response = "error";

                }

            }
        } catch (MalformedURLException e) {
            response = "error";
            e.printStackTrace();
        } catch (IOException e) {
            response = "error";
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Resp",""+result);
        progressDialog.dismiss();
        if(!result.equals("error") && !result.contains("errors"))
        {
            if(OnListen != null){
                OnListen.onAsyncResult(result);
            }
        }
        else if(OnListen != null){
                OnListen.onAsyncResult("Database not connected....");
            }


    }

    private String getPostDataString(LinkedHashMap<String, String> linkedMap) throws UnsupportedEncodingException {
        StringBuilder result=new StringBuilder();
        boolean booleanValue=true;
        for (Map.Entry<String,String> entry:linkedMap.entrySet()) {
            if (booleanValue){
                booleanValue=false;
            }else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

        }
        return result.toString();
    }
}
