package com.example.alarmclockapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import androidx.appcompat.app.AppCompatActivity;

public class StopwatchActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        chronometer = findViewById(R.id.chronometer);
        Button startBtn = findViewById(R.id.startBtn);
        Button pauseBtn = findViewById(R.id.pauseBtn);
        Button resetBtn = findViewById(R.id.resetBtn);

        startBtn.setOnClickListener(v -> {
            if (!running) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                running = true;
            }
        });

        pauseBtn.setOnClickListener(v -> {
            if (running) {
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                running = false;
            }
        });

        resetBtn.setOnClickListener(v -> {
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        });
    }
}
