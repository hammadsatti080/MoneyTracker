package com.example.moneytracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnSignup;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        appDatabase = AppDatabase.getInstance(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Check if user already exists
                User existingUser = appDatabase.userDao().getUserByUsername(username);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Username already exists. Please choose another one.", Toast.LENGTH_SHORT).show());
                } else {
                    // Save user to database
                    User user = new User(username, password);
                    appDatabase.userDao().insert(user);

                    // Save user info to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.apply();

                    // Redirect to MainActivity
                    runOnUiThread(() -> {
                        Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Prevent going back to signup activity on back press
                    });
                }
            }
        });
    }
}
