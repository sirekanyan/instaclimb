package me.vadik.instaclimb.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class PreferencesHelper {

    private static final String PROPERTY_DARK_THEME = "me.vadik.instaclimb.dark_theme";
    private static final String PROPERTY_USERNAME = "me.vadik.instaclimb.username";
    private static final String PROPERTY_PASSWORD = "me.vadik.instaclimb.password";
    private final SharedPreferences preferences;
    private final Context context;

    public PreferencesHelper(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void refreshTheme() {
        refreshTheme(false);
    }

    public void refreshThemeWithActionBar() {
        refreshTheme(true);
    }

    private void refreshTheme(boolean withActionBar) {
        int lightTheme = withActionBar ? R.style.AppTheme : R.style.AppTheme_NoActionBar;
        int darkTheme = withActionBar ? R.style.AppThemeDark : R.style.AppThemeDark_NoActionBar;
        context.setTheme(isDark() ? darkTheme : lightTheme);
    }

    public boolean isDark() {
        return preferences.getBoolean(PROPERTY_DARK_THEME, false);
    }

    public void applyDark() {
        boolean isDark = preferences.getBoolean(PROPERTY_DARK_THEME, false);
        preferences.edit().putBoolean(PROPERTY_DARK_THEME, !isDark).apply();
    }

    public void saveCredentials(String username, String password) {
        preferences.edit()
                .putString(PROPERTY_USERNAME, username)
                .putString(PROPERTY_PASSWORD, password)
                .apply();
    }

    public boolean hasSavedCredentials() {
        return !TextUtils.isEmpty(getUsername())
                && !TextUtils.isEmpty(getPassword());
    }

    public String getUsername() {
        return preferences.getString(PROPERTY_USERNAME, "");
    }

    public String getPassword() {
        return preferences.getString(PROPERTY_PASSWORD, "");
    }
}
