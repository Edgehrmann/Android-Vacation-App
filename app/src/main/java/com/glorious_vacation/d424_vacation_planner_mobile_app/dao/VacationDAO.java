package com.glorious_vacation.d424_vacation_planner_mobile_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationTitle LIKE :query ORDER BY vacationTitle ASC")
    List<Vacation> searchVacations(String query);

    @Query("Select * FROM vacations WHERE startDate >= :currentDate ORDER BY startDate ASC")
    List<Vacation> getUpcomingVacations(String currentDate);
}
