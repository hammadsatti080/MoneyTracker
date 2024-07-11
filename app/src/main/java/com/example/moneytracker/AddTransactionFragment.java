package com.example.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddTransactionFragment extends Fragment {

    private EditText etDescription, etAmount, etCategory;
    private Button btnAddIncome, btnAddExpense, btnScanBarcode;

    private AppDatabase appDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        etDescription = view.findViewById(R.id.etDescription);
        etAmount = view.findViewById(R.id.etAmount);
        etCategory = view.findViewById(R.id.etCategory);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);
        btnScanBarcode = view.findViewById(R.id.btnScanBarcode);

        appDatabase = AppDatabase.getInstance(getActivity());

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
                scanBarcode();
            }
        });

        return view;
    }

    private void addIncome() {
        String description = etDescription.getText().toString();
        String amountStr = etAmount.getText().toString();

        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(getActivity(), "Please enter description and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        Income income = new Income();
        income.setDescription(description);
        income.setAmount(amount);

        new Thread(() -> {
            appDatabase.incomeDao().insertIncome(income);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Income added", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                etAmount.setText("");
            });
        }).start();
    }

    private void addExpense() {
        String description = etDescription.getText().toString();
        String amountStr = etAmount.getText().toString();
        String category = etCategory.getText().toString();

        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(category)) {
            Toast.makeText(getActivity(), "Please enter description, amount, and category", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCategory(category);

        new Thread(() -> {
            appDatabase.expenseDao().insertExpense(expense);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Expense added", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                etAmount.setText("");
                etCategory.setText("");
            });
        }).start();
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scannedData = result.getContents();
                etDescription.setText(scannedData);
                Toast.makeText(getActivity(), "Scanned data: " + scannedData + ". Please enter amount and description.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
