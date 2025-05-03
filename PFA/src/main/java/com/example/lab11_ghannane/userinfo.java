package com.example.lab11_ghannane;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userinfo extends BaseActivity {

    TextView textEmail;
    EditText newPasswordInput;
    Button btnChangePassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo); // make sure this XML file matches

        textEmail = findViewById(R.id.text_email);
        newPasswordInput = findViewById(R.id.edit_new_password); // we'll add this to XML
        btnChangePassword = findViewById(R.id.btn_change_password);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            textEmail.setText(email);
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordInput.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
                    Toast.makeText(userinfo.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.updatePassword(newPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(userinfo.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            newPasswordInput.setText(""); // Clear the field
                        } else {
                            Toast.makeText(userinfo.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}