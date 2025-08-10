package com.example.alarmclockapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView currentTime;
    private Button setAlarmBtn, stopwatchBtn;
    private CheckBox mon, tue, wed, thu, fri, sat, sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTime = findViewById(R.id.currentTime);
        setAlarmBtn = findViewById(R.id.setAlarmBtn);
        stopwatchBtn = findViewById(R.id.stopwatchBtn);

        // Day checkboxes
        mon = findViewById(R.id.monCheck);
        tue = findViewById(R.id.tueCheck);
        wed = findViewById(R.id.wedCheck);
        thu = findViewById(R.id.thuCheck);
        fri = findViewById(R.id.friCheck);
        sat = findViewById(R.id.satCheck);
        sun = findViewById(R.id.sunCheck);

        updateTime();

        setAlarmBtn.setOnClickListener(v -> showTimePicker());
        stopwatchBtn.setOnClickListener(v -> startActivity(new Intent(this, StopwatchActivity.class)));
    }

    private void updateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(Calendar.getInstance().getTime());
                currentTime.setText(time);
                handler.postDelayed(this, 1000);
            }
        }, 10);
    }

    private void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            List<Integer> selectedDays = new ArrayList<>();
            if (mon.isChecked()) selectedDays.add(Calendar.MONDAY);
            if (tue.isChecked()) selectedDays.add(Calendar.TUESDAY);
            if (wed.isChecked()) selectedDays.add(Calendar.WEDNESDAY);
            if (thu.isChecked()) selectedDays.add(Calendar.THURSDAY);
            if (fri.isChecked()) selectedDays.add(Calendar.FRIDAY);
            if (sat.isChecked()) selectedDays.add(Calendar.SATURDAY);
            if (sun.isChecked()) selectedDays.add(Calendar.SUNDAY);

            if (selectedDays.isEmpty()) {
                // If no day selected, set for today only
                selectedDays.add(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }

            for (int day : selectedDays) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, day);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }

                Intent intent = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, day, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }, 12, 0, true);
        timePicker.show();
    }
}
