package me.vadik.instaclimb.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class MyAppCompatPreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.setTheme(this, true);
        super.onCreate(savedInstanceState);
    }
}
