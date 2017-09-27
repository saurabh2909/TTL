package com.example.saubhagyam.thetalklist.util;

import android.content.Context;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Saubhagyam on 01/09/2017.
 */

public interface FileUploadService {
    @Multipart
    @POST("https://www.thetalklist.com/api/profile_video?uid=17618&video=")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part video
    );
}
