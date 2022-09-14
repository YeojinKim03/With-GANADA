package com.example.withganada;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {

    @Multipart
    @POST("upload.php")
    Call<ResponseBody> uploadMp3(       //mp3 확장자로 녹음 음성을 웹서버에 저장
            @Part("description") RequestBody description,
            @Part MultipartBody.Part mp3
            );      //mp3를 photo로
}
