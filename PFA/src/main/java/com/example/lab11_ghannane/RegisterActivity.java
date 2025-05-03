package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends BaseActivity {
    EditText etname, etMail, etPassword, etconfirm_password;
    Button blogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname = findViewById(R.id.etusername);
        etMail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etpassword);
        etconfirm_password = findViewById(R.id.etconfirm_password);
        blogin = findViewById(R.id.bregister);

        mAuth = FirebaseAuth.getInstance();

        blogin.setOnClickListener(v -> {
            String mail = etMail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String password1 = etconfirm_password.getText().toString().trim();

            if (TextUtils.isEmpty(mail)) {
                Toast.makeText(this, getString(R.string.empty_fields_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, getString(R.string.empty_fields_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password1)) {
                Toast.makeText(this, getString(R.string.confirm_password_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, getString(R.string.password_length_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(password1)) {
                Toast.makeText(this, getString(R.string.password_mismatch_warning), Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase registration
            mAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.registration_failed) + ": " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
