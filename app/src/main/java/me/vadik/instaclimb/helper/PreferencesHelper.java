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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isDark = preferences.getBoolean("dark_theme", false);
        context.setTheme(isDark ? darkTheme : lightTheme);
    }

    public static void setTheme1(Context context, boolean isDark) {
        setTheme1(context, false, isDark);
    }

    public static void setTheme1(Context context, boolean withActionBar, boolean isDark) {
        int lightTheme = withActionBar ? R.style.AppTheme : R.style.AppTheme_NoActionBar;
        int darkTheme = withActionBar ? R.style.AppThemeDark : R.style.AppThemeDark_NoActionBar;
        context.setTheme(isDark ? darkTheme : lightTheme);
    }
}
