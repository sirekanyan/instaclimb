package me.vadik.instaclimb.view.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.vadik.instaclimb.helper.PreferencesHelper;

/**
 * User: vadik
 * Date: 4/15/16
 */
public class MyAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.setTheme(this);
        super.onCreate(savedInstanceState);
    }
}
