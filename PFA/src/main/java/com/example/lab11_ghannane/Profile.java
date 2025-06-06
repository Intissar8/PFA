package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends BaseActivity {

    // Declare clickable layout sections
    LinearLayout icontext, icontext2, icontext3, icontext4, icontext5, icontext6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Make sure this matches your XML file name

        // Initialize views
        icontext = findViewById(R.id.icontext);
        icontext2 = findViewById(R.id.icontext2);
        icontext3 = findViewById(R.id.icontext3);
        icontext4 = findViewById(R.id.icontext4);
        icontext5 = findViewById(R.id.icontext5);
        icontext6 = findViewById(R.id.icontext6);

        // Example actions - replace with actual activity navigation or logic
        icontext.setOnClickListener(view -> {
            // Navigate to OrdersActivity or show a Toast, etc.
             startActivity(new Intent(Profile.this, delivery_status.class));
        });

        icontext2.setOnClickListener(view -> {
            // Navigate to ProfileDetailsActivity
            startActivity(new Intent(Profile.this, userinfo.class));
        });

        icontext3.setOnClickListener(view -> {
            // Navigate to LanguageSettingsActivity
            startActivity(new Intent(Profile.this, translationActivity.class));
        });

        icontext4.setOnClickListener(view -> {
            // Navigate to FAQActivity
            startActivity(new Intent(Profile.this, Faq.class));
        });

        icontext5.setOnClickListener(view -> {
            // Navigate to NotificationsActivity
            startActivity(new Intent(Profile.this, notification_settings.class));
        });

        icontext6.setOnClickListener(view -> {
            // Logout logic here
            logoutUser();
        });

        // Handle bottom nav (optional: give IDs to the inner LinearLayouts for more control)
        setupBottomNavigation();
    }

    private void logoutUser() {
        // Clear user session or preferences if needed
        // Redirect to LoginActivity
        Intent intent = new Intent(Profile.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        LinearLayout homeNav = findViewById(R.id.home_nav);
        LinearLayout productsNav = findViewById(R.id.products_nav);
        LinearLayout ordersNav = findViewById(R.id.orders_nav);
        LinearLayout profileNav = findViewById(R.id.profile_nav);

        homeNav.setOnClickListener(view -> {
            startActivity(new Intent(Profile.this, Home.class));
            finish(); // optional, to prevent backstack clutter
        });

        productsNav.setOnClickListener(view -> {
            // startActivity(new Intent(Profile.this, ProductsActivity.class));
            finish();
        });

        ordersNav.setOnClickListener(view -> {
          //  startActivity(new Intent(Profile.this, OrdersActivity.class));
            finish();
        });

        profileNav.setOnClickListener(view -> {
            // You're already on the profile screen, so maybe highlight it or do nothing
        });
    }

}