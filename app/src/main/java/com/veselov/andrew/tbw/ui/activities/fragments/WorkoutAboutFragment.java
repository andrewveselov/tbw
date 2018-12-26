package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 2
// Homework 22-Dec-2018
// Andrew Veselov
//
// 1. Добавить датчики температуры и влажности (TYPE_AMBIENT_TEMPERATURE, TYPE_ABSOLUTE_HUMIDITY).
//    Показывать текущую температуру, влажность в погодном приложении (если такой датчик присутствует
//    в аппарате).
//
// 2. * Создать свой элемент View и использовать его в погодном приложении.
//

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veselov.andrew.tbw.R;


public class WorkoutAboutFragment extends Fragment {

    public WorkoutAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }



}
