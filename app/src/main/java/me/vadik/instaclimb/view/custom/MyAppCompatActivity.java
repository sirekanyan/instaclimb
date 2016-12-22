package me.vadik.instaclimb.view.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.vadik.instaclimb.helper.PreferencesHelper;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class MyAppCompatActivity extends AppCompatActivity {

    protected PreferencesHelper preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = new PreferencesHelper(this);
        preferences.refreshTheme();
        super.onCreate(savedInstanceState);
    }
}
