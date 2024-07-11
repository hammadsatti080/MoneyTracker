package com.example.moneytracker;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM user_table WHERE username = :username")
    User getUserByUsername(String username);
}

