package com.glorious_vacation.d424_vacation_planner_mobile_app.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.Context;
import android.app.AlarmManager;
import android.content.Intent;

import android.app.DatePickerDialog;



import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.R;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Excursion;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.Vacation;

public class VacationDetails extends AppCompatActivity {

    String title;
    String hotel;
    int vacationID;
    String setStartDate;
    String setEndDate;

    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    List<Excursion> filteredExcursions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel  = getIntent().getStringExtra("hotel");
        setStartDate = getIntent().getStringExtra("startDate");
        setEndDate = getIntent().getStringExtra("endDate");
        editTitle.setText(title);
        editHotel.setText(hotel);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });

        //finds the excursions associated with the vacation and populates the RecyclerView list with it
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Excursion e: repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setmExcursions(filteredExcursions);



        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (setStartDate != null) {
            try {
                Date startDate = sdf.parse(setStartDate);
                Date endDate = sdf.parse(setEndDate);
                myCalendarStart.setTime(startDate);
                myCalendarEnd.setTime(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editStartDate.getText().toString();
                if (info.equals("")) info = setStartDate;
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //sets the calendar instance to whatever is selected
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editEndDate.getText().toString();
                if (info.equals("")) info = setEndDate;
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //sets the calendar instance to whatever is selected
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };
    }

    //updates the date displayed after it has been chosen
    private void updateLabelStart() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formattedDate = sdf.format(myCalendarStart.getTime());
        if (isValidDate(formattedDate)) {
            editStartDate.setText(formattedDate);
        } else {
            Toast.makeText(this, "Invailid start  date format!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLabelEnd() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formattedDate = sdf.format(myCalendarEnd.getTime());
        if (isValidDate(formattedDate)) {
            editEndDate.setText(formattedDate);
        } else {
            Toast.makeText(this, "Invalid end date format!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDate(String date) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.US);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    //creates the menu and fills it with the items
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //takes user back to VacationList
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        //if the user selects the Save Vacation menu option...
        if (item.getItemId() == R.id.vacationsave) {
            String titleInput = editTitle.getText().toString().trim();
            String hotelInput = editHotel.getText().toString().trim();
            String startDateString = editStartDate.getText().toString().trim();
            String endDateString = editEndDate.getText().toString().trim();

            if (titleInput.isEmpty() || hotelInput.isEmpty() || startDateString.isEmpty() || endDateString.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return true;
            }

            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            try {
                Date startDate = sdf.parse(startDateString);
                Date endDate = sdf.parse(endDateString);

                if (endDate.before(startDate)) {
                    Toast.makeText(this, "End date must be AFTER start date", Toast.LENGTH_LONG).show();
                    return true;
                }

                Vacation vacation;
                if (vacationID == -1) {
                    if (repository.getmAllVacations().isEmpty()) vacationID = 1;
                    else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationId() + 1;

                    vacation = new Vacation(vacationID, titleInput, hotelInput, startDateString, endDateString);
                    repository.insert(vacation);
                } else {
                    vacation = new Vacation(vacationID, titleInput, hotelInput, startDateString, endDateString);
                    repository.update(vacation);
                }

                Toast.makeText(this, "Vacation saved successfully!", Toast.LENGTH_SHORT).show();
                this.finish();
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date format!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        //if the user selects the menu option Delete Vacation...
        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vac : repository.getmAllVacations()) {
                if (vac.getVacationId() == vacationID) currentVacation = vac;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }
            //if the vacation has any associated excursions, prevent deletion of the vacation, otherwise delete it
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
            }
        }
        //sets alert for Start Date
        if (item.getItemId() == R.id.alertstart) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + title + " is starting";
            alertPicker(dateFromScreen, alert);

            return true;
        }

        //sets alert for End Date
        if (item.getItemId() == R.id.alertend) {
            String dateFromScreen = editEndDate.getText().toString();
            String alert = "Vacation " + title + " is ending";
            alertPicker(dateFromScreen, alert);

            return true;
        }

        //sets alert for both Start Date and End Date
        if (item.getItemId() == R.id.alertfull) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + title + " is starting";
            alertPicker(dateFromScreen, alert);
            dateFromScreen = editEndDate.getText().toString();
            alert = "Vacation " + title + " is ending";
            alertPicker(dateFromScreen, alert);

            return true;
        }

        //allows the Vacation info to be shared
        //prepares contents of the note input to share
        if (item.getItemId() == R.id.share) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Shared");
            //need to put the items into a constructed String or else each item will replace the last
            StringBuilder shareData = new StringBuilder();
            shareData.append("Vacation title: " + editTitle.getText().toString() + "\n");
            shareData.append("Hotel name: " + editHotel.getText().toString() + "\n");
            shareData.append("Start Date: " + editStartDate.getText().toString() + "\n");
            shareData.append("End Date: " + editEndDate.getText().toString() + "\n");
            for (int i = 0; i < filteredExcursions.size(); i++) {
                shareData.append("Excursion " + (i + 1) + ": " + filteredExcursions.get(i).getExcursionTitle() + "\n");
                shareData.append("Excursion " + (i + 1) + " Date: " + filteredExcursions.get(i).getExcursionDate() + "\n");
            }
            sentIntent.putExtra(Intent.EXTRA_TEXT, shareData.toString());
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }

        return true;
    }

    //the logic to set the date alarm, inclusive of start/end/both
    public void alertPicker(String dateFromScreen, String alert) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = myDate.getTime();
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", alert);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //finds the excursions associated with the vacation and populates the RecyclerView list with it
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Filter excursions associated with the current vacation
        List<Excursion> filteredExcursions = repository.getAssociatedExcursions(vacationID);

        //Handle empty excursion list
        if (filteredExcursions.isEmpty()) {
            Toast.makeText(this, "No excursions found for this vacation.", Toast.LENGTH_SHORT).show();
        } else {
            excursionAdapter.setmExcursions(filteredExcursions);
            excursionAdapter.notifyDataSetChanged();
        }

        // Update labels
        updateLabelStart();
        updateLabelEnd();

        Log.d("Debug", "Vacation ID: " + vacationID);
        for (Excursion e : filteredExcursions) {
            Log.d("Debug", "Excursion: " + e.getExcursionTitle() + ", Vacation ID: " + e.getVacationID());
        }
    }
}