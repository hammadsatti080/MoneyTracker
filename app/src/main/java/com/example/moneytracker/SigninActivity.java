package com.example.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class SigninActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnSignin, btnSignup;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignin = findViewById(R.id.btnSignin);
        btnSignup = findViewById(R.id.btnSignup);

        appDatabase = AppDatabase.getInstance(this);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }

    private void signin() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                User user = appDatabase.userDao().getUserByUsername(username);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null && user.getPassword().equals(password)) {
                            // Successful sign in, proceed to MainActivity
                            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Call finish to prevent going back to sign-in activity on back press
                        } else {
                            // Invalid username or password
                            Toast.makeText(SigninActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
