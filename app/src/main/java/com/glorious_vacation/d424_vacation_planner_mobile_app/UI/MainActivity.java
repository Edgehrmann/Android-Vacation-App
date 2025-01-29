package com.glorious_vacation.d424_vacation_planner_mobile_app.UI;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glorious_vacation.d424_vacation_planner_mobile_app.R;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;

    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        repository = new Repository(getApplication());

        //Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this
       ));

        List<Vacation> allVacations = repository.getmAllVacations();
        vacationAdapter.setVacations(allVacations);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    List<Vacation> filteredVacations = repository.searchVacations(newText);
                    vacationAdapter.setVacations(filteredVacations);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {

                    vacationAdapter.setVacations(new ArrayList<>()); //Clear adapter
                    recyclerView.setVisibility(View.GONE); //Hide Recycler View
                }
                return true;
            }
        });

        // Add a listener for when the search bar is closed
        searchView.setOnCloseListener(()-> {
            vacationAdapter.setVacations(new ArrayList<>()); //Clear adapter
            recyclerView.setVisibility(View.GONE); //Hide RecyclerView
            return false; //Allow default close behavior
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VacationList.class);
                intent.putExtra("test", "Information sent");
                startActivity(intent);
            }
        });

        Button btnUpcomingVacations = findViewById(R.id.btnUpcomingVacations);
        btnUpcomingVacations.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UpcomingVacationsActivity.class);
            startActivity(intent);
        });

        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //    return insets;
        //});
    }
}