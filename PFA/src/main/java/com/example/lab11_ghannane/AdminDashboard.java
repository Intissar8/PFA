package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    Button manageProductsBtn, manageCommandsBtn, manageLivreursBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        manageProductsBtn = findViewById(R.id.btnManageProducts);
        manageCommandsBtn = findViewById(R.id.btnManageCommands);
        manageLivreursBtn = findViewById(R.id.btnManageLivreurs);
        logoutBtn = findViewById(R.id.btnLogout);

        manageProductsBtn.setOnClickListener(v -> {
            // Navigate to Manage Products Activity
            startActivity(new Intent(AdminDashboard.this, manage_produit.class));
        });

        manageCommandsBtn.setOnClickListener(v -> {
            // Navigate to Manage Commands Activity
         //   startActivity(new Intent(AdminDashboard.this, ManageCommandsActivity.class));
        });

        manageLivreursBtn.setOnClickListener(v -> {
            // Navigate to Manage Livreurs Activity
           startActivity(new Intent(AdminDashboard.this, manage_livreur.class));
        });

        logoutBtn.setOnClickListener(v -> {
            // Log out and return to login screen
            finish();
            startActivity(new Intent(AdminDashboard.this, MainActivity.class));
        });
    }
}
