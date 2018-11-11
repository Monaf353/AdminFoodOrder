package com.develop.windexit.admin.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WINDEX IT on 12-Mar-18.
 */

public class FCMRetrofitClient {
    private static Retrofit retrofit  = null;

    public static Retrofit getClient(String fcmUrl)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(fcmUrl)
                    .addConverterFactory(GsonConverterFactory.create())//
                    .build();
        }
        return retrofit;
    }
}
