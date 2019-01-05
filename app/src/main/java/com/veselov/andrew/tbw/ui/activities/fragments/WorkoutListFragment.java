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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.squareup.picasso.Picasso;
import com.veselov.andrew.tbw.interfaces.OnWorkoutListItemSelectedListener;
import com.veselov.andrew.tbw.model.Workout;
import com.veselov.andrew.tbw.model.WorkoutList;
import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.utils.Constants;

public class WorkoutListFragment extends Fragment {
    // TAG ID for logging
    private static final String TAG = "WorkoutListFragment";

    // Workouts
    private List<Workout> workouts;
    WorkoutAdapter workoutAdapter;
    View root;

    OnWorkoutListItemSelectedListener callbackActivity;

    public void refreshList() {
        workoutAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackActivity = (OnWorkoutListItemSelectedListener) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.workout_list_main, menu);
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
        MenuItem timer = menu.findItem(R.id.workout_list_main_auto_timer);
        MenuItem favorites = menu.findItem(R.id.workout_list_main_show_favorites);
        timer.setChecked(preferences.getBoolean(Constants.WORKOUT_STOPWATCH_AUTO_START,true));
        favorites.setChecked(preferences.getBoolean(Constants.WORKOUT_FAVORITES,false));
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor sharedPreferencesEditor = preferences.edit();
        switch (item.getItemId()) {
            case R.id.workout_list_main_auto_timer:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                sharedPreferencesEditor.putBoolean(Constants.WORKOUT_STOPWATCH_AUTO_START, item.isChecked());
                sharedPreferencesEditor.apply();
                return super.onOptionsItemSelected(item);
            case R.id.workout_list_main_show_favorites:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                sharedPreferencesEditor.putBoolean(Constants.WORKOUT_FAVORITES, item.isChecked());
                sharedPreferencesEditor.apply();
                showListWorkouts(root);
                return super.onOptionsItemSelected(item);
            case R.id.workout_list_main_add_ex:
//                FragmentManager fragmentManager = getFragmentManager();
//                WorkoutAddExerciseFragment addExerciseFragment = new WorkoutAddExerciseFragment();
//                fragmentManager.beginTransaction().replace(R.id.container,addExerciseFragment).addToBackStack(null).commit();
                noWorking();
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate() Called");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_workout_list,container,false);
        showListWorkouts(root);
        return root;
    }

    private static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        CardView workoutListItem;
        // Header of cards
        TextView textViewItemTitle;
        TextView textViewItemDescription;
        ImageView imageItemPicture;
        ImageView imageItemMenu;
        // Record area of cards
        View horizontalSeparator;
        LinearLayout linearLayoutItemRecordArea;
        TextView textViewItemRecordDate;
        TextView textViewItemRecordAttemptCount;
        TextView textViewItemRecordRepeatCount;
        TextView textViewItemRecordTimer;

