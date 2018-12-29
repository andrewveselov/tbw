package com.veselov.andrew.tbw.ui.activities.activities;
// Android Level 2 Lesson 3
// Homework 26-Dec-2018
// Andrew Veselov
//
// 1. Создать приложение с любой тяжелой обработкой на основе AsyncTask.
//

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.interfaces.OnWorkoutListItemSelectedListener;
import com.veselov.andrew.tbw.ui.activities.fragments.TempAndHum;
import com.veselov.andrew.tbw.ui.activities.fragments.WorkoutAboutFragment;
import com.veselov.andrew.tbw.ui.activities.fragments.WorkoutDetailFragment;
import com.veselov.andrew.tbw.ui.activities.fragments.WorkoutExternalPageFragment;
import com.veselov.andrew.tbw.ui.activities.fragments.WorkoutListFragment;

public class MainActivity extends AppCompatActivity implements OnWorkoutListItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    android.support.v4.app.FragmentManager fragmentManager;
    WorkoutListFragment listFragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavDrawer(toolbar);
        listFragment = new WorkoutListFragment();
        fragmentManager = getSupportFragmentManager();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction().replace(R.id.port_container, listFragment).commit();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WorkoutDetailFragment detailFragment = WorkoutDetailFragment.getInstance(0);
            fragmentManager.beginTransaction().replace(R.id.land_list_container, listFragment).commit();
            fragmentManager.beginTransaction().replace(R.id.land_detail_container, detailFragment).commit();
        }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_about) {
            WorkoutAboutFragment workoutAboutFragment = new WorkoutAboutFragment();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction().replace(R.id.port_container, workoutAboutFragment).addToBackStack(null).commit();}
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                fragmentManager.beginTransaction().replace(R.id.land_detail_container, workoutAboutFragment).commit();
            }
        } else if (id == R.id.nav_tempandhum) {
            TempAndHum tempAndHum = new TempAndHum();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                fragmentManager.beginTransaction().replace(R.id.port_container, tempAndHum).addToBackStack(null).commit();}
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                fragmentManager.beginTransaction().replace(R.id.land_detail_container, tempAndHum).commit();
            }

        } else if (id == R.id.nav_external_web) {
            WorkoutExternalPageFragment workoutExternalPageFragment = new WorkoutExternalPageFragment();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                fragmentManager.beginTransaction().replace(R.id.port_container, workoutExternalPageFragment).addToBackStack(null).commit();
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                fragmentManager.beginTransaction().replace(R.id.land_detail_container, workoutExternalPageFragment).commit();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initNavDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.workout_navi_user_name, R.string.workout_navi_user_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
}
