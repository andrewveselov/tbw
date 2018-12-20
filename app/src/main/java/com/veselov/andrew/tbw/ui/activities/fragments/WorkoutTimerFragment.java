package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 1 Lesson 8
// Homework 05-Jul-2018
// Andrew Veselov
//
// 1. Добавить во второй фрагмент вложенный фрагмент; (уже сделано в прошлом ДЗ)
//
// 2. *Создать разные ресурсы для портретной и ландшафтной ориентации (чтобы в портретной
//     ориентации отображать экраны по очереди и в ландшафтной - список упражнений и детали упражнения рядом).
//

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.utils.Constants;

import java.util.Locale;

public class WorkoutTimerFragment extends Fragment {
    private Handler handler;
    private Runnable runnable;
    private Button resetButton;
    private Button startButton;
    private Button stopButton;
    private TextView timeView;
    // I know this is a bad way to exchange data between fragments.
    // But I could not think of a way to do it in case of a timer.
    public static boolean timerRunning;
    public static int timerSeconds;
    // For testing onSaveInstanceState
    private static final String KEY_TIMER_SECONDS = "seconds";
    private static final String KEY_TIMER_RUNNING = "timerRunning";
    private static final String KEY_TIMER_WAS_RUNNING = "wasRunning";
    private boolean timerWasRunning;
    // TAG ID for logging
    private static final String TAG = "WorkoutTimerFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        setRetainInstance(false);
        View root = inflater.inflate(R.layout.fragment_workout_timer,container,false);
        resetButton = root.findViewById(R.id.workout_detail_timer_reset);
        startButton = root.findViewById(R.id.workout_detail_timer_start);
        stopButton = root.findViewById(R.id.workout_detail_timer_stop);
        timeView = root.findViewById(R.id.workout_detail_timer_value);
        timerSeconds = 0;
        timerRunning = false;
        timerWasRunning = false;

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerRunning = false;
                timerSeconds = 0;
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerRunning = true;
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerRunning = false;
            }
        });
        Log.d(TAG,"onCreateView() Called");
        runTimer(root);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
            timerSeconds = savedInstanceState.getInt(KEY_TIMER_SECONDS);
            timerRunning = savedInstanceState.getBoolean(KEY_TIMER_RUNNING);
            timerWasRunning = savedInstanceState.getBoolean(KEY_TIMER_WAS_RUNNING);
        }
        Log.d(TAG,"onCreate() Called");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        if (timerWasRunning) {
            timerRunning = true;
        }
        Log.d(TAG,"onStart() Called");
        super.onStart();
    }

    @Override
    public void onStop() {
        timerWasRunning = timerRunning;
        timerRunning = false;
        handler.removeCallbacks(runnable);
        Log.d(TAG,"onStop() Called");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(KEY_TIMER_SECONDS, timerSeconds);
        savedInstanceState.putBoolean(KEY_TIMER_RUNNING, timerRunning);
        savedInstanceState.putBoolean(KEY_TIMER_WAS_RUNNING, timerWasRunning);
        super.onSaveInstanceState(savedInstanceState);
    }

    // Method Stopwatch, handler based
    public  void runTimer(View root) {
        final int DELAY = 1000;
        Log.d(TAG,"runTimer() Called");
        runnable = new Runnable() {
            @Override
            public void run() {
                timeView.setText(printTime(timerSeconds));
                if (timerRunning) {
                    timerSeconds++;
                }
                handler.postDelayed(this, DELAY);
            }
        };
        handler = new Handler();
        handler.post(runnable);

    }

    // Method converts seconds to the string time format H:MM:SS
    public static String printTime(int timerSeconds) {
        final int MILLS_IN_SECOND = 3600;
        final int SECONDS_IN_MUNUTE = 60;
        int hours = timerSeconds / MILLS_IN_SECOND;
        int minutes = (timerSeconds % MILLS_IN_SECOND) / SECONDS_IN_MUNUTE;
        int secs = timerSeconds % 60;
        return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, secs);
    }
}
