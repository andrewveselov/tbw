package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 4
// Homework 30-Dec-2018
// Andrew Veselov
//
// 1. В погодном приложении сделать сохранение и загрузку настроек (например, выбранный домашний город).
//
// 2. * Сделать текстовый мини-браузер с применением WebView, OkHttp и полем ввода страницы.
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
