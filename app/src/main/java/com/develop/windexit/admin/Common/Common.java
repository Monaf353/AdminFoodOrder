package com.develop.windexit.admin.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.develop.windexit.admin.Model.adminUser;
import com.develop.windexit.admin.Remote.APIService;
import com.develop.windexit.admin.Remote.FCMRetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by WINDEX IT on 27-Feb-18.
 */

public class Common {

    public static String topicName = "News";
    public static adminUser currentUser;
    public static Request currentRequest;

 /*   public static final String SHIPPER_TABLE = "Shippers";
    public static final String ORDER_NEED_SHIP_TABLE = "OrderNeedShip";*/

    public static String PHONE_TEXT = "userPhone";
    public static final String fcmUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return FCMRetrofitClient.getClient(fcmUrl).create(APIService.class);
    }
    public static final int PICK_IMAGE_REQUEST = 703;

    //public static final String baseURL = "https://maps.googleapis.com";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static boolean isConnectedToINternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    public static String convertCodeToStatus(String status) {
        if (status.equals("0")) {
            return "Placed";
        } else if (status.equals("1")) {
            return "Being processed";
        } else {
            return "Complete";
        }
    }




    public static String date() {
        Calendar calendar = Calendar.getInstance();
        // calendar.setTimeInMillis(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public static String convertCodeToAvailable(String s) {
        if(s.equals("1"))
        {
            return "Available";
        }else
        {
            return "Not Available";
        }
    }
}
