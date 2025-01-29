package com.glorious_vacation.d424_vacation_planner_mobile_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glorious_vacation.d424_vacation_planner_mobile_app.R;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Excursion;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        //make the Add Vacations button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        //creates a RecyclerView list and populates it with all vacations in the db
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

    }

    //make menu and populate it with menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    //needs to be duplicated here because it is outside of onCreate()
    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    //define what will happen when a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //takes user back to home
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        //manually adds sample vacations and excursions to db when user clicks My Sample
        if (item.getItemId() == R.id.mysample) {
            repository = new Repository(getApplication());
            Vacation vacation = new Vacation(0, "Orlando Florida", "Mariott", "2025-05-01", "2025-05-08");
            repository.insert(vacation);
            vacation = new Vacation(0, "Madrid Spain", "Hilton", "2025-11-05", "2025-11-10");
            repository.insert(vacation);
            vacation = new Vacation(0, "Fort Myers Florida", "Baymont", "2025-04-07", "2025-04-11" );
            repository.insert(vacation);
            vacation = new Vacation(0, "Cancun Mexico", "Hotel Riu", "2025-07-04", "2025-07-13" );
            repository.insert(vacation);
            vacation = new Vacation(0, "Tijuana Mexico", "Grand Hotel Tijuana", "2025-06-03", "2025-06-10");
            repository.insert(vacation);
            Excursion excursion = new Excursion(0, "Cycling", 1, "2025-11-06");
            repository.insert(excursion);
            excursion = new Excursion(0, "Wine Tasting", 1, "2025-11-07" );
            repository.insert(excursion);
            return true;
        }

        return true;
    }
}