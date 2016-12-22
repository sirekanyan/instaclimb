package me.vadik.instaclimb.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.login.UserCredentials;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class PreferencesHelper {

    private static final String PROPERTY_DARK_THEME = "me.vadik.instaclimb.dark_theme";
    private static final String PROPERTY_EMAIL = "me.vadik.instaclimb.email";
    private static final String PROPERTY_PASSWORD = "me.vadik.instaclimb.password";
    private static final String PROPERTY_REMEMBER_ME = "me.vadik.instaclimb.remember_me";
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

    public void saveCredentials(UserCredentials credentials) {
        preferences.edit()
                .putString(PROPERTY_EMAIL, credentials.getEmail())
                .putString(PROPERTY_PASSWORD, credentials.getPassword())
                .putBoolean(PROPERTY_REMEMBER_ME, credentials.isRememberMe())
                .apply();
    }

    public void clearCredentials() {
        saveCredentials(UserCredentials.EMPTY);
    }

    public boolean hasSavedCredentials() {
        UserCredentials credentials = getCredentials();
        return !TextUtils.isEmpty(credentials.getEmail())
                && !TextUtils.isEmpty(credentials.getPassword());
    }

    public UserCredentials getCredentials() {
        return new UserCredentials(
                preferences.getString(PROPERTY_EMAIL, ""),
                preferences.getString(PROPERTY_PASSWORD, ""),
                preferences.getBoolean(PROPERTY_REMEMBER_ME, false));
    }
}
