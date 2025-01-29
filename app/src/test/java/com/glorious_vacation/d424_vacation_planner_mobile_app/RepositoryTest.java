package com.glorious_vacation.d424_vacation_planner_mobile_app;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.VacationDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.UserDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.dao.ExcursionDAO;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class RepositoryTest {
    private Repository repository;

    @Mock private VacationDAO mockVacationDAO;
    @Mock private UserDAO mockUserDAO;
    @Mock private ExcursionDAO mockExcursionDAO;

    @Before
    public void setUp() {
        // Initialize Mockito annotations for the mock objects
        MockitoAnnotations.initMocks(this);

        // Create the repository with mocked DAOs
        repository = new Repository(mockVacationDAO, mockUserDAO, mockExcursionDAO, Executors.newSingleThreadExecutor());
    }

    @Test
    public void testGetAllVacations() {
        // Fake data
        List<Vacation> fakeVacations = Arrays.asList(
                new Vacation(1, "Orlando", "Mariott", "2025-05-01", "2025-05-08"),
                new Vacation(2, "Paris", "Hilton", "2025-06-10", "2025-06-20")
        );

        // Mock DAO behavior
        when(mockVacationDAO.getAllVacations()).thenReturn(fakeVacations);

        // Call repository method
        List<Vacation> result = repository.getmAllVacations();

        // Verify results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Orlando", result.get(0).getVacationTitle());
    }

    @Test
    public void testAuthenticateUser() {
        // Fake user
        User fakeUser = new User("testUser", "hashedpassword");

        // Mock DAO behavior
        when(mockUserDAO.authenticate("testUser", "hashedpassword")).thenReturn(fakeUser);

        // Call repository method
        User result = repository.authenticateUser("testUser", "hashedpassword");

        // Verify results
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }
}
