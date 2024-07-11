package com.example.moneytracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewTransactionsActivity extends AppCompatActivity {

    private LinearLayout layoutTransactions;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);

        layoutTransactions = findViewById(R.id.layoutTransactions);
        appDatabase = AppDatabase.getInstance(this);

        // Fetch and display transactions in the background
        new FetchTransactionsTask().execute();
    }

    private class FetchTransactionsTask extends AsyncTask<Void, Void, List<Transaction>> {

        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            List<Income> incomes = appDatabase.incomeDao().getAllIncomes();
            List<Expense> expenses = appDatabase.expenseDao().getAllExpenses();

            List<Transaction> transactions = new ArrayList<>();
            for (Income income : incomes) {
                transactions.add(new Transaction(income.getDescription(), income.getAmount(), "Income", ""));
            }

            for (Expense expense : expenses) {
                transactions.add(new Transaction(expense.getDescription(), expense.getAmount(), "Expense", expense.getCategory()));
            }

            return transactions;
        }

        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            for (Transaction transaction : transactions) {
                addTransactionToLayout(transaction);
            }
        }
    }

    private void addTransactionToLayout(Transaction transaction) {
        TextView textView = new TextView(this);
        textView.setText("Description: " + transaction.getDescription() + "\n" +
                "Amount: " + transaction.getAmount() + "\n" +
                "Type: " + transaction.getType() + "\n" +
                "Category: " + transaction.getCategory() + "\n");
        layoutTransactions.addView(textView);
    }

    private class Transaction {
        private String description;
        private double amount;
        private String type;
        private String category;

        public Transaction(String description, double amount, String type, String category) {
            this.description = description;
            this.amount = amount;
            this.type = type;
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public String getCategory() {
            return category;
        }
    }
}

