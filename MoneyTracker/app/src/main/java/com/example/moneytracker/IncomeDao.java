package com.example.moneytracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncomeDao {
    @Insert
    void insertIncome(Income income);

    @Query("SELECT * FROM income_table")
    List<Income> getAllIncomes();
}