        private WorkoutViewHolder(View itemView) {
            super(itemView);

            workoutListItem = itemView.findViewById(R.id.workout_list_item);
            textViewItemTitle = itemView.findViewById(R.id.workout_list_item_title);
            textViewItemDescription = itemView.findViewById(R.id.workout_list_item_description);
            imageItemPicture = itemView.findViewById(R.id.workout_list_item_image);
            imageItemMenu = itemView.findViewById(R.id.workout_list_item_menu);
            // Record area
            horizontalSeparator = itemView.findViewById(R.id.horizontal_separator);
            linearLayoutItemRecordArea = itemView.findViewById(R.id.workout_list_item_record_area);
            textViewItemRecordTimer = itemView.findViewById(R.id.workout_list_item_record_timer);
            textViewItemRecordAttemptCount = itemView.findViewById(R.id.workout_list_item_record_attempt_count);
            textViewItemRecordRepeatCount = itemView.findViewById(R.id.workout_list_item_record_repeat_count);
            textViewItemRecordDate = itemView.findViewById(R.id.workout_list_item_record_date);
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutViewHolder> {
        List<Workout> workouts;
        Context context;

        private WorkoutAdapter(List<Workout> workouts, Context context) {
            this.workouts = workouts;
            this.context = context;
        }

        @NonNull
        @Override
        public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.workout_list_item, parent, false);
            return new WorkoutViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final WorkoutViewHolder holder, final int position) {
            Workout workout = workouts.get(position);
            holder.textViewItemTitle.setText(workout.getTitle());
            holder.textViewItemDescription.setText(workout.getDescription());
            Picasso
                    .get()
                    .load(workout.getImageURL())
                    .placeholder(R.drawable.ic_picture)
                    .into(holder.imageItemPicture);

            // Show menu if not favorites
            SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
            if (!preferences.getBoolean(Constants.WORKOUT_FAVORITES,false)) {
                holder.imageItemMenu.setVisibility(View.VISIBLE);
            }

            // Show record area when record exists
            if (workout.getRecordDate() != null) {
                holder.textViewItemRecordDate.setText(
                        String.valueOf(new SimpleDateFormat(Constants.WORKOUT_DATE_FORMAT, Locale.ROOT).format(workout.getRecordDate())));
                holder.textViewItemRecordAttemptCount.setText(String.valueOf(workout.getRecordAttemptCount()));
                holder.textViewItemRecordRepeatCount.setText(String.valueOf(workout.getRecordRepeatCount()));
                holder.textViewItemRecordTimer.setText(workout.getStopwatch());
                holder.horizontalSeparator.setBackgroundResource(R.color.colorAccent);
                holder.linearLayoutItemRecordArea.setVisibility(View.VISIBLE);
            }

            // Call WorkoutDetailFragment
            holder.workoutListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getFragmentManager();
                    callbackActivity.onWorkoutListItemSelected(holder.getAdapterPosition());
//                    WorkoutDetailFragment detailFragment = WorkoutDetailFragment.getInstance(holder.getAdapterPosition());
//                    fragmentManager.beginTransaction().replace(R.id.container,detailFragment).addToBackStack(null).commit();
                    }
            });

            holder.imageItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view,holder,position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return workouts.isEmpty() ? 0 : workouts.size();
        }
    }

    // Popup menu on every item of list
    private void showPopupMenu(View v, final WorkoutViewHolder holder,int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.workout_item_menu);
        Menu menu = popupMenu.getMenu();
        MenuItem favorites = menu.findItem(R.id.workout_item_menu_favorites);
        final Workout workout = workouts.get(position);
        favorites.setChecked(workout.isFavorite());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.workout_item_menu_favorites:
                                if (item.isChecked()) {
                                    item.setChecked(false);
                                    workout.setFavorite(false);

                                } else {
                                    item.setChecked(true);
                                    workout.setFavorite(true);
                                    Toast.makeText(getContext(),
                                            getString(R.string.workout_list_added_favorites),
                                            Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.workout_item_menu_delete:
                                removeAt(holder.getAdapterPosition());
                                Toast.makeText(getContext(),
                                        getString(R.string.workout_list_deleted),
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

    // Method removing the workout at position
    private void removeAt(int position) {
        workouts.remove(position);
        Log.d(TAG,"Item removed at " + position);
        workoutAdapter.notifyItemRemoved(position);
        workoutAdapter.notifyItemRangeChanged(position, workouts.size());
        if(workouts.size() == 0) showListWorkouts(root); // After deleting the last item updating the list to print the message
    }

    // Method showing list of workouts
    private void showListWorkouts(View root) {
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
        if (preferences.getBoolean(Constants.WORKOUT_FAVORITES,false)) {
            workoutAdapter = new WorkoutAdapter(WorkoutList.getInstance().getFavoritsWorkouts(), getContext());
            workouts = WorkoutList.getInstance().getFavoritsWorkouts();
        }
        else {
            workoutAdapter = new WorkoutAdapter(WorkoutList.getInstance().getWorkouts(), getContext());
            workouts = WorkoutList.getInstance().getWorkouts();
        }

        RecyclerView recyclerView = root.findViewById(R.id.workout_recycler_view);
        recyclerView.setAdapter(workoutAdapter);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (workouts.isEmpty()) {
            TextView workoutRecyclerTitle = root.findViewById(R.id.workout_recycler_title);
            workoutRecyclerTitle.setText(getString(R.string.workout_list_is_empty));
        }
    }

    private void noWorking(){
        Toast.makeText(getContext(), "Пока не работает.", Toast.LENGTH_SHORT).show();
    }


    // For testing lifecircles
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() Called");
    }

    @Override
    public void onResume() {
        super.onResume();
        showListWorkouts(root);
        Log.d(TAG,"onResume() Called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() Called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() Called");
    }

}
