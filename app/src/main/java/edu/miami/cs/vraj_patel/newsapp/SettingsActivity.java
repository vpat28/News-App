package edu.miami.cs.vraj_patel.newsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(androidx.preference.R.id.contentPanel, new MySettingsFragment())
                .commit();
    }
}