package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 4
// Homework 30-Dec-2018
// Andrew Veselov
//
// 1. В погодном приложении сделать сохранение и загрузку настроек (например, выбранный домашний город).
//
// 2. * Сделать текстовый мини-браузер с применением WebView, OkHttp и полем ввода страницы.
//


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.veselov.andrew.tbw.interfaces.OnWorkoutListItemSelectedListener;
import com.veselov.andrew.tbw.model.Singleton;
import com.veselov.andrew.tbw.model.Workout;
import com.veselov.andrew.tbw.model.WorkoutList;
import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.utils.Constants;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutDetailFragment extends Fragment {
    // Title
    private TextView workoutTitle;
    // Picture
    private ImageView workoutPicture;
    // Description
    private TextView workoutDescription;
    // Number attempts
    private SeekBar attemptCountSeekBar;
    private TextView attemptCountTextView;
    private final int minimumValue = 1;
    //Блок количества подходов
    private Button incButton;
    private Button decButton;
    private Button saveRecordButton;
    // Record area
    private LinearLayout recordArea;
    private TextView textViewRecordAttemptCount;
    private TextView textViewRecordRepeatCount;
    private TextView textViewRecordTimer;
    private TextView textViewDateRecordDate;
    private Button shareRecord;

    private int workoutIndex;
    // TAG ID for logging
    private static final String TAG = "WorkoutDetailFragment";

    FragmentManager fragmentManager;
    OnWorkoutListItemSelectedListener callbackActivity;
    private static String FRAGMENT_INSTANCE_NAME = "timer_fragment";

    public static WorkoutDetailFragment getInstance(int workoutIndex){
        WorkoutDetailFragment fragment = new WorkoutDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.WORKOUT_INDEX,workoutIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutIndex = getArguments().getInt(Constants.WORKOUT_INDEX, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workout_detail,container,false);
        initGUI(root);
        initTimerFragment();
//        fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.timer_container, new WorkoutTimerFragment()).commit();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackActivity = (OnWorkoutListItemSelectedListener) context;
    }


    @Override
    public void onPause() {
        super.onPause();
        // Testing singleton
        Singleton st = Singleton.getInstance();
        st.setAttempts(attemptCountSeekBar.getProgress() + minimumValue);
        Log.d(TAG,"onPause() Saving attempts result " + st.getAttempts());
    }


    private void initGUI(View root) {
        // Init workout
        Workout workout = WorkoutList.getInstance().getWorkoutByIndex(workoutIndex);
        // Check a non-existent exercise
        if (workout == null) {
            Toast.makeText(getContext(), getString(R.string.workout_detail_error_choose_exercise), Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
        else {
            // Define all areas
            // Title
            workoutTitle = root.findViewById(R.id.workout_detail_title);
            // Picture
            workoutPicture = root.findViewById(R.id.workout_detail_picture);
            // Description
            workoutDescription = root.findViewById(R.id.workout_detail_description);
            // Attempts
            attemptCountSeekBar = root.findViewById(R.id.workout_detail_seek_bar);
            attemptCountTextView = root.findViewById(R.id.workout_detail_attempt_count_text_view);
            // Repeats
            saveRecordButton = root.findViewById(R.id.workout_detail_repeat_counter);
            incButton = root.findViewById(R.id.workout_detail_repeat_inc);
            decButton = root.findViewById(R.id.workout_detail_repeat_dec);
            // Record
            textViewRecordAttemptCount = root.findViewById(R.id.workout_detail_record_attempt_count);
            textViewRecordRepeatCount = root.findViewById(R.id.workout_detail_record_repeat_count);
            textViewRecordTimer = root.findViewById(R.id.workout_detail_record_timer);
            textViewDateRecordDate = root.findViewById(R.id.workout_detail_record_date);
            recordArea = root.findViewById(R.id.workout_detail_record_area);
            saveRecordButton = root.findViewById(R.id.workout_detail_repeat_counter);
            shareRecord = root.findViewById(R.id.workout_detail_share);

            fillWorkout(workout);
            addListeners(workout);
        }
    }

    // Method fills workout data all areas
    private void fillWorkout(Workout workout) {
        // Title
        workoutTitle.setText(workout.getTitle());
        // Picture
        Picasso
                .get()
                .load(workout.getImageURL())
                .placeholder(R.drawable.ic_picture)
                .into(workoutPicture);
        // Description
        workoutDescription.setText(workout.getDescription());
        // Repeat count
        saveRecordButton.setText(String.valueOf(workout.getRepeatCount()));
        // Record
        printRecord(workout);
    }

    // Method creates listeners
    private void addListeners(final Workout workout){
        // Attempts
        attemptCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                attemptCountTextView.setText(String.valueOf(progress + minimumValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Repeats
        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For testing SharedPreferences
                SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Constants.WORKOUT_STOPWATCH_AUTO_START,true)) WorkoutTimerFragment.timerRunning = true; // Starting timer (if its not already running)
                workout.incRepeatCount();
                saveRecordButton.setText(String.valueOf(workout.getRepeatCount()));
            }
        });
        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workout.getRepeatCount() > 0) {
                    workout.decRepeatCount();
                    saveRecordButton.setText(String.valueOf(workout.getRepeatCount()));
                } else {
                    Toast.makeText(getContext(), getString(R.string.workout_detail_error_repeat), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Record
        saveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord(workout);
            }
        });

        // Share record
        shareRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareRecord(workout);
            }
        });
    }

    // Method checking result and saving the record
    private void saveRecord(Workout workout) {
        int result = (attemptCountSeekBar.getProgress() + minimumValue) * workout.getRepeatCount();
        if (result > (workout.getRecordAttemptCount() * workout.getRecordRepeatCount())) {
            WorkoutTimerFragment.timerRunning = false;
            workout.setSeconds(WorkoutTimerFragment.timerSeconds);
            workout.setRecordAttemptCount(attemptCountSeekBar.getProgress() + minimumValue);
            workout.setRecordRepeatCount(workout.getRepeatCount());
            workout.setRecordDate(new Date());
            printRecord(workout);
            callbackActivity.refreshList();
        } else {
            Toast.makeText(getContext(), getString(R.string.workout_detail_error_record), Toast.LENGTH_SHORT).show();
        }
    }

    // Method prints record data
    private void printRecord(Workout workout) {
        if (workout.getRecordDate() != null) {
            textViewRecordRepeatCount.setText(String.valueOf(workout.getRecordRepeatCount()));
            textViewRecordAttemptCount.setText(String.valueOf(workout.getRecordAttemptCount()));
            textViewRecordTimer.setText(String.valueOf(WorkoutTimerFragment.printTime(workout.getSeconds())));
            textViewDateRecordDate.setText(String.valueOf(new SimpleDateFormat(Constants.WORKOUT_DATE_FORMAT, Locale.ROOT)
                    .format(workout.getRecordDate())));
            recordArea.setVisibility(View.VISIBLE);
        }
    }

    // Method shares record with friend
    private void shareRecord(Workout workout) {
        String messageText = getString(R.string.workput_detail_record_message) + "\n\n" +
                            workout.getTitle() + "\n" +
                            getString(R.string.workout_detail_record_date) + " " + new SimpleDateFormat(Constants.WORKOUT_DATE_FORMAT, Locale.ROOT).format(workout.getRecordDate()) + "\n" +
                            getString(R.string.workout_detail_record_attempts) + " " + workout.getRecordAttemptCount() + "\n" +
                            getString(R.string.workout_detail_record_repeats) + " " + workout.getRecordRepeatCount() + "\n" +
                            getString(R.string.workout_detail_record_timer) + " " + WorkoutTimerFragment.printTime(workout.getSeconds());
        System.out.println(messageText);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageText);
        String chooserTitle = getString(R.string.workput_detail_choose_sender_record);
        Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
        if (chooserIntent != null) {
            startActivity(chooserIntent);
        }
        else Toast.makeText(getContext(), getString(R.string.workput_detail_error_send_message), Toast.LENGTH_SHORT).show();
    }

    private void initTimerFragment() {
        FragmentManager fm = getChildFragmentManager();
        WorkoutTimerFragment fragment = new WorkoutTimerFragment();
        fm.beginTransaction().add(R.id.timer_container, fragment, FRAGMENT_INSTANCE_NAME).commit();
    }
}
