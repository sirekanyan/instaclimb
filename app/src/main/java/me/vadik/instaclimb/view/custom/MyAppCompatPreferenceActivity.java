package me.vadik.instaclimb.view.custom;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import me.vadik.instaclimb.helper.PreferencesHelper;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class MyAppCompatPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper preferences = new PreferencesHelper(this);
        preferences.refreshThemeWithActionBar();
        super.onCreate(savedInstanceState);
    }
}
