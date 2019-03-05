package com.example.mostafa.eatit.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mostafa.eatit.Model.User;
import com.example.mostafa.eatit.Remote.APIService;
import com.example.mostafa.eatit.Remote.RetrofitClient;

/**
 * Created by mostafa on 9/3/2017.
 */

public class Common {
    public static User currentUser;
    public static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static final String Delete = "Delete";
    public static final String USER_KEY = "User";
    public static final String PD_KEY = "Password";

    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Shipped";

    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }
        return false;
    }

}
