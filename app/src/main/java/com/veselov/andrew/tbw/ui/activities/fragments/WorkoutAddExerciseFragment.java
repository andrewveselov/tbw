package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 6
// Homework 12-Jan-2019
// Andrew Veselov
//
// 1. Переделать запросы и обработку ответа в погодном приложении на Retrofit 2.
//
// 2. * Оживить погодное приложение картинками из Интернета.
//

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.model.WorkoutList;

import java.util.Objects;

public class WorkoutAddExerciseFragment extends Fragment{
    private EditText workoutTitle;
    private EditText workoutDescription;
    private EditText workoutURL;

    public WorkoutAddExerciseFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_exercise,container,false);
        Button button = root.findViewById(R.id.workout_add_exercise_button);
        button.setOnClickListener(onClickListener);
        workoutTitle = root.findViewById(R.id.workout_add_exercise_title);
        workoutDescription = root.findViewById(R.id.workout_add_exercise_description);
        workoutURL = root.findViewById(R.id.workout_add_exercise_url);
        return root;
    }

    private final View.OnClickListener onClickListener = new
            View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (workoutTitle.getText().length() == 0 || workoutDescription.getText().length() == 0 || workoutURL.getText().length() == 0) {
                        Toast.makeText(getContext(), getString(R.string.workout_add_error_exercise), Toast.LENGTH_SHORT).show();
                    } else {
                        WorkoutList.getInstance()
                                .addWorkout(workoutTitle.getText().toString(),
                                        workoutDescription.getText().toString(),
                                        workoutURL.getText().toString());
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    }
                    }
            };
}