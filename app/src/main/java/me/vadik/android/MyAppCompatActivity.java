package me.vadik.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
