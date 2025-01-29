package com.glorious_vacation.d424_vacation_planner_mobile_app.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glorious_vacation.d424_vacation_planner_mobile_app.R;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpcomingVacationsActivity extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_vacations);

        TextView timestamp = findViewById(R.id.timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        timestamp.setText("Report generated on: " + currentTime);


        repository = new Repository(getApplication());

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());

        List<Vacation> upcomingVacations = repository.getUpcomingVacations(today);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        // Populate RecyclerView
        vacationAdapter.setVacations(upcomingVacations);

        // Handle empty state
        if (upcomingVacations.isEmpty()) {
            Toast.makeText(this, "No upcoming vacations found!", Toast.LENGTH_SHORT).show();
        }

        List<Vacation> allVacations = repository.getmAllVacations();
        for (Vacation vacation : allVacations) {
            Log.d("DEBUG", "Vacation: " + vacation.getVacationTitle() + ", Start Date: " + vacation.getStartDate());
        }



    }
}