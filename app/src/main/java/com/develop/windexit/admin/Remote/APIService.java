package com.develop.windexit.admin.Remote;

/**
 * Created by WINDEX IT on 12-Mar-18.
 */


import com.develop.windexit.admin.Model.MyResponse;
import com.develop.windexit.admin.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAWUfKv00:APA91bEMVKSGPPrpSdnz5mbBl7xKnkMIHdeB9udTVk87ew6BiLQaW3TyCMM8tsX3oGFXPt-y50JLTxSrhMTO7bj7_4_caUpB38GpCwD19BeVVcFovxRLmARHnNwsG5WcEh3Sl5RWQMb2"
    })
    @POST("fcm/send")
    Call<MyResponse>sendNotification(@Body Sender body);
}
