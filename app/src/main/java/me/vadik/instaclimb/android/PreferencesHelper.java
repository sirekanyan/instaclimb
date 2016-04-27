package me.vadik.instaclimb.android;

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isDark = preferences.getBoolean("dark_theme", false);
        context.setTheme(isDark ? darkTheme : lightTheme);
    }
}
