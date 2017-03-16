package com.example.nicolas.ig2i_tp1.tp2.API;

import android.content.Context;

import com.example.nicolas.ig2i_tp1.tp2.API.okhttp.interceptor.AddCookiesInterceptor;
import com.example.nicolas.ig2i_tp1.tp2.API.okhttp.interceptor.ReceivedCookiesInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.nicolas.ig2i_tp1.tp2.API.ClientRequest.BASE_URL;

/**
 * Created by Nicolas on 01/03/2017.
 */

public class APICreator {

    /**
     * Crée l'objet ClientRequest utilisé pour les requêtes à l'API.
     * Ajoute à Retrofit l'utilisation des Cookies.
     * @param context
     * @return
     */
    static public ClientRequest getRetrofit(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // REQUIRED
                .client(client) // VERY VERY IMPORTANT
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // REQUIRED

        ClientRequest cr = retrofit.create(ClientRequest.class);

        return cr;
    }

}
