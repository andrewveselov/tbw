package com.veselov.andrew.tbw.ui.activities.activities;
// Android Level 1 Lesson 8
// Homework 05-Jul-2018
// Andrew Veselov
//
// 1. Добавить во второй фрагмент вложенный фрагмент; (уже сделано в прошлом ДЗ)
//
// 2. *Создать разные ресурсы для портретной и ландшафтной ориентации (чтобы в портретной
//     ориентации отображать экраны по очереди и в ландшафтной - список упражнений и детали упражнения рядом).
//

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.interfaces.OnWorkoutListItemSelectedListener;
import com.veselov.andrew.tbw.ui.activities.fragments.WorkoutDetailFragment;
import com.veselov.andrew.tbw.ui.activities.fragments.WorkoutListFragment;

public class MainActivity extends AppCompatActivity implements OnWorkoutListItemSelectedListener{
    android.support.v4.app.FragmentManager fragmentManager;
    WorkoutListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFragment = new WorkoutListFragment();
        fragmentManager = getSupportFragmentManager();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction().replace(R.id.port_container, listFragment).commit();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WorkoutDetailFragment detailFragment = WorkoutDetailFragment.getInstance(0);
            fragmentManager.beginTransaction().replace(R.id.land_list_container, listFragment).commit();
            fragmentManager.beginTransaction().replace(R.id.land_detail_container, detailFragment).commit();
        }


//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.container, listFragment).addToBackStack(null).commit();

/*        WorkoutListFragment listFragment = new WorkoutListFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, listFragment);
        transaction.commit();
*/

    }

    @Override
    public void onWorkoutListItemSelected(int position) {
        WorkoutDetailFragment detailFragment = WorkoutDetailFragment.getInstance(position);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction().replace(R.id.port_container, detailFragment).addToBackStack(null).commit();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction().replace(R.id.land_detail_container, detailFragment).commit();
        }
    }

    @Override
    public void refreshList() {
        listFragment.refreshList();
    }

}
