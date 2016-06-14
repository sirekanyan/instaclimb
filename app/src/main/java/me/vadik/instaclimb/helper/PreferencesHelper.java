package me.vadik.instaclimb.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class PreferencesHelper {

    public static void setTheme(Context context) {
        setTheme(context, false);
    }

    public static void setTheme(Context context, boolean withActionBar) {
        int lightTheme = withActionBar ? R.style.AppTheme : R.style.AppTheme_NoActionBar;
        int darkTheme = withActionBar ? R.style.AppThemeDark : R.style.AppThemeDark_NoActionBar;
        context.setTheme(isDark(context) ? darkTheme : lightTheme);
    }

    public static boolean isDark(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("dark_theme", false);
    }

    public static void applyDark(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isDark = preferences.getBoolean("dark_theme", false);
        preferences.edit().putBoolean("dark_theme", !isDark).apply();
    }
}
