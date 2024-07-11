package com.example.moneytracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private EditText etDescription, etAmount, etCategory;
    private Button btnAddIncome, btnAddExpense, btnScanBarcode, btnViewTransactions, btnViewSummary;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is signed in
        if (!isUserSignedIn()) {
            // Redirect to SigninActivity
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
            return; // Exit the onCreate method
        }

        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnViewTransactions = findViewById(R.id.btnViewTransactions);
        btnViewSummary = findViewById(R.id.btnViewSummary);

        try {
            appDatabase = AppDatabase.getInstance(this);
            initializeDatabase();
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing database", e);
            Toast.makeText(this, "Error initializing database", Toast.LENGTH_SHORT).show();
        }

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });

        btnViewTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewTransactionsActivity.class));
            }
        });

        btnViewSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSummary();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scannedData = result.getContents();
                etDescription.setText(scannedData);
                Toast.makeText(this, "Scanned: " + scannedData, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isUserSignedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        return username != null;
    }

    private void initializeDatabase() {
        new Thread(() -> {
            UserDao userDao = appDatabase.userDao();
            if (userDao.getUserByUsername("testuser") == null) {
                User user = new User();
                user.setUsername("testuser");
                user.setPassword("testpassword");
                userDao.insert(user);

                IncomeDao incomeDao = appDatabase.incomeDao();
                Income income = new Income();
                income.setDescription("Sample Income");
                income.setAmount(1000);
                incomeDao.insertIncome(income);

                ExpenseDao expenseDao = appDatabase.expenseDao();
                Expense expense = new Expense();
                expense.setDescription("Sample Expense");
                expense.setAmount(500);
                expense.setCategory("Sample Category");
                expenseDao.insertExpense(expense);
            }
        }).start();
    }

    private void addIncome() {
        String description = etDescription.getText().toString();
        String amountStr = etAmount.getText().toString();

        if (description.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter description and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        Income income = new Income();
        income.setDescription(description);
        income.setAmount(amount);

        new Thread(() -> {
            appDatabase.incomeDao().insertIncome(income);
            runOnUiThread(() -> {
                Toast.makeText(this, "Income added", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                etAmount.setText("");
            });
        }).start();
    }

    private void addExpense() {
        String description = etDescription.getText().toString();
        String amountStr = etAmount.getText().toString();
        String category = etCategory.getText().toString();

        if (description.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please enter description, amount, and category", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCategory(category);

        new Thread(() -> {
            appDatabase.expenseDao().insertExpense(expense);
            runOnUiThread(() -> {
                Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                etAmount.setText("");
                etCategory.setText("");
            });
        }).start();
    }

    private void viewSummary() {
        startActivity(new Intent(MainActivity.this, SummaryActivity.class));
    }
}
