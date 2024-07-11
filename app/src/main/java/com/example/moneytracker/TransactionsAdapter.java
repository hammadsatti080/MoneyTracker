package com.example.moneytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EXPENSE = 1;
    private static final int TYPE_INCOME = 2;

    private List<Expense> expenses;
    private List<Income> incomes;

    public TransactionsAdapter(List<Expense> expenses, List<Income> incomes) {
        this.expenses = expenses;
        this.incomes = incomes;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < incomes.size()) {
            return TYPE_INCOME;
        } else {
            return TYPE_EXPENSE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_INCOME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
            return new IncomeViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
            return new ExpenseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_INCOME) {
            Income income = incomes.get(position);
            ((IncomeViewHolder) holder).bind(income);
        } else {
            Expense expense = expenses.get(position - incomes.size());
            ((ExpenseViewHolder) holder).bind(expense);
        }
    }

    @Override
    public int getItemCount() {
        return incomes.size() + expenses.size();
    }

    static class IncomeViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvAmount;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(Income income) {
            tvDescription.setText(income.getDescription());
            tvAmount.setText(String.valueOf(income.getAmount()));
        }
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvAmount, tvCategory;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }

        public void bind(Expense expense) {
            tvDescription.setText(expense.getDescription());
            tvAmount.setText(String.valueOf(expense.getAmount()));
            tvCategory.setText(expense.getCategory());
        }
    }
}
