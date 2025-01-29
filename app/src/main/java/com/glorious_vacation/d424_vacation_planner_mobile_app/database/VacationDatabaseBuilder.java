package com.glorious_vacation.d424_vacation_planner_mobile_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.ExcursionDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.UserDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.VacationDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Excursion;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.User;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class, User.class }, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class}) //Register the date converter
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    public abstract UserDAO userDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    // determines asynchronous database
    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            //if synchronous database instead
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}