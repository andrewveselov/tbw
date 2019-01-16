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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veselov.andrew.tbw.R;

public class WorkoutAboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
