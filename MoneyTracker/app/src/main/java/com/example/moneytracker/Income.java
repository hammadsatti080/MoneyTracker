package com.example.moneytracker;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "income_table")
public class Income {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private double amount;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}

