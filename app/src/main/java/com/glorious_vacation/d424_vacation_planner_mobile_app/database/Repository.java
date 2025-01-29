package com.glorious_vacation.d424_vacation_planner_mobile_app.database;

import android.app.Application;
import android.util.Log;

import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.ExcursionDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.UserDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.VacationDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Excursion;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.User;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;

    private UserDAO mUserDAO;

    private List<Vacation> mAllVacations = new ArrayList<>();
    private List<Excursion> mAllExcursions = new ArrayList<>();

    //Dynamically calculate the optimal number of threads for the thread pool
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors() * 2 + 1;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
        mUserDAO = db.userDAO();
    }

    // Testing constructor
    public Repository(VacationDAO vacationDAO, UserDAO userDAO, ExcursionDAO excursionDAO, ExecutorService executorService) {
        this.mVacationDAO = vacationDAO;
        this.mUserDAO = userDAO;
        this.mExcursionDAO = excursionDAO;
    }

    public void insertUser(User user) {
        databaseExecutor.execute(() -> {
            mUserDAO.insert(user);
        });
    }

    public User authenticateUser(String username, String password) {
        try {
            return databaseExecutor.submit(() -> mUserDAO.authenticate(username, password)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Repository", "Authentication error: ", e);
            return null;
        }
    }

    public User getUserById(int userId) {
        try {
            return databaseExecutor.submit(() -> mUserDAO.getUserById(userId)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error fetching user: ", e);
            return null;
        }
    }

    // Add these methods to Repository.java
    public User findUserByUsername(String username) {
        try {
            return databaseExecutor.submit(() -> mUserDAO.findUserByUsername(username)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error finding user by username", e);
            return null;
        }
    }


    // retrieves vacations from the db, else creates a new db
    public List<Vacation> getmAllVacations() {

        try{
           return databaseExecutor.submit(mVacationDAO::getAllVacations).get();
        } catch(InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error fetching all vacations: ", e);
            return new ArrayList<>();
        }

    }

    //creates vacation creation method
    public void insert (Vacation vacation) {
        databaseExecutor.execute(() ->
            mVacationDAO.insert(vacation));
    }

    //creates vacation update method
    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
    }

    //creates vacation delete method
    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
    }

    //retrieves excursions from the db, else creates a new db
    public List<Excursion> getmAllExcursions() {
        try{
            return databaseExecutor.submit(mExcursionDAO::getAllExcursions).get();
        } catch(InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error fetching all excursions: ", e);
            return new ArrayList<>();
        }
    }

    //gets excursions associated with a vacation id
    public List<Excursion> getAssociatedExcursions(int vacationID) {
        try{
            return databaseExecutor.submit(() -> mExcursionDAO.getAssociatedExcursions(vacationID)).get();
        } catch(InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error fetching associated excursions: ", e);
            return new ArrayList<>();
        }
    }

    //creates excursion creation method
    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            Log.d("Repository", "Inserting excursion: " + excursion.getExcursionTitle());
            mExcursionDAO.insert(excursion);
        });
    }

    //creates excursion update method
    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
    }

    //creates excursion delete method
    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
    }

    public List<Vacation> searchVacations(String query) {
        try {
            return databaseExecutor.submit(() -> mVacationDAO.searchVacations("%" + query + "%")).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error during searchVacations: ", e);
            return new ArrayList<>(); // Return an empty list if there is an error
        }
    }

    public List<Vacation> getUpcomingVacations(String currentDate) {
        try {
            return databaseExecutor.submit(() -> mVacationDAO.getUpcomingVacations(currentDate)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Repository", "Error fetching upcoming vacations: ", e);
            return new ArrayList<>();
        }
    }

}