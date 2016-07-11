package me.vadik.instaclimb.login;

import android.content.Context;
import android.content.SharedPreferences;

import me.vadik.instaclimb.provider.UserProvider;

/**
 * User: vadik
 * Date: 6/15/16
 */
public class LoginManager {

    private static final String PREF_NAME = "me.vadik.instaclimb.session_storage";
    private static final String SESSION_ID = "me.vadik.instaclimb.session_id";
    private static final String USER_ID = "me.vadik.instaclimb.user_id";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveSession(Context context, UserSession session) {
        getSharedPreferences(context).edit()
                .putString(SESSION_ID, session.getSessionId())
                .putInt(USER_ID, session.getUserId())
                .apply();
    }

    public static String getSessionId(Context context) {
        return getSharedPreferences(context).getString(SESSION_ID, null);
    }

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt(USER_ID, 0);
    }

    public static boolean isLoggedIn(Context context) {
        return getSessionId(context) != null && getUserId(context) != 0;
    }

    public static UserSession getSession(Context context) {
        return new UserSession(getSessionId(context), getUserId(context));
    }

    public static void logOut(Context context) {
        getSharedPreferences(context).edit()
                .remove(SESSION_ID)
                .remove(USER_ID)
                .apply();
    }
}
