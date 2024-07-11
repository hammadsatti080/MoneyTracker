package com.example.moneytracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewTransactionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransactionsAdapter adapter;
    private AppDatabase appDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_transactions, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appDatabase = AppDatabase.getInstance(getContext());
        loadTransactions();

        return view;
    }

    private void loadTransactions() {
        new Thread(() -> {
            List<Expense> expenses = appDatabase.expenseDao().getAllExpenses();
            List<Income> incomes = appDatabase.incomeDao().getAllIncomes();
            getActivity().runOnUiThread(() -> {
                adapter = new TransactionsAdapter(expenses, incomes);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
