package com.example.lab11_ghannane;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private String currentLang;

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "fr"); // default to French
        Context context = updateLocale(newBase, language);
        super.attachBaseContext(context);
    }

    public static Context updateLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Track current language for later comparison
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        currentLang = prefs.getString("My_Lang", "fr");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "fr");

        if (!lang.equals(currentLang)) {
            recreate(); // Language changed, reload activity
            currentLang = lang;
        }
    }
}