package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 5
// Homework 09-Jan-2019
// Andrew Veselov
//
// 1. Посвятите это время работе над вашим проектом. Ваше портфолио - это ваше лицо для будущих работодателей.
//
// 2. Доделайте те задания, которые не успели сделать к предыдущим урокам.
//
// 3*. Добавьте в ваш проект какие-нибудь новые фичи на ваше усмотрение.
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
