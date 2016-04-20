package me.vadik.instaclimb.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * User: vadik
 * Date: 4/19/16
 */
public class UserHelper {
    public static String getCurrentUserId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("user_id", null);
    }
}
