package com.example.nicolas.ig2i_tp1.tp2.API;

import android.content.Context;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Nicolas on 27/02/2017.
 */

public interface ClientRequest {

    String BASE_URL = "http://10.0.2.2/android_chat/";

    @GET("data.php?action=connexion")
    Call<JsonObject> connexion(@Query("login") String login,
                               @Query("passe") String passe);

    @GET("data.php?action=getConversations")
    Call<JsonObject> getConversations();

    @GET("data.php?action=getMessages")
    Call<JsonObject> getConversation(@Query("idConv") int id_conversation,
                                     @Query("idLastMessage") int id_last_message);
}
