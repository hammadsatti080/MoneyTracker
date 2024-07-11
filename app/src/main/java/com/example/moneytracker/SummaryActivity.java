package com.example.moneytracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private TextView tvSummary;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        tvSummary = findViewById(R.id.tvSummary);
        appDatabase = AppDatabase.getInstance(this);

        // Perform the database operations in the background
        new FetchSummaryTask().execute();
    }

    private class FetchSummaryTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            List<Income> incomes = appDatabase.incomeDao().getAllIncomes();
            List<Expense> expenses = appDatabase.expenseDao().getAllExpenses();

            double totalIncome = 0;
            double totalExpense = 0;

            for (Income income : incomes) {
                totalIncome += income.getAmount();
            }

            for (Expense expense : expenses) {
                totalExpense += expense.getAmount();
            }

            double balance = totalIncome - totalExpense;

            return "Total Income: " + totalIncome + "\n" +
                    "Total Expense: " + totalExpense + "\n" +
                    "Balance: " + balance;
        }

        @Override
        protected void onPostExecute(String summary) {
            tvSummary.setText(summary);
        }
    }
}
