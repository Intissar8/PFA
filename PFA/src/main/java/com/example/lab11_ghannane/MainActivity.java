package com.example.lab11_ghannane;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
//step 1 : declaration
    EditText etMail,etPassword;
    Button blogin;

    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
       //step2 : Recuperation des ids
        etMail=findViewById(R.id.etMail);
        etPassword=findViewById(R.id.etPassword);
        blogin=findViewById(R.id.blogin);
        tvRegister=findViewById(R.id.tvRegister);

        //step3: Association de listeners
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //step4: traitement
                String email=etMail.getText().toString();
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(),"All fields must be filled !",Toast.LENGTH_SHORT).show();
                }
                else{

                    if(etMail.getText().toString().equals("Intissar") & etPassword.getText().toString().equals("123")){
                          startActivity(new Intent(MainActivity.this,Profile.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Login or password incorrect !",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //step4: traitement
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });



    }
}