package com.glorious_vacation.d424_vacation_planner_mobile_app.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findUserByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username and passwordHash = :passwordHash LIMIT 1")
    User authenticate(String username, String passwordHash);

    @Query("SELECT * FROM users WHERE userId = :id LIMIT 1")
    User getUserById(int id);
}
