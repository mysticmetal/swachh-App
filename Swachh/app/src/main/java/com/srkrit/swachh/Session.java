package com.srkrit.swachh;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Session {
    public static String ACCESS_TOKEN = "access_token";
    public static String EXPIRY = "expiry";
    public static String TYPE = "type";

    public static String SP_FILE_NAME = "SESSION";

    private SharedPreferences prefs = null;

    public Session(Activity activity) {
        prefs = activity.getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE);
    }
    public Session(Context activity) {
        prefs = activity.getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE);
    }

    public void set(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String get(String key) {
        return prefs.getString(key, "");
    }



//    public void clear() {
//        prefs.edit().remove(UserFields.NAME)
//                .remove(UserFields.ID)
//                .remove(UserFields.GUARDIAN)
//                .remove(UserFields.LATITUDE)
//                .remove(UserFields.LONGITUDE)
//                .remove(UserFields.USER_ID)
//                .remove(ACCESS_TOKEN)
//                .remove(EXPIRY)
//                .remove(TYPE)
//                .apply();
//    }
}